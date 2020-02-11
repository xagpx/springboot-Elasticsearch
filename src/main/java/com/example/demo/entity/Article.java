package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "article", type = "test")
public class Article {
    @Id
    private String id;
    private String author;
    private String title;
    private String content;
    private Date time;
}