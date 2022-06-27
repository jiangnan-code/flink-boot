package com.intsmaze.flink.client.task.nursing;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;

import com.intsmaze.flink.client.constant.CdcOpConstant;
import com.intsmaze.flink.client.constant.DateConstant;
import com.intsmaze.flink.client.constant.SyncOperationInfoV2Constant;
import com.intsmaze.flink.client.constant.SysPostConstants;
import com.intsmaze.flink.client.entity.Comparison;
import com.intsmaze.flink.client.entity.cdc.CdcMsg;
import com.intsmaze.flink.client.entity.cdc.CdcVo;
import com.intsmaze.flink.client.entity.med.MedDeptDictVo;
import com.intsmaze.flink.client.entity.med.MedHisUsersVo;
import com.intsmaze.flink.client.entity.med.MedPatMasterIndexVo;
import com.intsmaze.flink.client.entity.nursing.*;
import com.intsmaze.flink.client.utils.CompareObjUtil;
import com.intsmaze.flink.client.utils.DateUtils;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.ververica.cdc.debezium.utils.TemporalConversions;
import io.debezium.data.Envelope;
import io.debezium.time.MicroTimestamp;
import io.debezium.time.NanoTimestamp;
import io.debezium.time.Timestamp;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.data.TimestampData;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.eclipse.jetty.util.StringUtil;

import java.time.LocalDateTime;
import java.util.*;

import static com.intsmaze.flink.client.utils.humpToLine.humpToLine;

public class NursingSystemDeserializationSchema implements DebeziumDeserializationSchema<String> {

    List<String> U_OIList = new ArrayList<>();
    List<String> U_OTList = new ArrayList<>();
    List<String> U_SUList = new ArrayList<>();
    //    List<String> U_OSList = new ArrayList<>();
    List<String> N_ONList = new ArrayList<>();

    String interfaceUrl = "";

    String nursingInterfaceUrl = "";

    String isSendMsg = "";

    String gmt = "GMT+0";

    Integer hour = 0;
    Map<String, String> postMap = new HashMap();

    public NursingSystemDeserializationSchema(NursingParameters nursingParameters) {

        interfaceUrl = nursingParameters.getInterfaceUrl();

        nursingInterfaceUrl = nursingParameters.getNursingInterfaceUrl();

        isSendMsg = nursingParameters.getIsSendMsg();

        gmt = nursingParameters.getGmt();

        hour = nursingParameters.getHour();

        if (nursingParameters.getU_OIList().size() > 0) {
            U_OIList = nursingParameters.getU_OIList();
        } else {
            U_OIList.add("patientId");
            U_OIList.add("visitId");
            U_OIList.add("operId");
            U_OIList.add("deptStayed");
            U_OIList.add("operatingRoom");
            U_OIList.add("operatingRoomNo");
            U_OIList.add("sequence");
            U_OIList.add("operationClass");
            U_OIList.add("sequence");
            U_OIList.add("diagBeforeOperation");
            U_OIList.add("patientCondition");
            U_OIList.add("operationScale");
            U_OIList.add("woundType");
            U_OIList.add("scheduledDateTime");
            U_OIList.add("emergencyIndicator");
            U_OIList.add("patientCondition");
        }

        if (nursingParameters.getU_OTList().size() > 0) {
            U_OTList = nursingParameters.getU_OTList();
        } else {
            U_OTList.add("startDateTime");
            U_OTList.add("endDateTime");
            U_OTList.add("anesStartTime");
            U_OTList.add("anesEndTime");
            U_OTList.add("inDateTime");
            U_OTList.add("outEateTime");
        }

        if (nursingParameters.getU_SUList().size() > 0) {
            U_SUList = nursingParameters.getU_SUList();
        } else {
            U_SUList.add("surgeon");
            U_SUList.add("firstSupplyNurse");
            U_SUList.add("secondSupplyNurse");
            U_SUList.add("firstOperationNurse");
            U_SUList.add("secondOperationNurse");
            U_SUList.add("secondAssistant");
            U_SUList.add("thirdAssistant");
            U_SUList.add("fourthAssistant");
            U_SUList.add("anesthesiaDoctor");
            U_SUList.add("anesthesiaAssistant");
            U_SUList.add("secondAnesAssistant");
            U_SUList.add("thirdAnesAssistan");
            U_SUList.add("fourthAnesAssistant");
            U_SUList.add("firstAnesNurse");
            U_SUList.add("secondAnesNurse");
            U_SUList.add("thirdAnesNurse");
            U_SUList.add("cpbDoctor");
            U_SUList.add("firstCpbAssistant");
            U_SUList.add("secondCpbAssistant");
            U_SUList.add("thirdCpbAssistant");
            U_SUList.add("fourthCpbAssistant");
            U_SUList.add("bloodTransfuser");
            U_SUList.add("pacuDoctor");
            U_SUList.add("firstPacuAssistant");
            U_SUList.add("firstPacuAssistant");
            U_SUList.add("firstPacuNurse");
            U_SUList.add("secondPacuNurse");
            U_SUList.add("firstAssistant");
        }

        N_ONList.add("operStatus");


        postMap.put("surgeon", "0031");
        postMap.put("firstSupplyNurse", "0011");
        postMap.put("secondSupplyNurse", "0012");
        postMap.put("firstOperationNurse", "0021");
        postMap.put("secondOperationNurse", "0022");
        postMap.put("firstAssistant", "0032");
        postMap.put("secondAssistant", "0033");
        postMap.put("thirdAssistant", "0034");
        postMap.put("fourthAssistant", "0035");
        postMap.put("anesthesiaDoctor", "0041");
        postMap.put("anesthesiaAssistant", "0042");
        postMap.put("secondAnesAssistant", "0043");
        postMap.put("thirdAnesAssistan", "0044");
        postMap.put("fourthAnesAssistant", "0045");
        postMap.put("firstAnesNurse", "0011");
        postMap.put("secondAnesNurse", "0052");
        postMap.put("thirdAnesNurse", "0053");
        postMap.put("cpbDoctor", "0061");
        postMap.put("firstCpbAssistant", "0062");
        postMap.put("secondCpbAssistant", "0063");
        postMap.put("thirdCpbAssistant", "0064");
        postMap.put("fourthCpbAssistant", "0065");
        postMap.put("bloodTransfuser", "0071");
        postMap.put("pacuDoctor", "0081");
        postMap.put("firstPacuAssistant", "0082");
        postMap.put("firstPacuAssistant", "0083");
        postMap.put("firstPacuNurse", "0091");
        postMap.put("secondPacuNurse", "0092");

    }

    /**
     * {
     * "db":"",
     * "tableName":"",
     * "before":{"id":"1001","name":""...},
     * "after":{"id":"1001","name":""...},
     * "op":""
     * }
     */

    /**
     * 主要关注在MED_OPERATION_MASTER  上的数据变化
     * (1) 手术排班:
     * 麻醉系统在MED_OPERATION_MASTER  上插入数据，
     * 消息代码：N001
     * <p>
     * (2)  手术取消
     * 麻醉系统在MED_OPERATION_MASTER    删除一条记录
     * 消息代码：D001
     * <p>
     * (3)  手术信息变化
     * 麻醉系统MED_OPERATION_MASTER  中字段发生变化
     * 变化可能的值有：
     * 基本信息： 包括但并不限于，可以配置
     * 消息代码： U_OI
     * <p>
     * (4)  手术名称新增：
     * MED_OPERATION_NAME  上的INSERT
     * 消息代码：N_ON
     *
     * @param sourceRecord
     * @param collector
     * @throws Exception
     */
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {
//        System.out.println(aa+"aa");
        //创建JSON对象用于封装结果数据
        JSONObject result = new JSONObject();

        //获取库名&表名
        String topic = sourceRecord.topic();
        String[] fields = topic.split("\\.");
        result.put("db", fields[1]);
        result.put("tableName", fields[2]);

        //获取before数据
        Struct value = (Struct) sourceRecord.value();
        Struct before = value.getStruct("before");
        JSONObject beforeJson = new JSONObject();
        if (before != null) {
            //获取列信息
            Schema schema = before.schema();
            List<Field> fieldList = schema.fields();

//            before.schema().fields().forEach(f-> beforeJson.put(f.name(), before.get(f)));
            for (Field field : fieldList) {
                beforeJson.put(field.name(), before.get(field));
            }
        }
        result.put("before", beforeJson);

        //获取after数据
        Struct after = value.getStruct("after");
        JSONObject afterJson = new JSONObject();
        if (after != null) {
            //获取列信息
            Schema schema = after.schema();
            List<Field> fieldList = schema.fields();
//            after.schema().fields().forEach(f-> afterJson.put(f.name(), after.get(f)));
            for (Field field : fieldList) {
                afterJson.put(field.name(), after.get(field));
            }
        }
        result.put("after", afterJson);

        Long updateTime = System.currentTimeMillis();
        //获取操作类型
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        result.put("op", operation);
        CdcVo cdcVo = JSONUtil.toBean(result.toJSONString(), CdcVo.class);
        System.out.println("==============" + cdcVo.toString());
//        result.put("updateTime", updateTime);
        List<SyncOperationMsg> msgList = getjson(cdcVo);
        for (SyncOperationMsg msg : msgList){
            collector.collect(JSONObject.toJSONString(msg));
        }
    }

    public List<SyncOperationMsg> getjson(CdcVo cdc) {
        List<Comparison> list = new ArrayList<>();
        List<SyncOperationMsg> msgList = new ArrayList<>();
        String tablename = cdc.getTableName();
        String op = cdc.getOp();
        String after = cdc.getAfter();
        if (StrUtil.isNotBlank(after) && after.equals("{}")) {
            after = cdc.getBefore();
        }
        JSONObject beforeJsonObject = new JSONObject();
        JSONObject afterJsonObject = JSONObject.parseObject(after);

        if (StringUtil.isNotBlank(cdc.getBefore())) {
            beforeJsonObject = JSONObject.parseObject(cdc.getBefore());
        }
        //更新之前
        String beforeStr = JSONUtil.toJsonStr(formatKey(beforeJsonObject, false));
        //更新之后
        String afterStr = JSONUtil.toJsonStr(formatKey(afterJsonObject, false));
        System.out.println("afterStr" + afterStr);
        if (StringUtil.isNotBlank(tablename)) {
            if (tablename.equals("MED_OPERATION_NAME")) {
                ByOperationNameDomain beforeByOperationNameDomain = JSONUtil.toBean(beforeStr, ByOperationNameDomain.class);

                ByOperationNameDomain afterByOperationNameDomain = JSONUtil.toBean(afterStr, ByOperationNameDomain.class);
                if (op.equals(CdcOpConstant.CREATE)) {
                    afterByOperationNameDomain.setOp(SyncOperationInfoV2Constant.CREATEOPERATIONINFO);
                    afterByOperationNameDomain.setOperationMasterId(afterByOperationNameDomain.getPatientId() + afterByOperationNameDomain.getVisitId() + afterByOperationNameDomain.getOperId());
                    msgList.add(new SyncOperationMsg(afterByOperationNameDomain.getOp(),JSONObject.toJSONString(afterByOperationNameDomain)));
                } else if (op.equals(CdcOpConstant.UPDATE)) {
                    afterByOperationNameDomain.setOp(SyncOperationInfoV2Constant.CREATEOPERATIONINFO);
                    afterByOperationNameDomain.setOperationMasterId(afterByOperationNameDomain.getPatientId() + afterByOperationNameDomain.getVisitId() + afterByOperationNameDomain.getOperId());
                    msgList.add(new SyncOperationMsg(afterByOperationNameDomain.getOp(), JSONObject.toJSONString(afterByOperationNameDomain)));
                }
                System.out.println(afterByOperationNameDomain.toString());
            } else if (tablename.equals("MED_OPERATION_MASTER")) {
                MedOperationMaster afterMedOperationMaster = JSONUtil.toBean(afterStr, MedOperationMaster.class);
                afterMedOperationMaster.setOperationMasterId(afterMedOperationMaster.getPatientId() + afterMedOperationMaster.getVisitId() + afterMedOperationMaster.getOperId());
                String operationMasterId = afterMedOperationMaster.getOperationMasterId();
                if (op.equals(CdcOpConstant.CREATE)) {
                    afterMedOperationMaster.setOp(SyncOperationInfoV2Constant.CREATEMASTER);
                    ByOperationMasterDomain byOperationMasterDomain = JSONUtil.toBean(afterStr, ByOperationMasterDomain.class);
                    byOperationMasterDomain.setRemark(afterMedOperationMaster.getMemo());
                    //格式化staff
                    byOperationMasterDomain = getOperationMasterStaff(byOperationMasterDomain);
                    //根据患者ID号查询患者基本信息
                    MedPatMasterIndexVo medPatMasterIndexVo = getOperationPatient(byOperationMasterDomain.getPatientId());
                    ByOperationPatientDomain byOperationPatientDomain = BeanUtil.toBean(medPatMasterIndexVo, ByOperationPatientDomain.class);
                    if (medPatMasterIndexVo.getMedPatsInHospitalVo() != null) {
                        byOperationPatientDomain.setBedNo(medPatMasterIndexVo.getMedPatsInHospitalVo().getBedNo());
                        byOperationPatientDomain.setDoctorInCharge(medPatMasterIndexVo.getMedPatsInHospitalVo().getDoctorInCharge());
                        byOperationMasterDomain.setDeptStayedName(medPatMasterIndexVo.getMedPatsInHospitalVo().getDeptName());
                        byOperationMasterDomain.setOperatingDeptName(medPatMasterIndexVo.getMedPatsInHospitalVo().getDeptName());
                    }
                    List<ByOperationStaffDomain> byOperationStaffDomainList = new ArrayList<>();
                    byOperationStaffDomainList = getOperationStaffList(byOperationMasterDomain);
                    N001 n001 = new N001();
                    n001.setOperationMasterId(operationMasterId);
                    n001.setOperationName(null);
                    Date scheduledDateTime = DateUtils.getDateTime2(byOperationMasterDomain.getScheduledDateTime(), 0,hour);
                    byOperationMasterDomain.setScheduledDateTime(scheduledDateTime);
                    n001.setOperationMaster(byOperationMasterDomain);
                    n001.setOperationStaff(byOperationStaffDomainList);
                    n001.setOperationPatient(byOperationPatientDomain);
                    msgList.add(new SyncOperationMsg(afterMedOperationMaster.getOp(), JSONObject.toJSONString(n001)));
                } else if (op.equals(CdcOpConstant.DELETE)) {
                    afterMedOperationMaster.setOp(SyncOperationInfoV2Constant.CANCELMASTER);
                    CdcMsg cdcMsg = new CdcMsg();
                    cdcMsg.setReason(null);
                    cdcMsg.setOperationMasterId(operationMasterId);
                    msgList.add(new SyncOperationMsg(afterMedOperationMaster.getOp(), JSONObject.toJSONString(cdcMsg)));
                } else if (op.equals(CdcOpConstant.UPDATE)) {
//                   boolean dd =  MyJSONUtil.compareTwoJSONObject(beforeJsonObject, afterJsonObject);
//                   System.out.println(dd);
                    try {
                        MedOperationMaster beforeMedOperationMaster = JSONUtil.toBean(beforeStr, MedOperationMaster.class);
                        beforeMedOperationMaster.setOperationMasterId(beforeMedOperationMaster.getPatientId() + beforeMedOperationMaster.getVisitId() + beforeMedOperationMaster.getOperId());
                        list = CompareObjUtil.compareObj(beforeMedOperationMaster, afterMedOperationMaster);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (Comparison c : list) {
                        String field = c.getField().toString();
                        String afterValue = "";
                        if (!StrUtil.isBlankIfStr(c.getAfter())) {
                            afterValue = c.getAfter().toString();
                        }
                        if (U_OIList.contains(c.getField())) {//手术基本信息  U_OI
                            c.setOp("U_OI");
                            CdcMsg cdcMsg = new CdcMsg();
                            cdcMsg.setOperationMasterId(operationMasterId);
                            cdcMsg.setInfoType(humpToLine(field));
                            cdcMsg.setInfoValue(afterValue);
                            msgList.add(new SyncOperationMsg(c.getOp(), JSONObject.toJSONString(cdcMsg)));
                        } else if (U_OTList.contains(c.getField())) {//时间信息修改  U_OT
                            c.setOp("U_OT");
                            CdcMsg cdcMsg = new CdcMsg();
                            cdcMsg.setOperationMasterId(operationMasterId);
                            cdcMsg.setNodetype(getNodetype(c.getField().toString()));
//                            Date date = DateUtil.parse(afterValue.replace("T"," "));
//                            String date2 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.dateTime(date, 0));
//                            Date date = new Date();
//                            DateUtil.formatDateTime(date)
                            String datetime = afterValue.replace("T", " ");
                            if(datetime.length()<19){
                                datetime+=":00";
                            }
                            if(datetime.length()>=19){
                                Date date = DateUtil.parse(datetime);
                                datetime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getDateTime2(date, 0,hour));
                            }
                            cdcMsg.setEventTime(datetime);
                            msgList.add(new SyncOperationMsg(c.getOp(), JSONObject.toJSONString(cdcMsg)));
                        } else if (U_SUList.contains(c.getField())) {//手术人员信息  U_SU
                            MedHisUsersVo medHisUsersVo = getMedHisUsersVo(afterValue);
                            CdcMsg cdcMsg = new CdcMsg();
                            cdcMsg.setOperationMasterId(operationMasterId);
                            if (medHisUsersVo != null) {
                                cdcMsg.setPost(postMap.get(c.getField().toString()));
                                cdcMsg.setStaffName(medHisUsersVo.getUserName());
                                cdcMsg.setStaffNo(medHisUsersVo.getUserId());
                            }
                            c.setOp("U_SU");
                            msgList.add(new SyncOperationMsg(c.getOp(), JSONObject.toJSONString(cdcMsg)));
                        } else if (N_ONList.contains(c.getField())) {//手术状态修改  N_ON
                            c.setOp("N_ON");
                        }
                    }
//                    afterMedOperationMaster.setOp(SyncOperationInfoV2Constant.UPDATEOPERATIONINFO);//手术信息修改
//                    afterMedOperationMaster.setOp(SyncOperationInfoV2Constant.NODETIME);//手术信息修改
                }
                System.out.println(afterMedOperationMaster.toString());
//            } else if (tablename.equals("MED_PAT_MASTER_INDEX")) {
//                OdsMedPatMasterIndex odsMedPatMasterIndex = JSONUtil.toBean(afterStr, OdsMedPatMasterIndex.class);
//                if (op.equals(CdcOpConstant.CREATE)) {
//                    odsMedPatMasterIndex.setCreateBy("CDC");
//                    odsMedPatMasterIndex.setCreateTime(LocalDateTime.now());
//                    odsMedPatMasterIndexService.save(odsMedPatMasterIndex);
//                } else if (op.equals(CdcOpConstant.DELETE)) {
//                    odsMedPatMasterIndexService.delete(odsMedPatMasterIndex);
//                } else if (op.equals(CdcOpConstant.UPDATE)) {
//                    odsMedPatMasterIndex.setCreateBy("CDC");
//                    odsMedPatMasterIndex.setCreateTime(LocalDateTime.now());
//                    odsMedPatMasterIndexService.update(odsMedPatMasterIndex);
//                } else{
//                    odsMedPatMasterIndex.setCreateBy("CDC");
//                    odsMedPatMasterIndex.setCreateTime(LocalDateTime.now());
//                    odsMedPatMasterIndexService.save(odsMedPatMasterIndex);
//                }
            }
            for (SyncOperationMsg msg : msgList) {
                sendMsg(msg.getMethod(),msg.getMsg());
            }
        }
        return msgList;
    }


    public MedPatMasterIndexVo getOperationPatient(String patientId) {
        Map paramMap = new HashMap();
        paramMap.put("patientId", patientId);
        String result = HttpUtil.get(interfaceUrl + "med/patMasterIndex/getMedPatMasterIndex", paramMap);
        if (StrUtil.isBlank(result)) {
            return null;
        }
        MedPatMasterIndexVo medPatMasterIndexVo = JSONUtil.toBean(result, MedPatMasterIndexVo.class);
        return medPatMasterIndexVo;
    }

    public MedDeptDictVo getMedDeptDict(String deptCode) {
        Map paramMap = new HashMap();
        paramMap.put("deptCode", deptCode);
        String result = HttpUtil.get(interfaceUrl + "med/deptDict/getMedDeptDict", paramMap);
        if (StrUtil.isBlank(result)) {
            return null;
        }
        MedDeptDictVo medDeptDictVo = JSONUtil.toBean(result, MedDeptDictVo.class);
        return medDeptDictVo;
    }

    public MedHisUsersVo getMedHisUsersVo(String staffNo) {
        Map paramMap = new HashMap();
        paramMap.put("userId", staffNo);
        String result = HttpUtil.get(interfaceUrl + "med/hisUsers/getMedHisUsersVo", paramMap);
        if (StrUtil.isBlank(result)) {
            return null;
        }
        MedHisUsersVo medHisUsersVo = JSONUtil.toBean(result, MedHisUsersVo.class);
        return medHisUsersVo;
    }

    public ByOperationMasterDomain getOperationMasterStaff(ByOperationMasterDomain domain) {
        //第一巡回护士 0011
        if (StrUtil.isNotBlank(domain.getFirstSupplyNurse()) || StrUtil.isNotBlank(domain.getFirstSupplyNurseNo())) {
            MedHisUsersVo firstSupplyNurseVo = getMedHisUsersVo(domain.getFirstSupplyNurse());
            if (firstSupplyNurseVo != null) {
                domain.setFirstSupplyNurse(firstSupplyNurseVo.getUserName());
                domain.setFirstSupplyNurseNo(firstSupplyNurseVo.getUserId());
            }
        }
        //第二巡回护士 0012
        if (StrUtil.isNotBlank(domain.getSecondSupplyNurse())) {
            MedHisUsersVo secondSupplyNurseVo = getMedHisUsersVo(domain.getSecondSupplyNurse());
            if (secondSupplyNurseVo != null) {
                domain.setSecondSupplyNurse(secondSupplyNurseVo.getUserName());
                domain.setSecondSupplyNurseNo(secondSupplyNurseVo.getUserId());
            }
        }
        if (StrUtil.isNotBlank(domain.getFirstOperationNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstOperationNurse());
            if (vo != null) {
                domain.setFirstOperationNurse(vo.getUserName());
                domain.setFirstOperationNurseNo(vo.getUserId());
            }
        }

        if (StrUtil.isNotBlank(domain.getSecondOperationNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSecondOperationNurse());
            if (vo != null) {
                domain.setSecondOperationNurse(vo.getUserName());
                domain.setSecondOperationNurseNo(vo.getUserId());
            }
        }

        if (StrUtil.isNotBlank(domain.getSurgeon())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSurgeon());
            if (vo != null) {
                domain.setSurgeon(vo.getUserName());
                domain.setSurgeonNo(vo.getUserId());
            }
        }

        if (StrUtil.isNotBlank(domain.getFirstAssistantNo()) || StrUtil.isNotBlank(domain.getFirstAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstAssistant());
            if (vo != null) {
                domain.setFirstAssistant(vo.getUserName());
                domain.setFirstAssistantNo(vo.getUserId());
            }
        }
        //第二手术助手 0033
        if (StrUtil.isNotBlank(domain.getSecondAssistantNo()) || StrUtil.isNotBlank(domain.getSecondAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSecondAssistant());
            if (vo != null) {
                domain.setSecondAssistant(vo.getUserName());
                domain.setSecondAssistantNo(vo.getUserId());
            }
        }
        //第三手术助手 0034
        if (StrUtil.isNotBlank(domain.getThirdAssistantNo()) || StrUtil.isNotBlank(domain.getThirdAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getThirdAssistant());
            if (vo != null) {
                domain.setThirdAssistant(vo.getUserName());
                domain.setThirdAssistantNo(vo.getUserId());
            }
        }
        //第四手术助手 0035
        if (StrUtil.isNotBlank(domain.getFourthAssistantNo()) || StrUtil.isNotBlank(domain.getFourthAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFourthAssistant());
            if (vo != null) {
                domain.setFourthAssistant(vo.getUserName());
                domain.setFourthAssistantNo(vo.getUserId());
            }
        }
        // 麻醉医师 0041
        if (StrUtil.isNotBlank(domain.getAnesthesiaDoctorNo()) || StrUtil.isNotBlank(domain.getAnesthesiaDoctor())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getAnesthesiaDoctor());
            if (vo != null) {
                domain.setAnesthesiaDoctor(vo.getUserName());
                domain.setAnesthesiaDoctorNo(vo.getUserId());
            }
        }
        //第一麻醉助手 0042
        if (StrUtil.isNotBlank(domain.getAnesthesiaAssistantNo()) || StrUtil.isNotBlank(domain.getAnesthesiaAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getAnesthesiaAssistant());
            if (vo != null) {
                domain.setAnesthesiaAssistant(vo.getUserName());
                domain.setAnesthesiaAssistantNo(vo.getUserId());
            }
        }
        //第2麻醉助手 0043
        if (StrUtil.isNotBlank(domain.getSecondAnesAssistantNo()) || StrUtil.isNotBlank(domain.getSecondAnesAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSecondAnesAssistant());
            if (vo != null) {
                domain.setSecondAnesAssistant(vo.getUserName());
                domain.setSecondAnesAssistantNo(vo.getUserId());
            }
        }
        //第3麻醉助手  0044
        if (StrUtil.isNotBlank(domain.getThirdAnesAssistanNo()) || StrUtil.isNotBlank(domain.getThirdAnesAssistan())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getThirdAnesAssistan());
            if (vo != null) {
                domain.setThirdAnesAssistan(vo.getUserName());
                domain.setThirdAnesAssistanNo(vo.getUserId());
            }
        }
        //第4麻醉助手 0045
        if (StrUtil.isNotBlank(domain.getFourthAnesAssistantNo()) || StrUtil.isNotBlank(domain.getFourthAnesAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFourthAnesAssistant());
            if (vo != null) {
                domain.setFourthAnesAssistant(vo.getUserName());
                domain.setFourthAnesAssistantNo(vo.getUserId());
            }
        }
        //第一麻醉护士 0051
        if (StrUtil.isNotBlank(domain.getFirstAnesNurseNo()) || StrUtil.isNotBlank(domain.getFirstAnesNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstAnesNurse());
            if (vo != null) {
                domain.setFirstAnesNurse(vo.getUserName());
                domain.setFirstAnesNurseNo(vo.getUserId());
            }
        }
        //第2麻醉护士 0052
        if (StrUtil.isNotBlank(domain.getSecondAnesNurseNo()) || StrUtil.isNotBlank(domain.getSecondAnesNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSecondAnesNurse());
            if (vo != null) {
                domain.setSecondAnesNurse(vo.getUserName());
                domain.setSecondAnesNurseNo(vo.getUserId());
            }
        }
        //第3麻醉护士 0053
        if (StrUtil.isNotBlank(domain.getThirdAnesNurseNo()) || StrUtil.isNotBlank(domain.getThirdAnesNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getThirdAnesNurse());
            if (vo != null) {
                domain.setThirdAnesNurse(vo.getUserName());
                domain.setThirdAnesNurseNo(vo.getUserId());
            }
        }
        //灌注医生 0061
        if (StrUtil.isNotBlank(domain.getCpbDoctorNo()) || StrUtil.isNotBlank(domain.getCpbDoctor())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getCpbDoctor());
            if (vo != null) {
                domain.setCpbDoctor(vo.getUserName());
                domain.setCpbDoctorNo(vo.getUserId());
            }
        }
        //第一灌注医生助手 0062
        if (StrUtil.isNotBlank(domain.getFirstCpbAssistantNo()) || StrUtil.isNotBlank(domain.getFirstCpbAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstCpbAssistant());
            if (vo != null) {
                domain.setFirstCpbAssistant(vo.getUserName());
                domain.setFirstCpbAssistantNo(vo.getUserId());
            }
        }
        //第2灌注医生助手 0063
        if (StrUtil.isNotBlank(domain.getSecondCpbAssistantNo()) || StrUtil.isNotBlank(domain.getSecondCpbAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSecondCpbAssistant());
            if (vo != null) {
                domain.setSecondCpbAssistant(vo.getUserName());
                domain.setSecondCpbAssistantNo(vo.getUserId());
            }
        }
        //第3灌注医生助手 0064
        if (StrUtil.isNotBlank(domain.getThirdCpbAssistantNo()) || StrUtil.isNotBlank(domain.getThirdCpbAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getThirdCpbAssistant());
            if (vo != null) {
                domain.setThirdCpbAssistant(vo.getUserName());
                domain.setThirdCpbAssistantNo(vo.getUserId());
            }
        }
        //第4灌注医生助手 0065
        if (StrUtil.isNotBlank(domain.getFourthCpbAssistantNo()) || StrUtil.isNotBlank(domain.getFourthCpbAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFourthCpbAssistant());
            if (vo != null) {
                domain.setFourthCpbAssistant(vo.getUserName());
                domain.setFourthCpbAssistantNo(vo.getUserId());
            }
        }
        //输血医师  0071
        if (StrUtil.isNotBlank(domain.getBloodTransfuserNo()) || StrUtil.isNotBlank(domain.getBloodTransfuser())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getBloodTransfuser());
            if (vo != null) {
                domain.setBloodTransfuser(vo.getUserName());
                domain.setBloodTransfuserNo(vo.getUserId());
            }
        }
        //PACU医生  0081
        if (StrUtil.isNotBlank(domain.getPacuDoctorNo()) || StrUtil.isNotBlank(domain.getPacuDoctor())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getPacuDoctor());
            if (vo != null) {
                domain.setPacuDoctor(vo.getUserName());
                domain.setPacuDoctorNo(vo.getUserId());
            }
        }
        //PACU第一助手  0082
        if (StrUtil.isNotBlank(domain.getFirstPacuAssistantNo()) || StrUtil.isNotBlank(domain.getFirstPacuAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstPacuAssistant());
            if (vo != null) {
                domain.setFirstPacuAssistant(vo.getUserName());
                domain.setFirstPacuAssistantNo(vo.getUserId());
            }
        }
        //PACU第2助手  0083
        if (StrUtil.isNotBlank(domain.getFirstPacuAssistantNo()) || StrUtil.isNotBlank(domain.getFirstPacuAssistant())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstPacuAssistant());
            if (vo != null) {
                domain.setFirstPacuAssistant(vo.getUserName());
                domain.setFirstPacuAssistantNo(vo.getUserId());
            }
        }
        //PACU第一护士 0091
        if (StrUtil.isNotBlank(domain.getFirstPacuNurseNo()) || StrUtil.isNotBlank(domain.getFirstPacuNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getFirstPacuNurse());
            if (vo != null) {
                domain.setFirstPacuNurse(vo.getUserName());
                domain.setFirstPacuNurseNo(vo.getUserId());
            }
        }
        //PACU第一护士 0092
        if (StrUtil.isNotBlank(domain.getSecondPacuNurseNo()) || StrUtil.isNotBlank(domain.getSecondPacuNurse())) {
            MedHisUsersVo vo = getMedHisUsersVo(domain.getSecondPacuNurse());
            if (vo != null) {
                domain.setSecondPacuNurse(vo.getUserName());
                domain.setSecondPacuNurseNo(vo.getUserId());
            }
        }
        return domain;
    }

    /**
     * 封装手术名称信息
     *
     * @param domain 手术主记录
     * @return void
     * @author 姜楠
     * @since 2022/6/8 10:29
     **/
    public List<ByOperationStaffDomain> getOperationStaffList(ByOperationMasterDomain domain) {
        List<ByOperationStaffDomain> staffs = new ArrayList<>();
        //第一巡回护士 0011
        if (StrUtil.isNotBlank(domain.getFirstSupplyNurseNo()) || StrUtil.isNotBlank(domain.getFirstSupplyNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0011, domain.getFirstSupplyNurse(), domain.getFirstSupplyNurseNo()));
        }
        //第二巡回护士 0012
        if (StrUtil.isNotBlank(domain.getSecondSupplyNurseNo()) || StrUtil.isNotBlank(domain.getSecondSupplyNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0012, domain.getSecondSupplyNurse(), domain.getSecondSupplyNurseNo()));
        }
//        //第三巡回护士 0013
//        if (StrUtil.isNotBlank(domain.getFirstSupplyNurseNo()) || StrUtil.isNotBlank(domain.getFirstSupplyNurse())) {
//            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0013, domain.getFirstSupplyNurse(), domain.getFirstSupplyNurseNo()));
//        }
//        //第四巡回护士 0014
//        if (StrUtil.isNotBlank(domain.getFirstSupplyNurseNo()) || StrUtil.isNotBlank(domain.getFirstSupplyNurse())) {
//            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0014, domain.getFirstSupplyNurse(), domain.getFirstSupplyNurseNo()));
//        }
        //第一洗手护士 0021
        if (StrUtil.isNotBlank(domain.getFirstOperationNurseNo()) || StrUtil.isNotBlank(domain.getFirstOperationNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0021, domain.getFirstOperationNurse(), domain.getFirstOperationNurseNo()));
        }
        //第二洗手护士 0022
        if (StrUtil.isNotBlank(domain.getSecondOperationNurseNo()) || StrUtil.isNotBlank(domain.getSecondOperationNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0022, domain.getSecondOperationNurse(), domain.getSecondOperationNurseNo()));
        }
////        //第三洗手护士 0023
//        if (StrUtil.isNotBlank(domain.getFirstOperationNurseNo()) || StrUtil.isNotBlank(domain.getFirstOperationNurse())) {
//            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0023, domain.getFirstOperationNurse(), domain.getFirstOperationNurseNo()));
//        }
//        //第四洗手护士 0024
//        if (StrUtil.isNotBlank(domain.getFirstOperationNurseNo()) || StrUtil.isNotBlank(domain.getFirstOperationNurse())) {
//            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0024, domain.getFirstOperationNurse(), domain.getFirstOperationNurseNo()));
//        }
        //手术医生 0031
        if (StrUtil.isNotBlank(domain.getSurgeonNo()) || StrUtil.isNotBlank(domain.getSurgeon())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0031, domain.getSurgeon(), domain.getSurgeonNo()));
        }
        //第一手术助手 0032
        if (StrUtil.isNotBlank(domain.getFirstAssistantNo()) || StrUtil.isNotBlank(domain.getFirstAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0032, domain.getFirstAssistant(), domain.getFirstAssistantNo()));
        }
        //第二手术助手 0033
        if (StrUtil.isNotBlank(domain.getSecondAssistantNo()) || StrUtil.isNotBlank(domain.getSecondAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0033, domain.getSecondAssistant(), domain.getSecondAssistantNo()));
        }
        //第三手术助手 0034
        if (StrUtil.isNotBlank(domain.getThirdAssistantNo()) || StrUtil.isNotBlank(domain.getThirdAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0034, domain.getThirdAssistant(), domain.getThirdAssistantNo()
            ));
        }
        //第四手术助手 0035
        if (StrUtil.isNotBlank(domain.getFourthAssistantNo()) || StrUtil.isNotBlank(domain.getFourthAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0035, domain.getFourthAssistant(), domain.getFourthAssistantNo()
            ));
        }
        // 麻醉医师 0041
        if (StrUtil.isNotBlank(domain.getAnesthesiaDoctorNo()) || StrUtil.isNotBlank(domain.getAnesthesiaDoctor())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0041, domain.getAnesthesiaDoctor(), domain.getAnesthesiaDoctorNo()
            ));
        }
        //第一麻醉助手 0042
        if (StrUtil.isNotBlank(domain.getAnesthesiaAssistantNo()) || StrUtil.isNotBlank(domain.getAnesthesiaAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0042, domain.getAnesthesiaAssistant(), domain.getAnesthesiaAssistantNo()
            ));
        }
        //第2麻醉助手 0043
        if (StrUtil.isNotBlank(domain.getSecondAnesAssistantNo()) || StrUtil.isNotBlank(domain.getSecondAnesAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0043, domain.getSecondAnesAssistant(), domain.getSecondAnesAssistantNo()
            ));
        }
        //第3麻醉助手  0044
        if (StrUtil.isNotBlank(domain.getThirdAnesAssistanNo()) || StrUtil.isNotBlank(domain.getThirdAnesAssistan())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0044, domain.getThirdAnesAssistan(), domain.getThirdAnesAssistanNo()
            ));
        }
        //第4麻醉助手 0045
        if (StrUtil.isNotBlank(domain.getFourthAnesAssistantNo()) || StrUtil.isNotBlank(domain.getFourthAnesAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0045, domain.getFourthAnesAssistant(), domain.getFourthAnesAssistantNo()
            ));
        }
        //第一麻醉护士 0051
        if (StrUtil.isNotBlank(domain.getFirstAnesNurseNo()) || StrUtil.isNotBlank(domain.getFirstAnesNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0051, domain.getFirstAnesNurse(), domain.getFirstAnesNurseNo()
            ));
        }
        //第2麻醉护士 0052
        if (StrUtil.isNotBlank(domain.getSecondAnesNurseNo()) || StrUtil.isNotBlank(domain.getSecondAnesNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0052, domain.getSecondAnesNurse(), domain.getSecondAnesNurseNo()
            ));
        }
        //第3麻醉护士 0053
        if (StrUtil.isNotBlank(domain.getThirdAnesNurseNo()) || StrUtil.isNotBlank(domain.getThirdAnesNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0053, domain.getThirdAnesNurse(), domain.getThirdAnesNurseNo()
            ));
        }
        //灌注医生 0061
        if (StrUtil.isNotBlank(domain.getCpbDoctorNo())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0061, domain.getCpbDoctor(), domain.getCpbDoctorNo()
            ));
        }
        //第一灌注医生助手 0062
        if (StrUtil.isNotBlank(domain.getFirstCpbAssistantNo()) || StrUtil.isNotBlank(domain.getFirstCpbAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0062, domain.getFirstCpbAssistant(), domain.getFirstCpbAssistantNo()
            ));
        }
        //第2灌注医生助手 0063
        if (StrUtil.isNotBlank(domain.getSecondCpbAssistantNo()) || StrUtil.isNotBlank(domain.getSecondCpbAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0063, domain.getSecondCpbAssistant(), domain.getSecondCpbAssistantNo()
            ));
        }
        //第3灌注医生助手 0064
        if (StrUtil.isNotBlank(domain.getThirdCpbAssistantNo()) || StrUtil.isNotBlank(domain.getThirdCpbAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0064, domain.getThirdCpbAssistant(), domain.getThirdCpbAssistantNo()
            ));
        }
        //第4灌注医生助手 0065
        if (StrUtil.isNotBlank(domain.getFourthCpbAssistantNo()) || StrUtil.isNotBlank(domain.getFourthCpbAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0065, domain.getFourthCpbAssistant(), domain.getFourthCpbAssistantNo()
            ));
        }
        //输血医师  0071
        if (StrUtil.isNotBlank(domain.getBloodTransfuserNo()) || StrUtil.isNotBlank(domain.getBloodTransfuser())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0071, domain.getBloodTransfuser(), domain.getBloodTransfuserNo()
            ));
        }
        //PACU医生  0081
        if (StrUtil.isNotBlank(domain.getPacuDoctorNo()) || StrUtil.isNotBlank(domain.getPacuDoctor())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0081, domain.getPacuDoctor(), domain.getPacuDoctorNo()
            ));
        }
        //PACU第一助手  0082
        if (StrUtil.isNotBlank(domain.getFirstPacuAssistantNo()) || StrUtil.isNotBlank(domain.getFirstPacuAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0082, domain.getFirstPacuAssistant(), domain.getFirstPacuAssistantNo()
            ));
        }
        //PACU第2助手  0083
        if (StrUtil.isNotBlank(domain.getFirstPacuAssistantNo()) || StrUtil.isNotBlank(domain.getFirstPacuAssistant())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0083, domain.getSecondPacuAssistant(), domain.getSecondPacuAssistantNo()
            ));
        }
        //PACU第一护士 0091
        if (StrUtil.isNotBlank(domain.getFirstPacuNurseNo()) || StrUtil.isNotBlank(domain.getFirstPacuNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0091, domain.getFirstPacuNurse(), domain.getFirstPacuNurseNo()
            ));
        }
        //PACU第一护士 0092
        if (StrUtil.isNotBlank(domain.getSecondPacuNurseNo()) || StrUtil.isNotBlank(domain.getSecondPacuNurse())) {
            staffs.add(new ByOperationStaffDomain(SysPostConstants.POST_CODE_0092, domain.getSecondPacuNurse(), domain.getSecondPacuNurseNo()
            ));
        }
        return staffs;
    }

    public String getNodetype(String dateTitle) {
        String nodetype = "";
        switch (dateTitle) {
            case DateConstant.START_DATE_TIME:
                nodetype = "300";
                break;
            case DateConstant.END_DATE_TIME:
                nodetype = "500";
                break;
            case DateConstant.ANES_START_TIME:
                nodetype = "250";
                break;
            case DateConstant.ANES_END_TIME:
                nodetype = "550";
                break;
            case DateConstant.IN_DATE_TIME:
                nodetype = "200";
                break;
            case DateConstant.OUT_DATE_TIME:
                nodetype = "570";
                break;
            default:
                System.out.println("default");
        }
        return nodetype;
    }

    public void sendMsg(String method, String msg) {
        System.out.println("msg" + msg);
        String url = nursingInterfaceUrl + method;
        if(isSendMsg.equals("1")){
            String result = HttpUtil.post(url, msg);
            System.out.println("result.toString()" + result.toString());
        }
    }

    /**
     * 转换为驼峰格式/转换为下划线方式
     *
     * @param json  等待转换的方法
     * @param upper 首字母大写或者小写
     * @return 转换后的
     */
    public static JSONObject formatKey(final JSONObject json, boolean upper) {
        JSONObject real = new JSONObject();
        for (String it : json.keySet()) {
            Object objR = json.get(it);
            // 转换为驼峰格式/转换为下划线方式
            String key = it.toLowerCase();
            key = key.contains("_") ? StrUtil.toCamelCase(key) : StrUtil.toUnderlineCase(key);
            // 首字母大写或者小写
            key = upper ? StrUtil.upperFirst(key) : StrUtil.lowerFirst(key);
            if (objR instanceof String) {
                real.put(key, objR);
            }
            if (objR instanceof Integer) {
                real.put(key, objR);
            }
            if (objR instanceof Long) {

                real.put(key, LocalDateTimeUtil.of(((Long) objR).longValue()));
            }
            if (objR instanceof JSONObject) {
                real.put(key, formatKey((JSONObject) objR, upper));
            }
            if (objR instanceof JSONArray) {
                JSONArray jsonA = new JSONArray();
                for (Object objA : (JSONArray) objR) {
                    jsonA.add(formatKey((JSONObject) objA, upper));
                }
                real.put(key, jsonA);
            }
        }

        return real;
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}
