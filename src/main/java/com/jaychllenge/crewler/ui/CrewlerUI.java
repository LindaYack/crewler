package com.jaychllenge.crewler.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.custom.ScrolledComposite;

public class CrewlerUI extends ApplicationWindow {
	private Text text;
	private Text text_1;
	private Button btnjpg;
	private Button btnpng;
	private Button btnjs;
	private Button btncss;
	private Button btngif;
	private Button button;
	private Button btnimg;
	private TextViewer textViewer;

	/**
	 * Create the application window.
	 */
	public CrewlerUI() {
		super(null);
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

		btnjpg = new Button(group, SWT.CHECK);
		btnjpg.setSelection(true);
		btnjpg.setBounds(3, 17, 72, 17);
		btnjpg.setText("*.jpg");

		btnpng = new Button(group, SWT.CHECK);
		btnpng.setBounds(81, 17, 72, 17);
		btnpng.setText("*.png");

		btnjs = new Button(group, SWT.CHECK);
		btnjs.setBounds(237, 17, 72, 17);
		btnjs.setText("*.js");

		btncss = new Button(group, SWT.CHECK);
		btncss.setBounds(315, 17, 72, 17);
		btncss.setText("*.css");

		btngif = new Button(group, SWT.CHECK);
		btngif.setBounds(159, 17, 72, 17);
		btngif.setText("*.gif");

		button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				button.setEnabled(false);
				Job buttonJob = new Job("buttonJob") {
					@Override
					protected IStatus run(IProgressMonitor arg0) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								zhuaTu();
							}
						});
						return Status.OK_STATUS;
					}
				};
				buttonJob.setUser(true);
				buttonJob.schedule();
			}
		});
		button.setBounds(117, 193, 80, 27);
		button.setText("\u6293\u53D6");
		container.pack();

		btnimg = new Button(container, SWT.NONE);
		btnimg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnimg.setEnabled(false);
				Job btnimgJob = new Job("imgJob") {

					@Override
					protected IStatus run(IProgressMonitor arg0) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								zhuaImgTu();
							}
						});
						return Status.OK_STATUS;
					}

				};
				btnimgJob.setUser(true);
				btnimgJob.schedule();
			}
		});
		btnimg.setBounds(218, 193, 80, 27);
		btnimg.setText("\u6293\u53D6img\u6807\u7B7E");

		ScrolledComposite scrolledComposite = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 226, 408, 151);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		textViewer = new TextViewer(scrolledComposite, SWT.MULTI | SWT.V_SCROLL);
		StyledText styledText = textViewer.getTextWidget();
		scrolledComposite.setContent(styledText);
		scrolledComposite.setMinSize(styledText.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return container;
	}

	private String urlstring;

	private void zhuaTu() {
		// 设置下载的文件格式
		StringBuffer sb = new StringBuffer("[^\\w/:\\.](");
		if (btnjpg.getSelection()) {
			sb.append("([\\w/:\\._]+\\.jpg)|");
		}
		if (btnpng.getSelection()) {
			sb.append("([\\w/:\\._]+\\.png)|");
		}
		if (btnjs.getSelection()) {
			sb.append("([\\w/:\\._]+\\.js)|");
		}
		if (btncss.getSelection()) {
			sb.append("([\\w/:\\._]+\\.css)|");
		}
		if (btngif.getSelection()) {
			sb.append("([\\w/:\\._]+\\.gif)|");
		}
		if (sb.charAt(sb.length() - 1) == '|') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(")[^\\w/:\\.]");
		// 获取网页文字
		urlstring = text.getText();
		URI uriroot = URI.create(urlstring);
		// 下载进程记录
		StringBuilder downloadProgress = new StringBuilder();
		StringBuilder error = new StringBuilder();
		boolean errorflag = false;
		try {
			String htmlcontent = Jsoup.connect(urlstring).get().html();
			// 获取需要下载的文件的地址
			Matcher matcher = Pattern.compile(sb.toString()).matcher(htmlcontent);

			// 防重复下载
			List<String> hasdown = new LinkedList<String>();
			while (matcher.find()) {
				// 下载文件
				String downloadfile = matcher.group();
				final String downloadsite = downloadfile.split("[^\\w/:\\.]")[1];
				// 发送网络连接
				final URI uridownload = uriroot.resolve(downloadsite);
				if (hasdown.contains(uridownload.toString())) {
					downloadProgress.append("'").append(uridownload).append("' has downloaded.\n");
				} else {
					hasdown.add(uridownload.toString());
					CloseableHttpClient chc = HttpClients.createDefault();
					// 开始下载
					String filename = downloadsite
							.substring(downloadsite.lastIndexOf('/') > 0 ? downloadsite.lastIndexOf('/') : 0);
					File dir = new File(text_1.getText());
					File file = new File(dir, filename);
					FileOutputStream fos = null;

					try {
						downloadProgress.append("downloading '").append(uridownload).append("' ...\n");
						HttpGet hgdownload = new HttpGet(uridownload);
						// 模拟referer破解防盗链
						hgdownload.addHeader("origin", urlstring);
						hgdownload.addHeader("referer", urlstring);
						HttpResponse hrdownload = chc.execute(hgdownload);
						System.out.println(hrdownload.getStatusLine().getStatusCode() + "-----" + filename);
						if (hrdownload.getStatusLine().getStatusCode()==200&&hrdownload.getEntity().getContentLength() > 0) {
							fos = new FileOutputStream(file);
							hrdownload.getEntity().writeTo(fos);
							fos.flush();
							downloadProgress.append("'").append(uridownload).append("' has downloaded.\n");
						} else {
							downloadProgress.append("'").append(uridownload).append("' can't download!\n");
						}
					} catch (Exception e) {
						error.append("download '").append(uridownload).append("' error:").append(e.getMessage())
								.append('\n');
						errorflag = true;
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
			}
			textViewer.setDocument(new org.eclipse.jface.text.Document(downloadProgress.toString()));
			textViewer.refresh();
			if (errorflag) {
				MessageDialog md = new MessageDialog(getShell(), "错误", null, error.toString(), MessageDialog.ERROR,
						new String[] { "OK" }, 0);
				md.open();
			} else {
				MessageDialog md = new MessageDialog(getShell(), "成功", null, "抓取成功！", MessageDialog.INFORMATION,
						new String[] { "OK" }, 0);
				md.open();
			}
		} catch (Exception e) {
			MessageDialog md = new MessageDialog(getShell(), "错误", null, e.getMessage(), MessageDialog.ERROR,
					new String[] { "OK" }, 0);
			md.open();
		} finally {
			button.setEnabled(true);
		}
	}

	private void zhuaImgTu() {
		// 获取网页文字
		String urlstring = text.getText();
		URI uriroot = URI.create(urlstring);
		CloseableHttpClient chc = HttpClients.createDefault();
		try {
			Document doc = Jsoup.connect(urlstring).get();
			Elements imgs = doc.getElementsByTag("img");
			// 下载进程记录
			StringBuffer error = new StringBuffer();
			StringBuffer downloadProgress = new StringBuffer();
			boolean errorflag = false;

			// 防止重复下载
			List<String> hasdown = new LinkedList<String>();
			for (Element e : imgs) {
				if (!StringUtil.isBlank(e.attr("src"))) {
					System.out.println(e.attr("src"));
					String uri = e.attr("src");
					URI uridownload = uriroot.resolve(uri);
					System.out.println(uridownload);
					if (hasdown.contains(uridownload.toString())) {
						downloadProgress.append("'").append(uridownload).append("' has downloaded\n");
					} else {
						hasdown.add(uridownload.toString());
						downloadProgress.append("downloading '").append(uridownload).append("' ...\n");
						HttpGet hgdownload = new HttpGet(uridownload);
						// 模拟referer破解防盗链
						hgdownload.addHeader("origin", urlstring);
						hgdownload.addHeader("referer", urlstring);

						File dir = new File(text_1.getText());
						String filename = uridownload.toString()
								.substring(uridownload.toString().lastIndexOf('/') > 0
										? uridownload.toString().lastIndexOf('/')
										: 0);
						File file = new File(dir, filename);
						FileOutputStream fos = null;
						try {
							HttpResponse hrdownload = chc.execute(hgdownload);
							System.out.println(hrdownload.getStatusLine().getStatusCode() + "-----" + filename);
							if (hrdownload.getStatusLine().getStatusCode() == 200
									&& hrdownload.getEntity().getContentLength() > 0) {
								fos = new FileOutputStream(file);
								hrdownload.getEntity().writeTo(fos);
								fos.flush();
								downloadProgress.append("'").append(uridownload).append("' has downloaded.\n");
							} else {
								downloadProgress.append("'").append(uridownload).append("' can't download!\n");
							}
						} catch (Exception ex) {
							error.append("download '").append(uridownload).append("' error:").append(ex.getMessage())
									.append('\n');
							errorflag = true;
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				}
			}
			textViewer.setDocument(new org.eclipse.jface.text.Document(downloadProgress.toString()));
			textViewer.refresh();
			if (errorflag) {
				MessageDialog md = new MessageDialog(getShell(), "错误", null, error.toString(), MessageDialog.ERROR,
						new String[] { "OK" }, 0);
				md.open();
			} else {
				MessageDialog md = new MessageDialog(getShell(), "成功", null, "抓取成功！", MessageDialog.INFORMATION,
						new String[] { "OK" }, 0);
				md.open();
			}
		} catch (Exception e) {
			MessageDialog md = new MessageDialog(getShell(), "错误", null, e.getMessage(), MessageDialog.ERROR,
					new String[] { "OK" }, 0);
			md.open();
		} finally {
			btnimg.setEnabled(true);
		}
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
		return new Point(444, 429);
	}
}
