package com.idega.block.application.business;

import com.idega.block.application.data.*;
import java.sql.SQLException;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import java.util.List;
import java.util.Hashtable;
import java.sql.*;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.util.database.ConnectionBroker;
import java.lang.StringBuffer;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ApplicationFinder {

  public static List ListOfSubject(){
    try{
      return EntityFinder.findAll(new ApplicationSubject());
    }
    catch(SQLException e){return null;}
  }


}// class end