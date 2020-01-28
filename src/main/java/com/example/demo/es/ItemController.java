package com.example.demo.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemRepository itemRepository;
	
	@RequestMapping("/add")
	public String add() {
	  Item item = new Item(1L,"小米手机7","手机","小米",3499.00,"http://image.baidu.com/13123.jpg");
	  itemRepository.save(item);
	  return "0";
	}
	@RequestMapping("/addAll")
	public String insertList() {
	    List<Item> list = new ArrayList<>();
	    list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.baidu.com/13123.jpg"));
	    list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
	    list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
	    list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.baidu.com/13123.jpg"));
	    list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.baidu.com/13123.jpg"));
	    // 接收对象集合，实现批量新增
	    itemRepository.saveAll(list);
	    return "0";
	}
	@RequestMapping("/selectAll")
	public String testQueryAll(){
	    // 查找所有
        //Iterable<Item> list = this.itemRepository.findAll();
        // 对某字段排序查找所有 Sort.by("price").descending() 降序
        // Sort.by("price").ascending():升序
        Iterable<Item> list = this.itemRepository.findAll(Sort.by("price").ascending());
        for (Item item:list){
            System.out.println(item);
        }
        return "0";
    }
	@RequestMapping("/selectAlls")
	public String queryByPriceBetween(){
	    List<Item> list = this.itemRepository.findByPriceBetween(2000.00, 3500.00);
	    for (Item item : list) {
	        System.out.println("item = " + item);
	    }
	    return "0";
	}
	@RequestMapping("/selects")
	public String testMatchQuery(){
	    // 构建查询条件
	    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
	    // 添加基本分词查询
	    queryBuilder.withQuery(QueryBuilders.matchQuery("title", "小米手机"));
	    // 搜索，获取结果
	    Page<Item> items = this.itemRepository.search(queryBuilder.build());
	    // 总条数
	    long total = items.getTotalElements();
	    System.out.println("total = " + total);
	    for (Item item : items) {
	        System.out.println(item);
	    }
	    
	    return "0";
	}
	
	@DeleteMapping("/delete")
	public String deleteById() {
		itemRepository.deleteAll();
	    return  "0";
	}
	@RequestMapping("/Agg")
	public String testAgg(){
	    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
	    // 不查询任何结果
	    queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
	    // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
	    queryBuilder.addAggregation(
	        AggregationBuilders.terms("brands").field("brand"));
	    // 2、查询,需要把结果强转为AggregatedPage类型
	    AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
	    // 3、解析
	    // 3.1、从结果中取出名为brands的那个聚合，
	    // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
	    StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
	    // 3.2、获取桶
	    List<StringTerms.Bucket> buckets = agg.getBuckets();
	    // 3.3、遍历
	    for (StringTerms.Bucket bucket : buckets) {
	        // 3.4、获取桶中的key，即品牌名称
	        System.out.println(bucket.getKeyAsString());
	        // 3.5、获取桶中的文档数量
	        System.out.println(bucket.getDocCount());
	    }
	    return  "0";
	}
	@RequestMapping("/SubAgg")
	public String testSubAgg(){
	    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
	    // 不查询任何结果
	    queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
	    // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
	    queryBuilder.addAggregation(
	        AggregationBuilders.terms("brands").field("brand")
	        .subAggregation(AggregationBuilders.avg("priceAvg").field("price")) // 在品牌聚合桶内进行嵌套聚合，求平均值
	    );
	    // 2、查询,需要把结果强转为AggregatedPage类型
	    AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
	    // 3、解析
	    // 3.1、从结果中取出名为brands的那个聚合，
	    // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
	    StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
	    // 3.2、获取桶
	    List<StringTerms.Bucket> buckets = agg.getBuckets();
	    // 3.3、遍历
	    for (StringTerms.Bucket bucket : buckets) {
	        // 3.4、获取桶中的key，即品牌名称  3.5、获取桶中的文档数量
	        System.out.println(bucket.getKeyAsString() + "，共" + bucket.getDocCount() + "台");
	
	        // 3.6.获取子聚合结果：
	        InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
	        System.out.println("平均售价：" + avg.getValue());
	    }
	    return  "0";
	}
}
