package com.brandpark.simplepostsboard.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderBase {
    DATE_DESC("최신순", "createdDate", "desc"),
    VIEW_DESC("조회순", "viewCount", "desc");

    private final String name;
    private final String value;
    private final String dir;
}

