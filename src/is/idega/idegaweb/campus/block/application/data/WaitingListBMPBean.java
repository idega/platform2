/*
 * $Id: WaitingListBMPBean.java,v 1.13 2004/06/24 13:23:33 aron Exp $
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

import com.idega.data.GenericEntity;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class WaitingListBMPBean extends GenericEntity implements WaitingList {
  private static final String ENTITY_NAME = "cam_waiting_list";
  private static final String COMPLEX_ID = "bu_complex_id";
  private static final String APARTMENT_TYPE_ID = "bu_apartment_type_id";
  private static final String APPLICANT_ID = "app_applicant_id";
  private static final String ORDER = "ordered";
  private static final String TYPE = "list_type";
  private static final String CHOICE_NUMBER = "choice_number";
  private static final String LAST_CONFIRMATION = "last_confirmation";
  private static final String NUMBER_OF_REJECTIONS = "number_of_rejections";
  private static final String REJECT_FLAG = "reject_flag";
  private static final String REMOVED_FROM_LIST = "removed_from_list";
  private static final String PRIORITY_LEVEL = "priority_level";
  private static final String ACCEPTED_DATE = "accepted_date";

  public static final String YES = "Y";
  public static final String NO = "N";

  private final String PRIORITY_A = "A";
  private final String PRIORITY_B = "B";
  private final String PRIORITY_C = "C";
  private final String PRIORITY_D = "D";
  private final String PRIORITY_E = "E";


  private final String TYPE_APPLICATION = "A";
  private final String TYPE_TRANSFER = "T";

  public WaitingListBMPBean() {
    super();
  }

  public WaitingListBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(COMPLEX_ID,"Complex",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.Complex");
    addAttribute(APARTMENT_TYPE_ID,"Apartment type",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.ApartmentType");
    addAttribute(APPLICANT_ID,"Applicant",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(ORDER,"Order",true,true,"java.lang.Integer");
    addAttribute(TYPE,"Waiting list type",true,true,"java.lang.String");
    addAttribute(CHOICE_NUMBER,"Choice number",true,true,"java.lang.Integer");
    addAttribute(LAST_CONFIRMATION,"Last confirmation date",true,true,"java.sql.Timestamp");
    addAttribute(NUMBER_OF_REJECTIONS,"Number of rejections",true,true,"java.lang.Integer");
	addAttribute(REJECT_FLAG,"Reject flag",true,true,Boolean.class);
    addAttribute(REMOVED_FROM_LIST,"Removed from list",true,true,"java.lang.String");
    addAttribute(PRIORITY_LEVEL,"Priority level",true,true,String.class);
	addAttribute(ACCEPTED_DATE,"Accepted date",true,true,java.sql.Timestamp.class);
	
    setMaxLength(TYPE,1);
    setMaxLength(REMOVED_FROM_LIST,1);
    setMaxLength(PRIORITY_LEVEL,1);
  }

  public String getEntityName() {
    return ENTITY_NAME;
  }

  public static String getEntityTableName(){
    return ENTITY_NAME;
  }

  public static String getComplexIdColumnName() {
    return COMPLEX_ID;
  }

  public static String getApartmentTypeIdColumnName() {
    return APARTMENT_TYPE_ID;
  }

  public static String getApplicantIdColumnName() {
    return APPLICANT_ID;
  }

  public static String getTypeColumnName() {
    return TYPE;
  }

  public static String getOrderColumnName() {
    return ORDER;
  }

  public static String getChoiceNumberColumnName() {
    return CHOICE_NUMBER;
  }

  public static String getLastConfirmationColumnName() {
    return LAST_CONFIRMATION;
  }

  public static String getNumberOfRejectionsColumnName() {
    return NUMBER_OF_REJECTIONS;
  }

  public static String getRemovedFromListColumnName() {
    return REMOVED_FROM_LIST;
  }

  public void setComplexId(int id) {
    setColumn(COMPLEX_ID,id);
  }

  public Integer getComplexId() {
    return getIntegerColumnValue(COMPLEX_ID);
  }

  public void setApartmentTypeId(int id) {
    setColumn(APARTMENT_TYPE_ID,id);
  }

  public void setApartmentTypeId(Integer id) {
    setColumn(APARTMENT_TYPE_ID,id);
  }

  public Integer getApartmentTypeId() {
    return getIntegerColumnValue(APARTMENT_TYPE_ID);
  }

  public void setApplicantId(int id) {
    setColumn(APPLICANT_ID,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(APPLICANT_ID,id);
  }

  public Integer getApplicantId() {
    return getIntegerColumnValue(APPLICANT_ID);
  }

  private void setType(String type) {
    setColumn(TYPE,type);
  }

  public void setTypeApplication() {
    setType(TYPE_APPLICATION);
  }

  public void setTypeTransfer() {
    setType(TYPE_TRANSFER);
  }

  public String getType() {
    return getStringColumnValue(TYPE);
  }

  public void setOrder(int order) {
    setColumn(ORDER,order);
  }

  public void setOrder(Integer order) {
    setColumn(ORDER,order);
  }

  public Integer getOrder() {
    return getIntegerColumnValue(ORDER);
  }

  public void setLastConfirmationDate(Timestamp date) {
    setColumn(LAST_CONFIRMATION,date);
  }

  public Timestamp getLastConfirmationDate() {
    return (Timestamp)getColumnValue(LAST_CONFIRMATION);
  }
  
  public void setAcceptedDate(Timestamp date) {
	  setColumn(ACCEPTED_DATE,date);
	}

	public Timestamp getAcceptedDate() {
	  return (Timestamp)getColumnValue(ACCEPTED_DATE);
	}

  public void setNumberOfRejections(int count) {
    setColumn(NUMBER_OF_REJECTIONS,count);
  }

  public void setNumberOfRejections(Integer count) {
    setColumn(NUMBER_OF_REJECTIONS,count);
  }
  
  public void incrementRejections(boolean flagAsRejected){
  	int count = getNumberOfRejections();
  	if(count <0 )
  		count = 0;
  	count++;
  	setNumberOfRejections(count);
  	setRejectFlag(flagAsRejected);
  }

  public int getNumberOfRejections() {
    return getIntColumnValue(NUMBER_OF_REJECTIONS);
  }
  
  public boolean getRejectFlag(){
  	return getBooleanColumnValue(REJECT_FLAG);
  }
  
  public void setRejectFlag(boolean flag){
  	setColumn(REJECT_FLAG,flag);
  }

  public void setChoiceNumber(int choice) {
    setColumn(CHOICE_NUMBER,choice);
  }

  public void setChoiceNumber(Integer choice) {
    setColumn(CHOICE_NUMBER,choice);
  }

  public Integer getChoiceNumber() {
    return getIntegerColumnValue(CHOICE_NUMBER);
  }

  public boolean getRemovedFromList() {
    String removed = getStringColumnValue(REMOVED_FROM_LIST);
    if ((removed == null) || (removed.equals(NO))) {
      return false;
    }
    else if (removed.equals(YES)) {
      return true;
    }
    else {
      return false;
    }
  }

  public void setRemovedFromList(String removed) {
    if ((removed != null) && (removed.equalsIgnoreCase(YES))) {
      setColumn(REMOVED_FROM_LIST,YES);
    }
    else {
      setColumn(REMOVED_FROM_LIST,NO);
    }
  }

  public String getPriorityLevel() {
    return getStringColumnValue(PRIORITY_LEVEL);
  }

  private void setPriorityLevel(String level) {
    setColumn(PRIORITY_LEVEL,level);
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
  
  public void setSamePriority(WaitingList listEntry){
  	setPriorityLevel(listEntry.getPriorityLevel());
  }

  public static String getPriorityColumnName() {
    return PRIORITY_LEVEL;
  }

  public Collection ejbFindByApartmentTypeAndComplexForApplicationType(int aprtId, int complexId) throws FinderException {
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(getTableName());
    sql.append(" where ");
    sql.append(getApartmentTypeIdColumnName());
    sql.append(" = ");
    sql.append(aprtId);
    sql.append(" and ");
    sql.append(getComplexIdColumnName());
    sql.append(" = ");
    sql.append(complexId);
    sql.append(" and ");
    sql.append(getTypeColumnName());
    sql.append(" = ");
    sql.append(TYPE_APPLICATION);
    sql.append(" order by ");
    sql.append(getPriorityColumnName());
    sql.append(", ");
    sql.append(getOrderColumnName());

    return super.idoFindPKsBySQL(sql.toString());
  }

  public Collection ejbFindByApartmentTypeAndComplexForTransferType(int aprtId, int complexId) throws FinderException {
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(getTableName());
    sql.append(" where ");
    sql.append(getApartmentTypeIdColumnName());
    sql.append(" = ");
    sql.append(aprtId);
    sql.append(" and ");
    sql.append(getComplexIdColumnName());
    sql.append(" = ");
    sql.append(complexId);
    sql.append(" and ");
    sql.append(getTypeColumnName());
    sql.append(" = ");
    sql.append(TYPE_TRANSFER);
    sql.append(" order by ");
    sql.append(getPriorityColumnName());
    sql.append(", ");
    sql.append(getOrderColumnName());

    return super.idoFindPKsBySQL(sql.toString());
  }

  public Collection ejbFindByApartmentTypeAndComplex(int aprtId, int complexId) throws FinderException {
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(getTableName());
    sql.append(" where ");
    sql.append(getApartmentTypeIdColumnName());
    sql.append(" = ");
    sql.append(aprtId);
    sql.append(" and ");
    sql.append(getComplexIdColumnName());
    sql.append(" = ");
    sql.append(complexId);
    sql.append(" order by ");
    sql.append(getPriorityColumnName());
    sql.append(", ");
    sql.append(getOrderColumnName());

    return super.idoFindPKsBySQL(sql.toString());
  }
  
	public Collection ejbFindNextForTransferByApartmentTypeAndComplex(int aprtId, int complexId, int orderedFrom) throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName());
		sql.append(" where ");
		sql.append(getApartmentTypeIdColumnName());
		sql.append(" = ");
		sql.append(aprtId);
		sql.append(" and ");
		sql.append(getComplexIdColumnName());
		sql.append(" = ");
		sql.append(complexId);
		sql.append(" and ");
		sql.append(getPriorityColumnName());
		sql.append(" = 'C' and ");
		sql.append(getOrderColumnName());
		sql.append(" > ");
		sql.append(orderedFrom);
		sql.append(" order by ");
		sql.append(getOrderColumnName());

		return super.idoFindPKsBySQL(sql.toString());
	}  
	
	public Collection ejbFindByApplicantID(Integer ID)throws FinderException{
		String[] orderby = {getPriorityColumnName(),getOrderColumnName()		};
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()).appendOrderBy(orderby));
	}
	
	public Collection ejbFindBySQL(String sql)throws FinderException{
		return super.idoFindPKsBySQL(sql);
	}
	
	public int getCountOfRecords(String sql) throws FinderException{
		try {
			return super.getIntTableValue(sql);
		}
		catch (SQLException e) {
			throw new FinderException(e.getMessage());
		}
	}
}