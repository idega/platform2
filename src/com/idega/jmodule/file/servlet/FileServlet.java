

package com.idega.jmodule.file.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import com.idega.jmodule.*;
import com.idega.servlet.IWCoreServlet;

public class FileServlet extends IWCoreServlet
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
		String file_name;
		String from_file;
		String file_location;


		if (myServletRequest.getParameter("generateHTMLCode") != null){

			generateHTMLCode(myServletRequest,myServletResponse);
			//generates the necessary HTML code to call the servlet via URL
		}
		else{

		String file_id = myServletRequest.getParameter("file_id");

		try{

			conn = getConnection();
			Stmt = conn.createStatement();
/*
       		RS = Stmt.executeQuery("SELECT * from file_ where file_id='"+file_id+"'");
			RS.next();
       		content_type = RS.getString("content_type");
       		file_name = RS.getString("file_name");
       		from_file = RS.getString("from_file"); //Indicator for if to get from file or DB.

       		BufferedOutputStream OS	= new BufferedOutputStream( myServletResponse.getOutputStream() );

       		myServletResponse.setContentType(content_type);

       		if (from_file.equals("Y")){

       			file_location = RS.getString("file_value");

				//generate a stream to read from file
				BufferedInputStream	 myInputStream = new BufferedInputStream( new FileInputStream( file_location ) );
				byte	buffer[]= new byte[1024];
				int		noRead	= 0;

				//Write out the file to the browser
				noRead	= myInputStream.read( buffer, 0, 1023 );
				while ( noRead != -1 )
				{
					OS.write( buffer, 0, noRead );
					noRead	= myInputStream.read( buffer, 0, 1023 );
				}

				//eiki
				myInputStream.close();


       		}
       		else{*/

				//ur blob.jsp
				RS = Stmt.executeQuery("select * from file_ where file_id='"+file_id+"'");
				Blob myBlob=null;
				String filename = null;
				InputStream	 myInputStream = null;
				//generate a stream to read from the DB
				//InputStream  myInputStream;


				while(RS.next()){

				//	myBlob = RS.getBlob("image_value");

				//	filename = RS.getString("image_name");
					content_type = RS.getString("content_type");

				/*if( myBlob == null ) System.out.println("Blobbið er NULL");
					else myInputStream = myBlob.getBinaryStream();*/

				myInputStream = RS.getBinaryStream("file_value");


				}

                               // System.out.println("Debug: FileModule; file content type : "+content_type);
                                //System.out.println("Debug: FileModule; file id : "+file_id);


				myServletResponse.setContentType(content_type);

				DataOutputStream output;


					output = new DataOutputStream( myServletResponse.getOutputStream() );


					byte	buffer[]= new byte[1024];
					int		noRead	= 0;

					noRead	= myInputStream.read( buffer, 0, 1023 );

					//Write out the file to the browser
					while ( noRead != -1 ){
						output.write( buffer, 0, noRead );
						noRead	= myInputStream.read( buffer, 0, 1023 );

					}



					output.flush();
					output.close();
					myInputStream.close();
			//}


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

	URIString = URIString+"?file_id="+request.getParameter("file_id");

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

	out.println("<a href=\""+URIString+"\"</a>");




}



}

//-------------
//- End of file
//-------------
