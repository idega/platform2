/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.presentation.IWContext;

/**
 * Description: This class extends WorkReportImporter and takes the uploaded file and reads and imports the account data.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportAccountImporter extends WorkReportImporter {
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportaccountimporter.step_name";

	protected WorkReportAccountImporter() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);

		if (getWorkReportFileId() != -1) { //do nothing before we have the file id
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);

			boolean success = getWorkReportBusiness(iwc).importAccountPart(getWorkReportFileId(), getWorkReportId());
			if (success) {
				add(iwrb.getLocalizedString("WorkReportAccountImporter.import_successful", "Importing yearly account info completed successfully."));
			}
			else {
				add(iwrb.getLocalizedString("WorkReportAccountImporter.import_failed", "Importing yearly account failed!"));
			}
		}
	}
}