package com.example.demo.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;


/**
  * @Title:User.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:27:09
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
@Data
@Document(indexName = "user", type = "test")
public class User {
    @Id
    private String id;
    private String name;
    private int age = 18;
    private Date createTime = new Date();
}