package com.cgnfex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.cgnfex.bean.Config;
import com.cgnfex.bean.Content;
import com.cgnfex.exception.HandlerException;
import com.cgnfex.view.CustomDialog;

public class EmailHelper {

	
	public static JFrame mainPain;
	/**
	 * 首次运行创建配置文件 再次运行，则发送邮件 1、初始化检查文件 2、从文件读取数据 3、创建线程池发送邮件
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * 视图
		 */
		mainPain = new CustomDialog("正在帮您发送邮件...", "EmailHelper");
		/*
		 * 全局异常
		 */
		HandlerException handlerException = new HandlerException();
		Thread.currentThread().setDefaultUncaughtExceptionHandler(handlerException);
		
		/*
		 * 检查配置文件
		 */
		ConfigManager.CheckConfigFile();
		ContentManager.checkContentFile();
		
		/*
		 * 获取配置
		 */
		ConfigManager configManager = new ConfigManager();
		Config config = configManager.getConfig();
		if (config == null) {
			throw new IllegalArgumentException("从D:\\EmailHelper\\content.txt文件获取的Config对象为null");
		}

		/*
		 * 获取内容
		 */
		ContentManager conManager = new ContentManager();
		ArrayList<Content> contents = conManager.getContentList();
		int conSize = 0;
		if (contents == null || (conSize = contents.size()) <= 0) {
			throw new IllegalArgumentException("从D:\\EmailHelper\\content.txt文件获取的Content为空");
		}

		/*
		 * 创建线程发送邮件
		 */
		ExecutorService executor = Executors.newFixedThreadPool(10);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();
		for (int i = 0; i < conSize; i++) {
			Content content = contents.get(i);
			SendEmailTask sendEmailTask = new SendEmailTask(config
					.getSendEmail(), config.getPassword(),
					(ArrayList<String>) content.getEmails(), content
							.getSubject(), (HashMap<String, String>) content
							.getBody());

			// executor.execute(sendEmailTask);
			tasks.add((Callable<String>) sendEmailTask);
		}

		try {
			List<Future<String>> results = executor.invokeAll(tasks);
			for (Future<String> future : results) {
				FileLog.i(future.get(), "邮件发送完成");
			}
			
			/*
			 * 邮件发送完成
			 */
			Thread.sleep(500);
			FileLog.complete();

			JOptionPane.showMessageDialog(mainPain,
					"EmailHelper运行结束，请到D：\\EmailHelper下检查结果", "EmailHelper",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
