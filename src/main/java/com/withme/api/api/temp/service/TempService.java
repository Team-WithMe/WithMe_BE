package com.withme.api.api.temp.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TempService {

    private final String[] names = {"본훈", "대희", "찬규", "승현", "요셉"};
    private final String[] name1st = {"본 : 본투비", "대 : 대머리 깎아라", "찬 : 찬규님 반갑습니다.", "승 : 승현아", "요 : 요셉님"};
    private final String[] name2nd = {"훈 : 훈훈한 사람...", "희 : 희희^_^", "규 : 뀨 >_<", "현 : 현질 그만해", "셉 : 솔직히 셉은 못하겠네요 죄송합니다."};

    public Map<String, String> test(){

        int randomNum = this.getRandomNum(5);

        Map<String, String> map = new HashMap<>();
        map.put("name", names[randomNum]);
        map.put("1st", name1st[randomNum]);
        map.put("2nd", name2nd[randomNum]);

        return map;
    }

    private int getRandomNum(int max){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return random.nextInt(max);
    }

}
