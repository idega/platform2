// idega - Gimmi & Eiki
package com.idega.projects.golf.startingtime.servlet.popupwidow;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.projects.golf.templates.page.JmoduleWindowModuleWindow;
import com.idega.projects.golf.startingtime.presentation.RegisterTime;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public class RegisterTeeTime extends com.idega.jmodule.JSPWindowModule {

  public void initializePage(){
    setPage(new RegisterTime());

  }

}
