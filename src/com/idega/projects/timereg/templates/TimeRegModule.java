package com.idega.projects.timereg.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.timereg.moduleobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import java.sql.*;

public class TimeRegModule extends JSPModule{

  public void initializePage(){
    setPage(new TimeRegPage());
	Page timaReg = getPage();
	timaReg.setMarginHeight(0);
	timaReg.setMarginWidth(0);
	timaReg.setLeftMargin(0);
	timaReg.setTopMargin(0);
	timaReg.setAlinkColor("black");
	timaReg.setVlinkColor("black");
	timaReg.setLinkColor("black");
	
	
  }
  
  public Connection getConnection()throws SQLException {
  	return getConnection("timereg");
  }
  
  
  
  public Connection getConnection(String connectionString)throws SQLException{
   	try{
    	Class.forName("org.gjt.mm.mysql.Driver").newInstance();  //loads the driver
    	//out.print("Driver Loaded Successfully!");
    	//String url = "jdbc:odbc:fred";
    	//DriverManager.getConnection(url, "userID", "passwd");
	}
	catch (Exception E) {
		//out.print("Unable to load driver");
		E.printStackTrace();
	}

  	String url = "jdbc:mysql://localhost:3306/"+connectionString;
	
	Connection Conn = null;
	Conn = DriverManager.getConnection(url,"root","crazy0nes");
    return Conn;            

  }
  
  public void addBreak() {
  	getPage().addBreak();
  }
  
  public void setLinkOfImage(String imageName,String link){
  	((TimeRegPage)getPage()).setLinkOfImage(imageName,link);
  }
  public void setLocation(String location) {
  	((TimeRegPage)getPage()).setLocation(location);
  }

  
 }
