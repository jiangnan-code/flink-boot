package com.intsmaze.test;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.service.DataService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ：intsmaze
 * @date ：Created in 2021/1/22 11:22
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class BeanFactoryTest {

    public static void main(String[] args) {
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("topology-base.xml");
//		DataService da = applicationContext.getBean(DataService.class);
//		FlowData flowData=new FlowData();
//		flowData.setBillNumber("1231");
//		System.out.println(da.findUUID(flowData));
//		String datetime = "2021-06-24 12:11:00";
//		System.out.println(datetime.length());
        // TODO Auto-generated method stub
        Date date = new Date(1503544630000L);  // 对应的北京时间是2017-08-24 11:17:10

        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区

        SimpleDateFormat tokyoSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 东京
        tokyoSdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));  // 设置东京时区

        SimpleDateFormat londonSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 伦敦
        londonSdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));  // 设置伦敦时区

        System.out.println("毫秒数:" + date.getTime() + ", 北京时间:" + bjSdf.format(date));
        System.out.println("毫秒数:" + date.getTime() + ", 东京时间:" + tokyoSdf.format(date));
        System.out.println("毫秒数:" + date.getTime() + ", 伦敦时间:" + londonSdf.format(date));
    }
}
