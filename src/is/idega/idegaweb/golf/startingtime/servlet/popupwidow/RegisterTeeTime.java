// idega - Gimmi & Eiki
package is.idega.idegaweb.golf.startingtime.servlet.popupwidow;


import is.idega.idegaweb.golf.startingtime.presentation.RegisterTimeWindow;
import is.idega.idegaweb.golf.templates.JmoduleWindowModule;


public class RegisterTeeTime extends JmoduleWindowModule {

  public void initializePage(){
    setPage(new RegisterTimeWindow());

  }

}
