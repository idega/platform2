package is.idega.experimental.builder;

import com.idega.builder.data.IBPage;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Tester extends GenericServlet {

  public Tester() {
  }

  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    loadImage();
  }

  public void saveImage() {
   try {
      IBPage page = new IBPage();
  		InputStream stream = new FileInputStream("c:\\programming\\source\\idega\\is\\idega\\experimental\\builder\\page2.xml");

      page.setName("Test2");
      page.setPageValue(stream);
      page.insert();
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
    catch(FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void loadImage() {
    try {
      System.out.println("Getting page 23");

      IBPage page = new IBPage(23);
      InputStream myInputStream = page.getPageValue();
      if (myInputStream == null)
        System.out.println("empty string");

      java.io.FileOutputStream file = new java.io.FileOutputStream("c:\\temp.xml");
      int buffersize=1;
      byte buffer[]= new byte[buffersize];
      int noRead	= 0;
      while (noRead != -1) {
        noRead = myInputStream.read(buffer,0,buffersize);
        file.write(buffer);
      }
      file.close();

      myInputStream.close();
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }
}

