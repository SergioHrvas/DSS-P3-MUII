package com.fastcart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fastcart.model.Product;
import com.fastcart.service.interf.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// Vistas
	@GetMapping
	public String productos(Model model) {
		List<Product> productos = productService.getAllProducts();
		model.addAttribute("products", productos);

		return "products"; // Thymeleaf buscará el archivo en templates/productos.html
	}

	@GetMapping("/searchAndFilter")
	public String searchAndFilterProducts(@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "0") Double minPrice,
			@RequestParam(required = false, defaultValue = "10000") Double maxPrice, Model model) {
		List<Product> products = productService.searchAndFilterProducts(name, minPrice, maxPrice);
		model.addAttribute("products", products);

		model.addAttribute("name", name);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);

		return "products";
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		return productService.getProductById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

}
