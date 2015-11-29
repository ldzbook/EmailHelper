package com.cgnfex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

import com.cgnfex.bean.BaseEmail;

/**
 * 发送邮件任务
 * 
 * @author Administrator
 * 
 */
public class SendEmailTask implements Callable<String>, ConnectionListener {

	private String fromAccount;
	private String fromPwd;
	private Message message;
	private Session session;
	String toAcc = new String();

	public SendEmailTask(String fromAccount, String fromPwd,
			ArrayList<String> toAccounts, String subject,
			HashMap<String, String> body) {
		if (fromAccount == null || "".equals(fromAccount)) {
			throw new IllegalArgumentException("发件人不能为空");
		}
		if (fromPwd == null || "".equals(fromPwd)) {
			throw new IllegalArgumentException("发件人密码不能为空");
		}
		this.fromAccount = fromAccount;
		this.fromPwd = fromPwd;
		for (String acc : toAccounts) {
			toAcc += acc + ",";
			if (toAccounts.indexOf(acc) >= toAccounts.size() - 1) {
				toAcc = toAcc.substring(0, toAcc.length() - 1);
			}
		}
		synchronized (SendEmailTask.class) {
			session = getSession();
			message = new BaseEmail(session, fromAccount, toAccounts, subject,
					body).create();
		}
		if (message == null) {
			throw new IllegalArgumentException("邮件对象为null");
		}
	}


	public synchronized void run() {

		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(false);
		try {

			// 2、通过session得到transport对象
			Transport ts = session.getTransport();
			// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
			ts.connect(Constants.DEFAULT_HOST, fromAccount, fromPwd);
			// 4、创建邮件
			// Message message = new BaseEmail(session, fromAccount, toAccounts,
			// null, body).create();
			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			ts.addConnectionListener(this);
			ts.close();
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			// System.out.println("输入有误");
			FileLog.e(toAcc, "输入有误");
			throw new IllegalArgumentException("输入有误");
		}
	}

	private Session getSession() {
		Properties prop = new Properties();
		prop.setProperty("mail.host", Constants.DEFAULT_HOST);
		prop.setProperty("mail.transport.protocol", Constants.DEFAULT_PROTOCOL);
		prop.setProperty("mail.smtp.auth", "true");
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		return Session.getInstance(prop);
	}

	/**
	 * 连接监听，只有传输结束时调用closed()
	 */
	@Override
	public void closed(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Transport closed");
		
	}

	@Override
	public void disconnected(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Transport disconnected");
	}

	@Override
	public void opened(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Transport opened");

	}

	@Override
	public String call() throws Exception {
		run();
		return toAcc;
	}

}
