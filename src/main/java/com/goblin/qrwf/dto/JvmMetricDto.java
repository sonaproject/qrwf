package com.goblin.qrwf.dto;

import lombok.Data;

import java.util.ArrayList;

/**
 * packageName : com.goblin.qrwf.dto
 * fileName : JvmMetricDto
 * author : goodhyoju
 * date : 2023/05/10 3:39 PM
 * description :
 */
@Data
public class JvmMetricDto {
    private ArrayList<Measurements> measurements;
    @Data static public class Measurements{
        private long value;
    }
}
