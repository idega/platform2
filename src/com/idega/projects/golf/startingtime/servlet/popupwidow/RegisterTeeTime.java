// idega - Gimmi & Eiki
package com.idega.projects.golf.startingtime.servlet.popupwidow;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.templates.page.JmoduleWindowModuleWindow;
import com.idega.projects.golf.startingtime.presentation.RegisterTime;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public abstract class RegisterTeeTime extends com.idega.jmodule.JSPWindowModule {

  public void initializePage(){
    setPage(new RegisterTime());
  }

}
