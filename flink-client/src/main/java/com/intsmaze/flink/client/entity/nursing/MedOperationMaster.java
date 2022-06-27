/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.intsmaze.flink.client.entity.nursing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 病人手术主记录
 *
 * @author 姜楠
 * @date 2022-03-02 15:54:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedOperationMaster{

    /**
     * 住院号
     */

    private String op;


    private String operationMasterId;

    private String patientId;

    /**
     * 住院次数
     */
    private Integer visitId;

    /**
     * 手术号;一个病人一次住院期间手术的标识，从1开始顺序排列。如果为门诊病人，则在VISIT_ID为0 的所有记录中顺序排列
     */
    private Integer operId;

    /**
     * 病人所在科室;病人所在科室，即申请科室 
     */
    private String deptStayed;

    /**
     * 手术室;手术室科室代码
     */
    private String operatingRoom;

    /**
     * 手术间;手术间号，见手术间床位字典  OPERATING_BED_DICT字典
     */
    private String operatingRoomNo;

    /**
     * 术前主要诊断;病人手术前的诊断描述
     */
    private String diagBeforeOperation;

    /**
     * 病情说明
     */
    private String patientCondition;

    /**
     * 手术等级;指一次手术的综合等级。取值：特、大、中、小
     */
    private String operationScale;

    /**
     * 术后诊断;病人手术后的诊断描述
     */
    private String diagAfterOperation;

    /**
     * 急诊标志;0-择期 1-急诊
     */
    private Integer emergencyIndicator;

    /**
     * 隔离标志;指手术是否需要隔离，1-正常 2-隔离 3-放射
     */
    private Integer isolationIndicator;

    /**
     * 手术类型;1-一般手术 2-急抢救手术 3-术中急抢救
     */
    private String operationClass;

    /**
     * 手术科室;实施手术的科室代码
     */
    private String operatingDept;

    /**
     * 手术者;手术医师姓名
     */
    private String surgeon;

    /**
     * 第一手术助手;第一手术助手姓名
     */
    private String firstAssistant;

    /**
     * 第二手术助手;第二手术助手姓名
     */
    private String secondAssistant;

    /**
     * 第三手术助手;第三手术助手姓名
     */
    private String thirdAssistant;

    /**
     * 第四手术助手;第四手术助手姓名
     */
    private String fourthAssistant;

    /**
     * 麻醉方法
     */
    private String anesthesiaMethod;

    /**
     * 麻醉医师;麻醉医师姓名
     */
    private String anesthesiaDoctor;

    /**
     * 麻醉助手;麻醉助手姓名
     */
    private String anesthesiaAssistant;

    /**
     * 输血者;输血医师姓名
     */
    private String bloodTranDoctor;

    /**
     * 第一台上护士;护士姓名
     */
    private String firstOperationNurse;

    /**
     * 第二台上护士;护士姓名
     */
    private String secondOperationNurse;

    /**
     * 第一供应护士;护士姓名
     */
    private String firstSupplyNurse;

    /**
     * 第二供应护士;护士姓名
     */
    private String secondSupplyNurse;

    /**
     * 手术护士换班标志;0-未换班 1-换班
     */
    private Integer nurseShiftIndicator;

    /**
     * 手术开始日期及时间
     */
    private LocalDateTime startDateTime;

    /**
     * 手术结束日期及时间
     */
    private LocalDateTime endDateTime;

    /**
     * 麻醉满意程度;1-满意 2-不全满意 3-改麻醉
     */
    private Integer satisfactionDegree;

    /**
     * 手术过程顺利标志;1-顺利 0-不顺利
     */
    private Integer smoothIndicator;

    /**
     * 输液量;此处含义重新界定，原来指总入量，单位：毫升
     */
    private BigDecimal inFluidsAmount;

    /**
     * 尿量;此处含义重新界定，原来指总出量，单位：毫升
     */
    private BigDecimal outFluidsAmount;

    /**
     * 失血量;术中失血量，单位：毫升
     */
    private BigDecimal bloodLossed;

    /**
     * 输血量;术中输血量，单位：毫升
     */
    private BigDecimal bloodTransfered;

    /**
     * 录入者
     */
    private String enteredBy;

    /**
     * 第三供应护士
     */
    private String thirdSupplyNurse;

    /**
     * 医嘱提交
     */
    private Integer orderTransfer;

    /**
     * 费用提交
     */
    private Integer chargeTransfer;

    /**
     * 完成标识;1-手术登记完成，完成后不允许再修改
     */
    private Integer endIndicator;

    /**
     * reckGroup
     */
    private String reckGroup;

    /**
     * 手术状态;0-新申请，1-已安排，2-术中，3-PACU，4-术后，5-已提交
     */
    private Integer operStatus;

    /**
     * 麻醉助手2;目前解释为灌注医生1
     */
    private String secondAnesthesiaAssistant;

    /**
     * 麻醉助手3;目前解释为灌注医生1
     */
    private String thirdAnesthesiaAssistant;

    /**
     * 麻醉助手4;目前未使用
     */
    private String fourthAnesthesiaAssistant;

    /**
     * 手术体位
     */
    private String operationPosition;

    /**
     * 器械清点结果;0-对数，1-不对数
     */
    private Integer operationEquipIndicator;

    /**
     * 麻醉医生2
     */
    private String secondAnesthesiaDoctor;

    /**
     * 麻醉医生3
     */
    private String thirdAnesthesiaDoctor;

    /**
     * 其它入量;术中其它入量，单位：毫升
     */
    private BigDecimal otherInAmount;

    /**
     * 其它出量;术中其它出量，单位：毫升
     */
    private BigDecimal otherOutAmount;

    /**
     * 进入手术室日期及时间
     */
    private LocalDateTime inDateTime;

    /**
     * 离开手术室日期及时间
     */
    private LocalDateTime outDateTime;

    /**
     * 手术申请执行科室
     */
    private String reserved1;

    /**
     * bloodWholeSelf
     */
    private BigDecimal bloodWholeSelf;

    /**
     * bloodWhole
     */
    private BigDecimal bloodWhole;

    /**
     * bloodRbc
     */
    private BigDecimal bloodRbc;

    /**
     * bloodPlasm
     */
    private BigDecimal bloodPlasm;

    /**
     * bloodOther
     */
    private BigDecimal bloodOther;

    /**
     * reserved2
     */
    private String reserved2;

    /**
     * specialEquipment
     */
    private String specialEquipment;

    /**
     * specialInfect
     */
    private String specialInfect;

    /**
     * hepatitisIndicator
     */
    private BigDecimal hepatitisIndicator;

    /**
     * anesStartDateTime
     */
    private LocalDateTime anesStartDateTime;

    /**
     * returnDateTime
     */
    private LocalDateTime returnDateTime;

    /**
     * 台次;目前没有使用
     */
    private Integer sequence;

    /**
     * 进入PACU日期及时间
     */
    private LocalDateTime inPacuDateTime;

    /**
     * 离开PACU日期及时间
     */
    private LocalDateTime outPacuDateTime;

    /**
     * operationId
     */
    private String operationId;

    /**
     * reserved3
     */
    private String reserved3;

    /**
     * reserved4
     */
    private String reserved4;

    /**
     * reserved5
     */
    private String reserved5;

    /**
     * reserved6
     */
    private String reserved6;

    /**
     * reserved7
     */
    private String reserved7;

    /**
     * reserved8
     */
    private String reserved8;

    /**
     * reserved9
     */
    private LocalDateTime reserved9;

    /**
     * reserved10
     */
    private LocalDateTime reserved10;

    /**
     * reserved11
     */
    private BigDecimal reserved11;

    /**
     * reserved12
     */
    private BigDecimal reserved12;

    /**
     * bloodReuse
     */
    private BigDecimal bloodReuse;

    /**
     * selfBlood
     */
    private BigDecimal selfBlood;

    /**
     * enteredDatetime
     */
    private LocalDateTime enteredDatetime;

    /**
     * 备注
     */
    private String memo;

    /**
     * 麻醉单编号
     */
    private String anesthesiaId;

    /**
     * xj
     */
    private BigDecimal xj;

    /**
     * ai
     */
    private BigDecimal ai;

    /**
     * at
     */
    private BigDecimal at;

    /**
     * jt
     */
    private BigDecimal jt;

    /**
     * 体表面积(天总)
     */
    private String bodyArea;

    /**
     * 气道与通气 (天总)
     */
    private String gasPipe;

    /**
     * 病人离开的术室情况(天总)
     */
    private String patLeaveShow;

    /**
     * 全麻(天总)
     */
    private String wholeAnes;

    /**
     * 区域阻断(天总)
     */
    private String stopAnesArea;

    /**
     * 阻断药物(天总)
     */
    private String stopAnesAreaMed;

    /**
     * 椎管内(天总)
     */
    private String holePipleAnes;

    /**
     * 阻断技术(天总)
     */
    private String stopAnesAreaTech;

    /**
     * 针号(天总)
     */
    private String pinSize;

    /**
     * 置管上(天总)
     */
    private String pipleUp;

    /**
     * 置管下(天总)
     */
    private String pipleDown;

    /**
     * 刺激神经(天总)
     */
    private String irritateNerve;

    /**
     * 麻醉范围(天总)
     */
    private String anesRange;

    /**
     * 备用药物(天总)
     */
    private String bakMed;

    /**
     * 全麻监测(天总)
     */
    private String watchAnes;

    /**
     * 全麻诱导药物静脉(天总)
     */
    private String allAnesMedLead1;

    /**
     * 全麻诱导药物吸入(天总)
     */
    private String allAnesMedLead2;

    /**
     * 全麻维持药物静脉(天总)
     */
    private String allAnesMedKeep1;

    /**
     * 全麻维持药物吸入(天总)
     */
    private String allAnesMedKeep2;

    /**
     * 胸水(天总)
     */
    private String chestWater;

    /**
     * 腹水(天总)
     */
    private String abdomenWater;

    /**
     * 术前访视日期
     */
    private LocalDateTime inquiryBeforeDate;

    /**
     * 术后随访日期
     */
    private LocalDateTime inquiryAfterDate;

    /**
     * thirdOperationNurse
     */
    private String thirdOperationNurse;

    /**
     * 烟台毓璜顶医院PACU医生
     */
    private String pacuDoctor;

    /**
     * 安阳 胶体液
     */
    private BigDecimal waterJt1;

    /**
     * 安阳 晶体液
     */
    private BigDecimal waterJt2;

    /**
     * 安阳 血小板
     */
    private BigDecimal bloodXb;

    /**
     * 安阳 冷沉淀
     */
    private BigDecimal coolThing;

    /**
     * 安阳 自体回输
     */
    private BigDecimal cryWather;

    /**
     * 安阳 悬浮红细胞
     */
    private BigDecimal redBlood;

    /**
     * 安阳 血浆
     */
    private BigDecimal bloodAmount;

    /**
     * scheduledDateTime
     */
    private LocalDateTime scheduledDateTime;

    /**
     * bedNo
     */
    private String bedNo;

    /**
     * reqDateTime
     */
    private LocalDateTime reqDateTime;

    /**
     * qiekouClass
     */
    private String qiekouClass;

    /**
     * qiekouNumber
     */
    private BigDecimal qiekouNumber;

    /**
     * memo1
     */
    private String memo1;

    /**
     * operationName
     */
    private String operationName;

    /**
     * menZhen
     */
    private String menZhen;

    /**
     * anesthesiaResult
     */
    private String anesthesiaResult;

    /**
     * simpleSick
     */
    private String simpleSick;

    /**
     * isolationNeed
     */
    private String isolationNeed;

    /**
     * danbingzhong
     */
    private String danbingzhong;

    /**
     * yibao
     */
    private String yibao;

    /**
     * firstShiftSupplyNurse
     */
    private String firstShiftSupplyNurse;

    /**
     * firstShiftOperationNurse
     */
    private String firstShiftOperationNurse;

    /**
     * firstShiftSupplyDatetime
     */
    private LocalDateTime firstShiftSupplyDatetime;

    /**
     * firstShiftOperationDatetime
     */
    private LocalDateTime firstShiftOperationDatetime;

    /**
     * anesStartTime
     */
    private LocalDateTime anesStartTime;

    /**
     * anesEndTime
     */
    private LocalDateTime anesEndTime;

    /**
     * induceStartTime
     */
    private LocalDateTime induceStartTime;

    /**
     * induceEndTime
     */
    private LocalDateTime induceEndTime;

    /**
     * pacuStartTime
     */
    private LocalDateTime pacuStartTime;

    /**
     * pacuEndTime
     */
    private LocalDateTime pacuEndTime;

    /**
     * doneDateTime
     */
    private LocalDateTime doneDateTime;

    /**
     * cancelDateTime
     */
    private LocalDateTime cancelDateTime;

    /**
     * analgesicPumps
     */
    private String analgesicPumps;

    /**
     * 术后确认
     */
    private String confirm;

    /**
     * 麻醉手术名称
     */
    private String anesOperation;

    /**
     * 术后确认手术名称
     */
    private String operationNameConfirm;

    /**
     * 导管护士1
     */
    private String firstCatheterNurse;

    /**
     * 导管护士2
     */
    private String secondCatheterNurse;

    /**
     * 护理手术开始时间
     */
    private LocalDateTime startDateTime1;

    /**
     * 护理手术结束时间
     */
    private LocalDateTime endDateTime1;

    /**
     * 是否早开
     */
    private String earlyStart;

    /**
     * 修改类型
     */
    private String updateType;


//    public String getOperationMasterId() {
//        return patientId+visitId+operId;
//    }
//
//    public void setOperationMasterId(String operationMasterId) {
//        this.operationMasterId = operationMasterId;
//    }
}
