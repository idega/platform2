package com.idega.block.reports.data;

import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ReportCategoryBMPBean extends com.idega.block.category.data.ICCategoryBMPBean implements com.idega.block.reports.data.ReportCategory {

  public ReportCategoryBMPBean(){
    super();
  }
  public ReportCategoryBMPBean(int id)throws SQLException{
    super(id);
  }

  public String getCategoryType(){
    return "reports";
  }

}
