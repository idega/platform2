/*
 * Created on Jun 12, 2006
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
public class GenericStatsWindow extends StyledIWAdminWindow {
    
    protected IWResourceBundle iwrb;
    protected IWBundle iwb;
    protected static final String STATS_DYNAMIC_LAYOUT_FILE_ID = "dr_dynamicLayoutFile";
    protected static final String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
    protected static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
    protected String invocationFileName = null;
    protected String layoutFileName = null;
    protected String windowTitle = "GenericStatsWindow";
      
    public GenericStatsWindow() {
        super();
		setScrollbar(false);
		setResizable(true);
		setHeight(400);
		setWidth(400);
    }

    public void main(IWContext iwc) throws Exception {
        super.main(iwc);
        if (this.iwrb == null) {
        	this.iwrb = getResourceBundle(iwc);
        }
        this.iwb = getBundle(iwc);     
        
        setTitle(this.windowTitle);
        addTitle(this.windowTitle, TITLE_STYLECLASS);
        Table table = new Table(2, 1);
        table.setWidthAndHeightToHundredPercent();
        table.setColumnWidth(1, "200");
        table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
        table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
        table.setCellpaddingAndCellspacing(0);
        table.mergeCells(1, 1, 1, 2);

		ReportGenerator repGen = new ReportGenerator();	
	    repGen.setMethodInvocationBundleAndFileName(this.iwb, this.invocationFileName);
		String layoutFileID = iwc.getParameter(STATS_DYNAMIC_LAYOUT_FILE_ID);
		if (layoutFileID != null) {
			if (this.layoutFileName != null && layoutFileID.equals("-1")) {
		    	repGen.setLayoutBundleAndFileName(this.iwb, this.layoutFileName);
		    }
		    else if (layoutFileID != null) {
		    	repGen.setLayoutICFileID(new Integer(layoutFileID));
		    }
		} else if (this.layoutFileName != null) {
			repGen.setLayoutBundleAndFileName(this.iwb, this.layoutFileName);
		}
	    repGen.setReportName(this.windowTitle);
	    table.add(formatHeadline(this.windowTitle), 1, 1); //not a selector
	    table.addBreak(1, 1);
		table.add(repGen, 1, 1); //not a selector
		add(table, iwc);
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }
}