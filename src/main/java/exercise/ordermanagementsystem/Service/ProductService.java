package exercise.ordermanagementsystem.Service;
import exercise.ordermanagementsystem.Dao.ProductRepository;
import exercise.ordermanagementsystem.Dto.ProductDTO;
import exercise.ordermanagementsystem.Entities.Product;
import exercise.ordermanagementsystem.Exception.OrderDeletionException;
import exercise.ordermanagementsystem.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .price((productDTO.getPrice()))
                .stock(productDTO.getStock())
                .build();
        return productRepository.save(product);
    }
    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        return  productRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("product not found"));
    }
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Integer id,ProductDTO productDTO) {
        Product product = productRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("product not found"));
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (!product.getOrderItems().isEmpty()) {
            throw new OrderDeletionException("Cannot delete product, it has existing orders");
        }

        productRepository.delete(product);
    }
}
