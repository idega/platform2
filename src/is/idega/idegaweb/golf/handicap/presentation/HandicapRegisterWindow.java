/*
 * Created on 14.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.templates.page.GolfWindow;

import com.idega.presentation.IWContext;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HandicapRegisterWindow extends GolfWindow {

    public HandicapRegisterWindow() {
        setWidth(600);
        setHeight(600);
        setTitle("Handicap register");
        add(new HandicapRegister());
    }
    
    public void main(IWContext iwc) throws Exception {
  			super.main(iwc);
    		addHeading(_iwrb.getLocalizedString("handicap.register_scorecard", "Register scorecard"));
    }
}