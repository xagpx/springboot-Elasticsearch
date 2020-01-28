package com.example.demo.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.example.demo.SpringbootOauth2Application;

@SpringBootTest(classes = SpringbootOauth2Application.class)
public class EsDemoApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
      * @Description:创建索引，会根据Item类的@Document注解信息来创建
      * @Author: https://blog.csdn.net/chen_2890
      * @Date: 2018/9/29 0:51
       */
    @Test
    public void testCreateIndex() {
        elasticsearchTemplate.createIndex(Item.class);
    }
}