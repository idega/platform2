package com.idega.jmodule.object.textObject;

import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.io.IOException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Hafthor Hilmarsson
 * @version 1.0
 *
 */

public class Anchor extends Link {

  public Anchor() {
    super("");
  }

  public Anchor(String text, String anchorname) {
    this(new Text(text),anchorname);
  }

   public Anchor(int text, int anchorname) {
    this(Integer.toString(text),Integer.toString(anchorname));
  }

  public Anchor(Text text, String anchorname) {
    super(text,anchorname);
  }

  public void setURL(String url){
	setAttribute("name",url);
  }

  public void print(ModuleInfo modinfo)throws Exception{
    initVariables(modinfo);
    ModuleObject obj = super.getObject();
    if (getLanguage().equals("HTML")){
      print("<a "+getAttributeString()+" >");
      if ( obj!=null) obj.print(modinfo);
      print("</a>");
    }
  }

  public synchronized Object clone() {
    Anchor obj = null;
    try {
      obj = (Anchor)super.clone();
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }


}