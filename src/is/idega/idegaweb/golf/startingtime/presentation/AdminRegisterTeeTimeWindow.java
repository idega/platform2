package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;

/**
 * @author gimmi
 */
public class AdminRegisterTeeTimeWindow extends GolfWindow{

	public AdminRegisterTeeTimeWindow() {
		super.setAllMargins(0);
		super.setWidth(600);
		super.setResizable(true);
		super.setHeight(500);
		super.setToolbar(true);
		super.setScrollbar(true);	
	}

	public void main(IWContext modinfo) {
		AdminRegisterTime art = new AdminRegisterTime();
		art.setForPrinting(true);
		add(art);
	}
}
