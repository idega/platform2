package com.idega.block.school.data;

import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.LocalizedTextHome;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
import com.idega.data.*;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolBMPBean extends GenericEntity implements School {

  public final static String SCHOOL = "sch_school";
  public final static String NAME = "school_name";
  public final static String ADDRESS = "school_address";
  public final static String INFO = "school_info";
  public final static String HEADMASTER = "headmaster_group_id";
  public final static String SCHOOLTYPE = "sch_school_type_id";
  public final static String SCHOOLAREA = "sch_school_area_id";
  public final static String ZIPCODE = "zip_code";
  public final static String ZIPAREA = "zip_area";
  public final static String PHONE = "phone";
  public final static String KEYCODE = "key_kode";
  public final static String LONGITUDE = "longitude";
  public final static String LATITUDE = "latitude";
  /** Gimmi 4-5 Nov 2002 */
  public final static String FAX = "fax_nr";
  public final static String WEB_PAGE = "web_page";
  public final static String MANAGEMENT_TYPE = "managment_type";
  public final static String HEADMASTER_USER_ID = "headmaster_user_id";
  public final static String ASSISTANT_HEADMASTER_USER_ID = "assistant_hm_user_id";


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    //this.addAttribute(SCHOOLTYPE,"Schooltype",true,true,Integer.class,this.MANY_TO_ONE,SchoolType.class);
    this.addAttribute(SCHOOLAREA,"Schoolarea",true,true,Integer.class,this.MANY_TO_ONE,SchoolArea.class);
    this.addAttribute(NAME,"Schoolname",true,true,String.class);
    this.addAttribute(INFO,"Info",true,true,String.class,4000);
    this.addAttribute(ADDRESS,"Address",true,true,String.class,100);
    this.addAttribute(ZIPAREA,"Ziparea",true,true,String.class,20);
    this.addAttribute(ZIPCODE,"Zipcode",true,true,String.class,20);
    this.addAttribute(PHONE,"phone",true,true,String.class,20);
    this.addAttribute(KEYCODE,"keycode",true,true,String.class,20);
    this.addAttribute(LATITUDE,"latitude",true,true,String.class,20);
    this.addAttribute(LONGITUDE,"longitude",true,true,String.class,20);
    this.addAttribute(HEADMASTER,"Headmaster",true,true,Integer.class,this.MANY_TO_ONE,Group.class);
    /** Gimmi 4-5 Nov 2002 */
    this.addAttribute(FAX,"fax",true,true,String.class,20);
    this.addAttribute(WEB_PAGE,"web_page",true,true,String.class,50);
    this.addAttribute(MANAGEMENT_TYPE,"management_type",true,true,Integer.class);
    this.addAttribute(HEADMASTER_USER_ID, "headmaster user id", true, true, Integer.class, this.MANY_TO_ONE, User.class);
    this.addAttribute(ASSISTANT_HEADMASTER_USER_ID, "assistant headmaster user id", true, true, Integer.class, this.MANY_TO_ONE, User.class);
    
    
    
    this.addManyToManyRelationShip(SchoolType.class);
    this.addManyToManyRelationShip(SchoolYear.class);
    // Grimur 16.10.2002
    this.addManyToManyRelationShip(LocalizedText.class);
    this.addManyToManyRelationShip(ICFile.class);
  }
  public String getEntityName() {
    return SCHOOL;
  }

  public String getName(){
    return getSchoolName();
  }

  public int getSchoolTypeId(){
    return this.getIntColumnValue(SCHOOLTYPE);
  }
  public void setSchoolTypeId(int id){
    this.setColumn(SCHOOLTYPE,id);
  }
   public int getSchoolAreaId(){
    return this.getIntColumnValue(SCHOOLAREA);
  }
  public void setSchoolAreaId(int id){
    this.setColumn(SCHOOLAREA,id);
  }
  public String getSchoolName(){
    return this.getStringColumnValue(NAME);
  }
  public void setSchoolName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolInfo(){
    return this.getStringColumnValue(INFO);
  }
  public void setSchoolInfo(String info){
    this.setColumn(INFO,info);
  }
  public String getSchoolAddress(){
    return this.getStringColumnValue(ADDRESS);
  }
  public void setSchoolAddress(String address){
    this.setColumn(ADDRESS,address);
  }

  public String getSchoolZipArea(){
    return this.getStringColumnValue(ZIPAREA);
  }
  public void setSchoolZipArea(String ziparea){
    this.setColumn(ZIPAREA,ziparea);
  }

  public String getSchoolZipCode(){
    return this.getStringColumnValue(ZIPCODE);
  }
  public void setSchoolZipCode(String zipcode){
    this.setColumn(ZIPCODE,zipcode);
  }

  public String getSchoolPhone(){
    return this.getStringColumnValue(PHONE);
  }
  public void setSchoolPhone(String phone){
    this.setColumn(PHONE,phone);
  }

  public int getHeadmasterGroupId(){
    return this.getIntColumnValue(HEADMASTER);
  }
  
  public Group getHeadmasterGroup() throws RemoteException, FinderException{
  	return ((GroupHome) IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getHeadmasterGroupId()));	
  }
  
  public void setHeadmasterGroupId(int masterGroupId ){
    this.setColumn(HEADMASTER,masterGroupId);
  }


  public String getSchoolKeyCode(){
    return this.getStringColumnValue(KEYCODE);
  }
  public void setSchoolKeyCode(String code){
    this.setColumn(KEYCODE,code);
  }
   public String getSchoolLatitude(){
    return this.getStringColumnValue(LATITUDE);
  }
  public void setSchoolLatitude(String lat){
    this.setColumn(LATITUDE,lat);
  }
   public String getSchoolLongitude(){
    return this.getStringColumnValue(LONGITUDE);
  }
  public void setSchoolLongitude(String lon){
    this.setColumn(LONGITUDE,lon);
  }

  public Collection ejbFindAllBySchoolType(int typeId) throws javax.ejb.FinderException{
    String select = "select s.* from "+SCHOOL+" s,sch_school_sch_school_type m where m.sch_school_type_id = "+typeId+" and m.sch_school_id = s.sch_school_id";
    return super.idoFindPKsBySQL(select);
  }
  
  public Collection ejbFindAllBySchoolName(String schoolName) throws javax.ejb.FinderException{
  	return super.idoFindAllIDsByColumnBySQL(NAME,schoolName);
  }

  public Collection ejbFindAllBySchoolArea(int areaId)throws javax.ejb.FinderException{
    return super.idoFindPKsBySQL("select * from "+SCHOOL+" where "+SCHOOLAREA+" = "+areaId);
  }

  public Collection ejbFindAllSchools()throws javax.ejb.FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public void addSchoolTypes(int[] ids){
    try{
      super.addTo(SchoolType.class,ids);
    }
    catch(java.sql.SQLException sql){
      sql.printStackTrace();
    }
  }

  public void addSchoolYears(int[] ids){
    try{
      super.addTo(SchoolYear.class,ids);
    }
    catch(java.sql.SQLException sql){

    }
  }
  
  public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException{
      super.idoAddTo(year);
  }

  public void addSchoolYearsRemoveOther(int[] ids){
    try{
      super.removeFrom(SchoolYear.class);
    }
    catch(java.sql.SQLException ex){}
    this.addSchoolYears(ids);
  }

  public void addSchoolTypesRemoveOther(int[] ids){
    try{
      super.removeFrom(SchoolType.class);
    }
    catch(java.sql.SQLException ex){}
    this.addSchoolTypes(ids);
  }

  public Collection findRelatedSchoolTypes()throws com.idega.data.IDORelationshipException{
    return super.idoGetRelatedEntities(SchoolType.class);
  }

  public Collection findRelatedSchoolYears()throws com.idega.data.IDORelationshipException{
    return super.idoGetRelatedEntities(SchoolYear.class);
  }

  public Collection ejbFindAllByAreaAndType(int area,int type) throws javax.ejb.FinderException{
    StringBuffer sql = new StringBuffer("select s.* ");
    sql.append(" from sch_school_area a, sch_school s, sch_school_type t, sch_school_sch_school_type m ");
    sql.append(" where a.sch_school_area_id = s.sch_school_area_id ");
    sql.append(" and t.sch_school_type_id = m.sch_school_type_id ");
    sql.append(" and s.sch_school_id = m.sch_school_id ");
    sql.append(" and t.sch_school_type_id = ");
    sql.append( type);
    sql.append(" and a.sch_school_area_id = ");
    sql.append(area);

    return super.idoFindPKsBySQL(sql.toString());

  }
  
  public Collection ejbFindAllBySchoolGroup(Group schoolGroup) throws javax.ejb.FinderException,RemoteException{
  	StringBuffer sql = new StringBuffer("Select s.* ");
  	sql.append("  from sch_school s ");
	sql.append(" where s.headmaster_group_id in ( ");
	sql.append(" select r.ic_group_id from ic_group_relation r ");
	sql.append(" where r.ic_group_id in(select headmaster_group_id from sch_school ) ");
	sql.append(" and r.related_ic_group_id = ");
	sql.append(schoolGroup.getPrimaryKey().toString() );
	sql.append(" ) ");
  	return super.idoFindPKsBySQL(sql.toString());
  }
  

	public LocalizedText getLocalizedText(int localeId) throws IDORelationshipException, RemoteException {
    LocalizedText text = TextFinder.getLocalizedText(this, localeId);
    if (text == null) text = TextFinder.getLocalizedText(this, 1);

		return text;
	}

   
  public void setLocalizedText(LocalizedText text) throws IDORelationshipException {
  	/** Supports only one locale, if a new one in inserted it will overwrite the old one... */
		this.idoRemoveFrom( LocalizedText.class);	
  	this.idoAddTo(text);	
  }
  
  public String getSchoolFax() {
  	return getStringColumnValue(FAX);	
  }
  
  public void setSchoolFax(String fax) {
  	setColumn(FAX, fax);	
  }
  
  public String getSchoolWebPage() {
  	return getStringColumnValue(WEB_PAGE);	
  }
  
  public void setSchoolWebPage(String webPage) {
  	setColumn(WEB_PAGE, webPage);	
  }
  
  public int getSchoolManagermentType() {
  	return getIntColumnValue(MANAGEMENT_TYPE);	
  }
  
  public void setSchoolManagementType(int managementType) {
  	setColumn(MANAGEMENT_TYPE, managementType);	
  }
  
  public Collection getImages() throws IDORelationshipException {
  	return this.idoGetRelatedEntities( ICFile.class);
  }
   
  public void removeImages() throws IDORelationshipException {
  	this.idoRemoveFrom(ICFile.class);	
  } 
  
  public void setImage(ICFile image) throws IDORelationshipException {
		removeImages();
  	this.idoAddTo(image);	
  }

  public void addImage(ICFile image) throws IDORelationshipException {
  	this.idoAddTo(image);	
  }
  
  public void setImages(Collection images) throws IDORelationshipException, RemoteException {
  	this.idoRemoveFrom( ICFile.class );
  	if (images != null) {
  		Iterator iter = images.iterator();
  		ICFile file;
  		while (iter.hasNext()) {
				try {
					file = ((ICFileHome) IDOLookup.getHome( ICFile.class)).findByPrimaryKey(iter.next());
	  			addImage(file);
				} catch (FinderException e) {
					e.printStackTrace(System.err);
				} catch (IDORelationshipException e) {
				}
  		}	
  	}
  }
  
  
  public int getHeadmasterUserId() {
  	return getIntColumnValue(HEADMASTER_USER_ID);
  }
  
  public void setHeadmasterUserId(int userId) {
  	setColumn(HEADMASTER_USER_ID, userId);
  }
  
  public int getAssistantHeadmasterUserId() {
  	return getIntColumnValue(ASSISTANT_HEADMASTER_USER_ID);
  }
  
  public void setAssistantHeadmasterUserId(int userId) {
  	setColumn(ASSISTANT_HEADMASTER_USER_ID, userId);	
  }
  
  public static void main(String[] args){
   	System.out.println("hellu there");
   }
}