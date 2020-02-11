package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.BlogModelRepository;
import com.example.demo.entity.BlogModel;
import com.example.demo.util.ReturnT;


/**
  * @Title:BlogController.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:24:11
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */

@RestController
@RequestMapping("/blog")
public class BlogController {
	@Autowired
    private BlogModelRepository blogModelRepository;
	
//	@Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
	
	@RequestMapping("/add")
	public String add(BlogModel blogModel) {
		blogModelRepository.save(blogModel);
	    return "0";
	}
	@GetMapping("/get/{id}")
	public String getById(@PathVariable String id) {
	    Optional<BlogModel> blogModelOptional = blogModelRepository.findById(id);
	    if (blogModelOptional.isPresent()) {
	        BlogModel blogModel = blogModelOptional.get();
	        return new ReturnT(blogModel).toString();
	    }
	    return null;
	}
	@GetMapping("/get")
	public String getAll() {
	    Iterable<BlogModel> iterable = blogModelRepository.findAll();
	    List<BlogModel> list = new ArrayList<>();
	    iterable.forEach(list::add);
	    return   new ReturnT(list).toString();
	}
	@PostMapping("/update")
	public String updateById(@RequestBody BlogModel blogModel) {
	    String id = blogModel.getId();
	    if (StringUtils.isEmpty(id))
	        return "1";
	    blogModelRepository.save(blogModel);
	    return "0";
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteById(@PathVariable String id) {
		if (StringUtils.isEmpty(id))
			return "1";
		blogModelRepository.deleteById(id);
	    return "0";
	}
	@DeleteMapping("/delete")
	public String deleteById() {
		blogModelRepository.deleteAll();
	    return  "0";
	}
	
	@GetMapping("/rep/search/title")
	public  String repSearchTitle(String keyword) {
	    if (StringUtils.isEmpty(keyword))
	    	return null;
	   List<BlogModel> t= blogModelRepository.findByTitleLike(keyword);
	    return   new ReturnT(t).toString();
	}
	
	@GetMapping("/ts")
	public String testMatchQuery(){
	    // 构建查询条件
	    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
	    // 添加基本分词查询
	    queryBuilder.withQuery(QueryBuilders.matchQuery("title", "java"));
	    // 搜索，获取结果
	    Page<BlogModel> items = this.blogModelRepository.search(queryBuilder.build());
	    // 总条数
	    long total = items.getTotalElements();
	    return   new ReturnT(items).toString();
	}
}


