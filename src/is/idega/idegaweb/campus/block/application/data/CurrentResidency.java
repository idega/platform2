/*
 * $Id: CurrentResidency.java,v 1.1 2001/11/08 14:43:05 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.data;


import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CurrentResidency extends GenericEntity {
  private static final String name_ = "cam_curr_res";
  private static final String description_ = "description";
  private static final String requiresExtraInfo_ = "requires_extra_info";

  public CurrentResidency() {
    super();
  }

  public CurrentResidency(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(description_,"Lýsing",true,true,"java.lang.String");
    addAttribute(requiresExtraInfo_,"Þarfnast viðbótarupplýsinga",true,true,"java.lang.String");
    setMaxLength(description_,255);
    setMaxLength(requiresExtraInfo_,1);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getDescriptionColumnName() {
    return(description_);
  }

  public String getRequiresExtranInfoColumnName() {
    return(requiresExtraInfo_);
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

  public boolean getRequiresExtraInfo() {
    String tmp = (String)getColumnValue(requiresExtraInfo_);
    if (tmp.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public void setRequiresExtranInfo(boolean extraInfo) {
    if (extraInfo)
      setColumn(requiresExtraInfo_,"Y");
    else
      setColumn(requiresExtraInfo_,"N");
  }
}
