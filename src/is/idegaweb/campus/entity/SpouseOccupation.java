/*
 * $Id: SpouseOccupation.java,v 1.3 2001/06/25 22:57:18 palli Exp $
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
public class SpouseOccupation extends GenericEntity {
  private static final String name_ = "cam_spouse_occ";
  private static final String description_ = "description";

  public String getDescriptionColumnName() {
    return(description_);
  }

  public SpouseOccupation() {
    super();
  }

  public SpouseOccupation(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(description_,"Description",true,true,"java.lang.String");
    super.setMaxLength(description_,255);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getDescriptionColumnName() {
    return(description_);
  }

  public String getName() {
    return(getDescription());
  }

  public String getDescription() {
    return((String)getColumnValue(description_));
  }

  public void setDescription(String description) {
    setColumn(description_,description);
  }
}