package com.example.demo.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public BlogModel getById(@PathVariable String id) {
	    Optional<BlogModel> blogModelOptional = blogModelRepository.findById(id);
	    if (blogModelOptional.isPresent()) {
	        BlogModel blogModel = blogModelOptional.get();
	        return blogModel;
	    }
	    return null;
	}
	@GetMapping("/get")
	public List<BlogModel> getAll() {
	    Iterable<BlogModel> iterable = blogModelRepository.findAll();
	    List<BlogModel> list = new ArrayList<>();
	    iterable.forEach(list::add);
	    return   list;
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
	public  List<BlogModel> repSearchTitle(String keyword) {
	    if (StringUtils.isEmpty(keyword))
	    	return null;
	   List<BlogModel> t= blogModelRepository.findByTitleLike(keyword);
	   return t;
	}
	
	/*@GetMapping("/ts")
	public void testMatchQuery(){
	    // 构建查询条件
	    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
	    // 添加基本分词查询
	    queryBuilder.withQuery(QueryBuilders.matchQuery("title", "java"));
	    // 搜索，获取结果
	    Page<BlogModel> items = this.itemRepository.search(queryBuilder.build());
	    // 总条数
	    long total = items.getTotalElements();
	    System.out.println("total = " + total);
	    for (BlogModel item : items) {
	        System.out.println(item);
	    }
	}*/
}


