package ru.baskaeva.dressshopfront.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import ru.baskaeva.dressshopfront.dto.BagDTO;
import ru.baskaeva.dressshopfront.dto.BagProductDTO;
import ru.baskaeva.dressshopfront.dto.BagTotalDTO;
import ru.baskaeva.dressshopfront.dto.IdDTO;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class BagController {
    final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/bag")
    public String bagPage(Model model, @AuthenticationPrincipal String token) {
        if(token.equals("anonymousUser"))
            return "redirect:/login";
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));
        ResponseEntity<BagProductDTO[]> response = restTemplate.getForEntity("http://localhost:9090/bag", BagProductDTO[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            model.addAttribute("products", response.getBody());
            model.addAttribute("total", new BagTotalDTO(
                    Arrays.stream(response.getBody()).mapToInt(BagProductDTO::getCountInBag).sum(),
                    Arrays.stream(response.getBody()).mapToLong(p -> p.getCountInBag() * p.getPrice()).sum(),
                    250,
                    Arrays.stream(response.getBody()).mapToLong(p -> p.getCountInBag() * p.getPrice()).sum() + 250
            ));
        }
        return "bag";
    }

    @PostMapping("/increaseCount")
    public String increaseCount(IdDTO id, @AuthenticationPrincipal String token) {
        if(token.equals("anonymousUser"))
            return "redirect:/login";
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));

        log.info("{}", id.getId());
        restTemplate.postForEntity("http://localhost:9090/bag/" + id.getId(), null, Object.class);
        return "redirect:/bag";
    }

    @PostMapping("/decreaseCount")
    public String decreaseCount(IdDTO id, @AuthenticationPrincipal String token) {
        if(token.equals("anonymousUser"))
            return "redirect:/login";
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));

        log.info("{}", id.getId());
        restTemplate.delete("http://localhost:9090/bag/" + id.getId());
        return "redirect:/bag";
    }

    @PostMapping("/clearBag")
    public String clearBag(@AuthenticationPrincipal String token) {
        if(token.equals("anonymousUser"))
            return "redirect:/login";
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));
        restTemplate.delete("http://localhost:9090/bag");
        return "redirect:/bag";
    }

}
