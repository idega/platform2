package com.idega.projects.lv.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;


public class LvWindowModule extends JSPModule implements JspPage{


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
