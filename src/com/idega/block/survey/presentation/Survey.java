/*
 * Created on 27.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

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
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.TextArea;
import com.idega.util.IWTimestamp;

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
	
	private static final String PRM_SELECTION_SUFFIX = "su_q_";
	
	private SurveyBusiness _sBusiness = null;
	private SurveyEntity _currentSurvey = null;
	
	private String style_question;
	private String style_answer;
//	private String style_checkbox;
//	private String style_radiobutton;
//	private String style_textbox;
//	private String style_textarea;
//	private String style_submitbutton;
	private String style_form_element;
	
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
		
		String mode = iwc.getParameter(PRM_MODE);
		String switchToMode = iwc.getParameter(PRM_SWITCHTO_MODE);
		if(switchToMode != null){
			_mode = switchToMode;
		} else if(mode!= null){
			_mode = mode;
		}

	}
	
	public void main(IWContext iwc) throws Exception {

		
		if(_mode.equals(MODE_EDIT)){
			add(new SurveyEditor(this.getICObjectInstanceID()));
		} else {
			
			if(this.hasEditPermission()){
				add(getAdminPart());
			}
			
			add(getServeyPresentation(iwc));
			
			
		}

		
		
	}
	
	

	
	
	/**
	 * @param iwc
	 * @return
	 */
	private PresentationObject getServeyPresentation(IWContext iwc) {
		Form myForm = new Form();
		
		if(_currentSurvey != null){
			Table surveyTable = new Table();
			ICLocale locale = ICLocaleBusiness.getICLocale(_iLocaleID);
			try {
				Collection questions = _currentSurvey.getSurveyQuestions();
				int questionNumber = 1; 
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion)iter.next();
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
				answerTable.add(getAnswerTextArea());
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
	 * @return
	 */
	private PresentationObject getCheckBox(Object name, Object value) {
		CheckBox box = new CheckBox(PRM_SELECTION_SUFFIX+name.toString(),value.toString());
		return box;
	}

	/**
	 * @return
	 */
	private PresentationObject getRadioButton(Object name, Object value) {
		RadioButton r = new RadioButton(PRM_SELECTION_SUFFIX+name.toString(),value.toString());
		return r;
	}

	/**
	 * @return
	 */
	private PresentationObject getAnswerTextArea() {
		TextArea aTA = new TextArea();
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

	private Link getAdminPart() {
		Image editImage = _iwb.getImage("shared/edit.gif");
		Link adminLink = new Link(editImage);
		adminLink.addParameter(PRM_SWITCHTO_MODE,MODE_EDIT);

		return adminLink;
	}
	
	public synchronized Object clone(){
		//TMP		
		return (Survey)super.clone();
	}


}
