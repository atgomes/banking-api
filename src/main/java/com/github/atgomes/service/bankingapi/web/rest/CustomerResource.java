package com.github.atgomes.service.bankingapi.web.rest;
import com.github.atgomes.service.bankingapi.service.CustomerService;
import com.github.atgomes.service.bankingapi.service.UserService;
import com.github.atgomes.service.bankingapi.service.dto.UserDTO;
import com.github.atgomes.service.bankingapi.service.util.CustomerUpdate;
import com.github.atgomes.service.bankingapi.web.rest.errors.InternalServerErrorException;
import com.github.atgomes.service.bankingapi.web.rest.errors.InvalidPasswordException;
import com.github.atgomes.service.bankingapi.web.rest.util.HeaderUtil;
import com.github.atgomes.service.bankingapi.service.dto.CustomerDTO;
import com.github.atgomes.service.bankingapi.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

import java.util.Optional;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "bankingApiCustomer";

    private final CustomerService customerService;

    private final UserService userService;

    public CustomerResource(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    /**
     * PUT /api/customers
     * Using the current user, updates the customer.
     *
     * @param customerUpdate contains address and current/new passwords.
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/customers")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerUpdate customerUpdate) throws URISyntaxException {

        UserDTO userDTO = userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

        Optional<CustomerDTO> customerDTO = customerService.findCustomerByUserId(userDTO.getId());

        if (!customerDTO.isPresent()) {
            throw new InternalServerErrorException("Customer could not be found");
        }

        customerDTO.get().setAddress(customerUpdate.getAddress());

        if (!checkPasswordLength(customerUpdate.getNewPassword())) {
            throw new InvalidPasswordException();
        }

        // Save address
        CustomerDTO result = customerService.save(customerDTO.get());

        // Save password
        userService.changePassword(customerUpdate.getCurrentPassword(), customerUpdate.getNewPassword());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerDTO.get().getId().toString()))
            .body(result);
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
