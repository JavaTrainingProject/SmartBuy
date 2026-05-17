package com.example.smartbuy.config;

//@Configuration
//public class ImageConfig {

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:///" + uploadDir+"/");
//    }
//}
//



    @Configuration
    public class ImageConfig {

        @Value("${cloudinary.cloud-name}")
        private String cloudName;

        @Value("${cloudinary.api-key}")
        private String apiKey;

        @Value("${cloudinary.api-secret}")
        private String apiSecret;

        @Bean
        public Cloudinary cloudinary() {

            Map<String, String> values =
                    new HashMap<>();

            values.put("cloud_name", cloudName);

            values.put("api_key", apiKey);

            values.put("api_secret", apiSecret);

            return new Cloudinary(values);
        }
    }