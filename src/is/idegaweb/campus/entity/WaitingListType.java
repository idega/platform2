/*
 * $Id: WaitingListType.java,v 1.1 2001/06/15 10:28:37 palli Exp $
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
public class WaitingListType extends GenericEntity {
  private static String name_ = "app_wl_type";
  private static String description_ = "description";

  public WaitingListType() {
    super();
  }

  public WaitingListType(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(description_,"Lýsting á biðlista tegund",true,true,"java.lang.String");
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