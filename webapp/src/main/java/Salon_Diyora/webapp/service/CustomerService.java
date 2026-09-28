package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // The constructor should only initialize the repository
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}