package exercise.ordermanagementsystem.Dao;

import exercise.ordermanagementsystem.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomer_Id(Integer customerId);

    // for getOrderWithDetails
    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.id = :id")
    Optional<Order> findOrderWithDetails(@Param("id") Integer id);

    // for getCustomerOrders
    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.customer.id = :customerId")
    List<Order> findCustomerOrdersWithDetails(@Param("customerId") Integer customerId);
}
