/*
 * Created on 23.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import com.idega.presentation.ui.Window;


/**
 * Title: PrintStickersExcelWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class PrintStickersExcelWindow extends Window {

	/**
	 * 
	 */
	public PrintStickersExcelWindow() {
		this("TournamentAdmin",700,500);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public PrintStickersExcelWindow(String name, int width, int height) {
		super(name, width, height);
		this.add(new PrintStickersExcel());
	}

}
