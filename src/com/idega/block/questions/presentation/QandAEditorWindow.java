/*
 * Created on Dec 16, 2003
 *
 */
package com.idega.block.questions.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.questions.business.QuestionsService;
import com.idega.block.questions.data.Question;
import com.idega.block.questions.data.QuestionHome;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * QandAEditorWindow
 * @author aron 
 * @version 1.0
 */
public class QandAEditorWindow extends IWAdminWindow {
	
	public final static String PRM_QA_ID = QandAEditor.PRM_QA_ID;
	public final static String PRM_CATEGORY = QandAEditor.PRM_CATEGORY;
	
	
	public QandAEditorWindow(){
		super();
		setWidth(600);
		setHeight(550);
		setResizable(true);
		setScrollbar(true);
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		add(new QandAEditor());
		setTitle("Q&A");
		addTitle("Q&A");
	}
	
	public class QandAEditor extends Block {
		
		
		private final static String PRM_LOCALE_ID="qae_locid";
		public final static String PRM_QA_ID = "qa_id";
		private final static String PRM_Q_TITLE = "q_title";
		private final static String PRM_A_TITLE = "a_title";
		private final static String PRM_Q_BODY = "q_body";
		private final static String PRM_A_BODY = "a_body";
		private final static String PRM_SAVE = "qae_save";
		private final static String PRM_CLOSE = "qae_close";
		public final static String PRM_CATEGORY = "qae_cat";
		
		private IWResourceBundle iwrb;
		private IWBundle iwb;
		private String IW_BUNDLE_IDENTIFIER = "com.idega.block.questions";
		
		private Integer qaID = null;
		private Integer localeID = null;
		private Integer categoryID = null;
		
		private Question qaEntity = null;
		private LocalizedText question = null,answer = null;
		
		private String textStyle = "",interfaceStyle = "";
		
		
		/* (non-Javadoc)
		 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
		 */
		public void main(IWContext iwc) throws Exception {
			init(iwc);
			process(iwc);
			presentate(iwc);
		}
		
		private void init(IWContext iwc){
			iwb = getBundle(iwc);
			iwrb = getResourceBundle(iwc);
			if(iwc.isParameterSet(PRM_CATEGORY)){
				categoryID = Integer.valueOf(iwc.getParameter(PRM_CATEGORY));
			}
			if(iwc.isParameterSet(PRM_LOCALE_ID)){
				localeID = Integer.valueOf(iwc.getParameter(PRM_LOCALE_ID));
			}
			else{
				// default localeID fetched
				localeID = new Integer(ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale()));
			}
			if(iwc.isParameterSet(PRM_QA_ID)){
				qaID = Integer.valueOf(iwc.getParameter(PRM_QA_ID));
				try {
					qaEntity = getQuestionHome().findByPrimaryKey(qaID);
					initEntity(qaEntity);
				}
				catch (RemoteException e) {
					
				}
				catch (FinderException e) {
					
				}
			}
			
		}
		
		private void initEntity(Question qaentity)throws RemoteException{
			if(qaEntity!=null){
				if(qaEntity.getQuestionID()>0){
					ContentHelper helper = TextFinder.getContentHelper(qaEntity.getQuestionID(),localeID.intValue());
					if(helper!=null){
						question = helper.getLocalizedText();
					}
				}
				if(qaEntity.getAnswerID()>0){
					ContentHelper helper = TextFinder.getContentHelper(qaEntity.getAnswerID(),localeID.intValue());
					if(helper!=null){
						answer = helper.getLocalizedText();
					}
				}
				
			}
		}
		
		private void process(IWContext iwc){
			if(iwc.isParameterSet(PRM_CLOSE)){
				setParentToReload();
				close();
			}
			else if(iwc.isParameterSet(PRM_SAVE)){
				String qTitle = iwc.getParameter(PRM_Q_TITLE);
				String aTitle = iwc.getParameter(PRM_A_TITLE);
				String qBody = iwc.getParameter(PRM_Q_BODY);
				String aBody = iwc.getParameter(PRM_A_BODY);
				try{
					qaEntity = getQuestionService(iwc).storeQuestion(qaID,localeID,categoryID,new Integer(iwc.getCurrentUserId()),qTitle,qBody,aTitle,aBody);
					initEntity(qaEntity);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
		private void presentate(IWContext iwc){
			Table table = new Table();
			// locale choosing part
			table.add(formatText(iwrb.getLocalizedString("locale","Locale")),1,1);
			DropdownMenu localeSelect = ICLocalePresentation.getLocaleDropdownIdKeyed(PRM_LOCALE_ID);
			localeSelect.setToSubmit();
			if(localeID!=null)
				localeSelect.setSelectedElement(localeID.toString());
			table.add(localeSelect,2,1);
			// question part
			String titletext = iwrb.getLocalizedString("title","Title");
			String bodytext = iwrb.getLocalizedString("body","Body");
			table.mergeCells(1,2,2,2);
			table.setAlignment(1,2,Table.HORIZONTAL_ALIGN_CENTER);
			table.add(formatHeadline(iwrb.getLocalizedString("question","Question")),1,2);
			table.add(formatText(titletext),1,3);
			table.add(formatText(bodytext),1,4);
			table.setVerticalAlignment(1,4,Table.VERTICAL_ALIGN_TOP);
			table.mergeCells(1,5,2,5);
			table.setAlignment(1,5,Table.HORIZONTAL_ALIGN_CENTER);
			table.add(formatHeadline(iwrb.getLocalizedString("answer","Answer")),1,5);
			table.add(formatText(titletext),1,6);
			table.add(formatText(bodytext),1,7);
			table.setVerticalAlignment(1,7,Table.VERTICAL_ALIGN_TOP);
			TextInput questionTitle = new TextInput(PRM_Q_TITLE);
			questionTitle.setWidth("500");
			TextInput answerTitle = new TextInput(PRM_A_TITLE);
			answerTitle.setWidth("500");
			
			TextEditor questionBody = new TextEditor(PRM_Q_BODY,"");
			questionBody.setColumns(70);
			questionBody.setRows(10);
			TextEditor answerBody = new TextEditor(PRM_A_BODY,"");
			answerBody.setColumns(70);
			answerBody.setRows(10);
			
			table.add(questionTitle,2,3);
			table.add(questionBody,2,4);
			table.add(answerTitle,2,6);
			table.add(answerBody,2,7);
			
			if(question!=null){
				String headline,body;
				if((headline= question.getHeadline())!=null)
					questionTitle.setContent(headline);
				if((body=question.getBody())!=null)
					questionBody.setContent(body);
			}
			if(answer!=null){
				String headline,body;
				if((headline= answer.getHeadline())!=null)
					answerTitle.setContent(headline);
				if((body=answer.getBody())!=null)
					answerBody.setContent(body);
			}
			
			SubmitButton save = new SubmitButton(PRM_SAVE,iwrb.getLocalizedString("save","Save"));
			SubmitButton close = new SubmitButton(PRM_CLOSE,iwrb.getLocalizedString("close","Close"));
			Table buttonTable = new Table();
			buttonTable.setWidth(Table.HUNDRED_PERCENT);
			buttonTable.setWidth(1,Table.HUNDRED_PERCENT);
			buttonTable.add(save,2,1);
			buttonTable.add(close,3,1);
			table.setAlignment(2, 8, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(buttonTable,2,8);
			// answer part
			
			// save & close part
			Form form = new Form();
			form.maintainParameter(PRM_QA_ID);
			form.maintainParameter(PRM_CATEGORY);
			
			form.add(table);
			add(form);
		}
		
		private QuestionHome getQuestionHome() throws RemoteException{
			return (QuestionHome)IDOLookup.getHome(Question.class);
		}
		
		private QuestionsService getQuestionService(IWApplicationContext iwac) throws RemoteException{
			return (QuestionsService) IBOLookup.getServiceInstance(iwac,QuestionsService.class);
		}
		
		public void setTextStyle(String style){
			this.textStyle = style;
		}
		
		public void setInterfaceStyle(String style){
			this.interfaceStyle = style;
		}
	}

}
