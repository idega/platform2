/*
 * $Id: ApplicationStatus.java,v 1.2 2001/06/21 16:20:46 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationStatus extends GenericEntity {
  public static final String name_ = "app_appl_status";
  public static final String description_ = "description";

  public ApplicationStatus() {
    super();
  }

  public ApplicationStatus(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(description_,"Lýsing á stöðu umsóknar",true,true,"java.lang.String");
    setMaxLength(description_,255);
  }

  public String getEntityName() {
    return(name_);
  }

  public void setDescription(String description) {
    setColumn(description_,description);
  }

  public String getDescription() {
    return((String)getColumnValue(description_));
  }
}