/*
 * Created on 14.4.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import com.idega.presentation.ui.Window;


/**
 * Title: AdminRegisterTimeWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class AdminRegisterTimeWindow extends Window {

	public AdminRegisterTimeWindow() {
		this("Gluggi",600,600);
	}
	
	public AdminRegisterTimeWindow(String name, int width, int hight) {
		super(name,width,hight);
		
		this.setResizable(true);
		this.setScrollbar(true);
		
	    this.setMarginHeight(0);
	    this.setMarginWidth(0);
	    this.setLeftMargin(0);
	    this.setTopMargin(0);
	    this.setAlinkColor("black");
	    this.setVlinkColor("black");
	    this.setLinkColor("black");
	    
		add(new AdminRegisterTime());
	}
}
