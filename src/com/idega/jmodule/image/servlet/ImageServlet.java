//Changed march 2000, idega - Tryggvi Larusson

package com.idega.jmodule.image.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import com.idega.servlet.IWCoreServlet;
import com.idega.idegaweb.IWMainApplication;
import com.idega.jmodule.image.data.ImageEntity;

public class ImageServlet extends IWCoreServlet
{
private boolean cacheImagesToFiles = true;

	public void doGet( HttpServletRequest _req, HttpServletResponse _res) throws IOException{
		doPost(_req,_res);
	}

	public void doPost( HttpServletRequest _req, HttpServletResponse _res) throws IOException
	{
                      //IWMainApplication iwma = IWMainApplication.getIWMainApplication(getServletContext());

		Connection conn = null;


		Statement Stmt;
		ResultSet RS;

		HttpServletResponse myServletResponse = _res;
		HttpServletRequest myServletRequest = _req;
		String contentType=null;
		String imageName;
		String fromFile;
		String fileLocation;


		if (myServletRequest.getParameter("generateHTMLCode") != null){

			generateHTMLCode(myServletRequest,myServletResponse);
			//generates the necessary HTML code to call the servlet via URL
		}
		else{

		String imageId = myServletRequest.getParameter("image_id");

		try{

                  conn = getConnection();
                  Stmt = conn.createStatement();

                  RS = Stmt.executeQuery("select * from image where image_id='"+imageId+"'");
                  Blob myBlob=null;
                  String filename = null;
                  InputStream myInputStream = null;

                  while(RS.next()){
                    contentType = RS.getString("content_type");
                    myInputStream = RS.getBinaryStream("image_value");
                  }

                  myServletResponse.setContentType(contentType);

                  DataOutputStream output;


                          output = new DataOutputStream( myServletResponse.getOutputStream() );


                          byte	buffer[]= new byte[1024];
                          int		noRead	= 0;

                          noRead	= myInputStream.read( buffer, 0, 1024 );

                          //Write out the file to the browser
                          while ( noRead != -1 ){
                                  output.write( buffer, 0, noRead );
                                  noRead = myInputStream.read( buffer, 0, 1024 );

                          }


//
                          if(cacheImagesToFiles) cacheImageToFile(imageId);

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

private void cacheImageToFile(String imageId) throws Exception{
  ImageEntity image = new ImageEntity(Integer.parseInt(imageId));

}

}//end
