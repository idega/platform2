package com.idega.projects.golf.service;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.entity.Member;
import com.idega.projects.golf.entity.UnionMemberInfo;


public class MemberCommenter extends Editor{

  Member eMember = null;
  UnionMemberInfo eUMI = null;
  String AddToImageUrl = "/pics/formtakks/baetavid.gif";
  String ShiftImageUrl = "/pics/formtakks/skiptaut.gif";
  String CloseImageUrl = "/pics/formtakks/loka.gif";

  public MemberCommenter(){
  }

  protected void control(ModuleInfo modinfo){
    String member_id = modinfo.getParameter("member_id");
    String union_id = modinfo.getParameter("union_id");
    this.makeView();
    if(member_id != null && union_id != null){
      try{
      eMember = new Member(Integer.parseInt(member_id));
      eUMI = eMember.getUnionMemberInfo(Integer.parseInt(union_id));
      String comment = modinfo.getParameter("mc_comment")!=null?modinfo.getParameter("mc_comment"):"";
        if(modinfo.getParameter("adding")!=null || modinfo.getParameter("adding.x")!=null ){
          doAddToComment(eUMI,comment);
        }
        else if(modinfo.getParameter("shifting")!=null || modinfo.getParameter("shifting.x")!=null ){
          doShiftComment(eUMI,comment);
        }
        this.addLinks(formatText("Athugasemdir"));
        this.addMain(doView(eMember,eUMI));
        this.setBorder(2);

      }
      catch(SQLException sql){add("sql vandræði");}
    }
    else
      add("enginn valinn");
  }

  protected ModuleObject makeLinkTable(int menuNr){
    return new Text("");
  }

  private void doAddToComment(UnionMemberInfo eUmi,String comment){
    String temp = eUmi.getComment()!=null?eUmi.getComment():"";
    eUmi.setComment(temp + comment);
    try {
      eUmi.update();
    }
    catch (SQLException ex) {
      add("Tókst ekki að skrifa í gagnagrunn");
    }

  }
  private void doShiftComment(UnionMemberInfo eUmi,String comment){
    String temp = eUmi.getComment()!=null?eUmi.getComment():"";
    eUmi.setComment(comment);
    try {
      eUmi.update();
    }
    catch (SQLException ex) {
      add("Tókst ekki að skrifa í gagnagrunn");
    }
  }
  private ModuleObject doView(Member member, UnionMemberInfo umi){
    Table T = new Table();
    T.add(formatText("Nafn :"),1,1);
    T.add(formatText(member.getName()),1,1);
    T.add(formatText("Kt :"),1,2);
    T.add(formatText(member.getSocialSecurityNumber()),1,2);
    T.add(formatText("Athugasemd :"),1,3);
    String comment = umi.getComment();
    if(comment != null){
       T.add(formatText(comment ),1,4);
    }
    else{
      T.add(formatText("Ekkert skráð"),1,4);
    }
    TextArea TA = new TextArea("mc_comment");
    TA.setHeight(3);
    TA.setWidth(40);
    T.add(TA,1,6);
    SubmitButton adder    = new SubmitButton(new Image(AddToImageUrl),"adding");
    SubmitButton renewer  = new SubmitButton(new Image(ShiftImageUrl),"shifting","Skipta út");
    CloseButton closer    = new CloseButton(new Image(CloseImageUrl));
    T.add(adder,1,7);
    T.add(renewer,1,7);
    T.add(closer,1,7);
    T.add(new HiddenInput("member_id",String.valueOf(member.getID())));
    T.add(new HiddenInput("union_id",String.valueOf(umi.getUnionID())));
    Form myForm = new Form();
    myForm.add(T);

    return myForm;
  }
}
