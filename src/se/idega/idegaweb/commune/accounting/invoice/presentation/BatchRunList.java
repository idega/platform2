package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.util.Iterator;

import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue;
import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue.BatchRunObject;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Displays a list of all batches that are in the queue, and buttons that makes
 * it possible to delete/terminate a batch run
 * 
 * @author Joakim
 *  
 */
public class BatchRunList extends AccountingBlock {
	private static String KEY = "removeKey";
	private static String UPDATE_KEY = "updateKey";
	private String response;
	public void init(IWContext iwc) {
		response = null;
		handleAction(iwc);
		Form form = new Form();

		Iterator queueIterator = BatchRunQueue.iterator();

		int row = 0;

		Table errorTable = new Table();

		if (queueIterator.hasNext()) {
			errorTable.add(getLocalizedSmallHeader("batchlist.Order", "Order"), 1, 1);
			errorTable.add(getLocalizedSmallHeader("batchlist.batch", "Batch"), 2, 1);

			while (queueIterator.hasNext()) {
				BatchRunObject batchRunObject = (BatchRunObject) queueIterator.next();
				errorTable.setRowColor(row + 1, (row % 2 == 0) ? getZebraColor1() : getZebraColor2());
				if (row == 0) {
					errorTable.add(getLocalizedText("batchlist.Running", "Running"), 1, row + 2);
				} else {
					errorTable.add(new Text("" + row), 1, row + 2);
				}
				errorTable.add(new Text(batchRunObject.toString()), 2, row + 2);
				SubmitButton submitButton =
					new SubmitButton(
						getLocalizedString("batchlist.remove", "remove", iwc),
						KEY,
						batchRunObject.toString());
				submitButton.setAsImageButton(true);
				errorTable.add(submitButton, 3, row + 2);
				row++;
			}
			form.add(errorTable);
		} else {
			response = "batchlist.No_batchruns_in_queue";
		}
		if (response != null) {
			form.add(getLocalizedText(response, response.replace('_', ' ')));
			form.add(new Break());
		}
		SubmitButton submitButton =
			new SubmitButton(getLocalizedString("batchlist.Update", "Update", iwc), UPDATE_KEY, "");
		submitButton.setAsImageButton(true);
		form.add(submitButton);
		add(form);
		/*
		 * } else { SubmitButton submitButton = new
		 * SubmitButton(getLocalizedString("batchlist.Update","Update",iwc),UPDATE_KEY,"");
		 * submitButton.setAsImageButton(true); form.add(submitButton);
		 * add(getLocalizedText("batchlist.No_batchruns_in_queue","No batchruns
		 * in queue")); }
		 */
	}

	/**
	 * If a delete button has been pressed, delet a batch run
	 * 
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if (iwc.isParameterSet(KEY)) {
			response = BatchRunQueue.removeBatchRunFromQueue(iwc.getParameter(KEY));
			System.out.println(iwc.getParameter(KEY));
		}
	}
}
