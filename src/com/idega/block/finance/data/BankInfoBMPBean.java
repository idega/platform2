/*
 * Created on Dec 7, 2004
 *
 */
package com.idega.block.finance.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;
import com.idega.user.data.GroupBMPBean;


/**
 * @author birna
 *
 */
public class BankInfoBMPBean extends GenericEntity implements BankInfo {
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameClaimantsSSN(), "claimants ssn", true, true, String.class);
		addAttribute(getColumnNameClaimantsName(), "claimants name", true, true, String.class);
		addAttribute(getColumnNameClaimantsBankBranchNumber(), "claimants bank branch nr", true, true, String.class);
		addAttribute(getColumnNameAccountBook(), "account book", true, true, Integer.class);
		addAttribute(getColumnNameAccountId(), "account id", true, true, String.class);
		addAttribute(getColumnNameGroupId(), "group id", true, true, Integer.class, "one-to-one", Group.class);
		addAttribute(getColumnNameUsername(), "user name", true, true, String.class);
		addAttribute(getColumnNamePassword(), "password", true, true, String.class);
		addAttribute(getColumnNameClubId(), "club id", true, true, Integer.class);
		addAttribute(getColumnNameDivisionId(), "div id", true, true, Integer.class);

	}
	
	public static String getEntityTableName() { return "fin_bank_info"; }
	public static String getColumnNameClaimantsSSN() { return "claimants_ssn"; }
	public static String getColumnNameClaimantsName() { return "claimants_name"; }
	public static String getColumnNameClaimantsBankBranchNumber() { return BankBranchBMPBean.getColumnNameBankBranchNumber(); }
	public static String getColumnNameAccountBook() { return "account_book"; }
	public static String getColumnNameAccountId() { return "account_id"; }
	public static String getColumnNameGroupId() { return GroupBMPBean.getColumnNameGroupID(); }
	public static String getColumnNameUsername() { return "user_name"; }
	public static String getColumnNamePassword() { return "password"; }
	public static String getColumnNameClubId() { return "club_id"; }
	public static String getColumnNameDivisionId() { return "division_id"; }
	
	public String getEntityName() {
		return getEntityTableName();
	}
	public int getAccountBook() {
		return getIntColumnValue(getColumnNameAccountBook());
	}
	public String getAccountId() {
		return getStringColumnValue(getColumnNameAccountId());
	}
	public String getClaimantsBankBranchNumber() {
		return getStringColumnValue(getColumnNameClaimantsBankBranchNumber());
	}
	public int getClubId() {
		return getIntColumnValue(getColumnNameClubId());
	}
	public int getDivisionId() {
		return getIntColumnValue(getColumnNameDivisionId());
	}
	public String getClaimantsSSN() {
		return getStringColumnValue(getColumnNameClaimantsSSN());
	}
	public String getClaimantsName() {
		return getStringColumnValue(getColumnNameClaimantsName());
	}
	public int getGroupId() {
		return getIntColumnValue(getColumnNameGroupId());
	}
	public String getUsername() {
		return getStringColumnValue(getColumnNameUsername());
	}
	public String getPassword() {
    String str = null;
    try {
      str = getStringColumnValue(getColumnNamePassword());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      // str = null;
    }
    if(str != null){
      char[] pass = new char[str.length()/2];
      try {
        for (int i = 0; i < pass.length; i++) {
          pass[i] = (char)Integer.decode("0x"+str.charAt(i*2)+str.charAt((i*2)+1)).intValue();
        }
        return String.valueOf(pass);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return str;
      }
    }
    return str;
	}
	public void setAccountBook(int accountBook) {
		setColumn(getColumnNameAccountBook(), accountBook);
	}
	public void setAccountId(String accountId) {
		setColumn(getColumnNameAccountId(), accountId);
	}
	public void setClaimantsBankBranchNumber(String bankBranchNumber) {
		setColumn(getColumnNameClaimantsBankBranchNumber(), bankBranchNumber);
	}
	public void setClubId(int clubId) {
		setColumn(getColumnNameClubId(), clubId);
	}
	public void setDivisionId(int divisionId) {
		setColumn(getColumnNameDivisionId(), divisionId);
	}
	public void setClaimantsSSN(String claimantsSSN) {
		setColumn(getColumnNameClaimantsSSN(), claimantsSSN);
	}
	public void setClaimantsName(String claimantsName) {
		setColumn(getColumnNameClaimantsName(), claimantsName);
	}
	public void setGroupId(int groupId) {
		setColumn(getColumnNameGroupId(), groupId);
	}
	public void setUsername(String username) {
		setColumn(getColumnNameUsername(), username);
	}
	public void setPassword(String pwd) {
    try {
      String str = "";
      char[] pass = pwd.toCharArray();
      for (int i = 0; i < pass.length; i++) {
        String hex = Integer.toHexString((int)pass[i]);
        while (hex.length() < 2) {
          String s = "0";
          s += hex;
          hex = s;
        }
        str += hex;
      }
      if(str.equals("") && !pwd.equals("")){
        str = null;
      }
      setColumn(getColumnNamePassword(), str);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      setColumn(getColumnNamePassword(), pwd);
    }
	}
	public BankInfo ejbFindByGroupId(int groupId) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameGroupId(), groupId);
		return (BankInfo)idoFindOnePKByQuery(query);
	}
	public Collection ejbFindAllByClub(Group club) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(getColumnNameClubId(), ((Integer) club.getPrimaryKey()).intValue());
		System.out.println(query.toString());
		return idoFindPKsByQuery(query);
	}
}
