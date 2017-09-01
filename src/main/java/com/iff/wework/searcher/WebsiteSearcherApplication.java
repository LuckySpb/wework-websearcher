package com.iff.wework.searcher;

import com.iff.wework.searcher.services.SearcherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebsiteSearcherApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebsiteSearcherApplication.class, args);
        SearcherService searcherService = context.getBean(SearcherService.class);
        searcherService.performSearch();
    }
}
