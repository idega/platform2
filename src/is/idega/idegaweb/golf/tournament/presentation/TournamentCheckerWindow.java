/*
 * Created on 23.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;


/**
 * Title: TournamentCheckerWindow
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TournamentCheckerWindow extends GolfWindow {

		/**
		 * 
		 */
		public TournamentCheckerWindow() {
			this("TournamentAdmin",700,500);
		}

		/**
		 * @param name
		 * @param width
		 * @param height
		 */
		public TournamentCheckerWindow(String name, int width, int height) {
			super(name, width, height);
			this.setGolfClassToInstanciate(TournamentChecker.class);
			this.setScrollbar(true);
		}

	}
