package is.idega.idegaweb.golf.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Payment;
import is.idega.idegaweb.golf.entity.PaymentHome;
import is.idega.idegaweb.golf.entity.PaymentRound;
import is.idega.idegaweb.golf.entity.PaymentRoundHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import com.idega.util.IWTimestamp;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/

public class GiroFile  {
  private int bankoffice,finalpayday;
  private String giroB1, giroB2, giroB3,  giroB4;
  private int roundid;
  private String sFileLink = "";

  public GiroFile(){}

  public GiroFile(int roundid,int bankoffice,int finalpayday,String giroB1,String giroB2, String giroB3, String giroB4){
    this.bankoffice = bankoffice;
    this.finalpayday = finalpayday;
    this.giroB1 = giroB1;
    this.giroB2 = giroB2;
    this.giroB3 = giroB3;
    this.giroB4 = giroB4;
    this.roundid = roundid;
  }

  public void setBankOffice(int bankoffice){
    this.bankoffice = bankoffice;
  }
  public void setFinalPayDay(int finalpayday){
    this.finalpayday = finalpayday;
  }
  public void setGiroTextB1(String giroB1){
    this.giroB1 = giroB1;
  }
  public void setGiroTextB2(String giroB2){
    this.giroB2 = giroB2;
  }
  public void setGiroTextB3(String giroB3){
    this.giroB3 = giroB3;
  }
  public void setGiroTextB4(String giroB4){
    this.giroB4 = giroB4;
  }

  public void setRoundID(int roundid){
    this.roundid = roundid;
  }

  public void makeFile(IWContext modinfo) throws SQLException,IOException, FinderException{
    giroFiling(modinfo,roundid,bankoffice,finalpayday,giroB1, giroB2, giroB3,  giroB4);
  }

  public String getFileLinkString(){
    return this.sFileLink;
  }

  public void setFileLinkString(String sFileLink){
    this.sFileLink = sFileLink;
  }

  public void writeFile(IWContext modinfo,Payment[] ePayments, int bankoffice,int finalpayday,String giroB1,String giroB2, String giroB3, String giroB4, int union_id) throws SQLException,IOException, FinderException{
    Union U = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(union_id);
    IWTimestamp datenow = new IWTimestamp();

    String sLowerCaseUnionAbbreviation = U.getAbbrevation().toLowerCase();
    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+sLowerCaseUnionAbbreviation+fileSeperator);
    StringBuffer fileName = new StringBuffer(sLowerCaseUnionAbbreviation);
    fileName.append(datenow.getDay());
    fileName.append(datenow.getMonth());
    fileName.append(datenow.getMinute());
    fileName.append(".krafa.banki");

    String sWholeFileString = fileSeperator+fileName.toString();
    this.setFileLinkString(fileSeperator+sLowerCaseUnionAbbreviation+sWholeFileString);
    File outputFile = new File(filepath+sWholeFileString);
    FileWriter out = new FileWriter(outputFile);
    char[] c ;

    String giroID = "1";
    java.text.DecimalFormat myFormatter = new DecimalFormat("00000000000");
    java.text.DecimalFormat referenceFormatter = new DecimalFormat("00000000");
    java.text.DecimalFormat countFormatter = new DecimalFormat("0000000");
    java.text.DecimalFormat dateFormatter = new DecimalFormat("00");
    StringBuffer SB = new StringBuffer();


    int TotalPrice = 0;
    int price = 0;
    int totalcount = 0;
    //Formfærsla kröfuhafa
    SB.append(bankoffice); //Bankanúmer þjónustuútibús
    SB.append("KR");//inniheldur KR
    SB.append("      ");
    SB.append("//");
    SB.append("Innheimtukerfi            ");
    SB.append("\n");

    if(!giroB1.equals("")){
      SB.append("B1080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB1);// Autt
      SB.append("\n");
    }
    if(!giroB2.equals("")){
      SB.append("B2080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB2);// Autt
      SB.append("\n");
    }
    if(!giroB3.equals("")){
      SB.append("B3080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB3);// Autt
      SB.append("\n");
    }
    if(!giroB4.equals("")){
      SB.append("B4080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB4);// Autt
      SB.append("\n");
    }

    // write the first part to file
    c = SB.toString().toCharArray();
    out.write(c);

    Member M;IWTimestamp PayDate;
    String year,month,day,kt;
    int theMonth;
    String strFinalDay = dateFormatter.format(finalpayday);
    for(int i = 0; i < ePayments.length ;i++){
      SB = new StringBuffer();
      M = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(ePayments[i].getMemberId());
      kt = M.getSocialSecurityNumber();
      PayDate = new IWTimestamp(ePayments[i].getPaymentDate());
      price = ePayments[i].getPrice();
      year = String.valueOf(PayDate.getYear());
      month = dateFormatter.format(PayDate.getMonth());
      day = dateFormatter.format(PayDate.getDay());
      SB.append("C2080");                   // Inniheldur C2 og 080
      SB.append("          ");              // kennitala kröfuhafa(10)
      SB.append(kt);			    //kennitala greiðanda(10)
      SB.append("EEEEE"); 	            //Efnislykill (5)
      TotalPrice += price;
      SB.append(myFormatter.format(price)); //kröfuupphæð sem gildir að gjalddaga(11)
      SB.append("00");  		    // "aurar" (,2)
      SB.append("             ");           // kröfuupphæð sem gildir frá og með gjalddaga eða núll(11,2)
      // tilvísun (16)(8+8):
      SB.append(referenceFormatter.format(M.getID()));
      SB.append(referenceFormatter.format(ePayments[i].getID()));
      SB.append(" ");                       //autt svæði (1)
      // Gjalddagi seðils eyða ef reglulegar kröfur (6)
      SB.append(year);
      SB.append(month);
      SB.append(day);

      // Eindagi seðils eyða ef reglulegar kröfur eða aldrei vanskil(6)
      if(finalpayday != 0){
        SB.append(year);
        SB.append(month);
        SB.append(strFinalDay);
      }
      else{
        SB.append("      ");
      }
      SB.append("       ");     // Seðilnúmer (7)
      SB.append("           "); // Vanskilakostnaður (11)
      SB.append("S");
      SB.append("\n");
      totalcount ++;

      // write a line to file
      c = SB.toString().toCharArray();
      out.write(c);
    }
    SB = new StringBuffer();
    SB.append("LF"); // á að innihalda "LF" (2)
    SB.append("080"); // // 080 (3)
    SB.append("          ");// Kt.kröfuhafa (10)
    SB.append("          ");// autt (10)
    SB.append(myFormatter.format(TotalPrice));
    SB.append("00");  // "aurar" (,2)
    SB.append("             ");//Upphæð 2
    SB.append(countFormatter.format(totalcount));
    SB.append("\n");

    // write the last part to file
    c = SB.toString().toCharArray();
    out.write(c);
    out.close();
    //add(SB.toString());
  }

   private void giroFiling(IWContext modinfo,int roundid, int bankoffice,int finalpayday,String giroB1,String giroB2, String giroB3, String giroB4) throws SQLException,IOException, FinderException{
    //PaymentType[] giroPtype = (PaymentType[]) (new PaymentType()).findAll("select * from payment_type where name like 'Gíró%'");
    String union_id = (String)  modinfo.getSession().getAttribute("golf_union_id");
    int un_id = Integer.parseInt(union_id)  ;
    Union U = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(un_id);
    IWTimestamp datenow = new IWTimestamp();

    String sLowerCaseUnionAbbreviation = U.getAbbrevation().toLowerCase();
    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+sLowerCaseUnionAbbreviation+fileSeperator);
    StringBuffer fileName = new StringBuffer(sLowerCaseUnionAbbreviation);
    fileName.append(datenow.getDay());
    fileName.append(datenow.getMonth());
    fileName.append(datenow.getMinute());
    fileName.append(".krafa.banki");

    String sWholeFileString = fileSeperator+fileName.toString();
    this.setFileLinkString(fileSeperator+sLowerCaseUnionAbbreviation+sWholeFileString);
    File outputFile = new File(filepath+sWholeFileString);
    FileWriter out = new FileWriter(outputFile);
    char[] c ;

    String giroID = "1";
    java.text.DecimalFormat myFormatter = new DecimalFormat("00000000000");
    java.text.DecimalFormat referenceFormatter = new DecimalFormat("00000000");
    java.text.DecimalFormat countFormatter = new DecimalFormat("0000000");
    java.text.DecimalFormat dateFormatter = new DecimalFormat("00");
    StringBuffer SB = new StringBuffer();

    PaymentRound PR = ((PaymentRoundHome) IDOLookup.getHomeLegacy(PaymentRound.class)).findByPrimaryKey(roundid);
    String paymRoundIDstring = String.valueOf(PR.getID());
    Payment[] payments = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAllByColumnEquals("status","N","payment_type_id",giroID );

    int TotalPrice = 0;
    int price = 0;
    int totalcount = 0;
    //Formfærsla kröfuhafa
    SB.append(bankoffice); //Bankanúmer þjónustuútibús
    SB.append("KR");//inniheldur KR
    SB.append("      ");
    SB.append("//");
    SB.append("Innheimtukerfi            ");
    SB.append("\n");

    if(!giroB1.equals("")){
      SB.append("B1080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB1);// Autt
      SB.append("\n");
    }
    if(!giroB2.equals("")){
      SB.append("B2080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB2);// Autt
      SB.append("\n");
    }
    if(!giroB3.equals("")){
      SB.append("B3080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB3);// Autt
      SB.append("\n");
    }
    if(!giroB4.equals("")){
      SB.append("B4080");
      SB.append("          ");// Kennitala kröfuhafa
      SB.append("          ");// Autt
      SB.append(giroB4);// Autt
      SB.append("\n");
    }

    // write the first part to file
    c = SB.toString().toCharArray();
    out.write(c);

    Member M;IWTimestamp PayDate;
    String year,month,day,kt;
    int theMonth;
    String strFinalDay = dateFormatter.format(finalpayday);
    for(int i = 0; i < payments.length ;i++){
      SB = new StringBuffer();
      M = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(payments[i].getMemberId());
      kt = M.getSocialSecurityNumber();
      PayDate = new IWTimestamp(payments[i].getPaymentDate());
      price = payments[i].getPrice();
      year = String.valueOf(PayDate.getYear());
      month = dateFormatter.format(PayDate.getMonth());
      day = dateFormatter.format(PayDate.getDay());
      SB.append("C2080");                   // Inniheldur C2 og 080
      SB.append("          ");              // kennitala kröfuhafa(10)
      SB.append(kt);			    //kennitala greiðanda(10)
      SB.append("EEEEE"); 	            //Efnislykill (5)
      TotalPrice += price;
      SB.append(myFormatter.format(price)); //kröfuupphæð sem gildir að gjalddaga(11)
      SB.append("00");  		    // "aurar" (,2)
      SB.append("             ");           // kröfuupphæð sem gildir frá og með gjalddaga eða núll(11,2)
      // tilvísun (16)(8+8):
      SB.append(referenceFormatter.format(M.getID()));
      SB.append(referenceFormatter.format(payments[i].getID()));
      SB.append(" ");                       //autt svæði (1)
      // Gjalddagi seðils eyða ef reglulegar kröfur (6)
      SB.append(year);
      SB.append(month);
      SB.append(day);

      // Eindagi seðils eyða ef reglulegar kröfur eða aldrei vanskil(6)
      if(finalpayday != 0){
        SB.append(year);
        SB.append(month);
        SB.append(strFinalDay);
      }
      else{
        SB.append("      ");
      }
      SB.append("       ");     // Seðilnúmer (7)
      SB.append("           "); // Vanskilakostnaður (11)
      SB.append("S");
      SB.append("\n");
      totalcount ++;

      // write a line to file
      c = SB.toString().toCharArray();
      out.write(c);
    }
    SB = new StringBuffer();
    SB.append("LF"); // á að innihalda "LF" (2)
    SB.append("080"); // // 080 (3)
    SB.append("          ");// Kt.kröfuhafa (10)
    SB.append("          ");// autt (10)
    SB.append(myFormatter.format(TotalPrice));
    SB.append("00");  // "aurar" (,2)
    SB.append("             ");//Upphæð 2
    SB.append(countFormatter.format(totalcount));
    SB.append("\n");

    // write the last part to file
    c = SB.toString().toCharArray();
    out.write(c);
    out.close();
    //add(SB.toString());
  }
  /** @todo: Bæta við*/
  public static Payment ProcessLine(String strBankline){
    Payment P = (Payment) IDOLookup.createLegacy(Payment.class);
    if(strBankline.startsWith("G2")){
      try{
        int iPID = Integer.parseInt(strBankline.substring(45,53));
        P = ((PaymentHome) IDOLookup.getHomeLegacy(Payment.class)).findByPrimaryKey(iPID);
        int iPrice = Integer.parseInt(strBankline.substring(53,65));
        int iOverdueInterest = Integer.parseInt(strBankline.substring(66,78));
        int iOverduePrice = Integer.parseInt(strBankline.substring(79,89));
        int iBankCost = Integer.parseInt(strBankline.substring(91,101));
        if(iPrice != P.getPrice())
          P.setPrice(iPrice);
/*        if(iOverdueInterest != 0)
          P.setOverDueInterest(iOverdueInterest);
        if(iOverduePrice != 0)
          P.setOverDuePrice(iOverduePrice);
        if(iBankCost != 0)
          P.setBankCost(iBankCost);
*/
        P.update();
      }
      catch(SQLException e){e.printStackTrace();}
      catch(FinderException e){e.printStackTrace();}
    }

    if(strBankline.startsWith("Z2")){
      try{
        int iReceivedAmount1 = Integer.parseInt(strBankline.substring(25,37));
        int iReceivedAmount2 = Integer.parseInt(strBankline.substring(38,50));
        int iEntriesReceived = Integer.parseInt(strBankline.substring(51,58));
        int iFaultEntries = Integer.parseInt(strBankline.substring(58,65));
        int iCollectAmount =  Integer.parseInt(strBankline.substring(65,77));
        int iInterestAmount =  Integer.parseInt(strBankline.substring(78,90));
        int iOverdueAmount =  Integer.parseInt(strBankline.substring(91,101));
        int iEntryCount = Integer.parseInt(strBankline.substring(102,109));
      }
      catch(Exception e){e.printStackTrace();}
    }
    return P;
  }

  private void writeFile(String filename,String content) throws IOException{
    File outputFile = new File(filename);
    FileWriter out = new FileWriter(outputFile);
    char[] c = content.toCharArray();
    out.write(c);
    out.close();
  }
}// class Tariffer
