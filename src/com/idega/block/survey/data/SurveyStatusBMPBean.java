/*
 * Created on 31.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;

/**
 * Title:		SurveyResultBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyStatusBMPBean extends GenericEntity {

	public static final String COLUMNNAME_STATUS_REPORT_FILE_ID = "STATUS_REPORT_FILE_ID";
	public static final String COLUMNNAME_SURVEY_ID = "SU_SURVEY_ID";
	public static final String COLUMNNAME_TIME_OF_STATUS = "TIME_OF_STATUS";
//	public static final String COLUMNNAME_ = "S";
		
	/**
	 * 
	 */
	public SurveyStatusBMPBean() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return "SU_SURVEY_STATUS";
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMNNAME_TIME_OF_STATUS);

		addManyToOneRelationship(COLUMNNAME_SURVEY_ID,SurveyEntity.class);
		addManyToOneRelationship(COLUMNNAME_STATUS_REPORT_FILE_ID,ICFile.class);
		
	}

}
