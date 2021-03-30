package com.trendyol.demo.customerservice.repository;

import com.trendyol.demo.customerservice.domain.Customer;
import com.trendyol.demo.customerservice.domain.builder.CustomerBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void it_should_save_customer_and_find_by_id() {
        //given
        Date date = new Date();
        Customer customer = CustomerBuilder.aCustomer()
                .name("okan")
                .surname("yildirim")
                .birthDay(date)
                .email("okan@email.com")
                .build();
        //when
        Customer savedCustomer = customerRepository.save(customer);
        testEntityManager.flush();
        testEntityManager.clear();
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        //then

        AssertionsForClassTypes.assertThat(foundCustomer.isPresent()).isTrue();
        Customer fetchedCustomer = foundCustomer.get();
        AssertionsForClassTypes.assertThat(fetchedCustomer.getName()).isEqualTo(customer.getName());
        AssertionsForClassTypes.assertThat(fetchedCustomer.getSurname()).isEqualTo(customer.getSurname());
        AssertionsForClassTypes.assertThat(fetchedCustomer.getEmail()).isEqualTo(customer.getEmail());
        AssertionsForClassTypes.assertThat(fetchedCustomer.getId()).isEqualTo(savedCustomer.getId());
        AssertionsForClassTypes.assertThat(fetchedCustomer.getBirthday()).isEqualToIgnoringMillis(date);
    }
}