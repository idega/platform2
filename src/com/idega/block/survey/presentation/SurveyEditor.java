/*
 * Created on 30.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.presentation;


import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.category.presentation.FolderBlock;
import com.idega.block.survey.business.SurveyBusiness;
import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
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
import com.idega.presentation.ui.TimestampInput;
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



	final static String IW_BUNDLE_IDENTIFIER = Survey.IW_BUNDLE_IDENTIFIER;
	static final String HELP_BUNDLE_IDENTIFIER = Survey.IW_BUNDLE_IDENTIFIER;
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

	private final static String PRM_SURVEY_NAME = "sur_N";
	private final static String PRM_SURVEY_DESCRIPTION = "sur_D";
	private final static String PRM_SURVEY_START_TIME = "sur_Sd";
	private final static String PRM_SURVEY_END_DATE = "sur_Ed";
	private final static String PARAMETER_DELETE = "surpdl";
	
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
	
	public static final String PRM_SURVEY_SELECTED = "su_sursel";
	
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
	
	public static final String PRM_DELETE_QUESTION = "su_del_q";
	public static final String PRM_DELETE_ANSWER = "su_del_a";
	
	public static final String PRM_DELETED_QUESTION = "su_del2_q";
	public static final String PRM_DELETED_ANSWER = "su_del2_a";

	
	private boolean _surveyHasBeenLoaded = false;
	public static final String PRM_SURVEY_LOADED = "su_loaded";
	
	private Vector prmVector = new Vector();
	private HashMap _prmValues = new HashMap();
	private Vector _delQuestion = new Vector();
	private Vector _delAnswer = new Vector();
	private boolean _maintainDelPRM = true;
	private Vector _deletedQuestion = new Vector();
	private Vector _deletedAnswer = new Vector();
	
	private String messageTextStyle;// = "font-weight: bold;";
	private String messageTextHighlightStyle ;//= "font-weight: bold;color: #FF0000;";



	/**
	 * 
	 */
	public SurveyEditor(int instanceID) {
		super();
		this.useLocalizedFolders(false);
		setICObjectInstanceID(instanceID);
		this.prmVector = new Vector();
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void setInitialNumberOfQuestions(int number){
		if(number > 0 ){
			this._numberOfQuestions = number;
		}
	}
	
	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		this._iwc = iwc;
		this._iwrb = getResourceBundle(iwc);
		this._iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		this._iwbSurvey = getBundle(iwc);
//		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		this._iLocale = iwc.getCurrentLocale();
		this._date = new IWTimestamp();
		
		processParameters(iwc);
	}
	
	private Link getModeChangeLink() {
		Image editImage = this._iwb.getImage("shared/edit.gif");
		Link adminLink = new Link(editImage);
		adminLink.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_SURVEY);

		return adminLink;
	}
	
	public void main(IWContext iwc) throws Exception {
		
		add(getModeChangeLink());
		
		Form myForm = new Form();
		
		//save to DB
		if(this._action==ACTION_SAVE){
			
			if(this._surveyID == SURVEY_NOT_STORED){
				//create
				Object pk = createSurvey(iwc);
				this.prmVector.add(new Parameter(PRM_SURVEY_ID,String.valueOf(pk)));
			} else {
				//update
				storeSurvey(iwc);
				//prmVector.add(new Parameter(PRM_SURVEY_ID,String.valueOf(pk)));
			}
			
			add(Text.BREAK);
			add(Text.BREAK);
			add(getMessageTextObject(this._iwrb.getLocalizedString("survey_has_been_saved","Survey has been saved"),false));
			add(Text.BREAK);
			add(Text.BREAK);
		}
		
		if (!iwc.isParameterSet(PRM_SURVEY_SELECTED) || this._action==ACTION_SAVE) {
			handleDelete(iwc);
			myForm.add(getSurveyList(iwc));
		} else {
		//Edit		
//		if(this.hasEditPermission()){
			myForm.maintainParameter(PRM_SURVEY_SELECTED);
			switch (this._state) {
				case STATE_ONE :
					add(getHelp("su_help_question_step"));
					myForm.add(getStateOne(iwc));
					break;
				case STATE_TWO :
					add(getHelp("su_help_answer_step"));
					myForm.add(getStateTwo(iwc));
					break;
			}			
//		} else {
//			//store information temporary while logging in
//		}

		}

		
		this.add(myForm);
		
		beforeParameterListIsAdded();
		for (Iterator iter = this.prmVector.iterator(); iter.hasNext();) {
			myForm.add((Parameter)iter.next());
		}

	}
	
	private void handleDelete(IWContext iwc) throws RemoteException {
		String toDelete = iwc.getParameter(PARAMETER_DELETE);
		if (toDelete != null) {
			SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
			try {
				SurveyEntity entity = business.getSurveyHome().findByPrimaryKey(new Integer(toDelete));
				entity.setRemoved(iwc.getCurrentUser());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private FieldSet getSurveyList(IWContext iwc) throws RemoteException {
		FieldSet fs = new FieldSet(this._iwrb.getLocalizedString("survey_editor", "Survey editor"));
		Table table = new Table();
		int row = 1;
		
		try {
			SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
			Collection surveys = business.getSurveyHome().findAllSurveys(this.getWorkFolder().getEntity());
			
			table.add(getMessageTextObject(this._iwrb.getLocalizedString("name", "Name"), true), 1, row);
			table.add(getMessageTextObject(this._iwrb.getLocalizedString("description", "Description"), true), 2, row);
			table.add(getMessageTextObject(this._iwrb.getLocalizedString("begins", "Begins"), true), 3, row);
			table.add(getMessageTextObject(this._iwrb.getLocalizedString("ends", "Ends"), true), 4, row);
			if (surveys != null && !surveys.isEmpty()) {
				SurveyEntity survey;
				Link link;
				Link del;
				Image delIm = this._iwb.getImage("/shared/delete.gif");
				IWTimestamp from;
				IWTimestamp to;
				
				Iterator iter = surveys.iterator();
				while (iter.hasNext()) {
					++row;
					survey = (SurveyEntity) iter.next();
					link = new Link(getMessageTextObject(survey.getName(), false));
					link.addParameter(PRM_SURVEY_SELECTED, "true");
					link.addParameter(PRM_SURVEY_ID, survey.getPrimaryKey().toString());
					link.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_EDIT);
					
					try {
						from = new IWTimestamp(survey.getStartTime());
					} catch (Exception e ) {
						from = null;
					}
					try {
						to = new IWTimestamp(survey.getEndTime());
					} catch (Exception e ) {
						to = null;
					}
					
					table.add(link, 1, row);
					table.add(getMessageTextObject(survey.getDescription(), false), 2, row);
					if (from != null) {
						table.add(from.getLocaleDateAndTime(this._iLocale), 3, row);
					}
					if (to != null) {
						table.add(to.getLocaleDateAndTime(this._iLocale), 4, row);
					}
					
					del = new Link(delIm);
					//del.addParameter(PRM_SURVEY_SELECTED, "true");
					del.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_EDIT);
					del.addParameter(PARAMETER_DELETE, survey.getPrimaryKey().toString());
					table.add(del, 5, row);
					
					//addAttribute(getColumnNameName(), "Name", true, true, String.class);
					//addAttribute(getColumnNameDescription(), "Description", true, true, String.class);
					//addAttribute(getColumnNameStartTime(), "Begins", true, true, Timestamp.class);
					//addAttribute(getColumnNameEndTime(), "Ends", true, true, Timestamp.class);
					
				}

			}
			//business.getSurveyHome().findActiveSurveys(this.get)
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		//TODO crappis
		
		
		
		fs.add(table);
		return fs;
	}
	
	
	/**
	 * @param iwc
	 * @return
	 */
	private Object storeSurvey(IWContext iwc) throws IDOAddRelationshipException, CreateException, FinderException, IDORemoveRelationshipException, IDOLookupException, EJBException, RemoteException, RemoveException {
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		ICLocale locale = ICLocaleBusiness.getICLocale(this._iLocale);
		
		String sName = iwc.getParameter(PRM_SURVEY_NAME);
		String sDesc = iwc.getParameter(PRM_SURVEY_DESCRIPTION);
		String sFrom = iwc.getParameter(PRM_SURVEY_START_TIME);
		String sTo = iwc.getParameter(PRM_SURVEY_END_DATE);
		IWTimestamp fromStamp = IWTimestamp.RightNow();
		IWTimestamp toStamp = null;
		try {
			fromStamp = new IWTimestamp(sFrom);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			toStamp = new IWTimestamp(sTo);
		} catch(Exception e) {
			//e.printStackTrace();
			logWarning("SurveyEditor : toStamp was not created (ParameterValue = 	"+sTo+")");
		}
		
		if (sName == null || sName.equals("")) {
			sName = "Survey";
		}
		if (sDesc == null) {
			sDesc = "";
		}
		SurveyEntity survey = null;
		
		if(this._surveyID != null){
			survey = business.getSurveyHome().findByPrimaryKey(this._surveyID);
			survey.setName(sName);
			survey.setDescription(sDesc);
			if (fromStamp != null) {
				survey.setStartTime(fromStamp.getTimestamp());
			} else {
				survey.setStartTime(null);
			}
			if (toStamp != null) {
				survey.setEndTime(toStamp.getTimestamp());
			} else {
				survey.setEndTime(null);
			}
			survey.store();
		} else {
			survey = business.createSurvey(this.getWorkFolder(),sName,sDesc,fromStamp, toStamp);
		}
		
		String[] questions = (String[])this._prmValues.get(PRM_QUESTION);
		String[] answerType = (String[])this._prmValues.get(PRM_ANSWERTYPE);
		String[] questionIDs = (String[])this._prmValues.get(PRM_QUESTION_IDS);
		int NumberOfQuestionIDs = (questionIDs==null)?0:questionIDs.length;
		if(questions != null && answerType != null){
			for (int i = 0; i < questions.length && i < answerType.length; i++) {
				if(!"".equals(questions[i]) && !"".equals(answerType[i])){
					char type = answerType[i].charAt(0);
					
					SurveyQuestion question = null;
					if(NumberOfQuestionIDs <= i){
						question =  business.createSurveyQuestion(survey,questions[i],locale,type);
						this.prmVector.add(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,question.toString()));
					} else {
						question = business.getQuestionHome().findByPrimaryKey(business.getQuestionHome().decode(questionIDs[i]));
//						if(questions[i] != null && !"".equals(questions[i])){
//							business.removeQuestionFromSurvey(survey,question,iwc.getCurrentUser());
//							prmVector.remove(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,questionIDs[i]));
//						} else{
							question =  business.updateSurveyQuestion(survey,question,questions[i],locale,type);
//						}
					}
					
					
					
					//save answers
					String[] answers = (String[])this._prmValues.get(PRM_ANSWER+(i+1));
					String[] answerIDs = (String[])this._prmValues.get(PRM_ANSWER_IDS+(i+1));
					int numberOfAnswerIDs = (answerIDs==null)?0:answerIDs.length;
					if(answers != null){
						for (int j = 0; j < answers.length; j++) {
							if(answers[j] != null && !"".equals(answers[j])){
								if(numberOfAnswerIDs <= j){
									SurveyAnswer ans = business.createSurveyAnswer(question,(type == ANSWERTYPE_TEXTAREA)?"":answers[j],locale);
									this.prmVector.add(new Parameter(PRM_ANSWER_IDS+(i+1)+PRM_MAINTAIN_SUFFIX,ans.toString()));
								} else {
									SurveyAnswer ans = business.getAnswerHome().findByPrimaryKey(business.getAnswerHome().decode(answerIDs[j]));
//									if(answers[j] != null && !"".equals(answers[j]) && type != ANSWERTYPE_TEXTAREA){
//										prmVector.remove(new Parameter(PRM_ANSWER_IDS+(i+1)+PRM_MAINTAIN_SUFFIX,answerIDs[j]));
//										business.removeAnswerFromQuestion(question,ans,iwc.getCurrentUser());
//									} else {
										business.updateSurveyAnswer(ans,(type == ANSWERTYPE_TEXTAREA)?"":answers[j],locale);
//									}
								}								
							}
						}
					}
				}
			}
		}
		
		for (Iterator dQuestion = this._delQuestion.iterator(); dQuestion.hasNext();) {
			String dPK = (String)dQuestion.next();
			SurveyQuestion question = business.getQuestionHome().findByPrimaryKey(business.getQuestionHome().decode(dPK));
			this.prmVector.remove(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,question.toString()));
			business.removeQuestionFromSurvey(survey,question,iwc.getCurrentUser());
		}
		
		for (Iterator dAns = this._delAnswer.iterator(); dAns.hasNext();) {
			String dPK = (String)dAns.next();
			SurveyAnswer ans = business.getAnswerHome().findByPrimaryKey(business.getAnswerHome().decode(dPK));
//TMP	//TODO 
//			prmVector.add(new Parameter(PRM_ANSWER_IDS+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswerID));
			business.removeAnswer(ans,iwc.getCurrentUser());
		}
		this._maintainDelPRM = false;
		return survey.getPrimaryKey();
	}

	/**
	 * @param iwc
	 */
	private Object createSurvey(IWContext iwc) throws IDOLookupException, IDOAddRelationshipException, RemoteException, CreateException {
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		ICLocale locale = ICLocaleBusiness.getICLocale(this._iLocale);
		
		String sName = iwc.getParameter(PRM_SURVEY_NAME);
		String sDesc = iwc.getParameter(PRM_SURVEY_DESCRIPTION);
		String sFrom = iwc.getParameter(PRM_SURVEY_START_TIME);
		String sTo = iwc.getParameter(PRM_SURVEY_END_DATE);
		IWTimestamp fromStamp = IWTimestamp.RightNow();
		IWTimestamp toStamp = null;
		try {
			fromStamp = new IWTimestamp(sFrom);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			toStamp = new IWTimestamp(sTo);
		} catch(Exception e) {
			logWarning("SurveyEditor : toStamp was not created (ParameterValue = 	"+sTo+")");
			//e.printStackTrace();
		}
		
		
		if (sName == null || sName.equals("")) {
			sName = "Survey";
		}
		if (sDesc == null) {
			sDesc = "";
		}
		SurveyEntity survey = business.createSurvey(this.getWorkFolder(),sName,sDesc,fromStamp,toStamp);
		
		String[] questions = (String[])this._prmValues.get(PRM_QUESTION);
		String[] answerType = (String[])this._prmValues.get(PRM_ANSWERTYPE);
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
						String[] answers = (String[])this._prmValues.get(PRM_ANSWER+(i+1));
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
		if(this._maintainDelPRM){
			for (Iterator dQuestion = this._delQuestion.iterator(); dQuestion.hasNext();) {
				String dPK = (String)dQuestion.next();
				this.prmVector.add(new Parameter(PRM_DELETE_QUESTION,dPK));
			}
			
			for (Iterator dAns = this._delAnswer.iterator(); dAns.hasNext();) {
				String dPK = (String)dAns.next();
				this.prmVector.add(new Parameter(PRM_DELETE_ANSWER,dPK));
			}
		} else {
			for (Iterator dQuestion = this._delQuestion.iterator(); dQuestion.hasNext();) {
				String dPK = (String)dQuestion.next();
				this.prmVector.add(new Parameter(PRM_DELETED_QUESTION,dPK));
			}
			
			for (Iterator dAns = this._delAnswer.iterator(); dAns.hasNext();) {
				String dPK = (String)dAns.next();
				this.prmVector.add(new Parameter(PRM_DELETED_ANSWER,dPK));
			}
		}
		
		//Number of questions parameter
		this.prmVector.add(new Parameter(PRM_NUMBER_OF_QUESTIONS,String.valueOf(this._numberOfQuestions)));
	}

	private void processParameters(IWContext iwc) throws IDOLookupException, RemoteException, IDORelationshipException, FinderException {
		processSurveyIdPRM(iwc);
		processActionPRM(iwc);
		processStatePRM(iwc);
		processDeleteParameters(iwc);
		processQuestionAndAnswerPRMs(iwc);
		processNumberOfQuestionsPRM(iwc);
		
		if(true){ //while in Edit mode
			maintainModePRM(iwc);
		}	
	}
	
	/**
	 * @param iwc
	 */
	private void processDeleteParameters(IWContext iwc) {
		String[] deletedQuestions = iwc.getParameterValues(PRM_DELETED_QUESTION);
			if(deletedQuestions != null){
				for (int i = 0; i < deletedQuestions.length; i++) {
					this._deletedQuestion.add(deletedQuestions[i]);
					this.prmVector.remove(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,deletedQuestions[i]));
				}
			}
	
			String[] deletedAnswers = iwc.getParameterValues(PRM_DELETED_ANSWER);
			if(deletedAnswers!= null){
				for (int i = 0; i < deletedAnswers.length; i++) {
					this._deletedAnswer.add(deletedAnswers[i]);
//					prmVector.add(new Parameter(PRM_ANSWER_IDS+questionNumber+PRM_MAINTAIN_SUFFIX,deletedAnswers[i]));
				}
			}

		
		
		String[] delQuestions = iwc.getParameterValues(PRM_DELETE_QUESTION);
		if(delQuestions != null){
			for (int i = 0; i < delQuestions.length; i++) {
				if(!this._deletedQuestion.contains(delQuestions[i])){
					this._delQuestion.add(delQuestions[i]);
				}
			}
		}
		
		String[] delAnswers = iwc.getParameterValues(PRM_DELETE_ANSWER);
		if(delAnswers!= null){
			for (int i = 0; i < delAnswers.length; i++) {
				if(!this._deletedAnswer.contains(delAnswers[i])){
					this._delAnswer.add(delAnswers[i]);
				}
			}
		}
	}

	/**
	 * @param iwc
	 */
	private void processSurveyIdPRM(IWContext iwc) {
		String surveyLoaded = iwc.getParameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX);
		this._surveyHasBeenLoaded = (surveyLoaded != null);
		if(this._surveyHasBeenLoaded){
			this.prmVector.add(new Parameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX,Boolean.toString(this._surveyHasBeenLoaded)));		
		}
		
		String id = iwc.getParameter(PRM_SURVEY_ID);
		try {
			//TODO replace Integer.decode(id) with SurveyEntityHome#decode(id)
			this._surveyID = Integer.decode(id); //primaryKey for SurveyEntity
			this.prmVector.add(new Parameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX,this._surveyID.toString()));
		} catch (NullPointerException e) {
			id = iwc.getParameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX);
			try {
				//TODO replace Integer.decode(id) with SurveyEntityHome#decode(id)
				this._surveyID = Integer.decode(id);//primaryKey for SurveyEntity
				this.prmVector.add(new Parameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX,this._surveyID.toString()));
			} catch (NullPointerException e1) {
				this._surveyID = SURVEY_NOT_STORED;
			} catch (NumberFormatException e1) {
				this._surveyID = SURVEY_NOT_STORED;
			} 
		}	 catch (NumberFormatException e) {
			id = iwc.getParameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX);
			try {
				//TODO replace Integer.decode(id) with SurveyEntityHome#decode(id)
				this._surveyID = Integer.decode(id);//primaryKey for SurveyEntity
				this.prmVector.add(new Parameter(PRM_SURVEY_ID+PRM_MAINTAIN_SUFFIX,this._surveyID.toString()));
			} catch (NullPointerException e1) {
				this._surveyID = SURVEY_NOT_STORED;
			} catch (NumberFormatException e1) {
				this._surveyID = SURVEY_NOT_STORED;
			} 
		}	
	}

	/**
	 * @param iwc
	 */
	private void processQuestionAndAnswerPRMs(IWContext iwc) throws IDOLookupException, RemoteException, IDORelationshipException, FinderException {
		String surveyPRMVal = iwc.getParameter(PRM_SURVEY_ID);
		if(!this._surveyHasBeenLoaded && this._surveyID != null && surveyPRMVal != null){
			loadSurvey(iwc);
		} else {
			if(this._surveyID != null && !this._surveyHasBeenLoaded){
				loadSurveyIDs(iwc);
			}
			
			//survey
			processParameterValues(iwc,PRM_SURVEY_NAME, true);
			processParameterValues(iwc,PRM_SURVEY_DESCRIPTION, true);
			processParameterValues(iwc,PRM_SURVEY_START_TIME, true);
			processParameterValues(iwc,PRM_SURVEY_END_DATE, true);
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
		SurveyEntity survey = business.getSurveyHome().findByPrimaryKey(this._surveyID);
		
		Vector prmQuestionIDs = new Vector();
		int questionNumber = 1;
		Collection questions = survey.getSurveyQuestions();
		
		for (Iterator qIter = questions.iterator(); qIter.hasNext();questionNumber++) {
			SurveyQuestion question = (SurveyQuestion)qIter.next();
			
			String sQuestionID = question.getPrimaryKey().toString();
			prmQuestionIDs.add(sQuestionID);
			this.prmVector.add(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,sQuestionID));
						
			Vector prmAnswerIDs = new Vector();
			Collection answers = business.getAnswerHome().findQuestionsAnswer(question);
			for (Iterator aIter = answers.iterator(); aIter.hasNext();) {
				SurveyAnswer answer = (SurveyAnswer)aIter.next();
			
				String sAnswerID = answer.getPrimaryKey().toString();
				prmAnswerIDs.add(sAnswerID);
				this.prmVector.add(new Parameter(PRM_ANSWER_IDS+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswerID));
			}
			this._prmValues.put(PRM_ANSWER_IDS+questionNumber,prmAnswerIDs.toArray(new String[0]));
			
		}
		
		this._prmValues.put(PRM_QUESTION_IDS,prmQuestionIDs.toArray(new String[0]));
		
		this.prmVector.add(new Parameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX,Boolean.toString(this._surveyHasBeenLoaded)));

		
	}
	
	/**
	 * @param iwc
	 */
	private void loadSurvey(IWContext iwc) throws IDOLookupException, RemoteException, FinderException, IDORelationshipException {
		ICLocale locale = ICLocaleBusiness.getICLocale(this._iLocale);
		SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		SurveyEntity survey = business.getSurveyHome().findByPrimaryKey(this._surveyID);
		
		Vector prmQuestion = new Vector();
		Vector prmQuestionIDs = new Vector();
		Vector prmAnswerTypes = new Vector();
		Vector prmNumberOfAnswers = new Vector();
		
		int questionNumber = 1;
		Collection questions = survey.getSurveyQuestions();
		this._numberOfQuestions = Math.max(questions.size(),this._numberOfQuestions);
		for (Iterator qIter = questions.iterator(); qIter.hasNext();questionNumber++) {
			SurveyQuestion question = (SurveyQuestion)qIter.next();
			
			String sQuestion = question.getQuestion(locale);
			prmQuestion.add(sQuestion);
			this.prmVector.add(new Parameter(PRM_QUESTION+PRM_MAINTAIN_SUFFIX,sQuestion));
			
			String sQuestionID = question.getPrimaryKey().toString();
			prmQuestionIDs.add(sQuestionID);
			this.prmVector.add(new Parameter(PRM_QUESTION_IDS+PRM_MAINTAIN_SUFFIX,sQuestionID));
			
			String sAnswerType = String.valueOf(question.getAnswerType());
			prmAnswerTypes.add(sAnswerType);
			this.prmVector.add(new Parameter(PRM_ANSWERTYPE+PRM_MAINTAIN_SUFFIX,sAnswerType));
			
			Vector prmAnswers = new Vector();
			Vector prmAnswerIDs = new Vector();
			Collection answers = business.getAnswerHome().findQuestionsAnswer(question);
			for (Iterator aIter = answers.iterator(); aIter.hasNext();) {
				SurveyAnswer answer = (SurveyAnswer)aIter.next();
				
				String sAnswer = answer.getAnswer(locale);
				prmAnswers.add(sAnswer);
				this.prmVector.add(new Parameter(PRM_ANSWER+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswer));
			
				String sAnswerID = answer.getPrimaryKey().toString();
				prmAnswerIDs.add(sAnswerID);
				this.prmVector.add(new Parameter(PRM_ANSWER_IDS+questionNumber+PRM_MAINTAIN_SUFFIX,sAnswerID));
			}
			String sNumberOfAnswers = String.valueOf(answers.size());
			prmNumberOfAnswers.add(sNumberOfAnswers);
			this.prmVector.add(new Parameter(PRM_NUMBER_OF_ANSWERS+PRM_MAINTAIN_SUFFIX,sNumberOfAnswers));

			this._prmValues.put(PRM_ANSWER+questionNumber,prmAnswers.toArray(new String[0]));
			this._prmValues.put(PRM_ANSWER_IDS+questionNumber,prmAnswerIDs.toArray(new String[0]));
			
		}
		
		this._prmValues.put(PRM_QUESTION,prmQuestion.toArray(new String[0]));
		this._prmValues.put(PRM_QUESTION_IDS,prmQuestionIDs.toArray(new String[0]));
		this._prmValues.put(PRM_ANSWERTYPE,prmAnswerTypes.toArray(new String[0]));
		this._prmValues.put(PRM_NUMBER_OF_ANSWERS,prmNumberOfAnswers.toArray(new String[0]));
		
		this.prmVector.add(new Parameter(PRM_SURVEY_LOADED+PRM_MAINTAIN_SUFFIX,Boolean.toString(this._surveyHasBeenLoaded)));

	}

	private void processParameterValues(IWContext iwc, String prmName, boolean maintain){
		String[] values = iwc.getParameterValues(prmName);
		if(values != null && values.length > 0){
			this._prmValues.put(prmName,values);
			if(maintain){
				for (int i = 0; i < values.length; i++) {
					if(values[i] != null && !"".equals(values[i])){
						this.prmVector.add(new Parameter(prmName+PRM_MAINTAIN_SUFFIX,values[i]));
					}
				}
			}
		} else {
			values = iwc.getParameterValues(prmName+PRM_MAINTAIN_SUFFIX);
			if(values != null && values.length > 0){
				this._prmValues.put(prmName,values);
				if(maintain){
					for (int i = 0; i < values.length; i++) {
						if(values[i] != null && !"".equals(values[i])){
							this.prmVector.add(new Parameter(prmName+PRM_MAINTAIN_SUFFIX,values[i]));
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
			this.prmVector.add(new Parameter(Survey.PRM_MODE,smode));
		} else if(mode != null){
			this.prmVector.add(new Parameter(Survey.PRM_MODE,mode));
		}
	}

	/**
	 * @param iwc
	 */
	private void processNumberOfQuestionsPRM(IWContext iwc) {
		
		String NumberOfQuestions = iwc.getParameter(PRM_NUMBER_OF_QUESTIONS);
		try {
			this._numberOfQuestions = Integer.parseInt(NumberOfQuestions);
		} catch (NumberFormatException e1) {
			//
		}
		

		if((this._lastState == STATE_ONE && this._state==STATE_TWO)){ //|| (_lastState == STATE_TWO && _state==STATE_ONE)){
			String[] questions = (String[])this._prmValues.get(PRM_QUESTION);
			if(questions != null && questions.length != 0){
				this._numberOfQuestions =1;
				for (int i = 1; i < questions.length; i++) {
					if(questions[i] != null && !"".equals(questions[i])){
						this._numberOfQuestions++;
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
					this._numberOfQuestions += Integer.parseInt(NumberOfQuestionsToAdd);
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
			this._action = Integer.parseInt(action);
			this.prmVector.add(new Parameter(PRM_LAST_ACTION,String.valueOf(this._action)));
			someAction = true;
		} catch (NumberFormatException e) {
			this._action=ACTION_NO_ACTION;
		}
		
		String lastAction = iwc.getParameter(PRM_LAST_ACTION);
		try {
			this._lastAction = Integer.parseInt(lastAction);
			if(!someAction){
				this.prmVector.add(new Parameter(PRM_LAST_ACTION,String.valueOf(this._lastAction)));
			}
		} catch (NumberFormatException e1) {
			if(!someAction){
				this.prmVector.add(new Parameter(PRM_LAST_ACTION,String.valueOf(ACTION_NO_ACTION)));
			}
		}
	}
	
	/**
	 * @param iwc
	 */
	private void processStatePRM(IWContext iwc) {
		String state = iwc.getParameter(PRM_CURRENT_STATE);
		try {
			this._state = Integer.parseInt(state);
			this._lastState = this._state;
		} catch (NumberFormatException e1) {
			//
		}
		
		String gotoState = iwc.getParameter(PRM_GOTO_STATE);
		try {
			this._state = Integer.parseInt(gotoState);
		} catch (NumberFormatException e) {
			//
		}
		
		this.prmVector.add(new Parameter(PRM_CURRENT_STATE,String.valueOf(this._state)));
	}

	private PresentationObject getStateOne(IWContext iwc){
		Table stateOne = new Table();
		int rowIndex = 0;
		
		
		stateOne.add(getSurveyInfoFieldset(iwc), 1, ++rowIndex);
		
		String[] questions = (String[])this._prmValues.get(PRM_QUESTION);
		String[] selectedAnsTypes = (String[])this._prmValues.get(PRM_ANSWERTYPE);
		String[] numberOfAnswers = (String[])this._prmValues.get(PRM_NUMBER_OF_ANSWERS);		
		String[] questionIDs = (String[])this._prmValues.get(PRM_QUESTION_IDS);
		
		for(int i = 1; i <= this._numberOfQuestions; i++){
			String question = null;
			String selectedAnsType = null;
			String numberOfAns = null;
			if(questions != null && questions.length >= i){
				question = questions[i-1];
				selectedAnsType = selectedAnsTypes[i-1];
				numberOfAns = numberOfAnswers[i-1];
			}
			stateOne.add(getQuestionFieldset(i,question,selectedAnsType,numberOfAns,(questionIDs!=null && questionIDs.length>=i)),1,++rowIndex);
			
		}
		
		stateOne.add(getAddQuestionFieldset(),1,++rowIndex);
		
	
//		TODO fix bugs and uncomment
//		SubmitButton saveButton = new SubmitButton(_iwrb.getLocalizedString("save","  Save  "),PRM_ACTION,String.valueOf(ACTION_SAVE));
//		setStyle(saveButton);
//		stateOne.add(saveButton,1,++rowIndex);
//		stateOne.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
//		
//		stateOne.add(Text.NON_BREAKING_SPACE,1,rowIndex);
		
		SubmitButton forwardButton = new SubmitButton(this._iwrb.getLocalizedString("forward","  Forward  "),PRM_GOTO_STATE,String.valueOf(STATE_TWO));
		setStyle(forwardButton);
		//stateOne.add(forwardButton,1,rowIndex);
		stateOne.add(forwardButton,1,++rowIndex);
		stateOne.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);

		
		return stateOne;
	}
	
	/**
	 * @param iwc
	 * @param stateOne
	 * @param rowIndex
	 * @return
	 */
	private FieldSet getSurveyInfoFieldset(IWContext iwc) {
		try {
			SurveyBusiness business = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
			
			
			//survey
			String[] pName = (String[]) this._prmValues.get(PRM_SURVEY_NAME);
			String[] pDesc = (String[]) this._prmValues.get(PRM_SURVEY_DESCRIPTION);
			String[] pStart = (String[]) this._prmValues.get(PRM_SURVEY_START_TIME);
			String[] pEnd = (String[]) this._prmValues.get(PRM_SURVEY_END_DATE);
			
			SurveyEntity survey = null;
			String sName = "";
			String sDesc = "";
			IWTimestamp start = IWTimestamp.RightNow();
			IWTimestamp end = null;
			if(this._surveyID != null){
				survey = business.getSurveyHome().findByPrimaryKey(this._surveyID);
			}

			if (pName == null || pName.length == 0 || pName[0].equals("") ) {
				if (survey != null) {
					sName = survey.getName();
				}
			} else {
				sName = pName[0];
			}
			if (pDesc == null || pDesc.length == 0 || pDesc[0].equals("") ) {
				if (survey != null) {
					sDesc = survey.getDescription();
				}
			} else {
				sDesc = pDesc[0];
			}
			
			if (pStart == null || pStart.length == 0 || pStart[0].equals("") ) {
				if (survey != null) {
					Timestamp tStamp = survey.getStartTime();
					if (tStamp != null) {
						start = new IWTimestamp(tStamp);
					}
				}
			}  else {
				start = new IWTimestamp(pStart[0]);
			}
			
			if (pEnd == null || pEnd.length == 0 || pEnd[0].equals("") ) {
				if (survey != null) {
					Timestamp tStamp = survey.getEndTime();
					if (tStamp != null) {
						end = new IWTimestamp(tStamp);
					}
				}
			}	 else {
				end = new IWTimestamp(pEnd[0]);
			}

			FieldSet fs = new FieldSet(this._iwrb.getLocalizedString("general_info", "General information"));
			Table table = new Table();
			fs.add(table);
			int row = 1;
			
			table.add(getLabel(this._iwrb.getLocalizedString("name", "Name")), 1, row);
			table.add(getAnswerTextInput(PRM_SURVEY_NAME, sName), 2, row++);

			table.add(getLabel(this._iwrb.getLocalizedString("description", "Description")), 1, row);
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			table.add(getAnswerTextArea(PRM_SURVEY_DESCRIPTION, sDesc, false), 2, row++);
			
			TimestampInput from = new TimestampInput(PRM_SURVEY_START_TIME, false);
			if (start != null) {
				from.setYearRange(start.getYear(), IWTimestamp.RightNow().getYear()+2);
				from.setDate(start.getDate());
				from.setHour(start.getHour());
				from.setMinute(start.getMinute());
			}
			setStyle(from);
			table.add(getLabel(this._iwrb.getLocalizedString("starts", "Starts")), 1, row);
			table.add(from, 2, row++);
			
			TimestampInput to = new TimestampInput(PRM_SURVEY_END_DATE, false);
			if (end != null) {
				to.setYearRange(end.getYear(), IWTimestamp.RightNow().getYear()+2);
				to.setDate(end.getDate());
				to.setHour(end.getHour());
				to.setMinute(end.getMinute());
			}
			setStyle(to);
			table.add(getLabel(this._iwrb.getLocalizedString("ends", "Ends")), 1, row);
			table.add(to, 2, row++);
			
			return fs;
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return new FieldSet();
	}

	private PresentationObject getStateTwo(IWContext iwc){
		Table stateTwo = new Table();
		int rowIndex = 0;
		

		stateTwo.add(getSurveyInfoFieldset(iwc), 1, ++rowIndex);
		
		
		String[] questions = (String[])this._prmValues.get(PRM_QUESTION);
		String[] answertypes = (String[])this._prmValues.get(PRM_ANSWERTYPE);
		String[] numberOfAnswers = (String[])this._prmValues.get(PRM_NUMBER_OF_ANSWERS);
		String[] questionIDs = (String[])this._prmValues.get(PRM_QUESTION_IDS);
		if(questions != null && questions.length != 0){
			this._numberOfQuestions =0;
			for (int i = 0; i < questions.length; i++) {
				String question = questions[i];
				if((questionIDs != null && questionIDs.length > i && this._deletedQuestion.contains(questionIDs[i]))){
					continue;
				} else if(question!=null && !"".equals(question)){
					++this._numberOfQuestions;
					char answertype = answertypes[i].charAt(0);
					int noAnswers = Integer.parseInt(numberOfAnswers[i]);
					stateTwo.add(getAnswerFieldset(this._numberOfQuestions,question,answertype,noAnswers,(questionIDs!=null && questionIDs.length>i)),1,++rowIndex);
				}
			}
		}
		
		
		
		//stateOne.add(getAddQuestionFieldset(),1,++rowIndex);
		
	
		SubmitButton backButton = new SubmitButton(this._iwrb.getLocalizedString("back","  Back  "),PRM_GOTO_STATE,String.valueOf(STATE_ONE));
		setStyle(backButton);
		stateTwo.add(backButton,1,++rowIndex);
		stateTwo.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
		
//		stateTwo.add(Text.NON_BREAKING_SPACE,1,rowIndex);
//		SubmitButton CancelButton = new SubmitButton(_iwrb.getLocalizedString("cancel","  Cancel  "),PRM_ACTION,String.valueOf(ACTION_CANCEL));
//		setStyle(CancelButton);
//		stateTwo.add(CancelButton,1,rowIndex);
		
		stateTwo.add(Text.NON_BREAKING_SPACE,1,rowIndex);
		
		SubmitButton saveButton = new SubmitButton(this._iwrb.getLocalizedString("save","  Save  "),PRM_ACTION,String.valueOf(ACTION_SAVE));
		setStyle(saveButton);
		stateTwo.add(saveButton,1,rowIndex);
		//stateOne.setRowAlignment(rowIndex,Table.HORIZONTAL_ALIGN_RIGHT);
		


		
		
		
		return stateTwo;
	}
	

	
	private PresentationObject getAddQuestionFieldset(){
		Table t = new Table(2,1);
		
		SubmitButton addButton = new SubmitButton(ADD_QUESTION_PRM,this._iwrb.getLocalizedString("add_questions_to_form","  Add  "));
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
	
	private PresentationObject getQuestionFieldset(int no, String question, String selectedAnsType,String numberOfAns, boolean removable){
		FieldSet fs = new FieldSet(this._iwrb.getLocalizedString("Question","Question")+" "+no);
		Table qt = new Table();
		qt.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		
		//qt.setBorder(1);
		
		//todo put id from db-table in hiddeninput for update
		
		qt.add(getLabel(this._iwrb.getLocalizedString("Question","Question")),1,1);

		qt.add(getQuestionTextArea(PRM_QUESTION,question),2,1);
		qt.add(getLabel(this._iwrb.getLocalizedString("Answer_type","Answer type")),1,2);
		qt.add(getAnswerTypeDropdownMenu(PRM_ANSWERTYPE,selectedAnsType),2,2);
		qt.add(getLabel(this._iwrb.getLocalizedString("Number_of_answers","Number of answers")),1,3);
		qt.add(getNumberOfAnswersDropdownMenu(PRM_NUMBER_OF_ANSWERS,numberOfAns),2,3);
		if(removable){
			qt.add(getLabel(this._iwrb.getLocalizedString("Delete","Delete")),1,4);
			qt.add(getDeleteQuestionCheckBox(no),2,4);
		}		
		
		fs.add(qt);
		return fs;
	}
	
	private PresentationObject getAnswerFieldset(int no, String questionText, char answerType, int numberOfAnswers, boolean removable){
		FieldSet fs = new FieldSet(this._iwrb.getLocalizedString("Question","Question")+" "+no);
		Table qt = new Table();
		qt.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		
		//qt.setBorder(1);
		
		//todo put id from db-table in hiddeninput for update
		
		qt.add(getLabel(this._iwrb.getLocalizedString("Question","Question")),1,1);
		PresentationObject question = getQuestionTextArea(PRM_QUESTION,questionText);
		qt.add(question,2,1);
		

		switch (answerType) {
			case ANSWERTYPE_SINGLE_CHOICE :		
				//break;
			case ANSWERTYPE_MULTI_CHOICE :
				qt.add(getLabel(this._iwrb.getLocalizedString("Answers","Answers")),1,3);
				qt.add(getLabel(this._iwrb.getLocalizedString("Answer_type","Answer type")),1,2);
				qt.add(getListAnswerTypeDropdownMenu(PRM_ANSWERTYPE,answerType),2,2);

				String[] answers = (String[])this._prmValues.get(PRM_ANSWER+no);
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
				qt.add(getLabel(this._iwrb.getLocalizedString("Answers","Answers")),1,2);
				qt.add(getAnswerTextArea("ans_ta",null,true),2,2);
				qt.add(new HiddenInput(PRM_ANSWERTYPE,String.valueOf(answerType)),2,2);

				break;
		}
		
		if(removable){
			qt.add(getLabel(this._iwrb.getLocalizedString("Delete","Delete")),1,qt.getRows()+1);
			qt.add(getDeleteQuestionCheckBox(no),2,qt.getRows());
		}
				
		
		
		fs.add(qt);
		if(answerType != ANSWERTYPE_TEXTAREA){
			//fs.add(getAddAnswerFieldSet(no));
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
	

	private PresentationObject getDeleteQuestionCheckBox(int  value) {
		CheckBox box = new CheckBox(PRM_DELETE_QUESTION);
		
		String[] questionIDs = (String[])this._prmValues.get(PRM_QUESTION_IDS);
		box.setValue(questionIDs[value-1]);
		
		if(questionIDs.length >= value && this._delQuestion.contains(questionIDs[value-1])){
			box.setChecked(true);
		}
		//setStyle(box);
		return box;
	}
	

	private PresentationObject getDeleteAnswerCheckBox(Object value) {
		CheckBox box = new CheckBox(PRM_DELETE_ANSWER);
		box.setValue(value.toString());
//		if(check != null){
//			box.setChecked(true);
//		}
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
		
		SubmitButton addButton = new SubmitButton(this._iwrb.getLocalizedString("add_answers_to_question","  Add  "),ADD_ANSWER_PRM,"_"+questionNumber);
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
		d.addMenuElement(ANSWERTYPE_SINGLE_CHOICE,this._iwrb.getLocalizedString("Radio_group","Radio group (single-choice)"));
		d.addMenuElement(ANSWERTYPE_MULTI_CHOICE,this._iwrb.getLocalizedString("Checkboxes","Checkboxes  (multi-choice)"));
		d.addMenuElement(ANSWERTYPE_TEXTAREA,this._iwrb.getLocalizedString("Textarea","Textarea"));
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
		d.addMenuElement(ANSWERTYPE_SINGLE_CHOICE,this._iwrb.getLocalizedString("Radio_group","Radio group (single-choice)"));
		d.addMenuElement(ANSWERTYPE_MULTI_CHOICE,this._iwrb.getLocalizedString("Checkboxes","Checkboxes  (multi-choice)"));
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
		help.setHelpTextBundle(HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(helpTextKey);
		help.setLinkText("help");
		return help;
	}
	
	
	
	private PresentationObject getMessageTextObject(String message, boolean highlight) {
		Text text = new Text(message);
		if(!highlight){
			if(this.messageTextStyle != null){
				text.setStyleAttribute(this.messageTextStyle);
			}
		} else {
			if(this.messageTextHighlightStyle != null){
				text.setStyleAttribute(this.messageTextHighlightStyle);
			}
		}
		return text;
	}
	
	public void setMessageTextStyle(String style) {
		this.messageTextStyle = style;
	}


	public void setMessageTextHighlightStyle(String style) {
		this.messageTextHighlightStyle = style;
	}



}
