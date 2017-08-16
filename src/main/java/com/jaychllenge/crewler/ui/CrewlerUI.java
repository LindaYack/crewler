package com.jaychllenge.crewler.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CrewlerUI extends ApplicationWindow {
	private Text text;
	private Text text_1;

	/**
	 * Create the application window.
	 */
	public CrewlerUI() {
		super(null);
		createActions();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		Label label = new Label(container, SWT.NONE);
		label.setBounds(10, 10, 61, 17);
		label.setText("\u7F51\u5740\uFF1A");

		text = new Text(container, SWT.BORDER);
		text.setBounds(88, 4, 336, 23);

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(10, 51, 61, 17);
		label_1.setText("\u4E0B\u8F7D\u76EE\u5F55\uFF1A");

		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(88, 48, 237, 23);

		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dd = new DirectoryDialog(getShell());
				text_1.setText(dd.open());
			}
		});
		btnNewButton.setBounds(344, 46, 80, 27);
		btnNewButton.setText("\u9009\u62E9\u76EE\u5F55");

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setBounds(10, 91, 61, 17);
		label_2.setText("\u4E0B\u8F7D\u683C\u5F0F\uFF1A");

		Group group = new Group(container, SWT.NONE);
		group.setBounds(7, 115, 417, 63);

		final Button btnjpg = new Button(group, SWT.CHECK);
		btnjpg.setSelection(true);
		btnjpg.setBounds(3, 17, 72, 17);
		btnjpg.setText("*.jpg");

		final Button btnpng = new Button(group, SWT.CHECK);
		btnpng.setBounds(81, 17, 72, 17);
		btnpng.setText("*.png");

		final Button btnjs = new Button(group, SWT.CHECK);
		btnjs.setBounds(237, 17, 72, 17);
		btnjs.setText("*.js");

		final Button btncss = new Button(group, SWT.CHECK);
		btncss.setBounds(315, 17, 72, 17);
		btncss.setText("*.css");

		final Button btngif = new Button(group, SWT.CHECK);
		btngif.setBounds(159, 17, 72, 17);
		btngif.setText("*.gif");

		Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// 设置下载的文件格式
				StringBuffer sb = new StringBuffer("[^\\w/:\\.](");
				if (btnjpg.getSelection()) {
					sb.append("([\\w/:\\.]+\\.jpg)|");
				}
				if (btnpng.getSelection()) {
					sb.append("([\\w/:\\.]+\\.png)|");
				}
				if (btnjs.getSelection()) {
					sb.append("([\\w/:\\.]+\\.js)|");
				}
				if (btncss.getSelection()) {
					sb.append("([\\w/:\\.]+\\.css)|");
				}
				if (btngif.getSelection()) {
					sb.append("([\\w/:\\.]+\\.gif)|");
				}
				if (sb.charAt(sb.length() - 1) == '|') {
					sb.deleteCharAt(sb.length() - 1);
				}
				sb.append(")[^\\w/:\\.]");
				// System.out.println(sb.toString());
				// 获取网页文字
				String urlstring = text.getText();
				URI uriroot = URI.create(urlstring);
				CloseableHttpClient chc = HttpClients.createDefault();
				HttpGet hg = new HttpGet(uriroot);
				FileOutputStream fos = null;
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
					// 获取需要下载的文件的地址
					Matcher matcher = Pattern.compile(sb.toString()).matcher(htmlcontent);
					while (matcher.find()) {
						// 下载文件
						String downloadfile = matcher.group();
						System.out.println(downloadfile);
						String downloadsite = downloadfile.split("[^\\w/:\\.]")[1];
						String filename = downloadsite
								.substring(downloadsite.lastIndexOf('/') > 0 ? downloadsite.lastIndexOf('/') : 0);
						File dir = new File(text_1.getText());
						File file = new File(dir, filename);
						// 发送网络连接
						fos = new FileOutputStream(file);
						if (downloadsite.startsWith("http")) {
							HttpGet hgdownload = new HttpGet(downloadsite);
							HttpResponse hrdownload = chc.execute(hgdownload);
							hrdownload.getEntity().writeTo(new FileOutputStream(file));
						} else if (downloadsite.startsWith("//")) {
							downloadsite = "http:" + downloadsite;
							HttpGet hgdownload = new HttpGet(downloadsite);
							HttpResponse hrdownload = chc.execute(hgdownload);
							hrdownload.getEntity().writeTo(new FileOutputStream(file));
						} else {
							URI uridownload = uriroot.relativize(new URI(downloadsite));
							HttpGet hgdownload = new HttpGet(uridownload);
							HttpResponse hrdownload = chc.execute(hgdownload);
							hrdownload.getEntity().writeTo(new FileOutputStream(file));
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		button.setBounds(117, 193, 80, 27);
		button.setText("\u6293\u53D6");
		container.pack();

		Button btnimg = new Button(container, SWT.NONE);
		btnimg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// 获取网页文字
				String urlstring = text.getText();
				URI uriroot = URI.create(urlstring);
				CloseableHttpClient chc = HttpClients.createDefault();
				FileOutputStream fos = null;
				try {
					Document doc = Jsoup.connect(urlstring).get();
					Elements imgs = doc.getElementsByTag("img");
					for (Element e : imgs) {
						if (!StringUtil.isBlank(e.attr("src"))) {
							System.out.println(e.attr("src"));
							String uri="";
							if(e.attr("src").startsWith("//")) {
								uri="http:"+e.attr("src");
							}
							URI uridownload = uriroot.relativize(URI.create(uri));
							HttpGet hgdownload = new HttpGet(uridownload);
							HttpResponse hrdownload = chc.execute(hgdownload);
							File dir = new File(text_1.getText());
							String filename = uridownload.getRawPath()
									.substring(uridownload.getRawPath().lastIndexOf('/') > 0
											? uridownload.getRawPath().lastIndexOf('/')
											: 0);
							File file = new File(dir, filename);
							hrdownload.getEntity().writeTo(new FileOutputStream(file));
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		btnimg.setBounds(218, 193, 80, 27);
		btnimg.setText("\u6293\u53D6img\u6807\u7B7E");
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			CrewlerUI window = new CrewlerUI();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("爬虫");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(444, 272);
	}
}
