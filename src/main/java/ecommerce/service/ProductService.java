package ecommerce.service;

import ecommerce.entity.Product;
import ecommerce.exception.ProductDuplicateException;
import ecommerce.exception.ProductNotFoundException;
import ecommerce.exception.ProductOutOfStockException;
import ecommerce.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* ================= ADMIN FEATURES ================= */

    // ➕ Admin adds product
    public Product addProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ProductDuplicateException(
                    "Product already exists with name: " + product.getName()
            );
        }
        return productRepository.save(product);
    }

    // ✏️ Admin updates product
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product product = getProductById(productId);

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());

        return productRepository.save(product);
    }

    // ❌ Admin deletes product
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    /* ================= USER FEATURES ================= */

    // 📃 List all products (ADMIN + CUSTOMER)
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 🔍 Get product by ID (ADMIN + CUSTOMER)
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId)
                );
    }

    // 📦 Check stock availability
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public boolean isProductAvailable(Long productId, int requiredQty) {
        Product product = getProductById(productId);
        return product.getStock() >= requiredQty;
    }

    /* ================= ORDER FLOW ================= */

    // 🔥 Reduce stock when order is placed
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void reduceProductStock(Long productId, int orderedQty) {
        Product product = getProductById(productId);

        if (product.getStock() < orderedQty) {
            throw new ProductOutOfStockException(
                    "Only " + product.getStock() + " items left for product: "
                            + product.getName()
            );
        }

        product.setStock(product.getStock() - orderedQty);
        productRepository.save(product);
    }
}
