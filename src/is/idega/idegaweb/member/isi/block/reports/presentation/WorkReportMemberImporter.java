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

	protected final static String ROW_LABEL = "wr_imp_row";
	protected final static String COL_LABEL = "wr_imp_col";
	protected final static String DESC_LABEL = "wr_imp_desc";

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
					showReport();
				}
				else {
					add(this.iwrb.getLocalizedString("WorkReportMemberImporter.import_failed", "Importing members failed!"));
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (WorkReportImportException e) {
				add(this.iwrb.getLocalizedString(e.getMessage(), e.getMessage()));
				if (e.getRowForError() != null) {
					addBreak();
					add(this.iwrb.getLocalizedString(ROW_LABEL,"Row") + " : " + e.getRowForError());
				}
				if (e.getColumnForError() != null)  { 
					addBreak();
					add(this.iwrb.getLocalizedString(COL_LABEL,"Col") + " : " + e.getColumnForError());
				}
				if (e.getDetail() != null) { 
					addBreak();
					add(this.iwrb.getLocalizedString(DESC_LABEL,"Desc") + " : " + e.getDetail());
				}					
			}
		}
	}

	protected WorkReportImportReport getWorkReportMemberImportReport() {
		return this._report;
	}

	protected void setWorkReportMemberImportReport(WorkReportImportReport report) {
		this._report = report;
	}

	protected void showReport() {
		Form f = new Form();
		f.maintainParameters(getParametersToMaintain());
		Table t = new Table();
		t.add(this.iwrb.getLocalizedString("workreportmemberimport.import_statistics"), 1, 1);
		t.add(this.iwrb.getLocalizedString("workreportmemberimport.number_of_members"), 1, 3);
		t.add(Integer.toString(getWorkReportMemberImportReport().numberOfMembers), 2, 3);
		t.add(this.iwrb.getLocalizedString("workreportmemberimport.number_of_players"), 1, 5);
		t.add(Integer.toString(getWorkReportMemberImportReport().numberOfPlayers), 2, 5);
		t.add(this.iwrb.getLocalizedString("workreportmemberimport.number_of_divisions"), 1, 7);
		t.add(Integer.toString(getWorkReportMemberImportReport().leaguesMap.size()), 2, 7);
		t.add(this.iwrb.getLocalizedString("workreportmemberimport.not_imported"), 1, 9);
		int i = 9;
		if (getWorkReportMemberImportReport().notRead != null) {
			Iterator it = getWorkReportMemberImportReport().notRead.iterator();
			while (it.hasNext()) {
				t.add((String)it.next(), 2, i++);
			}
		}

		i++;
		SubmitButton submit = new SubmitButton(this.iwrb.getLocalizedString("workreportimporter.start", "start"));
		submit.setAsImageButton(true);

		SubmitButton confirm = new SubmitButton("MEMBER_IMPORT_CONFIRM",this.iwrb.getLocalizedString("workreportimporter.confirm", "confirm"));
		SubmitButton reject = new SubmitButton("MEMBER_IMPORT_REJECT",this.iwrb.getLocalizedString("workreportimporter.reject", "reject"));
		t.add(confirm,2,i);
		t.add(reject,2,i);
			
		f.add(t);	
		add(f);
	}
}