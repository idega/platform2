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


public class ReportContentViewer extends Editor{

  private final String sAction = "rcv_action";
  private String sActPrm = "";
  private int iAction = 0;
  private String prefix = "rcv_";
  private Vector vReportContent;
  private String sLastOrder = "0";
  private int iReport = -1;
  private int displayNumber = 20;
  private Report eReport = null;
  private int listStart = 1;

  public ReportContentViewer(){
    vReportContent = null;
  }
  public ReportContentViewer(Vector vRC){
    vReportContent = vRC;
  }
  public ReportContentViewer(String sql){
    ReportMaker rm = new ReportMaker();
    vReportContent = rm.makeReport(sql);
  }

  public ReportContentViewer(Report R){
    this.eReport = R;
  }
  public void setDisplayNumber(int number){
    this.displayNumber = number;
  }
  protected void control(ModuleInfo modinfo){
    try{

      if(modinfo.getParameter("start")!=null){
        listStart = Integer.parseInt(modinfo.getParameter("start"));
        modinfo.setSessionAttribute("liststart",new Integer(listStart));
      }

      if(modinfo.getParameter("report")!=null){
        iReport = Integer.parseInt(modinfo.getParameter("report"));
        eReport = new Report(iReport);
        ReportService.setSessionReport(modinfo,eReport);
        doMain(modinfo);
      }
      else if(modinfo.getParameter(sAction) != null){
        sActPrm = modinfo.getParameter(sAction);
        try{
          iAction = Integer.parseInt(sActPrm);
          switch(iAction){
            case ACT1:    break;
            case ACT2: doTable(modinfo);  break;
            case ACT3: doChange(modinfo); break;
            case ACT4: doUpdate(modinfo);        break;
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
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(this.DarkColor);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link("Stilltur");
    Link1.setFontColor(this.LightColor);
    Link1.addParameter(this.sAction,String.valueOf(this.ACT1));
    Link Link2 = new Link("Spakur");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.sAction,String.valueOf(this.ACT2));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }


  private void doMain(ModuleInfo modinfo){
    if(modinfo.getParameter("report") != null){
      String sql = eReport.getSQL();
      String[] headers = eReport.getHeaders();
      Vector v = new ReportMaker().makeReport(sql);
      modinfo.setSessionAttribute(prefix+"content",v);
      modinfo.setSessionAttribute(prefix+"headers",headers);
      makeView();
      if(v != null){
        addMain(this.doView(headers,v,listStart));
        this.addHeader(this.doHeader(eReport));
        this.addMain(this.doFooter(listStart,v.size()));

      }
      else
        addMain(new Text(" nothing to show"));
    }
    else{
      doTable(modinfo);
    }
  }
  protected void doChange(ModuleInfo modinfo) throws SQLException{

  }

  protected void doUpdate(ModuleInfo modinfo) throws SQLException{
  }

  private void doTable(ModuleInfo modinfo){
    if(modinfo.getSession().getAttribute(prefix+"content")!=null){
      Vector v= (Vector) modinfo.getSession().getAttribute(prefix+"content");
      eReport = ReportService.getSessionReport(modinfo);
      listStart = ((Integer)modinfo.getSessionAttribute("liststart")).intValue();
      String[] headers = null;
      if(modinfo.getSessionAttribute(prefix+"headers") != null){
        headers = (String[]) modinfo.getSessionAttribute(prefix+"headers");
      }

      if(modinfo.getSession().getAttribute(prefix+"lastorder")!=null)
        this.sLastOrder = (String) modinfo.getSession().getAttribute(prefix+"lastorder");
      else
        this.sLastOrder = "";

      String sOrder = "0";
      if(modinfo.getParameter("order")!= null){
        sOrder = modinfo.getParameter("order");
      }
      boolean reverse = false;
      if(this.sLastOrder.equalsIgnoreCase(sOrder))
        reverse = true;
      int order = Integer.parseInt(sOrder);

      OrderVector(v,order,reverse);

      modinfo.getSession().setAttribute(prefix+"lastorder",sOrder);

      this.makeView();
      if(v != null){
        addMain(this.doView(headers,v,listStart));
        this.addHeader(this.doHeader(eReport));
        this.addMain(this.doFooter(listStart,v.size()));
      }
      else
        addMain(new Text(" nothing to show"));
    }
  }
  private ModuleObject doHeader(Report R){
    Table T = new Table(4,2);
    T.add(getBodyText("Name:"),1,1);
    T.add(getBodyText("Info:"),1,2);
    T.add(getBodyText(R.getName()),2,1);
    T.add(getBodyText(R.getInfo()),2,2);
    return T;
  }
  private ModuleObject doFooter(int start,int total){
    Table T = new Table(5,1);
    T.setColor(this.DarkColor);
    T.setWidth("100%");
    T.setColumnAlignment(1,"left");
    T.setColumnAlignment(3,"center");
    T.setColumnAlignment(5,"right");
    int lastgroup = total/displayNumber;
    int left = total%displayNumber;
    int last = lastgroup + left;
    int laststart = lastgroup*displayNumber;
    int nextstart = start+displayNumber;
    int nextend = nextstart + displayNumber-1;

    if(!(start == 1)){
      Link leftLink = new Link("<<");
      leftLink.addParameter("start",start-displayNumber);
      leftLink.setFontColor(this.LightColor);
      T.add(leftLink,1,1);
      T.add(getHeaderText((start-displayNumber)+"-"+(start-1)),1,1);
    }
    if(nextstart < laststart){
      String interval;
      if(nextend > total){
        interval = nextstart + "-" +(nextstart+ left-1);
      }
      else{
        interval = nextstart+"-"+(nextstart+displayNumber-1);
      }
      T.add(getHeaderText(interval),5,1);
      Link rightLink = new Link(">>");
      rightLink.addParameter("start",start+displayNumber);
      rightLink.setFontColor(this.LightColor);
      T.add(rightLink,5,1);
    }
    T.add(getHeaderText(start+"-"+(start+displayNumber-1)+":"+total),3,1);
    return T;
  }

  private ModuleObject doView(String[] headers,Vector content,int start){
    int len = content.size();
    Table T= new Table(headers.length+1 ,displayNumber+1);
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setVerticalZebraColored(LightColor,MiddleColor);
    T.setRowColor(1,DarkColor);
    for(int j = 0; j < headers.length ;j++){
      Link L = new Link(getHeaderText(headers[j]));
      L.addParameter(this.sAction,this.ACT2);
      L.addParameter("order",String.valueOf(j));
      L.setFontColor(this.LightColor);
      T.add(L,j+2,1);
    }

    ReportContent RC;
    int cols = headers.length;
    int index = start;
    for(int i =0; i < displayNumber ;i++){

      RC = (ReportContent)content.elementAt((index)-1);
      for(int j = 0; j < cols;j++){
        T.add(getBodyText(RC.getContent(j)),j+2,i+2);
      }
      T.add(getBodyText(String.valueOf(index)),1,i+2);
      index++;
    }
    return T;
  }

  private Text getHeaderText(String text){
    Text T = new Text(text,true,false,false);
    T.setFontColor(this.LightColor);
    T.setFontSize(2);
    return T;
  }

  private Text getBodyText(String text){
    Text T = new Text(text,true,false,false);
    T.setFontColor(this.DarkColor);
    T.setFontSize(2);
    return T;
  }

  private void OrderVector(Vector mbs,int order,boolean reverse){
    ReportContentComparator RCC = new ReportContentComparator(order);
    if(reverse)
      Collections.reverse(mbs);
    else
      Collections.sort(mbs,RCC);
  }

  private Table makeTable(String[] header,String[][] content){
    Table T= new Table();
    for(int j = 0; j < header.length ;j++){
      Link L = new Link(header[j]);
      L.addParameter(this.sAction,this.ACT2);
      L.addParameter("order",String.valueOf(j));
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

}