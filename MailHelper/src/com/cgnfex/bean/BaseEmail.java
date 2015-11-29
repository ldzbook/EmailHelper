package com.cgnfex.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.text.StringContent;
import javax.xml.soap.Text;

import com.cgnfex.Constants;

/**
 * 邮件的基类
 * 
 * @author Administrator
 * 
 */
public class BaseEmail extends MimeMessage {
	private String fromAccount;
	private String toAccount;
	private String subject;
	private HashMap<String, String> body;

	private MimeMultipart mMulPart = null;

	/**
	 * 构造方法
	 * 
	 * @param fromAccount
	 * @param toAccout
	 */
	public BaseEmail(Session session, String fromAccount,
			ArrayList<String> toAccounts) {
		this(session, fromAccount, toAccounts, "无主题", null);
	}

	/**
	 * 构造方法
	 * 
	 * @param fromAccount
	 * @param toAccounts
	 * @param subject
	 * @param body
	 */
	public BaseEmail(Session session, String fromAccount,
			ArrayList<String> toAccounts, String subject,
			HashMap<String, String> body) {
		super(session);
		initFiled();
		if (fromAccount == null) {
			throw new IllegalArgumentException("创建Email对象时必须指定发件人");
		}
		if (toAccounts == null || toAccounts.size() <= 0) {
			throw new IllegalArgumentException("创建Email对象时必须指定收件人");
		}
		if (subject == null) {
			subject = "无主题";
		}
		// 初始化Email的参数
		try {
			initEmail(fromAccount, toAccounts, subject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (body == null) {
			body = new HashMap<String, String>();
			body.put(Constants.BODYTYPE_TEXT, "");
		}
		addBodyPart(body);
	}

	/**
	 * 初始化Email的参数
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param subject
	 * @throws Exception
	 */
	private void initEmail(String fromAccount, ArrayList<String> toAccounts,
			String subject) throws Exception {
		this.setFrom(new InternetAddress(fromAccount));
		for (String toAcc : toAccounts) {
			this.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toAcc));
		}
		this.setSubject(subject);
	}

	/**
	 * 初始化全局变量
	 */
	private void initFiled() {
		// TODO Auto-generated method stub
		mMulPart = new MimeMultipart();
	}

	/**
	 * 添加body 1遍历hashmap 2判断text、image、attach 3创建MimeBodyPart
	 * 4将MimeBodyPart对象添加到MaimeMultiPart
	 * 
	 * @param bodyPart
	 */
	private void addBodyPart(HashMap<String, String> bodyPart) {
		Iterator<Map.Entry<String, String>> iterator = bodyPart.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
					.next();
			MimeBodyPart body = new MimeBodyPart();
			String flag = entry.getValue();
			
			String content = null;

			try {
				// content = MimeUtility.decodeText(entry.getKey());
//				content = new String(entry.getKey().getBytes("ISO-8859-1"),"gb2312");
				 content = entry.getKey();
//				 System.out.println(1+content);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

				if (Constants.BODYTYPE_TEXT.equals(flag)) {
					if (content == null) {
						content = "";
					}
					// 文本信息
					body.setContent(content, "text/html;charset=UTF-8");
				} else if (Constants.BODYTYPE_IMAGE.equals(flag)) {
					if (content == null) {
						throw new IllegalArgumentException("image的路径不能为null");
					}
					// 准备图片数据
					DataHandler dh = new DataHandler(
							new FileDataSource(content));
					body.setDataHandler(dh);
					body.setContentID(System.currentTimeMillis() + ".png");
					// mMulPart.setSubType("related");
				} else if (Constants.BODYTYPE_ATTACH.equals(flag)) {
					if (content == null) {
						throw new IllegalArgumentException("image的路径不能为null");
					}
					// 创建邮件附件
					DataHandler dh = new DataHandler(
							new FileDataSource(content));
					body.setDataHandler(dh);
//					System.out.println(2+dh.getName());
//					body.setFileName(new String(dh.getName().getBytes("text/html;charset=UTF-8")));
					body.setFileName(MimeUtility.encodeText(dh.getName(),"gbk", "B"));

					// mMulPart.setSubType("mixed");
				} else {
					throw new IllegalArgumentException("传入Email的BodyPart类型错误");
				}
				// 将这个消息part添加到消息体
				mMulPart.addBodyPart(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public BaseEmail create() {
		try {
			this.setContent(mMulPart);
			this.saveChanges();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return this;
	}

	
	public static String getISOFileName(MimeBodyPart body) {
		// 设置一个标志，判断文件名从Content-Disposition中获取还是从Content-Type中获取   
		boolean flag = true;
		if (body == null) {
			return null;
		}
		String[] cdis;
		try {
			cdis = body.getHeader("Content-Disposition");
		} catch (Exception e) {
			return null;
		}
		if (cdis == null) {
			flag = false;
		}
		if (!flag) {
			try {
				cdis = body.getHeader("Content-Type");
			} catch (Exception e) {
				return null;
			}
		}
		if (cdis == null) {
			return null;
		}
		if (cdis[0] == null) {
			return null;
		}// 从Content-Disposition中获取文件名
		if (flag) {
			int pos = cdis[0].indexOf("filename=");
			if (pos < 0) {
				return null;
			}// 如果文件名带引号
			if (cdis[0].charAt(cdis[0].length() - 1) == '"') {
				return cdis[0].substring(pos + 10, cdis[0].length() - 1);
			}
			return cdis[0].substring(pos + 9, cdis[0].length());
		} else {
			int pos = cdis[0].indexOf("name=");
			if (pos < 0) {
				return null;
			}// 如果文件名带引号
			if (cdis[0].charAt(cdis[0].length() - 1) == '"') {
				return cdis[0].substring(pos + 6, cdis[0].length() - 1);
			}
			return cdis[0].substring(pos + 5, cdis[0].length());
		}
	}
}
