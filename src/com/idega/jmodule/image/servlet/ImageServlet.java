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

  String imageId = request.getParameter("image_id");

  try{
    conn = (ImageEntity.getStaticInstance("com.idega.jmodule.image.data.ImageEntity")).getConnection();
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


    //  if(cacheImagesToFiles) cacheImageToFile(imageId);

   //   output.flush();
      output.close();
      myInputStream.close();
    }

    Stmt.close();
    RS.close();



  }
  catch (Exception E) {
      E.printStackTrace(System.err);
  }
  finally{
      (ImageEntity.getStaticInstance("com.idega.jmodule.image.data.ImageEntity")).freeConnection(conn);
  }


}//end service


private void cacheImageToFile(String imageId) throws Exception{

   //imageEntityHash;
  // scorecardHash;

 // ImageEntity image = new ImageEntity(Integer.parseInt(imageId));

}

}//end
