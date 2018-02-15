package com.example.demo.repositories;

import com.example.demo.models.ComplaintQueryObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintQueryObjectRepository extends JpaRepository<ComplaintQueryObject, String> {
}
