package com.example.ec.service;

import com.example.ec.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    public void signup() {
        Optional<User> user = userService.signup("dummyUsername", "dummyPassword", "John", "Doe");
        assertThat(user.get().getPassword(), not("dummyPassword"));
        System.out.println("Encoded Password = " + user.get().getPassword());
    }
}
