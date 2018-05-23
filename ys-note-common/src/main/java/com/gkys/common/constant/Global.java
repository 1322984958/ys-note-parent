package com.gkys.common.constant;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class Global {
	/**
	 *  下载的路径
	* @param groupFile 分组文件夹名
	 */
	public static String getDownloadRootPath(String groupFile){
		String dateFile = DateFormatUtils.format(new Date(), "yyMMdd");
		StringBuilder builder = new StringBuilder();
		builder.append(SysPropertiesConstant.getValue("download.rootPath")+"/");
		builder.append(groupFile+"/");
		builder.append(dateFile+"/");
		return builder.toString();
	}
	
	public static String getUploadRootPath(String groupFile){
		String dateFile = DateFormatUtils.format(new Date(), "yyMMdd");
		StringBuilder builder = new StringBuilder();
		builder.append(SysPropertiesConstant.getValue("upload.rootPath")+"/");
		builder.append(groupFile+"/");
		builder.append(dateFile+"/");
		return builder.toString();
	}
	
}
