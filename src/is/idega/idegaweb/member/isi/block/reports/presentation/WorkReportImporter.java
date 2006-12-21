/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.idegaweb.presentation.BusyBar;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Description: This is a super class for the different importers of workreports.<br>
 * It extends WorkReportSelector so you must call its main(iwc) method first in the subclass's main(iwc) method<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportImporter extends WorkReportSelector {

	protected static final String PARAM_FILE_ID = "wr_im_f_id";
	protected static final String PARAM_UPLOADING = "wr_im_f_upl";
	protected static final String PARAM_FILE_NAME = "wr_im_f_name";

	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportimporter.step_name";
	protected int workReportFileId = -1;

	/**
	 * @return the ic_file id
	 */
	public int getWorkReportFileId() {
		return this.workReportFileId;
	}

	/**
	 * @param workReportFileId
	 */
	public void setWorkReportFileId(int workReportFileId) {
		this.workReportFileId = workReportFileId;
	}

	protected WorkReportImporter() {
		super();
		this.addToParametersToMaintainList(PARAM_FILE_ID);
		this.addToParametersToMaintainList(PARAM_FILE_NAME);
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);

		if (getWorkReportId() != -1) { //do nothing before we have the work report id
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);

			if (iwc.isParameterSet(PARAM_FILE_NAME)) {
				String fileName = iwc.getParameter(PARAM_FILE_NAME);
				addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,new Text(fileName));				
			}

			if (iwc.isParameterSet(PARAM_FILE_ID)) {
				System.out.println("PARAM_FILE_ID set.");
				this.workReportFileId = Integer.parseInt(iwc.getParameter(PARAM_FILE_ID));
			}
			else {
				System.out.println("PARAM_FILE_ID not set.");
				addUploadForm(iwc);
			}
			
		}
	}

	protected void addUploadForm(IWContext iwc) {
		Form uploadForm = new Form();
		uploadForm.maintainParameters(getParametersToMaintain());

		SimpleFileChooser uploader = new SimpleFileChooser(uploadForm, PARAM_FILE_ID);
		uploader.setShowChangeUploadedFileOption(false);
		uploadForm.add(uploader);

		if (!iwc.isParameterSet(PARAM_UPLOADING)) { //not uploaded yet
			uploadForm.addParameter(PARAM_UPLOADING, "TRUE");
		}
		else {
			String filename = this.iwrb.getLocalizedString("workreportimporter.no_file", "No file selected");
			try {
				filename = iwc.getUploadedFile().getFileName();
			}
			catch(Exception e) {
			}
			addToStepsExtraInfo(STEP_NAME_LOCALIZATION_KEY,new Text(filename));
			uploadForm.addParameter(PARAM_FILE_NAME,filename);

			Table t = new Table(1, 3);
			t.setCellpadding(0);
			t.setCellspacing(0);

			SubmitButton submit = new SubmitButton(this.iwrb.getLocalizedString("workreportimporter.start", "start"));
			//Do I need this??
			submit.setOnClick("this.form.submit()");			
			BusyBar busy = new BusyBar("readingfiles");
			busy.addDisabledObject(submit);
			busy.addBusyObject(submit);
			t.add(this.iwrb.getLocalizedString("workreportimporter.click.start", "Click start to import the data. It may take a while."), 1, 1);
			t.add(submit, 1, 2);
			t.add(busy, 1, 3);
			uploadForm.add(t);
		}

		add(uploadForm);
	}
}
