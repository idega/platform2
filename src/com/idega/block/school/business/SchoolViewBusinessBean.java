package com.idega.block.school.business;

import com.idega.block.school.data.*;
import com.idega.data.IDOLookup;
import java.util.Collection;
import com.idega.business.IBOServiceBean;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolViewBusinessBean extends IBOServiceBean {


  public List findAllViewsByType(int type)  {
    try{
      SchoolView sview = (SchoolView) IDOLookup.create(SchoolView.class);
      return sview.findAllBySchoolType(type);

    }
    catch(Exception ex){
      return new java.util.Vector();
    }
  }

}