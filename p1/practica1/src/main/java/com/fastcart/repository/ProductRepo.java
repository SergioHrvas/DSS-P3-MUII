package com.fastcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fastcart.model.Product;

//@RepositoryRestResource(collectionResourceRel="tasks",path="tasks")
public interface ProductRepo extends JpaRepository<Product, Long> {
	List<Product> findByNameContainingIgnoreCaseAndPriceBetween(String name, Double minPrice, Double maxPrice);
}
