package is.idega.experimental.builder;

import com.idega.builder.data.IBPage;
import com.idega.data.BlobWrapper;
import com.idega.servlet.IWPresentationServlet;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.DataOutputStream;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Tester extends GenericServlet {

  public Tester() {
  }

  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    try {
//      IBPage page = new IBPage();
      //BlobWrapper wrap = new BlobWrapper(page,"page_value");

      //wrap.setInputStreamForBlobWrite(new FileInputStream("c:\\programming\\source\\idega\\is\\idega\\experimental\\builder\\page2.xml"));
//		InputStream stream = new FileInputStream("c:\\programming\\source\\idega\\is\\idega\\experimental\\builder\\page2.xml");

//      page.setID(3);
//      page.setName("Test2");
      //page.setPageValue(wrap);
//      page.setPageValue(stream);
//      page.insert();

      System.out.println("Getting page 23");

      IBPage page = new IBPage(23);
      InputStream myInputStream = page.getPageValue();
      if (myInputStream == null)
        System.out.println("empty string");

      java.io.FileOutputStream file = new java.io.FileOutputStream("c:\\temp.xml");
      int buffersize=1;
      byte	buffer[]= new byte[buffersize];
      int		noRead	= 0;
      while ( noRead != -1 ){
        noRead	= myInputStream.read( buffer, 0, buffersize );
        file.write(buffer);
      }
      file.close();


/*                  response.setContentType("text/xml");

               //   DataOutputStream output = new DataOutputStream( response.getOutputStream() );

                          int buffersize=1;
                          byte	buffer[]= new byte[buffersize];
                          int		noRead	= 0;

                          noRead	= myInputStream.read( buffer, 0, buffersize );
                          int i=0;
                          //Write out the file to the browser
                          while ( noRead != -1 ){
System.out.println("Reading stuff");
                 //                 output.write( buffer, 0, noRead );
                                  response.getWriter().print((char)buffer[0]);
                                  noRead	= myInputStream.read( buffer, 0, buffersize );

                          }



//                          output.flush();
//                          output.close();*/
                          myInputStream.close();




    }
    catch(SQLException e) {
      e.printStackTrace();
    }
    catch(FileNotFoundException e2) {
      e2.printStackTrace();
    }
  }
}