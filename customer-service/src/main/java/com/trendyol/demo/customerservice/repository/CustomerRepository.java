package com.trendyol.demo.customerservice.repository;


import com.trendyol.demo.customerservice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
