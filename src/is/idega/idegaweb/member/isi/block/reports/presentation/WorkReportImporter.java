/*
 * Created on Jun 22, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;

/**
 * Description: This is a super class for the different importers of workreports.<br>
 * It extends WorkReportSelector so you must call its main(iwc) method first in the subclass's main(iwc) method<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportImporter extends WorkReportSelector {
	
	protected static final String PARAM_FILE_ID = "wr_im_f_id";
	protected int workReportFileId = -1;
	
	/**
	 * @return the ic_file id
	 */
	public int getWorkReportFileId() {
		return workReportFileId;
	}

	/**
	 * @param workReportFileId
	 */
	public void setWorkReportFileId(int workReportFileId) {
		this.workReportFileId = workReportFileId;
	}

	protected WorkReportImporter() {
		super();
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if(getWorkReportId()!=-1){//do nothing before we have the clubId
			if(iwc.isParameterSet(PARAM_FILE_ID)){
				workReportFileId = Integer.parseInt(PARAM_FILE_ID);
			}
			else{
				addUploadForm();
			}
		}
	}


	
	protected void addUploadForm() {
		Form uploadForm = new Form();
		uploadForm.maintainParameters(getParametersToMaintain());
		uploadForm.add(new SimpleFileChooser(uploadForm,PARAM_FILE_ID));
		
		add(uploadForm);
	}
}
