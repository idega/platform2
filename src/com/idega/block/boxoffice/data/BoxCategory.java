//idega 2000 - Laddi

package com.idega.block.boxoffice.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.block.text.data.LocalizedText;
import com.idega.core.user.data.User;

public class BoxCategory extends GenericEntity{

	public BoxCategory(){
		super();
	}

	public BoxCategory(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
    addAttribute(getColumnNameUserID(), "User", true, true, Integer.class);
    this.addManyToManyRelationShip(LocalizedText.class,"BX_CATEGORY_LOCALIZED_TEXT");
	}

	public static String getColumnNameBoxCategoryID() { return "BX_CATEGORY_ID"; }
  public static String getColumnNameUserID(){ return User.getColumnNameUserID();}
	public static String getEntityTableName() { return "BX_CATEGORY"; }

	public String getIDColumnName(){
		return getColumnNameBoxCategoryID();
	}

	public String getEntityName(){
		return getEntityTableName();
	}

  public int getUserID() {
    return getIntColumnValue(getColumnNameUserID());
  }

  public void setUserID(int userID) {
    setColumn(getColumnNameUserID(),userID);
  }

  public void delete() throws SQLException {
    BoxLink[] link = (BoxLink[]) BoxLink.getStaticInstance(BoxLink.class).findAllByColumn(getColumnNameBoxCategoryID(),getID());
    if ( link != null ) {
      for ( int a = 0; a < link.length; a++ ) {
        link[a].delete();
      }
    }
    removeFrom(LocalizedText.getStaticInstance(LocalizedText.class));
    removeFrom(BoxEntity.getStaticInstance(BoxEntity.class));
    super.delete();
  }
}
