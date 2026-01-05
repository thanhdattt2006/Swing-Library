package apps;

import models.MailModel;

public class MailCheck {
	public static void main (String[] agrs) {
		String from = "thanhdattt2006@gmail.com";
		String to = "thanhdattt2006@gmail.com";
		String subject = "Hello";
		String content = "Cái đụ con đỉ mẹ mày";
		MailModel mailModel = new MailModel();
		if (mailModel.send(from, to, subject, content)) {
			System.out.println("Success");
		}	else {
			System.out.println("Failed");
		}
	}
}
