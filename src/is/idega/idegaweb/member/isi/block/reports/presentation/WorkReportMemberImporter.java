/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

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
		
		if(getWorkReportFileId()!=-1){//do nothing before we have the file id
			//ICFile file = 
			System.out.println("WEEEE start importing now!");
			
		}
	}

}
