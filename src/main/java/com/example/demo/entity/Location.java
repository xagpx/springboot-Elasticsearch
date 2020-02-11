package com.example.demo.entity;

import java.io.Serializable;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 无参构造
@AllArgsConstructor // 有参构造
@Document(indexName = "location", type = "item")
public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String name; // 标题
	@GeoPointField
	private GeoPoint location;// 位置坐标 lon经度 lat纬度
	private String address;// 地址
	 private String distanceMeters;//距离多少米

}
