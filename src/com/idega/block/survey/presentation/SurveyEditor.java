/*
 * Created on 30.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.presentation;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.help.presentation.Help;
import com.idega.block.survey.business.SurveyBusiness;
import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.FolderBlock;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FieldSet;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;


/**
 * Title:		SurveyEditor
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyEditor extends FolderBlock {



	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.survey";
	public static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwbSurvey;
	protected IWContext _iwc;
	private Locale _iLocale;
	private IWTimestamp _date;

	
	public final static String STYLE = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000;";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	public final static String STYLE_BUTTON = "font-family:arial; font-size:8pt; color:#000000; text-align: center; border: 1 solid #000000;";

	public static final String PRM_SURVEY_ID = "su_id";
	public static final Object SURVEY_NOT_STORED = null;
	private Object _surveyID = SURVEY_NOT_STORED;

	public static final String PRM_ANSWERTYPE = "su_ans_type";
	public final static String PRM_MAINTAIN_SUFFIX = "_mt";
	public final static char ANSWERTYPE_SINGLE_CHOICE = SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE;
	public final static char ANSWERTYPE_MULTI_CHOICE = SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE;
	public final static char ANSWERTYPE_TEXTAREA = SurveyBusinessBean.ANSWERTYPE_TEXTAREA;
	
	private int _numberOfQuestions = 3;
	public static final String PRM_NUMBER_OF_QUESTIONS_TO_ADD = "su_noqta";
	public static final String PRM_NUMBER_OF_QUESTIONS = "su_noq";
	
	public static final String PRM_CURRENT_STATE = "su_curr_state";
	public static final String PRM_GOTO_STATE = "su_goto_state";
	public static final int STATE_ONE = 1;
	public static final int STATE_TWO = 2;
	private int _state = STATE_ONE;
	private int _lastState = STATE_ONE;
	
	public static final String PRM_ACTION = "su_action";
	public static final String PRM_LAST_ACTION = "su_last_action";
	public static final int ACTION_NO_ACTION = 0;
	public static final int ACTION_ADD_QUESTION = 1;
	public static final int ACTION_ADD_ANSWER = 2;
	public static final int ACTION_SAVE = 3;
	public static final int ACTION_CANCEL = 4;
	public static final int ACTION_BACK = 5;
	public static final int ACTION_FORWARD = 6;
	private int _action = ACTION_NO_ACTION;
	private int _lastAction = ACTION_NO_ACTION;
	
	public static final String PRM_NUMBER_OF_ANSWERS_TO_ADD = "su_noata";
	public static final String PRM_NUMBER_OF_ANSWERS = "su_noa";
	public static final int _defaultNumberOfAnswers = 3;
	
	public static final String ADD_QUESTION_PRM = "add_question";
	public static final String ADD_ANSWER_PRM = "add_answer";
	
	public static final String PRM_QUESTION = "su_q";
	public static final String PRM_ANSWER = "su_a";
	public static final String PRM_ADD_TEXT_INPUT = "su_ati";
	public static final String PRM_QUESTION_IDS = "su_q_id";
	public static final String PRM_ANSWER_IDS = "su_a_id";
	
	private boolean _surveyHasBeenLoaded = false;
	public static final String PRM_SURVEY_LOADED = "su_loaded";
	
	private Vector prmVector = new Vector();
	private HashMap _prmValues = new HashMap();
	


	/**
	 * 
	 */
	public SurveyEditor(int instanceID) {
		super();
		this.useLocalizedFolders(false);
		setICObjectInstanceID(instanceID);
		prmVector = new Vector();
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void setInitialNumberOfQuestions(int number){
		if(number > 0 ){
			_numberOfQuestions = number;
		}
	}
	
	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		_iwc = iwc;
		_iwrb = getResourceBundle(iwc);
		_iwb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		_iwbSurvey = getBundle(iwc);
//		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		_iLocale = iwc.getCurrentLocale();
		_date = new IWTimestamp();
		
		processParameters(iwc);
	}
	
	private Link getModeChangeLink() {
		Image editImage = _iwb.getImage("shared/edit.gif");
		Link adminLink = new Link(editImage);
		adminLink.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_SURVEY);

		return adminLink;
	}
	
	public void main(IWContext iwc) throws Exception {
		
		add(getModeChangeLink());
		
		Form myForm = new Form();
		this.add(myForm);
		//Edit		
//		if(this.hasEditPermission()){
			switch (_state) {
				case STATE_ONE :
					myForm.add(getStateOne());
					break;
				case STATE_TWO :
					myForm.add(getStateTwo());
					break;
				default :
					myForm.add(getDefaultState());
					break;
			}			
//		} else {
//			//store information temporary while logging in
//		}

		//save to DB
		if(_action==ACTION_SAVE){
			
			if(_surveyID == SURVEY_NOT_STORED){
				//create
				Object pk = createSurvey(iwc);
				prmVector.add(new Parameter(PRM_SURVEY_ID,String.valueOf(pk)));
			} else {
				//update
				storeSurvey(iwc);
				//prmVector.add(new Parameter(PRM_SURVEY_ID,String.valueOf(pk)));
			}
			
			add(new Text("Survey has been saved"));
		}
		
		
		beforeParameterListIsAdded();
		for (Iterator iter = prmVector.iterator(); iter.hasNext();) {
			myForm.add((Parameter)iter.next());
		}

	}


//	ICLocale locale = ICLocaleBusiness.getICLocale(_iLocaleID);
//	try {
//		Collection questions = _currentSurvey.getSurveyQuestions();
//		int questionNumber = 1; 
//		for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
//			SurveyQuestion question = (SurveyQuestion)iter.next();
//			surveyTable.add(new HiddenInput(PRM_QUESTIONS,question.getPrimaryKey().toString()),1,getQuestionRowIndex(questionNumber));
//			surveyTable.add(getQuestionLabel(questionNumber),1,getQuestionRowIndex(questionNumber));
//			try {
//				surveyTable.add(getQuestionTextObject(question.getQuestion(locale)),2,getQuestionRowIndex(questionNumber));
//			} catch (IDOLookupException e1) {
//				e1.printStackTrace();
//			} catch (FinderException e1) {
//				e1.printStackTrace();
//			}
//		
//		
//			surveyTable.add(getAnswerTable(question, locale),2,(surveyTable.getRows()+1));
//
//	Collection answers = _sBusiness.getAnswerHome().findQuestionsAnswer(question);


	/**
	 * @param iwc
	 * @return
	 */
	private Object storeSurvey(IWContext iwc) throws IDOAddRelationshipException, CreateException, IDOLookupException, RemoteException, FinderException {
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		ICLocale locale = ICLocaleBusiness.getICLocale(_iLocale);
		
		
		SurveyEntity survey = null;
		
		if(_surveyID != null){
			survey = business.getSurveyHome().findByPrimaryKey(_surveyID);
		} else {
			survey = business.createSurvey(this.getWorkingFolder(),"Survey",null,IWTimestamp.RightNow(),null);
		}
		
		String[] questions = (String[])_prmValues.get(PRM_QUESTION);
		String[] answerType = (String[])_prmValues.get(PRM_ANSWERTYPE);
		String[] questionIDs = (String[])_prmValues.get(PRM_QUESTION_IDS);
		int NumberOfQuestionIDs = (questionIDs==null)?0:questionIDs.length;
		if(questions != null && answerType != null){
			for (int i = 0; i < questions.length && i < answerType.length; i++) {
				if(!"".equals(questions[i]) && !"".equals(answerType[i])){
					char type = answerType[i].charAt(0);
					
					SurveyQuestion question = null;
					if(NumberOfQuestionIDs <= i){
						question =  business.createSurveyQuestion(survey,questions[i],locale,type);
					} else {
						question = business.getQuestionHome().findByPrimaryKey(business.getQuestionHome().decode(questionIDs[i]));
						question =  business.updateSurveyQuestion(survey,question,questions[i],locale,type);
					}
					
					
					
					//save answers
					String[] answers = (String[])_prmValues.get(PRM_ANSWER+(i+1));
					String[] answerIDs = (String[])_prmValues.get(PRM_ANSWER_IDS+(i+1));
					int numberOfAnswerIDs = (answerIDs==null)?0:answerIDs.length;
					if(answers != null){
						for (int j = 0; j < answers.length; j++) {
							if(answers[j] != null && !"".equals(answers[j])){
								if(numberOfAnswerIDs <= j){
									business.createSurveyAnswer(question,(type == ANSWERTYPE_TEXTAREA)?"":answers[j],locale);
								} else {
									SurveyAnswer ans = business.getAnswerHome().findByPrimaryKey(business.getAnswerHome().decode(answerIDs[j]));
									business.updateSurveyAnswer(ans,(type == ANSWERTYPE_TEXTAREA)?"":answers[j],locale);
								}								
							}
						}
					}
				}
			}
		}
		
		return survey.getPrimaryKey();
	}

	/**
	 * @param iwc
	 */
	private Object createSurvey(IWContext iwc) throws IDOLookupException, IDOAddRelationshipException, RemoteException, CreateException {
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		ICLocale locale = ICLocaleBusiness.getICLocale(_iLocale);
		
		SurveyEntity survey = business.createSurvey(this.getWorkingFolder(),"Survey",null,IWTimestamp.RightNow(),null);
		
		String[] questions = (String[])_prmValues.get(PRM_QUESTION);
		String[] answerType = (String[])_prmValues.get(PRM_ANSWERTYPE);
		if(questions != null && answerType != null){
			for (int i = 0; i < questions.length && i < answerType.length; i++) {
				if(!"".equals(questions[i]) && !"".equals(answerType[i])){
					char type = answerType[i].charAt(0);
					SurveyQuestion question =  business.createSurveyQuestion(survey,questions[i],locale,type);
					
					if(type == ANSWERTYPE_TEXTAREA){
						// set answer = ""
						business.createSurveyAnswer(question,"",locale);
					} else {
						//save answers
						String[] answers = (String[])_prmValues.get(PRM_ANSWER+(i+1));
						if(answers != null){
							for (int j = 0; j < answers.length; j++) {
								if(answers[j] != null && !"".equals(answers[j])){
									business.createSurveyAnswer(question,answers[j],locale);
								}
							}
						}
					}
				}
			}
		}
		
		
		
		return survey.getPrimaryKey();
	}

	/**
	 * 
	 */
	private void beforeParameterListIsAdded() {
		//Number of questions parameter
		prmVector.add(new Parameter(PRM_NUMBER_OF_QUESTIONS,String.valueOf(_numberOfQuestions)));
	}

	private void processParameters(IWContext iwc) throws IDOLookupException, RemoteException, IDORelationshipException, FinderException {
		processSurveyIdPRM(iwc);
		processActionPRM(iwc);
		processStatePRM(iwc);
		processQuestionAndAnswerPRMs(iwc);
		processNumberOfQuestionsPRM(iwc);
		
		if(true){ //while in Edit mode
			maintainModePRM(iwc);
		}	
	}
	
	/**
	 * @param iwc
	 */
	private void processSurveyIdPRM(IWContext iwc) {
		String surveyLoaded = iwc.getParameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX);
		_surveyHasBeenLoaded = (surveyLoaded != null);
		if(_surveyHasBeenLoaded){
			prmVector.add(new Parameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX,Boolean.toString(_surveyHasBeenLoaded)));		
		}
		
		String id = iwc.getParameter(PRM_SURVEY_ID);
		try {
			//TODO replace Integer.decode(id) with SurveyEntityHome#decode(id)
			_surveyID = Integer.decode(id); //primaryKey for SurveyEntity
			prmVector.add(new Parameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX,_surveyID.toString()));
		} catch (NullPointerException e) {
			id = iwc.getParameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX);
			try {
				//TODO replace Integer.decode(id) with SurveyEntityHome#decode(id)
				_surveyID = Integer.decode(id);//primaryKey for SurveyEntity
				prmVector.add(new Parameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX,_surveyID.toString()));
			} catch (NullPointerException e1) {
				_surveyID = SURVEY_NOT_STORED;
			} catch (NumberFormatException e1) {
				_surveyID = SURVEY_NOT_STORED;
			} 
		}	 catch (NumberFormatException e) {
			id = iwc.getParameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX);
			try {
				//TODO replace Integer.decode(id) with SurveyEntityHome#decode(id)
				_surveyID = Integer.decode(id);//primaryKey for SurveyEntity
				prmVector.add(new Parameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX,_surveyID.toString()));
			} catch (NullPointerException e1) {
				_surveyID = SURVEY_NOT_STORED;
			} catch (NumberFormatException e1) {
				_surveyID = SURVEY_NOT_STORED;
			} 
		}	
	}

	/**
	 * @param iwc
	 */
	private void processQuestionAndAnswerPRMs(IWContext iwc) throws IDOLookupException, RemoteException, IDORelationshipException, FinderException {
		String surveyPRMVal = iwc.getParameter(PRM_SURVEY_ID);
		if(!_surveyHasBeenLoaded && _surveyID != null && surveyPRMVal != null){
			loadSurvey(iwc);
		} else {
			if(_surveyID != null){
				loadSurveyIDs(iwc);
			}
			
			//questions
			processParameterValues(iwc,PRM_QUESTION,true);
			processParameterValues(iwc,PRM_QUESTION_IDS,true);
			//answers
			String[] questions = iwc.getParameterValues(PRM_QUESTION);
			if(questions != null){
				for (int i = 1; i <= questions.length; i++) {
					processParameterValues(iwc,PRM_ANSWER+i,true);
					processParameterValues(iwc,PRM_ANSWER_IDS+i,true);
					processParameterValues(iwc,PRM_ADD_TEXT_INPUT+i,true);
				}
			}
			//answertypes
			processParameterValues(iwc,PRM_ANSWERTYPE,true);
			//number of answers
			processParameterValues(iwc,PRM_NUMBER_OF_ANSWERS,true);
		}
	}
	
	private void loadSurveyIDs(IWContext iwc)  throws IDOLookupException, RemoteException, FinderException, IDORelationshipException {
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		SurveyEntity survey = business.getSurveyHome().findByPrimaryKey(_surveyID);
		
		Vector prmQuestionIDs = new Vector();
		int questionNumber = 1;
		Collection questions = survey.getSurveyQuestions();
		
		for (Iterator qIter = questions.iterator(); qIter.hasNext();questionNumber++) {
			SurveyQuestion question = (SurveyQuestion)qIter.next();
			
			String sQuestionID = question.getPrimaryKey().toString();
			prmQuestionIDs.add(sQuestionID);
			prmVector.add(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,sQuestionID));
						
			Vector prmAnswerIDs = new Vector();
			Collection answers = business.getAnswerHome().findQuestionsAnswer(question);
			for (Iterator aIter = answers.iterator(); aIter.hasNext();) {
				SurveyAnswer answer = (SurveyAnswer)aIter.next();
			
				String sAnswerID = answer.getPrimaryKey().toString();
				prmAnswerIDs.add(sAnswerID);
				prmVector.add(new Parameter(PRM_ANSWER_IDS+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswerID));
			}
			_prmValues.put(PRM_ANSWER_IDS+questionNumber,prmAnswerIDs.toArray(new String[0]));
			
		}
		
		_prmValues.put(PRM_QUESTION_IDS,prmQuestionIDs.toArray(new String[0]));
		
	}
	
	/**
	 * @param iwc
	 */
	private void loadSurvey(IWContext iwc) throws IDOLookupException, RemoteException, FinderException, IDORelationshipException {
		ICLocale locale = ICLocaleBusiness.getICLocale(_iLocale);
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		SurveyEntity survey = business.getSurveyHome().findByPrimaryKey(_surveyID);
		
		Vector prmQuestion = new Vector();
		Vector prmQuestionIDs = new Vector();
		Vector prmAnswerTypes = new Vector();
		Vector prmNumberOfAnswers = new Vector();
		
		int questionNumber = 1;
		Collection questions = survey.getSurveyQuestions();
		_numberOfQuestions = Math.max(questions.size(),_numberOfQuestions);
		for (Iterator qIter = questions.iterator(); qIter.hasNext();questionNumber++) {
			SurveyQuestion question = (SurveyQuestion)qIter.next();
			
			String sQuestion = question.getQuestion(locale);
			prmQuestion.add(sQuestion);
			prmVector.add(new Parameter(PRM_QUESTION+PRM_MAINTAIN_SUFFIX,sQuestion));
			
			String sQuestionID = question.getPrimaryKey().toString();
			prmQuestionIDs.add(sQuestionID);
			prmVector.add(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,sQuestionID));
			
			String sAnswerType = String.valueOf(question.getAnswerType());
			prmAnswerTypes.add(sAnswerType);
			prmVector.add(new Parameter(PRM_ANSWERTYPE+PRM_MAINTAIN_SUFFIX,sAnswerType));
			
			Vector prmAnswers = new Vector();
			Vector prmAnswerIDs = new Vector();
			Collection answers = business.getAnswerHome().findQuestionsAnswer(question);
			for (Iterator aIter = answers.iterator(); aIter.hasNext();) {
				SurveyAnswer answer = (SurveyAnswer)aIter.next();
				
				String sAnswer = answer.getAnswer(locale);
				prmAnswers.add(sAnswer);
				prmVector.add(new Parameter(PRM_ANSWER+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswer));
			
				String sAnswerID = answer.getPrimaryKey().toString();
				prmAnswerIDs.add(sAnswerID);
				prmVector.add(new Parameter(PRM_ANSWER_IDS+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswerID));
			}
			String sNumberOfAnswers = String.valueOf(answers.size());
			prmNumberOfAnswers.add(sNumberOfAnswers);
			prmVector.add(new Parameter(PRM_NUMBER_OF_ANSWERS+PRM_MAINTAIN_SUFFIX,sNumberOfAnswers));

			_prmValues.put(PRM_ANSWER+questionNumber,prmAnswers.toArray(new String[0]));
			_prmValues.put(PRM_ANSWER_IDS+questionNumber,prmAnswerIDs.toArray(new String[0]));
			
		}
		
		_prmValues.put(PRM_QUESTION,prmQuestion.toArray(new String[0]));
		_prmValues.put(PRM_QUESTION_IDS,prmQuestionIDs.toArray(new String[0]));
		_prmValues.put(PRM_ANSWERTYPE,prmAnswerTypes.toArray(new String[0]));
		_prmValues.put(PRM_NUMBER_OF_ANSWERS,prmNumberOfAnswers.toArray(new String[0]));
		
		prmVector.add(new Parameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX,Boolean.toString(_surveyHasBeenLoaded)));

	}

	private void processParameterValues(IWContext iwc, String prmName, boolean maintain){
		String[] values = iwc.getParameterValues(prmName);
		if(values != null && values.length > 0){
			_prmValues.put(prmName,values);
			if(maintain){
				for (int i = 0; i < values.length; i++) {
					if(values[i] != null && !"".equals(values[i])){
						prmVector.add(new Parameter(prmName+PRM_MAINTAIN_SUFFIX,values[i]));
					}
				}
			}
		} else {
			values = iwc.getParameterValues(prmName+PRM_MAINTAIN_SUFFIX);
			if(values != null && values.length > 0){
				_prmValues.put(prmName,values);
				if(maintain){
					for (int i = 0; i < values.length; i++) {
						if(values[i] != null && !"".equals(values[i])){
							prmVector.add(new Parameter(prmName+PRM_MAINTAIN_SUFFIX,values[i]));
						}
					}
				}
			}	
		}
	}

	/**
	 * @param iwc
	 */
	private void maintainModePRM(IWContext iwc) {
		String smode = iwc.getParameter(Survey.PRM_SWITCHTO_MODE);
		String mode = iwc.getParameter(Survey.PRM_MODE);
		if( smode != null){
			prmVector.add(new Parameter(Survey.PRM_MODE,smode));
		} else if(mode != null){
			prmVector.add(new Parameter(Survey.PRM_MODE,mode));
		}
	}

	/**
	 * @param iwc
	 */
	private void processNumberOfQuestionsPRM(IWContext iwc) {
		
		String NumberOfQuestions = iwc.getParameter(PRM_NUMBER_OF_QUESTIONS);
		try {
			_numberOfQuestions = Integer.parseInt(NumberOfQuestions);
		} catch (NumberFormatException e1) {
			//
		}
		

		if((_lastState == STATE_ONE && _state==STATE_TWO)){ //|| (_lastState == STATE_TWO && _state==STATE_ONE)){
			String[] questions = (String[])_prmValues.get(PRM_QUESTION);
			if(questions != null && questions.length != 0){
				_numberOfQuestions =1;
				for (int i = 1; i < questions.length; i++) {
					if(questions[i] != null && !"".equals(questions[i])){
						_numberOfQuestions++;
					}
				}
			} else {
				//Warning
				//System.err.println(this.getClassName()+"[Warning]: Trying to go forward without defining any question");
			}
		} else {

			String alterNumberOfQuestions = iwc.getParameter(ADD_QUESTION_PRM);
			if(alterNumberOfQuestions!=null){
				String NumberOfQuestionsToAdd = iwc.getParameter(PRM_NUMBER_OF_QUESTIONS_TO_ADD);
				try {
					_numberOfQuestions += Integer.parseInt(NumberOfQuestionsToAdd);
				} catch (NumberFormatException e) {
					//
				}
			}

		}
		
	}
	
	/**
	 * @param iwc
	 */
	private void processActionPRM(IWContext iwc) {
		String action = iwc.getParameter(PRM_ACTION);
		boolean someAction = false;
		try {
			_action = Integer.parseInt(action);
			prmVector.add(new Parameter(PRM_LAST_ACTION,String.valueOf(_action)));
			someAction = true;
		} catch (NumberFormatException e) {
			_action=ACTION_NO_ACTION;
		}
		
		String lastAction = iwc.getParameter(PRM_LAST_ACTION);
		try {
			_lastAction = Integer.parseInt(lastAction);
			if(!someAction){
				prmVector.add(new Parameter(PRM_LAST_ACTION,String.valueOf(_lastAction)));
			}
		} catch (NumberFormatException e1) {
			if(!someAction){
				prmVector.add(new Parameter(PRM_LAST_ACTION,String.valueOf(ACTION_NO_ACTION)));
			}
		}
	}
	
	/**
	 * @param iwc
	 */
	private void processStatePRM(IWContext iwc) {
		String state = iwc.getParameter(PRM_CURRENT_STATE);
		try {
			_state = Integer.parseInt(state);
			_lastState = _state;
		} catch (NumberFormatException e1) {
			//
		}
		
		String gotoState = iwc.getParameter(PRM_GOTO_STATE);
		try {
			_state = Integer.parseInt(gotoState);
		} catch (NumberFormatException e) {
			//
		}
		
		prmVector.add(new Parameter(PRM_CURRENT_STATE,String.valueOf(_state)));
	}

	private PresentationObject getStateOne(){
		Table stateOne = new Table();
		int rowIndex = 0;
		
		
		String[] questions = (String[])_prmValues.get(PRM_QUESTION);
		String[] selectedAnsTypes = (String[])_prmValues.get(PRM_ANSWERTYPE);
		String[] numberOfAnswers = (String[])_prmValues.get(PRM_NUMBER_OF_ANSWERS);		
		
		for(int i = 1; i <= _numberOfQuestions; i++){
			String question = null;
			String selectedAnsType = null;
			String numberOfAns = null;
			if(questions != null && questions.length >= i){
				question = questions[i-1];
				selectedAnsType = selectedAnsTypes[i-1];
				numberOfAns = numberOfAnswers[i-1];
			}
			stateOne.add(getQuestionFieldset(i,question,selectedAnsType,numberOfAns),1,++rowIndex);
			
		}
		
		stateOne.add(getAddQuestionFieldset(),1,++rowIndex);
		
	

		SubmitButton saveButton = new SubmitButton(_iwrb.getLocalizedString("save","  Save  "),PRM_ACTION,String.valueOf(ACTION_SAVE));
		setStyle(saveButton);
		stateOne.add(saveButton,1,++rowIndex);
		stateOne.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
		
		stateOne.add(Text.NON_BREAKING_SPACE,1,rowIndex);
		
		SubmitButton forwardButton = new SubmitButton(_iwrb.getLocalizedString("forward","  Forward  "),PRM_GOTO_STATE,String.valueOf(STATE_TWO));
		setStyle(forwardButton);
		stateOne.add(forwardButton,1,rowIndex);
		//stateOne.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
		

		
		
		
		return stateOne;
	}
	
	private PresentationObject getStateTwo(){
		Table stateTwo = new Table();
		int rowIndex = 0;
		
		
		String[] questions = (String[])_prmValues.get(PRM_QUESTION);
		String[] answertypes = (String[])_prmValues.get(PRM_ANSWERTYPE);
		String[] numberOfAnswers = (String[])_prmValues.get(PRM_NUMBER_OF_ANSWERS);
		if(questions != null && questions.length != 0){
			_numberOfQuestions =0;
			for (int i = 0; i < questions.length; i++) {
				String question = questions[i];
				if(question!=null && !"".equals(question)){
					++_numberOfQuestions;
					char answertype = answertypes[i].charAt(0);
					int noAnswers = Integer.parseInt(numberOfAnswers[i]);
					stateTwo.add(getAnswerFieldset(_numberOfQuestions,question,answertype,noAnswers),1,++rowIndex);
				}
			}
		}
		
		
		
		//stateOne.add(getAddQuestionFieldset(),1,++rowIndex);
		
	
		SubmitButton backButton = new SubmitButton(_iwrb.getLocalizedString("back","  Back  "),PRM_GOTO_STATE,String.valueOf(STATE_ONE));
		setStyle(backButton);
		stateTwo.add(backButton,1,++rowIndex);
		stateTwo.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
		
//		stateTwo.add(Text.NON_BREAKING_SPACE,1,rowIndex);
//		SubmitButton CancelButton = new SubmitButton(_iwrb.getLocalizedString("cancel","  Cancel  "),PRM_ACTION,String.valueOf(ACTION_CANCEL));
//		setStyle(CancelButton);
//		stateTwo.add(CancelButton,1,rowIndex);
		
		stateTwo.add(Text.NON_BREAKING_SPACE,1,rowIndex);
		
		SubmitButton saveButton = new SubmitButton(_iwrb.getLocalizedString("save","  Save  "),PRM_ACTION,String.valueOf(ACTION_SAVE));
		setStyle(saveButton);
		stateTwo.add(saveButton,1,rowIndex);
		//stateOne.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
		


		
		
		
		return stateTwo;
	}
	

	
	private PresentationObject getAddQuestionFieldset(){
		Table t = new Table(2,1);
		
		SubmitButton addButton = new SubmitButton(ADD_QUESTION_PRM,_iwrb.getLocalizedString("add_questions_to_form","  Add  "));
		setStyle(addButton);
		
		DropdownMenu amount = new DropdownMenu(PRM_NUMBER_OF_QUESTIONS_TO_ADD);
		setStyle(amount);
		for(int i = 1; i <= 15; i++){
			amount.addMenuElement(i,String.valueOf(i));
		}
		amount.setSelectedElement(1);
		
		
		t.add(addButton,1,1);
		t.add(amount,2,1);
		
		
		return t;
	}
	
	private PresentationObject getQuestionFieldset(int no, String question, String selectedAnsType,String numberOfAns){
		FieldSet fs = new FieldSet(_iwrb.getLocalizedString("Question","Question")+" "+no);
		Table qt = new Table(2,3);
		qt.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		
		//qt.setBorder(1);
		
		//todo put id from db-table in hiddeninput for update
		
		qt.add(getLabel(_iwrb.getLocalizedString("Question","Question")),1,1);

		qt.add(getQuestionTextArea(PRM_QUESTION,question),2,1);
		qt.add(getLabel(_iwrb.getLocalizedString("Answer_type","Answer type")),1,2);
		qt.add(getAnswerTypeDropdownMenu(PRM_ANSWERTYPE,selectedAnsType),2,2);
		qt.add(getLabel(_iwrb.getLocalizedString("Number_of_answers","Number of answers")),1,3);
		qt.add(getNumberOfAnswersDropdownMenu(PRM_NUMBER_OF_ANSWERS,numberOfAns),2,3);
		
		
		
		fs.add(qt);
		return fs;
	}
	
	private PresentationObject getAnswerFieldset(int no, String questionText, char answerType, int numberOfAnswers){
		FieldSet fs = new FieldSet(_iwrb.getLocalizedString("Question","Question")+" "+no);
		Table qt = new Table();
		qt.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		
		//qt.setBorder(1);
		
		//todo put id from db-table in hiddeninput for update
		
		qt.add(getLabel(_iwrb.getLocalizedString("Question","Question")),1,1);
		PresentationObject question = getQuestionTextArea(PRM_QUESTION,questionText);
		qt.add(question,2,1);
		

		switch (answerType) {
			case ANSWERTYPE_SINGLE_CHOICE :		
				//break;
			case ANSWERTYPE_MULTI_CHOICE :
				qt.add(getLabel(_iwrb.getLocalizedString("Answers","Answers")),1,3);
				qt.add(getLabel(_iwrb.getLocalizedString("Answer_type","Answer type")),1,2);
				qt.add(getListAnswerTypeDropdownMenu(PRM_ANSWERTYPE,answerType),2,2);

				String[] answers = (String[])_prmValues.get(PRM_ANSWER+no);
				//String[] useTextInput = (String[])_prmValues.get(PRM_ADD_TEXT_INPUT+no);
				for(int i = 0; i < numberOfAnswers; i++){
					String ans = null;
					//String check = null;
					if(answers != null && answers.length > i){
						ans = answers[i];
					}
					//if(useTextInput != null && useTextInput.length > i){
						//check = answers[i];
					//}
					
					qt.add(getLabel(String.valueOf(i+1)),2,i+3);
					qt.add(getAnswerTextInput(PRM_ANSWER+no,ans),2,i+3);
//					qt.add(Text.NON_BREAKING_SPACE);
//					qt.add(getAddTextInputCheckBox(PRM_ADD_TEXT_INPUT+no,check),2,i+3);
				}
				break;
			case ANSWERTYPE_TEXTAREA :
				qt.add(getLabel(_iwrb.getLocalizedString("Answers","Answers")),1,2);
				qt.add(getAnswerTextArea("ans_ta",null,true),2,2);
				qt.add(new HiddenInput(PRM_ANSWERTYPE,String.valueOf(answerType)),2,2);

				break;
		}
		
				
		
		
		fs.add(qt);
		if(answerType != ANSWERTYPE_TEXTAREA){
			fs.add(getAddAnswerFieldSet(no));
		}
		return fs;
	}
	
	/**
	 * @param string
	 * @param check
	 * @return
	 */
	private PresentationObject getAddTextInputCheckBox(String name, String check) {
		CheckBox box = new CheckBox(name);
		
		if(check != null){
			box.setChecked(true);
		}
		//setStyle(box);
		return box;
	}

	/**
	 * @param string
	 * @param ans
	 * @return
	 */
	private PresentationObject getAnswerTextInput(String name, String displayText) {
		TextInput i = new TextInput(name);
		i.setSize(40);
		setStyle(i);
		if(displayText != null){
			i.setValue(displayText);
		}
		return i;
	}

	private PresentationObject getAddAnswerFieldSet(int questionNumber){
		Table t = new Table(2,1);
		
		SubmitButton addButton = new SubmitButton(_iwrb.getLocalizedString("add_answers_to_question","  Add  "),ADD_ANSWER_PRM,"_"+questionNumber);
		setStyle(addButton);
		
		DropdownMenu amount = new DropdownMenu(PRM_NUMBER_OF_ANSWERS_TO_ADD+"_"+questionNumber);
		setStyle(amount);
		for(int i = 1; i <= 15; i++){
			amount.addMenuElement(i,String.valueOf(i));
		}
		amount.setSelectedElement(1);
		
		
		t.add(addButton,1,1);
		t.add(amount,2,1);
		
		
		return t;
	}


	
	/**
	 * @param string
	 * @return
	 */
	private DropdownMenu getNumberOfAnswersDropdownMenu(String name,String value) {
		DropdownMenu d = new DropdownMenu(name);
		setStyle(d);
		for(int i = 1; i <= 15; i++){
			d.addMenuElement(i,String.valueOf(i));
		}
		if(value != null){
			d.setSelectedElement(value);
		} else {
			d.setSelectedElement(_defaultNumberOfAnswers);
		}
		
		return d;
	}

	/**
	 * @param string
	 * @return
	 */
	private DropdownMenu getAnswerTypeDropdownMenu(String name, String value) {
		DropdownMenu d = new DropdownMenu(name);
		setStyle(d);
		d.addMenuElement(ANSWERTYPE_SINGLE_CHOICE,_iwrb.getLocalizedString("Radio_group","Radio group (single-choice)"));
		d.addMenuElement(ANSWERTYPE_MULTI_CHOICE,_iwrb.getLocalizedString("Checkboxes","Checkboxes  (multi-choice)"));
		d.addMenuElement(ANSWERTYPE_TEXTAREA,_iwrb.getLocalizedString("Textarea","Textarea"));
		if(value != null){
			d.setSelectedElement(value);
		}
		return d;
	}
	
	/**
	 * @param string
	 * @return
	 */
	private DropdownMenu getListAnswerTypeDropdownMenu(String name, char value) {
		DropdownMenu d = new DropdownMenu(name);
		setStyle(d);
		d.addMenuElement(ANSWERTYPE_SINGLE_CHOICE,_iwrb.getLocalizedString("Radio_group","Radio group (single-choice)"));
		d.addMenuElement(ANSWERTYPE_MULTI_CHOICE,_iwrb.getLocalizedString("Checkboxes","Checkboxes  (multi-choice)"));
		d.setSelectedElement(value);
		return d;
	}

	/**
	 * @return
	 */
	private PresentationObject getQuestionTextArea(String name, String displayText) {
		TextArea t = new TextArea(name);
		if(displayText!=null){
			t.setValue(displayText);
		}
		setStyle(t);
		t.setColumns(50);
		t.setRows(3);
		return t;
	}
	
	
	 private PresentationObject getAnswerTextArea(String name, String displayText, boolean disabled) {
		 TextArea t = new TextArea(name);
		 if(displayText!=null){
			 t.setValue(displayText);
		 }
		 setStyle(t);
		 t.setColumns(40);
		 t.setRows(4);
		 t.setDisabled(disabled);
		 return t;
	 }

	/**
	 * @param string
	 * @return
	 */
	private Text getLabel(String string) {
		Text t = new Text(string+": ");
		setStyle(t);
		return t;
	}
	
	public void setStyle(PresentationObject obj){
		if(obj instanceof Text){
			this.setStyle((Text)obj);
		} else if(obj instanceof GenericButton) {
			obj.setMarkupAttribute("style",STYLE_BUTTON);
		} else {
			obj.setMarkupAttribute("style",STYLE);
		}
	}

	public void setStyle(Text obj){
		obj.setMarkupAttribute("style",STYLE_2);
	}
	
	public synchronized Object clone(){
		SurveyEditor clone = (SurveyEditor)super.clone();
		clone._prmValues = new HashMap();
		clone.prmVector = new Vector();
		return clone;
	}
	public Help getHelp(String helpTextKey) {
		Help help = new Help();
		help.setHelpTextBundle( MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(helpTextKey);
		help.setLinkText("help");
		return help;
	}

}
