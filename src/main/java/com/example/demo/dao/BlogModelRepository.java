package com.example.demo.dao;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.entity.BlogModel;

/**
  * @Title:BlogModelRepository.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:25:08
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
public interface BlogModelRepository extends ElasticsearchRepository<BlogModel,String> {

	List<BlogModel> findByTitleLike(String keyword);
}
