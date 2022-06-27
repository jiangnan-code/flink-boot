package com.intsmaze.flink.client;

import com.intsmaze.flink.base.env.BaseFlink;
import com.intsmaze.flink.client.task.simple.SimpleFunction;
import com.intsmaze.flink.client.task.source.SimpleDataSource;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SimpleClient extends BaseFlink {

    /**
     * 本地启动参数  -isLocal local
     * 集群启动参数  -isIncremental isIncremental
     * -parallelism 4
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SimpleClient topo = new SimpleClient();
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
    public void createTopology(StreamExecutionEnvironment builder) {

//        DataStream<String> inputDataStrem = getKafkaSpout(properties.getProperty("read-topic").trim());
//
//        DataStream<String> processDataStream = inputDataStrem.flatMap(new SimpleFunction());
//
//        // 指定分区策略
//        FlinkKafkaProducer<String> triggerProducer = new FlinkKafkaProducer<String>(
//                properties.getProperty("kafkaIp").trim(),
//                properties.getProperty("send-topic").trim(),
//                new SimpleStringSchema());
//        triggerProducer.setWriteTimestampToKafka(true);
//        processDataStream.addSink(triggerProducer);
        DataStream<String> inputDataStrem = env.addSource(new SimpleDataSource());

        DataStream<String> processDataStream = inputDataStrem.flatMap(new SimpleFunction());

        processDataStream.print("输出结果");

    }
}

