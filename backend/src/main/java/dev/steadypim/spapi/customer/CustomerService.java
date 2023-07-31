package dev.steadypim.spapi.customer;


import dev.steadypim.spapi.exception.DuplicateResourseException;
import dev.steadypim.spapi.exception.RequestValidationException;
import dev.steadypim.spapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao, PasswordEncoder passwordEncoder, CustomerDTOMapper customerDTOMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }


    public List<CustomerDTO> getAllCustomers() {
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer with id = [%s] not found".formatted(id)
                        ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)) {
            throw new DuplicateResourseException("email already taken");
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );

        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer id){
        if(!customerDao.existsPersonWithId(id)){
            throw new ResourceNotFoundException(
                    "customer with id [%s] not found".formatted(id)
            );
        }

        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest request) {

        Customer customer = customerDao.selectCustomerById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer with id = [%s] not found".formatted(customerId)
                        ));

        boolean changes = false;

        if(request.name() != null && !request.name().equals(customer.getName())){
            customer.setName(request.name());
            changes = true;
        }

        if(request.email() != null && !request.email().equals(customer.getEmail())){
            if (customerDao.existsPersonWithEmail(request.email())) {
                throw new DuplicateResourseException("email already taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }

        if(request.age() != null && !request.age().equals(customer.getAge())){
            customer.setAge(request.age());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);
    }
}
