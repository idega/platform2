/*
 * Created on 18.2.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.blog.business;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.blog.data.BlogEntity;
import com.idega.block.category.business.FolderBlockBusinessBean;
import com.idega.block.text.business.ContentBusiness;
import com.idega.block.text.data.Content;
import com.idega.data.IDOLookupException;

/**
 * Title:		Blog
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */


public class BlogBusinessBean extends FolderBlockBusinessBean implements BlogBusiness{

	
	public BlogEntity saveBlog(int iBlogEntityId, int iLocalizedTextId, int iWorkFolderId, int iCategoryId, String sHeadline, String sTitle, String sAuthor, String sSource, String sBody, int iLocaleId, int iUserId, int InstanceId, Timestamp tsPubFrom, Timestamp tsPubTo, List listOfFiles, Timestamp blogDate) throws CreateException, IDOLookupException, FinderException {

		BlogEntity eBlogEntity = ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).create();
		if (iBlogEntityId > 0) {
			eBlogEntity = ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).findByPrimaryKey(new Integer(iBlogEntityId));
		}
		Content eContent = ContentBusiness.saveContent(eBlogEntity.getContentId(), iLocalizedTextId, iLocaleId, iUserId, tsPubFrom, tsPubTo, sHeadline, sBody, sTitle, listOfFiles, blogDate);
		if (eContent != null) {

			if (eContent.getID() > 0)
				eBlogEntity.setContentId(eContent.getID());

			if (sAuthor != null)
				eBlogEntity.setAuthor(sAuthor);

			if (sSource != null)
				eBlogEntity.setSource(sSource);

			eBlogEntity.setBlogFolderId(iWorkFolderId);
			
			if(iCategoryId != -1){
				eBlogEntity.setBlogCategoryId(iCategoryId);
			}
			eBlogEntity.store();
			return eBlogEntity;
		}
		return null;
	}
	
	
}
