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

public class Tester extends IWPresentationServlet {

  public Tester() {
  }

  public void __theService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    storeBlob();
  }

  public void storeBlob() {
    try {
      IBPage page = new IBPage();
      BlobWrapper wrap = new BlobWrapper(page,"page_value");

      wrap.setInputStreamForBlobWrite(new FileInputStream("c:\\programming\\source\\idega\\is\\idega\\experimental\\builder\\page2.xml"));

//      page.setID(3);
      page.setName("Test2");
      page.setPageValue(wrap);

      page.insert();
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
    catch(FileNotFoundException e2) {
      e2.printStackTrace();
    }
  }
}