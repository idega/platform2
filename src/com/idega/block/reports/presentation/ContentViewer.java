package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collections;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Script;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.ModuleObjectContainer;


public class ContentViewer extends ModuleObjectContainer{

  private final String sAction = "rcv_action";
  private String sActPrm = "";
  private int iAction = 0;
  private String prefix = "rcv_";
  private Vector vContent;
  private String sLastOrder = "0";
  private int iReport = -1;
  private int displayNumber = 20;
  private Report eReport = null;
  private int listStart = 1;
  private String[] sTitles = null;
  private boolean allowOrder = true;
  private final String prmContent = "ctv.content";
  private final String prmStart = "ctv.start";
  private final String prmListStart = "ctv.liststart";
  private final String prmHeaders = "ctv.headers";
  private final String prmOrder = "ctv.order";
  private final String prmLastOrder = "ctv.lastorder";

  protected final static int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6=6;
  protected String MiddleColor,LightColor,DarkColor,WhiteColor,TextFontColor,HeaderFontColor,IndexFontColor;
  protected Table Frame,MainFrame,HeaderFrame;
  protected int BORDER = 0;
  protected String sHeader = null;
  protected int fontSize = 2;
  protected boolean fontBold = false;
  protected String styleAttribute = "font-size: 8pt";
  private int iBorder = 2;

  public ContentViewer(){
    vContent = null;
    sTitles = null;
    LightColor = "#D7DADF";
    MiddleColor = "#9fA9B3";
    DarkColor = "#27334B";
    WhiteColor = "#FFFFFF";
    TextFontColor = "#000000";
    HeaderFontColor = DarkColor;
    IndexFontColor = "#000000";

  }
  public ContentViewer(String[] Titles,Vector Content){
    this();
    sTitles = Titles;
    vContent = Content;
  }

  public void setColors(String LightColor,String MainColor,String DarkColor){
    if(LightColor.startsWith("#"))
      this.LightColor = LightColor;
    if(MainColor.startsWith("#"))
      this.MiddleColor = MainColor;
    if(DarkColor.startsWith("#"))
      this.DarkColor = DarkColor;
  }
  public void setBorder(int border){
    this.iBorder = border;
  }
  public void setHeaderText(String sHeader){
    this.sHeader = sHeader;
  }
  public void setTextFontColor(String color){
    this.TextFontColor = color;
  }
  public void setHeaderFontColor(String color){
    this.HeaderFontColor = color;
  }
  public void setIndexFontColor(String color){
    this.IndexFontColor = color;
  }
  public void setTextFontSize(int size){
    this.fontSize = size;
  }
  public void setTextFontBold(boolean bold){
    this.fontBold = bold;
  }
  public void setStyleAttribute(String style){
    this.styleAttribute = style;
  }
  public void setContent(Vector Content){
    vContent = Content;
  }
  public void setTitles(String[] Titles){
    sTitles = Titles;
  }
  public void setDisplayNumber(int number){
    this.displayNumber = number;
  }
  public void setAllowOrder(boolean allow){
    this.allowOrder = allow;
  }
  protected void control(ModuleInfo modinfo){
    try{

      if(modinfo.getParameter(prmStart)!=null){
        listStart = Integer.parseInt(modinfo.getParameter(prmStart));
      }
      modinfo.setSessionAttribute(prmListStart,new Integer(listStart));

      if(modinfo.getParameter(sAction) != null){
        sActPrm = modinfo.getParameter(sAction);
        try{
          iAction = Integer.parseInt(sActPrm);
          switch(iAction){
            case ACT1:    break;
            case ACT2: doTable(modinfo);  break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
      else
        doMain(modinfo);
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  protected ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);

    return LinkTable;
  }

  private void doMain(ModuleInfo modinfo){
    if(modinfo.getSessionAttribute(prmContent) == null){
      String[] headers = sTitles;
      Vector v = vContent;
      modinfo.setSessionAttribute(prmContent,v);
      modinfo.setSessionAttribute(prmHeaders,headers);
      if(v != null){
        add(this.doFooter(listStart,v.size()));
        add(this.doView(headers,v,listStart));
        add(this.doFooter(listStart,v.size()));
      }
      else
        add(new Text(" nothing to show"));
    }
    else{
      doTable(modinfo);
    }
  }

  private void doTable(ModuleInfo modinfo){
    if(modinfo.getSession().getAttribute( prmContent)!=null){
      Vector v= (Vector) modinfo.getSession().getAttribute(prmContent);
      eReport = ReportService.getSessionReport(modinfo);
      listStart = ((Integer)modinfo.getSessionAttribute(prmListStart)).intValue();
      String[] headers = null;
      if(modinfo.getSessionAttribute(prmHeaders) != null){
        headers = (String[]) modinfo.getSessionAttribute(prmHeaders);
      }
      if(allowOrder){
        if(modinfo.getSession().getAttribute(prmLastOrder)!=null)
          this.sLastOrder = (String) modinfo.getSessionAttribute(prmLastOrder);
        else
          this.sLastOrder = "";

        String sOrder = "0";
        if(modinfo.getParameter(prmOrder)!= null){
          sOrder = modinfo.getParameter(prmOrder);
        }
        boolean reverse = false;
        if(this.sLastOrder.equalsIgnoreCase(sOrder))
          reverse = true;
        int order = Integer.parseInt(sOrder);
        if(!(modinfo.getParameter(prmStart)!= null))
          OrderVector(v,order,reverse);
        modinfo.setSessionAttribute(prmLastOrder,sOrder);
      }

      if(v != null){
        add(this.doFooter(listStart,v.size()));
        add(this.doView(headers,v,listStart));
        add(this.doFooter(listStart,v.size()));
      }
      else
        add(new Text(" nothing to show"));
    }
  }

  private ModuleObject doFooter(int start,int total){
    Table T = new Table(5,1);
    T.setColor(this.DarkColor);
    T.setWidth("100%");
    T.setWidth(1,"25%");
    T.setWidth(5,"25%");
    T.setColumnAlignment(1,"left");
    T.setColumnAlignment(3,"center");
    T.setColumnAlignment(5,"right");
    int lastgroup = total/displayNumber;
    int left = total%displayNumber;
    int last = lastgroup + left;
    int laststart = lastgroup*displayNumber;
    int nextstart = start+displayNumber;
    int nextend = nextstart + displayNumber-1;
    if(start != -1){
      if(!(start == 1)){
        Link leftLink = new Link("<< ");
        leftLink.addParameter(prmStart,start-displayNumber);
        leftLink.setFontColor(this.LightColor);
        T.add(leftLink,1,1);
        T.add(getHeaderText((start-displayNumber)+"-"+(start-1)),1,1);
      }
      if(nextstart <= total){
        String interval;
        if(nextend > total){
          interval = nextstart + "-" +(nextstart+ left-1);
        }
        else{
          interval = nextstart+"-"+(nextstart+displayNumber-1);
        }
        T.add(getHeaderText(interval),5,1);
        Link rightLink = new Link(" >>");
        rightLink.addParameter(prmStart,start+displayNumber);
        rightLink.setFontColor(this.LightColor);
        T.add(rightLink,5,1);
      }
      if(!((nextstart-1) < total) )
        T.add(getHeaderText(start+"-"+(start+left-1)+" of "+total),3,1);
      else
        T.add(getHeaderText(start+"-"+(start+displayNumber-1)+" of "+total),3,1);
    }
    else{

      T.add(getHeaderText("Total:"+total),3,1);

      Link PartLink = new Link("Partial");
      PartLink.addParameter(prmStart,1);
      PartLink.setFontColor(this.LightColor);
      T.add(PartLink,2,1);

    }

    Link WholeLink = new Link("All");
    WholeLink.addParameter(prmStart,-1);
    WholeLink.setFontColor(this.LightColor);
    T.add(WholeLink,4,1);
    return T;
  }

  private ModuleObject doView(String[] headers,Vector content,int start){
    int len = content.size();
    Table T;
    if(start != -1)
      T= new Table(headers.length+1 ,displayNumber+1);
    else
      T= new Table(headers.length+1 ,len+1);

    T.setWidth("100%");
    T.setWidth(1,"30");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setVerticalZebraColored(LightColor,MiddleColor);
    T.setRowColor(1,DarkColor);
    for(int j = 0; j < headers.length ;j++){
      String Header = headers[j];
      if(allowOrder){
        Link L = new Link(Header);
        L.addParameter(this.sAction,this.ACT2);
        L.addParameter(prmOrder,String.valueOf(j));
        L.setFontColor(WhiteColor);
        T.add(L,j+2,1);
      }
      else{
        T.add(getHeaderText(Header),j+2,1);
      }

    }
    Content C;
    int cols = headers.length;
    if(start != -1){
      int index = start;
      int end = start+displayNumber;
      for(int i =0; index < end && index <= len;i++){
        C = (Content)content.elementAt((index)-1);
        for(int j = 0; j < cols;j++){
          T.add(getBodyText((C.getContent(j)).toString()),j+2,i+2);
        }
        T.add(getBodyText(String.valueOf(index)),1,i+2);
        index++;
      }
    }
    else {
      int clen = content.size();
      for (int i = 0; i < clen; i++) {
        C = (Content)content.elementAt(i);
        for(int j = 0; j < cols;j++){
          T.add(getBodyText((C.getContent(j)).toString()),j+2,i+2);
        }
        T.add(getBodyText(String.valueOf(i+1)),1,i+2);
      }

    }

    return T;
  }

  private Text getHeaderText(String text){
    Text T = new Text(text,true,false,false);
    T.setFontColor(WhiteColor);
    T.setFontSize(2);
    return T;
  }

  private Text getBodyText(String text){
    Text T = new Text(text);
    T.setFontColor( TextFontColor);
    T.setFontSize( fontSize);
    if(fontBold)
      T.setBold();
    return T;
  }

  private void OrderVector(Vector mbs,int order,boolean reverse){
    ContentComparator CC = new ContentComparator(order);
    if(reverse)
      Collections.reverse(mbs);
    else
      Collections.sort(mbs,CC);
  }

  private Table makeTable(String[] header,String[][] content){
    Table T= new Table();
    for(int j = 0; j < header.length ;j++){
      Link L = new Link(header[j]);
      L.addParameter(this.sAction,this.ACT2);
      L.addParameter( prmOrder,String.valueOf(j));
      T.add(L,j+1,1);
    }
    for(int i =0; i < content.length;i++){
        for(int j = 0; j < content[i].length;j++){
          T.add(content[i][j],j+1,i+2);
      }
    }
    return T;
  }

  private String[][] makeStrings(Vector vContent){
    int len = vContent.size();
    String[][] s = null;
    if(len > 0){
    ReportContent RC = (ReportContent) vContent.elementAt(0);
    int cols = RC.size();
      s = new String[len][cols];
      for(int i = 0; i < len; i++){
        RC = (ReportContent)vContent.elementAt(i);
        for(int j = 0; j < cols ;j++){
          s[i][j] = RC.getContent(j);
          //System.err.println(s[i][j]);
        }
      }
    }
    return s;
  }

  public void main(ModuleInfo modinfo){
    control(modinfo);
  }

}