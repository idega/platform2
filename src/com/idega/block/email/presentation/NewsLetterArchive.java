package com.idega.block.email.presentation;

import com.idega.block.email.business.*;
import com.idega.block.presentation.CategoryBlock;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import java.util.Collection;
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

  private String prmGroup = "em_nla_grp";
  private String prmTopic = "em_nla_tpc";

  private IWBundle iwb, core;
  private IWResourceBundle iwrb;

  private String LinkStyle = "";
  private String InfoStyle = "";

  private Collection topics;
  private Collection groups;
  private Collection letters;

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

    if(iwc.isParameterSet(prmGroup)){
      group = Integer.parseInt(iwc.getParameter(prmGroup));
    }

    if(iwc.isParameterSet(prmTopic)){
      topic = Integer.parseInt(iwc.getParameter(prmTopic));
    }

    if (getCategoryId() > 0) {
      groups = MailFinder.getInstance().getEmailGroups(getICObjectInstanceID());
      if(group > 0)
        topics = MailFinder.getInstance().getTopics(group);
      if(topic > 0)
        letters = MailFinder.getInstance().getEmailLetters(topic);
    }
    if(iwc.hasEditPermission(this)){
      T.add(getAdminView(iwc), 1, 1);
      T.setAlignment(1, row, "left");
    }
    T.add(getGroupLinks(),1,2);
    if(group > 0)
      T.add(getTopics(),1,3);
    if(topic > 0)
      T.add(getLetters(),1,4);

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



  private PresentationObject getGroupLinks(){
    Table T = new Table();
    if(groups!=null && groups.size() >0){
      Iterator iter = groups.iterator();
      EmailGroup grp;
      Link link;
      Text info;
      int row = 1;
      while(iter.hasNext()){
        grp = (EmailGroup) iter.next();
        link = new Link(grp.getName());
        link.addParameter(prmGroup,grp.toString());
        link.setFontStyle(LinkStyle);
        info = new Text (grp.getDescription());
        info.setFontStyle(InfoStyle);
        T.add(link,1,row++);
        T.add(info,1,row++);
        row++;
      }
    }

    return T;
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
        link = new Link(tpc.getName());
        link.addParameter(prmGroup,tpc.getGroupId());
        link.addParameter(prmTopic,tpc.toString());
        link.setFontStyle(LinkStyle);
        info = new Text(tpc.getDescription());
        info.setFontStyle(InfoStyle);
        T.add(link,1,row++);
        T.add(info,1,row++);
        row++;
      }
    }
    return T;
  }

  public PresentationObject getLetters(){
    Table T = new Table();
    if(letters!=null && letters.size() > 0){
      Iterator iter = letters.iterator();
      EmailLetter let;
      int row = 1;
      while(iter.hasNext()){
        let = (EmailLetter) iter.next();
        T.add(let.getFromAddress()+" "+let.getFromName(),1,row++);
        T.add(let.getSubject(),1,row++);
        T.add(let.getBody(),1,row++);
        row++;
      }
    }
    return T;
  }

}