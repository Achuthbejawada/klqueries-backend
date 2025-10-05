package com.example.attendanceapp.repository;

import com.example.attendanceapp.entity.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QueryRepository extends JpaRepository<Query, Long> {
    List<Query> findByUserId(Long userId);
}
