/*
 * $Id: WaitingList.java,v 1.5 2001/07/12 13:39:24 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;

import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class WaitingList extends GenericEntity {
  private static final String name_ = "cam_waiting_list";
  private static final String complexId_ = "bu_complex_id";
  private static final String apartmentTypeId_ = "bu_apartment_type_id";
  private static final String applicantId_ = "app_applicant_id";
  private static final String order_ = "ordered";
  private static final String type_ = "type";

  public WaitingList() {
    super();
  }

  public WaitingList(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(complexId_,"Complex",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.Complex");
    addAttribute(apartmentTypeId_,"Apartment type",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.ApartmentType");
    addAttribute(applicantId_,"Applicant",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(order_,"Order",true,true,"java.lang.Integer");
    addAttribute(type_,"Waiting list type",true,true,"java.lang.String");
    setMaxLength(type_,1);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getComplexIdColumnName() {
    return(complexId_);
  }

  public String getApartmentTypeIdColumnName() {
    return(apartmentTypeId_);
  }

  public String getApplicantIdColumnName() {
    return(applicantId_);
  }

  public String getTypeColumnName() {
    return(type_);
  }

  public String getOrderColumnName() {
    return(order_);
  }

  public void setComplexId(int id) {
    setColumn(complexId_,id);
  }

  public Integer getComplexId() {
    return(getIntegerColumnValue(complexId_));
  }

  public void setApartmentTypeId(int id) {
    setColumn(apartmentTypeId_,id);
  }

  public void setApartmentTypeId(Integer id) {
    setColumn(apartmentTypeId_,id);
  }

  public Integer getApartmentTypeId() {
    return(getIntegerColumnValue(apartmentTypeId_));
  }

  public void setApplicantId(int id) {
    setColumn(applicantId_,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(applicantId_,id);
  }

  public Integer getApplicantId() {
    return(getIntegerColumnValue(applicantId_));
  }

  public void setType(String type) {
    setColumn(type_,type);
  }

  public String getType() {
    return(getStringColumnValue(type_));
  }

  public void setOrder(int order) {
    setColumn(order_,order);
  }

  public void setOrder(Integer order) {
    setColumn(order_,order);
  }

  public Integer getOrder() {
    return(getIntegerColumnValue(order_));
  }
}