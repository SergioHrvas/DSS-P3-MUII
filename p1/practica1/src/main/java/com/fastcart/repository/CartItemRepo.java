package com.fastcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.fastcart.model.CartItem;

import jakarta.transaction.Transactional;

//@RepositoryRestResource(collectionResourceRel="tasks",path="tasks")
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM CartItem ci WHERE ci.product.id = :productId")
	    void deleteByProductId(Long productId);
}
