/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Description: Account key entity, debet (d) or credit (c)<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportAccountKeyBMPBean extends GenericEntity implements WorkReportAccountKey{
	protected final static String ENTITY_NAME = "ISI_WR_ACCOUNT_KEY";
	protected final static String COLUMN_NAME_KEY_NAME = "KEY_NAME";
	protected final static String COLUMN_NAME_KEY_NUMBER = "KEY_NUMBER";
	protected final static String COLUMN_NAME_KEY_TYPE = "KEY_TYPE";
	protected final static String COLUMN_NAME_DEBET_OR_CREDIT = "DEB_OR_CRED";//values d/c
	
	protected final static String DEBET = "d";
	protected final static String CREDIT = "c";
	
	protected final static String INCOME_SHEET = "i";
	protected final static String BALANCE_SHEET = "b";
		
	public WorkReportAccountKeyBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_KEY_NAME,"Account key name",true,true,String.class);
		addAttribute(COLUMN_NAME_KEY_NUMBER,"Account key number ",true,true,String.class);
		addAttribute(COLUMN_NAME_KEY_TYPE,"Account key type",true,true,String.class);
		addAttribute(COLUMN_NAME_DEBET_OR_CREDIT,"Debet or Credit (d/c)",true,true,String.class);
	}
	
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	
	public boolean isDebet() {
		return DEBET.equals(getStringColumnValue(COLUMN_NAME_DEBET_OR_CREDIT));
	}

	public boolean isCredit() {
		return CREDIT.equals(getStringColumnValue(COLUMN_NAME_DEBET_OR_CREDIT));
	}
	
	public String ejbHomeGetCreditTypeString(){
		return CREDIT;
	}
	
	public String ejbHomeGetDebetTypeString(){
		return DEBET;
	}
		

	public void setAsDebet() {
		setColumn(COLUMN_NAME_DEBET_OR_CREDIT,DEBET);
	}
	
	public void setAsCredit() {
		setColumn(COLUMN_NAME_DEBET_OR_CREDIT,CREDIT);
	}
	

	public String getKeyName() {
		return getStringColumnValue(COLUMN_NAME_KEY_NAME);
	}

	public void setKeyName(String keyName) {
		setColumn(COLUMN_NAME_KEY_NAME, keyName);
	}

	public String getKeyNumber() {
		return getStringColumnValue(COLUMN_NAME_KEY_NUMBER);
	}

	public void setKeyNumber(String keyNumber) {
		setColumn(COLUMN_NAME_KEY_NUMBER,keyNumber);
	}

	public String getKeyType() {
		return getStringColumnValue(COLUMN_NAME_KEY_TYPE);
	}

	public void setKeyType(String keyType) {
		setColumn(COLUMN_NAME_KEY_TYPE,keyType);
	}
	
	
	public Integer ejbFindAccountKeyByName(String name) throws FinderException{
		return (Integer) idoFindOnePKByColumnBySQL(COLUMN_NAME_KEY_NAME,name);
	}
	
	public Integer ejbFindAccountKeyByNumber(String number) throws FinderException{
		return (Integer) idoFindOnePKByColumnBySQL(COLUMN_NAME_KEY_NUMBER,number);
	}

}
