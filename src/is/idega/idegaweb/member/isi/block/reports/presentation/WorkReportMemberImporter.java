/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.presentation.IWContext;

/**
 * Description: This class extends WorkReportImporter and takes the uploaded file and reads and imports the member data.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportMemberImporter extends WorkReportImporter {
	


	protected WorkReportMemberImporter() {
		super();
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getWorkReportFileId()!=-1){ //do nothing before we have the file id
		
			boolean success = getWorkReportBusiness(iwc).importMemberPart(getWorkReportFileId(),getWorkReportId());
			if(success){
				add(iwrb.getLocalizedString("WorkReportMemberImporter.import_successful","Importing members completed successfully."));
			}
			else{
				add(iwrb.getLocalizedString("WorkReportMemberImporter.import_failed","Importing members failed!"));
			}
		}
	}

}
