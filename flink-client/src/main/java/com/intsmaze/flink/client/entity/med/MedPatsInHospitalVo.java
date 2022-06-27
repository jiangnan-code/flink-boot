package com.intsmaze.flink.client.entity.med;

import lombok.Data;

import java.util.Date;


/**
 * 患者基本信息视图对象 med_pats_in_hospital
 *
 * @author jiangnan
 * @date 2022-06-09
 */
@Data
public class MedPatsInHospitalVo {

    private static final long serialVersionUID = 1L;

    
    private String patientId;

    
    private Long visitId;

    
    private Long depId;

    
    private String wardCode;

    private String wardName;

    
    private String deptCode;

    private String deptName;

    
    private String bedNo;

    
    private Date admissionDateTime;

    
    private Date admWardDateTime;

    
    private String diagnosis;

    
    private String patientCondition;

    
    private String nursingClass;

    
    private String doctorInCharge;

    
    private Date operatingDate;

    
    private Date billingDateTime;

    
    private Long prepayments;

    
    private Long totalCosts;

    
    private Long totalCharges;

    
    private String guarantor;

    
    private String guarantorOrg;

    
    private String guarantorPhoneNum;

    
    private Date billCheckedDateTime;

    
    private Long settledIndicator;

    
    private String reserved01;

    
    private String reserved02;

    
    private String reserved03;

    
    private String reserved04;

    
    private String reserved05;

    
    private String reserved06;

    
    private String reserved07;

    
    private String reserved08;

    
    private String reserved09;

    
    private String reserved10;

    
    private Date reservedDate01;

    
    private Date reservedDate02;

    
    private Date startDateTime;

    
    private Long frequencyNurse;

    
    private String nurseInCharge;


}
