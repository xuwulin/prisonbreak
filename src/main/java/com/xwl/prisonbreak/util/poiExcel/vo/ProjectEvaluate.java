package com.xwl.prisonbreak.util.poiExcel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述 整体评价
 * @author
 * @version 2017-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEvaluate implements Serializable {

	private static final long serialVersionUID = 1L;

	private String people;
	private String leader;
	private Double avg;
	private byte[] img;


	private Long id;//主键

	private Long projectId;//项目ID

	private String areaInfo;//区位条件

	private String resourceInfo;//资源禀赋

	private String manageInfo;// 经营现状

	private String reviewInfo;//考察印象

	private String teamInfo;//管理团队

	private String potentialInfo;//成长潜力

	private Long createUserId;//创建人

	private Date createTime;//创建时间

	private Double areaScore;//区位分数

	private Double resourceScore;//资源分数

	private Double manageScore;//经营分数

	private Double reviewScore;//考察分数

	private Double teamScore;//管理分数

	private Double potentialScore;//成长分数

	private String projectName;//项目名称

	private String ids;//导出参数

	private String areaName;//所属区域

	private String province;//省

	private String city;//市

	private String statusName;//项目状态

	private Integer scount;//总分

	private String keyWords;//关键词

	private Integer IsCurrent;//标记是否为当前数据:1当前数据,0历史数据

	private Long flowId;//流程ID


}
