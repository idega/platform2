package is.idega.idegaweb.golf.service;

import com.idega.presentation.*;
import com.idega.presentation.PresentationObject.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import is.idega.idegaweb.golf.entity.*;
import is.idega.idegaweb.golf.templates.*;
import is.idega.idegaweb.golf.*;
import com.idega.util.*;
import java.text.DecimalFormat;
import java.text.NumberFormat.*;
import java.util.*;
import java.sql.*;
import java.io.*;
/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/

public class VisaFile  {
  private String contract;
  private double addPercent;
  private int addAmount;
  private int roundid;
  private String sFileLink = "";

  public VisaFile(){}

  public VisaFile(int roundid,String contractNr ,double addPercent, int addAmount){
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

  public void makeFile(IWContext iwc) throws SQLException,IOException{
    visaFiling(iwc,roundid,contract,addPercent,addAmount);
  }

   public void setRoundID(int roundid){
    this.roundid = roundid;
  }

  public String getFileLinkString(){
    return this.sFileLink;
  }

  public void setFileLinkString(String sFileLink){
    this.sFileLink = sFileLink;
  }

  public void writeFile(IWContext iwc,Payment[] ePayments, String sContract, double dPercent, int iAmount, int iUnionId)throws SQLException,IOException{
    String euroID = "3";

    StringBuffer SB = new StringBuffer();

    PaymentType PayType = new PaymentType(2);
    Union U = new Union(iUnionId);
    idegaTimestamp datenow = new idegaTimestamp();

    String sLowerCaseUnionAbbreviation = U.getAbbrevation().toLowerCase();
    String fileSeperator = System.getProperty("file.separator");
    String filepath = iwc.getServletContext().getRealPath(fileSeperator+sLowerCaseUnionAbbreviation+fileSeperator);
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
    SB.append("VÍSA\n");
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

  private void visaFiling(IWContext iwc,int roundid,String contract,double prosent, int amount) throws SQLException,IOException{
    String union_id = (String)  iwc.getSession().getAttribute("golf_union_id");
    int un_id = Integer.parseInt(union_id)  ;

    String euroID = "3";

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
    String filepath = iwc.getServletContext().getRealPath(fileSeperator+fileSeperator+"files"+fileSeperator);
    StringBuffer fileName = new StringBuffer(U.getAbbrevation());
    fileName.append(datenow.getDay());
    fileName.append(datenow.getMonth());
    fileName.append("giro.dat");

    File outputFile = new File(filepath+fileSeperator+fileName.toString());
    FileWriter out = new FileWriter(outputFile);
    char[] c ;

    SB.append("\n\n\n");
    SB.append("VÍSA\n");
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
    String cardnr, cardname,cardkt,cardexpires,installments,startd;
    String year,month,day,kt;
    int price,instm, newMemberID,oldMemberID=0;
    double total = 0.0;
    for(int i = 0; i < payments.length ;i++){
      SB = new StringBuffer();
      price = payments[i].getPrice();
      startDate = new idegaTimestamp(payments[i].getPaymentDate());
      startd = startDate.getISLDate(".", true);
      instm = payments[i].getTotalInstallment();
      installments = String.valueOf(instm);
      newMemberID = payments[i].getMemberId();
      if(newMemberID == oldMemberID){
        total += price;
      }
      else{
        total += price;
        if(prosent != 0.0)
          total = total * prosent;
        else if (amount != 0)
          total = total + amount;
        M = new Member(newMemberID);
        UMI = M.getUnionMemberInfo(un_id);
        card = UMI.getCard();
        if(card.getExpireDate()!= null){
          cardexpiredate = new idegaTimestamp(card.getExpireDate());
          cardexpires = cardexpiredate.getISLDate(".", true);
        }
        else
          cardexpires = "null";
        cardnr = card.getCardNumber();
        cardname = card.getName();
        cardkt = card.getSocialSecurityNumber();
        SB.append(cardnr);SB.append("\t");
        SB.append(cardexpires);SB.append("\t");
        SB.append(cardkt);SB.append("\t");
        SB.append(cardname);SB.append("\t");
        SB.append(cardname);SB.append("\t");
        SB.append(installments);SB.append("\t");
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

}// class VisaFile
