package com.example.demo.controller;

import com.example.demo.entity.SampleEntity;
import com.example.demo.repository.SampleRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
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
        return repository.findAllByOrderByName().toList();
    }

    @GetMapping("/samples/first")
    @Transactional(readOnly = true)
    public SampleEntity first() {
        return repository.findAllByOrderByName().findFirst().orElseThrow();
    }

    @PostConstruct
    @Transactional
    void init() {
        var e = new SampleEntity();
        e.setId(UUID.randomUUID().toString());
        e.setName("a");
        e.setVal("b");
        repository.save(e);

        e.setId(UUID.randomUUID().toString());
        e.setName("c");
        e.setVal("d");
        repository.save(e);
    }
}
