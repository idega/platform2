package com.idega.jmodule;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;


public class JSPWindowModule extends JSPModule implements JspPage{


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
