package com.trendyol.demo.customerservice.service;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.trendyol.demo.customerservice.domain.Customer;
import com.trendyol.demo.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DummyCustomerCreator {

    private final Logger logger = LoggerFactory.getLogger(DummyCustomerCreator.class);

    private final CustomerRepository customerRepository;

    public DummyCustomerCreator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

   @Scheduled(fixedDelay = 5000)
    public void createRandomCustomer() {
        Customer randomCustomer = getRandomCustomer();
        customerRepository.save(randomCustomer);

        logger.info("Random Customer is inserted successfully with id {}", randomCustomer);
    }

    private Customer getRandomCustomer() {
        Faker faker = Faker.instance();
        Name randomName = faker.name();


        String name = randomName.firstName();
        String lastName = randomName.lastName();
        String email = new StringBuilder()
                .append(name.toLowerCase())
                .append(".")
                .append(lastName.toLowerCase())
                .append("@")
                .append("email.com")
                .toString();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(lastName);
        customer.setEmail(email);
        customer.setBirthday(faker.date().birthday());
        customer.setActive(false);

        return customer;
    }
}
