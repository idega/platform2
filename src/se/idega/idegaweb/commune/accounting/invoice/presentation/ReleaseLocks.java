package se.idega.idegaweb.commune.accounting.invoice.presentation;

import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunSemaphore;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Displays a list of all batches that are in the queue, and buttons that makes
 * it possible to delete/terminate a batch run
 * 
 * @author Joakim
 *  
 */
public class ReleaseLocks extends AccountingBlock {
	private static String PARAMETER = "release";
	private static String CHILDCARE_KEY = "childcareKey";
	private static String ELEMENTARY_KEY = "elementaryKey";
	private static String HIGHSHOOL_KEY = "highschoolKey";
	public void init(IWContext iwc) {
		int row = 2;
		boolean buttonDisplayed = false;
		handleAction(iwc);
		Form form = new Form();

		Table table = new Table();

		table.add(getLocalizedSmallHeader("release.Release", "Release"), 1, 1);

		if(BatchRunSemaphore.isChildcareRunFlag()){
			SubmitButton submitButton =
				new SubmitButton(
						getLocalizedString("release.childcare", "childcare", iwc),
						PARAMETER,
						CHILDCARE_KEY);
			submitButton.setAsImageButton(true);
			table.add(submitButton, 1, row++);
			buttonDisplayed = true;
		}
		if(BatchRunSemaphore.isElementaryRunFlag()){
			SubmitButton submitButton2 =
				new SubmitButton(
						getLocalizedString("release.elementary", "elementary", iwc),
						PARAMETER,
						ELEMENTARY_KEY);
			submitButton2.setAsImageButton(true);
			table.add(submitButton2, 1, row++);
			buttonDisplayed = true;
		}
		if(BatchRunSemaphore.isHighRunFlag()){
			SubmitButton submitButton3 =
				new SubmitButton(
						getLocalizedString("release.highschool", "highschool", iwc),
						PARAMETER,
						HIGHSHOOL_KEY);
			submitButton3.setAsImageButton(true);
			table.add(submitButton3, 1, row++);
			buttonDisplayed = true;
		}		
		form.add(table);
		
		if(!buttonDisplayed){
			form.add(getLocalizedText("release.nothing is locked", "Nothing is locked"));
			form.add(new Break());
		}

		add(form);
	}

	/**
	 * If a delete button has been pressed, delet a batch run
	 * 
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER)) {
			String param = iwc.getParameter(PARAMETER);
			if(param.equalsIgnoreCase(CHILDCARE_KEY)){
				BatchRunSemaphore.releaseChildcareRunSemaphore();
			}else if(param.equalsIgnoreCase(ELEMENTARY_KEY)){
				BatchRunSemaphore.releaseElementaryRunSemaphore();
			}else if(param.equalsIgnoreCase(HIGHSHOOL_KEY)){
				BatchRunSemaphore.releaseHighRunSemaphore();
			}
			System.out.println(param);
		}
	}
}
