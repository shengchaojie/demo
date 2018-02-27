package com.scj.projectimport;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectEntity {

    private Long id;

    /**
     * 项目编号
     */
    private String projectNumber;

    /**
     * 项目发起人
     */
    private String projectSponsor;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目负责人
     */
    private String projectLeader;

    /**
     * 项目开始时间
     */
    private Date projectStartTime;

    /**
     * 项目结束时间
     */
    private Date projectEndTime;

    /**
     * 项目类型Id
     */
    private String projectTypeId;

    private boolean isB2C;

    /**
     * 父项目Id
     */
    private Long projectParentId;

    /**
     * 项目流程Id
     */
    private String projectProcessId;

    /**
     * 项目最新流程环节
     */
    private String projectProcessLink;

    /**
     * 项目类别
     */
    private Integer projectCategory;

    /**
     * 项目状态
     */
    private Integer projectStatus;

    /**
     * 创建者
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新者
     */
    private String updatePerson;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 终止项目原因
     */
    private String endProjectReason;

    /**
     * 关联客户ID
     */
    private Long relateCustomerId;

    /**
     * 业务类型ID
     * @return
     */
    private Integer bizTypeId;

    /**
     * 运营类型ID
     * @return
     */
    private Integer operateTypeId;

    /**
     * 关联仓库ID
     */
    private String relateWarehouseId;

    /**
     * 关联NC系统ID
     */
    private String relateNcId;
}