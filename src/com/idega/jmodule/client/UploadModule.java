package com.idega.jmodule.client;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import com.idega.servlet.IWCoreServlet;

import com.oreilly.servlet.MultipartRequest;

public class UploadModule extends IWCoreServlet {


  public void doPost(HttpServletRequest request, HttpServletResponse response)
                                throws ServletException, IOException {

   	PrintWriter out = response.getWriter();
	Connection conn = null;

    MultipartRequest multi=null;
   /* String toDatabase ="";
    boolean writeToDB = false;
    String statement = "";


    Hashtable parameters= new Hashtable();

	//we need to make a new multipartobject to parse the multipart stream
	MultipartRequest multiParams;

	//multiParams = new MultipartRequest(...);
	multiParams = new MultipartRequest(request, ".", 5 * 1024 * 1024);

	//get the params from the multi part form
	//ATT: shorten this to only the needed params , toDatabase and statement
	Enumeration paramsFromRequest = multiParams.getParameterNames();

     while (paramsFromRequest.hasMoreElements()) {
        String name = (String)paramsFromRequest.nextElement();
        String value = (String) multiParams.getParameter(name);
        parameters.put(name,value);
          //out.println(name + " = " + value);
     }

     toDatabase = (String) parameters.get("todatabase");
     //out.println("breyta eitt : "+toDatabase);

     //toDatabase = "true";
    // statement = (String) parameters.get("statement");
     statement = "insert into images (image_name,image_value,content_type,from_file) values('mynd2', ? ,'image/gif','N')";


	if(toDatabase!=null){

		if(toDatabase.equals("false"))
		{
			writeToDB = false;
		}
		else
		{
			writeToDB = true;*/

			//conn = poolMgr.getConnection(DatabaseSource);

		//}

	//}

    try {

      // Blindly take it on faith this is a multipart/form-data request

      // Construct a MultipartRequest to help read the information.
      // Pass in the request, a directory to saves files to, and the
      // maximum POST size we should attempt to handle.
      // Here we (rudely) write to the server root and impose 5 Meg limit.


	/*if (writeToDB){

        if (conn==null){
			out.println("Can't get connection");
			return;
   		}*/
   		conn = getConnection();
   		multi = new MultipartRequest(request,conn,".", 5 * 1024 * 1024);
	/*}
	else{

		multi =  new MultipartRequest(request, ".", 5 * 1024 * 1024);
	}*/


	response.setContentType("text/html");

      out.println("<HTML>");
      out.println("<HEAD><TITLE>UploadTest</TITLE></HEAD>");
      out.println("<BODY>");
      out.println("<H1>UploadTest</H1>");

      // Print the parameters we received
      out.println("<H3>Params:</H3>");
      out.println("<PRE>");
      Enumeration params = multi.getParameterNames();
      while (params.hasMoreElements()) {
        String name = (String)params.nextElement();
        String value = multi.getParameter(name);
        out.println(name + " = " + value);
      }
      out.println("</PRE>");

      // Show which files we received
      out.println("<H3>Files:</H3>");
      out.println("<PRE>");
      Enumeration files = multi.getFileNames();
      while (files.hasMoreElements()) {
        String name = (String)files.nextElement();
        String filename = multi.getFilesystemName(name);
        String type = multi.getContentType(name);
        File f = multi.getFile(name);
        out.println("name: " + name);
        out.println("filename: " + filename);
        out.println("type: " + type);
        if (f != null) {
          out.println("length: " + f.length());
          out.println();
        }
        out.println("</PRE>");
      }
    }
    catch (Exception e) {
      out.println("<PRE>");
      e.printStackTrace(out);
      out.println("</PRE>");
    }
    finally{
    	freeConnection(conn);
    }



    out.println("</BODY></HTML>");



  }



}//end class
