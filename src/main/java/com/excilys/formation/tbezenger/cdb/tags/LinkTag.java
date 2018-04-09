package com.excilys.formation.tbezenger.cdb.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;

import com.excilys.formation.tbezenger.cdb.dto.ComputerPageDTO;


public class LinkTag extends TagSupport {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private ComputerPageDTO page;

	@Override
	public int doStartTag() throws JspException {

		StringBuilder listPages = new StringBuilder();
		if (page.getMaxPage() == 1) {
			listPages.append("<li><a href=\"dashboard?page=1&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\"><b><i>" + 1 + "</b></i></a></li>\n");
		} else {
			if (page.getNumPage() == 1) {
				listPages.append("<li><a href=\"dashboard?page=1&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\"><b><i>" + 1 + "</b></i></a></li>\n");
			} else {
				listPages.append("<li><a href=\"dashboard?page=1&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\">" + 1 + "</a></li>\n");
			}
			for (int i = -2; i < 3; i++) {
				if (page.getNumPage() + i > 1 && page.getNumPage() + i < page.getMaxPage()) {
					if (i == 0 && page.getNumPage() != 1) {
						listPages.append("<li><a href=\"dashboard?page=" + (page.getNumPage() + i) + "&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\"><b><i>" + (page.getNumPage() + i) + "</i></b></a></li>\n");
					} else {
					listPages.append("<li><a href=\"dashboard?page=" + (page.getNumPage() + i) + "&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\">" + (page.getNumPage() + i) + "</a></li>\n");
					}
				}
			}
			if (page.getNumPage() == page.getMaxPage()) {
				listPages.append("<li><a href=\"dashboard?page=" + (page.getMaxPage()) + "&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\"><b><i>" + page.getMaxPage() + "</b></i></a></li>\n");

			} else {
				listPages.append("<li><a href=\"dashboard?page=" + (page.getMaxPage()) + "&rows=" + page.getRows() + "&search=" + page.getSearch() + "&orderBy=" + page.getOrderBy() + "&isAscending=" + page.getIsAscending() + "\">" + page.getMaxPage() + "</a></li>\n");
			}
		}
		try {
			pageContext.getOut().print(listPages.toString());
		} catch (IOException e) {
			LogManager.getLogger("STDOUT").error(e.toString());
		}
		return 0;

	}

	public ComputerPageDTO getPage() {
		return page;
	}

	public void setPage(ComputerPageDTO page) {
		this.page = page;
	}

}
