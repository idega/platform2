package is.idegaweb.campus.finance.presentation;


import com.idega.presentation.ui.Window;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.Script;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MessageWindow extends Window {

  public MessageWindow() {
    setWidth(200);
    setHeight(100);
    //setOnLoad("setTimeout('window.close',5000)");
    getAssociatedScript().addFunction("ip","var ip = '<!--#echo var=\"REMOTE_ADDR\"-->';  document.write(ip);");
    setAttribute("onBlur=\"self.focus()\"");
    setToClose(10000);



  }

  public void main(IWContext iwc){
    Text T = new Text(" Vinnsla í gangi");
    T.setFontSize(4);
    add(T);
  }
}
