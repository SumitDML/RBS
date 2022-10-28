package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.ItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
    ItemEntity findByName(String name);


    @Query(value = "select * from items where (name like %:starts%)",nativeQuery = true)
    List<ItemEntity> startsWithName(@Param(value = "starts") String starts);
}
