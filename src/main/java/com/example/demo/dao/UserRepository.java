package com.example.demo.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.entity.User;


/**
  * @Title:UserRepository.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:27:44
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
public interface UserRepository extends ElasticsearchRepository<User, String> {

    User findByName(String name);
}