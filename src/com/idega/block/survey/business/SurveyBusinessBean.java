/*
 * Created on 6.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.business;

import javax.ejb.CreateException;

import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyAnswerHome;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyEntityHome;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.block.survey.data.SurveyQuestionHome;
import com.idega.block.survey.data.SurveyReply;
import com.idega.block.survey.data.SurveyReplyHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

/**
 * Title:		SurveyBusinessBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyBusinessBean extends IBOServiceBean implements SurveyBusiness {
	
	private SurveyEntityHome surveyHome;
	private SurveyAnswerHome answerHome;
	private SurveyQuestionHome questionHome;
	private SurveyReplyHome surveyReplyHome;
	
	public final static char ANSWERTYPE_SINGLE_CHOICE = 's';
	public final static char ANSWERTYPE_MULTI_CHOICE = 'm';
	public final static char ANSWERTYPE_TEXTAREA = 't';
	
	
	/**
	 * 
	 */
	public SurveyBusinessBean() {
		super();
	}
	
	private void initializeHomes() throws IDOLookupException{
		surveyHome = (SurveyEntityHome)IDOLookup.getHome(SurveyEntity.class);
		answerHome = (SurveyAnswerHome)IDOLookup.getHome(SurveyAnswer.class);
		questionHome = (SurveyQuestionHome)IDOLookup.getHome(SurveyQuestion.class);
		surveyReplyHome = (SurveyReplyHome)IDOLookup.getHome(SurveyReply.class);		
	}
	
	public SurveyEntity createSurvey(String name, String description, IWTimestamp startTime, IWTimestamp endTime) throws IDOLookupException, CreateException{
		SurveyEntity survey = getSurveyHome().create();
		
		survey.setName(name);
		if(description != null){
			survey.setDescription(description);
		}
		
		if(startTime != null){
			survey.setStartTime(startTime.getTimestamp());
		}
		
		if(endTime != null){
			survey.setEndTime(endTime.getTimestamp());
		}
		
		survey.store();
		
		return survey;
	}
	
	public SurveyQuestion createSurveyQuestion(SurveyEntity survey, String[] question, ICLocale[] locale, char answerType) throws IDOLookupException, CreateException, IDOAddRelationshipException{
		SurveyQuestion sQuestion = getQuestionHome().create();
		
		if(question != null && locale != null){
			for (int i = 0; i < question.length && i < locale.length; i++) {
				sQuestion.setQuestion(question[i],locale[i]);
			}
		}
				
		sQuestion.setAnswerType(answerType);
		
		sQuestion.store();
		
		survey.addQuestion(sQuestion);

		return sQuestion;
	}
	
	public SurveyQuestion createSurveyQuestion(SurveyEntity survey, String question, ICLocale locale, char answerType) throws IDOLookupException, IDOAddRelationshipException, CreateException{
		String[] questions = {question};
		ICLocale[] locales = {locale};
		return createSurveyQuestion(survey,questions,locales,answerType);
	}
	
	public SurveyAnswer createSurveyAnswer(SurveyQuestion question, String[] answer, ICLocale[] locale) throws IDOLookupException, CreateException{
		SurveyAnswer sAnswer = getAnswerHome().create();
		
		if(answer != null && locale != null){
			for (int i = 0; i < answer.length && i < locale.length; i++) {
				sAnswer.setAnswer((answer[i]==null)?"":answer[i],locale[i]);
			}
		}
		
		sAnswer.setSurveyQuestion(question);
				
		sAnswer.store();
		
		return sAnswer;
	}
	
	public SurveyAnswer createSurveyAnswer(SurveyQuestion question, String answer, ICLocale locale) throws IDOLookupException, CreateException{
		String[] answers = {(answer==null)?"":answer};
		ICLocale[] locales = {locale};
		return createSurveyAnswer(question,answers,locales);
	}
	
	

	/**
	 * @return
	 */
	public SurveyAnswerHome getAnswerHome() throws IDOLookupException {
		if(answerHome==null){
			initializeHomes();
		}
		return answerHome;
	}

	/**
	 * @return
	 */
	public SurveyEntityHome getSurveyHome() throws IDOLookupException {
		if(surveyHome==null){
			initializeHomes();
		}
		return surveyHome;
	}

	/**
	 * @return
	 */
	public SurveyQuestionHome getQuestionHome() throws IDOLookupException {
		if(questionHome==null){
			initializeHomes();
		}
		return questionHome;
	}

	/**
	 * @return
	 */
	public SurveyReplyHome getSurveyReplyHome() throws IDOLookupException {
		if(surveyReplyHome==null){
			initializeHomes();
		}
		return surveyReplyHome;
	}

}
