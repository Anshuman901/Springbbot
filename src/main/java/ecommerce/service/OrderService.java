package ecommerce.service;

import ecommerce.entity.Order;
import ecommerce.entity.OrderItem;
import ecommerce.entity.Product;
import ecommerce.entity.User;
import ecommerce.exception.ProductOutOfStockException;
import ecommerce.repository.OrderRepository;
import ecommerce.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    /**
     * PLACE ORDER FLOW:
     * 1️⃣ Check stock for all products
     * 2️⃣ If any stock insufficient → FAIL
     * 3️⃣ Save order
     * 4️⃣ Reduce stock
     */
    @Transactional
    public Order placeOrder(User user, List<OrderItem> orderItems) {

        // 1️⃣ STOCK CHECK (NO UPDATE YET)
        for (OrderItem item : orderItems) {
            Product product = productService.getProductById(
                    item.getProduct().getId()
            );

            if (product.getStock() < item.getQuantity()) {
                throw new ProductOutOfStockException(
                        "Product '" + product.getName() +
                                "' has only " + product.getStock() +
                                " items left"
                );
            }
        }

        // 2️⃣ CREATE ORDER
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        Order savedOrder = orderRepository.save(order);

        // 3️⃣ SAVE ORDER ITEMS + REDUCE STOCK
        for (OrderItem item : orderItems) {

            Product product = productService.getProductById(
                    item.getProduct().getId()
            );

            item.setOrder(savedOrder);
            item.setPrice(product.getPrice());

            orderItemRepository.save(item);

            // 🔥 Reduce stock
            productService.reduceProductStock(
                    product.getId(),
                    item.getQuantity()
            );
        }

        return savedOrder;
    }
}
