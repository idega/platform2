/*
 * Created on 6.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.business;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyAnswerHome;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyEntityHome;
import com.idega.block.survey.data.SurveyParticipant;
import com.idega.block.survey.data.SurveyParticipantHome;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.block.survey.data.SurveyQuestionHome;
import com.idega.block.survey.data.SurveyReply;
import com.idega.block.survey.data.SurveyReplyHome;
import com.idega.block.survey.data.SurveyStatus;
import com.idega.block.survey.data.SurveyStatusHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.category.data.InformationFolder;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;
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
	private SurveyParticipantHome surveyParticipantHome;
	private SurveyStatusHome statHome;
	
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
		surveyParticipantHome = (SurveyParticipantHome)IDOLookup.getHome(SurveyParticipant.class);		
		statHome = (SurveyStatusHome) IDOLookup.getHome(SurveyStatus.class);
	}
	
	public SurveyEntity createSurvey(InformationFolder folder, String name, String description, IWTimestamp startTime, IWTimestamp endTime) throws IDOLookupException, CreateException{
		SurveyEntity survey = getSurveyHome().create();
		
		survey.setFolder(folder.getEntity());
		
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
	
	
	public SurveyReply createSurveyReply(SurveyEntity survey, SurveyQuestion question, String participantKey, SurveyAnswer answer, String answerText) throws IDOLookupException, CreateException{
		SurveyReply reply = getSurveyReplyHome().create();
		
		reply.setSurvey(survey);
		reply.setQuestion(question);
		reply.setParticipantKey(participantKey);
		
		if(answer != null){
			reply.setAnswer(answer);
		} 
		
		if(answerText != null){
			if(answerText.length() > SurveyReply.SURVEY_ANSWER_MAX_LENGTH){
				reply.setAnswer(answerText.substring(0,SurveyReply.SURVEY_ANSWER_MAX_LENGTH));
				reply.store();
				createSurveyReply(survey, question, participantKey, answer, answerText.substring(SurveyReply.SURVEY_ANSWER_MAX_LENGTH));
			} else {
				reply.setAnswer(answerText);
			}
		}
		
		
		reply.store();
		
		return reply;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.survey.business.SurveyBusiness#updateSurveyQuestion(com.idega.block.survey.data.SurveyEntity, com.idega.block.survey.data.SurveyQuestion, java.lang.String, com.idega.core.localisation.data.ICLocale, char)
	 */
	public SurveyQuestion updateSurveyQuestion(SurveyEntity survey, SurveyQuestion question, String questionText, ICLocale locale, char type) throws IDOLookupException, CreateException {
		//??use surveyEntity to see if the question is related to more than this one and then create new Question
		question.setQuestion(questionText,locale);
		question.setAnswerType(type);
		question.store();
		return question;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.survey.business.SurveyBusiness#updateSurveyAnswer(com.idega.block.survey.data.SurveyAnswer, java.lang.String, com.idega.core.localisation.data.ICLocale)
	 */
	public SurveyAnswer updateSurveyAnswer(SurveyAnswer ans, String answerString, ICLocale locale) throws IDOLookupException, CreateException {
		ans.setAnswer(answerString,locale);
		ans.store();
		return ans;
	}
	
	

	/* (non-Javadoc)
	 * @see com.idega.block.survey.business.SurveyBusiness#removeQuestionFromSurvey(com.idega.block.survey.data.SurveyEntity, com.idega.block.survey.data.SurveyQuestion)
	 */
	public void removeQuestionFromSurvey(SurveyEntity survey, SurveyQuestion question, User user) throws IDORemoveRelationshipException {
//		Collection answers = this.getAnswerHome().findQuestionsAnswer(question);
//		for (Iterator aIter = answers.iterator(); aIter.hasNext();) {
//			this.removeAnswerFromQuestion(question,(SurveyAnswer)aIter.next(),user);
//		}	
		survey.removeQuestion(question);
		question.setRemoved(user);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.survey.business.SurveyBusiness#removeAnswerFromQuestion(com.idega.block.survey.data.SurveyQuestion, com.idega.block.survey.data.SurveyAnswer)
	 */
	public void removeAnswer(SurveyAnswer ans, User user) throws IDORemoveRelationshipException {
		ans.setRemoved(user);
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
	
	/**
	 * @return
	 */
	public SurveyParticipantHome getSurveyParticipantHome() throws IDOLookupException {
		if(surveyParticipantHome==null){
			initializeHomes();
		}
		return surveyParticipantHome;
	}
	
	public SurveyStatusHome getSurveyStatusHome() throws IDOLookupException {
		if(statHome==null){
			initializeHomes();
		}
		return statHome;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.survey.business.SurveyBusiness#reportParticipation(com.idega.block.survey.data.SurveyEntity, java.lang.String)
	 */
	public SurveyParticipant reportParticipation(SurveyEntity survey, String participant) throws IDOLookupException, CreateException {
		SurveyParticipant sp = getSurveyParticipantHome().create();
		sp.setSurvey(survey);
		sp.setParticipantName(participant);
		sp.store();
		return sp;
	}
	
	public SurveyStatus getSurveyStatus(SurveyEntity survey) {
		try {
			return statHome.findBySurvey(survey);
		} catch (FinderException e) {
			try {
				SurveyStatus status = statHome.create();
				status.setSurvey(survey);
				status.setIsModified(true);
				status.store();
				return status;
			} catch (CreateException e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
	}
	
}
