package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.survey.business.SurveyBusiness;
import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyParticipant;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.block.survey.data.SurveyReply;
import com.idega.block.survey.data.SurveyReplyHome;
import com.idega.block.survey.data.SurveyStatus;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.FieldSet;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.Legend;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
import com.idega.util.poi.POIUtility;

/**
 * Title:		SurveyResult
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gimmi@idega.is">Grimur Jonsson</a><br>
 * @version		1.0
 */
public class SurveyResultEditor extends Block {

	final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.survey";
	
	public static String PARAMETER_SURVEY_ID = SurveyEditor.PRM_SURVEY_ID;
	private static String PARAMETER_STATUS_ID = "prmStId";
	private static String PARAMETER_NEW_STATUS = "prmNSt";
	private static String PARAMETER_CREATE_EXCEL = "prmCEx";
	private SurveyBusiness _sBusiness;
	private IWResourceBundle _iwrb;
	private IWBundle _iwb;
	private IWBundle _iwbSurvey;
	private Locale _locale;
	private ICLocale _icLocale;
	private SurveyEntity _survey;
	private SurveyStatus _status;
	private Collection _allStatuses;
	private Collection _questions;
	
	private String messageTextStyle;// = "font-weight: bold;";
	private String messageTextHighlightStyle ;//= "font-weight: bold;color: #FF0000;";
	
	private String style_submitbutton = "font-family:arial; font-size:8pt; color:#000000; text-align: center; border: 1 solid #000000;";
	
	public SurveyResultEditor() {
		super();
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		_sBusiness = (SurveyBusiness) IBOLookup.getServiceInstance(iwc,SurveyBusiness.class);
		_iwrb = getResourceBundle(iwc);
		_iwb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		_iwbSurvey = getBundle(iwc);
		_locale = iwc.getCurrentLocale();
		_icLocale = ICLocaleBusiness.getICLocale(_locale);
		
		String surveyID = iwc.getParameter(PARAMETER_SURVEY_ID);
		if (surveyID != null) {
			try {
				_survey = _sBusiness.getSurveyHome().findByPrimaryKey(new Integer(surveyID));
				_questions = _survey.getSurveyQuestions();
				if (iwc.isParameterSet(PARAMETER_STATUS_ID)) {
					_status = _sBusiness.getSurveyStatusHome().findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_STATUS_ID)));
				} else {
					_status = _sBusiness.getSurveyStatus(_survey);
				}
				_allStatuses = _sBusiness.getSurveyStatusHome().findAllBySurvey(_survey);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public void main(IWContext iwc) throws RemoteException {
		
		if ( _survey != null ) {
			Legend legend = new Legend(_survey.getName()+" - "+_iwrb.getLocalizedString("history", "History"));
			FieldSet fs = new FieldSet(legend);
			fs.setWidth("450");
			if (iwc.isParameterSet(PARAMETER_STATUS_ID)) {
				fs.add(displayQuestions());
			} else {
				if (iwc.isParameterSet(PARAMETER_NEW_STATUS)) {
					newStatus(iwc);
				} else if (iwc.isParameterSet(PARAMETER_CREATE_EXCEL)) {
					createFile();
				}
				fs.add(displayStatuses());
			}
			add(fs);
			
			Form myForm = new Form();
			Legend legend2 = new Legend(_survey.getName()+" - "+_iwrb.getLocalizedString("random_participants", "Randam participants"));
			FieldSet fs2 = new FieldSet(legend2);
			fs2.setWidth("450");
			fs2.add(displayParticipants(iwc));
			myForm.add(fs2);
			add(myForm);
			
			
		} else {
			add(getText(_iwrb.getLocalizedString("no_survey_defined","No survey defined")));
		}
		add(Text.BREAK);
		BackButton link = new BackButton("Back");
		add(link);
	}
	
	/**
	 * @return
	 */
	private PresentationObjectContainer displayParticipants(IWContext iwc) {
		Table table = new Table();
		
		String prmNumberOfParticipants = "su_num_of_p";
		String prmSubmit = "su_subm";
		
		IntegerInput numberOfParticipantsInput = new IntegerInput(prmNumberOfParticipants);
		numberOfParticipantsInput.setValue(1);
		
		SubmitButton submit = new SubmitButton(prmSubmit,_iwrb.getLocalizedString("submit","  Submit  "));
		submit.setStyleAttribute(style_submitbutton);
		
		add(getText(_iwrb.getLocalizedString("number_of_participants","Number of participants")));
		table.add(numberOfParticipantsInput,1,1);
		table.add(submit,1,1);
		
		table.add(new Parameter(PARAMETER_SURVEY_ID, _survey.getPrimaryKey().toString()));
		table.add(new Parameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_RESULTS));
		
		try {
			String  prm = iwc.getParameter(prmNumberOfParticipants);
			if(prm != null){
				Table pTable = new Table();
				int numberOfParticipants = Integer.parseInt(prm);
				Collection participants = _sBusiness.getSurveyParticipantHome().getRandomParticipants(_survey,numberOfParticipants,true);
				
				int row = 1;
				for (Iterator iter = participants.iterator();iter.hasNext();row++) {
					SurveyParticipant participant = (SurveyParticipant) iter.next();
					pTable.add(participant.getParticipantName(),1,row);
				}
												
				table.add(pTable,1,2);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		
		
		return table;
	}

	private void newStatus(IWContext iwc) throws RemoteException{
		try {
			SurveyStatus status = _sBusiness.getSurveyStatusHome().create();
			status.setSurvey(_survey);
			status.setIsModified(false);
			status.store();
			
			_status = _sBusiness.getSurveyStatus(_survey);
			_allStatuses = _sBusiness.getSurveyStatusHome().findAllBySurvey(_survey);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Table displayStatuses() {
		Table table = new Table();
		table.setBorder(0);
		int row = 1;
		if (_allStatuses != null && !_allStatuses.isEmpty()) {
			Iterator iter = _allStatuses.iterator();
			SurveyStatus status;
			IWTimestamp stamp;
			boolean modified;
			boolean warning = false;
			boolean isLatest = false;
			Link link;
			ICFile reportFile;
			String YES = _iwrb.getLocalizedString("yes", "Yes");
			String NO = _iwrb.getLocalizedString("no", "No");
			
			table.add(getHeader(_iwrb.getLocalizedString("last_saved_modification", "Last saved modification")), 1, row);
			table.add(getHeader(_iwrb.getLocalizedString("modified_since", "Modified since")), 2, row);
			table.add(getHeader(_iwrb.getLocalizedString("report", "Report")), 3, row);
			table.mergeCells(3, row, 4, row);
			
			while (iter.hasNext()) {
				status = (SurveyStatus) iter.next();
				++row;
				stamp = new IWTimestamp(status.getTimeOfStatus());
				modified = status.getIsModified();
				isLatest = status.getPrimaryKey().equals(_status.getPrimaryKey());
				table.add(getText(stamp.getLocaleDateAndTime(_locale)), 1, row);
				table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_CENTER);
				if (modified) {
					table.add(getText(YES), 2, row);
				} else {
					table.add(getText(NO), 2, row);
				}
				
				if (isLatest) {
					link = new Link("HTML");
					link.addParameter(PARAMETER_SURVEY_ID, _survey.getPrimaryKey().toString());
					link.addParameter(PARAMETER_STATUS_ID, status.getPrimaryKey().toString());
					link.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_RESULTS);
					table.add(link, 3, row);
					table.add(Text.NON_BREAKING_SPACE, 3, row);
				} 
				
				reportFile = status.getReportFile();
				if (reportFile != null) {
					link = new Link(getText(_iwrb.getLocalizedString("excel","Excel")));
					link.setFile(reportFile);
					table.add(link, 4, row);
					if (modified) {
						warning = true;
						table.add(getText("*"), 4, row);
					}
				} 
				if (isLatest) {
					link = new Link();
					if (reportFile != null) {
						link = new Link(getText(_iwrb.getLocalizedString("recreate","Re-create")));
					} else {
						link = new Link(getText(_iwrb.getLocalizedString("create","Create")));
					}
					link.addParameter(PARAMETER_SURVEY_ID, _survey.getPrimaryKey().toString());
					link.addParameter(PARAMETER_CREATE_EXCEL, "true");
					link.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_RESULTS);
					table.add(getText(Text.NON_BREAKING_SPACE), 4, row);
					table.add(link, 4, row);
				}
			}
			++row;
			table.mergeCells(1,row, 4, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			Link newStatus = new Link(getText(_iwrb.getLocalizedString("new_status","New status")));
			newStatus.addParameter(PARAMETER_SURVEY_ID, _survey.getPrimaryKey().toString());
			newStatus.addParameter(Survey.PRM_SWITCHTO_MODE,Survey.MODE_RESULTS);
			newStatus.addParameter(PARAMETER_NEW_STATUS, "true");
			table.add(newStatus, 1, row);
			
			if (warning) {
				++row;
				table.mergeCells(1,row, 4, row);
				table.add(getText("*"), 1, row);
				table.add(getText(_iwrb.getLocalizedString("excel_contains_old_data", "Excel contains old data")), 1, row);
			}
		} else {
			add(getText(_iwrb.getLocalizedString("no_status","No status")));
		}
		return table;
	}
	
	private Table displayQuestions() throws RemoteException {
		Table table = new Table();
		int row = 1;
		
		if (_questions != null && !_questions.isEmpty()) {
			try {
				Iterator iter = _questions.iterator();
				SurveyQuestion question;
				while (iter.hasNext()) {
					question = (SurveyQuestion) iter.next();
					row = displayQuestion(question, table, row);
				}
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} 
		} else {
			add(getText(_iwrb.getLocalizedString("no_questions_defined","No questions defined")));
		}
		
		return table;
		
	}
	
	private void createFile() throws RemoteException {
		Table table = displayQuestions();
		ICFile icFile = POIUtility.createICFileFromTable(table, "SurveyResults.xls", "SurveyResults");
		
		if (icFile != null) {
			_status.setReportFile(icFile);
			_status.store();
			try {
				_status = _sBusiness.getSurveyStatus(_survey);
				_allStatuses = _sBusiness.getSurveyStatusHome().findAllBySurvey(_survey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			add(getText(_iwrb.getLocalizedString("file_creation_failed","File creation failed")));
		}
	}
	
	private int displayQuestion(SurveyQuestion question, Table table, int row) throws RemoteException {
		try {
			Collection answers = _sBusiness.getAnswerHome().findQuestionsAnswer(question);
			String questionName = question.getQuestion(_icLocale);
			SurveyReplyHome repHome = (SurveyReplyHome) IDOLookup.getHome(SurveyReply.class);
			Collection replys = repHome.findByQuestion(question);
			int column = 1;
			int[] totals;
			table.add(questionName, column, row);
			boolean choiceAnswer = SurveyBusinessBean.ANSWERTYPE_TEXTAREA != question.getAnswerType();
			boolean isCheckBox = SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE == question.getAnswerType();
			
			Object[] answersIds = new Object[]{};
			if (answers != null && !answers.isEmpty()) {
				Iterator iter = answers.iterator();
				answersIds = new Object[answers.size()+2]; // 0 and 1 are not used
				SurveyAnswer answer;
				while (iter.hasNext()) {
					answer = (SurveyAnswer) iter.next();
					answersIds[++column] = answer.getPrimaryKey();
					table.add(answer.getAnswer(_icLocale), column, row);
					
				}
			}

			if (replys != null && !replys.isEmpty()) {
				Iterator iter = replys.iterator();
				SurveyReply reply;
				String lastParticipant = "";
				String participant = "";
				totals = new int[answersIds.length];
				while (iter.hasNext()) {
					reply = (SurveyReply) iter.next();
					participant = reply.getParticipantKey();
					if (isCheckBox) {
						if (participant == null || !participant.equals(lastParticipant)) {
							++row;
							lastParticipant = participant;
						} 
					} else {
						++row;
					}
					for (int i = 2; i < answersIds.length; i++) {
						if (answersIds[i].equals(reply.getSurveyAnswer().getPrimaryKey())) {
							if (choiceAnswer) {
								totals[i] += 1;
								table.add(getText("X"), i, row);
							} else {
								table.add(reply.getAnswer(), i, row);
							}
						}
					}
				}
				if (choiceAnswer) {
					++row;
					table.add(getText(_iwrb.getLocalizedString("totals", "Totals")), 1, row);
					for (int i = 2; i < answersIds.length; i++) {
						table.add(getText(Integer.toString( totals[i] )), i, row);
					}
				}
			}
			++row;
			
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return row;
	}

	private Text getText(String text) {
		return (Text) getMessageTextObject(text, false);
	}
	
	private Text getHeader(String text) {
		return (Text) getMessageTextObject(text, true);
	}
	
	private PresentationObject getMessageTextObject(String message, boolean highlight) {
		Text text = new Text(message);
		if(!highlight){
			if(messageTextStyle != null){
				text.setStyleAttribute(messageTextStyle);
			}
		} else {
			if(messageTextHighlightStyle != null){
				text.setStyleAttribute(messageTextHighlightStyle);
			}
		}
		return text;
	}
	
	public void setMessageTextStyle(String style) {
		messageTextStyle = style;
	}


	public void setMessageTextHighlightStyle(String style) {
		messageTextHighlightStyle = style;
	}


}
