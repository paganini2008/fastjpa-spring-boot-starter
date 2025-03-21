package com.github.fastjpa.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.fastjpa.Restrictions;
import com.github.fastjpa.example.dao.OrderDao;
import com.github.fastjpa.example.dao.OrderProductDao;
import com.github.fastjpa.example.dao.ProductDao;
import com.github.fastjpa.example.dao.UserDao;
import com.github.fastjpa.example.entity.Order;
import com.github.fastjpa.example.entity.OrderProduct;
import com.github.fastjpa.example.entity.Product;
import com.github.fastjpa.example.entity.User;

/**
 * 
 * @Description: UserOrderService
 * @Author: Fred Feng
 * @Date: 21/03/2025
 * @Version 1.0.0
 */
@Transactional
@Service
public class UserOrderService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    public void makeOrder(Long userId, Map<String, Integer> items) {
        User user = userDao.getReferenceById(userId);
        List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemName = entry.getKey();
            Product product = productDao.findOne(Restrictions.eq(Product::getName, itemName)).get();
            totalPrice =
                    totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()))
                            .multiply(product.getDiscount() != null ? product.getDiscount()
                                    : BigDecimal.ONE));
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setAmount(entry.getValue());
            orderProduct.setProduct(product);
            orderProducts.add(orderProduct);
        }
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(totalPrice);
        order.setUser(user);
        Order ref = orderDao.save(order);

        orderProducts.forEach(op -> op.setOrder(ref));
        orderProductDao.saveAll(orderProducts);
    }

}
