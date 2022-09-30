package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.BuyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BuyItemRepository extends JpaRepository<BuyItem,String> {
}
