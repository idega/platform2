package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Link;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class StartingtimeReportWindow extends GolfWindow {

	public static final String PARAMETER_DATE = "sr_prm_d";
	public static final String PARAMETER_FIELD_ID = "sr_prm_f";

	public StartingtimeReportWindow() {
		super.setWidth(400);
		super.setHeight(450);
		super.setResizable(true);
	}

	public void main(IWContext modinfo) throws Exception{
		super.main(modinfo);
		StartingtimeReport sr = new StartingtimeReport();
		add(sr);
	}
	
	public static Link getLink(IWTimestamp date, int fieldId, PresentationObject po) { 
		Link print = new Link(po);
		print.setWindowToOpen(StartingtimeReportWindow.class);
		print.addParameter(StartingtimeReportWindow.PARAMETER_DATE, date.toSQLDateString());
		print.addParameter(StartingtimeReportWindow.PARAMETER_FIELD_ID, fieldId);
		return print;
	}

}
