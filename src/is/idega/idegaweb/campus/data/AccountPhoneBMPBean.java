/*
<<<<<<< AccountPhoneBMPBean.java
<<<<<<< AccountPhoneBMPBean.java
<<<<<<< AccountPhoneBMPBean.java
 * 
 * $Id: AccountPhoneBMPBean.java,v 1.8 2004/07/15 15:04:09 aron Exp $
 * Copyright (C) 2001 Idega hf. All Rights Reserved. This software is the
 * proprietary information of Idega hf. Use is subject to license terms.
 *  
=======
 * $Id: AccountPhoneBMPBean.java,v 1.8 2004/07/15 15:04:09 aron Exp $
=======
 * $Id: AccountPhoneBMPBean.java,v 1.8 2004/07/15 15:04:09 aron Exp $
>>>>>>> 1.3
=======
 * $Id: AccountPhoneBMPBean.java,v 1.8 2004/07/15 15:04:09 aron Exp $
>>>>>>> 1.4
 * 
 * Copyright (C) 2001-2004 Idega hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
>>>>>>> 1.2
 */
package is.idega.idegaweb.campus.data;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;

/**
 * 
 * Title:
 * 
 * Description:
<<<<<<< AccountPhoneBMPBean.java
 * 
 * Copyright: Copyright (c) 2001
 * 
 * Company: idega.is
 * 
 * @author 2000 - idega team -<br><a href="mailto:aron@idega.is">Aron Birkir
 *              </a><br>
 * 
 * 
 * Copyright: Copyright (c) 2001
 * 
 * Company: idega.is
 * 
 * @author 2000 - idega team -<br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * 
 * @version 1.0
 *  
 */

public class AccountPhoneBMPBean
	extends com.idega.data.GenericView
	implements is.idega.idegaweb.campus.data.AccountPhone {
	/*
	 * 
	 * create view V_PHONE_ACCOUNTS ( FIN_ACCOUNT_ID,CAM_PHONE_NUMBER
	 * ,VALID_FROM,VALID_TO,DELIVER_DATE,RETURN_DATE)
	 * 
	 * as
	 * 
	 * select ACC.fin_account_id, PHO.PHONE_NUMBER ,con.valid_from
	 * ,con.valid_to,
	 * 
	 * cc.deliver_date,cc.return_date
	 * 
	 * from cam_phone pho,cam_contract con, fin_account acc
	 * 
	 * where pho.bu_apartment_id = con.bu_apartment_id
	 * 
	 * and acc.ic_user_id = con.ic_user_id
	 * 
	 * and acc.account_type = 'PHONE'
	 * 
	 * order by pho.phone_number
	 *  
	 */
	public static String getEntityTableName() {
		return "V_PHONE_ACCOUNTS";
	}
	public static String getColumnNameAccountId() {
		return "FIN_ACCOUNT_ID";
	}
	public static String getColumnNamePhoneNumber() {
		return "CAM_PHONE_NUMBER";
	}
	public static String getColumnNameValidTo() {
		return "VALID_TO";
	}
	public static String getColumnNameValidFrom() {
		return "VALID_FROM";
	}
	public static String getColumnDeliverDate() {
		return "DELIVER_DATE";
	}
	public static String getColumnReturnDate() {
		return "RETURN_DATE";
	}
	public AccountPhoneBMPBean() {
	}
	public AccountPhoneBMPBean(int id) throws SQLException {
	}
	public void initializeAttributes() {
		addAttribute(getColumnNameAccountId(), "Account Id", true, true, java.lang.Integer.class);
		addAttribute(getColumnNamePhoneNumber(), "Phone number", true, true, java.lang.String.class);
		addAttribute(getColumnNameValidFrom(), "Valid from", true, true, java.sql.Date.class);
		addAttribute(getColumnNameValidTo(), "Valid to", true, true, java.sql.Date.class);
		addAttribute(getColumnDeliverDate(), "Deliver time", true, true, java.sql.Timestamp.class);
		addAttribute(getColumnReturnDate(), "Return time", true, true, java.sql.Timestamp.class);
		setAsPrimaryKey(getColumnNameAccountId(),true);
	}
	public String getEntityName() {
		return (getEntityTableName());
	}
	public void setValidFrom(Date date) {
		setColumn(getColumnNameValidFrom(), date);
	}
	public Date getValidFrom() {
		return ((Date) getColumnValue(getColumnNameValidFrom()));
	}
	public void setValidTo(Date date) {
		setColumn(getColumnNameValidTo(), date);
	}
	public Date getValidTo() {
		return ((Date) getColumnValue(getColumnNameValidTo()));
	}
	public Timestamp getDeliverTime() {
		return ((Timestamp) getColumnValue(getColumnDeliverDate()));
	}
	public Timestamp getReturnTime() {
		return ((Timestamp) getColumnValue(getColumnReturnDate()));
	}
	public Integer getAccountId() {
		return getIntegerColumnValue(getColumnNameAccountId());
	}
	public String getPhoneNumber() {
		return getStringColumnValue(getColumnNamePhoneNumber());
	}
	public void insert() throws SQLException {
	}
	public void delete() throws SQLException {
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("create view V_PHONE_ACCOUNTS ( FIN_ACCOUNT_ID,CAM_PHONE_NUMBER ");
		sql.append(" ,VALID_FROM,VALID_TO,DELIVER_DATE,RETURN_DATE) ");
		sql.append(" as ");
		sql.append(" select ACC.fin_account_id, PHO.PHONE_NUMBER ,con.valid_from ");
		sql.append(",con.valid_to, ");
		sql.append(" cc.deliver_date,cc.return_date ");
		sql.append(" from cam_phone pho,cam_contract con, fin_account acc ");
		sql.append(" where pho.bu_apartment_id = con.bu_apartment_id ");
		sql.append(" and acc.ic_user_id = con.ic_user_id ");
		sql.append(" and acc.account_type = 'PHONE' ");
		sql.append(" order by pho.phone_number ");
		return null;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericView#getViewName()
	 */
	public String getViewName() {
		// TODO Auto-generated method stub
		return getEntityTableName();
	}
	
	public Collection ejbFindByPhoneNumber(String number)throws FinderException{
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getColumnNamePhoneNumber(),number));
	}
	
	public Collection ejbFindAll()throws FinderException{
		return super.idoFindPKsByQuery(idoQueryGetSelect());
	}
	
	public Collection ejbFindValid(Date toDate)throws FinderException{
	   IDOQuery query = idoQueryGetSelect();
	   if(toDate!=null){
	   	query.appendWhere().append(getColumnNameValidTo()).appendGreaterThanOrEqualsSign().append(toDate);
	   }
	   return idoFindPKsByQuery(query);
	}
	
}

