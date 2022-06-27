package com.intsmaze.flink.client.constant;
/**
 * 手术数据同步模型V2
 * 动作码定义
 */
public class SyncOperationInfoV2Constant {
	/**
	 * 手术排班
	 */
	public static final String CREATEMASTER = "N001"; 
	/**
	 * 手术取消(手术停台)
	 */
	public static final String CANCELMASTER = "D001"; 
	/**
	 * 工作人员修改
	 */
	public static final String UPDATESTAFF = "U_SU"; 
	/**
	 * 工作人员交班
	 */
	public static final String HANDOVERSTAFF = "U_SH"; 
	/**
	 * 手术节点时间修改
	 */
	public static final String NODETIME = "U_OT"; 
	/**
	 * 手术状态修改
	 */
	public static final String STATUSTIME = "U_OS"; 
	/**
	 * 手术名称修改
	 */
	public static final String UPDATEOPERATIONNANE = "U_ON"; 
	/**
	 * 手术信息修改
	 */
	public static final String UPDATEOPERATIONINFO = "U_OI";
	
	/**
	 * 手术信息新增
	 */
	public static final String CREATEOPERATIONINFO = "N_ON";
	
	/**
	 * 抗生素
	 */
	public static final String N_AE = "N_AE";
	
	
	public static final String N_OV = "N_OV";
	
	
	
}
