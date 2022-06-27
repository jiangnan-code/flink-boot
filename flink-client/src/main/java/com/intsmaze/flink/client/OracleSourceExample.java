//package com.intsmaze.flink.client;
//
//import com.ververica.cdc.connectors.oracle.OracleSource;
//import com.ververica.cdc.connectors.oracle.table.StartupOptions;
//import org.apache.flink.streaming.api.CheckpointingMode;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.environment.CheckpointConfig;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.source.SourceFunction;
//
//import java.util.Properties;
//
//public class OracleSourceExample {
//    public static void main(String[] args) throws Exception {
////        if (args.length == 0) {
////            System.out.println("您调⽤main⽅法时没有指定任何参数！");
////            return;
////        }
////        System.out.println("您调⽤main⽅法时指定的参数包括：");
////        for (int i = 0; i < args.length; i++) {
////            System.out.println("参数" + (i + 1) + "的值为：" + args[i]);
////        }
////        Configuration configuration = new Configuration();
////        configuration.setString("execution.savepoint.path","D:\\tmp\\flink-ck\\18b9ba2f94f36bcce884040bdbc6ace0\\chk-2");
//        //1.创建执行环境
////        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
////        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
//        env.setParallelism(1);
//
//        env.execute("FlinkCDC");
//    }
//}