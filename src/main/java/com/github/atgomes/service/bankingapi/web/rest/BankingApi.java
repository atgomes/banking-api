package com.github.atgomes.service.bankingapi.web.rest;

import com.github.atgomes.service.bankingapi.config.ApplicationProperties;
import com.github.atgomes.service.bankingapi.domain.enumeration.BalanceType;
import com.github.atgomes.service.bankingapi.service.BalanceService;
import com.github.atgomes.service.bankingapi.service.BankAccountService;
import com.github.atgomes.service.bankingapi.service.PaymentService;
import com.github.atgomes.service.bankingapi.service.UserService;
import com.github.atgomes.service.bankingapi.service.dto.BalanceDTO;
import com.github.atgomes.service.bankingapi.service.dto.BankAccountDTO;
import com.github.atgomes.service.bankingapi.service.dto.PaymentDTO;
import com.github.atgomes.service.bankingapi.service.dto.UserDTO;
import com.github.atgomes.service.bankingapi.service.util.IbanTestResponse;
import com.github.atgomes.service.bankingapi.service.util.PaymentTentative;
import com.github.atgomes.service.bankingapi.web.rest.errors.*;
import com.github.atgomes.service.bankingapi.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BankingApi {

    private final Logger log = LoggerFactory.getLogger(BankingApi.class);

    private static final String ENTITY_NAME = "bankingApi";

    // TODO move to external resource
    private static final List<String> BLACKLISTED_ACCOUNTS = Arrays.asList("LU280019400644750000", "LU120010001234567891");

    private final UserService userService;

    private final BalanceService balanceService;

    private final BankAccountService bankAccountService;

    private final PaymentService paymentService;

    private final ApplicationProperties applicationProperties;

    public BankingApi(UserService userService, BankAccountService bankAccountService,
                      BalanceService balanceService, PaymentService paymentService, ApplicationProperties applicationProperties) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.balanceService = balanceService;
        this.paymentService = paymentService;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/bank-accounts")
    public List<BankAccountDTO> getAllBankAccounts() {
        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

        return bankAccountService.getBankAccountsByUserId(userDTO.getId());
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentDTO> makePayment(@RequestBody PaymentTentative paymentTentative) {

        // Validate giver account belongs to user
        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

        Optional<BankAccountDTO> bankAccountDTO = bankAccountService.getBankAccountsByUserId(userDTO.getId()).stream()
            .findFirst();

        if (!bankAccountDTO.isPresent()) {
            throw new InvalidOriginAccountException("Unauthorized account", ENTITY_NAME, "unauth_account");
        }

        // If there is an issue connecting to remote API, accepts the IBAN as valid
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<IbanTestResponse> response
                = restTemplate.getForEntity(applicationProperties.getIbanTestApiUrl() +
                paymentTentative.getBeneficiaryAccountNumber() + "?authcode=" +
                applicationProperties.getIbanTestApiKey(), IbanTestResponse.class);

            if (response.getBody().getCode().compareTo(2999) > 0) {
                throw new InvalidIbanException("Invalid IBAN", ENTITY_NAME, "invalid_iban");
            }
        } catch (RestClientException ex) {
            log.info(ex.getMessage());
        }

        // Validate not the same as giver
        if (paymentTentative.getBeneficiaryAccountNumber().equals(paymentTentative.getGiverAccount())) {
            throw new InvalidDestinationAccountException("Same account", ENTITY_NAME, "same_account");
        }

        // Validate IBAN not blacklisted
        if (BLACKLISTED_ACCOUNTS.contains(paymentTentative.getBeneficiaryAccountNumber())) {
            throw new InvalidDestinationAccountException("Blacklisted Account", ENTITY_NAME, "blackl_account");
        }

        // Validate available amount
        // TODO instead of find all, find by account
        Optional<BalanceDTO> balanceDTO = balanceService.findAll().stream()
            .filter(b->b.getCurrency().equalsIgnoreCase(paymentTentative.getCurrency()))
            .filter(b->bankAccountDTO.get().getId().equals(b.getBankAccountId()))
            .findAny();

        if (!balanceDTO.isPresent()) {
            throw new InternalServerErrorException("Balance could not be found");
        }

        // payment amount <= account amount
        if (!balanceDTO.get().getCurrency().equalsIgnoreCase(paymentTentative.getCurrency()) ||
            balanceDTO.get().getAmount().compareTo(paymentTentative.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds", ENTITY_NAME, "no_funds");
        }

        // Save payment
        PaymentDTO paymentDTO = new PaymentDTO().amount(paymentTentative.getAmount())
            .currency(paymentTentative.getCurrency())
            .beneficiaryAccountNumber(paymentTentative.getBeneficiaryAccountNumber())
            .beneficiaryName(paymentTentative.getBeneficiaryName())
            .communication(paymentTentative.getCommunication())
            .creationDate(Instant.now())
            .status(PaymentDTO.EXECUTED)
            .giverAccountId(bankAccountDTO.get().getId());

        PaymentDTO resultPayment = paymentService.save(paymentDTO);

        // Update sender bank account amount
        balanceDTO.get().setAmount(balanceDTO.get().getAmount().subtract(paymentTentative.getAmount()));
        BalanceDTO resultBalance = balanceService.save(balanceDTO.get());
        // TODO validate result

        // Update receiver bank account amount
        // TODO directly find account by number instead of findAll
        List<BankAccountDTO> allBankAccounts = bankAccountService.findAll();

        Optional<BankAccountDTO> beneficiaryAccount = allBankAccounts.stream()
            .filter(b->b.getAccountNumber().equals(paymentTentative.getBeneficiaryAccountNumber()))
            .findFirst();

        // TODO find balance by account instead of findAll
        if (beneficiaryAccount.isPresent()) {

            Optional<BalanceDTO> beneficiaryBalance = balanceService.findAll().stream()
                .filter(b->b.getCurrency().equalsIgnoreCase(paymentTentative.getCurrency()))
                .filter(b->beneficiaryAccount.get().getId().equals(b.getBankAccountId()))
                .findAny();

            if (beneficiaryBalance.isPresent()) {
                beneficiaryBalance.get().setAmount(beneficiaryBalance.get().getAmount().add(paymentTentative.getAmount()));

                BalanceDTO resultBeneficiaryBalance = balanceService.save(beneficiaryBalance.get());
                // TODO validate result
            } else {
                // Create new balance if one does not exist for this currency
                BalanceDTO newBalanceDTO = new BalanceDTO();
                newBalanceDTO.setAmount(paymentTentative.getAmount());
                newBalanceDTO.setCurrency(paymentTentative.getCurrency());
                newBalanceDTO.setType(BalanceType.END_OF_DAY);
                newBalanceDTO.setBankAccountId(beneficiaryAccount.get().getId());

                BalanceDTO resultBeneficiaryBalance = balanceService.save(newBalanceDTO);
                // TODO validate result
            }
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resultPayment.getId().toString()))
            .body(resultPayment);

    }

    @GetMapping("/payments")
    public List<PaymentDTO> getAllPayments(Pageable pageable) {
        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

        return paymentService.findAllByUserId(pageable, userDTO.getId());
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

        Optional<PaymentDTO> paymentDTO = paymentService.findOne(id)
            .filter(p->p.getGiverAccountId().equals(userDTO.getId()));

        paymentDTO.ifPresent(p->paymentService.delete(id));

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
