package com.idega.block.calendar.data;

import java.sql.SQLException;
import com.idega.core.data.ICCategory;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class CalendarCategory extends ICCategory{

  public CalendarCategory(){
    super();
  }
  public CalendarCategory(int id)throws SQLException{
    super(id);
  }

  public String getCategoryType(){
    return "calendar";
  }

}
