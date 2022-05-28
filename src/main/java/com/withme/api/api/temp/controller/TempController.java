package com.withme.api.api.temp.controller;

import com.withme.api.api.temp.service.TempService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TempController {

    private final TempService service;

    @GetMapping("/temp")
    public Map<String, String> test(){
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");

        return service.test();
    }

}