package com.idega.jmodule.client;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.idega.servlet.IWCoreServlet;

public class plainHTMLModule extends IWCoreServlet{



private Statement Stmt;
private ResultSet RS;




   	public void doGet( HttpServletRequest _req, HttpServletResponse _res) throws ServletException,IOException {

		doPost(_req,_res);
	}

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String text_id = req.getParameter("text_id");
		Connection Conn=null;
		try{




				//Get the specified databaseconnection from the PoolManager
				Conn = getConnection();
		        if (Conn == null){
		          	System.err.print("PlainHTMLModule: Can't get connection");
		          	return;
		        }


		    	Stmt = Conn.createStatement();
		        RS = Stmt.executeQuery("SELECT text_value from text where text_id='"+text_id+"'");

		        PrintWriter out = res.getWriter();

		        out.println(RS.getString("text_value"));


				// Clean up after ourselves
		        RS.close();
		        Stmt.close();


	    }
		catch (SQLException E) {
	    	System.err.print("In plainTextModule SQLException: " + E.getMessage());
	        System.err.print("In plainTextModule SQLState:     " + E.getSQLState());
	        System.err.print("In plainTextModule VendorError:  " + E.getErrorCode());
	    }
	     catch (Exception E) {
			//out.println("<br> Unable to load driver"+ "<br>");
			System.err.print("In plainTextModule Exceprion E");
			E.printStackTrace();
		}
		finally{
			//Clean-up
			freeConnection(Conn);
		}

   }



 }
