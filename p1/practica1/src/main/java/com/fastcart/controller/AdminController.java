package com.fastcart.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fastcart.model.Product;
import com.fastcart.service.interf.ProductService;

@Controller
@RequestMapping("/admin/")
public class AdminController {
	private final ProductService productService;

	public AdminController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping(value = "save_product")
	public String saveProduct(@ModelAttribute("newProduct") Product product, Model model) {
		Product new_product = productService.saveProduct(product);
		model.addAttribute("operation", "new");

		if (new_product != null) {
			model.addAttribute("created", true);
			model.addAttribute("new_product", new_product);
		} else {
			model.addAttribute("created", false);
		}

		return "redirect:/admin/new_product";

	}

	@PostMapping(value = "modify_product/{id}")
	public String editProduct(@ModelAttribute("newProduct") Product product, Model model) {
		// Obtenemos el producto antes de modificarlo
		Product existingProduct = productService.getProductById(product.getId())
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + product.getId()));

		String old_name = existingProduct.getName();
		double old_price = existingProduct.getPrice();

		Product new_product = productService.editProduct(product);

		if ((!new_product.getName().equals(old_name)) || (new_product.getPrice() != old_price)) {
			model.addAttribute("edited", true);
			model.addAttribute("new_product", new_product);
			model.addAttribute("operation", "new");
			return "redirect:/admin/new_product";
		} else {
			model.addAttribute("edited", false);
			model.addAttribute("operation", "edit");
			return "admin";
		}
	}

	@PostMapping("delete_product/{id}")
	public String deleteProduct(@PathVariable Long id, Model model) {
		model.addAttribute("deleted", productService.deleteProduct(id));
		model.addAttribute("operation", "new");

		return "redirect:/admin/new_product";

	}

	@GetMapping("/new_product")
	public String newProduct(Model model) {
		List<Product> productos = productService.getAllProducts();
		model.addAttribute("products", productos);
		model.addAttribute("operation", "new");
		model.addAttribute("newProduct", new Product());

		return "admin"; // Thymeleaf buscará el archivo en templates/formulario-product.html
	}

	@GetMapping("/edit_product/{id}")
	public String editProduct(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id).get();
		if (product == null) {
			// En que el producto no se encuentre
			return "admin";
		}

		model.addAttribute("newProduct", product);
		model.addAttribute("operation", "edit");

		return "admin"; // Thymeleaf buscará el archivo en templates/formulario-product.html
	}

	@GetMapping("/export_products")
	public ResponseEntity<byte[]> exportProducts() {
		byte[] sqlScript = productService.exportProducts();
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"products.sql\"")
				.contentType(MediaType.TEXT_PLAIN).body(sqlScript);
	}
}
