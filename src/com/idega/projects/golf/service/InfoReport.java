package com.idega.projects.golf.service;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.entity. *;
import com.idega.projects.golf.service.*;
import com.idega.util.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Table;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.idegaweb.*;
import java.sql.SQLException;
import com.idega.block.reports.business.*;
import com.idega.jmodule.object.JModuleObject;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/
public class InfoReport extends JModuleObject {

  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;
  private final int NOACT = 0;
  private int iAction;
  private String sAction = "list_action";
  private String sActPrm = "";
  private boolean isAdmin;

  private int paneWidth=600;
  private String boxName = "kassinn";
  private String[] sHandiCapLow = {};
  private String[] sHandiCapHigh = {};
  private String sUnionId = null;
  private int iUnionId = 1;
  private String sLastOrder;

  public InfoReport(){

  }

  private void control(ModuleInfo modinfo){

    try{

      if(modinfo.getSession().getAttribute("golf_union_id")!=null){
        sUnionId = (String)modinfo.getSession().getAttribute("golf_union_id");
        iUnionId = Integer.parseInt(sUnionId);
      }

      if(modinfo.getParameter(sAction) == null){
        doMain(modinfo);
      }
      if(modinfo.getParameter(sAction) != null){
        sActPrm = modinfo.getParameter(sAction);
        iAction = Integer.parseInt(sActPrm);
        switch(iAction){
        case ACT1: doTheShit(modinfo);     break;
        case ACT2: doTable(modinfo);      break;
        case ACT3:                        break;
        case ACT4:                        break;
        default:  doMain(modinfo);        break;
        }
      }
    }
    catch(Exception S){	S.printStackTrace();	}
    }

  private void doMain(ModuleInfo modinfo)  {

    add(new SubmitButton("Sækja",this.sAction,String.valueOf(ACT1)));
  }

  private void doUpdate(ModuleInfo modinfo){

  }

  private void doTheShit(ModuleInfo modinfo){
    try{
    UnionMemberInfo[] umi = (UnionMemberInfo[])new UnionMemberInfo().findAllByColumn("union_id",this.iUnionId);
    //UnionMemberInfo[] umi = (UnionMemberInfo[])new UnionMemberInfo().findAllByColumn("union_id",String.valueOf(this.iUnionId),"member_status","A");
    //UnionMemberInfo[] umi = (UnionMemberInfo[])new UnionMemberInfo().findAllByColumn("union_id",String.valueOf(this.iUnionId),"member_status","I");
    //UnionMemberInfo[] umi = (UnionMemberInfo[])new UnionMemberInfo().findAll("select * from union_member_info where union_id = "+iUnionId+" and payment_type_id = 2");
    int iMemberId;
    Member eMember;
    Address eAddress;
    ZipCode eZipCode;
    Account eAccount;
    PaymentType ePaymentType;
    Payment[] ePayment;
    int iPaid,iBalance;
    MemberInfo eMemberInfo;
    String[] sInfo = new String[9];
    ReportContent RC;
    Vector Content = new Vector();
    int len = umi.length;
    add(" lengd "+umi.length);
    sInfo[0] = "Nafn";
    sInfo[1] = "Kennitala";
    sInfo[2] = "Heimili";
    sInfo[3] = "Póstfang";
    sInfo[4] = "Forgjöf";
    sInfo[5] = "Álagning";
    sInfo[6] = "Greitt";
    sInfo[7] = "Staða";
    sInfo[8] = "Gerð";
    RC = new ReportContent(sInfo);
    Content.addElement(RC);
    for(int i = 0; i <  len ; i++){
      try{
        sInfo = new String[9];
        iMemberId = umi[i].getMemberID();
        System.err.print(" "+iMemberId);
        eMember = new Member(iMemberId);
        sInfo[0] = eMember.getName();
        sInfo[1] = eMember.getSocialSecurityNumber();
        try{ eAddress  = eMember.getAddress()[0];
        sInfo[2] = eAddress.getStreet();
        eZipCode = eAddress.getZipCode();
        sInfo[3] = eZipCode.getCode()+" "+eZipCode.getCity();
        }
        catch(Exception e){
          sInfo[2] = "";
          sInfo[3] = "";
        }
        eMemberInfo = eMember.getMemberInfo();
        try{
        sInfo[4] = String.valueOf(eMemberInfo.getHandicap());
        sInfo[4] += "_ ";
        }
        catch(Exception e){
          sInfo[4] = "";
        }
         try {
          ePayment = (Payment[])new Payment().findAllByColumn("member_id",String.valueOf(iMemberId),"status","Y");
          iPaid =0 ;
          for(int a = 0 ; a < ePayment.length;a++){
            iPaid += ePayment[a].getPrice();
          }
          sInfo[6] = String.valueOf(iPaid);
        }
        catch (Exception ex) {
          sInfo[6] = "";
          iPaid = 0;
        }
        try{
        eAccount = ((Account[])new Account().findAllByColumn("member_id",String.valueOf(iMemberId),"union_id",this.sUnionId))[0];
        iBalance = eAccount.getBalance();
        sInfo[7] = String.valueOf(iBalance);
        }
        catch(Exception e){
          iBalance = 0;
          sInfo[7] = "";
        }
        sInfo[5] = String.valueOf(iPaid - iBalance);
        ePaymentType = new PaymentType(umi[i].getPaymentTypeID());
        sInfo[8] = ePaymentType.getName();
        RC = new ReportContent(sInfo);
        Content.addElement(RC);
      }
      catch(SQLException sql){sql.printStackTrace();}
    }
    modinfo.getSession().setAttribute("content",Content);
    }
    catch(SQLException sql){
      sql.printStackTrace();
    }
    doTable(modinfo);
  }

  private void doTable(ModuleInfo modinfo){
    if(modinfo.getSession().getAttribute("content")!=null){
      Vector vContent = (Vector) modinfo.getSession().getAttribute("content");
      if(modinfo.getSession().getAttribute("lastorder")!=null)
        this.sLastOrder = (String) modinfo.getSession().getAttribute("lastorder");
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

        String[] headers = ((ReportContent) vContent.remove(0)).getWholeContent();
        //OrderVector(vContent,order,reverse);

        modinfo.getSession().setAttribute("lastorder",sOrder);
        add("Fjöldi "+vContent.size());
        String[][] s = this.makeStrings(vContent);
        //add(this.makeTable(s));
        makeXLS(modinfo,headers,s);

    }
  }

  private void makeXLS(ModuleInfo modinfo,String[] headers,String[][] content){
    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+"gsi/reports"+fileSeperator);
    StringBuffer fileName = new StringBuffer("haha");
    fileName.append(".xls");
    String st = filepath+fileName.toString();

    try{
    FileOutputStream fout = new FileOutputStream(st);

    ReportWriter.writeXLSReport(headers,content,fout);
    Link L = new Link("skrá","/excell?&dir="+st);
    }
    catch(Exception ex){ex.printStackTrace();}

  }

  private void OrderVector(Vector mbs,int order,boolean reverse){
    ReportContentComparator RCC = new ReportContentComparator(order);
    if(reverse)
      Collections.reverse(mbs);
    else
      Collections.sort(mbs,RCC);
  }

  private Table makeTable(String[][] s){
    Table T= new Table();
    for(int j = 0; j < s[0].length ;j++){
      Link L = new Link(s[0][j]);
      L.addParameter(this.sAction,this.ACT2);
      L.addParameter("order",String.valueOf(j));
      T.add(L,j+1,1);
    }
    for(int i =1; i< s.length;i++){
        for(int j = 0; j < s[i].length;j++){
          T.add(s[i][j],j+1,i+2);
      }
    }
    return T;
  }

  private String[][] makeStrings(Vector vContent){
    int len = vContent.size();
    ReportContent RC = (ReportContent) vContent.elementAt(0);
    int cols = RC.size();
    String[][] s = new String[len][cols];
    for(int i = 0; i < len; i++){
      RC = (ReportContent)vContent.elementAt(i);
      for(int j = 0; j < cols ;j++){
        s[i][j] = RC.getContent(j);
      }
    }
    return s;
  }

  public void main(ModuleInfo modinfo) {
    /* try{
      isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException e){
      isAdmin = false;
    }
    */
    isAdmin = true;
    control(modinfo);
  }
}//class MemberReport