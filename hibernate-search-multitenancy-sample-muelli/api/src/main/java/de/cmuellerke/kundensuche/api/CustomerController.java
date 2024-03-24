package de.cmuellerke.kundensuche.api;

import de.cmuellerke.kundensuche.api.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customer")
@Import(FeignClientsConfiguration.class)
public interface CustomerController {
    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable String id);

    @PutMapping("/{id}")
    public CustomerDTO replaceCustomer(@PathVariable String id, CustomerDTO customer);

    @PostMapping("/new")
    public CustomerDTO createCustomer(CustomerDTO customer);

    @PostMapping("/new/bulk")
    public List<CustomerDTO> createCustomers(List<CustomerDTO> customers);

    @PatchMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable String id, CustomerDTO customer);


    }
