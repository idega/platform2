/*
 * $Id: ContractBMPBean.java,v 1.22 2005/10/13 08:06:51 palli Exp $
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

	private static final String NAME = "cam_contract";

	private static final String USER_ID = "ic_user_id";

	private static final String APARTMENT_ID = "bu_apartment_id";

	private static final String VALID_FROM = "valid_from";

	private static final String VALID_TO = "valid_to";

	private static final String STATUS = "status";

	private static final String RENTED = "rented";

	private static final String APPLICANT_ID = "app_applicant_id";

	private static final String RESIGN_INFO = "resign_info";

	private static final String STATUS_DATE = "status_date";

	private static final String MOVING_DATE = "moving_date";

	private static final String DELIVER_DATE = "deliver_date";

	private static final String RETURN_DATE = "return_date";

	private static final String FILE = "ic_file_id";

	private static final String HAS_PHONE = "has_phone";

	private static final String PHONE_FROM_DATE = "phone_from";

	private static final String PHONE_TO_DATE = "phone_to";

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
		return STATUS;
	}

	public static String getApplicantIdColumnName() {
		return APPLICANT_ID;
	}

	public static String getValidToColumnName() {
		return VALID_TO;
	}

	public static String getValidFromColumnName() {
		return VALID_FROM;
	}

	public static String getApartmentIdColumnName() {
		return APARTMENT_ID;
	}

	public static String getUserIdColumnName() {
		return USER_ID;
	}

	public static String getResignInfoColumnName() {
		return RESIGN_INFO;
	}

	public static String getStatusDateColumnName() {
		return MOVING_DATE;
	}

	public static String getRentedColumnName() {
		return RENTED;
	}

	public static String getMovingDateColumnName() {
		return MOVING_DATE;
	}

	public static String getColumnReturnDate() {
		return RETURN_DATE;
	}

	public static String getColumnDeliverDate() {
		return DELIVER_DATE;
	}

	public static String getFileColumnName() {
		return FILE;
	}

	public static String getContractEntityName() {
		return NAME;
	}

	public static String getHasPhoneColumnName() {
		return HAS_PHONE;
	}
	
	public static String getPhoneFromDateColumnName() {
		return PHONE_FROM_DATE;
	}

	public static String getPhoneToDateColumnName() {
		return PHONE_TO_DATE;
	}
	
	public ContractBMPBean() {
	}

	public ContractBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(USER_ID, "User id", true, true, java.lang.Integer.class, "one-to-many", User.class);
		addAttribute(APARTMENT_ID, "Apartment id", true, true, java.lang.Integer.class, "one-to-many", Apartment.class);
		addAttribute(APPLICANT_ID, "Applicant id", true, true, java.lang.Integer.class, "one-to-one", Applicant.class);
		addAttribute(VALID_FROM, "Valid from", true, true, java.sql.Date.class);
		addAttribute(VALID_TO, "Valid to", true, true, java.sql.Date.class);
		addAttribute(STATUS_DATE, "Resign date", true, true, java.sql.Date.class);
		addAttribute(MOVING_DATE, "Moving date", true, true, java.sql.Date.class);
		addAttribute(DELIVER_DATE, "Deliver date", true, true, java.sql.Timestamp.class);
		addAttribute(RETURN_DATE, "Return date", true, true, java.sql.Timestamp.class);
		addAttribute(STATUS, "Status", true, true, java.lang.String.class, 1);
		addAttribute(RENTED, "Rented", true, true, java.lang.Boolean.class, 1);
		addAttribute(RESIGN_INFO, "Resign info", true, true, java.lang.String.class, 4000);
		addAttribute(FILE, "File id", true, true, java.lang.Integer.class);
		addAttribute(HAS_PHONE, "Has phone", true, true, java.lang.Boolean.class, 1);
		addAttribute(PHONE_FROM_DATE, "Phone from", true, true, Timestamp.class);
		addAttribute(PHONE_TO_DATE, "Phone to", true, true, Timestamp.class);
	}

	public String getEntityName() {
		return (NAME);
	}

	public void setUserId(int id) {
		setColumn(USER_ID, id);
	}

	public void setUserId(Integer id) {
		setColumn(USER_ID, id);
	}

	public Integer getUserId() {
		return (getIntegerColumnValue(USER_ID));
	}

	public User getUser() {
		return (User) getColumnValue(USER_ID);
	}

	public void setApplicantId(int id) {
		setColumn(APPLICANT_ID, id);
	}

	public void setApplicantId(Integer id) {
		setColumn(APPLICANT_ID, id);
	}

	public Integer getFileId() {
		return (getIntegerColumnValue(FILE));
	}

	public void setFileId(int id) {
		setColumn(FILE, id);
	}

	public void setFileId(Integer id) {
		setColumn(FILE, id);
	}

	public Integer getApplicantId() {
		return (getIntegerColumnValue(APPLICANT_ID));
	}

	public Applicant getApplicant() {
		return (Applicant) getColumnValue(APPLICANT_ID);
	}

	public void setApartmentId(int id) {
		setColumn(APARTMENT_ID, id);
	}

	public void setApartmentId(Integer id) {
		setColumn(APARTMENT_ID, id);
	}

	public Integer getApartmentId() {
		return (getIntegerColumnValue(APARTMENT_ID));
	}

	public Apartment getApartment() {
		return (Apartment) getColumnValue(APARTMENT_ID);
	}

	public void setValidFrom(Date date) {
		setColumn(VALID_FROM, date);
	}

	public Date getValidFrom() {
		return ((Date) getColumnValue(VALID_FROM));
	}

	public void setValidTo(Date date) {
		setColumn(VALID_TO, date);
	}

	public Date getValidTo() {
		return ((Date) getColumnValue(VALID_TO));
	}

	public void setMovingDate(Date date) {
		setColumn(MOVING_DATE, date);
	}

	public Date getMovingDate() {
		return (Date) getColumnValue(MOVING_DATE);
	}

	public void setStatusDate(Date date) {
		setColumn(STATUS_DATE, date);
	}

	public Date getStatusDate() {
		return (Date) getColumnValue(STATUS_DATE);
	}

	public void setDeliverTime(Timestamp stamp) {
		setColumn(DELIVER_DATE, stamp);
	}

	public Timestamp getDeliverTime() {
		return ((Timestamp) getColumnValue(DELIVER_DATE));
	}

	public void setReturnTime(Timestamp stamp) {
		setColumn(RETURN_DATE, stamp);
	}

	public Timestamp getReturnTime() {
		return ((Timestamp) getColumnValue(RETURN_DATE));
	}

	public String getResignInfo() {
		return getStringColumnValue(RESIGN_INFO);
	}

	public void setResignInfo(String info) {
		setColumn(RESIGN_INFO, info);
	}

	public boolean getIsRented() {
		return getBooleanColumnValue(RENTED);
	}

	public void setIsRented(boolean rented) {
		setColumn(RENTED, rented);
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
			setColumn(STATUS, status);
			setStatusDate(new Date(System.currentTimeMillis()));
		}
		else
			throw new IllegalStateException("Undefined state : " + status);
	}

	public String getStatus() {
		return ((String) getColumnValue(STATUS));
	}

	public void setHasPhone(boolean hasPhone) {
		if (hasPhone) {
			setColumn(PHONE_FROM_DATE, IWTimestamp.getTimestampRightNow());
		} else {
			setColumn(PHONE_TO_DATE, IWTimestamp.getTimestampRightNow());
		}
		setColumn(getHasPhoneColumnName(), hasPhone);
	}

	public boolean getHasPhone() {
		return getBooleanColumnValue(HAS_PHONE, false);
	}
	
	public Timestamp getPhoneFromDate() {
		return getTimestampColumnValue(PHONE_FROM_DATE);
	}
	
	public Timestamp getPhoneToDate() {
		return getTimestampColumnValue(PHONE_TO_DATE);
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
		sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
		sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
		sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
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
			sql.append(" and bu_complex_id  = ");
			sql.append(complexId);
		}
		if (buildingId != null && buildingId.intValue() > 0) {
			sql.append(" and bu_building_id = ");
			sql.append(buildingId);
		}
		if (floorId != null && floorId.intValue() > 0) {
			sql.append(" and bu_floor_id = ");
			sql.append(floorId);
		}
		if (typeId != null && typeId.intValue() > 0) {
			sql.append(" and bu_aprt_type_id = ");
			sql.append(typeId);
		}
		if (categoryId != null && categoryId.intValue() > 0) {
			sql.append(" and bu_aprt_cat_id = ");
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
		// System.out.println(outerQuery.toString());

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
				getStatusColumnName(), status).appendAnd().append(this.VALID_FROM).appendLessThanOrEqualsSign().append(date));
	}
}