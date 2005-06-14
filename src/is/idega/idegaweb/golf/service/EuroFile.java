package is.idega.idegaweb.golf.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.golf.entity.Card;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Payment;
import is.idega.idegaweb.golf.entity.PaymentRound;
import is.idega.idegaweb.golf.entity.PaymentRoundHome;
import is.idega.idegaweb.golf.entity.PaymentType;
import is.idega.idegaweb.golf.entity.PaymentTypeHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import com.idega.util.IWTimestamp;

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

  public void makeFile(IWContext modinfo) throws SQLException,IOException, FinderException{
    euroFiling(modinfo,roundid,contract,addPercent,addAmount);
  }

  public String getFileLinkString(){
   return sFileLink;
  }

  public void setFileLinkString(String sFileLink){
    this.sFileLink = sFileLink;
  }

  public void writeFile(IWContext modinfo,Payment[] ePayments, String sContract, double dPercent, int iAmount, int iUnionId)throws SQLException,IOException, FinderException{
    String euroID = "2";

    StringBuffer SB = new StringBuffer();

    PaymentType PayType = ((PaymentTypeHome) IDOLookup.getHomeLegacy(PaymentType.class)).findByPrimaryKey(2);
    Union U = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(iUnionId);
    IWTimestamp datenow = new IWTimestamp();
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
    IWTimestamp cardexpiredate,startDate;
    Card card;
    String sCardNumber, sCardName,sCardKt,sCardExpires,sInstallments,sStartDate;
    String year,month,day,kt;
    int price,instm, newMemberID,oldMemberID=0;
    double total = 0.0;
    for(int i = 0; i < ePayments.length ;i++){
      SB = new StringBuffer();
      price = ePayments[i].getPrice();
      startDate = new IWTimestamp(ePayments[i].getPaymentDate());
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
        M = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(newMemberID);
        UMI = M.getUnionMemberInfo(iUnionId);
        card = UMI.getCard();
         if(card.getExpireDate()!= null){
          cardexpiredate = new IWTimestamp(card.getExpireDate());
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

  private void euroFiling(IWContext modinfo, int roundid, String sContract, double dPercent, int iAmount) throws SQLException,IOException, FinderException{
    String union_id = (String)  modinfo.getSession().getAttribute("golf_union_id");
    int un_id = Integer.parseInt(union_id)  ;

    String euroID = "2";

    StringBuffer SB = new StringBuffer();

    PaymentRound PR = ((PaymentRoundHome) IDOLookup.getHomeLegacy(PaymentRound.class)).findByPrimaryKey(roundid);

    String prID = String.valueOf(PR.getID());
    String paymRoundIDstring = String.valueOf(PR.getID());
    Payment[] payments = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAll("select * from payment where round_id = '"+prID+"' and status = 'N' and payment_type_id = '"+euroID+"' order by member_id ");

    PaymentType PayType = ((PaymentTypeHome) IDOLookup.getHomeLegacy(PaymentType.class)).findByPrimaryKey(2);
    Union U = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(un_id);
    IWTimestamp datenow = new IWTimestamp();

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
    IWTimestamp cardexpiredate,startDate;
    Card card;
    String sCardNumber, sCardName,sCardKt,sCardExpires,sInstallments,sStartDate;
    String year,month,day,kt;
    int price,instm, newMemberID,oldMemberID=0;
    double total = 0.0;
    for(int i = 0; i < payments.length ;i++){
      SB = new StringBuffer();
      price = payments[i].getPrice();
      startDate = new IWTimestamp(payments[i].getPaymentDate());
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
        M = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(newMemberID);
        UMI = M.getUnionMemberInfo(un_id);
        card = UMI.getCard();
        cardexpiredate = new IWTimestamp(card.getExpireDate());
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
