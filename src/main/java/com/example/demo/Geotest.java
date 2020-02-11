package com.example.demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.example.demo.entity.People;

/**
  * @Title:Geotest.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月6日下午7:29:31
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
 */

public class Geotest {
	static TransportClient client;
	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnknownHostException {
		Settings settings = Settings.builder().put("cluster.name", "my-application").build();
		 client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
	
		  String index = "es";
	       String type = "people";
	      // init(index, type);
	        double lat1 = 42;
	         double lon1 = -72;
	         double lat2 = 40;
	         double lon2 =  -74;
	         //查询坐标1到坐标2矩形范围内的用户
	        // testGetNearbyPeople(index, type, lat1, lon1,lat2,lon2);
	         //获取坐标 附近1000米的用户信息
	         //testGetNearbyPeople2( index, type, 40, -70);
	          
	         System.out.println("---多边形查询 ----");
	         //testPolygonQuery(index, type);
	          
	 
	       double lat = 39.999517;
	        double lon = 116.465176;
	        long start = System.currentTimeMillis();
	        testGetNearbyPeople2( index, type, lat, lon);
	        long end = System.currentTimeMillis();
	        System.out.println((end - start) + "毫秒");
	
	}
	 /**
     * 查询坐标1到坐标2矩形范围内,的用户有那些
     * 
     * @param indexName
     * @param indexType
     * @param lat
     * @param lon
     */
    public static void testGetNearbyPeople(String indexName, String indexType, double top, double left, double bottom,
            double right) {
        SearchRequestBuilder srb = client.prepareSearch(indexName).setTypes(indexType);
        SearchResponse searchResponse = srb
                .setQuery(QueryBuilders.geoBoundingBoxQuery("location").setCorners(top, left, bottom, right)).get();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            System.out.println(searchHit.getSourceAsString());
        }
 
        System.out.println("-----");
    }
 
    /**
     * 获取坐标 附近1000米的用户信息
     * 
     * @param indexName
     * @param indexType
     * @param lat 纬度
     * @param lon 经度
     */
    public static void testGetNearbyPeople2(String indexName, String indexType, double lat, double lon) {
        SearchRequestBuilder srb = client.prepareSearch(indexName).setTypes(indexType);
        srb.setFrom(0).setSize(1000);// 1000人
        // lon, lat位于谦的坐标，查询距离于谦1米到1000米
        GeoDistanceQueryBuilder location1 = QueryBuilders.geoDistanceQuery("location").point(lat, lon).distance(1000,
                DistanceUnit.MILES);
        srb.setPostFilter(location1);
         
        //srb.setQuery(QueryBuilders.boolQuery().filter(location1));
 
        // 获取距离多少公里 这个才是获取点与点之间的距离的
        GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("location", lat, lon);
        sort.unit(DistanceUnit.MILES);
        sort.order(SortOrder.ASC);
        sort.point(lat, lon);
        srb.addSort(sort);
 
         
        SearchResponse searchResponse = srb.execute().actionGet();
         
       // System.out.println(srb.toString());
 
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHists = hits.getHits();
        // 搜索耗时
        
	    long usetime = searchResponse.getTook().getMillis()/1000;
        System.out.println("坐标附近的人(" + hits.getTotalHits() + "个)，耗时(" + usetime + "秒)：");
        for (SearchHit hit : searchHists) {
            String name = (String) hit.getSourceAsMap().get("name");
            Map location =  (Map) hit.getSourceAsMap().get("location");
            // 获取距离值，并保留两位小数点
            BigDecimal geoDis = new BigDecimal((Double) hit.getSortValues()[0]);
            Map<String, Object> hitMap = hit.getSourceAsMap();
            // 在创建MAPPING的时候，属性名的不可为geoDistance。
            hitMap.put("geoDistance", geoDis.setScale(0, BigDecimal.ROUND_HALF_DOWN));
            System.out.println(name + "的坐标：" + location + "他距离坐标" + hit.getSourceAsMap().get("geoDistance")
                    + DistanceUnit.METERS.toString());
        }
 
    }
     
     
       /**
     * 多边形查询
     */
    public static void testPolygonQuery(String indexName, String indexType) {
        List<GeoPoint> points=new ArrayList<GeoPoint>();
        points.add(new GeoPoint(42, -72));
        points.add(new GeoPoint(39, 117));
        points.add(new GeoPoint(40, 117));
         
        SearchResponse response = client.prepareSearch(indexName).setTypes(indexType)
                  .setQuery(QueryBuilders.geoPolygonQuery("location",points))
                  .get();
         
        System.out.println(response);
        System.err.println(response.getHits().getTotalHits());
    }
 
    private static void init(String indexName, String indexType) {
        //
        try {
            createIndex(indexName, indexType);
            addIndexData100000(indexName, indexType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // 创建索引
    public static void createIndex(String indexName, String indexType) throws IOException {
        // 创建Mapping
        XContentBuilder mapping = createMapping(indexType);
        System.out.println("mapping:" + mapping.toString());
 
        client.admin().indices().prepareCreate(indexName).execute().actionGet();
        PutMappingRequest putMapping = Requests.putMappingRequest(indexName).type(indexType).source(mapping);
        AcknowledgedResponse response = client.admin().indices().putMapping(putMapping).actionGet();
        if (!response.isAcknowledged()) {
            System.out.println("Could not define mapping for type [" + indexName + "]/[" + indexType + "].");
        } else {
            System.out.println("Mapping definition for [" + indexName + "]/[" + indexType + "] succesfully created.");
        }
    }
 
    // 创建mapping
    public static XContentBuilder createMapping(String indexType) {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(indexType).startObject("properties")
                    // ID
                    .startObject("id").field("type", "long").endObject()
                    // 姓名
                    .startObject("name").field("type", "text").endObject()
                    // 位置
                    .startObject("location").field("type", "geo_point").endObject().endObject().endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }
 
    public static Integer addIndexData100000(String indexName, String indexType) {
        List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
 
        double lat = 39.929986;
        double lon = 116.395645;
         
         
       for (int i = 0; i < 10; i++) {
        double max = 0.00001;
        double min = 0.000001;
        Random random = new Random();
        double s = random.nextDouble() % (max - min + 1) + max;
        DecimalFormat df = new DecimalFormat("######0.000000");
        // System.out.println(s);
        String lons = df.format(s + lon);
        String lats = df.format(s + lat);
        Double dlon = Double.valueOf(lons);
        Double dlat = Double.valueOf(lats);
        People city1 = new People(i, "Tony=" + i, dlat, dlon);
        cityList.add(obj2JsonUserData(city1));
       }
 
        People city1 = new People(1, "喜来登大酒店",41.12, -71.34);
        cityList.add(obj2JsonUserData(city1));
         
        People city2 = new People(2, "喜来登大酒店B", 42.12, -71.34);
        cityList.add(obj2JsonUserData(city2));
         
         
        // 创建索引库
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int i = 0; i < cityList.size(); i++) {
        	IndexRequest response = client.prepareIndex(indexName, indexType).setSource(cityList.get(i)).request();
            bulkRequest.add(response);
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            System.out.println(bulkResponse.buildFailureMessage());
            System.out.println("批量创建索引错误！");
        }
 
        return bulkRequest.numberOfActions();
    }
 
    public static Map<String, Object> obj2JsonUserData(People people) {
    	Map<String, Object> source = new HashMap<String, Object>();
    	Map<String, Object> location = new HashMap<String, Object>();
    	source.put("id", people.getId());
    	source.put("name", people.getName());
    	source.put("location", location);
    	location.put("lon", people.getLon());
    	location.put("lat", people.getLat());
    	return source;
    }
}
