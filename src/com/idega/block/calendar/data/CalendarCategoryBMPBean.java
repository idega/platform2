package com.idega.block.calendar.data;

import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class CalendarCategoryBMPBean extends com.idega.block.category.data.ICCategoryBMPBean implements com.idega.block.calendar.data.CalendarCategory {

  public CalendarCategoryBMPBean(){
    super();
  }
  public CalendarCategoryBMPBean(int id)throws SQLException{
    super(id);
  }

  public String getCategoryType(){
    return "calendar";
  }

}
