/*
 * Created on 6.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import javax.ejb.FinderException;

import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOTranslationEntityBMPBean;

/**
 * Title:		SurveyQuestionTranslationBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyQuestionTranslationBMPBean extends IDOTranslationEntityBMPBean implements SurveyQuestionTranslation {

	public static final String COLUMNNAME_QUESTION = "question";

	/**
	 * 
	 */
	public SurveyQuestionTranslationBMPBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOTranslationEntityBMPBean#getMainClass()
	 */
	protected Class getTranslatedEntityClass() {
		return SurveyQuestion.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMNNAME_QUESTION,"Question",true,true,String.class,1000);
	}
	
	public Object ejbFindQuestionTranslation(SurveyQuestion question, ICLocale locale) throws FinderException{
		return idoFindTranslation(question,locale);
	}
	
	public void setQuestion(String question){
		setColumn(COLUMNNAME_QUESTION,question);
	}
	
	public String getQuestion(){
		return getStringColumnValue(COLUMNNAME_QUESTION);
	}
}
