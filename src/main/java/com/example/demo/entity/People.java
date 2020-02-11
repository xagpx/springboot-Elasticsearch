package com.example.demo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class People {
	int id;
	String name;
	double lat;
    double lon;
}
