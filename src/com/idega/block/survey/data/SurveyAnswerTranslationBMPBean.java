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
 * Title:		SurveyAnswerTranslationBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyAnswerTranslationBMPBean extends IDOTranslationEntityBMPBean implements SurveyAnswerTranslation {

	public static final String COLUMNNAME_ANSWER = "answer";

	/**
	 * 
	 */
	public SurveyAnswerTranslationBMPBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOTranslationEntityBMPBean#getMainClass()
	 */
	protected Class getTranslatedEntityClass() {
		return SurveyAnswer.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMNNAME_ANSWER,"Answer",true,true,String.class,500);

	}
	
	public Object ejbFindAnswerTranslation(SurveyAnswer answer, ICLocale locale) throws FinderException{
		return idoFindTranslation(answer,locale);
	}
	
	public void setAnswer(String question){
		setColumn(COLUMNNAME_ANSWER,question);
	}
	
	public String getAnswer(){
		return getStringColumnValue(COLUMNNAME_ANSWER);
	}

}
