/*
 * Created on 18.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;

/**
 * Title:		SurveyParticipant
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyParticipantBMPBean extends GenericEntity implements SurveyParticipant{
	
	public static final String COLUMNNAME_NAME = "PARTICIPANT_NAME";
	public static final String COLUMNNAME_SURVEY = "SU_SURVEY_ID";
	
	/**
	 * 
	 */
	public SurveyParticipantBMPBean() {
		super();
	}


	public String getEntityName() {
		return "SU_SURVEY_PARTICIPANT";
	}


	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMNNAME_NAME,"Participant name",true,true,String.class);
		
		addManyToOneRelationship(COLUMNNAME_SURVEY,SurveyEntity.class);
	}
	
	public void setParticipantName(String name){
		setColumn(COLUMNNAME_NAME,name);
	}
	
	public void setSurvey(SurveyEntity survey){
		setColumn(COLUMNNAME_SURVEY,survey);
	}
	
	public String getParticipantName(){
		return getStringColumnValue(COLUMNNAME_NAME);
	}
	
	public SurveyEntity getSurvey(){
		return (SurveyEntity)getColumnValue(COLUMNNAME_SURVEY);
	}
	
	public int ejbHomeGetNumberOfParticipations(SurveyEntity survey, String name) throws IDOException{
		IDOQuery query = idoQueryGetSelectCount();
		query.appendWhereEquals(COLUMNNAME_SURVEY,survey);
		query.appendAndEquals(COLUMNNAME_NAME,name);
		
		return idoGetNumberOfRecords(query);
	}

}
