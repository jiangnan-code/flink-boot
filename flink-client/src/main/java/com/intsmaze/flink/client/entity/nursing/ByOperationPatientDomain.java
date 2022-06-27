package com.intsmaze.flink.client.entity.nursing;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ByOperationPatientDomain  implements Serializable {

// 手术患者信息

    /**
     * 记录ID
     */
    private String patVisitId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 出生日期
     */
    private Date dateOfBirth;
    /**
     * 国籍
     */
    private String citizenship;
    /**
     * 民族
     */
    private String nation;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 患者来源
     */
    private String patientSource;
    /**
     * 床号
     */
    private String bedNo;

    /**
     * 患者年龄
     */
    private Integer age;

    /**
     * 住院标识
     */
    private String inpNo;

    /**
     * 病情说明
     */
    private String patientCondition;

    /**
     * 护理等级
     */
    private String nursingClass;

    /**
     * 经治医生
     */
    private String doctorInCharge;

    /**
     * 主管护师
     */
    private String nurseInCharge;

    /**
     * 血型
     */
    private String bloodType;
    /**
     * Rh血型
     */
    private String bloodTypeRh;
    /**
     * 身高
     */
    private BigDecimal bodyHeight;
    /**
     * 体重
     */
    private BigDecimal bodyWeight;
}
