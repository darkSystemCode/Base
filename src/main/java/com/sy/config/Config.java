package com.sy.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/1 23:50
 * @Descript: 配置类
 */
@Configuration
public class Config implements WebMvcConfigurer {


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setWriterFeatures(
                JSONWriter.Feature.PrettyFormat, // json格式化
                JSONWriter.Feature.WriteNullListAsEmpty, // 空list为[]
                JSONWriter.Feature.WriteNullBooleanAsFalse, // 空boolean为false
                JSONWriter.Feature.WriteNullNumberAsZero, // 空数字为0
                JSONWriter.Feature.WriteNullStringAsEmpty, // 空String为""
                JSONWriter.Feature.WriteMapNullValue, // 空map值
                JSONWriter.Feature.MapSortField // map排序
        );
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        // 3. 添加支持的媒体类型
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mediaTypes.add(MediaType.APPLICATION_PDF);
        mediaTypes.add(MediaType.APPLICATION_RSS_XML);
        mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        mediaTypes.add(MediaType.APPLICATION_XML);
        mediaTypes.add(MediaType.IMAGE_GIF);
        mediaTypes.add(MediaType.IMAGE_JPEG);
        mediaTypes.add(MediaType.IMAGE_PNG);
        mediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_MARKDOWN);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_XML);

        converter.setSupportedMediaTypes(mediaTypes);
        converter.setFastJsonConfig(config);
        converters.add(0, converter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // *为所有
        registry.addMapping("/**") // 拦截所有的请求
                .allowedOriginPatterns("*") // 可以跨域的域名
                .allowCredentials(true)
                .allowedMethods("*") // 允许跨域的方法请求
                .allowedHeaders("*"); // 允许跨域的请求头
    }
}
