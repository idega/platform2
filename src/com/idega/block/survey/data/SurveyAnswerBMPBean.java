package com.idega.block.survey.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDOTranslationEntity;

/**
 * Title:		SurveyAnswerBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */

public class SurveyAnswerBMPBean extends com.idega.data.GenericEntity implements SurveyAnswer {

	public static final String COLUMNNAME_TEXT_INPUT = "TEXT_INPUT";
	public static final String COLUMNNAME_QUESTION_ID = "SU_SURVEY_QUESTION_ID";
	private HashMap storeMap = new HashMap();

	public SurveyAnswerBMPBean() {
		super();
	}

	public SurveyAnswerBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMNNAME_QUESTION_ID,SurveyQuestion.class);
		setNullable(COLUMNNAME_QUESTION_ID, false);
		addAttribute(COLUMNNAME_TEXT_INPUT,"Text input",true,true,Boolean.class);

	}

	public String getEntityName() {
		return "SU_SURVEY_ANSWER";
	}

	public SurveyQuestion getSurveyQuestion() {
		return (SurveyQuestion)getColumnValue(COLUMNNAME_QUESTION_ID);
	}

	public void setSurveyQuestion(SurveyQuestion question) {
		setColumn(COLUMNNAME_QUESTION_ID, question);
	}
	
	
	public String getAnswer(ICLocale locale) throws IDOLookupException, FinderException{
		SurveyAnswerTranslationHome satHome = (SurveyAnswerTranslationHome)IDOLookup.getHome(SurveyAnswerTranslation.class);
		SurveyAnswerTranslation qTR = satHome.findAnswerTranslation(this,locale);
		return qTR.getAnswer();
	}
	
	public void setAnswer(String question, ICLocale locale) throws IDOLookupException, CreateException{
		SurveyAnswerTranslationHome satHome = (SurveyAnswerTranslationHome)IDOLookup.getHome(SurveyAnswerTranslation.class);
		SurveyAnswerTranslation qTR = null;
		try {
			qTR = satHome.findAnswerTranslation(this,locale);
		} catch (FinderException e) {
			qTR = satHome.create();
			qTR.setLocale(locale);
		}
		
		qTR.setAnswer(question);
		
		storeMap.put(locale,qTR);
	}
	
	public Collection ejbFindQuestionsAnswer(SurveyQuestion question) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		
		query.appendWhereEquals(COLUMNNAME_QUESTION_ID,question);

		return idoFindPKsByQuery(query);
	}
	
	public boolean useTextInput(){
		return getBooleanColumnValue(COLUMNNAME_TEXT_INPUT);
	}
	
	public void setToUseTextInput(boolean value){
		setColumn(COLUMNNAME_TEXT_INPUT,value);
	}
	
	public void setToUseTextInput(Boolean value){
		setColumn(COLUMNNAME_TEXT_INPUT,value);
	}
	
	public void store(){
		super.store();
		Collection translations = storeMap.values();
		for (Iterator iter = translations.iterator(); iter.hasNext();) {
			IDOTranslationEntity element = (IDOTranslationEntity)iter.next();
			element.setTransletedEntity(this);
			element.store();
		}
	}


}
