package com.intsmaze.flink.client.entity.cdc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdcMsg {

    private String operationMasterId;
    private String infoType;
    private String infoValue;
    private String nodetype;
    private String eventTime;
    private String post;
    private String staffNo;
    private String staffName;
    private String reason;

}
