//-------
//-
//- ascii2gif.java  ( Version 1 )
//-
//- Author:		AR Williamson
//- Book:		Special Edition: Server API
//- Chapter:	#14
//- Date:		April 1997
//- Copyright:	Maybe used for non-commercial use.
//-
//-------

//Changed march 2000, idega - Tryggvi Larusson

package com.idega.jmodule.client;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import com.idega.servlet.IWCoreServlet;

public class imageModule extends IWCoreServlet
{


	public void doGet( HttpServletRequest _req, HttpServletResponse _res) throws IOException{
		doPost(_req,_res);
	}

	public void doPost( HttpServletRequest _req, HttpServletResponse _res) throws IOException
	{

		Connection conn = null;


		Statement Stmt;
		ResultSet RS;

		HttpServletResponse myServletResponse = _res;
		HttpServletRequest myServletRequest = _req;
		String content_type=null;
		String image_name;
		String from_file;
		String file_location;


		if (myServletRequest.getParameter("generateHTMLCode") != null){

			generateHTMLCode(myServletRequest,myServletResponse);
			//generates the necessary HTML code to call the servlet via URL
		}
		else{

		String image_id = myServletRequest.getParameter("image_id");

		try{

                  conn = getConnection();
                  Stmt = conn.createStatement();

                  RS = Stmt.executeQuery("select * from image where image_id='"+image_id+"'");
                  Blob myBlob=null;
                  String filename = null;
                  InputStream	 myInputStream = null;

                  while(RS.next()){
                    content_type = RS.getString("content_type");
                    myInputStream = RS.getBinaryStream("image_value");
                  }

                  myServletResponse.setContentType(content_type);

                  DataOutputStream output;


                          output = new DataOutputStream( myServletResponse.getOutputStream() );


                          byte	buffer[]= new byte[1024];
                          int		noRead	= 0;

                          noRead	= myInputStream.read( buffer, 0, 1024 );

                          //Write out the file to the browser
                          while ( noRead != -1 ){
                                  output.write( buffer, 0, noRead );
                                  noRead	= myInputStream.read( buffer, 0, 1024 );

                          }



                          output.flush();
                          output.close();
                          myInputStream.close();

          Stmt.close();
          RS.close();



        }
        catch (SQLException E) {
        	PrintWriter out = _res.getWriter();
        	out.print(E.toString() );
        }

        catch (Exception E) {
            PrintWriter out = _res.getWriter();
        	out.print(E.toString() );
        }
		finally{
			freeConnection(conn);
		}
        }//end if (myServletRequest.getParameter("generateHTMLCode").equals("Y")

	}//end service


public void generateHTMLCode( HttpServletRequest request, HttpServletResponse response) throws IOException{
//Generates the necessary HTML code to call the servlet.
//Here is a possible bug, if a request parameter has more than one values.

	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	String URIString = request.getRequestURI();

	URIString = URIString+"?image_id="+request.getParameter("image_id");

	/*Enumeration Enum = request.getAttributeNames();
	boolean beginning = true;

	while( Enum.hasMoreElements() ){

		String NextParameterName =  Enum.nextElement().toString();

		if ( NextParameterName != "generateHTMLCode"){
			if (beginning)
			{URIString = URIString + "?"+NextParameterName+"="+request.getParameter(NextParameterName);}
			else
			{URIString = URIString + "&"+NextParameterName+"="+request.getParameter(NextParameterName);}
		}
		beginning = false;


	}*/

	out.println("<img src=\""+URIString);
	out.println("border=0 alt=\"\"></img>");



}

public InputStream getImageInputStream(int imageId)  throws SQLException{
  return getImageInputStream(Integer.toString(imageId));
}

public InputStream getImageInputStream(String imageId) throws SQLException{
  Connection Conn = null;
  Statement Stmt;
  ResultSet RS;
  InputStream myInputStream = null;

  Conn = getConnection();
  Stmt = Conn.createStatement();
  RS = Stmt.executeQuery("select image_value from image where image_id='"+imageId+"'");

  while(RS.next()){
      myInputStream = RS.getBinaryStream("image_value");
  }

  if( Stmt!=null ) Stmt.close();
  if( RS!=null ) RS.close();
  if( Conn!=null ) freeConnection(Conn);

  return myInputStream;

}

}//end
