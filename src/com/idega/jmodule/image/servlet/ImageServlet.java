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
import com.idega.util.caching.BlobCacher;
import com.idega.idegaweb.IWMainApplication;
import java.io.FileInputStream;

public class ImageServlet extends IWCoreServlet{

//private boolean cacheImagesToFiles = true;
//private HashTable imageEntityHash = new Hashtable();
//private Hashtable scorecardHash = new Hashtable();

public void doGet( HttpServletRequest _req, HttpServletResponse _res) throws IOException{
  doPost(_req,_res);
}

public void doPost( HttpServletRequest _req, HttpServletResponse _res) throws IOException{
  //IWMainApplication iwma = IWMainApplication.getIWMainApplication(getServletContext());
  Connection conn = null;
  Statement Stmt;
  ResultSet RS;

  HttpServletResponse response = _res;
  HttpServletRequest request = _req;
  String contentType=null;
  String imageName;
  String fromFile;
  String fileLocation;
  IWMainApplication app;

  String imageId = request.getParameter("image_id");

  try{
    if( imageId!=null){

      app = getApplication();
      String URIString = BlobCacher.getCachedUrl("com.idega.jmodule.image.data.ImageEntity",Integer.parseInt(imageId), app ,"image_value");

      if( URIString == null ){
        conn = (ImageEntity.getStaticInstance("com.idega.jmodule.image.data.ImageEntity")).getConnection();

        if( conn!=null ){
          Stmt = conn.createStatement();

          RS = Stmt.executeQuery("select image_value,content_type from image where image_id='"+imageId+"'");

          String filename = null;
          InputStream myInputStream = null;

          while(RS.next()){
            contentType = RS.getString("content_type");
            myInputStream = RS.getBinaryStream("image_value");
          }



          response.setContentType(contentType);

          if(myInputStream!=null){
            DataOutputStream output = new DataOutputStream( response.getOutputStream() );

            byte	buffer[]= new byte[1024];
            int		noRead	= 0;


            noRead	= myInputStream.read( buffer, 0, 1024 );

            //Write out the file to the browser
            while ( noRead != -1 ){
                    output.write( buffer, 0, noRead );
                    noRead = myInputStream.read( buffer, 0, 1024 );

            }

          output.flush();
          output.close();
          myInputStream.close();
          }

          Stmt.close();
          RS.close();
      }


    }
    else { //Stream from a file

      java.io.File file = new File(app.getApplicationRealPath()+URIString);
      file.getC


    }
  }


  }
  catch (Exception E) {
      E.printStackTrace(System.err);
  }
  finally{
      (ImageEntity.getStaticInstance("com.idega.jmodule.image.data.ImageEntity")).freeConnection(conn);
  }


}//end service


}//end
