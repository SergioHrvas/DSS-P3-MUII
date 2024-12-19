package com.fastcart.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fastcart.model.Product;
import com.fastcart.service.interf.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/admin/")
public class ApiAdminController {
	private final ProductService productService;

    private final String uploadDir = "src/main/resources/static/uploads/";
    
	public ApiAdminController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping(value = "/save_product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> saveProduct(
	        @RequestPart("product") String productJson,
	        @RequestPart("file") MultipartFile file) {

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("El archivo de imagen no puede estar vacío.");
	    }

	    try {
	        System.out.println("Producto recibido (JSON): {}" + productJson);
	        System.out.println("Archivo recibido: {}" + file.getOriginalFilename());
	        // Convertir el JSON del producto a un objeto Product
	        ObjectMapper objectMapper = new ObjectMapper();

	        Product product = objectMapper.readValue(productJson, Product.class);

	        // Guardar la imagen y obtener la ruta
	        String imagePath = saveImage(file);

	        // Asociar la ruta de la imagen al producto
	        product.setImagePath(imagePath);

	        // Guardar el producto en la base de datos
	        Product savedProduct = productService.saveProduct(product);

	        // Retornar el producto guardado como respuesta
	        return ResponseEntity.ok(savedProduct);

	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el producto: " + e.getMessage());
	    }
	}


	// Método auxiliar para guardar la imagen
	private String saveImage(MultipartFile file) throws IOException {
	    Path uploadPath = Paths.get(uploadDir);
	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath); // Crea el directorio si no existe
	    }

	    // Genera un nombre único para la imagen
	    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	    Path filePath = uploadPath.resolve(filename);

	    // Guarda la imagen en el sistema de archivos
	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	    String filePathShort = "uploads/" + filename;
	    // Retorna la ruta relativa de la imagen
	    return filePathShort;
	}

	

	@DeleteMapping("delete_product/{id}")
	public boolean deleteProduct(@PathVariable Long id) {
		boolean deleted = productService.deleteProduct(id);

		return deleted;

	}
	
	@PostMapping("test")
	public String testEndpoint() {
	    System.out.println("Endpoint test ejecutado");
	    return "Funciona";
	}
}
