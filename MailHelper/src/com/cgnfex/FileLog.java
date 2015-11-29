package com.cgnfex;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.text.DateFormatter;

/**
 * Log文件操作类 log_error.txt log_success.txt
 * 
 * @author Administrator
 * 
 */
public class FileLog {

	public static final String DELIMITER = "    ";
	public static final String errorTxt = ConfigContants.APP_PATH
			+ "log_error.txt";
	private static final String successTxt = ConfigContants.APP_PATH
			+ "log_success.txt";
	private static final String COMPLETE = "*****本次邮件发送完成*****\r\n\r\n";

	/**
	 * 将發送成功信息写入log_success.txt
	 * 
	 * @param receiveEmail
	 * @param errorInfo
	 */
	public static void i(final String receiveEmail, final String successInfo) {
		writeTxt(successTxt, receiveEmail, successInfo);
	}

	/**
	 * 将错误信息写入log_error.txt
	 * 
	 * @param receiveEmail
	 * @param errorInfo
	 */
	public static void e(final String receiveEmail, final String errorInfo) {
		writeTxt(errorTxt, receiveEmail, errorInfo);
	}

	/**
	 * 
	 * @param file
	 * @param receiveEmail
	 * @param info
	 */
	private static void writeTxt(final String file, final String receiveEmail,
			final String info) {
		Runnable writeErroRunnable = new Runnable() {
			@Override
			public synchronized void run() {
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date());
				// String receiver = "receiver:" + receiveEmail;
				StringBuffer sBuffer = new StringBuffer();
				sBuffer.append(date).append(DELIMITER).append(receiveEmail)
						.append(DELIMITER).append(info);
				FileProcessor.writeNewLine(file, sBuffer.toString());
			}
		};
		new Thread(writeErroRunnable).start();
	}

	/**
	 * 邮件发送完成
	 */
	public static void complete() {
		Runnable writeErroRunnable = new Runnable() {
			@Override
			public synchronized void run() {
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date());
				FileProcessor.writeNewLine(errorTxt, date + COMPLETE);
				FileProcessor.writeNewLine(successTxt, date + COMPLETE);
				// System.exit(0);
			}
		};
		new Thread(writeErroRunnable).start();
	}
}
