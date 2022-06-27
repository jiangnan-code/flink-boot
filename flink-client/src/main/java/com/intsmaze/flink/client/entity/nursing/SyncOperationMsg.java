package com.intsmaze.flink.client.entity.nursing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncOperationMsg {
    private String method;
    private String msg;
}
