package com.withme.api.api.temp.controller;

import com.withme.api.api.temp.service.TempService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class TempController {

    private final TempService service;

    @GetMapping("/temp")
    public Map<String, String> test(){
        return service.test();
    }
}