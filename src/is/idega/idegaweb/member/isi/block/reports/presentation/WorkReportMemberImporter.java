/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException;
import is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportReport;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Description: This class extends WorkReportImporter and takes the uploaded file and reads and imports the member data.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportMemberImporter extends WorkReportImporter {

	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportmemberimporter.step_name";
	private WorkReportImportReport _report = null;

	protected WorkReportMemberImporter() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);

		if (getWorkReportFileId() != -1 && !iwc.isParameterSet("MEMBER_IMPORT_CONFIRM") && !iwc.isParameterSet("MEMBER_IMPORT_REJECT")) { //do nothing before we have the file id
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);

			try {
				String mainBoardName = getBundle(iwc).getProperty(WorkReportConstants.WR_MAIN_BOARD_NAME);
				WorkReportImportReport report = getWorkReportImportBusiness(iwc).importMemberPart(getWorkReportFileId(), getWorkReportId(), mainBoardName);
				if (report != null) {
					//					add(iwrb.getLocalizedString("WorkReportMemberImporter.import_successful","Importing members completed successfully."));
					setWorkReportMemberImportReport(report);
				}
				else {
					add(iwrb.getLocalizedString("WorkReportMemberImporter.import_failed", "Importing members failed!"));
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (WorkReportImportException e) {
				//				e.printStackTrace();
				add(iwrb.getLocalizedString(e.getMessage(), e.getMessage()));
			}
		}
	}

	protected WorkReportImportReport getWorkReportMemberImportReport() {
		return _report;
	}

	protected void setWorkReportMemberImportReport(WorkReportImportReport report) {
		_report = report;
	}

	protected void showReport() {
		Form f = new Form();
		Table t = new Table();
		t.add(iwrb.getLocalizedString("workreportmemberimport.import_statistics"), 1, 1);
		t.add(iwrb.getLocalizedString("workreportmemberimport.number_of_members"), 1, 3);
		t.add(Integer.toString(getWorkReportMemberImportReport().numberOfMembers), 2, 3);
		t.add(iwrb.getLocalizedString("workreportmemberimport.number_of_players"), 1, 5);
		t.add(Integer.toString(getWorkReportMemberImportReport().numberOfPlayers), 2, 5);
		t.add(iwrb.getLocalizedString("workreportmemberimport.number_of_divisions"), 1, 7);
		t.add(Integer.toString(getWorkReportMemberImportReport().leaguesMap.size()), 2, 7);
		t.add(iwrb.getLocalizedString("workreportmemberimport.not_imported"), 1, 9);
		int i = 9;
		if (getWorkReportMemberImportReport().notRead != null) {
			Iterator it = getWorkReportMemberImportReport().notRead.iterator();
			while (it.hasNext()) {
				t.add((String)it.next(), 2, i++);
			}
		}

		i++;
		SubmitButton confirm = new SubmitButton("MEMBER_IMPORT_CONFIRM");
		SubmitButton reject = new SubmitButton("MEMBER_IMPORT_REJECT");
		t.add(confirm,2,i);
		t.add(reject,2,i);
			
		f.add(t);	
		add(f);
	}

}