package com.idega.projects.hysing.templates;

import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.idegaweb.template.*;
import java.util.*;
import java.io.*;

public class HysingEditTemplate extends HysingTemplate{

  public String mainBoxHeader = "";

  public void initializePage(){
        HysingPage page = new HysingPage();
        page.setHeaderImageURL("/pics/headers/samstarfsadilar/samstarfsadilar_Topp.jpg");
        page.setMiddleImageURL("/pics/headers/samstarfsadilar/2samstarfs1a.gif");
        page.setMerged(true);
        page.setMainBoxHeader(mainBoxHeader);
        setPage(page);
  }

  public void setBoxHeader(String mainBoxHeader){
    this.mainBoxHeader = mainBoxHeader;
  }

}  // class HysingTemplate
