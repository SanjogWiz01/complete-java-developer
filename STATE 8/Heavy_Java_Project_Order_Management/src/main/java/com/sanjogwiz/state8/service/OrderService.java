package com.sanjogwiz.state8.service;

import com.sanjogwiz.state8.domain.Customer;
import com.sanjogwiz.state8.domain.OrderItem;
import com.sanjogwiz.state8.domain.OrderStatus;
import com.sanjogwiz.state8.domain.Product;
import com.sanjogwiz.state8.domain.PurchaseOrder;
import com.sanjogwiz.state8.dto.CreateOrderItemRequest;
import com.sanjogwiz.state8.dto.CreateOrderRequest;
import com.sanjogwiz.state8.dto.OrderResponse;
import com.sanjogwiz.state8.exception.InsufficientStockException;
import com.sanjogwiz.state8.exception.ResourceNotFoundException;
import com.sanjogwiz.state8.repository.ProductRepository;
import com.sanjogwiz.state8.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public OrderService(CustomerService customerService,
                        ProductRepository productRepository,
                        PurchaseOrderRepository purchaseOrderRepository) {
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Customer customer = customerService.getById(request.customerId());

        PurchaseOrder order = new PurchaseOrder();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CONFIRMED);

        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found for id " + itemRequest.productId()));

            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product " + product.getName()
                                + ". Available=" + product.getStockQuantity()
                                + ", requested=" + itemRequest.quantity());
            }

            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getUnitPrice());
            item.setLineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            order.addItem(item);

            total = total.add(item.getLineTotal());
        }

        order.setTotalAmount(total);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);
        return toResponse(savedOrder);
    }

    public OrderResponse getById(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id " + orderId));
        return toResponse(order);
    }

    public List<OrderResponse> getAll() {
        return purchaseOrderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(PurchaseOrder order) {
        List<OrderResponse.OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getFullName(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemResponses
        );
    }
}

