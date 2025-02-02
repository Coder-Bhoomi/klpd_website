package com.klpdapp.klpd.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepository;

    @Autowired
    ProductRepo pRepo;

    @Autowired
    UserRepo uRepo;

    @Autowired
    OrderRepository orderrepo;

    @Autowired
    OrderItemRepository orderitemrepo;

    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

    public float calculateSubtotal(List<Cart> cartItems) {
        return cartItems.stream().map(item -> item.getProductTotal()).reduce(0.0f, Float::sum);
    }

    public float calculateTax(float subtotal) {
        return subtotal * 0.10f;
    }

    public int calculateTotal(float subtotal, float tax, float discount) {
        return (int) (subtotal + tax - discount);
    }

    public void updateCartItem(Cart cartItem, Integer quantity) {
        Product product = cartItem.getProduct();
        cartItem.setQuantity(quantity);
        cartItem.setProductTotal(quantity * (product.getOfferPrice() != null ? product.getOfferPrice() : product.getMrp()));
        cartRepository.save(cartItem);
    }

    public void deleteCartItem(int cartId) {
        cartRepository.deleteById(cartId);
    }

    public void checkout(User user) {
        List<Cart> carts = cartRepository.findByUser(user);
        float subtotal = calculateSubtotal(carts);
        float discount = 0.0f;
        float tax = calculateTax(subtotal);
        int total = calculateTotal(subtotal, tax, discount);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmt(total);
        order.setOrderDate(LocalDate.now());
        orderrepo.save(order);

        for (Cart cart : carts) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProdQuantity(cart.getQuantity());
            orderItem.setProduct(cart.getProduct());
            orderitemrepo.save(orderItem);
        }

        cartRepository.deleteByUser(user);
    }

    public void addToCart(User user, Integer productId, Integer quantity) {
        Product product = pRepo.findById(productId).orElse(null);
        if (product != null) {
            Cart existingCartItem = cartRepository.findByProductAndUser(product, user).orElse(null);
            if (existingCartItem != null) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                float price = (product.getOfferPrice() != null) ? product.getOfferPrice() : product.getMrp();
                existingCartItem.setProductTotal(price * existingCartItem.getQuantity());
                cartRepository.save(existingCartItem);
            } else {
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setProduct(product);
                cart.setQuantity(quantity);
                float price = (product.getOfferPrice() != null) ? product.getOfferPrice() : product.getMrp();
                cart.setProductTotal(price * quantity);
                cartRepository.save(cart);
            }
        }
    }
}

