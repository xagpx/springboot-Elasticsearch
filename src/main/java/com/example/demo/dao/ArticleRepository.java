package com.example.demo.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.entity.Article;

/**
  * @Title:ArticleRepository.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:23:40
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

}