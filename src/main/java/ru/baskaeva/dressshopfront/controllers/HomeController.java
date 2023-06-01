package ru.baskaeva.dressshopfront.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ru.baskaeva.dressshopfront.JwtTokenAuthentication;
import ru.baskaeva.dressshopfront.dto.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@Slf4j
public class HomeController {
    final RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/")
    public String home(Model model, FilterDTO filterDTO, @AuthenticationPrincipal String token,
                       @RequestParam(required = false) Integer page) {
        String type = "Все товары";
        if(filterDTO.getTypes() != null)
            switch(filterDTO.getTypes().get(0)) {
                case COAT -> type = "Пиджаки";
                case DRESS -> type = "Платья";
                case SKIRT -> type = "Юбки";
                case BLOUSE -> type = "Блузки";
                case JACKET -> type = "Жакеты";
                case TROUSERS -> type = "Брюки";
            }
        page = page == null ? 1 : page;
        page -= 1;
        boolean isAuth = !token.equals("anonymousUser");
        model.addAttribute("isAuth", isAuth);
        log.info("{}", filterDTO);
        filterDTO = filterDTO == null ? FilterDTO.builder().build() : filterDTO;
        UriBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:9090/products")
                .queryParam("page", page)
                .queryParam("limit", 20);
        if(filterDTO.getColors() != null)
            uriBuilder.queryParam("colors", filterDTO.getColors());
        if(filterDTO.getSizes() != null)
            uriBuilder.queryParam("sizes", filterDTO.getSizes());
        if(filterDTO.getTypes() != null)
            uriBuilder.queryParam("types", filterDTO.getTypes());
        if(filterDTO.getPriceFrom() != null)
            uriBuilder.queryParam("priceFrom", filterDTO.getPriceFrom());
        if(filterDTO.getPriceTo() != null)
            uriBuilder.queryParam("priceTo", filterDTO.getPriceTo());
        if(filterDTO.getIsLiked() != null)
            uriBuilder.queryParam("isLiked", filterDTO.getIsLiked());
        ResponseEntity<ProductsPageableDTO> response = restTemplate.getForEntity(uriBuilder.build(), ProductsPageableDTO.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("{}", response.getBody());
            List<ProductDTO> products = response.getBody().getContent();
            model.addAttribute("products", products);
            var totalPages = response.getBody().getTotalPages();
            model.addAttribute("totalPage", totalPages);
            var pageNumber = response.getBody().getPageable().getPageNumber();
            model.addAttribute("currentPage", pageNumber + 1);
            model.addAttribute("isFirst", response.getBody().getFirst());
            model.addAttribute("isLast", response.getBody().getLast());
            model.addAttribute("pageAfter", pageNumber > 0 ? IntStream.range(1, pageNumber + 1).boxed().toList() : IntStream.empty().boxed().toList());
            model.addAttribute("pageBefore", pageNumber < totalPages - 1 ? IntStream.range(pageNumber + 1, totalPages).boxed().toList() : IntStream.empty().boxed().toList());
            model.addAttribute("type", type);
        }
        return "index";
    }

    @GetMapping("/test2")
    public String changeFilters(FilterDTO filterDTO) {
        log.info("{}", filterDTO);
        return "redirect:/";
    }

    @PostMapping("/increase")
    public String increaseCount(IdDTO id, @AuthenticationPrincipal String token, HttpServletRequest req) {
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));

        log.info("{}", id.getId());
        restTemplate.postForEntity("http://localhost:9090/bag/" + id.getId(), null, Object.class);
        return "redirect:" + req.getHeader("Referer");
    }

    @PostMapping("/decrease")
    public String decreaseCount(IdDTO id, @AuthenticationPrincipal String token, HttpServletRequest req) {
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));

        log.info("{}", id.getId());
        restTemplate.delete("http://localhost:9090/bag/" + id.getId());
        return "redirect:" + req.getHeader("Referer");
    }

    @GetMapping("/product/{id}")
    public String productCard(@PathVariable long id,  Model model, @RequestParam(required = false) String color, @RequestParam(required = false) String size) {
        log.info("color {}", color);
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity("http://localhost:9090/products/" + id, ProductDTO.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("{}", response.getBody());
            color = color == null ? response.getBody().getColors().get(0).getColor() : color;
            String finalColor = color;
            List<ProductColor> productColors = response.getBody().getColors();
            ProductColor prColor = productColors.stream().filter((c) -> c.getColor().equals(finalColor)).findFirst().orElse(productColors.get(0));
            List<ProductSize> productSizes = prColor.getSizes();
            ProductSize prSize = size == null ? productSizes.get(0) : productSizes.stream().filter(s -> s.getSize().equals(ProductSize.Size.valueOf(size))).findFirst().orElse(productSizes.get(0));
            log.info("curColor {}", prColor);
            model.addAttribute("product", response.getBody());
            model.addAttribute("curColor", prColor);
            model.addAttribute("productColors", productColors);
            model.addAttribute("curSize", prSize);
            model.addAttribute("productSizes", productSizes);
        }
        return "product";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/test")
    public String test(@RequestParam String list){
        log.info("{}", list);
        return "index";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(UserDTO userDTO) {
        log.info("{}", userDTO);
        ResponseEntity<TokenDTO> response = restTemplate.postForEntity("http://localhost:9090/register", userDTO, TokenDTO.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("{}", response.getBody());
            SecurityContextHolder.getContext().setAuthentication(new JwtTokenAuthentication(response.getBody().getAuthToken()));
        }
        return "redirect:/";
    }

    @PostMapping("/addToBag")
    public String addToBag(IdDTO id, @AuthenticationPrincipal String token) {
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));
        restTemplate.postForEntity("http://localhost:9090/bag/" + id.getId(), null, TokenDTO.class);
        return "redirect:/";
    }

    @PostMapping("/addToFavourite")
    public String addToFavourite(IdDTO id, @AuthenticationPrincipal String token, HttpServletRequest req) throws URISyntaxException {
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));
        restTemplate.postForEntity("http://localhost:9090/like/" + id.getId(), null, TokenDTO.class);
        return "redirect:" + new URI(req.getHeader("Referer")).getPath();
    }

    @PostMapping("/removeFavourite")
    public String removeFavourite(IdDTO id, @AuthenticationPrincipal String token, HttpServletRequest req) throws URISyntaxException {
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().put("Authorization", List.of("Bearer " + token));
            return execution.execute(request, body);
        }));
        restTemplate.delete("http://localhost:9090/like/" + id.getId());
        return "redirect:" + new URI(req.getHeader("Referer")).getPath();
    }

    @GetMapping("/favourite")
    public String favourite(Model model, @AuthenticationPrincipal String token,
                            @RequestParam(required = false) Integer page) {
        page = page == null ? 1 : page;
        page -= 1;
        boolean isAuth = !token.equals("anonymousUser");
        model.addAttribute("isAuth", isAuth);
        ResponseEntity<ProductsPageableDTO> response = restTemplate.getForEntity("http://localhost:9090/products?page={page}&limit={limit}&isLiked=true", ProductsPageableDTO.class, Map.of( "page", page,"limit", 20));
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<ProductDTO> products = response.getBody().getContent();
            model.addAttribute("products", products);
            var totalPages = response.getBody().getTotalPages();
            model.addAttribute("totalPage", totalPages);
            var pageNumber = response.getBody().getPageable().getPageNumber();
            model.addAttribute("currentPage", pageNumber + 1);
            model.addAttribute("isFirst", response.getBody().getFirst());
            model.addAttribute("isLast", response.getBody().getLast());
            model.addAttribute("pageAfter", pageNumber > 0 ? IntStream.range(1, pageNumber + 1).boxed().toList() : IntStream.empty().boxed().toList());
            model.addAttribute("pageBefore", pageNumber < totalPages - 1 ? IntStream.range(pageNumber + 1, totalPages).boxed().toList() : IntStream.empty().boxed().toList());
        }
        return "index";
    }
}
