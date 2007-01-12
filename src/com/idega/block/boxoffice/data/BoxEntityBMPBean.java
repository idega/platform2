//idega 2001 - Laddi



package com.idega.block.boxoffice.data;



import java.sql.SQLException;

import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.GenericEntity;



public class BoxEntityBMPBean extends com.idega.data.GenericEntity implements com.idega.block.boxoffice.data.BoxEntity {



	public BoxEntityBMPBean(){

		super();

	}



	public BoxEntityBMPBean(int id)throws SQLException{

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

    BoxLink[] link = (BoxLink[]) GenericEntity.getStaticInstance(BoxLink.class).findAllByColumn(getColumnNameBoxID(),Integer.toString(getID()),"=");

    if ( link != null ) {

      for ( int a = 0; a < link.length; a++ ) {

        link[a].delete();

      }

    }

    removeFrom(GenericEntity.getStaticInstance(ICObjectInstance.class));

    removeFrom(GenericEntity.getStaticInstance(BoxCategory.class));

    super.delete();

  }

}

