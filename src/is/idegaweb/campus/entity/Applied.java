/*
 * $Id: Applied.java,v 1.1 2001/06/15 01:31:22 palli Exp $
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
public class Applied extends GenericEntity {
  private static String name_ = "cam_applied";
  private static String appartmentTypeId_ = "bu_aprt_type_id";
  private static String applicationId_ = "cam_application_id";
  private static String order_ = "ordered";

  public Applied() {
    super();
  }

  public Applied(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(appartmentTypeId_,"Íbúðartegund",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.AppartmentType");
    addAttribute(applicationId_,"Umsóknarnúmer Campus",true,true,"java.lang.Integer","one-to-many","is.idegaweb.campus.entity.Application");
    addAttribute(order_,"Umbeðið númer",true,true,"java.lang.Integer");
  }

  public String getEntityName() {
    return(name_);
  }

  public void setAppartmentTypeId(int id) {
    setColumn(appartmentTypeId_,id);
  }

  public void setAppartmentTypeId(Integer id) {
    setColumn(appartmentTypeId_,id);
  }

  public Integer getAppartmentTypeId() {
    return((Integer)getColumnValue(appartmentTypeId_));
  }

  public void setApplicationId(int id) {
    setColumn(applicationId_,id);
  }

  public void setApplicationId(Integer id) {
    setColumn(applicationId_,id);
  }

  public Integer getApplicationId() {
    return((Integer)getColumnValue(applicationId_));
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