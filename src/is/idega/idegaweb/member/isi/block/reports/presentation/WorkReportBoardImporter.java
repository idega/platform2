/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.presentation.IWContext;

/**
 * Description: This class extends WorkReportImporter and takes the uploaded file and reads and imports the board and division data.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportBoardImporter extends WorkReportImporter {
	
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportboardimporter.step_name";

	protected WorkReportBoardImporter() {
		super();
		setStepNameLocalizedKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getWorkReportFileId()!=-1){ //do nothing before we have the file id
		
			boolean success = getWorkReportBusiness(iwc).importBoardPart(getWorkReportFileId(),getWorkReportId());
			if(success){
				add(iwrb.getLocalizedString("WorkReportBoardImporter.import_successful","Importing board info completed successfully."));
			}
			else{
				add(iwrb.getLocalizedString("WorkReportBoardImporter.import_failed","Importing board account failed!"));
			}
		}
	}
	
	protected String getCurrentStepNameLocalizedKey(){
		return STEP_NAME_LOCALIZATION_KEY;
	}

}
