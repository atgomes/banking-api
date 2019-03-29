package com.github.atgomes.service.bankingapi.web.rest;
import com.github.atgomes.service.bankingapi.service.CustomerService;
import com.github.atgomes.service.bankingapi.service.UserService;
import com.github.atgomes.service.bankingapi.service.dto.UserDTO;
import com.github.atgomes.service.bankingapi.service.util.CustomerUpdate;
import com.github.atgomes.service.bankingapi.web.rest.errors.BadRequestAlertException;
import com.github.atgomes.service.bankingapi.web.rest.errors.InternalServerErrorException;
import com.github.atgomes.service.bankingapi.web.rest.errors.InvalidPasswordException;
import com.github.atgomes.service.bankingapi.web.rest.util.HeaderUtil;
import com.github.atgomes.service.bankingapi.service.dto.CustomerDTO;
import com.github.atgomes.service.bankingapi.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
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
     * POST  /customers : Create a new customer.
     *
     * @param customerDTO the customerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerDTO, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customers : Updates an existing customer.
     *
     * @param customerDTO the customerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerDTO,
     * or with status 400 (Bad Request) if the customerDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
/*    @PutMapping("/customers")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerDTO.getId().toString()))
            .body(result);
    }*/

    /**
     * Using the current user, update the customer
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

    /**
     * GET  /customers : get all the customers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomers() {
        log.debug("REST request to get all Customers");
        return customerService.findAll();
    }

    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    /**
     * DELETE  /customers/:id : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
