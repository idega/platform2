package com.idega.block.forum.data;

import com.idega.data.*;
import java.sql.*;


/**
 * Title:        idegaForms
 * Description:
 * Copyright:    Copyright (c) 2000 idega margmiðlun hf.
 * Company:      idega margmiðlun hf.
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ForumUnion extends GenericEntity {

  public ForumUnion() {
    super();
  }

  public ForumUnion(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    /**@todo: implement this com.idega.data.GenericEntity abstract method*/
  }

  public String getIDColumnName(){
    return "forum_union_id";
  }


  public String getEntityName() {
    return "i_forum_union";
  }
}