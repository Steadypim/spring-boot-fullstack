package dev.steadypim.spapi.customer;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import dev.steadypim.spapi.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        Faker faker = new Faker();
        Name name = faker.name();
        String firstname = name.firstName();
        String lastname = name.lastName();
        String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@steadypim.com";
        Customer customer = new Customer(
                firstname + " " + lastname,
                email,
                20
        );

        underTest.save(customer);

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailWhenEmailNotPresent() {
        Faker faker = new Faker();
        Name name = faker.name();
        String firstname = name.firstName();
        String lastname = name.lastName();
        String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@steadypim.com";


        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        Faker faker = new Faker();
        Name name = faker.name();
        String firstname = name.firstName();
        String lastname = name.lastName();
        String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@steadypim.com";
        Customer customer = new Customer(
                firstname + " " + lastname,
                email,
                20
        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWhenIdNotPresent() {
        int id = -1;

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }
}