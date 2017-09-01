package com.iff.wework.searcher.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearcherService {

    @Value("${searcher.url: https://s3.amazonaws.com/fieldlens-public/urls.txt}")
    private String url;

    @Value("${searcher.search-term}")
    private String searchTerm;

    @Value("${searcher.site-filter:}")
    private String siteFilter;


    @Autowired
    private SiteListLoader siteListLoader;

    @Autowired
    private LimitedConnectionService limitedConnectionService;

    @Autowired
    private BodySearcherService bodySearcherService;

    @Autowired
    private ResultFileService resultFileService;

    public void performSearch() {

        List<String> siteList = siteListLoader.loadSiteList(url);

        // Executor service just to span threads, but not to limit number of concurrent connections
        ExecutorService executorService = Executors.newCachedThreadPool();

        Map<String, Boolean> resultMap = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = siteList.stream()
                // Additional site filter (e.g. for testing purposes)
                .filter(site -> StringUtils.isEmpty(siteFilter) || site.contains(siteFilter))
                .map(site -> CompletableFuture
                        // Obtaining URL connection and reading body with limitation-aware service
                        .supplyAsync(() -> limitedConnectionService.parseURL(site), executorService)
                        // Performing search in the body
                        .thenApply(body -> bodySearcherService.searchBody(body, searchTerm))
                        // map result - put true if result found
                        .thenAccept(searchResult -> resultMap.put(site, !StringUtils.isEmpty(searchResult)))
                )
                .collect(Collectors.toList());

        futures.parallelStream().forEach(CompletableFuture::join);

        log.trace("Result map {}", resultMap);
        executorService.shutdown();

        resultFileService.saveResultToFile(resultMap);

    }

}


