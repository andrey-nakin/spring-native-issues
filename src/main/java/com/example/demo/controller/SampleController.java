package com.example.demo.controller;

import com.example.demo.config.SampleDataProperties;
import com.example.demo.entity.SampleEntity;
import com.example.demo.repository.SampleRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private final SampleRepository repository;
    private final SampleDataProperties sampleDataProperties;

    public SampleController(SampleRepository repository, SampleDataProperties sampleDataProperties) {
        this.repository = repository;
        this.sampleDataProperties = sampleDataProperties;
    }

    @GetMapping("/samples")
    @Transactional(readOnly = true)
    public List<SampleEntity> list() {
        return repository.findAll();
    }

    @PostConstruct
    @Transactional
    void populateDb() {
        if (repository.count() == 0) {
            repository.saveAll(sampleDataProperties.getData());
        }
    }
}
