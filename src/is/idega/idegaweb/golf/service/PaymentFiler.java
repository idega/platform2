package is.idega.idegaweb.golf.service;

import com.idega.presentation.*;
import com.idega.presentation.PresentationObject.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import is.idega.idegaweb.golf.entity.*;
import is.idega.idegaweb.golf.templates.*;
import is.idega.idegaweb.golf.*;
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

public class PaymentFiler  {
  private int bankoffice,finalpayday;
  private String giroB1, giroB2, giroB3,  giroB4;
  private int roundid;
  private String sFileLink = "";

  public PaymentFiler(){}

  public void makeFile(IWContext iwc,String filepath,int payment_type_id,int account_year_id,int union_id) throws SQLException,IOException{

    StringBuffer SB = new StringBuffer();

    PaymentType payType = new PaymentType(payment_type_id);
    AccountYear accYear = new AccountYear(account_year_id);

    StringBuffer sql = new StringBuffer("select p.* from payment p, account a , union_member_info umi");
    sql.append(" where union_id = ");
    sql.append(union_id);
    sql.append(" and umi.member_id = a.member_id and umi.union_id = ");
    sql.append(union_id);
    sql.append(" and umi.member_status = 'A' ");
    sql.append(" and a.account_id = p.account_id and a.account_year_id = ");
    sql.append(account_year_id);
    sql.append(" and p.payment_type_id = ");
    sql.append(payment_type_id);
    sql.append(" order by p.member_id ");

    Payment[] payments = (Payment[]) (new Payment()).findAll(sql.toString());

    Union U = new Union(union_id);
    idegaTimestamp datenow = new idegaTimestamp();

    StringBuffer fileName = new StringBuffer(U.getAbbrevation());
    fileName.append("_");
    fileName.append(payType.getName());
    fileName.append("_");
    fileName.append(accYear.getName());
    fileName.append("_");
    fileName.append(accYear.getID());


    fileName.append(".dat");

    File outputFile = new File(filepath+fileName.toString());
    FileWriter out = new FileWriter(outputFile);
    char[] c ;
    /*
    SB.append("\n\n\n");
    SB.append("VÍSA\n");
    SB.append(PayType.getName());
    SB.append("\n");
    SB.append(U.getName());
    SB.append("\t\t\t");
    SB.append(datenow.getISLDate(".", true));
    SB.append("\n");
    //SB.append("Samningsnúmer:");
    //SB.append(contract);

    SB.append("\n");
    */
    SB.append("Nafn\tKennitala\tFj.Gr.\tUpphæð\tKort");
    SB.append("\n");

    // write the first part to file
    c = SB.toString().toCharArray();
    out.write(c);

    int  newMemberID=-1,oldMemberID=0;
    Vector payms = new Vector();
    oldMemberID = payments[0].getMemberId();
    for(int i = 0; i < payments.length ;i++){
      newMemberID = payments[i].getMemberId();
      if( newMemberID != oldMemberID){
        out.write(getMemberPaymentsString(oldMemberID,union_id,payms));
        payms.clear();
      }
      payms.add(payments[i]);

      oldMemberID = newMemberID;
    }

      /*

      if(newMemberID == oldMemberID){
        payms.add(payments[i]);
      }
      else{
        if(payms != null){

        }
        else
          payms = new Vector();
        payms.add(payments[i]);
      }
      oldMemberID = newMemberID;
    }*/

   out.close();
  }

  private String getMemberPaymentsString(int member_id,int un_id,List payments)throws SQLException{
    Member M = new Member(member_id);
    UnionMemberInfo UMI = M.getUnionMemberInfo(un_id);
    Card card = null;
    if(UMI !=null && UMI.getCardId()>0)
      card = new Card(UMI.getCardId());
    int installMents = payments.size();
    int total = 0;
    Payment p ;
    if(payments !=null){
      for (int i = 0; i < installMents; i++) {
        p = (Payment) payments.get(i);
        total += p.getPrice();
      }

    }
    StringBuffer SB = new StringBuffer(M.getName());
    SB.append("\t");
    SB.append(M.getSocialSecurityNumber());
    SB.append("\t");
    SB.append(installMents);
    SB.append("\t");
    SB.append(total);
    SB.append("\t");
    if(card !=null){
      SB.append(card.getCardNumber());
    }
    SB.append("\n");
     if(payments !=null){
      for (int i = 0; i < installMents; i++) {
        p = (Payment) payments.get(i);
        SB.append(p.getInstallmentNr());
        SB.append("\t");
        SB.append(new idegaTimestamp(p.getPaymentDate()).getISLDate(".",true));
        SB.append("\t");
        SB.append(p.getPrice());
        SB.append("\t");
        SB.append("\n");
      }

    }
    return SB.toString();

  }

  private void writeFile(String filename,String content) throws IOException{
    File outputFile = new File(filename);
    FileWriter out = new FileWriter(outputFile);
    char[] c = content.toCharArray();
    out.write(c);
    out.close();
  }
}// class Tariffer
