package com.idega.projects.golf.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.ModuleObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.templates.*;
import com.idega.projects.golf.*;
import com.idega.projects.*;
import com.idega.util.*;
import com.idega.io.*;
import java.math.*;
import com.idega.jmodule.*;
import java.text.DecimalFormat;
import java.text.NumberFormat.*;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/

public class EuroFile  {
  private String contract;
  private double addPercent;
  private int addAmount;
  private int roundid;
  private String sFileLink = "";

  public EuroFile(){}

  public EuroFile(int roundid,String contractNr ,double addPercent, int addAmount){
    this.contract = contractNr;
    this.addPercent = addPercent;
    this.addAmount = addAmount;
    this.roundid = roundid;
  }

  public void setContract(String contractNr){
    this.contract = contractNr;
  }

  public void setAddPercent(double addPercent){
    this.addPercent = addPercent;
  }

  public void setAddAmount(int addAmount){
    this.addAmount = addAmount;
  }

  public void setRoundID(int roundid){
    this.roundid = roundid;
  }

  public void makeFile(ModuleInfo modinfo) throws SQLException,IOException{
    euroFiling(modinfo,roundid,contract,addPercent,addAmount);
  }

  public String getFileLinkString(){
   return sFileLink;
  }

  public void setFileLinkString(String sFileLink){
    this.sFileLink = sFileLink;
  }

  public void writeFile(ModuleInfo modinfo,Payment[] ePayments, String sContract, double dPercent, int iAmount, int iUnionId)throws SQLException,IOException{
    String euroID = "2";

    StringBuffer SB = new StringBuffer();

    PaymentType PayType = new PaymentType(2);
    Union U = new Union(iUnionId);
    idegaTimestamp datenow = new idegaTimestamp();
    String sLowerCaseUnionAbbreviation = U.getAbbrevation().toLowerCase();

    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+sLowerCaseUnionAbbreviation+fileSeperator);
    StringBuffer fileName = new StringBuffer(sLowerCaseUnionAbbreviation);
    fileName.append(datenow.getDay());
    fileName.append(datenow.getMonth());
    fileName.append("euro.dat");

    String sWholeFileString = fileSeperator+fileName.toString();
    this.setFileLinkString(fileSeperator+sLowerCaseUnionAbbreviation+sWholeFileString);
    File outputFile = new File(filepath+sWholeFileString);
    FileWriter out = new FileWriter(outputFile);
    char[] c ;

    SB.append("\n\n\n");
    SB.append("Euro\n");
    SB.append(PayType.getName());SB.append("\n");
    SB.append(U.getName());SB.append("\t\t\t");SB.append(datenow.getISLDate(".", true));SB.append("\n");
    SB.append("Samningsnúmer:");SB.append(contract);SB.append("\n");
    SB.append("Kortnúmer\tGildi\tKennitala\tHeiti\tkorthafa\tFj.Gr.\tByrjar\tUpphæð");
    SB.append("\n");

     // write the first part to file
    c = SB.toString().toCharArray();
    out.write(c);

    Member M;
    UnionMemberInfo UMI;
    idegaTimestamp cardexpiredate,startDate;
    Card card;
    String sCardNumber, sCardName,sCardKt,sCardExpires,sInstallments,sStartDate;
    String year,month,day,kt;
    int price,instm, newMemberID,oldMemberID=0;
    double total = 0.0;
    for(int i = 0; i < ePayments.length ;i++){
      SB = new StringBuffer();
      price = ePayments[i].getPrice();
      startDate = new idegaTimestamp(ePayments[i].getPaymentDate());
      sStartDate = startDate.getISLDate(".", true);
      instm = ePayments[i].getTotalInstallment();
      sInstallments = String.valueOf(instm);
      newMemberID = ePayments[i].getMemberId();
      if(newMemberID == oldMemberID){
        total += price;
      }
      else{
        total += price;
        if(dPercent != 0.0)
          total = total * dPercent;
        else if (iAmount != 0)
          total = total + iAmount;
        M = new Member(newMemberID);
        UMI = M.getUnionMemberInfo(iUnionId);
        card = UMI.getCard();
         if(card.getExpireDate()!= null){
          cardexpiredate = new idegaTimestamp(card.getExpireDate());
          sCardExpires = cardexpiredate.getISLDate(".", true);
        }
        else
          sCardExpires = "null";

        sCardNumber = card.getCardNumber();
        sCardName = card.getName();
        sCardKt = card.getSocialSecurityNumber();
        SB.append(sCardNumber);SB.append("\t");
        SB.append(sCardExpires);SB.append("\t");
        SB.append(sCardKt);SB.append("\t");
        SB.append(sCardName);SB.append("\t");
        SB.append(sCardName);SB.append("\t");
        SB.append(sInstallments);SB.append("\t");
        SB.append(total/instm);SB.append("\t");
        SB.append("\n");
        total = 0.0;
        // write a line to file
        c = SB.toString().toCharArray();
        out.write(c);
      }
      oldMemberID = newMemberID;
    }

    out.close();

  }

  private void euroFiling(ModuleInfo modinfo, int roundid, String sContract, double dPercent, int iAmount) throws SQLException,IOException{
    String union_id = (String)  modinfo.getSession().getAttribute("golf_union_id");
    int un_id = Integer.parseInt(union_id)  ;

    String euroID = "2";

    StringBuffer SB = new StringBuffer();

    PaymentRound PR = new PaymentRound(roundid);

    String prID = String.valueOf(PR.getID());
    String paymRoundIDstring = String.valueOf(PR.getID());
    //Payment[] payments = (Payment[]) (new Payment()).findAllByColumn("status","N","payment_type_id",giroID );
    Payment[] payments = (Payment[]) (new Payment()).findAll("select * from payment where round_id = '"+prID+"' and status = 'N' and payment_type_id = '"+euroID+"' order by member_id ");

    PaymentType PayType = new PaymentType(2);
    Union U = new Union(un_id);
    idegaTimestamp datenow = new idegaTimestamp();

    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+fileSeperator+"files"+fileSeperator);
    StringBuffer fileName = new StringBuffer(U.getAbbrevation());
    fileName.append(datenow.getDay());
    fileName.append(datenow.getMonth());
    fileName.append("giro.dat");

    File outputFile = new File(filepath+fileSeperator+fileName.toString());
    FileWriter out = new FileWriter(outputFile);
    char[] c ;

    SB.append("\n\n\n");
    SB.append("Euro\n");
    SB.append(PayType.getName());SB.append("\n");
    SB.append(U.getName());SB.append("\t\t\t");SB.append(datenow.getISLDate(".", true));SB.append("\n");
    SB.append("Samningsnúmer:");SB.append(sContract);SB.append("\n");
    SB.append("Kortnúmer\tGildi\tKennitala\tHeiti\tkorthafa\tFj.Gr.\tByrjar\tUpphæð");
    SB.append("\n");

     // write the first part to file
    c = SB.toString().toCharArray();
    out.write(c);

    Member M;
    UnionMemberInfo UMI;
    idegaTimestamp cardexpiredate,startDate;
    Card card;
    String sCardNumber, sCardName,sCardKt,sCardExpires,sInstallments,sStartDate;
    String year,month,day,kt;
    int price,instm, newMemberID,oldMemberID=0;
    double total = 0.0;
    for(int i = 0; i < payments.length ;i++){
      SB = new StringBuffer();
      price = payments[i].getPrice();
      startDate = new idegaTimestamp(payments[i].getPaymentDate());
      sStartDate = startDate.getISLDate(".", true);
      instm = payments[i].getTotalInstallment();
      sInstallments = String.valueOf(instm);
      newMemberID = payments[i].getMemberId();
      if(newMemberID == oldMemberID){
        total += price;
      }
      else{
        total += price;
        if(dPercent != 0.0)
          total = total * dPercent;
        else if (iAmount != 0)
          total = total + iAmount;
        M = new Member(newMemberID);
        UMI = M.getUnionMemberInfo(un_id);
        card = UMI.getCard();
        cardexpiredate = new idegaTimestamp(card.getExpireDate());
        sCardExpires = cardexpiredate.getISLDate(".", true);
        sCardNumber = card.getCardNumber();
        sCardName = card.getName();
        sCardKt = card.getSocialSecurityNumber();
        SB.append(sCardNumber);SB.append("\t");
        SB.append(sCardExpires);SB.append("\t");
        SB.append(sCardKt);SB.append("\t");
        SB.append(sCardName);SB.append("\t");
        SB.append(sCardName);SB.append("\t");
        SB.append(sInstallments);SB.append("\t");
        SB.append(total/instm);SB.append("\t");
        SB.append("\n");
        total = 0.0;
        // write a line to file
        c = SB.toString().toCharArray();
        out.write(c);
      }
      oldMemberID = newMemberID;
    }

    out.close();

  }
}// class EuroFile
