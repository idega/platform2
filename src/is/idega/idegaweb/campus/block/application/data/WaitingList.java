/*
 * $Id: WaitingList.java,v 1.5 2001/12/07 12:22:33 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.data;

import com.idega.data.GenericEntity;
import java.sql.Timestamp;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class WaitingList extends GenericEntity {
  private static final String _name = "cam_waiting_list";
  private static final String _complexId = "bu_complex_id";
  private static final String _apartmentTypeId = "bu_apartment_type_id";
  private static final String _applicantId = "app_applicant_id";
  private static final String _order = "ordered";
  private static final String _type = "list_type";
  private static final String _choiceNumber = "choice_number";
  private static final String _lastConfirmation = "last_confirmation";
  private static final String _numberOfRejections = "number_of_rejections";
  private static final String _removedFromList = "removed_from_list";

  public static final String YES = "Y";
  public static final String NO = "N";

  /**
   *
   */
  public WaitingList() {
    super();
  }

  /**
   *
   */
  public WaitingList(int id) throws SQLException {
    super(id);
  }

  /**
   *
   */
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(_complexId,"Complex",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.Complex");
    addAttribute(_apartmentTypeId,"Apartment type",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.ApartmentType");
    addAttribute(_applicantId,"Applicant",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(_order,"Order",true,true,"java.lang.Integer");
    addAttribute(_type,"Waiting list type",true,true,"java.lang.String");
    addAttribute(_choiceNumber,"Choice number",true,true,"java.lang.Integer");
    addAttribute(_lastConfirmation,"Last confirmation date",true,true,"java.sql.Timestamp");
    addAttribute(_numberOfRejections,"Number of rejections",true,true,"java.lang.Integer");
    addAttribute(_removedFromList,"Removed from list",true,true,"java.lang.String");
    setMaxLength(_type,1);
    setMaxLength(_removedFromList,1);
  }

  /**
   *
   */
  public String getEntityName() {
    return(_name);
  }

  /**
   *
   */
  public static String getEntityTableName(){
    return(_name);
  }

  /**
   *
   */
  public static String getComplexIdColumnName() {
    return(_complexId);
  }

  /**
   *
   */
  public static String getApartmentTypeIdColumnName() {
    return(_apartmentTypeId);
  }

  /**
   *
   */
  public static String getApplicantIdColumnName() {
    return(_applicantId);
  }

  /**
   *
   */
  public static String getTypeColumnName() {
    return(_type);
  }

  /**
   *
   */
  public static String getOrderColumnName() {
    return(_order);
  }

  /**
   *
   */
  public static String getChoiceNumberColumnName() {
    return(_choiceNumber);
  }

  /**
   *
   */
  public static String getLastConfirmationColumnName() {
    return(_lastConfirmation);
  }

  /**
   *
   */
  public static String getNumberOfRejectionsColumnName() {
    return(_numberOfRejections);
  }

  /**
   *
   */
  public static String getRemovedFromListColumnName() {
    return(_removedFromList);
  }

  /**
   *
   */
  public void setComplexId(int id) {
    setColumn(_complexId,id);
  }

  /**
   *
   */
  public Integer getComplexId() {
    return(getIntegerColumnValue(_complexId));
  }

  /**
   *
   */
  public void setApartmentTypeId(int id) {
    setColumn(_apartmentTypeId,id);
  }

  /**
   *
   */
  public void setApartmentTypeId(Integer id) {
    setColumn(_apartmentTypeId,id);
  }

  /**
   *
   */
  public Integer getApartmentTypeId() {
    return(getIntegerColumnValue(_apartmentTypeId));
  }

  /**
   *
   */
  public void setApplicantId(int id) {
    setColumn(_applicantId,id);
  }

  /**
   *
   */
  public void setApplicantId(Integer id) {
    setColumn(_applicantId,id);
  }

  /**
   *
   */
  public Integer getApplicantId() {
    return(getIntegerColumnValue(_applicantId));
  }

  /**
   *
   */
  public void setType(String type) {
    setColumn(_type,type);
  }

  /**
   *
   */
  public String getType() {
    return(getStringColumnValue(_type));
  }

  /**
   *
   */
  public void setOrder(int order) {
    setColumn(_order,order);
  }

  /**
   *
   */
  public void setOrder(Integer order) {
    setColumn(_order,order);
  }

  /**
   *
   */
  public Integer getOrder() {
    return(getIntegerColumnValue(_order));
  }

  /**
   *
   */
  public void setLastConfirmationDate(Timestamp date) {
    setColumn(_lastConfirmation,date);
  }

  /**
   *
   */
  public Timestamp getLastConfirmationDate() {
    return((Timestamp)getColumnValue(_lastConfirmation));
  }

  /**
   *
   */
  public void setNumberOfRejections(int count) {
    setColumn(_numberOfRejections,count);
  }

  /**
   *
   */
  public void setNumberOfRejections(Integer count) {
    setColumn(_numberOfRejections,count);
  }

  /**
   *
   */
  public int getNumberOfRejections() {
    return(getIntColumnValue(_numberOfRejections));
  }

  /**
   *
   */
  public void setChoiceNumber(int choice) {
    setColumn(_choiceNumber,choice);
  }

  /**
   *
   */
  public void setChoiceNumber(Integer choice) {
    setColumn(_choiceNumber,choice);
  }

  /**
   *
   */
  public Integer getChoiceNumber() {
    return(getIntegerColumnValue(_choiceNumber));
  }

  /**
   *
   */
  public boolean getRemovedFromList() {
    String removed = getStringColumnValue(_removedFromList);
    if ((removed == null) || (removed.equals(NO)))
      return(false);
    else if (removed.equals(YES))
      return(true);
    else
      return(false);
  }

  /**
   *
   */
  public void setRemovedFromList(String removed) {
    if ((removed != null) && (removed.equalsIgnoreCase(YES)))
      setColumn(_removedFromList,YES);
    else
      setColumn(_removedFromList,NO);
  }
}