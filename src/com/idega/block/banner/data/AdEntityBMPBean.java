//idega 2001 - Laddi



package com.idega.block.banner.data;



import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.UserBMPBean;



public class AdEntityBMPBean extends com.idega.data.GenericEntity implements com.idega.block.banner.data.AdEntity {



	public AdEntityBMPBean(){

		super();

	}



	public AdEntityBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

    addAttribute(getColumnNameUserID(), "User", true, true, Integer.class);

		addAttribute(getColumnNameAdName(),"Ad name",true,true, String.class);

		addAttribute(getColumnNameHits(), "Hits", true, true, Integer.class);

		addAttribute(getColumnNameMaxHits(), "Max hits", true, true, Integer.class);

		addAttribute(getColumnNameImpressions(), "Impressions", true, true, Integer.class);

		addAttribute(getColumnNameMaxImpressions(), "Max impressions", true, true, Integer.class);

		addAttribute(getColumnNameBeginDate(),"Begin date",true,true, Timestamp.class);

		addAttribute(getColumnNameEndDate(),"End date",true,true, Timestamp.class);

		addAttribute(getColumnNameURL(),"URL",true,true,String.class);

    setNullable(getColumnNameHits(),false);

    setNullable(getColumnNameMaxHits(),false);

    setNullable(getColumnNameImpressions(),false);

    setNullable(getColumnNameMaxImpressions(),false);



    addManyToManyRelationShip(ICFile.class,"BA_AD_IC_FILE");

	}



	public static String getEntityTableName() { return "BA_AD"; }



	public static String getColumnNameAdID() { return "BA_AD_ID"; }

  public static String getColumnNameUserID(){ return UserBMPBean.getColumnNameUserID();}

	public static String getColumnNameAdName() { return "NAME"; }

	public static String getColumnNameHits() { return "HITS"; }

	public static String getColumnNameMaxHits() { return "MAX_HITS"; }

	public static String getColumnNameImpressions() { return "IMPRESSIONS"; }

	public static String getColumnNameMaxImpressions() { return "MAX_IMPRESSIONS"; }

	public static String getColumnNameBeginDate() { return "BEGIN_DATE"; }

	public static String getColumnNameEndDate() { return "END_DATE"; }

	public static String getColumnNameURL() { return "URL"; }



  public String getIDColumnName(){

		return getColumnNameAdID();

	}



	public String getEntityName(){

		return getEntityTableName();

	}





  //Get

  public int getUserID() {

    return getIntColumnValue(getColumnNameUserID());

  }



	public String getAdName() {

		return (String) getColumnValue(getColumnNameAdName());

	}



	public int getHits() {

		return getIntColumnValue(getColumnNameHits());

	}



	public int getMaxHits() {

		return getIntColumnValue(getColumnNameMaxHits());

	}



	public int getImpressions() {

		return getIntColumnValue(getColumnNameImpressions());

	}



	public int getMaxImpressions() {

		return getIntColumnValue(getColumnNameMaxImpressions());

	}



	public Timestamp getBeginDate() {

		return (Timestamp) getColumnValue(getColumnNameBeginDate());

	}



	public Timestamp getEndDate() {

		return (Timestamp) getColumnValue(getColumnNameEndDate());

	}



  public String getURL() {

		return (String) getColumnValue(getColumnNameURL());

	}





  //Set

  public void setUserID(int userID) {

    setColumn(getColumnNameUserID(),userID);

  }



	public void setAdName(String adName) {

		setColumn(getColumnNameAdName(),adName);

	}



	public void setHits(int hits) {

		setColumn(getColumnNameHits(),hits);

	}



	public void setMaxHits(int maxHits) {

		setColumn(getColumnNameMaxHits(),maxHits);

	}



	public void setImpressions(int impressions) {

		setColumn(getColumnNameImpressions(),impressions);

	}



	public void setMaxImpressions(int maxImpressions) {

		setColumn(getColumnNameMaxImpressions(),maxImpressions);

	}



	public void setBeginDate(Timestamp beginDate) {

		setColumn(getColumnNameBeginDate(),beginDate);

	}



	public void setEndDate(Timestamp endDate) {

		setColumn(getColumnNameEndDate(), endDate);

	}



	public void setURL(String URL) {

		setColumn(getColumnNameURL(),URL);

	}





  public void delete() throws SQLException {

    removeFrom(GenericEntity.getStaticInstance(ICFile.class));

    removeFrom(GenericEntity.getStaticInstance(BannerEntity.class));

    super.delete();

  }
  
  
  public Collection getRelatedFiles() throws IDORelationshipException{
  	return this.idoGetRelatedEntities(ICFile.class);
  }
  

}

