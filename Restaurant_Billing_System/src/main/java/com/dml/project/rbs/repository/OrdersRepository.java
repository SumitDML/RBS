package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrdersRepository extends JpaRepository<Orders,String> {
}
