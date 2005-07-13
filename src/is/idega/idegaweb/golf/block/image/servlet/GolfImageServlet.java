//Changed march 2000, idega - Tryggvi Larusson

package is.idega.idegaweb.golf.block.image.servlet;

import is.idega.idegaweb.golf.block.image.data.ImageEntity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.servlet.IWCoreServlet;

public class GolfImageServlet extends IWCoreServlet{

public void doGet( HttpServletRequest _req, HttpServletResponse _res) throws IOException{
  doPost(_req,_res);
}

public void doPost( HttpServletRequest _req, HttpServletResponse _res) throws IOException{

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

        conn = (((IDOLegacyEntity)IDOLookup.instanciateEntity(ImageEntity.class))).getConnection();

        if( conn!=null ){
          Stmt = conn.createStatement();

          RS = Stmt.executeQuery("select image_value,content_type from image where image_id="+imageId);

          String filename = null;
          InputStream myInputStream = null;

		  RS.next();
          //while(RS.next()){
            contentType = RS.getString("content_type");
            myInputStream = RS.getBinaryStream("image_value");
          //}



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


  }
  catch (Exception E) {
      E.printStackTrace(System.err);
  }
  finally{
      ((IDOLegacyEntity)IDOLookup.instanciateEntity(ImageEntity.class)).freeConnection(conn);
  }


}//end service


}//end
