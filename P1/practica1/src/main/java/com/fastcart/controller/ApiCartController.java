package com.fastcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fastcart.dto.CartItemDto;
import com.fastcart.service.interf.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ApiCartController {
	private final CartService cartService;

	public ApiCartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping
	public List<CartItemDto> all(Long idUser) {
		
		List<CartItemDto> items = cartService.getProductsCart("admin");

		return items;
	}
/*
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
*/
}
