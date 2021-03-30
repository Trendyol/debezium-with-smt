package com.trendyol.demo.userconsumer.repository;

import com.trendyol.demo.userconsumer.domain.User;
import com.trendyol.demo.userconsumer.domain.builder.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void it_should_save_customer_and_find_by_id() {
        //given
        Date date = new Date();
        User user = UserBuilder.anUser()
                .id(4074L)
                .name("okan")
                .surname("yildirim")
                .birthDay(date)
                .email("okan@email.com")
                .build();
        //when
        userRepository.save(user);
        testEntityManager.flush();
        testEntityManager.clear();
        Optional<User> optionalUser = userRepository.findById(4074L);
        //then

        assertThat(optionalUser.isPresent()).isTrue();
        User fetchedUser = optionalUser.get();
        assertThat(fetchedUser.getName()).isEqualTo(user.getName());
        assertThat(fetchedUser.getSurname()).isEqualTo(user.getSurname());
        assertThat(fetchedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(fetchedUser.getId()).isEqualTo(4074L);
        assertThat(fetchedUser.getBirthday()).isEqualToIgnoringMillis(date);
    }
}