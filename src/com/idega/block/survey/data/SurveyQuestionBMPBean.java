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
import com.idega.util.IWTimestamp;
import com.idega.user.data.User;


/**
 * Title:		SurveyQuestionBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */

public class SurveyQuestionBMPBean extends com.idega.data.GenericEntity implements SurveyQuestion{
	
	private HashMap storeMap = new HashMap();
	public static final String COLUMNNAME_CREATION_LOCALE = "CREATION_LOCALE";	

	private final static String DELETED_COLUMN = "DELETED";
	private final static String DELETED_BY_COLUMN = "DELETED_BY";
	private final static String DELETED_WHEN_COLUMN = "DELETED_WHEN";
	public final static String DELETED = "Y";
	public final static String NOT_DELETED = "N";

	
	public SurveyQuestionBMPBean() {
		super();
	}

	public SurveyQuestionBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
//		addAttribute(getColumnNameMultiChoice(), "Multi-Choice",true,true,Boolean.class);
		addAttribute(getColumnNameAnswerType(), "Answer type",true,true,String.class,1);
		setNullable(getColumnNameAnswerType(),false);
		
		addManyToOneRelationship(COLUMNNAME_CREATION_LOCALE, "Locale id", ICLocale.class);

		addAttribute(DELETED_COLUMN, "Deleted", true, true, String.class, 1);
		addAttribute(DELETED_BY_COLUMN, "Deleted by", true, true, Integer.class, "many-to-one", User.class);
		addAttribute(DELETED_WHEN_COLUMN, "Deleted when", true, true, Timestamp.class);

	}

	
//	public static String getColumnNameMultiChoice(){
//		return "MULTI_CHOICE";
//	}
	
	public static String getColumnNameAnswerType(){
		return "ANSWER_TYPE";
	}

	public String getEntityName() {
		return "SU_SURVEY_QUESTION";
	}

//	public void setMultiChoice(boolean value){
//		setColumn(getColumnNameMultiChoice(),value);
//	}
//	
//	public void setMultiChoice(Boolean value){
//		setColumn(getColumnNameMultiChoice(),value);
//	}
	
	public void setAnswerType(char value){
		setColumn(getColumnNameAnswerType(),value);
	}
	
	public char getAnswerType(){
		return getCharColumnValue(getColumnNameAnswerType());
	}
	
	public String getQuestion(ICLocale locale) throws IDOLookupException, FinderException{
		SurveyQuestionTranslationHome sqtHome = (SurveyQuestionTranslationHome)IDOLookup.getHome(SurveyQuestionTranslation.class);
		SurveyQuestionTranslation qTR;
		try {
			qTR = sqtHome.findQuestionTranslation(this, locale);
		} catch (FinderException e) {
			qTR = sqtHome.findQuestionTranslation(this, this.getCreationLocale());
		}
		return qTR.getQuestion();
	}
	
	public void setQuestion(String question, ICLocale locale) throws IDOLookupException, CreateException{
		SurveyQuestionTranslationHome sqtHome = (SurveyQuestionTranslationHome)IDOLookup.getHome(SurveyQuestionTranslation.class);
		SurveyQuestionTranslation qTR = null;
		try {
			qTR = sqtHome.findQuestionTranslation(this,locale);
		} catch (FinderException e) {
			qTR = sqtHome.create();
			qTR.setLocale(locale);
			if(this.getCreationLocale()== null){
				this.setCreationLocale(locale);
			}
		}
		
		qTR.setQuestion(question);
		
		storeMap.put(locale,qTR);
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
			SurveyQuestionTranslation element = (SurveyQuestionTranslation)iter.next();
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
