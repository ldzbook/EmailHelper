package com.cgnfex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理 : 1、从本地读取文件，将文件中的邮箱以list返回 2、写log文件，将输出的log按行写入文件
 * 
 * @author Administrator
 * 
 */
public class FileProcessor {

	/**
	 * 读取文件
	 * 
	 * @param fileDir
	 * @return 以行为元素的List
	 */
	public static List<String> readFile(String fileDir) {
		List<String> lines = new ArrayList<String>();
		BufferedReader bufR = null;
		File file = new File(fileDir);
		if (file.exists()) {
			try {
				bufR = new BufferedReader(new InputStreamReader(
						new FileInputStream(fileDir),"UTF-8"));
				String line = null;
				while ((line = bufR.readLine()) != null) {
					if (!line.trim().startsWith("#")) {
						lines.add(line.trim());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (bufR != null) {
					try {
						bufR.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		return lines;
	}

	/**
	 * 向fileDir文件，写入一行logLine
	 * 
	 * @param fileDir
	 * @param logLine
	 */
	public static void writeNewLine(String fileDir, String logLine) {
		if (logLine == null) {
			logLine = "";
		}
		BufferedWriter bufW = null;
		checkExit(fileDir);
		try {
			bufW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileDir, true),"GBK"));
			bufW.write(logLine);
			bufW.newLine();
			bufW.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufW != null) {
				try {
					bufW.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 检查并创建路径+文件
	 * 
	 * @param fileDir
	 */
	public static void checkExit(String fileDir) {
		if (fileDir == null) {
			throw new IllegalArgumentException(
					"FileProcessor.checkExit(fileDir)传入值为null");
		}
		int index = fileDir.lastIndexOf("/");
		String pathDir = fileDir.substring(0, index);
		String fileName = fileDir.substring(index + 1);
		// 创建路径
		File path = new File(pathDir);
		if (!path.exists()) {
			path.mkdirs();
		}
		// 创建文件
		File file = new File(fileDir);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
