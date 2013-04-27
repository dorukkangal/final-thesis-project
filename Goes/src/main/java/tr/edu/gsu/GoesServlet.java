package tr.edu.gsu;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;

public class GoesServlet extends ApplicationServlet {
	private static final long serialVersionUID = 1L;

	protected WebApplicationContext applicationContext;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request) {
		return applicationContext.getBean(GoesApplication.class);
	}

	@Override
	protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
		return GoesApplication.class;
	}

	@Override
	protected void writeAjaxPageHtmlHeader(final BufferedWriter page, String title, String themeUri, final HttpServletRequest request) throws IOException {
		super.writeAjaxPageHtmlHeader(page, title, themeUri, request);

		page.write("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>\n");
		page.write("<script type='text/javascript' src='" + themeUri + "/js/jquery-1.4.4.js'></script>");
		page.write("<script type='text/javascript' src='" + themeUri + "/js/highcharts-2.1.6.js'></script>");
		page.write("<link rel='shortcut icon' type='image/vnd.microsoft.icon' href='" + themeUri + "/favicon.ico' />");
		page.write("<link rel='icon' type='image/vnd.microsoft.icon' href='" + themeUri + "/favicon.ico' />");
		page.write("<title>Online Exam Sytem</title>");
	}
}
