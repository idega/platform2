package com.idega.block.blog.business;
import com.idega.block.blog.data.BlogEntity;
import com.idega.block.text.business.ContentHelper;
/**
 * Title:
 * Description:
 * Copyright: Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company: idega
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 * @deprecated replaced by BlogEntry
 */
public class BlogHelper {
	private BlogEntity eBlog;
	private ContentHelper cHelper;
	public BlogEntity getBlogEntity() {
		return this.eBlog;
	}
	public void setBlog(BlogEntity blog) {
		this.eBlog = blog;
	}
	public ContentHelper getContentHelper() {
		return this.cHelper;
	}
	public void setContentHelper(ContentHelper contentHelper) {
		this.cHelper = contentHelper;
	}
}
