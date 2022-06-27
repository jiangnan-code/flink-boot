/**
  * Copyright 2022 bejson.com 
  */
package com.intsmaze.flink.client.entity.cdc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdcVo {

    private String op;
    private String before;
    private String after;
    private String db;
    private String tableName;

}