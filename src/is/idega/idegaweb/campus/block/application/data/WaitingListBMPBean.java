/*
 * $Id: WaitingListBMPBean.java,v 1.14.4.4 2007/07/05 11:09:23 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.Complex;
import com.idega.data.GenericEntity;
import com.idega.data.query.InCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class WaitingListBMPBean extends GenericEntity implements WaitingList {
	private static final String ENTITY_NAME = "cam_waiting_list";

	private static final String COLUMN_COMPLEX = "bu_complex_id";

	// private static final String COLUMN_APARTMENT_TYPE =
	// "bu_apartment_type_id";

	private static final String COLUMN_SUBCATEGORY = "bu_subcategory_id";

	private static final String COLUMN_APPLICANT = "app_applicant_id";

	private static final String COLUMN_ORDER = "ordered";

	private static final String COLUMN_LIST_TYPE = "list_type";

	private static final String COLUMN_CHOICE_NUMBER = "choice_number";

	private static final String COLUMN_LAST_CONFIRMATION = "last_confirmation";

	private static final String COLUMN_NUMBER_OF_REJECTIONS = "number_of_rejections";

	private static final String COLUMN_REJECT_FLAG = "reject_flag";

	private static final String COLUMN_REMOVED_FROM_LIST = "removed_from_list";

	private static final String COLUMN_PRIORITY_LEVEL = "priority_level";

	private static final String COLUMN_ACCEPTED_DATE = "accepted_date";
	
	private static final String COLUMN_APARTMENT = "bu_apartment_id";

	public static final String YES = "Y";

	public static final String NO = "N";

	private final String PRIORITY_A = "A";

	private final String PRIORITY_B = "B";

	private final String PRIORITY_C = "C";

	private final String PRIORITY_D = "D";

	private final String PRIORITY_E = "E";

	public static final String TYPE_APPLICATION = "A";

	public static final String TYPE_TRANSFER = "T";

	public WaitingListBMPBean() {
		super();
	}

	public WaitingListBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_COMPLEX, Complex.class);
		addManyToOneRelationship(COLUMN_SUBCATEGORY, ApartmentSubcategory.class);
		addManyToOneRelationship(COLUMN_APPLICANT, Applicant.class);
		addAttribute(COLUMN_ORDER, "Order", Integer.class);
		addAttribute(COLUMN_LIST_TYPE, "Waiting list type", String.class);
		addAttribute(COLUMN_CHOICE_NUMBER, "Choice number", Integer.class);
		addAttribute(COLUMN_LAST_CONFIRMATION, "Last confirmation date",
				Timestamp.class);
		addAttribute(COLUMN_NUMBER_OF_REJECTIONS, "Number of rejections",
				Integer.class);
		addAttribute(COLUMN_REJECT_FLAG, "Reject flag", Boolean.class);
		addAttribute(COLUMN_REMOVED_FROM_LIST, "Removed from list",
				Boolean.class);
		addAttribute(COLUMN_PRIORITY_LEVEL, "Priority level", String.class);
		addAttribute(COLUMN_ACCEPTED_DATE, "Accepted date", Timestamp.class);
		addManyToOneRelationship(COLUMN_APARTMENT, Apartment.class);

		setMaxLength(COLUMN_LIST_TYPE, 1);
		setMaxLength(COLUMN_REMOVED_FROM_LIST, 1);
		setMaxLength(COLUMN_PRIORITY_LEVEL, 1);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public static String getEntityTableName() {
		return ENTITY_NAME;
	}

	public static String getComplexIdColumnName() {
		return COLUMN_COMPLEX;
	}

	public static String getApartmentSubcategoryColumnName() {
		return COLUMN_SUBCATEGORY;
	}

	public static String getApplicantIdColumnName() {
		return COLUMN_APPLICANT;
	}

	public static String getOrderColumnName() {
		return COLUMN_ORDER;
	}

	public static String getPriorityColumnName() {
		return COLUMN_PRIORITY_LEVEL;
	}

	public void setComplexId(int id) {
		setColumn(COLUMN_COMPLEX, id);
	}

	public Integer getComplexId() {
		return getIntegerColumnValue(COLUMN_COMPLEX);
	}

	public void setApartmentSubcategory(int id) {
		setColumn(COLUMN_SUBCATEGORY, id);
	}

	public void setApartmentSubcategory(Integer id) {
		setColumn(COLUMN_SUBCATEGORY, id);
	}

	public void setApartmentSubcategory(ApartmentSubcategory subcategory) {
		setColumn(COLUMN_SUBCATEGORY, subcategory);
	}

	public Integer getApartmentSubcategoryID() {
		return getIntegerColumnValue(COLUMN_SUBCATEGORY);
	}

	public ApartmentSubcategory getApartmentSubcategory() {
		return (ApartmentSubcategory) getColumnValue(COLUMN_SUBCATEGORY);
	}

	public void setApplicantId(int id) {
		setColumn(COLUMN_APPLICANT, id);
	}

	public void setApplicantId(Integer id) {
		setColumn(COLUMN_APPLICANT, id);
	}

	public Integer getApplicantId() {
		return getIntegerColumnValue(COLUMN_APPLICANT);
	}

	private void setType(String type) {
		setColumn(COLUMN_LIST_TYPE, type);
	}

	public void setTypeApplication() {
		setType(TYPE_APPLICATION);
	}

	public void setTypeTransfer() {
		setType(TYPE_TRANSFER);
	}

	public String getType() {
		return getStringColumnValue(COLUMN_LIST_TYPE);
	}

	public void setOrder(int order) {
		setColumn(COLUMN_ORDER, order);
	}

	public void setOrder(Integer order) {
		setColumn(COLUMN_ORDER, order);
	}

	public Integer getOrder() {
		return getIntegerColumnValue(COLUMN_ORDER);
	}

	public void setLastConfirmationDate(Timestamp date) {
		setColumn(COLUMN_LAST_CONFIRMATION, date);
	}

	public Timestamp getLastConfirmationDate() {
		return (Timestamp) getColumnValue(COLUMN_LAST_CONFIRMATION);
	}

	public void setAcceptedDate(Timestamp date) {
		setColumn(COLUMN_ACCEPTED_DATE, date);
	}

	public Timestamp getAcceptedDate() {
		return (Timestamp) getColumnValue(COLUMN_ACCEPTED_DATE);
	}

	public void setNumberOfRejections(int count) {
		setColumn(COLUMN_NUMBER_OF_REJECTIONS, count);
	}

	public void setNumberOfRejections(Integer count) {
		setColumn(COLUMN_NUMBER_OF_REJECTIONS, count);
	}

	public void incrementRejections(boolean flagAsRejected) {
		int count = getNumberOfRejections();
		if (count < 0)
			count = 0;
		count++;
		setNumberOfRejections(count);
		setRejectFlag(flagAsRejected);
	}

	public int getNumberOfRejections() {
		return getIntColumnValue(COLUMN_NUMBER_OF_REJECTIONS);
	}

	public boolean getRejectFlag() {
		return getBooleanColumnValue(COLUMN_REJECT_FLAG);
	}

	public void setRejectFlag(boolean flag) {
		setColumn(COLUMN_REJECT_FLAG, flag);
	}

	public void setChoiceNumber(int choice) {
		setColumn(COLUMN_CHOICE_NUMBER, choice);
	}

	public void setChoiceNumber(Integer choice) {
		setColumn(COLUMN_CHOICE_NUMBER, choice);
	}

	public Integer getChoiceNumber() {
		return getIntegerColumnValue(COLUMN_CHOICE_NUMBER);
	}

	public boolean getRemovedFromList() {
		String removed = getStringColumnValue(COLUMN_REMOVED_FROM_LIST);
		if ((removed == null) || (removed.equals(NO))) {
			return false;
		} else if (removed.equals(YES)) {
			return true;
		} else {
			return false;
		}
	}

	public void setRemovedFromList(String removed) {
		if ((removed != null) && (removed.equalsIgnoreCase(YES))) {
			setColumn(COLUMN_REMOVED_FROM_LIST, YES);
		} else {
			setColumn(COLUMN_REMOVED_FROM_LIST, NO);
		}
	}

	public String getPriorityLevel() {
		return getStringColumnValue(COLUMN_PRIORITY_LEVEL);
	}

	public void setPriorityLevel(String level) {
		setColumn(COLUMN_PRIORITY_LEVEL, level);
	}

	public void setPriorityLevelA() {
		setPriorityLevel(PRIORITY_A);
	}

	public void setPriorityLevelB() {
		setPriorityLevel(PRIORITY_B);
	}

	public void setPriorityLevelC() {
		setPriorityLevel(PRIORITY_C);
	}

	public void setPriorityLevelD() {
		setPriorityLevel(PRIORITY_D);
	}

	public void setPriorityLevelE() {
		setPriorityLevel(PRIORITY_E);
	}

	public void setSamePriority(WaitingList listEntry) {
		setPriorityLevel(listEntry.getPriorityLevel());
	}
	
	public Apartment getApartment() {
		return (Apartment) getColumnValue(COLUMN_APARTMENT);
	}
	
	public void setApartment(Apartment apartment) {
		setColumn(COLUMN_APARTMENT, apartment);
	}

	public Collection ejbFindByApartmentSubcategoryForApplicationType(
			int subcatId) throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName());
		sql.append(" where ");
		sql.append(COLUMN_SUBCATEGORY);
		sql.append(" = ");
		sql.append(subcatId);
		sql.append(" and ");
		sql.append(COLUMN_LIST_TYPE);
		sql.append(" = ");
		sql.append(TYPE_APPLICATION);
		sql.append(" order by ");
		sql.append(COLUMN_PRIORITY_LEVEL);
		sql.append(", ");
		sql.append(COLUMN_ORDER);
		sql.append(", ");
		sql.append(getIDColumnName());

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByApartmentSubcategoryForTransferType(
			int subcatId) throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName());
		sql.append(" where ");
		sql.append(COLUMN_SUBCATEGORY);
		sql.append(" = ");
		sql.append(subcatId);
		sql.append(" and ");
		sql.append(COLUMN_LIST_TYPE);
		sql.append(" = ");
		sql.append(TYPE_TRANSFER);
		sql.append(" order by ");
		sql.append(COLUMN_PRIORITY_LEVEL);
		sql.append(", ");
		sql.append(COLUMN_ORDER);
		sql.append(", ");
		sql.append(getIDColumnName());

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByApartmentSubcategory(int subcatId) throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName());
		sql.append(" where ");
		sql.append(COLUMN_SUBCATEGORY);
		sql.append(" = ");
		sql.append(subcatId);
		sql.append(" order by ");
		sql.append(COLUMN_PRIORITY_LEVEL);
		sql.append(", ");
		sql.append(COLUMN_ORDER);
		sql.append(", ");
		sql.append(getIDColumnName());

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindNextForTransferByApartmentSubcategory(
			int subcatId, int orderedFrom, String setTranserferToPriorityLevel)
			throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName());
		sql.append(" where ");
		sql.append(COLUMN_SUBCATEGORY);
		sql.append(" = ");
		sql.append(subcatId);
		sql.append(" and ");
		sql.append(COLUMN_PRIORITY_LEVEL);
		sql.append(" in ('");
		sql.append(setTranserferToPriorityLevel);
		sql.append("') and ");
		sql.append(COLUMN_ORDER);
		sql.append(" > ");
		sql.append(orderedFrom);
		sql.append(" order by ");
		sql.append(COLUMN_ORDER);
		sql.append(", ");
		sql.append(getIDColumnName());

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByApartmentSubcategory(int[] subcatId)
			throws FinderException {
		Table laddiJoli = new Table(this);

		SelectQuery q2 = new SelectQuery(laddiJoli);
		q2.addColumn(new WildCardColumn(laddiJoli));
		q2
				.addCriteria(new InCriteria(laddiJoli, COLUMN_SUBCATEGORY,
						subcatId));

		return idoFindPKsByQuery(q2);
	}

	public Collection ejbFindByApplicantID(Integer ID) throws FinderException {
		String[] orderby = { COLUMN_PRIORITY_LEVEL, COLUMN_ORDER, getIDColumnName() };
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(COLUMN_APPLICANT, ID.intValue())
				.appendOrderBy(orderby));
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}

	public int getCountOfRecords(String sql) throws FinderException {
		try {
			return super.getIntTableValue(sql);
		} catch (SQLException e) {
			throw new FinderException(e.getMessage());
		}
	}
}