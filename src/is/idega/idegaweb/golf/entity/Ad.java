//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class Ad extends GolfEntity{

	public Ad(){
		super();
	}

	public Ad(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("banner_name","banner_nafn",true,true,"java.lang.String");
		addAttribute("hits", "hits", true, true, "java.lang.Integer");
		addAttribute("impressions", "impressions", true, true, "java.lang.Integer");
		addAttribute("max_impressions", "max_impressions", true, true, "java.lang.Integer");
		addAttribute("begin_date","upphafsdagsetning",true,true,"java.sql.Date");
		addAttribute("end_date","lokadagsetning",true,true,"java.sql.Date");
		addAttribute("image_id", "númer myndar", true, true, "java.lang.Integer");
		addAttribute("url","url",true,true,"java.lang.String");
		addAttribute("max_hits", "max_hits", true, true, "java.lang.Integer");

                      addManyToManyRelationShip("is.idega.idegaweb.golf.entity.AdCatagory","ad_ad_catagory");

	}

	public String getEntityName(){
		return "ad";
	}

	public String getUrl() {
		return getStringColumnValue("url");
	}

	public void setUrl(String url) {
		setColumn("url",url);
	}

	public int getImageID(boolean addImpression) {

		if (addImpression) {
			try {
				setImpressions(getImpressions() +1);
				this.update();
			}
			catch (SQLException e) {
				System.err.println("Error in updating impression : "+e.getMessage());
			}
		}

		return getIntColumnValue("image_id");
	}

	public void delete()throws SQLException {
		Connection Conn = getConnection();
		try{
			Statement Stmt = Conn.createStatement();
			Stmt.executeUpdate("delete from ad_ad_catagory where ad_id='"+this.getID()+"'");
			Stmt.close();
			super.delete();
		}
		catch (SQLException s){
				System.err.println("Error in deleting ads : "+s.getMessage());
		}
		finally{
			freeConnection(Conn);
		}


	}

	public int getImageID() {
		return getIntColumnValue("image_id");
	}

	public void setImageID(int image_id) {
		setColumn("image_id",new Integer(image_id));
	}

	public Date getBeginDate() {
		return (Date) getColumnValue("begin_date");
	}

	public void setBeginDate(Date begin_date) {
		setColumn("begin_date", begin_date);
	}

	public Date getEndDate() {
		return (Date) getColumnValue("end_date");
	}

	public void setEndDate(Date end_date) {
		setColumn("end_date", end_date);
	}

	public int getMaxImpressions() {
		return getIntColumnValue("max_impressions");
	}

	public void setMaxImpressions(int max_impressions) {
		setColumn("max_impressions",new Integer(max_impressions));
	}

	public int getImpressions() {
		return getIntColumnValue("impressions");
	}

	public void setImpressions(int impressions) {
		setColumn("impressions",new Integer(impressions));
	}

	public int getHits() {
		return getIntColumnValue("hits");
	}

	public void setHits(int hits) {
		setColumn("hits",new Integer(hits));
	}

	public int getMaxHits() {
		return getIntColumnValue("max_hits");
	}

	public void setMaxHits(int max_hits) {
		setColumn("max_hits",new Integer(max_hits));
	}


	public String getBannerName() {
		return getStringColumnValue("banner_name");
	}

	public void setBannerName(String banner_name) {
		setColumn("banner_name",banner_name);
	}









}
