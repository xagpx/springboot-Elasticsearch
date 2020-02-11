package com.example.demo.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.entity.Location;

public interface LocationRepository extends ElasticsearchRepository<Location,String> {
}