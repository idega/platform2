package se.idega.idegaweb.commune.accounting.childcare.check.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.data.query.Criteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.user.data.User;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckBMPBean extends AbstractCaseBMPBean implements Check, Case {

	private static final String ENTITY_NAME = "CC_CHECK";
	private static final String CASE_CODE_KEY = "MBANCHK";
	private static final String CASE_CODE_DESCRIPTION = "Request for child care check";

	private static final String[] CASE_STATUS_KEYS = { "NYTT", "UBEH", "OMPR", "BVJD", "CHIN" };
	private static final String[] CASE_STATUS_DESCRIPTIONS = { "New case", "Case open", "Retrial", "Approved", "Redeemed" };

	private static final String COLUMN_CHILD_CARE_TYPE = "CHILD_CARE_TYPE";
	private static final String COLUMN_WORK_SITUATION_1 = "WORK_SITUATION_1";
	private static final String COLUMN_WORK_SITUATION_2 = "WORK_SITUATION_2";
	private static final String COLUMN_MOTHER_TONGUE_MC = "MOTHER_TONGUE_MC";
	private static final String COLUMN_MOTHER_TONGUE_FC = "MOTHER_TONGUE_FC";
	private static final String COLUMN_MOTHER_TONGUE_P = "MOTHER_TONGUE_P";
	private static final String COLUMN_CHILD_ID = "CHILD_ID";
	private static final String COLUMN_METHOD = "METHOD";
	private static final String COLUMN_AMOUNT = "AMOUNT";
	private static final String COLUMN_CHECK_FEE = "CHECK_FEE";
	private static final String COLUMN_MANAGER_ID = "MANAGER_ID";
	private static final String COLUMN_NOTES = "NOTES";
	private static final String COLUMN_RULE_1 = "RULE_1";
	private static final String COLUMN_RULE_2 = "RULE_2";
	private static final String COLUMN_RULE_3 = "RULE_3";
	private static final String COLUMN_RULE_4 = "RULE_4";
	private static final String COLUMN_RULE_5 = "RULE_5";
	private static final String COLUMN_USER_NOTES = "USER_NOTES";

	public CheckBMPBean() {
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addGeneralCaseRelation();

		//    this.addAttribute(COLUMN_CHILD_CARE_TYPE,"Type of child care",Integer.class,MANY_TO_ONE,com.idega.block.school.data.SchoolType.class);
		this.addAttribute(COLUMN_CHILD_CARE_TYPE, "Type of child care", Integer.class);
		this.addAttribute(COLUMN_WORK_SITUATION_1, "Work situation for custodian 1", Integer.class);
		this.addAttribute(COLUMN_WORK_SITUATION_2, "Work situation for custodian 2", Integer.class);
		this.addAttribute(COLUMN_CHILD_ID, "Child id", Integer.class);
		this.addAttribute(COLUMN_MOTHER_TONGUE_MC, "Mother tongue Mother-Child", String.class);
		this.addAttribute(COLUMN_MOTHER_TONGUE_FC, "Mother tongue Father-Child", String.class);
		this.addAttribute(COLUMN_MOTHER_TONGUE_P, "Mother tongue Parents", String.class);
		this.addAttribute(COLUMN_METHOD, "Method used when applying for check (1 citizen, 2 quick)", Integer.class);
		this.addAttribute(COLUMN_AMOUNT, "Total check amount", Integer.class);
		this.addAttribute(COLUMN_CHECK_FEE, "The fee citizen pays", Integer.class);
		this.addAttribute(COLUMN_MANAGER_ID, "The manager for the check request", Integer.class);
		this.addAttribute(COLUMN_NOTES, "Notes from the manager for the check request", String.class, 1000);
		this.addAttribute(COLUMN_RULE_1, "Control rule for nationally registered", Boolean.class);
		this.addAttribute(COLUMN_RULE_2, "Control rule for child over one year", Boolean.class);
		this.addAttribute(COLUMN_RULE_3, "Control rule for work situation approved", Boolean.class);
		this.addAttribute(COLUMN_RULE_4, "Control rule for dept", Boolean.class);
		this.addAttribute(COLUMN_RULE_5, "Control rule for special need", Boolean.class);
		this.addAttribute(COLUMN_USER_NOTES, "Notes to the user about the check request", String.class, 1000);
	}

	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	public String getCaseCodeDescription() {
		return CASE_CODE_DESCRIPTION;
	}

	public String[] getCaseStatusKeys() {
		return CASE_STATUS_KEYS;
	}

	public String[] getCaseStatusDescriptions() {
		return CASE_STATUS_DESCRIPTIONS;
	}

	public void setChildCareType(int type) {
		this.setColumn(COLUMN_CHILD_CARE_TYPE, type);
	}

	public int getChildCareType() {
		return this.getIntColumnValue(COLUMN_CHILD_CARE_TYPE);
	}

	public void setWorkSituation1(int type) {
		this.setColumn(COLUMN_WORK_SITUATION_1, new Integer(type));
	}

	public int getWorkSituation1() {
		return this.getIntColumnValue(COLUMN_WORK_SITUATION_1);
	}

	public void setWorkSituation2(int type) {
		this.setColumn(COLUMN_WORK_SITUATION_2, new Integer(type));
	}

	public int getWorkSituation2() {
		return this.getIntColumnValue(COLUMN_WORK_SITUATION_2);
	}

	public void setMotherTongueMotherChild(String s) {
		this.setColumn(COLUMN_MOTHER_TONGUE_MC, s);
	}

	public String getMotherToungueMotherChild() {
		return this.getStringColumnValue(COLUMN_MOTHER_TONGUE_MC);
	}

	public void setMotherTongueFatherChild(String s) {
		this.setColumn(COLUMN_MOTHER_TONGUE_FC, s);
	}

	public String getMotherToungueFatherChild() {
		return this.getStringColumnValue(COLUMN_MOTHER_TONGUE_FC);
	}

	public void setMotherTongueParents(String s) {
		this.setColumn(COLUMN_MOTHER_TONGUE_P, s);
	}

	public String getMotherToungueParents() {
		return this.getStringColumnValue(COLUMN_MOTHER_TONGUE_P);
	}

	public void setChildId(int id) {
		this.setColumn(COLUMN_CHILD_ID, new Integer(id));
	}

	public int getChildId() {
		return this.getIntColumnValue(COLUMN_CHILD_ID);
	}

	public void setMethod(int type) {
		this.setColumn(COLUMN_METHOD, new Integer(type));
	}

	public int getMethod() {
		return this.getIntColumnValue(COLUMN_METHOD);
	}

	public void setAmount(int amount) {
		this.setColumn(COLUMN_AMOUNT, new Integer(amount));
	}

	public int getAmount() {
		return this.getIntColumnValue(COLUMN_AMOUNT);
	}

	public void setCheckFee(int fee)  {
		this.setColumn(COLUMN_CHECK_FEE, new Integer(fee));
	}

	public int getCheckFee()  {
		return this.getIntColumnValue(COLUMN_CHECK_FEE);
	}

	public void setManagerId(int id)  {
		this.setColumn(COLUMN_MANAGER_ID, new Integer(id));
	}

	public int getManagerId()  {
		return this.getIntColumnValue(COLUMN_MANAGER_ID);
	}

	public void setNotes(String notes)  {
		this.setColumn(COLUMN_NOTES, notes);
	}

	public String getNotes()  {
		return this.getStringColumnValue(COLUMN_NOTES);
	}

	public void setRule1(boolean flag)  {
		this.setColumn(COLUMN_RULE_1, new Boolean(flag));
	}

	public boolean getRule1()  {
		return this.getBooleanColumnValue(COLUMN_RULE_1);
	}

	public void setRule2(boolean flag)  {
		this.setColumn(COLUMN_RULE_2, new Boolean(flag));
	}

	public boolean getRule2()  {
		return this.getBooleanColumnValue(COLUMN_RULE_2);
	}

	public void setRule3(boolean flag)  {
		this.setColumn(COLUMN_RULE_3, new Boolean(flag));
	}

	public boolean getRule3()  {
		return this.getBooleanColumnValue(COLUMN_RULE_3);
	}

	public void setRule4(boolean flag)  {
		this.setColumn(COLUMN_RULE_4, new Boolean(flag));
	}

	public boolean getRule4()  {
		return this.getBooleanColumnValue(COLUMN_RULE_4);
	}

	public void setRule5(boolean flag)  {
		this.setColumn(COLUMN_RULE_5, new Boolean(flag));
	}

	public boolean getRule5()  {
		return this.getBooleanColumnValue(COLUMN_RULE_5);
	}

	public void setUserNotes(String userNotes)  {
		this.setColumn(COLUMN_USER_NOTES, userNotes);
	}

	public String getUserNotes()  {
		return this.getStringColumnValue(COLUMN_USER_NOTES);
	}

	public Collection ejbFindChecks() throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(this.getEntityName());
		return this.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindChecksByUser(User user) throws FinderException {
			return super.ejbFindAllCasesByUser(user);
	}
	
	public Collection ejbFindChecksByUserAndStatus(User user,String caseStatus) throws FinderException {
			return super.ejbFindAllCasesByUserAndStatus(user,caseStatus);
	}

	public Collection ejbFindApprovedChecksByUser(User user) throws FinderException {
		return ejbFindChecksByUserAndStatus(user,this.getCaseStatusGranted());
	}

	public Collection ejbFindNonApprovedChecks() throws FinderException {
		String[] statusArray = { getCaseStatusOpen(),CASE_STATUS_KEYS[2] };
		return ejbFindAllCasesByStatusArray(statusArray);
	}

	/**
	 * Finds all cases for all users with the specified caseStatus and the associated caseCode
	 */
	public Collection ejbFindAllCasesByStatus(CaseStatus caseStatus) throws FinderException {
		return super.ejbFindAllCasesByStatus(caseStatus.getStatus());
	}
	
	public Integer ejbFindCheckForChild(int childID) throws FinderException {
	    SelectQuery query = idoSelectQueryGetAllCases();
		query.addCriteria(idoCriteriaForChild(new Integer(childID)));
		return (Integer) super.idoFindOnePKByQuery(query);
	}
	
	protected Criteria idoCriteriaForChild(Integer childID){
	    return new MatchCriteria(idoTableSubCase(),COLUMN_CHILD_ID,MatchCriteria.EQUALS,childID);
	}
	
	public Collection ejbFindByStatusAndChild(String status, Integer child) throws FinderException{
	    SelectQuery query = idoSelectQueryGetAllCasesByStatus(status);
	    query.addCriteria(idoCriteriaForChild(child));
	    return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByUserAndChild(User user, Integer child) throws FinderException{
	    SelectQuery query = idoSelectQueryGetAllCasesByUser(user);
	    query.addCriteria(idoCriteriaForChild(child));
	    return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByStatusAndUserAndChild(String status,User user,Integer childID) throws FinderException{
	    SelectQuery query = idoSelectQueryGetAllCasesByUserAndStatus(user,status);
	    query.addCriteria(idoCriteriaForChild(childID));
	    return idoFindPKsByQuery(query);
	}
}
