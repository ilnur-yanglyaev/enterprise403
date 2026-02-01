package com.example.lab1.Repository;

import com.example.lab1.Model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {
}
