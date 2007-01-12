package com.idega.block.reports.presentation;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.idega.block.reports.business.Content;
import com.idega.block.reports.business.ContentComparator;
import com.idega.block.reports.business.ReportService;
import com.idega.block.reports.data.Report;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;


public class ContentViewer extends Block{

  private final String sAction = "rcv_action";
  private String sActPrm = "";
  private int iAction = 0;
  private String prefix = "rcv_";
	private List listOfContent = null;
  private String sLastOrder = "0";
  private int iReport = -1;
  private int displayNumber = 20;
  private Report eReport = null;
  private int listStart = 1;
  private String[] sTitles = null;
  private boolean allowOrder = true;
	private boolean clear = false;
  private final String prmContent = "ctv.content";
  private final String prmStart = "ctv.start";
  private final String prmListStart = "ctv.liststart";
  private final String prmHeaders = "ctv.headers";
  private final String prmOrder = "ctv.order";
  private final String prmLastOrder = "ctv.lastorder";
  private Link headerLinkToClone = new Link();
  private boolean isHeaderLinkCloned = false;

  {
    this.headerLinkToClone.setFontColor("#FFFFFF");
  }

  protected final static int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6=6;
  protected String MiddleColor,LightColor,DarkColor,WhiteColor,TextFontColor,HeaderFontColor,IndexFontColor;
  protected Table Frame,MainFrame,HeaderFrame;
  protected int BORDER = 0;
  protected String sHeader = null;
  protected int fontSize = 2;
  protected boolean fontBold = false;
  protected String styleAttribute = "font-size: 8pt";
  private int iBorder = 2;
	private int iInstId = -1;

  public ContentViewer(){
		this.clear = true;
		this.listOfContent = null;
    this.sTitles = null;
    this.LightColor = "#D7DADF";
    this.MiddleColor = "#9fA9B3";
    this.DarkColor = "#27334B";
    this.WhiteColor = "#FFFFFF";
    this.TextFontColor = "#000000";
    this.HeaderFontColor = this.DarkColor;
    this.IndexFontColor = "#000000";

  }
  public ContentViewer(String[] Titles,Vector Content){
    this();
    this.sTitles = Titles;
    this.listOfContent = Content;
  }

	public ContentViewer(String[] Titles,List listOfContent){
    this();
    this.sTitles = Titles;
    this.listOfContent = listOfContent;
  }

  public void setColors(String LightColor,String MainColor,String DarkColor){
    if(LightColor.startsWith("#")) {
		this.LightColor = LightColor;
	}
    if(MainColor.startsWith("#")) {
		this.MiddleColor = MainColor;
	}
    if(DarkColor.startsWith("#")) {
		this.DarkColor = DarkColor;
	}
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
		this.clear = true;
    this.listOfContent = Content;
  }
  public void setTitles(String[] Titles){
    this.sTitles = Titles;
  }
  public void setDisplayNumber(int number){
    this.displayNumber = number;
  }
  public void setAllowOrder(boolean allow){
    this.allowOrder = allow;
  }
  protected void control(IWContext iwc){
		this.iInstId = Math.abs(getICObjectInstanceID());
    try{

      if(iwc.getParameter(this.prmStart+this.iInstId)!=null){
        this.listStart = Integer.parseInt(iwc.getParameter(this.prmStart+this.iInstId));
				iwc.setSessionAttribute(this.prmStart+this.iInstId,String.valueOf(this.listStart));
      }
			else if(iwc.getSessionAttribute(this.prmStart+this.iInstId)!=null){
			  this.listStart = Integer.parseInt((String) iwc.getSessionAttribute(this.prmStart+this.iInstId));
			}

      iwc.setSessionAttribute(this.prmListStart+this.iInstId,new Integer(this.listStart));

      if(iwc.getParameter(this.sAction+this.iInstId) != null){
        this.sActPrm = iwc.getParameter(this.sAction+this.iInstId);
        try{
          this.iAction = Integer.parseInt(this.sActPrm);
          switch(this.iAction){
            case ACT1:    break;
            case ACT2: doTable(iwc);  break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
	else {
		doMain(iwc);
	}
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);

    return LinkTable;
  }

  private void doMain(IWContext iwc){
		if(this.clear && iwc.getSessionAttribute(this.prmContent+this.iInstId) != null) {
			iwc.removeSessionAttribute(this.prmContent+this.iInstId);
		}

    if(iwc.getSessionAttribute(this.prmContent+this.iInstId) == null){
      String[] headers = this.sTitles;
      List L = this.listOfContent;
      iwc.setSessionAttribute(this.prmContent+this.iInstId,L);
      iwc.setSessionAttribute(this.prmHeaders+this.iInstId,headers);
      if(L != null){
        add(this.doFooter(this.listStart,L.size()));
        add(this.doView(headers,L,this.listStart));
        add(this.doFooter(this.listStart,L.size()));
      }
	else {
		add(new Text(" nothing to show"));
	}
    }
    else{
      doTable(iwc);
    }
  }

  private void doTable(IWContext iwc){
    if(iwc.getSession().getAttribute( this.prmContent+this.iInstId)!=null){
      Vector v= (Vector) iwc.getSession().getAttribute(this.prmContent+this.iInstId);
      this.eReport = ReportService.getSessionReport(iwc);
      this.listStart = ((Integer)iwc.getSessionAttribute(this.prmListStart+this.iInstId)).intValue();
      String[] headers = null;
      if(iwc.getSessionAttribute(this.prmHeaders+this.iInstId) != null){
        headers = (String[]) iwc.getSessionAttribute(this.prmHeaders+this.iInstId);
      }
      if(this.allowOrder){
        if(iwc.getSession().getAttribute(this.prmLastOrder+this.iInstId)!=null) {
			this.sLastOrder = (String) iwc.getSessionAttribute(this.prmLastOrder+this.iInstId);
		}
		else {
			this.sLastOrder = "";
		}

        String sOrder = "0";
        if(iwc.getParameter(this.prmOrder)!= null){
          sOrder = iwc.getParameter(this.prmOrder+this.iInstId);
        }
        boolean reverse = false;
        if(this.sLastOrder.equalsIgnoreCase(sOrder)) {
			reverse = true;
		}
        int order = Integer.parseInt(sOrder);
        if(!(iwc.getParameter(this.prmStart+this.iInstId)!= null)) {
			OrderVector(v,order,reverse);
		}
        iwc.setSessionAttribute(this.prmLastOrder+this.iInstId,sOrder);
      }

      if(v != null){
        add(this.doFooter(this.listStart,v.size()));
        add(this.doView(headers,v,this.listStart));
        add(this.doFooter(this.listStart,v.size()));
      }
	else {
		add(new Text(" nothing to show"));
	}
    }
  }

  private PresentationObject doFooter(int start,int total){
    Table T = new Table(5,1);
    T.setColor(this.DarkColor);
    T.setWidth("100%");
    T.setWidth(1,"25%");
    T.setWidth(5,"25%");
    T.setColumnAlignment(1,"left");
    T.setColumnAlignment(3,"center");
    T.setColumnAlignment(5,"right");
    int left = total%this.displayNumber;
    int nextstart = start+this.displayNumber;
    int nextend = nextstart + this.displayNumber-1;
    if(start != -1){
      if(!(start == 1)){
        Link leftLink = new Link("<< ");
        leftLink.addParameter(this.prmStart+this.iInstId,start-this.displayNumber);
        leftLink.setFontColor(this.LightColor);
        T.add(leftLink,1,1);
        T.add(getHeaderText((start-this.displayNumber)+"-"+(start-1)),1,1);
      }
      if(nextstart <= total){
        String interval;
        if(nextend > total){
          interval = nextstart + "-" +(nextstart+ left-1);
        }
        else{
          interval = nextstart+"-"+(nextstart+this.displayNumber-1);
        }
        T.add(getHeaderText(interval),5,1);
        Link rightLink = new Link(" >>");
        rightLink.addParameter(this.prmStart+this.iInstId,start+this.displayNumber);
        rightLink.setFontColor(this.LightColor);
        T.add(rightLink,5,1);
      }
      if(!((nextstart-1) < total) ) {
		T.add(getHeaderText(start+"-"+(start+left-1)+" of "+total),3,1);
	}
	else {
		T.add(getHeaderText(start+"-"+(start+this.displayNumber-1)+" of "+total),3,1);
	}
    }
    else{

      T.add(getHeaderText("Total:"+total),3,1);

      Link PartLink = new Link("Partial");
      PartLink.addParameter(this.prmStart+this.iInstId,1);
      PartLink.setFontColor(this.LightColor);
      T.add(PartLink,2,1);

    }

    Link WholeLink = new Link("All");
    WholeLink.addParameter(this.prmStart+this.iInstId,-1);
    WholeLink.setFontColor(this.LightColor);
    T.add(WholeLink,4,1);
    return T;
  }

  private PresentationObject doView(String[] headers,List content,int start){
    int len = content.size();
    Table T;
    if(start != -1) {
		T= new Table(headers.length+1 ,this.displayNumber+1);
	}
	else {
		T= new Table(headers.length+1 ,len+1);
	}

    T.setWidth("100%");
    T.setWidth(1,"30");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setVerticalZebraColored(this.LightColor,this.MiddleColor);
    T.setRowColor(1,this.DarkColor);
    for(int j = 0; j < headers.length ;j++){
      Link HeaderLink = (Link) this.headerLinkToClone.clone();
      HeaderLink.setText(headers[j]);
      if(this.allowOrder){
        HeaderLink.addParameter(this.sAction,ContentViewer.ACT2);
        HeaderLink.addParameter(this.prmOrder+this.iInstId,String.valueOf(j));
        //L.setFontColor(WhiteColor);
        T.add(HeaderLink,j+2,1);
      }
      else{
        if (!this.isHeaderLinkCloned) {
          HeaderLink.setFontSize( 2);
          HeaderLink.setBold();
//          (Text) HeaderLink.getObject();
        }
        T.add( HeaderLink.getObject(), j+2, 1);
      }

    }
    Content C;
    int cols = headers.length;
    if(start != -1){
      int index = start;
      int end = start+this.displayNumber;
      for(int i =0; index < end && index <= len;i++){
        C = (Content)content.get((index)-1);
        for(int j = 0; j < cols;j++){
          T.add(getBodyObject((C.getContent(j))),j+2,i+2);
        }
        T.add(getBodyText(String.valueOf(index)),1,i+2);
        index++;
      }
    }
    else {
      int clen = content.size();
      for (int i = 0; i < clen; i++) {
        C = (Content)content.get(i);
        for(int j = 0; j < cols;j++){
          T.add(getBodyObject((C.getContent(j))),j+2,i+2);
        }
        T.add(getBodyText(String.valueOf(i+1)),1,i+2);
      }

    }

    return T;
  }

	private PresentationObject getBodyObject(Object Obj){
	  if(Obj instanceof PresentationObject) {
		return (PresentationObject)Obj;
	}
	else {
		return getBodyText(Obj.toString());
	}
	}

  private Text getHeaderText(String text){
    Text T = new Text(text,true,false,false);
    T.setFontColor(this.WhiteColor);
    T.setFontSize(2);
    return T;
  }

  private Text getBodyText(String text){
    Text T = new Text(text);
    T.setFontColor( this.TextFontColor);
    T.setFontSize( this.fontSize);
    if(this.fontBold) {
		T.setBold();
	}
    return T;
  }

  private void OrderVector(Vector mbs,int order,boolean reverse){
    ContentComparator CC = new ContentComparator(order);
    if(reverse) {
		Collections.reverse(mbs);
	}
	else {
		Collections.sort(mbs,CC);
	}
  }

  private Table makeTable(String[] header,String[][] content){
    Table T= new Table();
    for(int j = 0; j < header.length ;j++){
      Link L = new Link(header[j]);
      L.addParameter(this.sAction,ContentViewer.ACT2);
      L.addParameter( this.prmOrder+this.iInstId,String.valueOf(j));
      T.add(L,j+1,1);
    }
    for(int i =0; i < content.length;i++){
        for(int j = 0; j < content[i].length;j++){
          T.add(content[i][j],j+1,i+2);
      }
    }
    return T;
  }

  public void setHeaderLinkProperties(Link linkToClonePropertiesFrom){
    this.isHeaderLinkCloned = true;
    this.headerLinkToClone = linkToClonePropertiesFrom;
  }

  public void main(IWContext iwc){
    control(iwc);
  }

}
