package com.excilys.formation.tbezenger.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


public class LinkTag extends TagSupport {
	/**
	 */
	private static final long serialVersionUID = 1L;
	private int minPage;
	private int current;
	private int maxPage;


	public int doStartTag() throws JspException {

		StringBuilder listPages = new StringBuilder();
		listPages.append("<li><a href=\"dashboard?page=1\">" + minPage + "</a></li>\n");
		for (int i = -2; i < 3; i++) {
			if (current + i > 1 && current + i < maxPage) {
				if (i == 0) {
					listPages.append("<li><a href=\"dashboard?page=" + (current + i) + " \"><b><i>" + (current + i) + "</i></b></a></li>\n");
				} else {
				listPages.append("<li><a href=\"dashboard?page=" + (current + i) + " \">" + (current + i) + "</a></li>\n");
				}
			}
		}
		listPages.append("<li><a href=\"dashboard?page=" + (maxPage) + "\">" + maxPage + "</a></li>\n");
		try {
			pageContext.getOut().print(listPages.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
