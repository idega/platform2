package com.idega.block.survey.data;

import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

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
	public static final String COLUMNNAME_CREATION_LOCALE = "CREATION_LOCALE";	
	
	private final static String DELETED_COLUMN = "DELETED";
	private final static String DELETED_BY_COLUMN = "DELETED_BY";
	private final static String DELETED_WHEN_COLUMN = "DELETED_WHEN";
	public final static String DELETED = "Y";
	public final static String NOT_DELETED = "N";

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

		addManyToOneRelationship(COLUMNNAME_CREATION_LOCALE, "Locale id", ICLocale.class);

		addAttribute(DELETED_COLUMN, "Deleted", true, true, String.class, 1);
		addAttribute(DELETED_BY_COLUMN, "Deleted by", true, true, Integer.class, "many-to-one", User.class);
		addAttribute(DELETED_WHEN_COLUMN, "Deleted when", true, true, Timestamp.class);

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
		SurveyAnswerTranslation qTR;
		try {
			qTR = satHome.findAnswerTranslation(this, locale);
		} catch (FinderException e) {
			qTR = satHome.findAnswerTranslation(this, this.getCreationLocale());
		}
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
			if(this.getCreationLocale()== null){
				this.setCreationLocale(locale);
			}
		}
		
		qTR.setAnswer(question);
		
		storeMap.put(locale,qTR);
	}
	
	public Collection ejbFindQuestionsAnswer(SurveyQuestion question) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		
		query.appendWhereEquals(COLUMNNAME_QUESTION_ID,question);
		query.appendAnd();
		query.append(DELETED_COLUMN);
		query.appendNOTLike();
		query.appendWithinSingleQuotes(DELETED);

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
	
	public void setCreationLocale(ICLocale locale){
		setColumn(COLUMNNAME_CREATION_LOCALE,locale);
	}
	
	public ICLocale getCreationLocale(){
		return (ICLocale)getColumnValue(COLUMNNAME_CREATION_LOCALE);
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
	

	/**
	 *
	 */
	public void setRemoved(User user){
		setColumn(DELETED_COLUMN, DELETED);
		setDeletedWhen(IWTimestamp.getTimestampRightNow());
		setDeletedBy(user);

		super.store();
	}

	/**
	 *
	 */
	private void setDeletedBy(User user) {
		setColumn(DELETED_BY_COLUMN, user);
	}
	
	/**
	 *
	 */
	private void setDeletedWhen(Timestamp when) {
		setColumn(DELETED_WHEN_COLUMN, when);
	}



}
