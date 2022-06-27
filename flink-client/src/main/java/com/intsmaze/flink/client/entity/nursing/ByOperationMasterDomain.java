package com.intsmaze.flink.client.entity.nursing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 手术护理主记录 (ByOperationMaster)实体类
 *
 * @author CuiErdan
 * @since 2020-07-03 11:00:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ByOperationMasterDomain implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 唯一标识
     */
    private String operationId;
    /**
     * 患者标识号
     */
    private String patientId;
    /**
     * 手术主记录ID
     */
    private String operationMasterId;
    /**
     * 患者本次住院标识
     */
    private Integer visitId;
    /**
     * 患者本次手术标识
     */
    private Integer operId;
    /**
     * 患者所在院区
     */
    private String hospitalDistrict;

    /**
     * 患者所在院区名称
     */
    private String hospitalDistrictName;
    /**
     * 患者所在科室
     */
    private String deptStayed;
    private String deptStayedCode;
    /**
     * 患者所在病区
     */
    private String wardStayed;
    private String wardStayedCode;
    /**
     * 患者所在病区名称
     */
    private String wardStayedName;
    /**
     * 手术室
     */
    private String operatingRoom;
    
    /**
     * 手术室名字
     */
    private String operatingRoomName;
    /**
     * 手术间
     */
    private String operatingRoomNo;
    /**
     * 台次
     */
    private Integer sequence;
    /**
     * 手术类型
     */
    private String operationClass;
    /**
     * 手术名称
     */
    private List<ByOperationNameDomain> operationName;
    
    /**
     * 术前诊断
     */
    private String diagBeforeOperation;
    private String diagBeforeOperationCode;

    /**
     * 手术等级
     */
    private String operationScale;
    /**
     * 切口类型
     */
    private String woundType;
    /**
     * 切口数量
     */
    private Integer woundNumber;
    /**
     * ASA等级
     */
    private String asaGrade;
    /**
     * 术后诊断
     */
    private String diagAfterOperation;
    private String diagAfterOperationCode;
    /**
     * 手术来源
     */
    private String operationSource;
    /**
     * 急诊标志    1急症  0择期 
     */
    private Integer emergencyIndicator;
    /**
     * 感染标志
     */
    private Integer infectedIndicator;
    /**
     * 放射标志
     */
    private Integer radiateIndicator;
    /**
     * 隔离标志  1 隔离 0 正常
     */
    private Integer isolationIndicator;
    /**
     * 手术科室
     */
    private String operatingDept;
    
    /**
     * 手术科室名称
     */
    private String operatingDeptName;
    
    /**
     * 手术者
     */
    private String surgeon;
    /**
     * 手术者工号
     */
    private String surgeonNo;
    /**
     * 第一手术助手
     */
    private String firstAssistant;
    /**
     * 第一手术助手工号
     */
    private String firstAssistantNo;
    /**
     * 第二手术助手
     */
    private String secondAssistant;
    /**
     * 第二手术助手工号
     */
    private String secondAssistantNo;
    /**
     * 第三手术助手
     */
    private String thirdAssistant;
    /**
     * 第三手术助手工号
     */
    private String thirdAssistantNo;
    /**
     * 第四手术助手
     */
    private String fourthAssistant;
    /**
     * 第四手术助手工号
     */
    private String fourthAssistantNo;
    /**
     * 麻醉方法
     */
    private String anesthesiaMethod;
    /**
     * 麻醉医师
     */
    private String anesthesiaDoctor;
    /**
     * 麻醉医师工号
     */
    private String anesthesiaDoctorNo;
    /**
     * 麻醉助手
     */
    private String anesthesiaAssistant;
    /**
     * 麻醉助手工号
     */
    private String anesthesiaAssistantNo;
    /**
     * 第二麻醉助手
     */
    private String secondAnesAssistant;
    /**
     * 第二麻醉助手工号
     */
    private String secondAnesAssistantNo;
    /**
     * 第三麻醉助手
     */
    private String thirdAnesAssistan;
    /**
     * 第三麻醉助手工号
     */
    private String thirdAnesAssistanNo;
    /**
     * 第四麻醉助手
     */
    private String fourthAnesAssistant;
    /**
     * 第四麻醉助手工号
     */
    private String fourthAnesAssistantNo;
    /**
     * 第一麻醉护士
     */
    private String firstAnesNurse;
    /**
     * 第一麻醉护士工号
     */
    private String firstAnesNurseNo;
    /**
     * 第二麻醉护士
     */
    private String secondAnesNurse;
    /**
     * 第二麻醉护士工号
     */
    private String secondAnesNurseNo;
    /**
     * 第三麻醉护士
     */
    private String thirdAnesNurse;
    /**
     * 第三麻醉护士工号
     */
    private String thirdAnesNurseNo;
    /**
     * 灌注医生
     */
    private String cpbDoctor;
    /**
     * 灌注医生工号
     */
    private String cpbDoctorNo;
    /**
     * 第一灌注医生助手
     */
    private String firstCpbAssistant;
    /**
     * 第一灌注医生助手工号
     */
    private String firstCpbAssistantNo;
    /**
     * 第二灌注医生助手
     */
    private String secondCpbAssistant;
    /**
     * 第二灌注医生助手工号
     */
    private String secondCpbAssistantNo;
    /**
     * 第三灌注医生助手
     */
    private String thirdCpbAssistant;
    /**
     * 第三灌注医生助手工号
     */
    private String thirdCpbAssistantNo;
    /**
     * 第四灌注医生助手
     */
    private String fourthCpbAssistant;
    /**
     * 第四灌注医生助手工号
     */
    private String fourthCpbAssistantNo;
    /**
     * 第一洗手护士
     */
    private String firstOperationNurse;
    /**
     * 第一洗手护士工号
     */
    private String firstOperationNurseNo;
    /**
     * 第二洗手护士
     */
    private String secondOperationNurse;
    /**
     * 第二洗手护士工号
     */
    private String secondOperationNurseNo;
    
    /**
     * 第3洗手护士
     */
    private String thirdOperationNurse;
    /**
     * 第3洗手护士工号
     */
    private String thirdOperationNurseNo;
    /**
     * 第4洗手护士
     */
    private String fourthOperationNurse;
    /**
     * 第4洗手护士工号
     */
    private String fourthOperationNurseNo;
    
    
    
    
    /**
     * 第一巡回护士
     */
    private String firstSupplyNurse;
    /**
     * 第一巡回护士工号
     */
    private String firstSupplyNurseNo;

    /**
     * 第二巡回护士
     */
    private String secondSupplyNurse;
    /**
     * 第二巡回护士工号
     */
    private String secondSupplyNurseNo;
    
    /**
     * 第3巡回护士
     */
    private String thirdSupplyNurse;
    /**
     * 第3巡回护士工号
     */
    private String thirdSupplyNurseNo;
    
    /**
     * 第4巡回护士
     */
    private String fourthSupplyNurse;
    /**
     * 第4巡回护士工号
     */
    private String fourthSupplyNurseNo;
    
    
    
    

    /**
     * 输血医师
     */
    private String bloodTransfuser;
    /**
     * 输血医师工号
     */
    private String bloodTransfuserNo;
    /**
     * PACU医生
     */
    private String pacuDoctor;
    /**
     * PACU医生工号
     */
    private String pacuDoctorNo;
    /**
     * PACU第一助手
     */
    private String firstPacuAssistant;
    /**
     * PACU第一助手工号
     */
    private String firstPacuAssistantNo;
    /**
     * PACU第二助手
     */
    private String secondPacuAssistant;
    /**
     * PACU第二助手工号
     */
    private String secondPacuAssistantNo;
    /**
     * PACU第一护士
     */
    private String firstPacuNurse;
    /**
     * PACU第一护士工号
     */
    private String firstPacuNurseNo;
    /**
     * PACU第二护士
     */
    private String secondPacuNurse;
    /**
     * PACU第二护士工号
     */
    private String secondPacuNurseNo;
    /**
     * 体位
     */
    private String operationPosition;
    /**
     * 手术申请日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date reqDateTime;
    /**
     * 手术安排日期
     */
    
    private Date scheduledDateTime;
    /**
     * 手术护士换班标志
     */
    private String nurseShiftIndicator;
    /**
     * 手术状态
     */
    private Integer operationStatus;
    /**
     * 患者去向
     */
    private String patWhereaborts;
    /**
     * 手术过程顺利标志
     */
    private String smoothIndicator;
    /**
     * 录入人
     */
    private String enteredBy;
    /**
     * 手术预计时长
     */
    private String estimatedOperationTime;
    /**
     * 手术调整预计结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date estimatedEndTime;
    /**
     * 手术开始日期及时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startDateTime;
    /**
     * 手术结束日期及时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endDateTime;


    /**
     * 入院日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date admissionDateTime;
    /**
     * 入科日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date adnWardDateTime;
    /**
     * 主要诊断
     */
    private String diagnosis;
    /**
     * 手术日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operatingDate;
    /**
     * 联系人
     */
    private String nextOfKin;
    /**
     * 与联系人关系
     */
    private String relationship;
    /**
     * 联系人电话
     */
    private String nextOfKinPhone;
    
    /**
     * 缴费类别
     */
    private String chargeType;
   
    
    //-----------手术报表中的数据---------------
    /**
     * 手术护士
     */
    private String nurseName;
    /**
     * 开始日期（String）
     */
    private String startDate;
    
    /**
     * 结束日期（String）
     */
    private String endDate;
    /**
     * 开始日期(Date)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dStartDate;
    
    /**
     * 结束日期(Date)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dEndDate;
    
    /**
     * 患者所在科室名称
     */
    private String deptStayedName;
    /**
     * 手术名称
     */
    private String operationNameStr;
    /**
     * 开始年龄
     */
    private Integer startAge;
    /**
     * 结束年龄
     */
    private Integer endAge;


    /**
     * 患者年龄(String类型)
     */
    private String ageString;
    /**
     * 入手术室时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date enterOperatingRoomDate;
    
    /**
     * 出手术室时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date leaveOperatingRoomDate;
    
    /**
     * 麻醉开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date anesthesiaStartDate;
    
    /**
     * 麻醉结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date anesthesiaEndDate;
    
    /**
     * 手术开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operatingStartDate;
    
    /**
     * 手术结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operatingEndDate;
    /**
     * 入复苏室时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date enterPACU;
    /**
     * 出复苏室时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date leavePACU;
    /**
     * 取消手术原因
     */
    private String cancledReson;
    /**
     * 巡回护士
     */
    private String supplyNurse;
    
    /**
     * 	洗手护士
     */
    private String operationNurse;
    
    /** 出血量 */
    private Long bloodLossed;
    
    /** 用血者特异性抗体检测结果 */
    private String unexpectedAntibody;
    /**
     * 输血量 单位：毫升
     */
    private Long  bloodTransfused;
    /**
     * 输血内容
     */
    private String bloodContents;
    /**
     * 输液量 单位：毫升
     */
    private Long infusionVolume;
    /**
     * 尿量 单位：毫升
     */
    private Long urineAmount;
    /**
     * 其它出量 单位：毫升
     */
    private Long otherOutAmount;
    
    private String owner;
    
    private Integer operStatus;
    private String syncType;
    
    /**
     * 手术部
     * 
     * 手术部
     * 		住院手术室：operation_room=2401   operation_room_no=01,02~16   1
                     门诊手术室：operation_room=2401   operation_room_no=M1,M2~M9   2
                     急诊手术室：operation_room=4003								   3
     */
    private String operationRoomType;
    
    private String status;
    
    
    
    private Date doctorInDateIiem ;//'手术医生入室时间' ,
    private String roomTemperature;//'手术间温度' ,
    private String roomHumidity;//'手术间湿度' ,
    private Integer isAirdisInfection ;//'是否空气消毒' ,
    private Integer isSurfaceWiping ;//'是否物表擦拭' ,
    private Integer isAntibioticsInUse;//'术中使用抗生素' ,
    private String remark;// '手术术信息备注'  
    private String operationMark;
    private String billedOperationName;
    private String notesOnOperation;
    //感染筛查
    private String infectionScreening;
    private String surgicalSite;
    
    
    
    
    
}