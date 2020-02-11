package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.User;

/**
  * @Title:UserController.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:27:33
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired private UserRepository userRepository;
    @PostMapping("")
    public User save(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping("")
    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/{name}")
    public User findOne(@PathVariable String name){
        return userRepository.findByName(name);
    }
}