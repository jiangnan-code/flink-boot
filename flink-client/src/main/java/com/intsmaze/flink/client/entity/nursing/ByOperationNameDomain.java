package com.intsmaze.flink.client.entity.nursing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 手术名称实体类
 *
 * @author liushuzheng
 * @since 2020-7-13 15:28:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ByOperationNameDomain implements Serializable {

    private static final long serialVersionUID = 992661922478940333L;

    /**
     * 操作代码
     */
    private String op;
    /**
     * 手术名称ID
     */
    private String operationNameId;
    /**
     * 手术ID
     */
    private String operationMasterId;

    private String operationId;


    private String patientId;

    private Integer visitId;

    private Integer operId;
    /**
     * 手术名称序号
     */
    private int operationNo;
    /**
     * 手术名称
     */
    private String operation;
    /**
     * 手术操作码
     */
    private String operationCode;
    /**
     * 手术等级
     */
    private String operationScale;
    /**
     * 切口等级
     */
    private String woundGrade;


    public String getOperationId() {
        return patientId+visitId+operId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

}