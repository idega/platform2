package com.idega.block.email.presentation;

import com.idega.block.email.business.*;
import com.idega.block.presentation.CategoryBlock;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.util.text.TextSoap;
import com.idega.presentation.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class NewsLetterArchive extends CategoryBlock {

  private int group = -1;
  private int topic = -1;
  private int letter = -1;

  private String prmLetter = "em_nla_let";
  private String prmTopic = "em_nla_tpc";

  private IWBundle iwb, core;
  private IWResourceBundle iwrb;

  private String LinkStyle = "";
  private String InfoStyle = "";

  private Collection topics;
  private Collection groups;
  private Collection letters;
  
  private EmailLetter theLetter;
  private DateFormat df ;

  public static String EMAIL_BUNDLE_IDENTIFIER = "com.idega.block.email";

  public NewsLetterArchive() {
    setAutoCreate(false);
  }
  public String getCategoryType() {
    return "Newsletter";
  }
  public boolean getMultible() {
    return true;
  }

  public String getBundleIdentifier() {
    return EMAIL_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwb = getBundle(iwc);
    core = iwc.getApplication().getCoreBundle();
    iwrb = getResourceBundle(iwc);
    Table T = new Table();
    int row = 1;
	df = DateFormat.getDateInstance(DateFormat.LONG,iwc.getCurrentLocale());
   
    if(iwc.isParameterSet(prmTopic)){
      topic = Integer.parseInt(iwc.getParameter(prmTopic));
    }
    if(iwc.isParameterSet(prmLetter)){
      letter = Integer.parseInt(iwc.getParameter(prmLetter));
    }


    if (getCategoryId() > 0) {
      topics = MailFinder.getInstance().getTopics(this.getICObjectInstanceID());
      if(topic > 0)
        letters = MailFinder.getInstance().getEmailLetters(topic);
      	
    }
    if(iwc.hasEditPermission(this)){
      T.add(getAdminView(iwc), 1, 1);
      T.setAlignment(1, row, "left");
      T.mergeCells(1,1,2,1);
    }
    if(topics!=null && !topics.isEmpty()){
    	T.add(getTopics(),1,2);
    	if(topic < 0)
    		topic = Integer.parseInt(((EmailTopic) topics.iterator().next()).toString());	
    }
   
    if(theLetter !=null)
      T.add(getLetter(),2,2);
      
    T.setAlignment(1,2,T.VERTICAL_ALIGN_TOP);
    T.setAlignment(2,2,T.VERTICAL_ALIGN_TOP);

    Form F = new Form();
    F.add(T);
    add(F);
  }

  private PresentationObject getAdminView(IWContext iwc) {
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellpadding(0);
    T.add(getCategoryLink(core.getImage("/shared/detach.gif")), 1, 1);

    return T;
  }

  private Link getCategoryLink(Image image) {
    Link L = getCategoryLink();
    L.setImage(image);
    return L;
  }

  private PresentationObject getTopics(){
    Table T = new Table();
    if(topics!=null && topics.size() > 0){
      Iterator iter = topics.iterator();
      EmailTopic tpc;
      Link link;
      Text info;
      int row =1;
      while(iter.hasNext()){
        tpc = (EmailTopic) iter.next();
        int id = Integer.parseInt(tpc.toString());
        link = getStyleLink(tpc.getName(),SN_TOPIC);
        link.addParameter(prmTopic,tpc.toString());

        T.add(link,1,row);
        if(topic > 0 && topic == id)
      		T.add(getLettersHeads(),1,row++);
      }
    }
    return T;
  }

   public PresentationObject getLetter(){
    Table T = new Table();
     T.add(getStyleText(theLetter.getSubject(),SN_SUBJ),1,1);
     T.mergeCells(1,1,2,1);
     T.add(getStyleText( iwrb.getLocalizedString("from","From")+":",SN_TITLE),1,2);
     T.add(getStyleText(iwrb.getLocalizedString("date","Date")+":",SN_TITLE),1,3);
     
     T.add(getStyleText(theLetter.getFromName()+"[ "+theLetter.getFromAddress()+" ]",SN_FROM),2,2);
     T.add(getStyleText(df.format(theLetter.getCreated()),SN_DATE),2,3);
     T.add(Text.getBreak(),1,4);
  	
     String body = TextSoap.findAndReplace( theLetter.getBody(),"\n","<br>");
     T.mergeCells(1,5,2,5);
     T.add(getStyleText(body,SN_BODY),1,5);

    return T;
  }
  
   public PresentationObject getLettersHeads(){
    Table T = new Table();
    if(letters!=null && letters.size() > 0){
      Iterator iter = letters.iterator();
      EmailLetter let;
      int row = 1;
      T.add(getStyleText(iwrb.getLocalizedString("from","From"),SN_TITLE),1,row);
      T.add(getStyleText(iwrb.getLocalizedString("subject","Subject"),SN_TITLE),2,row);
      row++;
      Link link;
      while(iter.hasNext()){
        let = (EmailLetter) iter.next();
        int id = Integer.parseInt(let.toString());
        if(id==letter)
        	theLetter = let;
        link = getStyleLink( let.getSubject(),SN_SUBJ);
        link.addParameter(prmTopic,String.valueOf(topic));
        link.addParameter(prmLetter,String.valueOf(let.toString()));
        T.add(link,1,row);
        T.add(getStyleText(let.getFromName(),SN_FROM),2,row);
        row++;
      }
    }
    return T;
  }
  
  public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { 
			SN_TOPIC ,
			SN_FROM ,
			SN_SUBJ,
			SN_DATE ,
			SN_BODY,
			SN_TITLE
		
		};
		String[] styleValues = { 
			DEF_TOPIC ,
			DEF_FROM,
			DEF_SUBJ,
			DEF_DATE,
			DEF_BODY,
			DEF_TITLE
			
	 	};
	 	for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
  }
	 	
	 	public final static String SN_TOPIC = "Topic";
		public final static String SN_FROM = "From";
		public final static String SN_SUBJ = "Subject";
		public final static String SN_DATE = "Date";
		public final static String SN_BODY = "Body";
		public final static String SN_TITLE = "Title";
		
	
		public final static String DEF_TOPIC = "font-style:normal;color:#000000;font-size:13px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
		public final static String DEF_FROM ="font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight;";
		public final static String DEF_SUBJ = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight;";
		public final static String DEF_DATE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight;";
		public final static String DEF_BODY="font-weight:plain;"; 
		public final static String DEF_TITLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	
}
