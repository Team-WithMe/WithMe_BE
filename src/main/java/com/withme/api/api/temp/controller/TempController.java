package com.withme.api.api.temp.controller;

import com.withme.api.api.temp.service.TempService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/temp")
public class TempController {

    private final TempService service;

    @GetMapping("/test")
    public Map<String, String> test(){
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");

        return service.test();
    }

}