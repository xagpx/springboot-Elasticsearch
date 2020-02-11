package com.example.demo.controller;

import java.util.List;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.LocationRepository;
import com.example.demo.entity.Location;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;



/**
  * @Title:GeoDataRestController.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月7日下午3:20:13
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
  */
@Api(tags = "GeoDataRestApi")
@RestController
@RequestMapping("/location")
public class GeoDataRestController {
  private static final Logger logger = LoggerFactory.getLogger(GeoDataRestController.class);
  @Autowired
  private LocationRepository locationRepository;

  @PostMapping("/save")
  public Location save(@RequestBody Location location){
      return locationRepository.save(location);
  }
  /**
   * 搜索附近
   * @param lon 当前位置 经度
   * @param lat 当前位置 纬度
   * @param distance 搜索多少范围
   * @param pageable 分页参数
   * @return
   */
  @ApiOperation(value = "搜索附近商店", notes = "根据经纬度查询周围商店")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "lon", value = "经度",required = true,paramType = "query", dataType = "String"),
          @ApiImplicitParam(name = "lat", value = "维度",required = true,paramType = "query", dataType = "String"),
          @ApiImplicitParam(name = "distance", value = "距离",required = true,paramType = "query", dataType = "String")
  })
  @GetMapping("/searchNear")
  public List<Location> searchNearWithOrder(double lon, double lat, String distance, @PageableDefault Pageable pageable){
	  //搜索字段为 location
      GeoDistanceQueryBuilder geoBuilder = new GeoDistanceQueryBuilder("location");
      geoBuilder.point(lat, lon);//指定从哪个位置搜索
      geoBuilder.distance(distance, DistanceUnit.KILOMETERS);//指定搜索多少km
      //距离排序
      GeoDistanceSortBuilder sortBuilder = new GeoDistanceSortBuilder("location", lat, lon);
      sortBuilder.order(SortOrder.ASC);//升序
      sortBuilder.unit(DistanceUnit.METERS);

      //构造查询器
      NativeSearchQueryBuilder qb = new NativeSearchQueryBuilder()
              .withPageable(pageable)
              .withFilter(geoBuilder)
              .withSort(sortBuilder);

      //可添加其他查询条件
      //qb.must(QueryBuilders.matchQuery("address", address));
      Page<Location> page = locationRepository.search(qb.build());
      List<Location> list = page.getContent();
      list.forEach(l -> {
          double calculate = GeoDistance.PLANE.calculate(l.getLocation().getLat(), l.getLocation().getLon(), lat, lon, DistanceUnit.METERS);
          l.setDistanceMeters("距离" + (int)calculate + "m");
          });
      logger.info(list.toString());
      return list;
  }
}