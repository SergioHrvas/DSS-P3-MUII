package com.fastcart.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastcart.dto.CartItemDto;
import com.fastcart.service.interf.CartService;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping
	public String all(Model model, Authentication authentication) {
		List<CartItemDto> items = cartService.getProductsCart(authentication.getName());

		double total = 0;
		for (CartItemDto item : items) {
			total += item.getPrice() * item.getNum();
		}

		model.addAttribute("items", items);
		model.addAttribute("total", total);

		return "cart";
	}

	@PostMapping("/add_item")
	public String addCartItem(@RequestParam(required = true, defaultValue = "1") int num,
			@RequestParam(required = true) Long productId, Authentication authentication,  RedirectAttributes redirectAttributes) {
		boolean added = cartService.addItemCart(authentication.getName(), productId, num);

		redirectAttributes.addFlashAttribute("added", added);

		return "redirect:/products";

	}

	@PostMapping("/delete_item/{id}")
	public String deleteCartItem(@PathVariable Long id, @RequestParam int num, Authentication authentication,
			Model model) {
		boolean deleted = cartService.updateOrDeleteProductCart(authentication.getName(), id, num);

		model.addAttribute("deleted", deleted);
		return "redirect:/cart";
	}

	@GetMapping("/pdf")
	public ResponseEntity<byte[]> getCartPdf(Authentication authentication) {
		byte[] pdfBytes = cartService.generateCartPdf(authentication.getName());
		// Establecer los headers para la descarga del archivo
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "cart.pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

}
