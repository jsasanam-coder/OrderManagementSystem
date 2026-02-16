package exercise.ordermanagementsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "name must not be empty")
    private String name;
    @PositiveOrZero(message = "Price always from Zero or positive")
    private Double price;
    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;
}
