package com.idega.block.blog.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.category.data.ICInformationFolder;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookupException;

public interface BlogEntity extends com.idega.data.IDOEntity {
	
	public final static String UNIQUE_ENTITY_NAME = "BG_BLOG";
	public final static String UFN_FOLDER = "IC_INFO_FOLDER_ID";
	public final static String UFN_CATEGORY = "IC_INFO_CATEGORY_ID";
	public final static String UFN_CONTENT = "CONTENT_ID";
	public final static String UFN_AUTHOR = "AUTHOR";
	public final static String UFN_SOURCE = "SOURCE";
	
	public int getID();
	public java.lang.String getAuthor();
	public int getBlogFolderId();
	public com.idega.block.text.data.Content getContent();
	public int getContentId();
	public java.util.Collection getRelatedFiles() throws com.idega.data.IDORelationshipException;
	public java.lang.String getSource();
	public void setAuthor(java.lang.String p0);
	public void setBlogFolderId(int p0);
	public void setBlogFolderId(java.lang.Integer p0);
	public void setContentId(java.lang.Integer p0);
	public void setContentId(int p0);
	public void setSource(java.lang.String p0);
	public ICInformationFolder getBlogFolder();
	
	public int getBlogCategoryId();
	public void setBlogCategoryId(Integer blog_category_id);
	public void setBlogCategoryId(int blog_category_id) ;
	
	
	
	public String getAuthor(ICLocale locale) throws IDOLookupException, FinderException ;
	public void setAuthor(ICLocale locale, String author) throws IDOLookupException, CreateException ;
	public String getBlog(ICLocale locale) throws IDOLookupException, FinderException ;
	public void setBlog(ICLocale locale, String source) throws IDOLookupException, CreateException ;
	public String getSource(ICLocale locale) throws IDOLookupException, FinderException ;
	public void setSource(ICLocale locale, String source) throws IDOLookupException, CreateException ;
	public String getTitle(ICLocale locale) throws IDOLookupException, FinderException ;
	public void setTitle(ICLocale locale, String author) throws IDOLookupException, CreateException ;
	
}
