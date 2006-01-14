/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

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
  protected final static String COLUMN_NAME_PARENT_KEY_NUMBER = "PARENT_KEY_NUMBER";
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
    addAttribute(COLUMN_NAME_PARENT_KEY_NUMBER, "Account key number of the parent", true, true, String.class);
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

  public String getParentKeyNumber()  {
    return getStringColumnValue(COLUMN_NAME_PARENT_KEY_NUMBER);
  }
  
  public void setParentKeyNumber(String parentKeyNumber) {
    setColumn(COLUMN_NAME_PARENT_KEY_NUMBER, parentKeyNumber);
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

	public Collection ejbFindIncomeAccountKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(INCOME_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(DEBET).append('\'');
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindIncomeAccountKeysWithoutSubKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(INCOME_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(DEBET).append('\'');
		sql.append(" AND ").append(COLUMN_NAME_PARENT_KEY_NUMBER).append(" is null");
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	

	
	public Collection ejbFindExpensesAccountKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(INCOME_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(CREDIT).append('\'');
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindExpensesAccountKeysWithoutSubKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(INCOME_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(CREDIT).append('\'');
		sql.append(" AND ").append(COLUMN_NAME_PARENT_KEY_NUMBER).append(" is null");
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindAssetAccountKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(BALANCE_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(DEBET).append('\'');
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindAssetAccountKeysWithoutSubKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(BALANCE_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(DEBET).append('\'');
		sql.append(" AND ").append(COLUMN_NAME_PARENT_KEY_NUMBER).append(" is null");
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	


	public Collection ejbFindDeptAccountKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(BALANCE_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(CREDIT).append('\'');
		
		return super.idoFindPKsBySQL(sql.toString());
	}	
	
	public Collection ejbFindDeptAccountKeysWithoutSubKeys() throws FinderException {
		StringBuffer sql = new StringBuffer("select " + getIDColumnName() + " from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_NAME_KEY_TYPE);
		sql.append(" = '");
		sql.append(BALANCE_SHEET);
		sql.append("' and ");
		sql.append(COLUMN_NAME_DEBET_OR_CREDIT);
		sql.append(" = '");
		sql.append(CREDIT).append('\'');
		sql.append(" AND ").append(COLUMN_NAME_PARENT_KEY_NUMBER).append(" is null");
		
		return super.idoFindPKsBySQL(sql.toString());
	}	
	
}