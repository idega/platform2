/*
 * Created on 14.4.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: MemberSearchWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class MemberSearchWindow extends GolfWindow {
	
	public MemberSearchWindow() {
		this(null);
	}
	
	public MemberSearchWindow(String name) {
		super();
		this.setWidth(600);
		this.setHeight(500);
		
		if(name != null) {
			this.setTitle(name);
		}
		this.add(new MemberSearch());
	}
	
	
	

}
