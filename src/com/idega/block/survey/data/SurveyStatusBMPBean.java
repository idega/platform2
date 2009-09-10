/*
 * Created on 31.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.IWTimestamp;

/**
 * Title:		SurveyResultBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyStatusBMPBean extends GenericEntity implements SurveyStatus {

	public static final String COLUMNNAME_STATUS_REPORT_FILE_ID = "STATUS_REPORT_FILE_ID";
	public static final String COLUMNNAME_SURVEY_ID = "SU_SURVEY_ID";
	public static final String COLUMNNAME_MODIFIED = "MODIFIED";
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
		addAttribute(COLUMNNAME_TIME_OF_STATUS, "time that status was set", true, true, Timestamp.class);
		addAttribute(COLUMNNAME_MODIFIED, "modified since last report file created", true, true, Boolean.class);

		addManyToOneRelationship(COLUMNNAME_SURVEY_ID,SurveyEntity.class);
		addManyToOneRelationship(COLUMNNAME_STATUS_REPORT_FILE_ID,ICFile.class);
	}

	public void setIsModified(boolean isModified) {
		setColumn(COLUMNNAME_MODIFIED, isModified);
	}
	
	public void setReportFile(ICFile file) {
		setColumn(COLUMNNAME_STATUS_REPORT_FILE_ID, file);
		setIsModified(false);
	}
	
	public void setSurvey(SurveyEntity survey) {
		setColumn(COLUMNNAME_SURVEY_ID, survey.getPrimaryKey());
	}
	
	public void store() {
		setColumn(COLUMNNAME_TIME_OF_STATUS, IWTimestamp.getTimestampRightNow());
		super.store();
	}
	
	public boolean getIsModified() {
		return getBooleanColumnValue(COLUMNNAME_MODIFIED);
	}
	
	public ICFile getReportFile() {
		return (ICFile) getColumnValue(COLUMNNAME_STATUS_REPORT_FILE_ID);
	}
	
	public Timestamp getTimeOfStatus() {
		return getTimestampColumnValue(COLUMNNAME_TIME_OF_STATUS);
	}
	
	public SurveyEntity getSurvey() {
		return (SurveyEntity) getColumnValue(COLUMNNAME_SURVEY_ID);
	}
	
	public Object ejbFindBySurvey(SurveyEntity survey) throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this)
		.appendWhereEquals(COLUMNNAME_SURVEY_ID, survey.getPrimaryKey().toString())
		.appendOrderByDescending(COLUMNNAME_TIME_OF_STATUS);
		return this.idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAllBySurvey(SurveyEntity survey) throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this)
		.appendWhereEquals(COLUMNNAME_SURVEY_ID, survey.getPrimaryKey().toString())
		.appendOrderByDescending(COLUMNNAME_TIME_OF_STATUS);
		return this.idoFindPKsByQuery(query);
	}
}
