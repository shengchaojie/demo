package com.scj.spring.transaction;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shengchaojie on 2017/6/12.
 *
 * 小结单缺失数据迁移
 * 查询时间段内华为库内的callid
 * 查询时间段内workorder产生的数据
 * 对比产生sql
 */
public class TestMain2 {
    public static void main(String[] args) throws IOException, ParseException {
        Long smallWorkOrderId =2017070600010000L;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        BufferedReader br =new BufferedReader(new FileReader("D:\\scj\\callid.txt"));
        StringBuilder sb =new StringBuilder();
        char[] buffer =new char[1024];
        int size =0;
        while ((size =br.read(buffer))>0){
            sb.append(new String(buffer,0,size));
        }
        IOUtils.close(br);

        FileWriter fileWriter =new FileWriter("d:\\scj\\call.sql");

        Set<String> callIdSet = Sets.newHashSet(Lists.newArrayList(sb.toString().split(",")));

        System.out.println("一共有"+callIdSet.size()+"个callid");

        ApplicationContext context =new ClassPathXmlApplicationContext("classpath:transaction/transactionContext.xml");
        ((AbstractApplicationContext)context).registerShutdownHook();

        JdbcTemplate jdbcTemplate =context.getBean(JdbcTemplate.class);

        String sql ="SELECT * FROM call_tran.`call2` c left join entry e on c.deviceno =e.hw_no;";
        List<Map<String,Object>> lists =jdbcTemplate.queryForList(sql);
        StringBuilder mysql =new StringBuilder();
        int i =0;
        for (Map<String,Object> list :lists){
            String callid =(String) list.get("callid");
            if(callIdSet.contains(callid)){
                continue;
            }
            String callerNo =processNumber(list.get("callerno").toString());
            String calleeNo =processNumber(list.get("calleeno").toString());
            Date callBegin =sdf.parse(list.get("callbegin").toString()) ;
            Date callEnd =sdf.parse(list.get("callend").toString());
            String hwNo =list.get("deviceno").toString();
            String calltype =list.get("calltype").toString();
            if(list.get("user_id")==null){
                continue;
            }
            String format ="insert into wo_small_workorder(SMALL_WORKORDER_ID,call_id,customers_telephone,STATUS,support_name,group_leader,director,caller_num,called_num,call_type,begin_time,end_time,call_duration,hw_no,CREATE_TIME,CREATE_PERSON,update_time,update_person) values \r\n";
            mysql.append(format);
            String userId =list.get("user_id").toString();
            String supportName =list.get("support_name").toString();
            String groupName =list.get("group_name").toString();
            String director =list.get("director").toString();
            mysql.append("(")
                    .append("'").append("XJ"+(smallWorkOrderId+i++)).append("',")
                    .append("'").append(callid).append("',")
                    .append("'").append("0".equals(calltype)?callerNo:calleeNo).append("',")
                    .append("'").append(0).append("',")
                    .append("'").append(supportName).append("',")
                    .append("'").append(groupName).append("',")
                    .append("'").append(director).append("',")
                    .append("'").append(callerNo).append("',")
                    .append("'").append(calleeNo).append("',")
                    .append("'").append(calltype).append("',")
                    .append("'").append(sdf.format(callBegin)).append("',")
                    .append("'").append(sdf.format(callEnd)).append("',")
                    .append("'").append((callEnd.getTime()-callBegin.getTime())/1000).append("',")
                    .append("'").append(hwNo).append("',")
                    .append("'").append( sdf.format(new Date())).append("',")
                    .append("'").append(userId).append("',")
                    .append("'").append( sdf.format(new Date())).append("',")
                    .append("'").append(userId).append("'")
                    .append(");\r\n");
        }
        System.out.println(mysql);
        fileWriter.write(mysql.toString());
        IOUtils.close(fileWriter);
        System.out.println(i);
    }

    public static String processNumber(String number){
        if(number.startsWith("90")||number.startsWith("80")){
            return number.substring(2);
        }
        return number;
    }
}
