/*
 * $Id: AccountPhone.java,v 1.1 2001/09/24 12:50:07 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;

import com.idega.data.GenericEntity;
import java.sql.Date;
import java.lang.IllegalStateException;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class AccountPhone extends GenericEntity {

/*
 create view V_PHONE_ACCOUNTS ( FIN_ACCOUNT_ID,CAM_PHONE_NUMBER )
as
select ACC.fin_account_id, PHO.PHONE_NUMBER
from cam_phone pho,cam_contract con, fin_account acc
where pho.bu_apartment_id = con.bu_apartment_id
and acc.ic_user_id = con.ic_user_id
and con.status = 'S'
*/
  public static String getEntityTableName(){return "V_PHONE_ACCOUNTS";}
  public static String getColumnNameAccountId(){return "FIN_ACCOUNT_ID";}
  public static String getColumnNamePhoneNumber(){return  "CAM_PHONE_NUMBER";}


  public AccountPhone() {
  }
  public AccountPhone(int id) throws SQLException {

  }
  public void initializeAttributes() {
    addAttribute(getColumnNameAccountId(),"Account Id",true,true,java.lang.Integer.class);
    addAttribute(getColumnNamePhoneNumber(),"Phone number",true,true,java.lang.String.class);

  }
  public String getEntityName() {
    return(getEntityTableName());
  }

  public Integer getAccountId(){
    return getIntegerColumnValue(getColumnNameAccountId());
  }
  public String getPhoneNumber(){
    return getStringColumnValue(getColumnNamePhoneNumber());
  }
  public void insert()throws SQLException{

  }
  public void delete()throws SQLException{

  }
}