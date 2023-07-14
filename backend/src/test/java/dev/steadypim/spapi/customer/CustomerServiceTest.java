package dev.steadypim.spapi.customer;

import dev.steadypim.spapi.exception.DuplicateResourseException;
import dev.steadypim.spapi.exception.RequestValidationException;
import dev.steadypim.spapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {

        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {

        int id = 10;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id = [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {

        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19
        );

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {

        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19
        );

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourseException.class)
                .hasMessage("email already taken");


        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        int id = 10;

        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowDeleteCustomerByIdNotExists() {
        int id = 10;

        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", newEmail, 23);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());

    }

    @Test
    void canUpdateOnlyCustomerName() {
        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", null, null);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));


        String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);

    }

    @Test
    void canUpdateOnlyCustomerAge() {
        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 22);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }


    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourseException.class)
                .hasMessage("email already taken");

        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        int id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        verify(customerDao, never()).updateCustomer(any());
    }
}