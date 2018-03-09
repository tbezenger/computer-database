package com.excilys.formation.tbezenger.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;


public class LinkTag extends TagSupport {
	/**
	 */
	private static final long serialVersionUID = 1L;
	private int minPage;
	private int current;
	private int maxPage;


	public int doStartTag() throws JspException {

		StringBuilder listPages = new StringBuilder();
		if (maxPage == 1) {
			listPages.append("<li><a href=\"dashboard?page=1\"><b><i>" + minPage + "</b></i></a></li>\n");
		} else {
			if (current == 1) {
				listPages.append("<li><a href=\"dashboard?page=1\"><b><i>" + minPage + "</b></i></a></li>\n");
			} else {
				listPages.append("<li><a href=\"dashboard?page=1\">" + minPage + "</a></li>\n");
			}
			for (int i = -2; i < 3; i++) {
				if (current + i > 1 && current + i < maxPage) {
					if (i == 0 && current != 1) {
						listPages.append("<li><a href=\"dashboard?page=" + (current + i) + " \"><b><i>" + (current + i) + "</i></b></a></li>\n");
					} else {
					listPages.append("<li><a href=\"dashboard?page=" + (current + i) + " \">" + (current + i) + "</a></li>\n");
					}
				}
			}
			if (current == maxPage) {
				listPages.append("<li><a href=\"dashboard?page=" + (maxPage) + "\"><b><i>" + maxPage + "</b></i></a></li>\n");

			} else {
				listPages.append("<li><a href=\"dashboard?page=" + (maxPage) + "\">" + maxPage + "</a></li>\n");
			}
		}
		try {
			pageContext.getOut().print(listPages.toString());
		} catch (IOException e) {
			LogManager.getLogger("STDOUT").error(e.toString());
		}
		return 0;

	}

	public int getMinPage() {
		return minPage;
	}

	public void setMinPage(int minPage) {
		this.minPage = minPage;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
}
