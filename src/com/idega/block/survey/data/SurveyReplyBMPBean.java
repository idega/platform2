/*
 * Created on 2.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;


import com.idega.data.GenericEntity;

/**
 * Title:		SurveyReplyBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */

public class SurveyReplyBMPBean extends GenericEntity implements SurveyReply{

	private static final String ENTITY_NAME ="SU_SURVEY_REPLY";
	//PARTICIPANT_KEY could either be generated key or encripted representative string (e.g. e-mail) 
	private final static String COLUMNNAME_PARTICIPANT_KEY = "PARTICIPANT_KEY";  
	private static final String COLUMNNAME_ANSWER = "ANSWER";
	private final static String COLUMNNAME_SURVEY_ID = "SU_SURVEY_ID";
	private final static String COLUMNNAME_QUESTION_ID = "SU_QUESTION_ID";
	private final static String COLUMNNAME_ANSWER_ID = "SU_ANSWER_ID";
	

	
	
	
	/**
	 * 
	 */
	public SurveyReplyBMPBean() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(this.getIDColumnName());
		
		addAttribute(COLUMNNAME_PARTICIPANT_KEY,"Participant key",true,true,String.class,255);
		addManyToOneRelationship(COLUMNNAME_SURVEY_ID,SurveyEntity.class);
		addManyToOneRelationship(COLUMNNAME_QUESTION_ID,SurveyQuestion.class);
		
		addManyToOneRelationship(COLUMNNAME_ANSWER_ID,SurveyAnswer.class);
		addAttribute(COLUMNNAME_ANSWER,"Answer",true,true,String.class,500);

	}
	
	public void setParticipantKey(String key){
		setColumn(COLUMNNAME_PARTICIPANT_KEY,key);
	}
	
	public void setAnswer(String answer){
		setColumn(COLUMNNAME_ANSWER,answer);
	}
	
	public void setSurvey(SurveyEntity survey){
		setColumn(COLUMNNAME_SURVEY_ID,survey);
	}
	
	public void setQuestion(SurveyQuestion question){
		setColumn(COLUMNNAME_QUESTION_ID,question);
	}
	
	
	public String getParticipantKey(){
		return getStringColumnValue(COLUMNNAME_PARTICIPANT_KEY);
	}

	public String getAnswer(){
		return getStringColumnValue(COLUMNNAME_ANSWER);
	}

	public SurveyEntity getSurvey(){
		return (SurveyEntity)getColumnValue(COLUMNNAME_SURVEY_ID);
	}

	public SurveyQuestion getQuestion(){
		return (SurveyQuestion)getColumnValue(COLUMNNAME_QUESTION_ID);
	}
	
	

}
