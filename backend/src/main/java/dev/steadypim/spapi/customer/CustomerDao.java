package dev.steadypim.spapi.customer;


import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();

    Optional<Customer> selectCustomerById(Integer id);

    void insertCustomer(Customer customer);

    void deleteCustomerById(Integer id);

    boolean existsPersonWithEmail(String email);

    boolean existsPersonWithId(Integer id);

    void updateCustomer(Customer update);

    Optional<Customer> selectUserByEmail(String email);
}
