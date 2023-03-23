package com.example.demo.repository;

import com.example.demo.entity.SampleEntity;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<SampleEntity, Integer> {

    Stream<SampleEntity> findAllByOrderByName();
}
