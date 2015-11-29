package com.cgnfex;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cgnfex.bean.Content;

public class ContentManager {

	private static final String SEPARATOR = ":::";
	private static String contentFile = ConfigContants.APP_PATH + "content.txt";
	ArrayList<Content> contentList;

	public ContentManager() {
		ArrayList<String> contents = (ArrayList<String>) FileProcessor
				.readFile(contentFile);
		contentList = new ArrayList<Content>();
		int contentSize = 0;
		if (contents != null && (contentSize = contents.size()) > 0) {
			for (int i = 0; i < contentSize; i++) {
				String content = contents.get(i);
				String[] conts = content.split(SEPARATOR);
				if (conts != null && conts.length == 3) {
					List<String> emailList = new ArrayList<String>();
					String[] emails = conts[0].split(",");
					if (emails != null && emails.length >= 1) {
						emailList.addAll(Arrays.asList(emails));
					}
					String subject = conts[1];
					String[] bodies = conts[2].split(",");
					Map<String, String> bodyMap = new HashMap<String, String>();
					for (String body : bodies) {
						int sep = body.indexOf("=");
						String value = body.substring(0, sep).trim();
						if (!Constants.BODYTYPE_TEXT.equals(value)
								&& !Constants.BODYTYPE_ATTACH.equals(value)) {
							FileLog.e(conts[0], "邮件类型只能是“text”或“attach”");
							throw new IllegalArgumentException(
									"邮件类型只能是“text”或“attach”");
						}
						bodyMap.put(body.substring(sep + 1),value );
					}
					contentList.add(new Content(emailList, subject, bodyMap));
				}
			}
		} else {
			throw new IllegalArgumentException("请配置D:\\EmailHelper\\content.txt发送内容");
		}
	}

	public ArrayList<Content> getContentList() {
		return contentList;
	}

	public static void checkContentFile() {
		File contFile = new File(contentFile);
		if (!contFile.exists()) {
			FileProcessor.checkExit(contentFile);
			StringBuffer contentTips = new StringBuffer();
			contentTips
					.append("# 格式：")
					.append(Constants.NEWLINE)
					.append("# receiveEmails:::subject:::body")
					.append(Constants.NEWLINE)
					.append("# 注意：")
					.append(Constants.NEWLINE)
					.append("# 1、编辑过程中的分隔符号全部为英文状态下，内容可以是中文")
					.append(Constants.NEWLINE)
					.append("# 2、多个单元中间使用\",\"隔开")
					.append(Constants.NEWLINE)
					.append("# 3、文件与本软件在同一目录时，可直接用相对路径；不同路径时，使用绝地路径")
					.append(Constants.NEWLINE)
					.append("# 4、路径表示：使用\"/\"或\"\\\"")
					.append(Constants.NEWLINE)
					.append("# 例子：")
					.append(Constants.NEWLINE)
					.append(
							"# email1,email2,email3:::this is subject:::text=这是文本内容,attach=D:\\\\dir\\\\a.pdf,attach=D:/dir2/b.xls");

			FileProcessor.writeNewLine(contentFile, contentTips.toString());
		}
	}

}
