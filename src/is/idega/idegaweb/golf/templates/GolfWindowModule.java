package is.idega.idegaweb.golf.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;


public class GolfWindowModule extends JSPModule implements JspPage{

    private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }
	public void setWindow(Window window){
		setPage(window);
	}




	public void initializePage(){
		setPage(new Window());
	}



	public Window getWindow(){
		return (Window) getPage();
	}

}
