package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends CrudRepository<Item, Long> {
    Set<Item> findByLostTrue();
    Set<Item> findByLostFalse();
}
