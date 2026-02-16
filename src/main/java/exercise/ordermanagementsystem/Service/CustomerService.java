package exercise.ordermanagementsystem.Service;

import exercise.ordermanagementsystem.Dao.CustomerRepository;
import exercise.ordermanagementsystem.Dto.CustomerDTO;
import exercise.ordermanagementsystem.Entities.Customer;
import exercise.ordermanagementsystem.Exception.DuplicateResourceException;
import exercise.ordermanagementsystem.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerDTO) {
        if(customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (customerRepository.existsByPhoneNumber(String.valueOf(customerDTO.getPhoneNumber()))) {
            throw new DuplicateResourceException("Phone number already exists");
        }
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .build();
        return customerRepository.save(customer);
    }
    @Transactional(readOnly = true)
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("customer not found with the given Id"));
    }
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Integer id,CustomerDTO customerDTO) {
        Customer updatedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with given id"));
        updatedCustomer.setName(customerDTO.getName());
        updatedCustomer.setEmail(customerDTO.getEmail());
        updatedCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        updatedCustomer.setAddress(customerDTO.getAddress());
        return customerRepository.save(updatedCustomer);
    }

    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with given id"));
        customerRepository.delete(customer);
    }
}
