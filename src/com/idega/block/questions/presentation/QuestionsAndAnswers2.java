package com.idega.block.questions.presentation;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.questions.business.QAndALayoutHandler;
import com.idega.block.questions.business.QuestionsService;
import com.idega.block.questions.data.Question;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.business.TextFinder;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Anchor;
import com.idega.presentation.text.AnchorLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/**
 * 
 * 
 *
 */
public class QuestionsAndAnswers2 extends CategoryBlock {
	
	private IWResourceBundle iwrb;
	private IWBundle iwb,core;
	private String IW_BUNDLE_IDENTIFIER = "com.idega.block.questions";
	private boolean isAdmin = false;
	private QuestionsService questionsService;
	private Locale currentLocale;
	private String prmViewCategory = "qa_view_cat_id";
	private String valViewCategory = null;
	
	private boolean showAll = true;
	private boolean showAllCategories =true;
	private boolean showQuestionTitle = true;
	private boolean showQuestionBody = true;
	private boolean showAnswerTitle = true;
	private boolean showAnswerBody = true;
	private boolean showQuestionList = true;
	private boolean showQuestionListCount = true;
	private boolean showDeletedQuestions = true;
	private boolean showDeleteButton = true;
	private boolean showMoveButtons = true;
	private boolean showHomeButton = true;
	
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
	
	private int layout = QAndALayoutHandler.DEFAULT_LAYOUT;
	
	
	private final static String DEFAULT_MAIN_STYLE_CLASS = "questions_and_answers";
	private String mainStyleClass = DEFAULT_MAIN_STYLE_CLASS;
	
	
	public QuestionsAndAnswers2(){
		setAutoCreate(false);
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/**
	 * @see com.idega.block.category.presentation.CategoryBlock#getCategoryType()
	 */
	public String getCategoryType() {
		return "QA2";
	}
	
	/**
	 * @see com.idega.block.category.presentation.CategoryBlock#getMultible()
	 */
	public boolean getMultible() {
		return true;
	}
	
	public void main(IWContext iwc)throws RemoteException{
		//debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		core = iwc.getIWMainApplication().getCoreBundle();
		isAdmin = iwc.hasEditPermission(this);
		
		questionsService = (QuestionsService)IBOLookup.getServiceInstance(iwc,QuestionsService.class);
		currentLocale = iwc.getCurrentLocale();		
		
		if(layout == QAndALayoutHandler.SINGLE_RANDOM_LAYOUT){
		    if(isAdmin){
		        
	      		add(getCategoryAdminPart(iwc));
	      		add(Text.getBreak());
		    }
		    ///add(getRandomQAndA(iwc)); /// XXX what with this???
		    
		} else {
			valViewCategory = iwc.getParameter(prmViewCategory);
			
			processForm(iwc); // form processing
		
			Layer mainLayer = createLayerWithStyleClass(this.getMainStyleClass());	
			
			mainLayer.getChildren().add(getCategoryAdminPart(iwc));	 //admin part to manage categories            
            mainLayer.getChildren().add(getQuestionsAdminPart(iwc)); //admin part for questions            
			mainLayer.getChildren().add(getQuestionsListPart(iwc));  //list of questions and their answers
			
			add(mainLayer);
		}
	}
	
	public PresentationObject getCategoryAdminPart(IWContext iwc){
		Layer layer = createLayerWithStyleClass("category_admin");
        
        Link link = getCategoryLink();
        link.setImage(core.getImage("/shared/detach.gif")); 	
		link.setStyleClass("category_management"); 
		layer.getChildren().add(link); 
		
		String helpTitle = iwrb.getLocalizedString("help_title", "Q & A");
		String helpText = iwrb.getLocalizedString("help_text", "If the blank page icon appears you have to save changes with the save button, else changes are saved in the editor window (when the open icon appears)");
	    HelpButton help = new HelpButton(helpTitle, helpText);	
	    help.setStyleClass("category_help"); 
		layer.getChildren().add(help); 
		
		layer.getChildren().add(createLayerWithStyleClass("clearer")); 
		
		return layer;
	}	
	
	private DropdownMenu getInvalidQuestions(String name, int categoryId)throws RemoteException{
		DropdownMenu drop = new DropdownMenu(name);
        drop.addMenuElementFirst("-1", iwrb.getLocalizedString(
                "deleted_questions", "Deleted questions"));
        try {
            Collection questions = questionsService.getQuestionHome()
                    .findAllInvalidByCategory(categoryId);
            Iterator iter = questions.iterator();
            while (iter.hasNext()) {
                Question quest = (Question) iter.next();
                ContentHelper helper = TextFinder.getContentHelper(quest
                        .getQuestionID(), currentLocale);
                if (helper.getLocalizedText() != null) {
                    String headline = helper.getLocalizedText().getHeadline();
                    if (headline.length() > 20)
                        headline = headline.substring(0, 20) + "...";
                    drop.addMenuElement(quest.getPrimaryKey().toString(),
                            headline);
                }
            }

        } catch (FinderException fex) {
            throw new RemoteException(fex.getMessage());
        }
        return drop;
	}
	
	private void processForm(IWContext iwc)throws RemoteException{
		
		int cat_id = -1;
		if (iwc.isParameterSet("save_cat"))
			cat_id = Integer.parseInt(iwc.getParameter("save_cat"));
		String entityId = iwc.getParameter("ent_id");

		int ent_id = -1;
		try {
			if (entityId != null)
				ent_id = Integer.parseInt(entityId);
		} catch (Exception e) {
		}

		if (ent_id > 0 && iwc.isParameterSet("trash_quest")) {																
			questionsService.invalidateQuestion(ent_id);
			
		} else if (ent_id > 0 && iwc.isParameterSet("delete_quest")) {
			questionsService.removeQuestion(ent_id);
			
		} else if (iwc.isParameterSet("validate_quest")) {
			int inv_quest_id = Integer.parseInt( iwc.getParameter( "inv_quest" + cat_id ) );
			if (inv_quest_id > 0)
				questionsService.validateQuestion(inv_quest_id);
			
		} else if (ent_id > 0 && iwc.isParameterSet("move_up")) {
			int swap_quest_id = Integer.parseInt(iwc
					.getParameter("swap_up_quest_id" + entityId));
			if (swap_quest_id > 0)
				questionsService.swapSequences(ent_id, swap_quest_id);
			
		} else if (ent_id > 0 && iwc.isParameterSet("move_down")) {			
			int swap_quest_id = Integer.parseInt(iwc
					.getParameter("swap_down_quest_id" + entityId));
			
			if (swap_quest_id > 0)
				questionsService.swapSequences(ent_id, swap_quest_id);
			
		}
		
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

	
	public void setShowAll(boolean showAll){
		this.showAll = showAll;
	}
	
	public void setShowOnlyOneCategory(boolean showOnlyOneCategory){
		this.showAllCategories = !showOnlyOneCategory;
	}
	
	public void setShowAllCategories(boolean showAllCategories){
		this.showAllCategories = showAllCategories;
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
	
	public void setShowDeleteButton(boolean showDeleteButton){
		this.showDeleteButton = showDeleteButton;
	}
	
	public void setShowMoveButtons(boolean showMoveButtons){
		this.showMoveButtons = showMoveButtons;
	}
	
	public void setShowHomeButton(boolean showHomeButton){
		this.showHomeButton = showHomeButton;
	}
	
	public void setShowDeletedQuestions(boolean showDeletedQuestions){
		this.showDeletedQuestions = showDeletedQuestions;
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
        QuestionsAndAnswers2 obj = null;
        try {
            obj = (QuestionsAndAnswers2) super.clone();

            obj.answerPrefixImage = this.answerPrefixImage;
            obj.questionPrefixImage = this.questionPrefixImage;

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return obj;
    }
	
    /**
     * @param layout The layout to set.
     */
    public void setLayout(int layout) {
        this.layout = layout;
    }

	public String getMainStyleClass() {
		return mainStyleClass;
	}

	public void setMainStyleClass(String mainStyleClass) {
		this.mainStyleClass = mainStyleClass;
	}
	
	/** 
	 * Helper method that returns Layer object with class attribute defined in styleClass param
	 * 
	 * @param styleClass
	 * @return
	 */
	private Layer createLayerWithStyleClass(String styleClass) {
		Layer l = new Layer();
		l.setStyleClass(styleClass);
		return l;
	}
    
    
//// admin part for questions ////////////////////////////////////////////////////////////////////////
    private Layer getQuestionsAdminPart(IWContext iwc) throws RemoteException{
        Layer l = createLayerWithStyleClass("questions_admin");   
        
        Collection categories = null;
        categories = getCategories();
        
        if ( categories != null && !categories.isEmpty() ){
            Iterator iter = categories.iterator();
            walkAPCategoryTree(iwc, iter, l);            
        } else { // no categories exist, so show an info message            
            Paragraph p = new Paragraph();
            p.setStyleClass("info"); 
            p.getChildren().add(new Text(iwrb.getLocalizedString("no_category", "Please create a category")));
            l.getChildren().add(p);           
        }        
        
        return l;
    } 
    
    /**
     * walks through category tree in recursive way and displays categories and their questions
     * 
     * @param iwc
     * @param iter
     * @param poc
     * @throws RemoteException
     */
    private void walkAPCategoryTree(IWContext iwc, Iterator iter,
            PresentationObjectContainer poc) throws RemoteException {
        while (iter.hasNext()) {
            ICCategory cat = (ICCategory) iter.next();
            Integer catId = (Integer) cat.getPrimaryKey();

            // create and add a category header...
            getAPCategory(poc, cat);

            if (cat.isLeaf()) {
                // get and add all category questions
                poc.getChildren().add(getAPCategoryQuestions(iwc, cat));
                
                //te jāpievieno jauna jautājuma ieviešanas / jautājumu atjaunošanas forma
                poc.getChildren().add(getAPCategoryQestionForm(catId));
                
            } else {
                // go deeper in the tree in recursive mode
                walkAPCategoryTree(iwc, cat.getChildrenIterator(), poc);
            }
        }
    }    
    
    /**
     * returns category title
     */
    private void getAPCategory(PresentationObjectContainer poc, ICCategory cat) {
        String catPK = ((Integer) cat.getPrimaryKey()).toString();
        
        Layer layer = createLayerWithStyleClass("category");        
        AnchorLink link = new AnchorLink(new Text(cat.getName(currentLocale)),
                "cat" + catPK);
        layer.getChildren().add(link);        
        Anchor anchor = new Anchor("ap_cat" + catPK);        
        layer.getChildren().add(anchor);        
        
        poc.getChildren().add(layer);
    }
    
   
    /**
     * returns questions, that belong to given category
     */
    private PresentationObjectContainer getAPCategoryQuestions(IWContext iwc,
            ICCategory cat) throws RemoteException {
        PresentationObjectContainer poc = new PresentationObjectContainer();

        Collection questions = new Vector();
        Integer catId = (Integer) cat.getPrimaryKey();
        try {
            questions = questionsService.getQuestionHome().findAllByCategory(
                    catId.intValue());
        } catch (FinderException ex) {
        }

        Question quest = null, previous  = null, latter = null;
        
        ArrayList list = new ArrayList(questions);
        for (int i = 0; i < list.size(); i++) {
            quest = (Question) list.get(i);
            
            if (i > 0)  {
                previous = (Question) list.get(i - 1);
            }
            if ((i + 1) < list.size()) {
                latter = (Question) list.get(i + 1);
            } else {
                latter = null;
            }
            
            poc.getChildren().add(getAPQuestionItem(iwc, quest, i + 1, previous, latter));  
        }

        return poc;
    }     
    
    /**
     * creates info for one question
     */
    private Layer getAPQuestionItem(IWContext iwc, Question question, int number, Question previous, Question latter) throws RemoteException {
        // gather data
        ContentHelper helper = TextFinder.getContentHelper(question
                .getQuestionID(), currentLocale);
        String qHeadline = helper.getLocalizedText() != null ? helper
                .getLocalizedText().getHeadline() : "";
                
        //create(encode) presentation
        Layer item = createLayerWithStyleClass("item");
        
        Anchor anchor = new Anchor("ap_q" + question.getPrimaryKey());
        item.getChildren().add(anchor);   

        //question number
        Paragraph numberP = new Paragraph();
        numberP.setStyleClass("number");
        numberP.getChildren().add(new Text(Integer.toString(number) + "."));
        item.getChildren().add(numberP);
        
        //question title
        Paragraph titleP = new Paragraph();
        titleP.setStyleClass("title");      
        AnchorLink l = new AnchorLink(new Text(qHeadline), "q" + question.getPrimaryKey());
        titleP.getChildren().add(l);             
        
        item.getChildren().add(titleP);
        
        item.getChildren().add(getAPQuestionForm(iwc, question, previous, latter));  
        
        Paragraph clearer = new Paragraph();
        clearer.setStyleClass("clearer");
        item.getChildren().add(clearer);
        
        return item;
    }
    
    /**
     * returns invalidate / delete / move up / move down form for question 
     *      
     * @param iwc
     * @param question
     * @param previous
     * @param latter
     * @return
     */
    private PresentationObject getAPQuestionForm(IWContext iwc, Question question, Question previous, Question latter) {
        Paragraph p = new Paragraph();
        p.setStyleClass("question_form");
        
        Integer categoryId = question.getCategoryId();
        Integer questionId = (Integer) question.getPrimaryKey();
        
        //edit link
        Link editLink = new Link();
        editLink.setStyleClass("edit");
        editLink.setWindowToOpen(QandAEditorWindow.class);
        editLink.addParameter(QandAEditorWindow.PRM_CATEGORY, categoryId.toString());   
        editLink.setImage(iwb.getImage("open.gif", iwrb.getLocalizedString("button_edit_question", "Edit question")));
        editLink.addParameter(QandAEditorWindow.PRM_QA_ID, questionId.toString());   
        p.getChildren().add(editLink);
        
        //trash
        Link trash = new Link(iwb.getImage("trashcan_empty.gif", iwrb.getLocalizedString("button_invalidate","Trashcan")));
        trash.setStyleClass("trash");
        trash.addParameter("ent_id", questionId.toString());
        trash.addParameter("trash_quest", "true");
        p.getChildren().add(trash);
        
        //delete
        Link delete = new Link(iwb.getImage("delete.gif",iwrb.getLocalizedString("button_remove","Remove from list")));
        delete.setStyleClass("delete");
        delete.addParameter("ent_id", questionId.toString());
        delete.addParameter("delete_quest","true"); 
        p.getChildren().add(delete);
        
        //up
        if(previous != null){
            Link up = new Link(iwb.getImage("up.gif",iwrb.getLocalizedString("button_up","Move up")));
            up.setStyleClass("up");
            up.addParameter("move_up","true");
            up.addParameter("ent_id", questionId.toString());
            up.addParameter("swap_up_quest_id" + questionId, previous.getPrimaryKey().toString());
            p.getChildren().add(up);
        }        
        
        //down
        if(latter != null){
            Link down = new Link(iwb.getImage("down.gif", iwrb.getLocalizedString("button_down","Move down")));
            down.setStyleClass("down");
            down.addParameter("move_down","true");
            down.addParameter("ent_id", questionId.toString());
            down.addParameter("swap_down_quest_id" + questionId, latter.getPrimaryKey().toString());
            p.getChildren().add(down);
        }        
        
        return p;
    }
    
     
    /** 
     * generates question creation and revalidation form for given category
     */
    private PresentationObject getAPCategoryQestionForm(Integer categoryId) throws RemoteException { 
        Layer l = new Layer(); 
        l.setStyleClass("new_and_deleted_questions");        
        
        Form form = new Form();
        
        form.add(new HiddenInput("save_cat",categoryId.toString()));
        
        //link to create new question
        Link newQandA = new Link();
        newQandA.setStyleClass("new");
        newQandA.setWindowToOpen(QandAEditorWindow.class);
        newQandA.addParameter(QandAEditorWindow.PRM_CATEGORY,categoryId.toString());
        newQandA.setImage(iwb.getImage("new.gif",iwrb.getLocalizedString("button_create_question","Create question")));
        form.add(newQandA);
        
        //meny containing invalidated questions
        DropdownMenu deletedQuestions = getInvalidQuestions("inv_quest" + categoryId.toString(), categoryId.intValue());
        deletedQuestions.setStyleClass("deleted_questions");
        form.add(deletedQuestions);
        
        //button to revalidate invidated question
        SubmitButton sb = new SubmitButton(iwb.getImage("validate.gif",iwrb.getLocalizedString("button_validate", "Validate  selected")),
                 "validate_quest");
        sb.setStyleClass("validate");
        form.add(sb);
        
        l.getChildren().add(form);
        
        Paragraph clearer = new Paragraph();
        clearer.setStyleClass("clearer");
        l.getChildren().add(clearer);
        
        return l;
    }
    
    
    
    
//// list of questions and answers ////////////////////////////////////////////////////////////////////////////////
    /**
     * returns div, which contains list of questions and their answers
     */
    private Layer getQuestionsListPart(IWContext iwc) throws RemoteException{
        Layer l = createLayerWithStyleClass("questions_list"); 
        
        Collection categories = null;
        categories = getCategories();
        
        if ( categories != null && !categories.isEmpty() ){
            Iterator iter = categories.iterator();
            walkQLCategoryTree(iwc, iter, l);  
        }
        
        return l;
    }   
    
    /**
     * walks through category tree in recursive manner
     */        
    private void walkQLCategoryTree(IWContext iwc, Iterator iter,
            PresentationObjectContainer poc) throws RemoteException {
        while (iter.hasNext()) {
            ICCategory cat = (ICCategory) iter.next();           

            // create and add a category header...
            getQLCategoryItem(poc, cat);

            if (cat.isLeaf()) {
                // get and add all category questions
                poc.getChildren().add(getQLCategoryQuestions(iwc, cat));
            } else {
                // go deeper in the tree in recursive mode
                walkQLCategoryTree(iwc, cat.getChildrenIterator(), poc);
            }
        }
    }
    
    /**
     * return questions that belong to given category
     */
    private PresentationObjectContainer getQLCategoryQuestions(IWContext iwc,
            ICCategory cat) throws RemoteException {
        PresentationObjectContainer poc = new PresentationObjectContainer();

        Collection questions = new Vector();
        Integer catId = (Integer) cat.getPrimaryKey();
        try {
            questions = questionsService.getQuestionHome().findAllByCategory(
                    catId.intValue());
        } catch (FinderException ex) {
        }

        Question quest;

        ArrayList list = new ArrayList(questions);
        for (int i = 0; i < list.size(); i++) {
            quest = (Question) list.get(i);
            poc.getChildren().add(getQLQuestionItem(quest));
        }

        return poc;
    }    
    
    /**
     * returns category headline
     */
    private void getQLCategoryItem(PresentationObjectContainer poc, ICCategory cat) {
        String catPK = ((Integer) cat.getPrimaryKey()).toString();
        
        Layer layer = createLayerWithStyleClass("category");  
        
        AnchorLink link = new AnchorLink(new Text(cat.getName(currentLocale)),
                "ap_cat" + catPK);
        layer.getChildren().add(link);        
        Anchor anchor = new Anchor("cat" + catPK);        
        layer.getChildren().add(anchor);      
        
        poc.getChildren().add(layer);
    }
    
    /**
     * returns one question and it's answer
     */    
    private Layer getQLQuestionItem(Question question) throws RemoteException {
        // gather data
        ContentHelper helper = TextFinder.getContentHelper(question
                .getQuestionID(), currentLocale);
        String qHeadline = helper.getLocalizedText() != null ? helper
                .getLocalizedText().getHeadline() : "";
        String qBody = helper.getLocalizedText() != null ? helper
                .getLocalizedText().getBody() : "";

        //create(encode) presentation
        Layer item = createLayerWithStyleClass("item"); 
        
        //question
        Layer q = createLayerWithStyleClass("question");  
        
        //anchor so we can get here from list in q admin part        
        item.getChildren().add(
                new Anchor("q" + question.getPrimaryKey().toString()));
        
        Paragraph qPrefix = new Paragraph();
        qPrefix.setStyleClass("prefix"); 
        qPrefix.getChildren().add(new Text(questionPrefixText));
        q.getChildren().add(qPrefix);

        Paragraph qHeadlineP = new Paragraph();
        qHeadlineP.setStyleClass("title");
        qHeadlineP.getChildren().add(new Text(qHeadline));
        q.getChildren().add(qHeadlineP);

        Paragraph qBodyP = new Paragraph();
        qBodyP.setStyleClass("body");
        qBodyP.getChildren().add(new Text(qBody));
        q.getChildren().add(qBodyP);

        item.getChildren().add(q);

        //answer
        if (question.getAnswerID() > 0) {
            // gather data
            helper = TextFinder.getContentHelper(question.getAnswerID(),
                    currentLocale);
            String answerHeadline = helper.getLocalizedText() != null ? helper
                    .getLocalizedText().getHeadline() : "";
            String answerBody = helper.getLocalizedText() != null ? helper
                    .getLocalizedText().getBody() : "";

            //create presentation
            Layer a = createLayerWithStyleClass("answer"); 

            Paragraph aPrefixP = new Paragraph();
            aPrefixP.setStyleClass("prefix"); 
            aPrefixP.getChildren().add(new Text(answerPrefixText));
            a.getChildren().add(aPrefixP);

            Paragraph aHeadlineP = new Paragraph();
            aHeadlineP.setStyleClass("title");
            aHeadlineP.getChildren().add(new Text(answerHeadline));
            a.getChildren().add(aHeadlineP);

            Paragraph aBodyP = new Paragraph();
            aBodyP.setStyleClass("body"); 
            aBodyP.getChildren().add(new Text(answerBody));
            a.getChildren().add(aBodyP);

            item.getChildren().add(a);
        }
        
        //'home' link 
        AnchorLink al = new AnchorLink(iwb.getImage("home.gif"),"ap_q" + question.getPrimaryKey());
        al.setStyleClass("home");        
        item.getChildren().add(al);

        return item;
    }
    
}
