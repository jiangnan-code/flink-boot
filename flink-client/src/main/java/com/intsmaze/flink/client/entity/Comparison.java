package com.intsmaze.flink.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comparison {
    //ID
    private String operationMasterId;
    //字段
    private String Field;
    //字段旧值
    private Object before;
    //字段新值
    private Object after;

    private String op;
}
