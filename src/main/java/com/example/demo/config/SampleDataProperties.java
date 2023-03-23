package com.example.demo.config;

import com.example.demo.entity.SampleEntity;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sample-data")
@PropertySource(value = "classpath:sample-data.yaml", factory = YamlPropertySourceFactory.class)
public class SampleDataProperties {

    private List<SampleEntity> data;

    public List<SampleEntity> getData() {
        return data;
    }

    public void setData(List<SampleEntity> data) {
        this.data = data;
    }
}
