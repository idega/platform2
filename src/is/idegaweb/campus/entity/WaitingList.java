/*
 * $Id: WaitingList.java,v 1.2 2001/06/15 10:59:39 palli Exp $
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
  private static String name_ = "app_waiting_list";
  private static String apartmentTypeId_ = "bu_apartment_type_id";
  private static String applicantId_ = "app_applicant_id";
  private static String waitingListTypeId_ = "app_wl_type_id";
  private static String order_ = "ordered";

  public WaitingList() {
    super();
  }

  public WaitingList(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(apartmentTypeId_,"Tegund íbúðar sem beðið er eftir",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.ApartmentType");
    addAttribute(applicantId_,"Umsækjandi",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(waitingListTypeId_,"Tegund biðlista",true,true,"java.lang.Integer","one-to-many","is.idegaweb.campus.entity.WaitingListType");
    addAttribute(order_,"Hvar á biðlista er umsækjandi",true,true,"java.lang.Integer");
  }

  public String getEntityName() {
    return(name_);
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