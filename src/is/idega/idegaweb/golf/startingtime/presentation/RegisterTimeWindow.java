/*
 * Created on 30.6.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;

/**
 * Title: RegisterTimeWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class RegisterTimeWindow extends GolfWindow {

	/**
	 * 
	 */
	public RegisterTimeWindow() {
		this("Register Tee Time",400,340);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public RegisterTimeWindow(String name, int width, int height) {
		super(name, width, height);
		this.setScrollbar(true);
		this.setGolfClassToInstanciate(RegisterTime.class);
	}
	
	public void main(IWContext iwc) throws Exception{
		super.main(iwc);
	    this.setTitle(this.localize("start.register_tee_time","Register tee time"));
	}
	

}
