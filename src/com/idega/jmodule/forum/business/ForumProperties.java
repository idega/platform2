package com.idega.jmodule.forum.business;

import com.idega.presentation.*;
import com.idega.block.*;

/**
 * Title:        idegaForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */


public class ForumProperties extends BlockProperties{

  public static String BlockName = "Forum";

  public String[] IS = {"Umræðuflokkar","Þræðir","Uppfært síðast","Fyrirsögn","Skrá þráð","Efni ","Höfundur ","Svör","Sent","Nafn","Senda"};;
  public String[] EN = {"Forums","Threads","Last Updated","Headline","New Thread","Subject","Author","Answers","Sent","Name","Send"};


  public ForumProperties() {
  }



  public boolean useLogin(IWContext iwc){
/*    String url = iwc.getRequest().getRequestURI();
    if(url.startsWith("http://golf1.sidan") || url.startsWith("http://www.golf."))
      return false;
*/
    return true;
  }

  public boolean useEmail(IWContext iwc) {
    return false;
  }











}