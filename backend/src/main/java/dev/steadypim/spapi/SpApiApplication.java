package dev.steadypim.spapi;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import dev.steadypim.spapi.customer.Customer;
import dev.steadypim.spapi.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpApiApplication.class, args);

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            Faker faker = new Faker();
            Name name = faker.name();
            String firstname = name.firstName();
            String lastname = name.lastName();
            Customer customer = new Customer(
                    firstname + " " + lastname,
                    firstname.toLowerCase() + "." + lastname.toLowerCase() + "@steadypim.com",
                    faker.random().nextInt(16, 99)
            );
            customerRepository.save(customer);
        };
    }

}
