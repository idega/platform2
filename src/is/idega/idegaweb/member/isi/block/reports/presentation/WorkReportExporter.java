/*
 * Created on Jul 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import com.idega.presentation.IWContext;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WorkReportExporter extends RegionalUnionAndYearSelector {
	private static final String PARAMETER_SAVE = "param_save";
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportexporter.step_name";
	
	
	public WorkReportExporter() {
		super();
		
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
		
//		ZipEntry e = new ZipEntry("UMFI");
				
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if (getRegionalUnionId() != -1 && getYear() != -1) {
			
		}		
	}
}
