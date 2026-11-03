package com.quannet.demo.service;

import com.quannet.demo.dto.OrderItemRequest;
import com.quannet.demo.dto.OrderRequest;
import com.quannet.demo.model.*;
import com.quannet.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ComputerRepository computerRepository;

    @Transactional
    @Override
    public Order placeOrder(OrderRequest request){
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if(optionalUser.isEmpty()){
            throw new RuntimeException("Account Not Found!!!");
        }
        User user = optionalUser.get();
        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new RuntimeException("No item selected!!!");
        }
        double TotalPrice = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequest itemRequest : request.getItems()){
            Optional<Food> optionalFood = foodRepository.findByName(itemRequest.getName());
            if(optionalFood.isEmpty()){
                throw new RuntimeException(itemRequest.getName() + " not available!!!");
            }
            Food food = optionalFood.get();
            if(itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0){
                throw new RuntimeException("Quantity need to greater than 0");
            }
            if(food.getQuantity() < itemRequest.getQuantity()){
                throw new RuntimeException(food.getName() + " only have " + food.getQuantity() + "Quantity left!!");
            }
            food.setQuantity(food.getQuantity() - itemRequest.getQuantity());
            foodRepository.save(food);
            TotalPrice += food.getCost() * itemRequest.getQuantity();
            OrderItem orderItem = OrderItem.builder()
                    .food(food)
                    .name(food.getName())
                    .quantity(itemRequest.getQuantity())
                    .price(food.getCost())
                    .totalPrice(food.getCost() * itemRequest.getQuantity())
                    .build();
            orderItems.add(orderItem);
        }


        if(user.getBalance() < TotalPrice){
            throw new RuntimeException("Account balance not enough too buy!!, current balance: " + user.getBalance()
                                        + "Order total: " + TotalPrice);
        }
        user.setBalance(user.getBalance() - TotalPrice);
        userRepository.save(user);

        Optional<Computer> optionalComputer = computerRepository.findById(request.getComputerId());
        if(optionalComputer.isEmpty()){
            throw new RuntimeException("Computer id " + request.getComputerId() + " not found!!!");
        }
        Computer computer = optionalComputer.get();
        Order order = Order.builder()
                .userId(user.getId())
                .computerId(computer.getId())
                .fullCost(TotalPrice)
                .status("COMPLETED")
                .items(orderItems)
                .build();

        for(OrderItem item : orderItems){
            item.setOrder(order);
        }

        return orderRepository.save(order);

    }
}
