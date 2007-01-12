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

	protected final static String ENTITY_NAME = "fin_bank_info";

	protected final static String COLUMN_CLAIMANTS_SSN = "claimants_ssn";

	protected final static String COLUMN_CLAIMANTS_NAME = "claimants_name";

	protected final static String COLUMN_CLAIMANTS_BANK_BRANCH = "fin_bank_branch_id";

	protected final static String COLUMN_ACCOUNT_BOOK = "account_book";

	protected final static String COLUMN_ACCOUNT_ID = "account_id";

	protected final static String COLUMN_GROUP_ID = GroupBMPBean
			.getColumnNameGroupID();

	protected final static String COLUMN_USER_NAME = "user_name";

	protected final static String COLUMN_PASSWORD = "password";

	protected final static String COLUMN_CLUB_ID = "club_id";

	protected final static String COLUMN_DIVISION_ID = "division_id";

	protected final static String COLUMN_DELETED = "deleted";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_CLAIMANTS_SSN, "claimants ssn", String.class);
		addAttribute(COLUMN_CLAIMANTS_NAME, "claimants name", String.class);
		addAttribute(COLUMN_ACCOUNT_BOOK, "account book", Integer.class);
		addAttribute(COLUMN_ACCOUNT_ID, "account id", String.class);
		addAttribute(COLUMN_USER_NAME, "user name", String.class);
		addAttribute(COLUMN_PASSWORD, "password", String.class);
		addManyToOneRelationship(COLUMN_CLAIMANTS_BANK_BRANCH, BankBranch.class);
		addOneToOneRelationship(COLUMN_GROUP_ID, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION_ID, Group.class);
		addManyToOneRelationship(COLUMN_CLUB_ID, Group.class);
		addAttribute(COLUMN_DELETED, "deleted", Boolean.class);
	}

	public static String getEntityTableName() {
		return ENTITY_NAME;
	}

	public String getEntityName() {
		return getEntityTableName();
	}

	public int getAccountBook() {
		return getIntColumnValue(COLUMN_ACCOUNT_BOOK);
	}

	public String getAccountId() {
		return getStringColumnValue(COLUMN_ACCOUNT_ID);
	}

	public int getClaimantsBankBranchId() {
		return getIntColumnValue(COLUMN_CLAIMANTS_BANK_BRANCH);
	}

	public BankBranch getClaimantsBankBranch() {
		return (BankBranch) getColumnValue(COLUMN_CLAIMANTS_BANK_BRANCH);
	}

	public int getClubId() {
		return getIntColumnValue(COLUMN_CLUB_ID);
	}

	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB_ID);
	}

	public int getDivisionId() {
		return getIntColumnValue(COLUMN_DIVISION_ID);
	}

	public Group getDivision() {
		return (Group) getColumnValue(COLUMN_DIVISION_ID);
	}

	public String getClaimantsSSN() {
		return getStringColumnValue(COLUMN_CLAIMANTS_SSN);
	}

	public String getClaimantsName() {
		return getStringColumnValue(COLUMN_CLAIMANTS_NAME);
	}

	public int getGroupId() {
		return getIntColumnValue(COLUMN_GROUP_ID);
	}

	public Group getGroup() {
		return (Group) getColumnValue(COLUMN_GROUP_ID);
	}

	public String getUsername() {
		return getStringColumnValue(COLUMN_USER_NAME);
	}

	public String getPassword() {
		String str = null;
		try {
			str = getStringColumnValue(COLUMN_PASSWORD);
		} catch (Exception ex) {
			ex.printStackTrace();
			// str = null;
		}
		if (str != null) {
			char[] pass = new char[str.length() / 2];
			try {
				for (int i = 0; i < pass.length; i++) {
					pass[i] = (char) Integer.decode(
							"0x" + str.charAt(i * 2) + str.charAt((i * 2) + 1))
							.intValue();
				}
				return String.valueOf(pass);
			} catch (Exception ex) {
				ex.printStackTrace();
				return str;
			}
		}
		return str;
	}

	public boolean getDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}

	public void setAccountBook(int accountBook) {
		setColumn(COLUMN_ACCOUNT_BOOK, accountBook);
	}

	public void setAccountId(String accountId) {
		setColumn(COLUMN_ACCOUNT_ID, accountId);
	}

	public void setClaimantsBankBranchId(int bankBranchId) {
		setColumn(COLUMN_CLAIMANTS_BANK_BRANCH, bankBranchId);
	}

	public void setClaimantsBankBranchNumber(BankBranch bankBranch) {
		setColumn(COLUMN_CLAIMANTS_BANK_BRANCH, bankBranch);
	}

	public void setClubId(int clubId) {
		setColumn(COLUMN_CLUB_ID, clubId);
	}

	public void setDivisionId(int divisionId) {
		setColumn(COLUMN_DIVISION_ID, divisionId);
	}

	public void setClaimantsSSN(String claimantsSSN) {
		setColumn(COLUMN_CLAIMANTS_SSN, claimantsSSN);
	}

	public void setClaimantsName(String claimantsName) {
		setColumn(COLUMN_CLAIMANTS_NAME, claimantsName);
	}

	public void setGroupId(int groupId) {
		setColumn(COLUMN_GROUP_ID, groupId);
	}

	public void setUsername(String username) {
		setColumn(COLUMN_USER_NAME, username);
	}

	public void setPassword(String pwd) {
		try {
			String str = "";
			char[] pass = pwd.toCharArray();
			for (int i = 0; i < pass.length; i++) {
				String hex = Integer.toHexString(pass[i]);
				while (hex.length() < 2) {
					String s = "0";
					s += hex;
					hex = s;
				}
				str += hex;
			}
			if (str.equals("") && !pwd.equals("")) {
				str = null;
			}
			setColumn(COLUMN_PASSWORD, str);
		} catch (Exception ex) {
			ex.printStackTrace();
			setColumn(COLUMN_PASSWORD, pwd);
		}
	}

	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}

	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.appendLeftParenthesis();
		sql.append(COLUMN_DELETED);
		sql.append(" is null ");
		sql.appendOr();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendRightParenthesis();

		return idoFindPKsByQuery(sql);
	}

	public Integer ejbFindByGroupId(int groupId) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_GROUP_ID, groupId);
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.append(" is null ");
		query.appendOr();
		query.appendEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();

		return (Integer) idoFindOnePKByQuery(query);
	}

	public Collection ejbFindAllByClub(Group club) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CLUB_ID,
				((Integer) club.getPrimaryKey()).intValue());
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.append(" is null ");
		query.appendOr();
		query.appendEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByGroup(Group group) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_GROUP_ID, group);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.append(COLUMN_DELETED);
		sql.append(" is null ");
		sql.appendOr();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendRightParenthesis();

		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindByDivision(Group division) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_DIVISION_ID, division);
		sql.appendAnd();
		sql.append(COLUMN_GROUP_ID);
		sql.append(" is null ");
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.append(COLUMN_DELETED);
		sql.append(" is null ");
		sql.appendOr();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendRightParenthesis();

		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindByClub(Group club) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB_ID, club);
		sql.appendAnd();
		sql.append(COLUMN_DIVISION_ID);
		sql.append(" is null ");
		sql.appendAnd();
		sql.append(COLUMN_GROUP_ID);
		sql.append(" is null ");
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.append(COLUMN_DELETED);
		sql.append(" is null ");
		sql.appendOr();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendRightParenthesis();

		return idoFindOnePKByQuery(sql);
	}
	
	public Object ejbFindByUserNameAndBankShortName(String userName, String bankShortName) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelect();
		sql.append("info.* from ");
		sql.append(getEntityName());
		sql.append(" info, fin_bank_branch branch, fin_bank bank");
		sql.appendWhereEquals("info.fin_bank_branch_id", "branch.fin_bank_branch_id");
		sql.appendAnd();
		sql.appendEquals("branch.bank_id", "bank.fin_bank_id");
		sql.appendAnd();
		sql.appendEqualsQuoted("bank.short_name", bankShortName);
		sql.appendAnd();
		sql.appendEqualsQuoted(COLUMN_USER_NAME, userName);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendOr();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendRightParenthesis();
		
		System.out.println("sql = " + sql.toString());
		
		return idoFindOnePKByQuery(sql);
	}
}