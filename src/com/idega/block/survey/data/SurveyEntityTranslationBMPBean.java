/*
 * Created on 18.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import com.idega.data.IDOTranslationEntityBMPBean;

/**
 * Title:		SurveyEntityTranslationBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyEntityTranslationBMPBean extends IDOTranslationEntityBMPBean implements SurveyEntityTranslation{

	/**
	 * 
	 */
	public SurveyEntityTranslationBMPBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOTranslationEntityBMPBean#getTranslatedEntityClass()
	 */
	protected Class getTranslatedEntityClass() {
		return SurveyEntity.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getColumnNameName(), "Name", true, true, String.class);
		addAttribute(getColumnNameDescription(), "Description", true, true, String.class);

	}
	
	
	public static String getColumnNameName() {
		return "NAME";
	}
	
	public static String getColumnNameDescription() {
		return "DESCRIPTION";
	}
	
	public String getName() {
		return getStringColumnValue(getColumnNameName());
	}
	
	public String getDescription() {
		return getStringColumnValue(getColumnNameDescription());
	}
		
	public void setName(String name) {
		setColumn(getColumnNameName(), name);
	}
	
	public void setDescription(String description) {
		setColumn(getColumnNameDescription(), description);
	}



}
