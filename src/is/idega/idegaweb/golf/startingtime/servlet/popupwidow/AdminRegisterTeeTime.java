// idega - Gimmi & Eiki
package is.idega.idegaweb.golf.startingtime.servlet.popupwidow;


import is.idega.idegaweb.golf.startingtime.presentation.AdminRegisterTime;
import is.idega.idegaweb.golf.templates.JmoduleWindowModule;


public class AdminRegisterTeeTime extends JmoduleWindowModule {

  public void initializePage(){
    setPage(new AdminRegisterTime());
  }

}
