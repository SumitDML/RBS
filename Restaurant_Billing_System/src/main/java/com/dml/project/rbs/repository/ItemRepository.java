package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Item findByName(String name);

   // @Query(value = "select * from studyiqbackend.liveclass_faculty_subject where liveclass_id=:liveclass_id", nativeQuery = true)
    @Query(value = "select * from items where (name like %:starts%)",nativeQuery = true)
    List<Item> startsWithName(@Param(value = "starts") String starts);
}
