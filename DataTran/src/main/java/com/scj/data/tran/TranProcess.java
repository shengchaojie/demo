package com.scj.data.tran;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shengchaojie on 2016/5/20.
 */

public class TranProcess {

    private String basePath;
    private String fileName;
    private String matchPattern;
    private List<String> properties;

    public TranProcess(String basePath, String fileName,String matchPattern,List<String> properties) {
        this.basePath = basePath;
        this.matchPattern = matchPattern;
        this.fileName = fileName;
        this.properties =properties;
    }

    public void tranData() {
        //String basePath ="C:\\Users\\Administrator\\Desktop\\school_data";
        //String fileName ="province";
        //String myPattern="\\{\"(provinceId)\":(\\d)+,\"name\":\"[\\u4e00-\\u9fa5]+\"\\}";

        String path =basePath+"\\"+fileName+".json";
        String csvPath =basePath+"\\"+fileName+"_tran.csv";

        try {

            BufferedReader bf =new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            BufferedWriter bw =new BufferedWriter(new FileWriter(csvPath));

            try {
                char[] buffer =new char[128];
                StringBuffer content =new StringBuffer();
                int size =0;
                while ((size =bf.read(buffer))!=-1)
                {
                    content.append(new String(buffer,0,size));
                }

                System.out.println(content.toString());

                //String s ="\\{\"(provinceId)\":(\\d)+,\"name\":\"[\\u4e00-\\u9fa5]+\"\\}";
                Pattern pattern =Pattern.compile(matchPattern);
                Matcher matcher = pattern.matcher(content);
                StringBuilder sb =new StringBuilder();
                int id =1;
                String lineSeparator = System.getProperty("line.separator", "/n");
                while (matcher.find())
                {
                    JSONObject jsonObject = JSON.parseObject(matcher.group());
                    /*String provinceId =jsonObject.get("provinceId").toString();
                    String name =jsonObject.get("name").toString();
                    System.out.println(provinceId);
                    System.out.println(name);

                    sb.append("\"").append(id).append("\"");
                    sb.append(",");
                    sb.append("\"").append(provinceId).append("\"");
                    sb.append(",");
                    sb.append("\"").append(name).append("\"");
                    sb.append(lineSeparator);
                    id++;*/
                    sb.append("\"").append(id).append("\"");
                    sb.append(",");
                    for (String property:properties)
                    {
                        String propertyValue =jsonObject.get(property).toString();
                        sb.append("\"").append(propertyValue).append("\"");
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    sb.append(lineSeparator);
                    id++;
                }
                System.out.println(sb.toString());

                bw.write(new String(sb.toString().getBytes(),"UTF-8"));
            }finally {
                bf.close();
                bw.close();
            }


        }catch (IOException ignored)
        {}

    }


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getMatchPattern() {
        return matchPattern;
    }

    public void setMatchPattern(String matchPattern) {
        this.matchPattern = matchPattern;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
