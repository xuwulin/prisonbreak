package com.xwl.prisonbreak.common.util.poiExcel;


import com.xwl.prisonbreak.common.util.poiExcel.verify.*;

/**
 * 导入用户校验类
 * 
 * @author Administrator
 *
 */
public class ProjectVerifyBuilder extends AbstractVerifyBuidler {

	private static ProjectVerifyBuilder builder = new ProjectVerifyBuilder();

	public static ProjectVerifyBuilder getInstance() {
		return builder;
	}

	/**
	 * 定义列校验实体：提取的字段、提取列、校验规则
	 */
	private ProjectVerifyBuilder() {
		cellEntitys.add(new CellVerifyEntity("projectName", "B", new StringVerify("项目名称", true)));
		cellEntitys.add(new CellVerifyEntity("areaName", "C", new StringVerify("所属区域", true)));
		cellEntitys.add(new CellVerifyEntity("province", "D", new StringVerify("省份", true)));
		cellEntitys.add(new CellVerifyEntity("city", "E", new StringVerify("市", true)));
		cellEntitys.add(new CellVerifyEntity("people", "F", new StringVerify("项目所属人", true)));
		cellEntitys.add(new CellVerifyEntity("leader", "G", new StringVerify("项目领导人", true)));
		cellEntitys.add(new CellVerifyEntity("scount", "H", new IntegerVerify("总分", true)));
		cellEntitys.add(new CellVerifyEntity("avg", "I", new DoubleVerify("历史平均分", true)));
		cellEntitys.add(new CellVerifyEntity("createTime", "J", new DateTimeVerify("创建时间", "yyyy-MM-dd HH:mm", true)));
		cellEntitys.add(new CellVerifyEntity("img", "K", new ImgVerify("图片", false)));
		// 必须调用
		super.init();
	}
}
