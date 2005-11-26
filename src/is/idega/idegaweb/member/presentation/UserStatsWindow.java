/*
 * Created on Jan 3, 2005
 *
 */
package is.idega.idegaweb.member.presentation;

import com.idega.block.datareport.presentation.ReportGenerator;


import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * @author Sigtryggur
 *
 */
public class UserStatsWindow extends StyledIWAdminWindow {
    
    private IWResourceBundle iwrb;
    private IWBundle iwb;
    public static final String STATS_LOCALIZABLE_KEY_NAME = "STATS_LOCALIZABLE_KEY_NAME";
    public static final String STATS_INVOCATION_NAME_FROM_BUNDLE = "STATS_INVOCATION_NAME_FROM_BUNDLE";
    public static final String STATS_LAYOUT_NAME_FROM_BUNDLE = "STATS_LAYOUT_NAME_FROM_BUNDLE";
    public static final String STATS_DYNAMIC_LAYOUT_FILE_ID = "dr_dynamicLayoutFile";
    
    public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
    public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
      
    public UserStatsWindow() {
        super();
		setHeight(400);
		setWidth(400);
		setScrollbar(false);
		setResizable(true);
    }

    public void main(IWContext iwc) throws Exception {
        super.main(iwc);
        iwrb = getResourceBundle(iwc);
        iwb = getBundle(iwc);     
        
        String windowTitle = iwc.getParameter(STATS_LOCALIZABLE_KEY_NAME);
        setTitle(iwrb.getLocalizedString(windowTitle));
        addTitle(iwrb.getLocalizedString(windowTitle), TITLE_STYLECLASS);
        Table table = new Table(2, 1);
        table.setWidthAndHeightToHundredPercent();
        table.setColumnWidth(1, "200");
        table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
        table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
        table.setCellpaddingAndCellspacing(0);
        table.mergeCells(1, 1, 1, 2);

		ReportGenerator repGen = new ReportGenerator();	
		repGen.setParameterToMaintain(STATS_LAYOUT_NAME_FROM_BUNDLE);
		repGen.setParameterToMaintain(STATS_INVOCATION_NAME_FROM_BUNDLE);
		repGen.setParameterToMaintain(STATS_LOCALIZABLE_KEY_NAME);
		String invocationFileName = iwc.getParameter(STATS_INVOCATION_NAME_FROM_BUNDLE);
		String layoutFileName = iwc.getParameter(STATS_LAYOUT_NAME_FROM_BUNDLE);
	    String localizedNameKey = iwc.getParameter(STATS_LOCALIZABLE_KEY_NAME);
	    repGen.setMethodInvocationBundleAndFileName(iwb, invocationFileName);
		String layoutFileID = iwc.getParameter(STATS_DYNAMIC_LAYOUT_FILE_ID);
		if (layoutFileID != null) {
			if (layoutFileName != null && layoutFileID.equals("-1")) {
		    	repGen.setLayoutBundleAndFileName(iwb, layoutFileName);
		    }
		    else if (layoutFileID != null) {
		    	repGen.setLayoutICFileID(new Integer(layoutFileID));
		    }
		} else if (layoutFileName != null) {
			repGen.setLayoutBundleAndFileName(iwb, layoutFileName);
		}
	    if (localizedNameKey != null) {
	        String reportName = iwrb.getLocalizedString(localizedNameKey);
	        repGen.setReportName(reportName);
	        table.add(formatHeadline(reportName), 1, 1); //not a selector
	        table.addBreak(1, 1);
	    }
		table.add(repGen, 1, 1); //not a selector
		add(table, iwc);		
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }
}