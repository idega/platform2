package is.idega.idegaweb.campus.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.PresentationObject;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.SendMail;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class CampusRequests extends Block {

  private IWResourceBundle iwrb;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  private static String sAction = "reqs_act";
  private final static int REPAIR = 1,COMPUTER = 2,RESIGN = 3;
  private String tab = "\t";
  private String newline = "\n";
  private String columnwidth = "150";
  private int inputWidth = 40;

  public void control(IWContext iwc){
    int type = 0;
    if(iwc.isParameterSet(sAction))
      type = Integer.parseInt(iwc.getParameter(sAction));

    Table T = new Table();
    T.setWidth("100%");
    T.add(getLinks(),1,1);
    if(type > 0){
      if(iwc.isParameterSet("send")){
        T.add(formatText(processForm(type,iwc)),1,2);
      }
      else{
        T.add(getForm(type),1,2);
      }
    }

    add(T);
  }

  public PresentationObject getLinks(){
    Table T = new Table();
    T.setWidth("100%");
    T.setAlignment("center");
      T.add(getLink(REPAIR),1,1);
      T.add(getLink(COMPUTER),1,2);
      T.add(getLink(RESIGN),1,3);
    return T;
  }

  public Link getLink(int type){
    Link link = new Link(getSubject(type));
    link.setBold();
    link.setFontSize(4);
    link.addParameter(sAction,type);
    return link;
  }

  public PresentationObject getForm(int type){
    Form form = new Form();
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    int row = 1;
    T.add(new HorizontalRule(),1,row++);
    Text header = new Text(getSubject(type));
      header.setBold();
      header.setFontSize(4);
      T.setAlignment(1,row,"center");
    T.add(header,1,row++);
    T.add(getTenantFields(),1,row++);
    T.add(new HorizontalRule(),1,row++);
    T.add(getFields(type),1,row++);
    T.add(getButtons(),1,row++);
    T.add(new HorizontalRule(),1,row++);
    T.add(getCampusCommment(),1,row++);

    form.add(T);
    return form;
  }

  public PresentationObject getFields(int Type){
    PresentationObject obj = new Text();
    switch (Type) {
      case REPAIR: obj =  getRepairFields();  break;
      case COMPUTER: obj = getComputerFields(); break;
      case RESIGN: obj = getResignFields(); break;
    }
    return obj;
  }

  public String getEmail(int Type){
    String email = null;
    switch (Type) {
      case REPAIR: email =  "gunnar@fs.is";  break;
      case COMPUTER: email = "umsjon@fs.is"; break;
      case RESIGN: email = "iris@fs.is"; break;
    }
    return email;
  }

  public String getSubject(int Type){
    String subject = null;
    switch (Type) {
      case REPAIR: subject =  iwrb.getLocalizedString("repairrequest","Viðgerðarbeiðni");  break;
      case COMPUTER: subject = iwrb.getLocalizedString("computerrequest","Tölvuviðgerðarbeiðni"); break;
      case RESIGN: subject = iwrb.getLocalizedString("resignrequest","Uppsagnarbeiðni"); break;
    }
    return subject;
  }

  public PresentationObject getTenantFields(){
    Table T = new Table();
    T.add(formatText(iwrb.getLocalizedString("streetname","Götuheiti")),1,2);
    T.add(getTextInput("street",""),3,2);
    T.add(formatText(iwrb.getLocalizedString("streetname","Herb./Íbúð")),1,3);
    T.add(getTextInput("room",""),3,3);
    T.add(formatText(iwrb.getLocalizedString("tenantname","Nafn Leigutaka")),1,4);
    T.add(getTextInput("tenantname",""),3,4);
    T.add(formatText(iwrb.getLocalizedString("phone","Símanúmer Leigutaka")),1,5);
    T.add(getTextInput("phone",""),3,5);
    T.add(formatText(iwrb.getLocalizedString("email","Tölvupóstur Leigutaka")),1,6);
    T.add(getTextInput("email",""),3,6);
    T.setWidth(1,columnwidth);
    return T;
  }

  public PresentationObject getRepairFields(){
    Table T = new Table();
    T.add(formatText(iwrb.getLocalizedString("dateofcrash","Dagsetning bilunar")),1,2);
    TextInput dateOfCrash = getTextInput("dateofcrash","");
    dateOfCrash.setLength(40);
    T.add(dateOfCrash,3,2);
    T.add(formatText(iwrb.getLocalizedString("comment","Athugasemdir")),1,3);
    TextArea TA = getTextArea("comment","");

    T.add(TA,3,3);
    T.add(formatText(iwrb.getLocalizedString("daytime","Viðgerð má fara fram á dagvinnutíma, án þess að nokkur sé heima.Þriðjudagar eru almennir viðgerðardagar.")),3,4);
    T.add(new RadioButton("time","daytime"),1,4);
    T.add(formatText(iwrb.getLocalizedString("spectime","Ég óska eftir sérstakri tímasetningu og að viðgerð verði framkvæmd: ")),3,5);
    T.add(new TextInput("specialtime",""),3,5);
    T.add(new RadioButton("time","spectime"),1,5);
    T.add(new HiddenInput(sAction,String.valueOf(REPAIR)));
    T.setWidth(1,columnwidth);
    return T;
  }

   public PresentationObject getComputerFields(){
    Table T = new Table();
    T.add(formatText(iwrb.getLocalizedString("dateofcrash","Dagsetning bilunar")),1,2);
    T.add(getTextInput("dateofcrash",""),3,2);
    T.add(formatText(iwrb.getLocalizedString("comment","Athugasemdir")),1,3);
    T.add(getTextArea("comment",""),3,3);
    T.add(formatText(iwrb.getLocalizedString("spectime","Ég óska eftir sérstakri tímasetningu og að viðgerð verði framkvæmd: ")),1,4);
    T.add(getTextInput("specialtime",""),3,4);
    T.add(new HiddenInput(sAction,String.valueOf(COMPUTER)));
    T.setWidth(1,columnwidth);
    return T;
  }

  public PresentationObject getResignFields(){
    Table T = new Table();
    T.add(formatText(iwrb.getLocalizedString("movingdate","Áætlað að rýma herbergi/íbúð: ")),1,2);
    T.add(getTextInput("movingdate",""),3,2);
    T.add(formatText(iwrb.getLocalizedString("newaddress","Nýtt heimilisfang")),1,3);
    T.add(getTextInput("newaddress",""),3,3);
    T.add(formatText(iwrb.getLocalizedString("newzip","Nýtt póstfang")),1,4);
    T.add(getTextInput("newzip",""),3,4);
    T.add(formatText(iwrb.getLocalizedString("phone","Nýtt símanúmer")),1,5);
    T.add(getTextInput("phone",""),3,5);
    T.add(new HiddenInput(sAction,String.valueOf(RESIGN)));
    T.setWidth(1,columnwidth);
    return T;
  }

  private String processForm(int type,IWContext iwc){
    String tenantinfo = getTenantInfo(iwc);
    if(tenantinfo != null){
      StringBuffer info = new StringBuffer();
      String sinfo = getInfo(type,iwc);
      if(info != null){
        info.append(sinfo);
        info.append(iwrb.getLocalizedString("sendtime","Sent :"));
        info.append(idegaTimestamp.RightNow().getISLDate());
        try{
          SendMail.send("admin@campus.is",getEmail(type),"","aron@idega.is","mail.idega.is",getSubject(type),info.toString());
          return iwrb.getLocalizedString("requestsent","Beiðni hefur verið send !");
        }
        catch(Exception ex){
          ex.printStackTrace();
        }
        return iwrb.getLocalizedString("requestnotsent","Villa ,beiðni hefur ekki verið send !");
      }
      return iwrb.getLocalizedString("infoneeded2","Villa ,ekki nægar upplýsingar um leigjanda!");
    }
    return iwrb.getLocalizedString("infoneeded","Villa ,ekki nægar upplýsingar um leigjanda!");
  }

  private String getInfo(int type,IWContext iwc){
    String info = null;
    switch (type) {
      case REPAIR: info = getRepairInfo(iwc);        break;
      case RESIGN: info = getResignInfo(iwc);        break;
      case COMPUTER: info = getComputerInfo(iwc);        break;
    }
    return info;
  }

  private String getTenantInfo(IWContext iwc){
    StringBuffer info = new StringBuffer();
    String streetname = iwc.getParameter("street");
    if("".equals(streetname))
      return null;
    info.append(formatText(iwrb.getLocalizedString("streetname","Götuheiti")));
    info.append(tab);
    info.append(streetname);
    info.append(newline);
    String room = iwc.getParameter("room");
    if("".equals(room))
      return null;
    info.append(formatText(iwrb.getLocalizedString("streetname","Herb./Íbúð")));
    info.append(tab);
    info.append(room);
    info.append(newline);
    String tenantname = iwc.getParameter("tenantname");
    if("".equals(tenantname))
      return null;
    info.append(formatText(iwrb.getLocalizedString("tenantname","Nafn Leigutaka")));
    info.append(tab);
    info.append(tenantname);
    info.append(newline);
     String phone = iwc.getParameter("phone");
    if("".equals(phone))
      return null;
    info.append(formatText(iwrb.getLocalizedString("phone","Símanúmer Leigutaka")));
    info.append(tab);
    info.append(phone);
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("email","Tölvupóstur Leigutaka")));
    info.append(tab);
    info.append(iwc.getParameter("email"));
    info.append(newline);

    return info.toString();
  }

  private String getRepairInfo(IWContext iwc){
    StringBuffer info = new StringBuffer();
    info.append(formatText(iwrb.getLocalizedString("dateofcrash","Dagsetning bilunar")));
    info.append(tab);
    info.append(iwc.getParameter("dateofcrash"));
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("comment","Athugasemdir")));
    info.append(tab);
    info.append(iwc.getParameter("comment"));
    info.append(newline);

    String time = iwc.getParameter("time");
    if("daytime".equals(time)){
      info.append(formatText(iwrb.getLocalizedString("daytime","Viðgerð má fara fram á dagvinnutíma, án þess að nokkur sé heima.Þriðjudagar eru almennir viðgerðardagar.")));
    }
    else if("spectime".equals(time)){
      info.append(formatText(iwrb.getLocalizedString("spectime","Ég óska eftir sérstakri tímasetningu og að viðgerð verði framkvæmd: ")));
      info.append(iwc.getParameter("specialtime"));
    }
    info.append(newline);
    return info.toString();
  }

  private String getComputerInfo(IWContext iwc){
    StringBuffer info = new StringBuffer();
    info.append(formatText(iwrb.getLocalizedString("dateofcrash","Dagsetning bilunar")));
    info.append(tab);
    info.append(iwc.getParameter("dateofcrash"));
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("comment","Athugasemdir")));
    info.append(tab);
    info.append(iwc.getParameter("comment"));
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("spectime","Ég óska eftir sérstakri tímasetningu og að viðgerð verði framkvæmd: ")));
    info.append(iwc.getParameter("specialtime"));
    info.append(newline);

    return info.toString();
  }

  private String getResignInfo(IWContext iwc){
    StringBuffer info = new StringBuffer();
    info.append(formatText(iwrb.getLocalizedString("movingdate","Áætlað að rýma herbergi/íbúð: ")));
    info.append(tab);
    info.append(iwc.getParameter("movingdate"));
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("newaddress","Nýtt heimilisfang")));
    info.append(tab);
    info.append(iwc.getParameter("newaddress"));
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("newzip","Nýtt póstfang")));
    info.append(tab);
    info.append(iwc.getParameter("newzip"));
    info.append(newline);
    info.append(formatText(iwrb.getLocalizedString("phone","Nýtt símanúmer")));
    info.append(tab);
    info.append(iwc.getParameter("phone"));
    info.append(newline);
    return info.toString();
  }

  public PresentationObject getButtons(){
    Table T = new Table(1,1);
    T.setWidth("100%");
    T.add(new SubmitButton("send",iwrb.getLocalizedString("sendrequest","Senda beiðni")),1,1);
    T.setAlignment(1,1,"center");
    return T;
  }

  public Text getCampusCommment(){
    Text T = new Text("Staðfesting á móttöku beiðnar verður send í tölvupósti við fyrsta tækifæri. Ef staðfesting hefur ekki borist innan tveggja virkra daga þá vinsamlegast hafið samband við starfsfólk skrifstofu Stúdentagarða. ");
    T.setBold();
    T.setFontColor("#FF0000");
    return T;
  }

  public TextInput getTextInput(String name,String content){
    TextInput input = new TextInput(name,content);
    input.setLength(inputWidth);
    return input;
  }

  public TextArea getTextArea(String name,String content){
    TextArea input = new TextArea(name,content,inputWidth,5);
    return input;
  }


  public Text formatText(String text){
    Text T = new Text(text);
    T.setBold();
    return T;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    control(iwc);

  }
}