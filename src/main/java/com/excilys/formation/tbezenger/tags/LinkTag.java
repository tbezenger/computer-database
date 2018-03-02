package com.excilys.formation.tbezenger.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class LinkTag extends TagSupport{

	/**
	 */
	private static final long serialVersionUID = 1L;
	
	private int page;
	public void setPage(int pageNum) {
		page = pageNum;
	}

	public int doStartTag() throws JspException{
		
		return Tag.SKIP_BODY;
	}
}
