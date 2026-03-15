package com.example.lab2.Service;
//бизнес-логика + кэширование

import com.example.lab2.Model.Customer;
import com.example.lab2.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Cacheable(value = "allCustomers")
    public List<Customer> getAllCustomers() {
        System.out.println(">>> HIT DATABASE: Fetching all customers...");
        return customerRepository.findAll();
    }

    @Cacheable(value = "customers", key = "#id")
    public Optional<Customer> getCustomerById(Long id) {
        System.out.println("ЗАПРОС В БАЗУ ДАННЫХ (КЭШ НЕ СРАБОТАЛ)");
        return customerRepository.findById(id);
    }

    @CacheEvict(value = {"customers", "allCustomers"}, allEntries = true)
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @CacheEvict(value = {"customers", "allCustomers"}, allEntries = true)
    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Обновляем поля
                    if (customerDetails.getFirstName() != null && !customerDetails.getFirstName().isBlank()) {
                        existingCustomer.setFirstName(customerDetails.getFirstName());
                    }
                    if (customerDetails.getLastName() != null && !customerDetails.getLastName().isBlank()) {
                        existingCustomer.setLastName(customerDetails.getLastName());
                    }
                    if (customerDetails.getEmail() != null && !customerDetails.getEmail().isBlank()) {
                        existingCustomer.setEmail(customerDetails.getEmail());
                    }
                    return customerRepository.save(existingCustomer);
                });
    }

    @CacheEvict(value = {"customers", "allCustomers"}, allEntries = true)
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}