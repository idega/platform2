/*
 * Created on 19.2.2004
 */
package com.idega.block.blog.data;

import com.idega.data.IDOTranslationEntityBMPBean;

/**
 * Title:		BlogEntityTranslationBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class BlogEntityTranslationBMPBean extends IDOTranslationEntityBMPBean implements BlogEntityTranslation {

	//${iConst} public final static String UFN_TITLE = "TITLE";
	//${iConst} public final static String UFN_BODY = "BODY";
	//${iConst} public final static String UFN_AUTHOR = "AUTHOR";
	//${iConst} public final static String UFN_SOURCE = "SOURCE";
	
	public final static String COLUMNNAME_TITLE = UFN_TITLE;
	public final static String COLUMNNAME_BODY = UFN_BODY;
	
	public final static String COLUMNNAME_AUTHOR = UFN_AUTHOR;
	public final static String COLUMNNAME_SOURCE = UFN_SOURCE;
	
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOTranslationEntityBMPBean#getTranslatedEntityClass()
	 */
	protected Class getTranslatedEntityClass() {
		return BlogEntity.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getColumnNameTitle(), "Title",true,true,String.class);
		addAttribute(getColumnNameBody(), "Body", true, true, String.class,30000);
		addAttribute(getColumnNameAuthor(), "Author", true, true, String.class);
		addAttribute(getColumnNameSource(), "Source", true, true, String.class);
	}
	
	
	public static String getColumnNameTitle() {
		return COLUMNNAME_TITLE;
	}
	
	public static String getColumnNameBody() {
		return COLUMNNAME_BODY;
	}
	
	public static String getColumnNameAuthor() {
		return COLUMNNAME_AUTHOR;
	}

	public static String getColumnNameSource() {
		return COLUMNNAME_SOURCE;
	}

}
