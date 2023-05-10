package com.goblin.qrwf.dto;

import lombok.Builder;
import lombok.Data;

/**
 * packageName : com.goblin.qrwf.dto
 * fileName : ActuatorDto
 * author : goodhyoju
 * date : 2023/05/10 3:35 PM
 * description :
 */

@Builder
@Data
public class ActuatorDto {
    private String name;
    private long value;
}
