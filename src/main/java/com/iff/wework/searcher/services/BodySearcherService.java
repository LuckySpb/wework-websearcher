package com.iff.wework.searcher.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class BodySearcherService {

    /**
     * Returns first found group by regexp
     */
    public String searchBody(String text, String regex) {
        if (text != null) {
            log.trace("Searching body: {}", text);

            Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
            if (matcher.find()) {
                String group = matcher.group();
                return group;
            }
        }
        return null;
    }
}
