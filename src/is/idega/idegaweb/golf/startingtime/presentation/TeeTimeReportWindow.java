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
public class TeeTimeReportWindow extends GolfWindow {

	public static final String PARAMETER_DATE = "sr_prm_d";
	public static final String PARAMETER_FIELD_ID = "sr_prm_f";

	public TeeTimeReportWindow() {
		super.setWidth(400);
		super.setHeight(480);
		super.setResizable(true);
	}

	public void main(IWContext modinfo) throws Exception{
		super.main(modinfo);
		TeeTimeReport sr = new TeeTimeReport();
		add(sr);
	}
	
	public static Link getLink(IWTimestamp date, int fieldId) { 
		Link print = new Link();
		print.setWindowToOpen(TeeTimeReportWindow.class);
		print.addParameter(TeeTimeReportWindow.PARAMETER_DATE, date.toSQLDateString());
		print.addParameter(TeeTimeReportWindow.PARAMETER_FIELD_ID, fieldId);
		return print;
	}

}
