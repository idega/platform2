/*
 * Created on 27.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.survey.business.SurveyBusiness;
import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
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
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.util.datastructures.QueueMap;

/**
 * Title:		Survey
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 * 
 * 
 * Survey does only utilise folders not categories in the FolderBlock system at this time
 */
public class Survey extends FolderBlock {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.survey";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwbSurvey;
	private Locale _iLocaleID;
	private IWTimestamp _date;
	
	private static final String PRM_SELECTION_PREFIX = "su_q_";
	private static final String PRM_ANSWER_IN_TEXT_AREA_PREFIX = "su_q_ta_";
	public final static String PRM_MAINTAIN_SUFFIX = "_mt";
	
	
	public static final String PRM_QUESTIONS = "su_questions";
	
	public static final String PRM_ACTION = "su_act";
	public static final String PRM_LAST_ACTION = "su_last_act";
	public static final int ACTION_NO_ACTION = 0;
	public static final int ACTION_SURVEYREPLY = 1;
	
	private int _action = ACTION_NO_ACTION;
	private int _lastAction = ACTION_NO_ACTION;
	
	private Vector prmVector = new Vector();
	private HashMap _prmValues = new HashMap();
	private QueueMap _reply = new QueueMap();
	
	private SurveyBusiness _sBusiness = null;
	private SurveyEntity _currentSurvey = null;
	
	private String style_question;
	private String style_answer;
//	private String style_checkbox;
//	private String style_radiobutton;
//	private String style_textbox;
//	private String style_textarea;
	private String style_submitbutton = "font-family:arial; font-size:8pt; color:#000000; text-align: center; border: 1 solid #000000;";
	private String style_form_element = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	
	public final static String MODE_EDIT = "edit";
	public static final String MODE_SURVEY = "survey";
	private String _mode = MODE_SURVEY;
	public final static String PRM_MODE = "su_mode";
	public final static String PRM_SWITCHTO_MODE = "su_swto_mode";
	


	/**
	 * 
	 */
	public Survey() {
		super();
		this.useLocalizedFolders(false);
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		_sBusiness = (SurveyBusiness)IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		_iwrb = getResourceBundle(iwc);
		_iwb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		_iwbSurvey = getBundle(iwc);
		_iLocaleID = iwc.getCurrentLocale();
		_date = new IWTimestamp();
		
		processParameters(iwc);
		
		if(MODE_SURVEY.equals(_mode)){
			initializeSurvey(iwc);
		}
	}
	
	private void initializeSurvey(IWContext iwc) throws IDOLookupException, RemoteException, FinderException{
		Collection surveys = _sBusiness.getSurveyHome().findActiveSurveys(this.getWorkingFolder().getEntity(),IWTimestamp.RightNow().getTimestamp());	
		Iterator surveysIter = surveys.iterator();
		while(surveysIter.hasNext()) {
			_currentSurvey = (SurveyEntity)surveysIter.next();
		}
	}

	/**
	 * @param iwc
	 */
	private void processParameters(IWContext iwc) {
		processActionPRM(iwc);
		processAnswerPRM(iwc);
		
		
		String mode = iwc.getParameter(PRM_MODE);
		String switchToMode = iwc.getParameter(PRM_SWITCHTO_MODE);
		if(switchToMode != null){
			_mode = switchToMode;
		} else if(mode!= null){
			_mode = mode;
		}

	}
	
	private void processAnswerPRM(IWContext iwc) {
		String[] questions = iwc.getParameterValues(PRM_QUESTIONS);	
		if(questions != null){
			for (int i = 0; i < questions.length; i++) {
				String[] answers = iwc.getParameterValues(PRM_SELECTION_PREFIX+questions[i]);
				if(answers != null){
					Vector ansList = new Vector();
					for (int j = 0; j < answers.length; j++) {
						Integer answerPK = Integer.decode(answers[j]);
						if(answerPK != null){
							Object[] answer = new Object[2];
							answer[0] = answerPK;
							String textAnswer = iwc.getParameter(PRM_ANSWER_IN_TEXT_AREA_PREFIX+answers[j]);
							if(textAnswer != null){
								answer[1] = textAnswer;
							}
							ansList.add(answer);
						}
					}
					Integer qPK = Integer.decode(questions[i]);
					if(qPK != null){
						_reply.put(qPK,ansList);
					}
				}

			}
		}
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

	
	public void main(IWContext iwc) throws Exception {

		
		if(_mode.equals(MODE_EDIT)){
			add(new SurveyEditor(this.getICObjectInstanceID()));
		} else {
			if(this.hasEditPermission()){
				add(getAdminPart());
			}
			
			if(_action == ACTION_SURVEYREPLY){
				add(_iwrb.getLocalizedString("survey_has_been_replied","Thank you for participating"));
				storeReply(iwc);
				
			} else {
				add(getSurveyPresentation(iwc));
			}
			
			
		}
	}
	
	

	
	
	/**
	 * @param iwc
	 */
	private void storeReply(IWContext iwc) throws FinderException, IDOLookupException, RemoteException, CreateException {
		
		Set questions = _reply.keySet();
		if(questions != null){
			String participantKey = StringHandler.getRandomStringNonAmbiguous(20);
			for (Iterator qIter = questions.iterator(); qIter.hasNext();) {
				Object questionPK = qIter.next();
				SurveyQuestion question = _sBusiness.getQuestionHome().findByPrimaryKey(questionPK);
				List answers = (List)_reply.get(questionPK);
				for (Iterator ansIter = answers.iterator(); ansIter.hasNext();) {
					Object[] answerPKAndText = (Object[])ansIter.next();
					SurveyAnswer answer = _sBusiness.getAnswerHome().findByPrimaryKey(answerPKAndText[0]);
					_sBusiness.createSurveyReply(_currentSurvey,question,participantKey,answer,(String)answerPKAndText[1]);
				}
			}	 	
		}
	}

	/**
	 * @param iwc
	 * @return
	 */
	private PresentationObject getSurveyPresentation(IWContext iwc) {
		Form myForm = new Form();
		
		if(_currentSurvey != null){
			Table surveyTable = new Table();
			ICLocale locale = ICLocaleBusiness.getICLocale(_iLocaleID);
			try {
				Collection questions = _currentSurvey.getSurveyQuestions();
				int questionNumber = 1; 
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion)iter.next();
					surveyTable.add(new HiddenInput(PRM_QUESTIONS,question.getPrimaryKey().toString()),1,getQuestionRowIndex(questionNumber));
					surveyTable.add(getQuestionLabel(questionNumber),1,getQuestionRowIndex(questionNumber));
					try {
						surveyTable.add(getQuestionTextObject(question.getQuestion(locale)),2,getQuestionRowIndex(questionNumber));
					} catch (IDOLookupException e1) {
						e1.printStackTrace();
					} catch (FinderException e1) {
						e1.printStackTrace();
					}
					
					
					surveyTable.add(getAnswerTable(question, locale),2,(surveyTable.getRows()+1));
					
				}
				
							
				if(surveyTable.getColumns()>=2){
					surveyTable.setColumnVerticalAlignment(1,Table.VERTICAL_ALIGN_TOP);
					surveyTable.setColumnVerticalAlignment(2,Table.VERTICAL_ALIGN_TOP);
					surveyTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);
					surveyTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_LEFT);
				}
				
				SubmitButton submit = new SubmitButton(_iwrb.getLocalizedString("submit","  Submit  "),PRM_ACTION,String.valueOf(ACTION_SURVEYREPLY));
				submit.setStyleAttribute(style_submitbutton);
				surveyTable.add(submit,2,getQuestionRowIndex(questionNumber+1));
				surveyTable.setAlignment(2,getQuestionRowIndex(questionNumber),Table.HORIZONTAL_ALIGN_RIGHT);
				
			} catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			myForm.add(surveyTable);		
		} else {
			add(getQuestionTextObject("No survey defined"));
		}
		
		return myForm;
	}
	
	
	/**
	 * @param iwc
	 * @return
	 */
	private Table getAnswerTable(SurveyQuestion question, ICLocale locale) {
		Table answerTable = new Table();

		try {
			Collection answers = _sBusiness.getAnswerHome().findQuestionsAnswer(question);
			if(question.getAnswerType()==SurveyBusinessBean.ANSWERTYPE_TEXTAREA){
				//hiddenInput
				//Textarea
				Iterator iter = answers.iterator();
				if(iter.hasNext()){
					SurveyAnswer answer = (SurveyAnswer)iter.next();
					answerTable.add(new HiddenInput(PRM_SELECTION_PREFIX+question.getPrimaryKey().toString(),answer.getPrimaryKey().toString()));
					answerTable.add(getAnswerTextArea(answer.getPrimaryKey()));
				}
			} else {
				int answerNumber = 1; 
				for (Iterator iter = answers.iterator(); iter.hasNext(); answerNumber++) {
					SurveyAnswer answer = (SurveyAnswer)iter.next();
					switch (question.getAnswerType()) {
						case SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE :
							answerTable.add(getRadioButton(question.getPrimaryKey(),answer.getPrimaryKey()),1,answerNumber);
							break;

						case SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE:
							answerTable.add(getCheckBox(question.getPrimaryKey(),answer.getPrimaryKey()),1,answerNumber);
							break;
					}
					
					try {
						//add the option that TextInput is added  
						answerTable.add(getAnswerTextObject(answer.getAnswer(locale)),2,answerNumber);
					} catch (IDOLookupException e1) {
						e1.printStackTrace();
					} catch (FinderException e1) {
						e1.printStackTrace();
					}
				}
								
				if(answerTable.getColumns()>=2){
					answerTable.setColumnVerticalAlignment(1,Table.VERTICAL_ALIGN_TOP);
					answerTable.setColumnVerticalAlignment(2,Table.VERTICAL_ALIGN_TOP);
					answerTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_CENTER);
					answerTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_LEFT);
				}
			}					
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	
		return answerTable;
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
	 * @return
	 */
	private PresentationObject getCheckBox(Object name, Object value) {
		CheckBox box = new CheckBox(PRM_SELECTION_PREFIX+name.toString(),value.toString());
		return box;
	}

	/**
	 * @return
	 */
	private PresentationObject getRadioButton(Object name, Object value) {
		RadioButton r = new RadioButton(PRM_SELECTION_PREFIX+name.toString(),value.toString());
		return r;
	}

	/**
	 * @return
	 */
	private PresentationObject getAnswerTextArea(Object name) {
		TextArea aTA = new TextArea(PRM_ANSWER_IN_TEXT_AREA_PREFIX+name);
		//aTA.setStyleAttribute(style_form_element);
		aTA.setColumns(50);
		aTA.setRows(8);
		return aTA;
	}

	private int getQuestionRowIndex(int questionNumber){
		return ((questionNumber*2)-1);
	}

	/**
	 * @param questionNumber
	 * @return
	 */
	private PresentationObject getQuestionLabel(int questionNumber) {
		Text text = new Text(questionNumber+".");
		return text;
	}

	/**
	 * @param question
	 * @return
	 */
	private PresentationObject getQuestionTextObject(String question) {
		Text text = new Text(question);
		text.setBold();
		return text;
	}
	


	/**
	 * @param answer
	 * @return
	 */
	private PresentationObject getAnswerTextObject(String answer) {
		Text text = new Text(answer);
		return text;
	}

	private Table getAdminPart() {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		Image createImage = _iwb.getImage("shared/create.gif");
		Link createLink = new Link(createImage);
		createLink.addParameter(PRM_SWITCHTO_MODE,MODE_EDIT);
		
		table.add(createLink);
		
		if(_currentSurvey != null){
			Image editImage = _iwb.getImage("shared/edit.gif");
			Link adminLink = new Link(editImage);
			adminLink.addParameter(PRM_SWITCHTO_MODE,MODE_EDIT);
			adminLink.addParameter(SurveyEditor.PRM_SURVEY_ID,_currentSurvey.getPrimaryKey().toString());
			table.add(adminLink);
		}

		return table;
	}
	
	public synchronized Object clone(){
		Survey clone = (Survey)super.clone();
		clone._reply = new QueueMap();	
		clone._prmValues = new HashMap();
		clone.prmVector = new Vector();
		return clone;
	}


}
