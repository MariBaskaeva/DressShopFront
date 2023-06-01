package ru.baskaeva.dressshopfront.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.baskaeva.dressshopfront.dto.*;

import java.util.List;

@Controller
public class OrderController {
    final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/payment")
    public String orderForm(@AuthenticationPrincipal String token, OrderDTO orderDTO) {
        if (token.equals("anonymousUser"))
            return "redirect:/login";

        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));

        ResponseEntity<Order> response = restTemplate.postForEntity("http://localhost:9090/order", orderDTO, Order.class);

        if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
            return "redirect:/bag";

        return "redirect:/payment?order=" + response.getBody().getId();
    }

    @GetMapping("/payment")
    public String getPayment(@RequestParam Long order, Model model) {
        model.addAttribute("orderId", order);

        return "payment";
    }

    @PostMapping("/pay")
    public String pay(@RequestBody PaymentDTO paymentDTO) {
        ResponseEntity<Order> response = restTemplate.patchForObject("http://localhost:9090/order/{id}?status={status}", HttpEntity.EMPTY, Order.class, paymentDTO.getOrderId(), Status.InProgress);

        return "redirect:/";
    }

}
