// idega - Gimmi & Eiki
package is.idega.idegaweb.golf.templates;


import is.idega.idegaweb.golf.templates.page.GolfWindow;

import javax.servlet.http.HttpServletRequest;

import com.idega.builder.servlet.IBMainServlet;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.MenuBar;
import com.idega.presentation.ui.Window;


public abstract class JmoduleWindowModule extends IBMainServlet {

private Table tafla;
public String URI = null;
//8ab490
public String header_color ="#F2BC00";
public String color = "#F2BCFF";

private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";

private MenuBar Menu;
public String MenuAlignment = "&nbsp;&nbsp;&nbsp;&nbsp;";


	public void setWindow(GolfWindow window){
          setPage(window);
	}
	
	public IWContext getModuleInfo() {
		return getIWContext();
	}


	public void initializePage(){
          HttpServletRequest request = getRequest();
            if (request != null) {
              URI = request.getRequestURI();
           }

            setPage(new GolfWindow());
  /*
            Menu = new MenuBar();
            MenuBar();
            getPage().add(Menu);
  */
	}



	public Window getWindow(){
          return (Window) getPage();
	}


	public void add(PresentationObject objectToAdd){
          ((GolfWindow)getPage()).add(objectToAdd);
	}




        public void addToFileMenu(String ItemName, String Url){
          ((GolfWindow)getWindow()).addToFileMenu(ItemName, Url);
        }


        public void addToAddOnsMenu(String ItemName, String Url){
          ((GolfWindow)getWindow()).addToAddOnsMenu(ItemName, Url);
        }

        public void addToToolsMenu(String ItemName, String Url){
          ((GolfWindow)getWindow()).addToToolsMenu(ItemName, Url);
        }

        public void addToOptionsMenu(String ItemName, String Url){
          ((GolfWindow)getWindow()).addToOptionsMenu(ItemName, Url);
        }

        public void addToHelpMenu(String ItemName, String Url){
          ((GolfWindow)getWindow()).addToHelpMenu(ItemName, Url);
        }


        public MenuBar getMenu(){
          return ((GolfWindow)getWindow()).getMenu();
        }

        public boolean isAdmin(IWContext modinfo)throws Exception{
          return is.idega.idegaweb.golf.access.AccessControl.isAdmin(modinfo);
        }

  public boolean isDeveloper() {
    return is.idega.idegaweb.golf.access.AccessControl.isDeveloper(getModuleInfo());
  }

  public boolean isClubAdmin() {
    return is.idega.idegaweb.golf.access.AccessControl.isClubAdmin(getModuleInfo());
  }

  public boolean isClubWorker() {
    boolean ret;

    try {
      ret = is.idega.idegaweb.golf.access.AccessControl.isClubWorker(getModuleInfo());
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      ret = false;
    }

    return(ret);
  }

  public boolean isUser() {
    return is.idega.idegaweb.golf.access.AccessControl.isUser(getModuleInfo());
  }


  public IWResourceBundle getResourceBundle(){
     return getResourceBundle(getModuleInfo());
  }

  public IWBundle getBundle(){
    return getBundle(getModuleInfo());
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


  public void removeUnionIdSessionAttribute(IWContext modinfo){
    modinfo.removeSessionAttribute("golf_union_id");
  }

  public String getUnionID(IWContext modinfo){
    return (String)modinfo.getSessionAttribute("golf_union_id");
  }

  public void setUnionID(IWContext modinfo, String union_id){
    modinfo.setSessionAttribute("golf_union_id", union_id);
  }




}
