/*
 * Created on 23.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import com.idega.presentation.ui.Window;


/**
 * Title: PrintStickersWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class PrintStickersWindow extends Window {

	/**
	 * 
	 */
	public PrintStickersWindow() {
		this("TournamentAdmin",650,500);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public PrintStickersWindow(String name, int width, int height) {
		super(name, width, height);
		add(new PrintStickers());
        this.setMenubar(true);
        this.setResizable(true);
        this.setScrollbar(true);
	}

}
