package com.intsmaze.flink.client;

import cn.hutool.core.util.StrUtil;
import com.intsmaze.flink.base.env.BaseFlink;
import com.intsmaze.flink.base.util.PropertiesUtils;
import com.intsmaze.flink.client.entity.nursing.NursingParameters;
import com.intsmaze.flink.client.task.nursing.NursingSystemDeserializationSchema;
import com.ververica.cdc.connectors.oracle.OracleSource;
import com.ververica.cdc.connectors.oracle.table.StartupOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.TimeZone;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class OracleCDC extends BaseFlink {

    protected Properties properties;

    /**
     * 本地启动参数  -isLocal local
     * com.intsmaze.flink.client.OracleCDC
     * -isIncremental local
     * 集群启动参数  -isIncremental isIncremental
     * -parallelism 4
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        OracleCDC topo = new OracleCDC();
        topo.run(ParameterTool.fromArgs(args));
    }

//    @Override
//    public String getTopoName() {
//        return "intsmaze-calc";
//    }

    @Override
    public String getJobName() {
        return "intsmaze-calc";
    }

    @Override
    public String getConfigName() {
        return "topology-base.xml";
    }

    @Override
    public String getPropertiesName() {
        return "config.properties";
    }

    @Override
    public void createTopology(StreamExecutionEnvironment builder) throws IOException {
        this.properties = PropertiesUtils.getProperties(getPropertiesName());
        String gmt = properties.getProperty("gmt");
        String hourStr = properties.getProperty("hour");
        String isSendMsg = properties.getProperty("isSendMsg");
        TimeZone.setDefault(TimeZone.getTimeZone(gmt));
        String hostname = properties.getProperty("hostname");
        String portStr = properties.getProperty("port");
        Integer port = Integer.parseInt(portStr);
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String databaseName = properties.getProperty("databaseName");
        String schemaList = properties.getProperty("schemaList");
        String tableList = properties.getProperty("tableList");

//        String U_OIStr = "anesthesiaMethod,operationPosition,patientId,visitId,operId,deptStayed,operatingRoom,operatingRoomNo,sequence,operationClass,sequence,diagBeforeOperation,diagAfterOperation,patientCondition,operationScale,woundType,scheduledDateTime,emergencyIndicator,patientCondition";
//        String U_OTStr = "startDateTime,endDateTime,anesStartTime,anesEndTime,inDateTime,outEateTime";
//        String U_SUStr = "surgeon,firstSupplyNurse,secondSupplyNurse,firstOperationNurse,secondOperationNurse,secondAssistant,thirdAssistant,fourthAssistant,anesthesiaDoctor,anesthesiaAssistant,secondAnesAssistant,thirdAnesAssistan,fourthAnesAssistant,firstAnesNurse,secondAnesNurse,thirdAnesNurse,cpbDoctor,firstCpbAssistant,secondCpbAssistant,thirdCpbAssistant,fourthCpbAssistant,bloodTransfuser,pacuDoctor,firstPacuAssistant,firstPacuAssistant,firstPacuNurse,secondPacuNurse,firstAssistant";

        String U_OIStr = properties.getProperty("U_OIList");
        String U_OTStr = properties.getProperty("U_OTList");
        String U_SUStr = properties.getProperty("U_SUList");
        String interfaceUrl = properties.getProperty("interfaceUrl");
        String nursingInterfaceUrl = properties.getProperty("nursingInterfaceUrl");
        String kafkaIp = properties.getProperty("kafkaIp");
        String sendTopic = properties.getProperty("sendTopic");
        String startupOptions = properties.getProperty("startupOptions");
        NursingParameters nursingParameters = new NursingParameters();
        nursingParameters.setNursingInterfaceUrl(nursingInterfaceUrl);
        nursingParameters.setInterfaceUrl(interfaceUrl);
        nursingParameters.setU_OIList(Arrays.asList(U_OIStr.split(",")));
        nursingParameters.setU_OTList(Arrays.asList(U_OTStr.split(",")));
        nursingParameters.setU_SUList(Arrays.asList(U_SUStr.split(",")));
        nursingParameters.setIsSendMsg(isSendMsg);
        if(StrUtil.isNotBlank(hourStr)){
            Integer hour = Integer.parseInt(hourStr);
            nursingParameters.setHour(hour);
        }


        StartupOptions startup = StartupOptions.latest();

        if(StringUtils.isNotBlank(startupOptions)){
            if(startupOptions.equals("latest")){
                startup = StartupOptions.latest();
            }else if (startupOptions.equals("initial")){
                startup = StartupOptions.initial();
            }
        }
        builder.setParallelism(1);
        final Properties debeziumProperties = new Properties();
        debeziumProperties.put("log.mining.strategy", "online_catalog");
        debeziumProperties.put("log.mining.continuous.mine", "true");
        SourceFunction<String> sourceFunction = OracleSource.<String>builder()
//                .hostname("192.168.10.1")
//                .port(1521)
//                .database("orcl") // monitor XE database
//                .schemaList("ROMA_LOGMINER") // monitor inventory schema
//                .tableList("ROMA_LOGMINER.MED_OPERATION_MASTER,ROMA_LOGMINER.MED_OPERATION_NAME") // monitor products table
//                .username("roma_logminer")
//                .password("password")
                .hostname(hostname)
                .port(port)
                .database(databaseName)
                .schemaList(schemaList)
                .tableList(tableList)
                .username(username)

                .password(password)
                .deserializer(new NursingSystemDeserializationSchema(nursingParameters)) // converts SourceRecord to JSON String
//                .startupOptions(StartupOptions.latest())
                .startupOptions(startup)
                .debeziumProperties(debeziumProperties)
                .build();

        DataStreamSource<String> stringDataStreamSource = env.addSource(sourceFunction);
        FlinkKafkaProducer<String> triggerProducer = new FlinkKafkaProducer<String>(
                kafkaIp, sendTopic,
                new SimpleStringSchema());
        triggerProducer.setWriteTimestampToKafka(true);
        stringDataStreamSource.addSink(triggerProducer);
        stringDataStreamSource.print();
        stringDataStreamSource.print("输出结果");
    }
}

