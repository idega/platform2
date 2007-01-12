//idega 2000 - Laddi



package com.idega.block.boxoffice.data;



import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.block.text.data.LocalizedText;
import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.user.data.UserBMPBean;



public class BoxLinkBMPBean extends com.idega.data.GenericEntity implements com.idega.block.boxoffice.data.BoxLink {



	public BoxLinkBMPBean(){

		super();

	}



	public BoxLinkBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute(com.idega.block.boxoffice.data.BoxEntityBMPBean.getColumnNameBoxID(), "BoxEntity", true, true, Integer.class, "many-to-one", BoxEntity.class);

		addAttribute(com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameBoxCategoryID(), "BoxCategory", true, true, Integer.class, "many-to-one", BoxCategory.class);

		addAttribute(getColumnNameURL(), "URL", true, true, String.class);

		addAttribute(getColumnNameTarget(), "Target", true, true, String.class);

		addAttribute(getColumnNameFileID(), "File", true, true, Integer.class, "one-to-one", ICFile.class);

		addAttribute(getColumnNamePageID(), "Page", true, true, Integer.class);

		addAttribute(getColumnNameCreationDate(), "Creation Date", true, true, Timestamp.class);

    addAttribute(getColumnNameUserID(), "User", true, true, Integer.class);

    addManyToManyRelationShip(LocalizedText.class,"BX_LINK_LOCALIZED_TEXT");

	}



	public static String getColumnNameBoxLinkID() { return "BX_LINK_ID"; }

	public static String getColumnNameBoxID() { return com.idega.block.boxoffice.data.BoxEntityBMPBean.getColumnNameBoxID(); }

	public static String getColumnNameBoxCategoryID() { return com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameBoxCategoryID(); }

	public static String getColumnNameURL() { return "URL"; }

	public static String getColumnNameTarget() { return "TARGET"; }

	public static String getColumnNameFileID() { return "IC_FILE_ID"; }

	public static String getColumnNamePageID() { return "IB_PAGE_ID"; }

	public static String getColumnNameCreationDate() { return "CREATION_DATE"; }

  public static String getColumnNameUserID(){ return UserBMPBean.getColumnNameUserID();}

	public static String getEntityTableName() { return "BX_LINK"; }



	public String getIDColumnName(){

		return getColumnNameBoxLinkID();

	}



	public String getEntityName(){

		return getEntityTableName();

	}



  public int getBoxID() {

    return getIntColumnValue(getColumnNameBoxID());

  }



  public int getBoxCategoryID() {

    return getIntColumnValue(getColumnNameBoxCategoryID());

  }



  public String getURL() {

    return (String) getColumnValue(getColumnNameURL());

  }



  public String getTarget() {

    return (String) getColumnValue(getColumnNameTarget());

  }



  public int getFileID() {

    return getIntColumnValue(getColumnNameFileID());

  }

	public ICFile getFile() {

		return (ICFile) getColumnValue(getColumnNameFileID());

	}



  public int getPageID() {

    return getIntColumnValue(getColumnNamePageID());

  }



  public Timestamp getCreationDate() {

    return (Timestamp) getColumnValue(getColumnNameCreationDate());

  }



  public int getUserID() {

    return getIntColumnValue(getColumnNameUserID());

  }



  public void setBoxID(int boxID) {

    setColumn(getColumnNameBoxID(),boxID);

  }



  public void setBoxCategoryID(int boxCategoryID) {

    setColumn(getColumnNameBoxCategoryID(),boxCategoryID);

  }



  public void setURL(String URL) {

    setColumn(getColumnNameURL(),URL);

  }



  public void setTarget(String target) {

    setColumn(getColumnNameTarget(),target);

  }



  public void setFileID(int ICFileID) {

    setColumn(getColumnNameFileID(),ICFileID);

  }



  public void setPageID(int IBPageID) {

    setColumn(getColumnNamePageID(),IBPageID);

  }



  public void setCreationDate(Timestamp date) {

    setColumn(getColumnNameCreationDate(),date);

  }



  public void setUserID(int ICUserID) {

    setColumn(getColumnNameUserID(),ICUserID);

  }



	public void delete() throws SQLException{

    removeFrom(GenericEntity.getStaticInstance(LocalizedText.class));

		super.delete();

	}

}

