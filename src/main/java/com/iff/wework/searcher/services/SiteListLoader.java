package com.iff.wework.searcher.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SiteListLoader {


    @Autowired
    private ApplicationContext context;

    public List<String> loadSiteList(String url) {

        List<String> siteList = new ArrayList<>();
        try {
            Resource resource = context.getResource(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            siteList = in.lines()
                    .skip(1)
                    .map(s -> {
                        // In this exact file the easiest is to extract sites from quotes
                        // no need to work with csv-like file
                        String[] recs = s.split("\"");
                        if (recs.length > 1) {
                            return "http://" + recs[1];
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("URL is not resolvable: {}", url, e);
        }
        return siteList;

    }
}
