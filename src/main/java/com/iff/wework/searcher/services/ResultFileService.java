package com.iff.wework.searcher.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResultFileService {

    @Value("${searcher.result-file: results.txt}")
    private String resultFilename;

    public void saveResultToFile(Map<String, Boolean> resultMap) {

        List<String> lines = resultMap.entrySet().stream()
                .map(e -> e.getKey() + "," + e.getValue())
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(resultFilename), lines);
        } catch (IOException e) {
            log.error("Can't write to file {}", resultFilename, e);
        }

    }


}
