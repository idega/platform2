package is.idega.idegaweb.campus.block.application.business;

import java.sql.*;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.data.IDOLookup;
import com.idega.util.database.ConnectionBroker;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class FamilyFix {

  public FamilyFix() {
  }

  public static void applicantFix()throws SQLException{
    /*
    select c.cam_contract_id,a.app_applicant_id,a.ssn from app_applicant a,cam_contract c
    where c.app_applicant_id = a.app_applicant_id
    order by a.ssn ,c.cam_contract_id desc
    */
    StringBuffer sql = new StringBuffer();
    sql.append(" select c.cam_contract_id,a.app_applicant_id,a.ssn ");
    sql.append(" from app_applicant a,cam_contract c ");
    sql.append(" where c.app_applicant_id = a.app_applicant_id ");
    sql.append(" order by a.ssn ,c.cam_contract_id desc ");
    Connection conn= null;
		Statement stmt= null;
    boolean theReturn = false;
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

    try {
      t.begin();

			conn = ConnectionBroker.getConnection();
			stmt = conn.createStatement();
      ResultSet RS = stmt.executeQuery(sql.toString());
      PreparedStatement updateContract = conn.prepareStatement("UPDATE CAM_CONTRACT SET APP_APPLICANT_ID = ? WHERE CAM_CONTRACT_ID = ?");
      /*
      updateSales.setInt(1, 50);
      updateSales.setString(2, "Colombian");
      updateSales.executeUpdate();
      */
      int contractId;
      int applicantId,oldId;
      String ssn,old = "";
      if(RS.next())
        contractId = RS.getInt(1);
        applicantId = RS.getInt(2);
        ssn = RS.getString(3);
        old = ssn;
        oldId = applicantId;
      while(RS.next()){
        contractId = RS.getInt(1);
        applicantId = RS.getInt(2);
        ssn = RS.getString(3);
        if(ssn!=null && ssn.length() >0){
          if(old.equals(ssn)){
            System.err.println("fixing "+contractId+" applicant "+applicantId+" old"+oldId+" ssn "+ssn);
            System.err.println("UPDATE CAM_CONTRACT SET APP_APPLICANT_ID = "+oldId +" WHERE APP_APPLICANT_ID = "+applicantId);
            updateContract.setInt(1,oldId);
            updateContract.setInt(2,applicantId);
            updateContract.executeUpdate();

          }
          else{
            old = ssn;
            oldId = applicantId;
          }
        }
      }
      RS.close();
      stmt.close();

      t.commit();
      //t.rollback();
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }
		finally{
			if(stmt != null){
				stmt.close();
			}
			if (conn != null){
				ConnectionBroker.freeConnection(conn);
			}
		}
  }

  public static void fix() throws SQLException{
    StringBuffer sql = new StringBuffer();
    sql.append(" select distinct a.app_applicant_id , c.spouse_name ,c.spouse_ssn ,c.children ");
    sql.append(" from app_applicant a, app_application b,cam_application c ,cam_contract d");
    sql.append(" where a.app_applicant_id = b.app_applicant_id ");
    sql.append(" and c.app_application_id = b.app_application_id ");
    sql.append(" and d.app_applicant_id = a.app_applicant_id ");
    sql.append(" and a.app_applicant_id not in (select distinct app_applicant_id from app_applicant_tree  )");

    Connection conn= null;
		Statement stmt= null;
    boolean theReturn = false;
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

    try {
      t.begin();

			conn = ConnectionBroker.getConnection();
			stmt = conn.createStatement();
      ResultSet RS = stmt.executeQuery(sql.toString());
      int applicantId;
      String name,ssn,childs;
      while(RS.next()){
        applicantId = RS.getInt(1);
        name = RS.getString(2);
        ssn = RS.getString(3);
        childs = RS.getString(4);

        Applicant superApplicant = ((ApplicantHome)IDOLookup.getHome(Applicant.class)).findByPrimaryKey(new Integer(applicantId));
        superApplicant.setStatus("S");
        superApplicant.addChild(superApplicant);
        boolean spouse = name!=null && name.length()>0;
        boolean children = childs !=null && childs.length() >0;
        if(spouse || children){
          System.err.println("fixing "+applicantId+" spouse "+name+" children "+childs);
        if(spouse)
          createApplicant(name,ssn,superApplicant,"P");
        if(children)
          createChildren(childs,superApplicant,"C");
        }
        superApplicant.store();

      }
      RS.close();
      stmt.close();

      t.commit();
      //t.rollback();
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }
		finally{
			if(stmt != null){
				stmt.close();
			}
			if (conn != null){
				ConnectionBroker.freeConnection(conn);
			}
		}
  }

  public static void createApplicant(String name, String ssn, Applicant superApplicant,String status)throws Exception{
    Applicant spouse = ((ApplicantHome)IDOLookup.getHome(Applicant.class)).create();
    spouse.setStatus(status);
    spouse.setFullName(name);
    spouse.setSSN(ssn);
    spouse.store();
    if(superApplicant!=null)
    superApplicant.addChild(spouse);
  }

  public static void createChildren(String children,Applicant superApplicant,String status)throws Exception{
    java.util.StringTokenizer st = new java.util.StringTokenizer(children,"\n");
    while(st.hasMoreTokens()){
      String all = st.nextToken();
      char[] ch = all.toCharArray();
      int endofName = -1;
      for (int i = 0; i < ch.length; i++) {
        if(Character.isDigit(ch[i])){
          endofName = i;
          break;
        }
      }
      String name = all.substring(0);
      String ssn = "";
      if(endofName > 0){
        name = all.substring(0,endofName);
        if(all.length()>endofName)
          ssn = all.substring(endofName);
      }

      if(ssn.length()>10)
        ssn = ssn.substring(0,10);
      System.err.println("child ssn :"+ssn);
      createApplicant(name,ssn,superApplicant,status);
    }
  }
}
