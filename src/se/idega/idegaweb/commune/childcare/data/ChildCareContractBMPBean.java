/*
 * Created on 26.3.2003
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.contract.data.Contract;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * The main class of a childcare contract. Is thought to be an extension of the standard Contract object (com.idega.block.contract.data.Contract).
 * @author laddi
 */
public class ChildCareContractBMPBean extends GenericEntity implements ChildCareContract {

	public static final String ENTITY_NAME = "comm_childcare_archive";
	
	public static final String COLUMN_CHILD_ID = "child_id";
	public static final String COLUMN_APPLICATION_ID = "application_id";
	public final static String COLUMN_CONTRACT_ID = "contract_id";
	public static final String COLUMN_CONTRACT_FILE_ID = "contract_file_id";
	public static final String COLUMN_SCH_CLASS_MEMBER = "sch_class_member_id";
	public static final String COLUMN_CREATED_DATE = "created_date";
	public static final String COLUMN_VALID_FROM_DATE = "valid_from_date";
	public static final String COLUMN_TERMINATED_DATE = "terminated_date";
	public static final String COLUMN_CARE_TIME = "care_time";
	public static final String COLUMN_WORK_SITUATION = "work_situation";
	
	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_CREATED_DATE,"",true,true,java.sql.Date.class);
		addAttribute(COLUMN_VALID_FROM_DATE,"",true,true,java.sql.Date.class);
		addAttribute(COLUMN_TERMINATED_DATE,"",true,true,java.sql.Date.class);
		addAttribute(COLUMN_CARE_TIME,"",true,true,java.lang.Integer.class);
		
		addManyToOneRelationship(COLUMN_CHILD_ID,User.class);
		addManyToOneRelationship(COLUMN_APPLICATION_ID,ChildCareApplication.class);
		addOneToOneRelationship(COLUMN_CONTRACT_FILE_ID,ICFile.class);
		addOneToOneRelationship(COLUMN_CONTRACT_ID,Contract.class);
		addOneToOneRelationship(COLUMN_SCH_CLASS_MEMBER,SchoolClassMember.class);
		addManyToOneRelationship(COLUMN_WORK_SITUATION,EmploymentType.class);
	}
	
	public Date getCreatedDate() {
		return (Date) getColumnValue(COLUMN_CREATED_DATE);
	}

	public Date getValidFromDate() {
		return (Date) getColumnValue(COLUMN_VALID_FROM_DATE);
	}
	
	public Date getTerminatedDate() {
		return (Date) getColumnValue(COLUMN_TERMINATED_DATE);
	}
	
	public int getCareTime() {
		return getIntColumnValue(COLUMN_CARE_TIME);
	}
	
	public int getChildID() {
		return getIntColumnValue(COLUMN_CHILD_ID);
	}
	
	public User getChild() {
		return (User) getColumnValue(COLUMN_CHILD_ID);
	}
	
	public int getApplicationID() {
		return getIntColumnValue(COLUMN_APPLICATION_ID);
	}
	
	public ChildCareApplication getApplication() {
		return (ChildCareApplication) getColumnValue(COLUMN_APPLICATION_ID);
	}

	public int getContractID() {
		return getIntColumnValue(COLUMN_CONTRACT_ID);
	}
	
	public Contract getContract() {
		return (Contract) getColumnValue(COLUMN_CONTRACT_ID);
	}
	
	public int getContractFileID() {
		return getIntColumnValue(COLUMN_CONTRACT_FILE_ID);
	}
	
	public ICFile getContractFile() {
		return (ICFile) getColumnValue(COLUMN_CONTRACT_FILE_ID);
	}
	
	public SchoolClassMember getSchoolClassMmeber() {
		return (SchoolClassMember) getColumnValue(COLUMN_SCH_CLASS_MEMBER);
	}
	
	public EmploymentType getEmploymentType() {
		return (EmploymentType) getColumnValue(COLUMN_WORK_SITUATION);
	}
	
	public void setCreatedDate(Date createdDate) {
		setColumn(COLUMN_CREATED_DATE, createdDate);
	}

	public void setValidFromDate(Date validFromDate) {
		setColumn(COLUMN_VALID_FROM_DATE, validFromDate);
	}
	
	public void setTerminatedDate(Date terminatedDate) {
		setColumn(COLUMN_TERMINATED_DATE, terminatedDate);
	}
	
	public void setCareTime(int careTime) {
		setColumn(COLUMN_CARE_TIME, careTime);
	}
	
	public void setChildID(int childID) {
		setColumn(COLUMN_CHILD_ID, childID);
	}
	
	public void setChild(User child) {
		setColumn(COLUMN_CHILD_ID, child);
	}
	
	public void setApplicationID(int applicationID) {
		setColumn(COLUMN_APPLICATION_ID, applicationID);
	}
	
	public void setApplication(ChildCareApplication application) {
		setColumn(COLUMN_APPLICATION_ID, application);
	}
	
	public void setContractID(int contractID) {
		setColumn(COLUMN_CONTRACT_ID, contractID);
	}
	
	public void setContract(Contract contract) {
		setColumn(COLUMN_CONTRACT_ID, contract);
	}
	
	public void setContractFileID(int contractFileID) {
		setColumn(COLUMN_CONTRACT_FILE_ID, contractFileID);
	}
	
	public void setContractFile(ICFile contractFile) {
		setColumn(COLUMN_CONTRACT_FILE_ID, contractFile);
	}
	
	public void setSchoolClassMemberID(int schoolClassMemberID) {
		setColumn(COLUMN_SCH_CLASS_MEMBER, schoolClassMemberID);
	}
	
	public void setSchoolClassMember(SchoolClassMember schoolClassMember) {
		setColumn(COLUMN_SCH_CLASS_MEMBER, schoolClassMember);
	}
	
	public void setTerminationDateAsNull(boolean setAsNull) {
		if (setAsNull)
			removeFromColumn(COLUMN_TERMINATED_DATE);
	}
	
	public void setEmploymentType(int employmentTypeID) {
		setColumn(COLUMN_WORK_SITUATION, employmentTypeID);
	}
	
	public Collection ejbFindByChild(int childID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CHILD_ID, childID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindByChildAndProvider(int childID, int providerID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).append(" a, ").append(ChildCareApplicationBMPBean.ENTITY_NAME).append(" c");
		sql.appendWhereEquals("a."+COLUMN_CHILD_ID, childID);
		sql.appendAndEquals("a."+COLUMN_CHILD_ID, "c."+ChildCareApplicationBMPBean.CHILD_ID);
		sql.appendAndEquals("c."+ChildCareApplicationBMPBean.PROVIDER_ID, providerID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindByApplication(int applicationID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}

	public Integer ejbFindValidContractByApplication(int applicationID, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(date);
		sql.appendOr().append(COLUMN_TERMINATED_DATE).append(" is null").appendRightParenthesis();
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return (Integer) idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindValidContractByProvider(int providerID, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).append(" c, ");
		sql.append(ChildCareApplicationBMPBean.ENTITY_NAME).append(" a");
		sql.appendWhere().append("c."+COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().appendLeftParenthesis().append("c."+COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(date);
		sql.appendOr().append("c."+COLUMN_TERMINATED_DATE).append(" is null").appendRightParenthesis();
		sql.appendAndEquals("c."+COLUMN_APPLICATION_ID,"a."+ChildCareApplicationBMPBean.ENTITY_NAME+"_id");
		sql.appendAndEquals("a."+ChildCareApplicationBMPBean.PROVIDER_ID, providerID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}
	
	public Integer ejbFindApplicationByContract(int contractID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CONTRACT_ID, contractID);
		return (Integer) idoFindOnePKByQuery(sql);
	}	

	public Integer ejbFindValidContractByChild(int childID, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CHILD_ID, childID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(date);
		sql.appendOr().append(COLUMN_TERMINATED_DATE).append(" is null").appendRightParenthesis();
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return (Integer) idoFindOnePKByQuery(sql);
	}

	public Integer ejbFindLatestTerminatedContractByChild(int childID, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CHILD_ID, childID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().append(COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(date);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return (Integer) idoFindOnePKByQuery(sql);
	}

	public Integer ejbFindLatestContractByChild(int childID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CHILD_ID, childID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return (Integer) idoFindOnePKByQuery(sql);
	}

	public Collection ejbFindFutureContractsByApplication(int applicationID, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendGreaterThanSign().append(date);
		return idoFindPKsByQuery(sql);
	}

	public int ejbHomeGetNumberOfActiveNotWithProvider(int childID, int providerID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(*) from ").append(this.getEntityName()).append(" a, comm_childcare c");
		sql.appendWhereEquals("a."+this.COLUMN_APPLICATION_ID, "c.comm_childcare_id");
		sql.appendAndEquals("a." + COLUMN_CHILD_ID, childID);
		sql.appendAnd().append("c.provider_id").appendNOTEqual().append(providerID);
		sql.appendAnd().append(this.COLUMN_TERMINATED_DATE).append(" is null");
		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetNumberOfActiveForApplication(int applicationID, Date date) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(date);
		sql.appendOr().append(COLUMN_TERMINATED_DATE).append(" is null").appendRightParenthesis();
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoGetNumberOfRecords(sql);
	}


	public int ejbHomeGetNumberOfTerminatedLaterNotWithProvider(int childID, int providerID, Date date) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(*) from ").append(this.getEntityName()).append(" a, comm_childcare c");
		sql.appendWhereEquals("a."+this.COLUMN_APPLICATION_ID, "c.comm_childcare_id");
		sql.appendAndEquals("a." + COLUMN_CHILD_ID, childID);
		sql.appendAnd().append("c.provider_id").appendNOTEqual().append(providerID);
		sql.appendAnd().append(this.COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(date);
		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetFutureContractsCountByApplication(int applicationID, Date date) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendGreaterThanSign().append(date);
		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetContractsCountByApplication(int applicationID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetContractsCountByDateRangeAndProvider(Date startDate, Date endDate, int providerID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(*) from ").append(this.getEntityName()).append(" a, ").append(ChildCareApplicationBMPBean.ENTITY_NAME).append(" c");
		sql.appendWhereEquals("a."+COLUMN_CHILD_ID, "c."+ChildCareApplicationBMPBean.CHILD_ID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(endDate);
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(startDate);
		sql.appendOr().append(COLUMN_TERMINATED_DATE).append(" is null").appendRightParenthesis();
		sql.appendAndEquals("c."+ChildCareApplicationBMPBean.PROVIDER_ID, providerID);
		return idoGetNumberOfRecords(sql);
	}

	public Integer ejbFindByContractFileID(int contractFileID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CONTRACT_FILE_ID, contractFileID);
		return (Integer) idoFindOnePKByQuery(sql);
	}

	public Collection ejbFindByDateRange(Date startDate, Date endDate) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelect().append(" distinct a.* from "+getEntityName()).append(" a, ").append(ChildCareApplicationBMPBean.ENTITY_NAME).append(" c");
		sql.appendWhereEquals("a."+COLUMN_CHILD_ID, "c."+ChildCareApplicationBMPBean.CHILD_ID);
		sql.appendAnd().append(COLUMN_VALID_FROM_DATE).appendLessThanOrEqualsSign().append(endDate);
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TERMINATED_DATE).appendGreaterThanSign().append(startDate);
		sql.appendOr().append(COLUMN_TERMINATED_DATE).append(" is null").appendRightParenthesis();
		return idoFindPKsByQuery(sql);
	}

	
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQueryGetSelect();
		return idoFindPKsByQuery(sql);
	}
}
