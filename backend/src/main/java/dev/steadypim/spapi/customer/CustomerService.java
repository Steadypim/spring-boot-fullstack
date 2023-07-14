package dev.steadypim.spapi.customer;


import dev.steadypim.spapi.exception.DuplicateResourseException;
import dev.steadypim.spapi.exception.RequestValidationException;
import dev.steadypim.spapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
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
                customerRegistrationRequest.age()
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

        Customer customer = getCustomer(customerId);

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
