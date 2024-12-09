package com.fastcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fastcart.model.Cart;

//@RepositoryRestResource(collectionResourceRel="tasks",path="tasks")
public interface CartRepo extends JpaRepository<Cart, Long> {

}
