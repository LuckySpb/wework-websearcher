package com.iff.wework.searcher.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
public class SearcherServiceTest {

    @Value("${searcher.result-file}")
    private String resultFile;
    @Autowired
    private SearcherService searcherService;

    @Test
    public void testSearch() throws Exception {

        searcherService.performSearch();
        List<String> lines = Files.readAllLines(Paths.get(resultFile));
        Assert.notEmpty(lines, "Result file should be not empty");
        Assert.isTrue(lines.size() == 1, "Result file should contain 1 line");
        Assert.isTrue(lines.get(0).contains("yahoo") && lines.get(0).contains("true"),
                "Search term should be found");

    }
}