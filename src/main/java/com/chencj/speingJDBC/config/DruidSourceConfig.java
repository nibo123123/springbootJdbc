package com.chencj.speingJDBC.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
//加入这个注解就相当于，配置文件的spring中的application.xml的效果
public class DruidSourceConfig {

	
	@ConfigurationProperties(prefix="spring.datasource")
	
	@Bean
	//相当于bean标签
	public DataSource getDataSource(){
		return new DruidDataSource();
	}
	
	//配置一个druid的监控器
	//需要servlet，需要一个web.xml但是没有，
	//可以使用ServletRegistrationBean方式实现，
	//和web.xml中servlet标签效果一样
	@Bean
	public ServletRegistrationBean getViewServlet(){
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(
				new StatViewServlet(),//相当于web.xml中的返回视图类
				"/druid/*");//相当于 url-pattern
		//可以配置一些初始化can数
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("loginUsername", "admin");
		initParameters.put("loginPassword", "123456");
		initParameters.put("allow", "");
		//initParameters.put("deny", "");
		
		registrationBean.setInitParameters(initParameters );
		
		
		return registrationBean;
	} 
	//配一个过滤器filter
	//这个filter就是web.xml中的filter标签
	//可以使用FilterRegistrationBean ,实现
	@Bean
	public FilterRegistrationBean webStateFilter(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		//添加过滤的处理类
		registrationBean.setFilter(new WebStatFilter());
		
		//添加初始化参数
		Map<String, String> initParameters = new HashMap<>();
		//过滤器放行的url路径
		initParameters.put("exclusions", "*.js,*.css,/druid/*");
		registrationBean.setInitParameters(initParameters );
		
		
		registrationBean.setUrlPatterns(Arrays.asList("/*"));
		
		return registrationBean;
	}
	
}
