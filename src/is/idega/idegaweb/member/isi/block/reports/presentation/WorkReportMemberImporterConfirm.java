/*
 * Created on Sep 26, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

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
		
		if (getWorkReportMemberImportReport() != null) { 
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
			if (iwc.isParameterSet("MEMBER_IMPORT_CONFIRM")) {
				
			}
			else if  (iwc.isParameterSet("MEMBER_IMPORT_REJECT")) {
				
			}
			else {
				showReport();
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
	
	protected void showReport() {
		Table t = new Table();
		t.add(iwrb.getLocalizedString("workreportmemberimport.import_statistics"),1,1);
		t.add(iwrb.getLocalizedString("workreportmemberimport.number_of_members"),1,3);
		t.add(Integer.toString(getWorkReportMemberImportReport().numberOfMembers),2,3);
		t.add(iwrb.getLocalizedString("workreportmemberimport.number_of_players"),1,5);
		t.add(Integer.toString(getWorkReportMemberImportReport().numberOfPlayers),2,5);
		t.add(iwrb.getLocalizedString("workreportmemberimport.number_of_divisions"),1,7);
		t.add(Integer.toString(getWorkReportMemberImportReport().leaguesMap.size()),2,7);
		t.add(iwrb.getLocalizedString("workreportmemberimport.not_imported"),1,9);
		if (getWorkReportMemberImportReport().notRead != null) {
			Iterator it = getWorkReportMemberImportReport().notRead.iterator();
			int i = 9;
			while (it.hasNext()) {
				t.add((String)it.next(),2,i++);
			}
		}
		
		add(t);
	}
}
