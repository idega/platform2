/*
 * $Id: ContractBMPBean.java,v 1.22.4.6 2009/02/06 15:43:53 palli Exp $
 * 
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 * 
 */
package is.idega.idegaweb.campus.block.allocation.data;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.data.Apartment;
import com.idega.data.IDOBoolean;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ContractBMPBean extends com.idega.data.GenericEntity implements Contract {

	private static final String ENTITY_NAME = "cam_contract";

	private static final String COLUMN_USER = "ic_user_id";

	private static final String COLUMN_APARTMENT = "bu_apartment_id";

	private static final String COLUMN_VALID_FROM = "valid_from";

	private static final String COLUMN_VALID_TO = "valid_to";

	private static final String COLUMN_STATUS = "status";

	private static final String COLUMN_RENTED = "rented";

	private static final String COLUMN_APPLICANT = "app_applicant_id";

	private static final String COLUMN_RESIGN_INFO = "resign_info";

	private static final String COLUMN_STATUS_DATE = "status_date";

	private static final String COLUMN_MOVING_DATE = "moving_date";

	private static final String COLUMN_DELIVER_DATE = "deliver_date";

	private static final String COLUMN_RETURN_DATE = "return_date";

	private static final String COLUMN_FILE = "ic_file_id";

	private static final String COLUMN_HAS_PHONE = "has_phone";

	private static final String COLUMN_PHONE_FROM_DATE = "phone_from";

	private static final String COLUMN_PHONE_TO_DATE = "phone_to";
	
	private static final String COLUMN_APPLICATION = "app_application_id";
	
	private static final String COLUMN_DISCOUNT_PERCENTAGE = "discount_perc";
	
	private static final String COLUMN_CHANGE_KEY_STATUS_AT = "change_key_at";
	
	private static final String COLUMN_CHANGE_KEY_STATUS_TO = "change_key_to";

	public static final String STATUS_CREATED = "C";

	public static final String STATUS_PRINTED = "P";

	public static final String STATUS_SIGNED = "S";

	public static final String STATUS_REJECTED = "R";

	public static final String STATUS_TERMINATED = "T";

	public static final String STATUS_ENDED = "E";

	public static final String STATUS_RESIGNED = "U";

	public static final String STATUS_GARBAGE = "G";

	public static final String STATUS_STORAGE = "Z";

	public static final String STATUS_DENIED = "D";

	public static final String STATUS_FINALIZED = "F";

	public static String getStatusColumnName() {
		return COLUMN_STATUS;
	}

	public static String getApplicantIdColumnName() {
		return COLUMN_APPLICANT;
	}

	public static String getValidToColumnName() {
		return COLUMN_VALID_TO;
	}

	public static String getValidFromColumnName() {
		return COLUMN_VALID_FROM;
	}

	public static String getApartmentIdColumnName() {
		return COLUMN_APARTMENT;
	}

	public static String getUserIdColumnName() {
		return COLUMN_USER;
	}

	public static String getResignInfoColumnName() {
		return COLUMN_RESIGN_INFO;
	}

	public static String getStatusDateColumnName() {
		return COLUMN_MOVING_DATE;
	}

	public static String getRentedColumnName() {
		return COLUMN_RENTED;
	}

	public static String getMovingDateColumnName() {
		return COLUMN_MOVING_DATE;
	}

	public static String getColumnReturnDate() {
		return COLUMN_RETURN_DATE;
	}

	public static String getColumnDeliverDate() {
		return COLUMN_DELIVER_DATE;
	}

	public static String getFileColumnName() {
		return COLUMN_FILE;
	}

	public static String getContractEntityName() {
		return ENTITY_NAME;
	}

	public static String getHasPhoneColumnName() {
		return COLUMN_HAS_PHONE;
	}
	
	public static String getPhoneFromDateColumnName() {
		return COLUMN_PHONE_FROM_DATE;
	}

	public static String getPhoneToDateColumnName() {
		return COLUMN_PHONE_TO_DATE;
	}
	
	public ContractBMPBean() {
	}

	public ContractBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_USER, "User id", true, true, java.lang.Integer.class, "one-to-many", User.class);
		addAttribute(COLUMN_APARTMENT, "Apartment id", true, true, java.lang.Integer.class, "one-to-many", Apartment.class);
		addAttribute(COLUMN_APPLICANT, "Applicant id", true, true, java.lang.Integer.class, "one-to-one", Applicant.class);
		addAttribute(COLUMN_VALID_FROM, "Valid from", true, true, java.sql.Date.class);
		addAttribute(COLUMN_VALID_TO, "Valid to", true, true, java.sql.Date.class);
		addAttribute(COLUMN_STATUS_DATE, "Resign date", true, true, java.sql.Date.class);
		addAttribute(COLUMN_MOVING_DATE, "Moving date", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DELIVER_DATE, "Deliver date", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_RETURN_DATE, "Return date", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_STATUS, "Status", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_RENTED, "Rented", true, true, java.lang.Boolean.class, 1);
		addAttribute(COLUMN_RESIGN_INFO, "Resign info", true, true, java.lang.String.class, 4000);
		addAttribute(COLUMN_FILE, "File id", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_HAS_PHONE, "Has phone", true, true, java.lang.Boolean.class, 1);
		addAttribute(COLUMN_PHONE_FROM_DATE, "Phone from", true, true, Timestamp.class);
		addAttribute(COLUMN_PHONE_TO_DATE, "Phone to", true, true, Timestamp.class);
		addAttribute(COLUMN_DISCOUNT_PERCENTAGE, "Discount percentage", Double.class);
		addAttribute(COLUMN_CHANGE_KEY_STATUS_AT, "Change key status at time", Timestamp.class);
		addAttribute(COLUMN_CHANGE_KEY_STATUS_TO, "Change key status to", Boolean.class);
		addManyToOneRelationship(COLUMN_APPLICATION, Application.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void setUserId(int id) {
		setColumn(COLUMN_USER, id);
	}

	public void setUserId(Integer id) {
		setColumn(COLUMN_USER, id);
	}

	public Integer getUserId() {
		return getIntegerColumnValue(COLUMN_USER);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public void setApplicantId(int id) {
		setColumn(COLUMN_APPLICANT, id);
	}

	public void setApplicantId(Integer id) {
		setColumn(COLUMN_APPLICANT, id);
	}

	public Integer getFileId() {
		return getIntegerColumnValue(COLUMN_FILE);
	}

	public void setFileId(int id) {
		setColumn(COLUMN_FILE, id);
	}

	public void setFileId(Integer id) {
		setColumn(COLUMN_FILE, id);
	}

	public Integer getApplicantId() {
		return getIntegerColumnValue(COLUMN_APPLICANT);
	}

	public Applicant getApplicant() {
		return (Applicant) getColumnValue(COLUMN_APPLICANT);
	}

	public void setApartmentId(int id) {
		setColumn(COLUMN_APARTMENT, id);
	}

	public void setApartmentId(Integer id) {
		setColumn(COLUMN_APARTMENT, id);
	}

	public Integer getApartmentId() {
		return getIntegerColumnValue(COLUMN_APARTMENT);
	}

	public Apartment getApartment() {
		return (Apartment) getColumnValue(COLUMN_APARTMENT);
	}

	public void setValidFrom(Date date) {
		setColumn(COLUMN_VALID_FROM, date);
	}

	public Date getValidFrom() {
		return (Date) getColumnValue(COLUMN_VALID_FROM);
	}

	public void setValidTo(Date date) {
		setColumn(COLUMN_VALID_TO, date);
	}

	public Date getValidTo() {
		return (Date) getColumnValue(COLUMN_VALID_TO);
	}

	public void setMovingDate(Date date) {
		setColumn(COLUMN_MOVING_DATE, date);
	}

	public Date getMovingDate() {
		return (Date) getColumnValue(COLUMN_MOVING_DATE);
	}

	public void setStatusDate(Date date) {
		setColumn(COLUMN_STATUS_DATE, date);
	}

	public Date getStatusDate() {
		return (Date) getColumnValue(COLUMN_STATUS_DATE);
	}

	public void setDeliverTime(Timestamp stamp) {
		setColumn(COLUMN_DELIVER_DATE, stamp);
	}

	public Timestamp getDeliverTime() {
		return (Timestamp) getColumnValue(COLUMN_DELIVER_DATE);
	}

	public void setReturnTime(Timestamp stamp) {
		setColumn(COLUMN_RETURN_DATE, stamp);
	}

	public Timestamp getReturnTime() {
		return (Timestamp) getColumnValue(COLUMN_RETURN_DATE);
	}

	public String getResignInfo() {
		return getStringColumnValue(COLUMN_RESIGN_INFO);
	}

	public void setResignInfo(String info) {
		setColumn(COLUMN_RESIGN_INFO, info);
	}

	public boolean getIsRented() {
		return getBooleanColumnValue(COLUMN_RENTED);
	}

	public void setIsRented(boolean rented) {
		setColumn(COLUMN_RENTED, rented);
	}

	public void setEnded() {
		setIsRented(false);
		setReturnTime(IWTimestamp.getTimestampRightNow());
	}

	public void setStarted() {
		// setIsRented(true);
		setStarted(IWTimestamp.getTimestampRightNow());
	}

	public void setStarted(Timestamp when) {
		setIsRented(true);
		setDeliverTime(when);
	}

	public void setStatus(String status) throws IllegalStateException {
		if ((status.equalsIgnoreCase(STATUS_CREATED)) || (status.equalsIgnoreCase(STATUS_ENDED))
				|| (status.equalsIgnoreCase(STATUS_REJECTED)) || (status.equalsIgnoreCase(STATUS_SIGNED))
				|| (status.equalsIgnoreCase(STATUS_TERMINATED)) || (status.equalsIgnoreCase(STATUS_RESIGNED))
				|| (status.equalsIgnoreCase(STATUS_GARBAGE)) || (status.equalsIgnoreCase(STATUS_STORAGE))
				|| (status.equalsIgnoreCase(STATUS_DENIED)) || (status.equalsIgnoreCase(STATUS_PRINTED))
				|| (status.equalsIgnoreCase(STATUS_FINALIZED))) {
			setColumn(COLUMN_STATUS, status);
			setStatusDate(new Date(System.currentTimeMillis()));
		}
		else
			throw new IllegalStateException("Undefined state : " + status);
	}

	public String getStatus() {
		return (String) getColumnValue(COLUMN_STATUS);
	}

	public void setHasPhone(boolean hasPhone) {
		if (hasPhone) {
			setColumn(COLUMN_PHONE_FROM_DATE, IWTimestamp.getTimestampRightNow());
		} else {
			setColumn(COLUMN_PHONE_TO_DATE, IWTimestamp.getTimestampRightNow());
		}
		setColumn(getHasPhoneColumnName(), hasPhone);
	}

	public boolean getHasPhone() {
		return getBooleanColumnValue(COLUMN_HAS_PHONE, false);
	}
	
	public Timestamp getPhoneFromDate() {
		return getTimestampColumnValue(COLUMN_PHONE_FROM_DATE);
	}
	
	public Timestamp getPhoneToDate() {
		return getTimestampColumnValue(COLUMN_PHONE_TO_DATE);
	}

	public void setStatusCreated() {
		setStatus(STATUS_CREATED);
	}

	public void setStatusEnded() {
		setStatus(STATUS_ENDED);
	}

	public void setStatusRejected() {
		setStatus(STATUS_REJECTED);
	}

	public void setStatusSigned() {
		setStatus(STATUS_SIGNED);
	}

	public void setStatusTerminated() {
		setStatus(STATUS_TERMINATED);
	}

	public void setStatusPrinted() {
		setStatus(STATUS_PRINTED);
	}

	public void setStatusResigned() {
		setStatus(STATUS_RESIGNED);
	}

	public void setStatusGarbage() {
		setStatus(STATUS_GARBAGE);
	}

	public void setStatusDenied() {
		setStatus(STATUS_DENIED);
	}

	public void setStatusStorage() {
		setStatus(STATUS_STORAGE);
	}

	public void setStatusFinalized() {
		setStatus(STATUS_FINALIZED);
	}

	public Application getApplication() {
		return (Application) getColumnValue(COLUMN_APPLICATION);
	}
	
	public int getApplicationID() {
		return getIntColumnValue(COLUMN_APPLICATION);
	}
	
	public void setApplication(Application application) {
		setColumn(COLUMN_APPLICATION, application);
	}
	
	public void setApplicationID(int id) {
		setColumn(COLUMN_APPLICATION, id);
	}

	public double getDiscountPercentage() {
		return getDoubleColumnValue(COLUMN_DISCOUNT_PERCENTAGE, 0.0d);
	}
	
	public void setDiscountPercentage(double percentage) {
		setColumn(COLUMN_DISCOUNT_PERCENTAGE, percentage);
	}
	
	public Timestamp getChangeKeyStatusAt() {
		return getTimestampColumnValue(COLUMN_CHANGE_KEY_STATUS_AT);
	}
	
	public void setChangeKeyStatusAt(Timestamp at) {
		setColumn(COLUMN_CHANGE_KEY_STATUS_AT, at);
	}
	
	public boolean getChangeKeyStatusTo() {
		return getBooleanColumnValue(COLUMN_CHANGE_KEY_STATUS_TO);
	}
	
	public void setChangeKeyStatusTo(boolean to) {
		setColumn(COLUMN_CHANGE_KEY_STATUS_TO, to);
	}
	
	//ejb
	public Collection ejbFindByApplicantID(Integer ID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),
				ID.intValue()));
	}

	public Collection ejbFindByUserID(Integer ID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(), ID.intValue()));
	}

	public Collection ejbFindByApartmentAndUser(Integer AID, Integer UID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(),
				UID.intValue()).appendAndEquals(getApartmentIdColumnName(), AID.intValue()));
	}

	public Collection ejbFindByUserAndRented(Integer ID, Boolean rented) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(), ID.intValue()).appendAndEqualsQuoted(
				getRentedColumnName(), IDOBoolean.toString(rented.booleanValue())));
	}

	public Collection ejbFindByApartmentID(Integer ID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),
				ID.intValue()).appendOrderByDescending(getValidToColumnName()));
	}

	public Collection ejbFindByApartmentAndStatus(Integer ID, String status) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),
				ID.intValue()).appendAndEqualsQuoted(getStatusColumnName(), status).appendOrderByDescending(
				getValidToColumnName()));
	}

	public Collection ejbFindByApartmentAndStatus(Integer ID, String[] status) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),
				ID.intValue()).appendAnd().append(getStatusColumnName()).appendInArrayWithSingleQuotes(status).appendOrderByDescending(
				getValidToColumnName()));
	}

	public Collection ejbFindByApplicantAndStatus(Integer ID, String status) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),
				ID.intValue()).appendAndEqualsQuoted(getStatusColumnName(), status));
	}

	public Collection ejbFindByApplicantAndStatus(Integer ID, String status[]) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),
				ID.intValue()).appendAnd().append(getStatusColumnName()).appendInArrayWithSingleQuotes(status));
	}

	public Collection ejbFindByApplicantAndRented(Integer ID, Boolean rented) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),
				ID.intValue()).appendAndEqualsQuoted(getRentedColumnName(), IDOBoolean.toString(rented.booleanValue())));
	}

	public Collection ejbFindByApartmentAndRented(Integer ID, Boolean rented) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),
				ID.intValue()).appendAndEqualsQuoted(getRentedColumnName(), IDOBoolean.toString(rented.booleanValue())));
	}

	public Collection ejbFindByStatus(String status) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEqualsQuoted(getStatusColumnName(), status));
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}

	public java.util.Collection ejbFindByApplicant(Integer ID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),
				ID.intValue()));
	}

	public Collection ejbFindByApplicantInCreatedStatus(Integer applicant) throws FinderException {
		return ejbFindByApplicantAndStatus(applicant, STATUS_CREATED);
	}

	public Collection ejbFindByApplicantInCreatedAndPrintedStatus(Integer applicant) throws FinderException {
		String statuses[] = {STATUS_CREATED, STATUS_PRINTED};
		return ejbFindByApplicantAndStatus(applicant, statuses);
	}

	
	public Date ejbHomeGetLastValidToForApartment(Integer apartment) throws FinderException {
		if (apartment != null) {
			try {
				return getDateTableValue("select max(c.valid_to) from cam_contract c where c.bu_apartment_id =  "
						+ apartment);
			}
			catch (SQLException e) {
				throw new FinderException(e.getMessage());
			}
		}
		return null;
	}

	public Date ejbHomeGetLastValidFromForApartment(Integer apartment) throws FinderException {
		if (apartment != null) {
			try {
				return getDateTableValue("select max(c.valid_from) from cam_contract c where c.bu_apartment_id =  "
						+ apartment);
			}
			catch (SQLException e) {
				throw new FinderException(e.getMessage());
			}
		}
		return null;
	}

	public Collection ejbFindBySearchConditions(String status, Integer complexId, Integer buildingId, Integer floorId,
			Integer typeId, Integer categoryId, int order, int returnResultSize, int startingIndex)
			throws FinderException {
		String sql = getSearchConditionSQL(status, complexId, buildingId, floorId, typeId, categoryId, order, false);
		return super.idoFindPKsBySQL(sql.toString(), returnResultSize, startingIndex);
	}

	public int ejbHomeCountBySearchConditions(String status, Integer complexId, Integer buildingId, Integer floorId,
			Integer typeId, Integer categoryId, int order) throws IDOException {
		String sql = getSearchConditionSQL(status, complexId, buildingId, floorId, typeId, categoryId, order, true);
		return idoGetNumberOfRecords(sql);
	}

	private String getSearchConditionSQL(String status, Integer complexId, Integer buildingId, Integer floorId,
			Integer typeId, Integer categoryId, int order, boolean count) {
		StringBuffer sql = new StringBuffer("select ");
		if (count)
			sql.append(" count( * )");
		else
			sql.append(" con.* ");
		sql.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
		sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_sub_cat sub, bu_aprt_cat y,cam_contract con ");
		sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
		sql.append(" and y.bu_aprt_cat_id = sub.aprt_cat");
		sql.append(" and sub.bu_aprt_sub_cat_id = t.bu_aprt_subcat");
		sql.append(" and a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and b.bu_complex_id = c.bu_complex_id ");
		sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
		sql.append(" and con.app_applicant_id = p.app_applicant_id");
		if (status != null && !"".equals(status)) {
			sql.append(" and con.status = '");
			sql.append(status);
			sql.append("' ");
		}
		if (complexId != null && complexId.intValue() > 0) {
			sql.append(" and c.bu_complex_id  = ");
			sql.append(complexId);
		}
		if (buildingId != null && buildingId.intValue() > 0) {
			sql.append(" and b.bu_building_id = ");
			sql.append(buildingId);
		}
		if (floorId != null && floorId.intValue() > 0) {
			sql.append(" and f.bu_floor_id = ");
			sql.append(floorId);
		}
		if (typeId != null && typeId.intValue() > 0) {
			sql.append(" and t.bu_aprt_type_id = ");
			sql.append(typeId);
		}
		if (categoryId != null && categoryId.intValue() > 0) {
			sql.append(" and y.bu_aprt_cat_id = ");
			sql.append(categoryId);
		}
		if (!count && order >= 0) {
			sql.append(" order by ");
			sql.append(ContractFinder.getOrderString(order));
		}
		
		return sql.toString();
	}

	public Collection ejbFindByComplexAndBuildingAndApartmentName(Integer complexID, Integer buildingID,
			String apartmentName) throws FinderException {
		StringBuffer sql = new StringBuffer("select con.* ");
		sql.append(" from bu_apartment a, bu_floor f, bu_building b, cam_contract con ");
		sql.append(" where a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
		sql.append(" and b.bu_complex_id  = ");
		sql.append(complexID);
		sql.append(" and b.bu_building_id = ");
		sql.append(buildingID);
		sql.append(" and a.name = '");
		sql.append(apartmentName);
		sql.append("'");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByComplexAndRented(Integer complexID, boolean rented) throws FinderException {
		StringBuffer sql = new StringBuffer("select con.* ");
		sql.append(" from bu_apartment a, bu_floor f, bu_building b, cam_contract con ");
		sql.append(" where a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
		sql.append(" and b.bu_complex_id  = ");
		sql.append(complexID);
		if (rented) {
			sql.append(" and con.rented = 'Y'");			
		} else {
			sql.append(" and (con.rented is null or con.rented = 'N')");
		}
		
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindByPersonalID(String ID) throws FinderException {
		StringBuffer sql = new StringBuffer("select c.* ");
		sql.append(" from cam_contract c, app_applicant a where ");
		sql.append(" c.app_applicant_id = a.app_applicant_id and a.ssn like '");
		sql.append(ID);
		sql.append("'");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbHomeGetUnsignedApplicants(String personalID) throws FinderException {
		try {
			StringBuffer sql = new StringBuffer("select a.* ");
			sql.append(" from app_applicant a ");
			sql.append(" where a.app_applicant_id ");
			sql.append(" not in (select c.app_applicant_id from cam_contract c) ");
			sql.append(" and ssn like '");
			sql.append(personalID);
			sql.append("'");
			return idoGetRelatedEntitiesBySQL(Applicant.class, sql.toString());
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}

	}

	public Collection ejbFindByStatusAndValidBeforeDate(String status, Date date) throws FinderException {
		IDOQuery query = super.idoQueryGetSelect().appendWhereEqualsWithSingleQuotes(getStatusColumnName(), status).appendAnd().append(
				getValidToColumnName()).appendLessThanOrEqualsSign().append(date);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindByStatusAndChangeDate(String status, Date date) throws FinderException {
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEqualsWithSingleQuotes(getStatusColumnName(),
				status).appendAnd().append(getStatusDateColumnName()).appendLessThanOrEqualsSign().append(date));
	}

	private IDOQuery getQueryByStatusAndOverlapPeriod(String[] status, Date from, Date to) {
		IDOQuery query = super.idoQueryGetSelect().appendWhere();
		query.append(getStatusColumnName());
		query.appendInArrayWithSingleQuotes(status);
		query.appendAnd();
		query.appendOverlapPeriod(getValidFromColumnName(), getValidToColumnName(), from, to);
		return query;
	}

	public Collection ejbFindByStatusAndOverLapPeriodMultiples(String[] status, Date from, Date to)
			throws FinderException {
		IDOQuery outerQuery = getQueryByStatusAndOverlapPeriod(status, from, to);
		IDOQuery innerQuery = super.idoQuery().appendSelect().append(getUserIdColumnName());
		innerQuery.appendFrom().append(this.getTableName());
		innerQuery.appendWhere();
		innerQuery.append(getStatusColumnName());
		innerQuery.appendInArrayWithSingleQuotes(status);
		innerQuery.appendAnd();
		innerQuery.appendOverlapPeriod(getValidFromColumnName(), getValidToColumnName(), from, to);
		innerQuery.appendGroupBy(getUserIdColumnName());
		innerQuery.appendHaving().appendCount(getIDColumnName()).appendGreaterThanSign().append(1);
		outerQuery.appendAnd().append(getUserIdColumnName()).appendIn(innerQuery);

		outerQuery.appendOrderBy(getUserIdColumnName() + "," + getIDColumnName());

		return super.idoFindPKsByQuery(outerQuery);
	}

	public Collection ejbFindByUserAndStatus(Integer userId, String[] status) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(), userId).appendAnd().append(
				getStatusColumnName()).appendInArrayWithSingleQuotes(status));
	}

	public Collection ejbFindByUserAndStatus(Integer userId, String status) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(), userId).appendAndEqualsQuoted(
				getStatusColumnName(), status));
	}
	
	public Collection ejbFindByUserAndStatusAndRentedBeforeDate(Integer userId, String status, Date date) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(), userId).appendAndEqualsQuoted(
				getStatusColumnName(), status).appendAnd().append(this.COLUMN_VALID_FROM).appendLessThanOrEqualsSign().append(date));
	}
	
	public Collection ejbFindAllWithKeyChangeDateSet() throws FinderException {
		IDOQuery query = super.idoQueryGetSelect();
		query.appendWhere();
		query.append(COLUMN_CHANGE_KEY_STATUS_AT);
		query.appendIsNotNull();

		return super.idoFindPKsByQuery(query);
	}
}