package is.idega.idegaweb.golf.handicap.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author gimmi
 */
public class HandicapOverviewWindow extends Window{

	public static String PARAMETER_MEMBER_ID = "hcow_m_id";

	public HandicapOverviewWindow() {
		super.setWidth(575);
		super.setHeight(400);
		super.setResizable(true);
	}

	public void main(IWContext modinfo) {
		String memberId = modinfo.getParameter(PARAMETER_MEMBER_ID);
		if (memberId != null) {
			HandicapOverview ch = new HandicapOverview(Integer.parseInt(memberId));
			ch.setIsWindow(true);
			ch.noIcons();
			add(ch);
		}	
	}
}
