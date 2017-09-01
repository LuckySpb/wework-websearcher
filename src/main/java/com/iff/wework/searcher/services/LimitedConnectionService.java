package com.iff.wework.searcher.services;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.Semaphore;

@Service
@Slf4j
public class LimitedConnectionService {

    @Value("${searcher.connection-limit: 1}")
    private Integer connectionLimit;

    private Semaphore semaphore;

    @PostConstruct
    private void init() {
        semaphore = new Semaphore(connectionLimit);
    }

    /**
     * Connects to URL and returns body of html page
     */
    public String parseURL(String url) {
        try {
            log.trace("Starting parse. Available permits: {}", semaphore.availablePermits());
            semaphore.acquire();
            log.trace("Parsing url: {}", url);
            return Jsoup.connect(url).get().body().text();
        } catch (IOException | InterruptedException e) {
            log.error("Error in parsing url: {}. Error: {}", url, e.getLocalizedMessage());
            return null;
        } finally {
            semaphore.release();
        }

    }


}
