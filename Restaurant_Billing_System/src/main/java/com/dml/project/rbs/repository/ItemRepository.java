package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Item findByName(String name);
}
