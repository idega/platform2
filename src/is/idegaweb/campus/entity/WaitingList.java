/*
 * $Id: WaitingList.java,v 1.4 2001/06/25 22:57:18 palli Exp $
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
  private static final String waitingListTypeId_ = "app_wl_type_id";
  private static final String order_ = "ordered";

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
    addAttribute(waitingListTypeId_,"Waiting list type",true,true,"java.lang.Integer","one-to-many","is.idegaweb.campus.entity.WaitingListType");
    addAttribute(order_,"Order",true,true,"java.lang.Integer");
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

  public String getWaitingListTypeIdColumnName() {
    return(waitingListTypeId_);
  }

  public String getOrderColumnName() {
    return(order_);
  }

  public void setComplexId(int id) {
    setColumn(complexId_,id);
  }

  public Integer getComplexId() {
    return((Integer)getColumnValue(complexId_));
  }

  public void setApartmentTypeId(int id) {
    setColumn(apartmentTypeId_,id);
  }

  public void setApartmentTypeId(Integer id) {
    setColumn(apartmentTypeId_,id);
  }

  public Integer getApartmentTypeId() {
    return((Integer)getColumnValue(apartmentTypeId_));
  }

  public void setApplicantId(int id) {
    setColumn(applicantId_,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(applicantId_,id);
  }

  public Integer getApplicantId() {
    return((Integer)getColumnValue(applicantId_));
  }

  public void setWaitingListTypeId(int id) {
    setColumn(waitingListTypeId_,id);
  }

  public void setWaitingListTypeId(Integer id) {
    setColumn(waitingListTypeId_,id);
  }

  public Integer getWaitingListTypeId() {
    return((Integer)getColumnValue(waitingListTypeId_));
  }

  public void setOrder(int order) {
    setColumn(order_,order);
  }

  public void setOrder(Integer order) {
    setColumn(order_,order);
  }

  public Integer getOrder() {
    return((Integer)getColumnValue(order_));
  }
}