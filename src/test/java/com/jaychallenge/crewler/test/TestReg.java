package com.jaychallenge.crewler.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestReg {

	public static void main(String[] args) {
			URI uriroot=URI.create("http://p1.17ll.com/apply/pc/?go=meiwen-design-new--554-1-2-1.html?appopen2=yes%26appopen3=yes%26from=singlemessage%26isappinstalled=1");
			URI uriget=uriroot.resolve("../ico/s1.png");
			System.out.println(uriget);
	}

	public static void testJsoup() {
		Document doc;
		try {
			doc = Jsoup.connect("https://mp.weixin.qq.com/s/ozeZC7Vu_pijaUDjWFF-cQ").get();
//			System.out.println(doc.html());
			System.out.println(doc.getElementsByTag("img").size());
			for(Element e:doc.getElementsByTag("img")) {
				System.out.println(e.attr("src"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void testHttpget() {
		// ��ȡ��ҳ����
				String urlstring = "https://mp.weixin.qq.com/s/r5xfldrOnvwe2HEz8qREFg";
				URI uriroot = URI.create(urlstring);
				CloseableHttpClient chc = HttpClients.createDefault();
				HttpGet hg = new HttpGet(uriroot);
				try {
					HttpResponse hr = chc.execute(hg);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					HttpEntity he = hr.getEntity();
					he.writeTo(bos);
					Header charset = he.getContentEncoding();
					String htmlcontent = "";
					if (charset == null) {
						String html = bos.toString();
						String charset1 = html.substring(html.indexOf("charset=") + 8,
								html.indexOf(">", html.indexOf("charset=") + 8));
						if (charset1.indexOf("'") > 0) {
							charset1 = charset1.substring(0, charset1.indexOf("'"));
						}
						if (charset1.indexOf('"') > 0) {
							charset1 = charset1.substring(0, charset1.indexOf('"'));
						}
						htmlcontent = bos.toString(charset1);
					} else {
						htmlcontent = bos.toString(charset.getValue());
					}
					// ��ȡ��Ҫ���ص��ļ��ĵ�ַ
					Matcher matcher = Pattern.compile("[^\\w/:\\.](([\\w/:.]+\\.jpg)|([\\w/:\\.]+\\.png))[^\\w/:.]").matcher(htmlcontent);
					while (matcher.find()) {
						// �����ļ�
						String downloadfile = matcher.group();
						System.out.println(downloadfile);
//						String downloadsite = downloadfile.split("[^w/:\\.]")[0];
//						String filename = downloadsite.substring(downloadsite.lastIndexOf('/')>0?downloadsite.lastIndexOf('/'):0);
						String[] names=downloadfile.split("[^\\w/:\\.]");
						for(int i=0;i<names.length;i++) {
							System.out.println(i+"------"+names[i]);
						}
//						File dir = new File("E:/testdown");
//						File file = new File(dir, filename);
//						// ������������
//						if (downloadsite.startsWith("http")) {
//							HttpGet hgdownload = new HttpGet(downloadsite);
//							HttpResponse hrdownload = chc.execute(hgdownload);
//							hrdownload.getEntity().writeTo(new FileOutputStream(file));
//						}else {
//							URI uridownload=uriroot.relativize(new URI(downloadsite));
//							HttpGet hgdownload = new HttpGet(uridownload);
//							HttpResponse hrdownload = chc.execute(hgdownload);
//							hrdownload.getEntity().writeTo(new FileOutputStream(file));
//						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

}
