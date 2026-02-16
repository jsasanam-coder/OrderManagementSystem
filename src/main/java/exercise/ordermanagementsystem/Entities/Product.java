package exercise.ordermanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_tbl")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Integer id;
    @Column(name = "p_name", nullable = false, length = 15)
    private String name;
    @Column(name = "p_price", nullable = false)
    private Double price;
    @Column(name ="p_stock", nullable = false)
    private Integer stock;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<OrderItem> orderItems=new ArrayList<>();
}
