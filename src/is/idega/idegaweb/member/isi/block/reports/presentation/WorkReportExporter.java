/*
 * Created on Jul 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFile;

import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WorkReportExporter extends RegionalUnionAndYearSelector {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private static final String PARAMETER_SAVE = "param_save";
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportexporter.step_name";
	private static final String ERROR_LOCALIZATION_KEY = "workreportexporter.error";
	
	private static final String WR_TEMPLATE_ID_KEY = "isi_wr_template_id";
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public WorkReportExporter() {
		super();
		
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		String templateId = getBundle(iwc).getProperty(WR_TEMPLATE_ID_KEY,"-1");
		
		if (getRegionalUnionId() != -1 && getYear() != -1) {
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);

			Collection col = getWorkReportImportBusiness(iwc).exportToExcel(getRegionalUnionId(),getYear(),Integer.parseInt(templateId),getBundle(iwc).getResourceBundle(iwc));
			if (col != null && !col.isEmpty()) {
				Iterator it = col.iterator();
				while (it.hasNext()) {
					WorkReportExportFile file = (WorkReportExportFile)it.next();
					if (file.getFileId() > 0) {
						String name = getWorkReportBusiness(iwc).getFileName(file.getFileId()); 				
						add(new Link(file.getFileId(),name));
						addBreak();
					}
				}
			}
			else {
				add(this.iwrb.getLocalizedString(ERROR_LOCALIZATION_KEY, "Unable to export file"));
			}
		}		
	}
}
