package com.intsmaze.flink.client.entity.nursing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 手术参与者实体类
 *
 * @author liushuzheng
 * @since 2020-7-13 15:28:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ByOperationStaffDomain implements Serializable {

    private static final long serialVersionUID = 992661922478940333L;
    /**
    * 手术参与人员ID
    */
    private String staffId;
    /**
    * 手术ID
    */
    private String operationId;
    /**
    * 手术参与人员岗位
    */
    private String post;
    /**
    * 人员姓名
    */
    private String staffName;
    /**
    * 人员工号
    */
    private String staffNo;
    /**
    * 参与开始时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Object startWorkingTime;
    /**
    * 参与结束时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endWorkingTime;
    
    private String  imageBase64;
    //最近一次签名时间
    private String createTime;
    //最近二次签名时间
    private String createTime2;
    
    private String idNo;


    public ByOperationStaffDomain(String post, String staffName, String staffNo) {
        this.post = post;
        this.staffName = staffName;
        this.staffNo = staffNo;
    }
}