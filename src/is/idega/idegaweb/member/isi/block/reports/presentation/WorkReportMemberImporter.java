/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;

/**
 * Description: This class extends WorkReportImporter and takes the uploaded file and reads and imports the member data.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportMemberImporter extends WorkReportImporter {
	
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportmemberimporter.step_name";

	protected WorkReportMemberImporter() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getWorkReportFileId()!=-1){ //do nothing before we have the file id
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
			try {
				String mainBoardName = getBundle(iwc).getProperty(WorkReportConstants.WR_MAIN_BOARD_NAME);
				boolean success = getWorkReportImportBusiness(iwc).importMemberPart(getWorkReportFileId(),getWorkReportId(),mainBoardName);
				if(success){
					add(iwrb.getLocalizedString("WorkReportMemberImporter.import_successful","Importing members completed successfully."));
				}
				else{
					add(iwrb.getLocalizedString("WorkReportMemberImporter.import_failed","Importing members failed!"));
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (WorkReportImportException e) {
//				e.printStackTrace();
				add( iwrb.getLocalizedString(e.getMessage(),e.getMessage()));
			}
		}
	}
}