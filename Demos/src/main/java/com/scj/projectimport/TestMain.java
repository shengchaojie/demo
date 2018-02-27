package com.scj.projectimport;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestMain {


    public static  Long projectIdGenerator = 202L;

    public static Long projectNumberGenerator =20L;

    public static void main(String[] args) throws IOException, InvalidFormatException {
        TestMain testMain = new TestMain();
        //得到主项目
        List<ProjectEntity> projectEntityList  = testMain.readExcel("C:\\Users\\10064749\\Desktop\\项目信息.xlsx");
        for(ProjectEntity project : projectEntityList){
            //创建流程和主项目
            project.setProjectName(project.getProjectName()+"(主)");
            String instanceId = testMain.generateProcess(project.isB2C());
            project.setProjectProcessId(instanceId);
            testMain.outputSql(testMain.generateProjectSql(project));

            //创建对应子项目流程和子项目
            instanceId = testMain.generateProcess(project.isB2C());
            project.setProjectName(project.getProjectName().substring(0,project.getProjectName().length()-3));
            project.setProjectProcessId(instanceId);
            project.setProjectParentId(project.getId());
            project.setId(projectIdGenerator++);
            project.setProjectNumber("P20180226"+String.format("%04d",projectNumberGenerator++));
            project.setProjectCategory(2);
            testMain.outputSql(testMain.generateProjectSql(project));
        }
    }

    public String generateProjectSql(ProjectEntity projectEntity){
        //INSERT INTO `project` VALUES (11, 'P201802260001', '盛超杰', '测试项目', '盛超杰', '2018-2-1 00:00:00', '2018-2-2 00:00:00', 'PMD20180226155256', NULL,
        // 'ee0e58f3517e40d194a6683e659f9191', '开仓完成待处理', 1, 0, '2018-2-26 16:18:47', '40', '2018-2-26 16:18:47', '40', NULL, 1000000002, NULL, NULL, NULL, NULL);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d hh:mm:ss");
        String format = "INSERT INTO `project`(`id`,`project_number`,`project_sponsor`,`project_name`,`project_leader`,`project_start_time`,`project_end_time`,`project_type_id`,`project_parent_id`,`project_process_id`,`project_process_link`,`project_category`,`project_status`,`create_date`,`create_person`,`update_date`,`update_person`,`end_project_reason`,`relate_customer_id`,`biz_type_id`,`operate_type_id`,`relate_warehouse_id`,`relate_nc_id`) VALUES (%s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, '%s', '%s', %s, %s, '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, '%s');\n";
        return String.format(format,
                this.handleNull(projectEntity.getId()),
                this.handleNull(projectEntity.getProjectNumber()),
                this.handleNull(projectEntity.getProjectSponsor()),
                this.handleNull(projectEntity.getProjectName()),
                this.handleNull(projectEntity.getProjectLeader()),
                this.handleNull(dateFormat.format(projectEntity.getProjectStartTime())),
                this.handleNull(dateFormat.format(projectEntity.getProjectEndTime())),
                this.handleNull(projectEntity.getProjectTypeId()),
                this.handleNull(projectEntity.getProjectParentId()),//parentid
                this.handleNull(projectEntity.getProjectProcessId()),
                "开仓完成待处理",
                this.handleNull(projectEntity.getProjectCategory()),
                this.handleNull(projectEntity.getProjectStatus()),
                this.handleNull(dateFormat.format(projectEntity.getCreateDate())),
                this.handleNull(projectEntity.getCreatePerson()),
                this.handleNull(dateFormat.format(projectEntity.getUpdateDate())),
                this.handleNull(projectEntity.getUpdatePerson()),
                this.handleNull(projectEntity.getEndProjectReason()),
                this.handleNull(projectEntity.getRelateCustomerId()),
                this.handleNull(projectEntity.getBizTypeId()),
                this.handleNull(projectEntity.getOperateTypeId()),
                this.handleNull(projectEntity.getRelateWarehouseId()),
                this.handleNull(projectEntity.getRelateNcId())
        );
    }

    public String handleNull(Object object){
        if(object==null){
            return "NULL";
        }
        return String.valueOf(object);
    }

    public List<ProjectEntity> readExcel(String location) throws IOException, InvalidFormatException {
        List<ProjectEntity> result = new ArrayList<>();
        Workbook workbook =null;
        try {
            File file = new File(location);
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int rowStart = sheet.getFirstRowNum() + 1;
            int rowEnd = sheet.getLastRowNum();

            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                ProjectEntity projectEntity = this.buildProjectEntity(row);
                if (projectEntity != null) {
                    result.add(projectEntity);
                }
            }
        }finally {
            if(workbook!=null) {
                workbook.close();
            }
        }

        return result;
    }

    public ProjectEntity buildProjectEntity(Row row){
        if(row ==null){
            return null;
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectIdGenerator++);
        projectEntity.setProjectNumber("P20180226"+String.format("%04d",projectNumberGenerator++));
        projectEntity.setProjectSponsor(row.getCell(5).getStringCellValue());
        projectEntity.setProjectName(row.getCell(4).getStringCellValue());
        if(row.getCell(6).getCellType()== Cell.CELL_TYPE_NUMERIC){
            DecimalFormat df = new DecimalFormat("#");
            double value = row.getCell(6).getNumericCellValue();
            projectEntity.setProjectLeader(df.format(value));
            //System.out.println(df.format(value));
        }else{
            projectEntity.setProjectLeader(row.getCell(6).getStringCellValue());
        }
        //projectEntity.setProjectLeader(row.getCell(6).getCellType()== Cell.CELL_TYPE_NUMERIC?new Double(row.getCell(6).getNumericCellValue()).toString():row.getCell(6).getStringCellValue());
        projectEntity.setProjectStartTime(row.getCell(7).getDateCellValue());
        projectEntity.setProjectEndTime(row.getCell(8).getDateCellValue());
        String projectType = row.getCell(9).getStringCellValue();
        if(projectType.startsWith("B2C")){
            projectEntity.setProjectTypeId("PMD20180226155312");
            projectEntity.setB2C(true);
        }else{//b2b
            projectEntity.setProjectTypeId("PMD20180226155256");
            projectEntity.setB2C(false);
        }
        projectEntity.setProjectCategory(1);//主项目
        projectEntity.setProjectStatus(1);//进行中
        projectEntity.setCreatePerson("1");
        projectEntity.setCreateDate(new Date());
        projectEntity.setUpdatePerson("1");
        projectEntity.setUpdateDate(new Date());
        projectEntity.setBizTypeId(1);
        projectEntity.setOperateTypeId(1);
        projectEntity.setRelateNcId(row.getCell(0).getStringCellValue());
        return projectEntity;
    }

    public String generateID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 创建流程 返回流程实例ID
     * @return
     */
    public String generateProcess(boolean isB2C){
        String taskId = generateID();
        String instanceId =generateID();
        String executionId = instanceId;
        String defId = null;
        if(isB2C){
            defId = "PMD20180226155312:1:91d10be4a5de462297e5e843c44d0315";
        }else{
            defId = "PMD20180226155256:1:a49420cedd7446fcae2b94ced9e086e5";
        }
        String act_ru_task_sql = "INSERT INTO `ACT_RU_TASK` VALUES ('"+taskId+"', 1, '"+executionId+"', '"+instanceId+"', '"+defId+"', '开仓完成', NULL, NULL, 'task1', NULL, NULL, NULL, 50, '2018-2-26 16:18:46', NULL, NULL, 1, '', NULL);\n";
        String act_ru_execution_sql ="INSERT INTO `ACT_RU_EXECUTION` VALUES ('"+executionId+"', 1, '"+instanceId+"', NULL, NULL, '"+defId+"', NULL, 'task1', 1, 0, 1, 0, 1, 2, '', NULL, NULL);\n";
        String act_hi_taskinst_sql ="INSERT INTO `ACT_HI_TASKINST` VALUES ('"+taskId+"', '"+defId+"', 'task1', '"+instanceId+"', '"+executionId+"', '开仓完成', NULL, NULL, NULL, NULL, '2018-2-26 16:18:46', NULL, NULL, NULL, NULL, 50, NULL, NULL, NULL, '');\n";
        String act_hi_procinst_sql = "INSERT INTO `ACT_HI_PROCINST` VALUES ('"+executionId+"', '"+instanceId+"', NULL, '"+defId+"', '2018-2-26 16:18:46', NULL, NULL, NULL, 'start', NULL, NULL, NULL, '', NULL);\n";
        outputSql(act_ru_execution_sql);
        outputSql(act_ru_task_sql);
        outputSql(act_hi_procinst_sql);
        outputSql(act_hi_taskinst_sql);


        return instanceId;
    }

    public void outputSql(String sql){
        FileWriter fw  =null;
        try {
            fw  = new FileWriter("C:\\Users\\10064749\\Desktop\\result.sql",true);
            fw.write(sql);
        }catch (Exception ex){

        }finally {
            IOUtils.closeQuietly(fw);
        }
    }

}
