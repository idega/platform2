// idega 2000 - laddi
package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.idegaweb.service.HelpWindow;

public class HelpButton extends Link {

public static final String PARAMETERSTRING_HEADLINE = "headline";
public static final String PARAMETERSTRING_TEXT = "text";
public static final String PARAMETERSTRING_URL = "url";

  public HelpButton(String text) {
    this("",text,"");
  }

  public HelpButton(String headline, String text) {
    this(headline,text,"");
  }

  public HelpButton(String headline, String text, String imageURL) {
    super(new Image("/pics/help.gif"));
    this.addParameter(PARAMETERSTRING_HEADLINE,headline);
    this.addParameter(PARAMETERSTRING_TEXT,text);
    this.addParameter(PARAMETERSTRING_URL,imageURL);
    this.setWindowToOpen(HelpWindow.class);
    this.setName(text);
  }

}