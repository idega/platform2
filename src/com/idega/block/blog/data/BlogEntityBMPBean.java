package com.idega.block.blog.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.category.data.ICInformationCategory;
import com.idega.block.category.data.ICInformationFolder;
import com.idega.block.text.data.Content;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOTranslatedEntityBMPBean;

/**
 * Title: Blog 
 * Description: 
 * Copyright: Copyright (c) 2003 
 * Company: idega Software
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @author 2004 - idega team -<br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.5b
 */

public class BlogEntityBMPBean extends IDOTranslatedEntityBMPBean implements com.idega.block.blog.data.BlogEntity {
	
	// UFN - UNIQUE_FIELD_NAME
	//${iConst} public final static String UNIQUE_ENTITY_NAME = "BG_BLOG";
	//${iConst} public final static String UFN_FOLDER = "IC_INFO_FOLDER_ID";
	//${iConst} public final static String UFN_CATEGORY = "IC_INFO_CATEGORY_ID";
	//${iConst} public final static String UFN_CONTENT = "CONTENT_ID";
	//${iConst} public final static String UFN_AUTHOR = "AUTHOR";
	//${iConst} public final static String UFN_SOURCE = "SOURCE";

	public final static String ENTITY_NAME = UNIQUE_ENTITY_NAME;
	public final static String COLUMNNAME_FOLDER = UFN_FOLDER;
	public final static String COLUMNNAME_CATEGORY = UFN_CATEGORY;
	public final static String COLUMNNAME_CONTENT = UFN_CONTENT;
	public final static String COLUMNNAME_AUTHOR = UFN_AUTHOR;
	public final static String COLUMNNAME_SOURCE = UFN_SOURCE;	
	
	
	public BlogEntityBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameContentId(), "Content", true, true, Integer.class, "many-to-one", Content.class);
		//    addAttribute(getColumnNameBlogCategoryId(), "Category", true, true, Integer.class, "many-to-one",ICCategory.class);
		addManyToOneRelationship(getColumnNameFolderID(), "Content folder", ICInformationFolder.class);
		addManyToOneRelationship(getColumnNameCatID(), "Category", ICInformationCategory.class);
		addAttribute(getColumnNameAuthor(), "Author", true, true, String.class);
		addAttribute(getColumnNameSource(), "Source", true, true, String.class);		
	}

	public String getEntityName() {
		return getEntityTableName();
	}

	public static String getEntityTableName() {
		return ENTITY_NAME;
	}

	//  public static String getColumnNameBlogCategoryId(){return "IC_CATEGORY_ID";}

	public static String getColumnNameFolderID() {
		return COLUMNNAME_FOLDER;
	}

	public static String getColumnNameCatID() {
		return COLUMNNAME_CATEGORY;
	}

	public static String getColumnNameContentId() {
		return COLUMNNAME_CONTENT;
	}

	public static String getColumnNameAuthor() {
		return COLUMNNAME_AUTHOR;
	}

	public static String getColumnNameSource() {
		return COLUMNNAME_SOURCE;
	}

	public void setDefaultValues() {
		this.setBlogFolderId(1);
		this.setSource("");
		this.setAuthor("");
	}

	public int getBlogFolderId() {
		return getIntColumnValue(getColumnNameFolderID());
	}
	
	public ICInformationFolder getBlogFolder(){
		return (ICInformationFolder)getColumnValue(getColumnNameFolderID());
	}

	public void setBlogFolderId(Integer blog_folder_id) {
		setColumn(getColumnNameFolderID(), blog_folder_id);
	}

	public void setBlogFolderId(int blog_folder_id) {
		setColumn(getColumnNameFolderID(), blog_folder_id);
	}
	
	public int getBlogCategoryId() {
		return getIntColumnValue(getColumnNameCatID());
	}
	
	public void setBlogCategoryId(Integer blog_category_id) {
		setColumn(getColumnNameCatID(), blog_category_id);
	}

	public void setBlogCategoryId(int blog_category_id) {
		setColumn(getColumnNameCatID(), blog_category_id);
	}

	public int getContentId() {
		return getIntColumnValue(getColumnNameContentId());
	}

	public void setContentId(int iContentId) {
		setColumn(getColumnNameContentId(), iContentId);
	}

	public void setContentId(Integer iContentId) {
		setColumn(getColumnNameContentId(), iContentId);
	}

	public String getAuthor() {
		return getStringColumnValue(getColumnNameAuthor());
	}

	public void setAuthor(String author) {
		setColumn(getColumnNameAuthor(), author);
	}
	
	public String getSource() {
		return getStringColumnValue(getColumnNameSource());
	}

	public void setSource(String source) {
		setColumn(getColumnNameSource(), source);
	}
	
	public Content getContent() {
		return (Content) this.getColumnValue(getColumnNameContentId());
	}

	public Collection getRelatedFiles() throws IDORelationshipException {
		return idoGetRelatedEntities(ICFile.class);
	}
	
	/**
	 * @deprecated temporary legacy method
	 */
	public Collection ejbFindAll(String sql) throws FinderException {
		//System.out.println("[Blog:QUERY]:"+sql);
		return idoFindPKsBySQL(sql);
	}

	
	
	//Translation begins
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOTranslatedEntityBMPBean#getTranslationEntityClass()
	 */
	public Class getTranslationEntityClass() {
		return BlogEntityTranslation.class;
	}
	
	
	public String getAuthor(ICLocale locale) throws IDOLookupException, FinderException {
		return getStringColumnValue(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_AUTHOR),locale);
	}

	public void setAuthor(ICLocale locale, String author) throws IDOLookupException, CreateException {
		setColumn(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_AUTHOR), author ,locale);
	}
	
	public String getBlog(ICLocale locale) throws IDOLookupException, FinderException {
		return getStringColumnValue(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_BODY),locale);
	}

	public void setBlog(ICLocale locale, String source) throws IDOLookupException, CreateException {
		setColumn(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_BODY), source ,locale);
	}
		
	public String getSource(ICLocale locale) throws IDOLookupException, FinderException {
		return getStringColumnValue(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_SOURCE),locale);
	}

	public void setSource(ICLocale locale, String source) throws IDOLookupException, CreateException {
		setColumn(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_SOURCE), source ,locale);
	}
	
	public String getTitle(ICLocale locale) throws IDOLookupException, FinderException {
		return getStringColumnValue(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_TITLE),locale);
	}

	public void setTitle(ICLocale locale, String author) throws IDOLookupException, CreateException {
		setColumn(getTranslationEntityDefinition().findFieldByUniqueName(BlogEntityTranslation.UFN_TITLE), author ,locale);
	}
	
	

}
