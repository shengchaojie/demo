package com.scj.data.tran;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shengchaojie on 2016/5/20.
 */

public class TranMain {

    /**
     * 导入完之后 还需要对id进行处理 UPDATE school SET school_id = CONCAT(parent_id,'-',school_id);
     *
     * @param args
     */
    public static void main(String[] args) {

        provinceTran();
        cityTran();
        districtTran();
        schoolTran();
    }

    public static void provinceTran() {
        String basePath = "C:\\Users\\Administrator\\Desktop\\school_data";
        String fileName = "province";
        String myPattern = "\\{\"(provinceId)\":(\\d)+,\"name\":\"[\\u4e00-\\u9fa5]+\"\\}";
        List<String> properties = new ArrayList<>();
        properties.add("provinceId");
        properties.add("name");

        TranProcess process = new TranProcess(basePath, fileName, myPattern, properties);

        process.tranData();
    }

    public static void cityTran() {
        String basePath = "C:\\Users\\Administrator\\Desktop\\school_data";
        String fileName = "city";
        String myPattern = "\\{\"parentId\":(\\d)+,\"cityId\":(\\d)+,\"name\":\"[\\u4e00-\\u9fa5]+\"\\}";//{"parentId":1,"cityId":1,"name":"合肥"}
        List<String> properties = new ArrayList<>();
        properties.add("cityId");
        properties.add("name");
        properties.add("parentId");

        TranProcess process = new TranProcess(basePath, fileName, myPattern, properties);

        process.tranData();
    }

    public static void districtTran() {
        String basePath = "C:\\Users\\Administrator\\Desktop\\school_data";
        String fileName = "districtJson2";
        String myPattern = "\\{\"districtId\":(\\d)+,\"parentId\":\"(\\d)+-(\\d)+\",\"name\":\"[\\u4e00-\\u9fa5]+\"\\}";//{"districtId":1,"parentId":"1-1","name":"巢湖市"}
        List<String> properties = new ArrayList<>();
        properties.add("districtId");
        properties.add("name");
        properties.add("parentId");

        TranProcess process = new TranProcess(basePath, fileName, myPattern, properties);

        process.tranData();
    }

    public static void schoolTran() {
        String basePath = "C:\\Users\\Administrator\\Desktop\\school_data";
        String fileName = "school";
        String myPattern = "\\{\"schoolId\":(\\d)+,\"parentId\":\"(\\d)+-(\\d)+-(\\d)+\",\"name\":\"[\\u4e00-\\u9fa5]+\"\\}";//{"schoolId":1,"parentId":"1-1-1","name":"三湾小学"}
        List<String> properties = new ArrayList<>();
        properties.add("schoolId");
        properties.add("name");
        properties.add("parentId");

        TranProcess process = new TranProcess(basePath, fileName, myPattern, properties);

        process.tranData();
    }
}
