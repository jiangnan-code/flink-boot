package com.intsmaze.flink.client.entity.med;

import lombok.Data;

import java.util.Date;


/**
 * 患者基本信息视图对象 med_pat_master_index
 *
 * @author jiangnan
 * @date 2022-06-09
 */
@Data
public class MedPatMasterIndexVo {

    private static final long serialVersionUID = 1L;

    private String patientId;

    private String inpNo;

    private String name;

    private String namePhonetic;

    private String sex;

    private String gender;

    private Integer age;

    private Date dateOfBirth;

    private String birthPlace;

    private String citizenship;

    private String nation;

    private String idNo;

    private String identity;

    private String chargeType;

    private String unitInContract;

    private String mailingAddress;

    private String zipCode;

    private String phoneNumberHome;

    private String phoneNumberBusiness;

    private String nextOfKin;

    private String relationship;

    private String nextOfKinAddr;

    private String nextOfKinZipCode;

    private String nextOfKinPhone;

    private Date lastVisitDate;

    private Long vipIndicator;

    private Date createDate;

    private String operator;

    private MedPatsInHospitalVo medPatsInHospitalVo;


}
