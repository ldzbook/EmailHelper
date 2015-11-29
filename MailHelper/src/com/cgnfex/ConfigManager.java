package com.cgnfex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.omg.CORBA.portable.ValueBase;

import com.cgnfex.bean.Config;

/**
 * 配置
 * 如果没有配置config文件，将会抛出异常
 * @author Administrator
 *
 */
public class ConfigManager {
	private static String configFile = ConfigContants.APP_PATH + "config";
	private Config config;
	 ;

	public ConfigManager() {
		ArrayList<String> lines = (ArrayList<String>) FileProcessor
				.readFile(configFile);
		Map<String, String> configMap = new HashMap<String, String>();
		if (lines!=null&&lines.size()>0) {
			int size = lines.size();
			for (int i = 0; i < size; i++) {
				String line = lines.get(i);
				int sep = line.indexOf(":");
				String value = line.substring(sep + 1);
				if (value==null||"".equals(value)) {
					FileProcessor.writeNewLine(FileLog.errorTxt, "请配置D:\\EmailHelper\\config文件");
					throw new IllegalArgumentException("请配置D:\\EmailHelper\\config文件");
				}
				configMap.put(line.substring(0, sep).toLowerCase(), value);
			}
			config = new Config(configMap.get(ConfigContants.HOST),
					configMap.get(ConfigContants.PEOTOCOL),
					configMap.get(ConfigContants.SEND_EMAIL),
					configMap.get(ConfigContants.PWD));
		}else {
			throw new IllegalArgumentException("请配置D:\\EmailHelper\\config文件");
		}

	}
	


	public Config getConfig() {
		return config;
	}



	/**
	 * 检查配置文件
	 * 
	 * @param lines
	 * @return
	 */
	public static void CheckConfigFile() {
		File file = new File(configFile);
		if (!file.exists()) {
			/*
			 * host:smtp.qq.com protocol:smtp sendEmail: password:
			 */
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("host:smtp.qq.com").append(Constants.NEWLINE).append(
					"protocol:smtp").append(Constants.NEWLINE).append("sendEmail:")
					.append(Constants.NEWLINE).append("password:");
			FileProcessor.writeNewLine(configFile, sBuilder.toString());
		}
	}
}
