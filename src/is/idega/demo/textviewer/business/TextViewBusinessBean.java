package is.idega.demo.textviewer.business;

import com.idega.business.IBOServiceBean;
import com.idega.block.media.business.MediaBusiness;

import com.idega.util.caching.Cache;
import java.io.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega hf
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class TextViewBusinessBean extends IBOServiceBean implements TextViewBusiness{

  public TextViewBusinessBean() {
  }


  public String getFileAsString(int fileID)throws FileNotFoundException,IOException{
    StringBuffer sbuffer = new StringBuffer();
    Cache cache = MediaBusiness.getCachedFileInfo(fileID,this.getIWApplicationContext().getApplication());
    InputStream in = new FileInputStream(cache.getRealPathToFile());
    InputStreamReader reader = new InputStreamReader(in);
    int bufflen = 10;
    char[] buf = new char[bufflen];

    int read = reader.read(buf);
    while (read!=-1) {
      sbuffer.append(buf);
      read = reader.read(buf);
    }
    in.close();
    reader.close();
    return sbuffer.toString();
  }

}