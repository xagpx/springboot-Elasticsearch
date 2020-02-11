package com.example.demo.controller;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dao.ArticleRepository;
import com.example.demo.entity.Article;


/**
  * @Title:ArticleController.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:23:26
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired private ArticleRepository articleRepository;
    @PostMapping("")
    public Article save(@RequestBody Article article){
        return articleRepository.save(article);
    }

    @GetMapping("")
    public Iterable<Article> findAll(){
        return articleRepository.findAll();
    }

    /**---------- QueryBuilder 查询 -----------**/
    /**分页查询*/
    @GetMapping("/page")
    public Page<Article> range(String query,
                               @PageableDefault(page = 0, size = 5, sort = "time", direction = Sort.Direction.DESC) Pageable pageable){
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if(query != null) {
            qb.must(QueryBuilders.matchQuery("title", query));
        }
        return articleRepository.search(qb, pageable);
    }

    /**精确匹配*/
    @GetMapping("/term")
    public Page<Article> term(String query){
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(QueryBuilders.termQuery("author", query));
        return (Page<Article>)articleRepository.search(qb);
    }

    /**模糊匹配*/
    @GetMapping("/match")
    public Page<Article> match(String query){
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(QueryBuilders.matchQuery("content", query));
        return (Page<Article>)articleRepository.search(qb);
    }

    /**短语模糊匹配*/
    @GetMapping("/matchPhrase")
    public Page<Article> matchPhraseQuery(String query){
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(QueryBuilders.matchPhraseQuery("content", query));
        return (Page<Article>)articleRepository.search(qb);
    }

    /**范围查询*/
    @GetMapping("/range")
    public Page<Article> range(long query){
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(QueryBuilders.rangeQuery("time").gt(query));
        //qb.must(QueryBuilders.rangeQuery("time").from(query).to(System.currentTimeMillis()));//大于query，小于当前时间
        return (Page<Article>)articleRepository.search(qb);
    }
}