// idega - Gimmi & Eiki
package com.idega.jmodule.template;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.projects.golf.templates.page.JmoduleWindowModuleWindow;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public abstract class JmoduleWindowModule extends JSPModule implements JspPage{

private Table tafla;
public String URI = null;
//8ab490
public String header_color ="#F2BC00";
public String color = "#F2BCFF";

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

private MenuBar Menu;
public String MenuAlignment = "&nbsp;&nbsp;&nbsp;&nbsp;";


	public void setWindow(JmoduleWindowModuleWindow window){
          setPage(window);
	}


	public void initializePage(){
          HttpServletRequest request = getRequest();
            if (request != null) {
              URI = request.getRequestURI();
           }

            setPage(new JmoduleWindowModuleWindow());
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
          ((JmoduleWindowModuleWindow)getPage()).add(objectToAdd);
	}




        public void addToFileMenu(String ItemName, String Url){
          ((JmoduleWindowModuleWindow)getWindow()).addToFileMenu(ItemName, Url);
        }


        public void addToAddOnsMenu(String ItemName, String Url){
          ((JmoduleWindowModuleWindow)getWindow()).addToAddOnsMenu(ItemName, Url);
        }

        public void addToToolsMenu(String ItemName, String Url){
          ((JmoduleWindowModuleWindow)getWindow()).addToToolsMenu(ItemName, Url);
        }

        public void addToOptionsMenu(String ItemName, String Url){
          ((JmoduleWindowModuleWindow)getWindow()).addToOptionsMenu(ItemName, Url);
        }

        public void addToHelpMenu(String ItemName, String Url){
          ((JmoduleWindowModuleWindow)getWindow()).addToHelpMenu(ItemName, Url);
        }


        public MenuBar getMenu(){
          return ((JmoduleWindowModuleWindow)getWindow()).getMenu();
        }

        public boolean isAdmin(IWContext iwc)throws Exception{
          return com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);
        }

  public boolean isDeveloper() {
    return com.idega.jmodule.login.business.AccessControl.isDeveloper(getIWContext());
  }

  public boolean isClubAdmin() {
    return com.idega.jmodule.login.business.AccessControl.isClubAdmin(getIWContext());
  }

  public boolean isClubWorker() {
    boolean ret;

    try {
      ret = com.idega.jmodule.login.business.AccessControl.isClubWorker(getIWContext());
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      ret = false;
    }

    return(ret);
  }

  public boolean isUser() {
    return com.idega.jmodule.login.business.AccessControl.isUser(getIWContext());
  }


  public IWResourceBundle getResourceBundle(){
     return getResourceBundle(getIWContext());
  }

  public IWBundle getBundle(){
    return getBundle(getIWContext());
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


  public void removeUnionIdSessionAttribute(IWContext iwc){
    iwc.removeSessionAttribute("golf_union_id");
  }

  public String getUnionID(IWContext iwc){
    return (String)iwc.getSessionAttribute("golf_union_id");
  }

  public void setUnionID(IWContext iwc, String union_id){
    iwc.setSessionAttribute("golf_union_id", union_id);
  }




}
