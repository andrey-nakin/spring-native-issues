package com.example.demo.controller;

import com.example.demo.entity.SampleEntity;
import com.example.demo.repository.SampleRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private final SampleRepository repository;

    public SampleController(SampleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/samples")
    @Transactional(readOnly = true)
    public List<SampleEntity> list() {
        return repository.findAll();
    }
}
