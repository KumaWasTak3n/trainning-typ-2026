package com.quannet.demo.controller;

import com.quannet.demo.dto.OrderRequest;
import com.quannet.demo.model.Order;
import com.quannet.demo.repository.OrderRepository;
import com.quannet.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders")

public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request){
        try {
            Order order = orderService.placeOrder(request);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("user/{user_id}")
    public ResponseEntity<?> findByUserId(@PathVariable("user_id") Long id){
        List<Order> list = orderRepository.findByUserId(id);
        if(!list.isEmpty()) return new ResponseEntity<>(list, HttpStatus.OK);
        else return new ResponseEntity<>("Id not found!!!", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isPresent()) return new ResponseEntity<>(optionalOrder, HttpStatus.OK);
        else return new ResponseEntity<>("Id not found", HttpStatus.BAD_REQUEST);
    }

}
