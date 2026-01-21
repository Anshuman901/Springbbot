package ecommerce.exception;

public class ProductDuplicateException extends RuntimeException {

    public ProductDuplicateException(String message) {
        super(message);
    }
}
