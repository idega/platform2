/*
 * Created on 4.6.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: TeeTimeFieldConfigurationWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TeeTimeFieldConfigurationWindow extends GolfWindow {

	/**
	 * 
	 */
	public TeeTimeFieldConfigurationWindow() {
		this("Field Configuration", 520, 320);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TeeTimeFieldConfigurationWindow(String name, int width, int height) {
		super(name, width, height);
		setGolfClassToInstanciate(TeeTimeFieldConfiguration.class);
		this.setContentHorizontalAlignment(Table.HORIZONTAL_ALIGN_CENTER);
		this.setContentVerticalAlignment(Table.VERTICAL_ALIGN_MIDDLE);
	}
	
	public void main(IWContext iwc) {
		this.setTitle(localize("start.field_configuration","Field Configuration"));
	}

}
