//idega 2001 - Laddi

package com.idega.block.boxoffice.data;

import com.idega.core.data.ICObjectInstance;
import java.sql.*;
import com.idega.data.*;

public class BoxEntity extends GenericEntity{

	public BoxEntity(){
		super();
	}

	public BoxEntity(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameAttribute(), "Attribute", true, true, String.class);
    addManyToManyRelationShip(ICObjectInstance.class,"BX_BOX_IC_OBJECT_INSTANCE");
    addManyToManyRelationShip(BoxCategory.class,"BX_BOX_CATEGORY");
	}

	public static String getColumnNameBoxID() { return "BX_BOX_ID"; }
	public static String getColumnNameAttribute() { return "ATTRIBUTE"; }
	public static String getEntityTableName() { return "BX_BOX"; }

	public String getIDColumnName(){
		return getColumnNameBoxID();
	}

	public String getEntityName(){
		return getEntityTableName();
	}

	public String getAttribute(){
		return (String) getColumnValue(getColumnNameAttribute());
	}

	public void setAttribute(String attribute){
	  setColumn(getColumnNameAttribute(),attribute);
	}

  public void delete() throws SQLException {
    BoxLink[] link = (BoxLink[]) BoxLink.getStaticInstance(BoxLink.class).findAllByColumn(getColumnNameBoxID(),Integer.toString(getID()),"=");
    if ( link != null ) {
      for ( int a = 0; a < link.length; a++ ) {
        link[a].delete();
      }
    }
    removeFrom(ICObjectInstance.getStaticInstance(ICObjectInstance.class));
    removeFrom(BoxCategory.getStaticInstance(BoxCategory.class));
    super.delete();
  }
}
