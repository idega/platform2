package com.idega.block.blog.business;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.blog.data.BlogEntity;
import com.idega.block.category.business.FolderBlockBusiness;
import com.idega.data.IDOLookupException;


public interface BlogBusiness extends com.idega.business.IBOService, FolderBlockBusiness
{
	public BlogEntity saveBlog(int iBlogEntityId, int iLocalizedTextId, int iWorkFolderId, int iCategoryId, String sHeadline, String sTitle, String sAuthor, String sSource, String sBody, int iLocaleId, int iUserId, int InstanceId, Timestamp tsPubFrom, Timestamp tsPubTo, List listOfFiles, Timestamp  blogDate) throws CreateException, IDOLookupException, FinderException;
}
