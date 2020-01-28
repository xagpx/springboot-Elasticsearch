package com.example.demo.es;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description:定义ItemRepository 接口
 * @Param:
 * 	Item:为实体类
 * 	Long:为Item实体类中主键的数据类型
 * @Author: https://blog.csdn.net/chen_2890
 * @Date: 2018/9/29 0:50
  */	 
public interface BlogModelRepository extends ElasticsearchRepository<BlogModel,String> {

	List<BlogModel> findByTitleLike(String keyword);
}
