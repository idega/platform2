package com.idega.block.reports.presentation;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.idega.block.reports.business.ReportContent;
import com.idega.block.reports.business.ReportContentComparator;
import com.idega.block.reports.business.ReportMaker;
import com.idega.block.reports.business.ReportService;
import com.idega.block.reports.data.Report;
import com.idega.business.IWEventListener;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;


public class ReportContentViewer extends Block implements Reports,IWEventListener{

  private final String sAction = "rcv_action";
  private String sActPrm = "";
  private int iAction = 0;
  private String prefix = "rcv_";
  private Vector vReportContent;
	private List listReportContent;
  private String sLastOrder = "0";
  private int iReport = -1;
  private int displayNumber = 20;
  private Report eReport = null;
  private int listStart = 1;
 //public final static String prmReportId = "rep.viewer.repid";
  protected static String prmContent = "rep.view.content";
  protected static String prmHeaders = "rep.view.headers";
  protected static String prmLastOrder = "rep.view.lastorder";
  protected static String prmListStart = "rep.view.liststart";
	private IWBundle iwb;
	private IWResourceBundle iwrb;

  public ReportContentViewer(){
    listReportContent = null;
  }
  public ReportContentViewer(Vector vRC){
    listReportContent = vRC;
  }
  public ReportContentViewer(String sql){
    ReportMaker rm = new ReportMaker();
     listReportContent = rm.makeReport(sql);
  }

  public ReportContentViewer(Report R){
    this.eReport = R;
  }
  public void setDisplayNumber(int number){
    this.displayNumber = number;
  }

  public String getLocalizedNameKey(){
    return "report_content";
  }
  public String getLocalizedNameValue(){
    return "Content";
  }
  protected void control(IWContext iwc){
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
    Table  T = new Table();
    T.setWidth("100%");
    T.setCellpadding(0);
    T.setCellspacing(0);
    try{

      if(iwc.getParameter("start")!=null){
        listStart = Integer.parseInt(iwc.getParameter("start"));
      }
      iwc.setSessionAttribute(prmListStart,new Integer(listStart));

      if(iwc.getParameter(PRM_REPORTID)!=null){
        iReport = Integer.parseInt(iwc.getParameter(PRM_REPORTID));
        if(iReport > 0){
          eReport = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).findByPrimaryKeyLegacy(iReport);
          ReportService.setSessionReport(iwc,eReport);
          T.add(doMain(iwc));
        }
        else{
          if(ReportService.isSessionReport(iwc))
            ReportService.removeSessionReport(iwc);
          add("no report");
        }
      }
      else if(iwc.getParameter(sAction) != null){
        sActPrm = iwc.getParameter(sAction);
        try{
          iAction = Integer.parseInt(sActPrm);
          switch(iAction){
            case ACT1:    break;
            case ACT2: T.add(doTable(iwc));  break;
            case ACT3: doChange(iwc); break;
            case ACT4: doUpdate(iwc);        break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
      else
        T.add(doMain(iwc));
    }
    catch(Exception S){
      S.printStackTrace();
    }
    add(T);
  }

  protected PresentationObject makeLinkTable(int menuNr){
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
    if(true){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }


  private PresentationObject doMain(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("100%");
    if(iwc.getParameter(PRM_REPORTID) != null){
      String sql = eReport.getSQL();
      String[] headers = eReport.getHeaders();
      List v = new ReportMaker().makeReport(sql);
      iwc.setSessionAttribute(prmContent,v);
      iwc.setSessionAttribute(prmHeaders,headers);
      if(v != null){
        T.add(this.doHeader(eReport),1,1);
        T.add(this.doFooter(listStart,v.size(),eReport.getID()),1,2);
        T.add(this.doView(headers,v,listStart,eReport.getID()),1,3);
        T.add(this.doFooter(listStart,v.size(),eReport.getID()),1,4);
      }
      else
        T.add(new Text(" nothing to show"),1,1);
      //Link back =  new Link(new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
      //this.addToHeader(back);
    }
    else{
      return doTable(iwc);
    }
    return T;
  }
  protected void doChange(IWContext iwc) throws SQLException{

  }

  protected void doUpdate(IWContext iwc) throws SQLException{
  }

  private PresentationObject doTable(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("100%");
    if(iwc.getSession().getAttribute(prmContent)!=null){
      Vector v= (Vector) iwc.getSession().getAttribute(prmContent);
      eReport = ReportService.getSessionReport(iwc);
      listStart = ((Integer)iwc.getSessionAttribute(prmListStart)).intValue();
      String[] headers = null;
      if(iwc.getSessionAttribute(prmHeaders) != null){
        headers = (String[]) iwc.getSessionAttribute(prmHeaders);
      }

      if(iwc.getSessionAttribute(prmLastOrder)!=null)
        this.sLastOrder = (String) iwc.getSessionAttribute(prmLastOrder);
      else
        this.sLastOrder = "";

      String sOrder = "0";
      if(iwc.getParameter("order")!= null){
        sOrder = iwc.getParameter("order");
      }
      boolean reverse = false;
      if(this.sLastOrder.equalsIgnoreCase(sOrder))
        reverse = true;
      int order = Integer.parseInt(sOrder);

      if(!(iwc.getParameter("start")!= null))
        OrderVector(v,order,reverse);

      iwc.setSessionAttribute(prmLastOrder,sOrder);

      if(v != null){
        T.add(this.doHeader(eReport),1,1);
        T.add(this.doFooter(listStart,v.size(),eReport.getID()),1,2);
        T.add(this.doView(headers,v,listStart,eReport.getID()),1,3);
        T.add(this.doFooter(listStart,v.size(),eReport.getID()),1,4);
      }
      else
        T.add(new Text(" nothing to show"),1,1);
      //Link back =  new Link(new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
      //this.addToHeader(back);
    }
    return T;
  }

  private PresentationObject doHeader(Report R){
    Table T2 = new Table(2,1);
    T2.setWidth("100%");
    T2.setColumnAlignment(1,"left");
    T2.setColumnAlignment(2,"right");
    Table T = new Table(2,2);
    T.add(getBodyText("Name:"),1,1);
    T.add(getBodyText("Info:"),1,2);
    T.add(getBodyText(R.getName()),2,1);
    T.add(getBodyText(R.getInfo()),2,2);
    T2.add(T,1,1);
    Table T3 = new Table(3,1);

    Link XLS = Reporter.getXLSLink(iwb.getImage("/shared/xls.gif"),R.getID());
    Link PDF = Reporter.getPDFLink(iwb.getImage("/shared/pdf.gif"),R.getID());
    Link TXT = Reporter.getTXTLink(iwb.getImage("/shared/txt.gif"),R.getID());

    T3.add(XLS,1,1);
    T3.add(PDF,2,1);
    T3.add(TXT,3,1);
    T2.add(T3,2,1);
    return T2;
  }
  private PresentationObject doFooter(int start,int total,int reportId){
    Table T = new Table(5,1);
    T.setColor(this.DarkColor);
    T.setWidth("100%");
    T.setWidth(1,"25%");
    T.setWidth(5,"25%");
    T.setColumnAlignment(1,"left");
    T.setColumnAlignment(3,"center");
    T.setColumnAlignment(5,"right");
    int left = total%displayNumber;
    int nextstart = start+displayNumber;
    int nextend = nextstart + displayNumber-1;
    if(start != -1){
      if(!(start == 1)){
        Link leftLink = new Link("<< ");
        leftLink.addParameter("start",start-displayNumber);
        leftLink.addParameter(PRM_REPORTID,reportId);
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
        rightLink.addParameter("start",start+displayNumber);
        rightLink.addParameter(PRM_REPORTID,reportId);
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
      PartLink.addParameter("start",1);
      PartLink.addParameter(PRM_REPORTID,reportId);
      PartLink.setFontColor(this.LightColor);
      T.add(PartLink,2,1);

    }
    Link WholeLink = new Link("All");
    WholeLink.addParameter("start",-1);
    WholeLink.addParameter(PRM_REPORTID,reportId);
    WholeLink.setFontColor(this.LightColor);
    T.add(WholeLink,4,1);
    return T;
  }

  private PresentationObject doView(String[] headers,List content,int start,int reportId){
    int len = content.size();
    Table T;
		int depth = (len < displayNumber ? len : displayNumber)+1;
    if(start != -1)
      T= new Table(headers.length+1 ,depth+1);
    else
      T= new Table(headers.length+1 ,len+1);

    T.setWidth("100%");
    T.setWidth(1,"30");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setVerticalZebraColored(LightColor,MiddleColor);
    T.setRowColor(1,DarkColor);
    for(int j = 0; j < headers.length ;j++){
      Link L = new Link(getHeaderText(headers[j]));
      L.addParameter(PRM_REPORTID,reportId);
      L.addParameter(this.sAction,this.ACT2);
      L.addParameter("order",String.valueOf(j));
      L.setFontColor(WhiteColor);
      T.add(L,j+2,1);
    }

    ReportContent RC;
    int cols = headers.length;
    if(start != -1){
      int index = start;
      int end = start+displayNumber;
      for(int i =0; index < end && index <= len;i++){
        RC = (ReportContent)content.get((index)-1);
        for(int j = 0; j < cols;j++){
          T.add(getBodyText(RC.getContent(j)),j+2,i+2);
        }
        T.add(getBodyText(String.valueOf(index)),1,i+2);
        index++;
      }
    }
    else {
      int clen = content.size();
      for (int i = 0; i < clen; i++) {
        RC = (ReportContent)content.get(i);
        for(int j = 0; j < cols;j++){
          T.add(getBodyText(RC.getContent(j)),j+2,i+2);
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
    Text T = new Text(text,true,false,false);
    T.setFontColor("#000000");
    T.setFontSize(1);
    return T;
  }

  private void OrderVector(Vector mbs,int order,boolean reverse){
    ReportContentComparator RCC = new ReportContentComparator(order);
    if(reverse)
      Collections.reverse(mbs);
    else
      Collections.sort(mbs,RCC);
  }

  private Table makeTable(String[] header,String[][] content,int reportId){
    Table T= new Table();
    for(int j = 0; j < header.length ;j++){
      Link L = new Link(header[j]);
      L.addParameter(this.sAction,this.ACT2);
      L.addParameter(PRM_REPORTID,reportId);
      L.addParameter("order",String.valueOf(j));
      T.add(L,j+1,1);
    }
    for(int i =0; i < content.length;i++){
        for(int j = 0; j < content[i].length;j++){
          T.add(ReportPresentation.formatText(content[i][j]),j+1,i+2);
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

  protected static void removeSessionParameters(IWContext iwc){
    if(iwc.getSessionAttribute(prmContent)!=null){
      iwc.removeSessionAttribute(prmContent );
    }
    if(iwc.getSessionAttribute(prmHeaders)!=null){
      iwc.removeSessionAttribute(prmHeaders );
    }
    if(iwc.getSessionAttribute(prmLastOrder)!=null){
      iwc.removeSessionAttribute(prmLastOrder );
    }
    if(iwc.getSessionAttribute(prmListStart)!=null){
      iwc.removeSessionAttribute(prmListStart );
    }
  }

  public boolean actionPerformed(IWContext iwc){
    removeSessionParameters(iwc);
    return true;
  }

  public String getBundleIdentifier(){
    return REPORTS_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    control(iwc);
  }

}