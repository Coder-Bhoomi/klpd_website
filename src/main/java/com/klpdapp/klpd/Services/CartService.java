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
import com.klpdapp.klpd.Repository.AddressRepo;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Address;
import com.klpdapp.klpd.model.Coupon;

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

    @Autowired
    ProductService productService;

    @Autowired
    AddressRepo addRepo;

    public List<Cart> getCartItems(Login user) {
        return cartRepository.findByUser(user);
    }

    public float calculateSubtotal(List<Cart> cartItems) {
        return cartItems.stream().map(item -> item.getProductTotal()).reduce(0.0f, Float::sum);
    }

    

    public int calculateTotal(float subtotal, float discount) {
        return (int) (subtotal  - discount);
    }


    public void deleteCartItem(int cartId) {
        cartRepository.deleteById(cartId);
    }

    public void checkout(Login user, String paymentMode, int AddressId,float discount, Coupon coupon) {
        List<Cart> carts = cartRepository.findByUser(user);
        float subtotal = calculateSubtotal(carts);
       
        int total = calculateTotal(subtotal, discount);
        Address address = addRepo.findById(AddressId).orElse(null);
        float discountpercentage = (discount / subtotal) *100;
        System.out.println("coupon"+coupon);
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmt(total);
        order.setOrderDate(LocalDate.now());
        order.setPaymentMode(paymentMode);
        order.setAddress(address);
        if (coupon != null) {
            System.out.println("coupon is not null");
            order.setCoupon(coupon);
        }
        orderrepo.save(order);
        for (Cart cart : carts) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProdQuantity(cart.getQuantity());
            orderItem.setProduct(cart.getProduct());
            orderItem.setStatus("Pending");
            orderItem.setPrice(cart.getProductTotal() - (cart.getProductTotal() * discountpercentage / 100));
            orderItem.setDeliveryDate(LocalDate.now().plusDays(5)); // Example: 5 days later
            orderitemrepo.save(orderItem);

            
            Product product = productService.findById(cart.getProduct().getPid());
            productService.incrementSales(product.getPid());
        }

        cartRepository.deleteByUser(user);
    }   
}   