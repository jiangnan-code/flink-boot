package com.intsmaze.flink.client.entity.nursing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NursingParameters {
    private String interfaceUrl;
    private String nursingInterfaceUrl;
    private List<String> U_OIList;
    private List<String> U_OTList;
    private List<String> U_SUList;
    private String isSendMsg;
    private Integer hour;
    private String gmt;
}
