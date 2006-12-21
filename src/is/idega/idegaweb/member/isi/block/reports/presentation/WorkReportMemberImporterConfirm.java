/*
 * Created on Sep 26, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.presentation.IWContext;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WorkReportMemberImporterConfirm extends WorkReportMemberImporter {
	
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportmemberimporterconfirm.step_name";

	protected WorkReportMemberImporterConfirm() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if (iwc.isParameterSet("MEMBER_IMPORT_CONFIRM") || iwc.isParameterSet("MEMBER_IMPORT_REJECT")) { 
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
			if (iwc.isParameterSet("MEMBER_IMPORT_CONFIRM")) {
				add(this.iwrb.getLocalizedString("WorkReportMemberImporter.import_successful","Importing members completed successfully."));
			}
			else if  (iwc.isParameterSet("MEMBER_IMPORT_REJECT")) {
				add(this.iwrb.getLocalizedString("WorkReportMemberImporter.import_failed","Importing members failed!"));
			}
/*			try {
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
*/		}
		}
	}
