package com.example.demo.config;
/**
  * @Title:LogIpConfig.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月7日下午2:10:54
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
*/
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
@Component
public class LogIpConfig extends ClassicConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogIpConfig .class);
    @Override
    public String convert(ILoggingEvent event) {
        try {
        	String ip=InetAddress.getLocalHost().getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            LOGGER.error("获取日志Ip异常", e);
        }
        return null;
    }
}