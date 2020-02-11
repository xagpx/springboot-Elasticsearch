package com.example.demo.dao;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.entity.Item;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
	 /**
     * @Description:根据价格区间查询
     * @Param price1
     * @Param price2
     * @Author: https://blog.csdn.net/chen_2890
     */
    List<Item> findByPriceBetween(double price1, double price2);
}