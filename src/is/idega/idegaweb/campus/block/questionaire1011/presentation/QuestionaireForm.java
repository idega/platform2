package is.idega.idegaweb.campus.block.questionaire1011.presentation;

import is.idega.idegaweb.campus.block.questionaire1011.business.QuestionaireBusiness;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.util.Edit;



/**
 * @author palli
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class QuestionaireForm extends CampusBlock {
	protected final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus.block.questionaire1011";
 
 	protected final static String SUBMIT_ANSWER = "sub_questionaire1011";
 	protected final static String ERROR_NOT_ALL = "err_not_all_answered";
 	protected final static String ERROR_NO_USER = "err_not_user";
 	protected final static String MSG_THANKS = "msg_thx";
 	protected final static String MSG_ALREADY_ANSWERED = "msg_already_answered";
 
	protected final static String QUESTION_1 = "question_1"; 
	protected final static String QUESTION_2 = "question_2"; 
	protected final static String QUESTION_3 = "question_3"; 
	protected final static String QUESTION_4 = "question_4"; 
	protected final static String QUESTION_5 = "question_5"; 
	protected final static String QUESTION_6 = "question_6"; 
	protected final static String QUESTION_7 = "question_7"; 
	protected final static String QUESTION_8 = "question_8"; 
	protected final static String QUESTION_9 = "question_9";
	
	protected final static String ANSWER_1_1 = "answer_1_1";
	protected final static String ANSWER_1_2 = "answer_1_2";
	
	protected final static String ANSWER_2_1 = "answer_2_1";
	protected final static String ANSWER_2_2 = "answer_2_2";
	protected final static String ANSWER_2_3 = "answer_2_3";
	protected final static String ANSWER_2_4 = "answer_2_4";
	
	protected final static String ANSWER_3_1 = "answer_3_1";
	protected final static String ANSWER_3_2 = "answer_3_2";
	protected final static String ANSWER_3_3 = "answer_3_3";
	protected final static String ANSWER_3_4 = "answer_3_4";
	protected final static String ANSWER_3_5 = "answer_3_5";
	protected final static String ANSWER_3_6 = "answer_3_6";
	protected final static String ANSWER_3_7 = "answer_3_7";
	
	protected final static String ANSWER_4_1 = "answer_4_1";
	protected final static String ANSWER_4_2 = "answer_4_2";
	protected final static String ANSWER_4_3 = "answer_4_3";
	
	protected final static String ANSWER_5_1 = "answer_5_1";
	protected final static String ANSWER_5_2 = "answer_5_2";
	
	protected final static String ANSWER_6_1 = "answer_6_1";
	protected final static String ANSWER_6_2 = "answer_6_2";
	protected final static String ANSWER_6_3 = "answer_6_3";
	protected final static String ANSWER_6_4 = "answer_6_4";
	protected final static String ANSWER_6_5 = "answer_6_5";
	
	protected final static String ANSWER_7_1 = "answer_7_1";
	protected final static String ANSWER_7_2 = "answer_7_2";
	protected final static String ANSWER_7_3 = "answer_7_3";
	
	protected final static String ANSWER_8_1 = "answer_8_1";
	protected final static String ANSWER_8_2 = "answer_8_2";
	protected final static String ANSWER_8_3 = "answer_8_3";
	protected final static String ANSWER_8_4 = "answer_8_4";
	protected final static String ANSWER_8_5 = "answer_8_5";
	
	protected final static String ANSWER_9_1 = "answer_9_1";
	protected final static String ANSWER_9_2 = "answer_9_2";
	protected final static String ANSWER_9_3 = "answer_9_3";
	protected final static String ANSWER_9_4 = "answer_9_4";
	protected final static String ANSWER_9_5 = "answer_9_5";

	public QuestionaireForm() {
		super();
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {	
		try {
			QuestionaireBusiness b = getQuestionaireBusiness(iwc);
			IWResourceBundle iwrb = getResourceBundle(iwc);
			
/*			User current = iwc.getUser();
			if (current == null) {
				add(Edit.headerText(iwrb.getLocalizedString(ERROR_NO_USER)));
				return;
			}
			
			boolean already = b.hasUserAlreadyAnswered(iwc.getUser());
			if (already) {
				add(Edit.headerText(iwrb.getLocalizedString(MSG_ALREADY_ANSWERED)));
				
				return;
			}*/
			
			if (iwc.isParameterSet(SUBMIT_ANSWER)) {
				boolean ok = checkAndStoreAnswers(iwc);
				
				if (ok) {
					add(Edit.headerText(iwrb.getLocalizedString(MSG_THANKS)));
					return;
				}
				else {
					add(Edit.headerText(iwrb.getLocalizedString(ERROR_NOT_ALL)));
				}
			}
			
			showForm(iwc);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean checkAndStoreAnswers(IWContext iwc) {
		String a1 = iwc.getParameter(QUESTION_1);
		String a2 = iwc.getParameter(QUESTION_2);
		String a3 = iwc.getParameter(QUESTION_3);
		String a4 = iwc.getParameter(QUESTION_4);
		String a5 = iwc.getParameter(QUESTION_5);
		String a6 = iwc.getParameter(QUESTION_6);
		String a7 = iwc.getParameter(QUESTION_7);
		String a8 = iwc.getParameter(QUESTION_8);
		String a9 = iwc.getParameter(QUESTION_9);
		
		if (a1 == null || a2 == null || a3 == null || a4 == null || a5 == null || a6 == null || a7 == null || a8 == null || a9 == null)
			return false;
		
		int ia1 = Integer.parseInt(a1);
		int ia2 = Integer.parseInt(a2);
		int ia3 = Integer.parseInt(a3);
		int ia4 = Integer.parseInt(a4);
		int ia5 = Integer.parseInt(a5);
		int ia6 = Integer.parseInt(a6);
		int ia7 = Integer.parseInt(a7);
		int ia8 = Integer.parseInt(a8);
		int ia9 = Integer.parseInt(a9);
		
		try {
			return getQuestionaireBusiness(iwc).insertAnswers(ia1,ia2,ia3,ia4,ia5,ia6,ia7,ia8,ia9,iwc.getCurrentUser());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void showForm(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Form f = new Form();
		Table t = new Table();
		RadioGroup answer_1 = new RadioGroup(QUESTION_1);
		answer_1.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_1_1)));
		answer_1.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_1_2)));
		answer_1.keepStatusOnAction();

		RadioGroup answer_2 = new RadioGroup(QUESTION_2);
		answer_2.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_2_1)));
		answer_2.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_2_2)));
		answer_2.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_2_3)));
		answer_2.addRadioButton(4,Edit.formatText(iwrb.getLocalizedString(ANSWER_2_4)));
		answer_2.keepStatusOnAction();

		RadioGroup answer_3 = new RadioGroup(QUESTION_3);
		answer_3.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_1)));
		answer_3.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_2)));
		answer_3.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_3)));
		answer_3.addRadioButton(4,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_4)));
		answer_3.addRadioButton(5,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_5)));
		answer_3.addRadioButton(6,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_6)));
		answer_3.addRadioButton(7,Edit.formatText(iwrb.getLocalizedString(ANSWER_3_7)));
		answer_3.keepStatusOnAction();

		RadioGroup answer_4 = new RadioGroup(QUESTION_4);
		answer_4.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_4_1)));
		answer_4.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_4_2)));
		answer_4.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_4_3)));
		answer_4.keepStatusOnAction();

		RadioGroup answer_5 = new RadioGroup(QUESTION_5);
		answer_5.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_5_1)));
		answer_5.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_5_2)));
		answer_5.keepStatusOnAction();

		RadioGroup answer_6 = new RadioGroup(QUESTION_6);
		answer_6.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_6_1)));
		answer_6.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_6_2)));
		answer_6.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_6_3)));
		answer_6.addRadioButton(4,Edit.formatText(iwrb.getLocalizedString(ANSWER_6_4)));
		answer_6.addRadioButton(5,Edit.formatText(iwrb.getLocalizedString(ANSWER_6_5)));
		answer_6.keepStatusOnAction();

		RadioGroup answer_7 = new RadioGroup(QUESTION_7);
		answer_7.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_7_1)));
		answer_7.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_7_2)));
		answer_7.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_7_3)));
		answer_7.keepStatusOnAction();

		RadioGroup answer_8 = new RadioGroup(QUESTION_8);
		answer_8.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_8_1)));
		answer_8.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_8_2)));
		answer_8.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_8_3)));
		answer_8.addRadioButton(4,Edit.formatText(iwrb.getLocalizedString(ANSWER_8_4)));
		answer_8.addRadioButton(5,Edit.formatText(iwrb.getLocalizedString(ANSWER_8_5)));
		answer_8.keepStatusOnAction();

		RadioGroup answer_9 = new RadioGroup(QUESTION_9);
		answer_9.addRadioButton(1,Edit.formatText(iwrb.getLocalizedString(ANSWER_9_1)));
		answer_9.addRadioButton(2,Edit.formatText(iwrb.getLocalizedString(ANSWER_9_2)));
		answer_9.addRadioButton(3,Edit.formatText(iwrb.getLocalizedString(ANSWER_9_3)));
		answer_9.addRadioButton(4,Edit.formatText(iwrb.getLocalizedString(ANSWER_9_4)));
		answer_9.addRadioButton(5,Edit.formatText(iwrb.getLocalizedString(ANSWER_9_5)));
		answer_9.keepStatusOnAction();
		
		int row = 1;
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_1)),1,row++);
		t.add(answer_1,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_2)),1,row++);
		t.add(answer_2,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_3)),1,row++);
		t.add(answer_3,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_4)),1,row++);
		t.add(answer_4,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_5)),1,row++);
		t.add(answer_5,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_6)),1,row++);
		t.add(answer_6,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_7)),1,row++);
		t.add(answer_7,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_8)),1,row++);
		t.add(answer_8,1,row++);
		t.add(getHeader(iwrb.getLocalizedString(QUESTION_9)),1,row++);
		t.add(answer_9,1,row++);
				
		f.add(t);
		
		SubmitButton s = new SubmitButton(SUBMIT_ANSWER,iwrb.getLocalizedString(SUBMIT_ANSWER));
		s.setAsImageButton(true);
		f.add(s);
		
		add(f);
	}
	
	private QuestionaireBusiness getQuestionaireBusiness(IWContext iwc) throws Exception {
		return (QuestionaireBusiness) IBOLookup.getServiceInstance(iwc, QuestionaireBusiness.class);
	}	
}