package exercise.ordermanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer_tbl")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Integer id;
    @Column(name = "c_name", nullable = false, length = 15)
    private String name;
    @Column(name = "c_email", nullable = false, length = 25, unique = true)
    private String email;
    @Column(name ="c_phoneNumber", nullable = false, length = 10, unique = true)
    private String phoneNumber;
    @Column(name = "c_address", nullable = false, length = 50)
    private String address;
    @OneToMany( mappedBy = "customer",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders=new ArrayList<>();

}
