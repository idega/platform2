package com.idega.block.questions.presentation;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.presentation.CategoryBlock;
import com.idega.block.questions.business.QuestionsService;
import com.idega.block.questions.data.Question;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.business.TextBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.presentation.TextChooser;
import com.idega.builder.business.BuilderLogic;
import com.idega.business.IBOLookup;
import com.idega.core.business.CategoryFinder;
import com.idega.core.data.ICCategory;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Anchor;
import com.idega.presentation.text.AnchorLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.text.TextFormat;
/**
 * 
 * <p>Company: idegaweb </p>
 * @author aron
 * 
 *
 */
public class QuestionsAndAnswers extends CategoryBlock {
	
	private IWResourceBundle iwrb;
	private IWBundle iwb,core,builder;
	private String IW_BUNDLE_IDENTIFIER = "com.idega.block.questions";
	private boolean isAdmin = false;
	private QuestionsService questionsService;
	private Locale currentLocale;
	private int row = 1;
	private int qaRow = 1;
	private String prmViewCategory = "qa_view_cat_id";
	private String valViewCategory = null;
	
	private boolean showAll = true;
	private boolean showOnlyOneCategory =false;
	private boolean showQuestionTitle = true;
	private boolean showQuestionBody = true;
	private boolean showAnswerTitle = true;
	private boolean showAnswerBody = true;
	private boolean showQuestionList = false;
	private boolean showQuestionListCount = true;
	
	private String questionPrefixText = "Q:";
	private String answerPrefixText = "A:";
	private Image questionPrefixImage = null;
	private Image answerPrefixImage = null;
	
	public final static String STYLENAME_Q_TITLE = "QuestionTitle";
	public final static String STYLENAME_Q_BODY = "QuestionBody";
	public final static String STYLENAME_A_TITLE = "AnswerTitle";
	public final static String STYLENAME_A_BODY = "AnswerBody";
	public final static String STYLENAME_Q_PREFIX = "QuestionPrefix";
	public final static String STYLENAME_A_PREFIX = "AnswerPrefix";
	public final static String STYLENAME_C_TITLE = "CategoryTitle";
	public final static String STYLENAME_Q_COUNT = "QuestionCount";
	
	public final static String DEFAULT_Q_TITLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	public final static String DEFAULT_Q_BODY = "font-weight:plain;";
	public final static String DEFAULT_A_TITLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	public final static String DEFAULT_A_BODY = "font-weight:plain;";
	public final static String DEFAULT_Q_PREFIX = "font-style:normal;color:#000000;font-size:13px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	public final static String DEFAULT_A_PREFIX = "font-style:normal;color:#000000;font-size:13px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";;
	public final static String DEFAULT_C_TITLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	public final static String DEFAULT_Q_COUNT = "font-weight:plain;";
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/**
	 * @see com.idega.block.presentation.CategoryBlock#getCategoryType()
	 */
	public String getCategoryType() {
		return "QA";
	}
	
	/**
	 * @see com.idega.block.presentation.CategoryBlock#getMultible()
	 */
	public boolean getMultible() {
		return true;
	}
	
	public void main(IWContext iwc)throws RemoteException{
		//debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		core = iwc.getApplication().getCoreBundle();
		builder =   iwc.getApplication().getBundle(BuilderLogic.IW_BUNDLE_IDENTIFIER);
		isAdmin = hasEditPermission();
		questionsService = (QuestionsService)IBOLookup.getServiceInstance(iwc,QuestionsService.class);
		
		currentLocale = iwc.getCurrentLocale();
		
		valViewCategory = iwc.getParameter(prmViewCategory);
		// form processing
		processForm(iwc);
	
		Table T = new Table();
		int row = 1;
		if(iwc.hasEditPermission(this)){
      		T.add(getAdminPart(iwc),1,row++);
		}
		Table QandATable = new Table();
		Table QATable =(Table) getQATable(iwc,QandATable);
		T.add(QATable,1,row++);
		if(showAll)
			T.add(QandATable,1,row);
		add(T);
	}
	
	public PresentationObject getAdminPart(IWContext iwc){
		Table T = new Table(3,1);
		T.setCellpadding(0);
	    T.setCellpadding(0);
	    T.setWidth("100%");
	    T.setWidth(2,"100%");
	    T.add(getCategoryLink(core.getImage("/shared/detach.gif")), 1, 1);
	    String helpTitle = iwrb.getLocalizedString("help_title","Q & A");
	    String helpText = iwrb.getLocalizedString("help_text","If the blank page icon appears you have to save changes with the save button, else changes are saved in the editor window ( when the open icon appears)");
	    HelpButton help = new HelpButton(helpTitle,helpText);
	    T.add(help,3,1);
			return T;	
	}
	
	 private Link getCategoryLink(Image image) {
	    Link L = getCategoryLink();
	    L.setImage(image);
	    return L;
	  }

	
	public PresentationObject getQATable(IWContext iwc,Table QandATable)throws RemoteException{
		Table QTable = new Table();
		Collection categories = null;
		
		if(showOnlyOneCategory && valViewCategory!=null){
			try {
				ICCategory viewCat = getCategoryHome().findByPrimaryKey(new Integer(valViewCategory));
				categories = new Vector(1);
				categories.add(viewCat);
			}
			catch (FinderException e) {
			}
		}
		else{
		 	categories = getCategories();
		}
		if(categories!=null){
			Iterator iter = categories.iterator();
			int row = 2;
			fillQuestionTree(iwc,iter,QTable,QandATable);
		}
		return QTable;
	}
	
	private void fillQuestionTree(IWContext iwc,Iterator iter,Table T,Table QandATable)throws RemoteException{
		 
		while(iter.hasNext()){
			ICCategory cat = (ICCategory) iter.next();
			boolean headerAdded = false;
			int headerRow = row;
			row++;
			if(cat.isLeaf()){
				if(!showOnlyOneCategory || (showOnlyOneCategory && cat.getPrimaryKey().toString().equals(valViewCategory))){
					Table questionsTable =(Table) getCategoryQuestions(iwc,cat,QandATable);
					if(showQuestionList)
						T.add(questionsTable,2,row++);
					if(isAdmin){
						Question nullQuestion=null;
						T.add(getQuestionForm(iwc,cat,nullQuestion),2,row++);
					}
				}
				if(showOnlyOneCategory){
					Link headerLink = new Link(getStyleText( cat.getName(),STYLENAME_C_TITLE));
					headerLink.addParameter(prmViewCategory,cat.getPrimaryKey().toString());
					T.add(headerLink,1,headerRow);
					headerAdded = true;
				}
			}		
			else{
				
				fillQuestionTree(iwc,cat.getChildren(),T,QandATable);
			}
			
			if(!headerAdded){
				AnchorLink anc = new AnchorLink(getStyleText(cat.getName(),STYLENAME_C_TITLE),"Cat"+cat.getPrimaryKey().toString());
				T.add(anc,1,headerRow);
			}
			row++;
		}
	}
	

	private PresentationObject getQuestionForm(IWContext iwc,ICCategory cat,Question question)throws RemoteException{
		Form F = new Form();
		Table T = new Table(5,1);
		String id = cat.getPrimaryKey().toString();
		T.add(getStyleText(questionPrefixText,STYLENAME_Q_TITLE),1,1);
		T.add(getStyleText(answerPrefixText,STYLENAME_A_TITLE),3,1);
		TextChooser quest = new TextChooser("quest"+id);
		int questID = -1,ansID=-1,entID = -1;
		if(question!=null){
			questID = question.getQuestionID();
			ansID = question.getAnswerID();
			entID = ((Integer) question.getPrimaryKey()).intValue();
		}
		if(questID<0)
			quest.setChooseImage(builder.getImage("toolbar_new.gif"));
		else
			quest.setSelectedText(questID);
		T.add(quest,2,1);
		TextChooser ans = new TextChooser("ans"+id);
		if(ansID<0)
			ans.setChooseImage(builder.getImage("toolbar_new.gif"));
		else
			ans.setSelectedText(ansID);
		T.add(ans,4,1);
		T.add(new HiddenInput("save_cat",id));
		T.add(new HiddenInput("ent_id"+id,String.valueOf(entID)));
		if(ansID <= 0 || questID <=0)
		T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save_quest"),5,1 );
		if(showOnlyOneCategory && valViewCategory!=null)
			T.add(new HiddenInput(prmViewCategory,valViewCategory));
		
		F.add(T);
		return F;
	}
	
	private void processForm(IWContext iwc)throws RemoteException{
		if(iwc.isParameterSet("save_cat")){
			int cat_id = Integer.parseInt(iwc.getParameter("save_cat"));
			String questionId = iwc.getParameter("quest"+cat_id);
			String answerId = iwc.getParameter("ans"+cat_id);
			String entityId = iwc.getParameter("ent_id"+cat_id);
			int q_id = -1, a_id = -1,ent_id = -1;
			try {
				q_id = Integer.parseInt(questionId);
			}catch (Exception e) {}
			try {
				a_id = Integer.parseInt(answerId);	
			}catch (Exception e) {}
			try {
				ent_id = Integer.parseInt(entityId);	
			}catch (Exception e) {}
			//System.err.println("Storing ("+ ent_id+","+q_id+","+a_id+","+cat_id+")");
			questionsService.storeQuestion(ent_id,q_id,a_id,cat_id);
		}
	}
	
	private PresentationObject getCategoryQuestions(IWContext iwc,ICCategory cat,Table QandATable)throws RemoteException{
		Table T = new Table();
		int row=1;
		Collection questions = new Vector();
		try{
			questions = questionsService.getQuestionHome().findAllByCategory(((Integer)cat.getPrimaryKey()).intValue());
		}catch(FinderException ex){}
		Iterator iter = questions.iterator();
		Question quest;
		AnchorLink anc;
		ContentHelper helper;
		int QuestCount = 1;
		if(showAll && !showOnlyOneCategory){
			createCategoryTitle(cat,QandATable);
		}
		while(iter.hasNext()){
			quest = (Question) iter.next();
			if(quest.getQuestionID() > 0){
				helper = TextFinder.getContentHelper(quest.getQuestionID(),currentLocale);
				if(showQuestionListCount)
				T.add(getStyleText((QuestCount++)+".",STYLENAME_Q_COUNT),1,row);
				
				if(showAll){
					anc = new AnchorLink(getStyleText(helper.getLocalizedText().getHeadline(),STYLENAME_Q_TITLE),"Q"+quest.getPrimaryKey().toString());
					if(showOnlyOneCategory && valViewCategory!=null)
						anc.addParameter(prmViewCategory,valViewCategory);
					T.add(anc,2,row);
					createQuestionsAndAnswers(iwc,helper,quest,cat,QandATable);
				}
				else{
					T.add(getStyleText(helper.getLocalizedText().getHeadline(),STYLENAME_Q_TITLE),2,row);
				}
				if(isAdmin)
					T.add(getQuestionForm(iwc,cat,quest),3,row);
				row++;
			}
		
		}
		return T;
	
	}
	
	public void createQuestionsAndAnswers(IWContext iwc,ContentHelper quest,Question question,ICCategory cat,Table QandATable)throws RemoteException{
		Table T = new Table();
		int row = 1;
		Anchor anc = new Anchor("Q"+question.getPrimaryKey().toString());
		T.add(anc,1,1);
		if(questionPrefixImage==null)
			T.add(getStyleText(questionPrefixText,STYLENAME_Q_PREFIX),1,row);
		else
			T.add(questionPrefixImage,1,row);
		if(showQuestionTitle && quest.getLocalizedText().getHeadline().length()>0)
			T.add(getStyleText(quest.getLocalizedText().getHeadline(),STYLENAME_Q_TITLE),2,row++);
		if(showQuestionBody){
			T.add(getStyleText(quest.getLocalizedText().getBody(),STYLENAME_Q_BODY),2,row);
		}
		row++;
		if(answerPrefixImage==null)
			T.add(getStyleText(answerPrefixText,STYLENAME_A_PREFIX),1,row);
		else
			T.add(answerPrefixImage,1,row);
		if(question.getAnswerID()>0){
			ContentHelper ans = TextFinder.getContentHelper(question.getAnswerID(),currentLocale);
			if(showQuestionTitle && ans.getLocalizedText().getHeadline().length()>0)
				T.add(getStyleText(ans.getLocalizedText().getHeadline(),STYLENAME_A_TITLE),2,row++);
			if(showQuestionBody){
				T.add(getStyleText(ans.getLocalizedText().getBody(),STYLENAME_A_BODY),2,row);
			}
		}
		T.setColumnVerticalAlignment(1,Table.VERTICAL_ALIGN_TOP);
		QandATable.add(T,1,qaRow++);
		if(isAdmin && !showQuestionList)
			QandATable.add(getQuestionForm(iwc,cat,question),1,qaRow++);
	}
	
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { 
			STYLENAME_Q_TITLE ,
			STYLENAME_Q_BODY ,
			STYLENAME_A_TITLE,
			STYLENAME_A_BODY ,
			STYLENAME_Q_PREFIX ,
			STYLENAME_A_PREFIX ,
			STYLENAME_C_TITLE ,
			STYLENAME_Q_COUNT
		
		};
		String[] styleValues = { 
			DEFAULT_Q_TITLE ,
			DEFAULT_Q_BODY,
			DEFAULT_A_TITLE,
			DEFAULT_A_BODY ,
			DEFAULT_Q_PREFIX ,
			DEFAULT_A_PREFIX ,
			DEFAULT_C_TITLE ,
			DEFAULT_Q_COUNT
			
	 	};

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}

	
	private void createCategoryTitle(ICCategory cat,Table QandATable)throws RemoteException{
		Anchor anc = new Anchor(getStyleText(cat.getName(),STYLENAME_C_TITLE),"Cat"+cat.getPrimaryKey().toString());
		QandATable.add(anc,1,qaRow++);
	}
	
	public void setShowAll(boolean showAll){
		this.showAll = showAll;
	}
	
	public void setShowOnlyOneCategory(boolean showOnlyOneCategory){
		this.showOnlyOneCategory = showOnlyOneCategory;
	}
	public void setShowQuestionTitle (boolean showQuestionTitle){
		this.showQuestionTitle = showQuestionTitle;
	}
	public void setShowQuestionBody(boolean showQuestionBody){
		this.showQuestionBody=showQuestionBody;
	}
	
	public void setShowAnswerTitle(boolean showAnswerTitle){
		this.showAnswerTitle = showAnswerTitle;
	}
	
	public void setShowAnswerBody(boolean showAnswerBody){
		this.showAnswerBody = showAnswerBody;
	}
	
	public void setShowQuestionList(boolean showQuestionList){
		this.showQuestionList = showQuestionList;
	}
	
	public void setQuestionPrefixText(String questionPrefixText){
		this.questionPrefixText = questionPrefixText;
	}
	
	public void setAnswerPrefixText(String answerPrefixText){
		this.answerPrefixText = answerPrefixText;
	}
		
	public void setQuestionPrefixImage(Image questionPrefixImage){
		this.questionPrefixImage = questionPrefixImage;
	}
	
	public void setAnswerPrefixImage(Image answerPrefixImage){
		this.answerPrefixImage= answerPrefixImage;
	}
	
	public synchronized Object clone() {
	    QuestionsAndAnswers obj = null;
	    try {
	      obj = (QuestionsAndAnswers)super.clone();
	
	      obj.answerPrefixImage = this.answerPrefixImage;
	      obj.questionPrefixImage = this.questionPrefixImage;
	
	    }
	    catch(Exception ex) {
	      ex.printStackTrace(System.err);
	    }
    	return obj;
  }
	

}
