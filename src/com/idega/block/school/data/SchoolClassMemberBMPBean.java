package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolClassMemberBMPBean extends GenericEntity implements SchoolClassMember {

  public final static String SCHOOLCLASSMEMBERID = "sch_class_member_id";
  public final static String SCHOOLCLASSMEMBER = "sch_class_member";
  public final static String MEMBER = "ic_user_id";
  public final static String NOTES = "notes";
  public final static String SCHOOLCLASS = "sch_school_class_id";
  public final static String REGISTER_DATE = "register_date";
  public final static String REGISTRATOR = "registrator";
  public final static String NEEDS_SPECIAL_ATTENTION = "NEEDS_SPECIAL_ATTENTION";
  public final static String SPECIALLY_PLACED = "SPECIALLY_PLACED";
  public final static String LANGUAGE = "LANGUAGE";

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(MEMBER,"classmember",true,true,Integer.class,MANY_TO_ONE ,com.idega.core.user.data.User.class);
    this.addAttribute(SCHOOLCLASS,"class",true,true,Integer.class,MANY_TO_ONE,SchoolClass.class);
    this.addAttribute(NOTES,"notes",true,true,String.class,255);
    this.addAttribute(REGISTER_DATE,"registerdate",true,true,Timestamp.class);
    this.addAttribute(REGISTRATOR,"registrator",true,true,Integer.class,MANY_TO_ONE,com.idega.core.user.data.User.class);
    this.addAttribute(NEEDS_SPECIAL_ATTENTION,"Needs special attention",true,true,Boolean.class);
    this.addAttribute(SPECIALLY_PLACED,"Specially placed",true,true,Boolean.class);
    this.addAttribute(LANGUAGE,"Needs special attention",true,true,String.class);
  }
  public String getEntityName() {
    return SCHOOLCLASSMEMBER;
  }
  public void setClassMemberId(int id){
    this.setColumn(MEMBER,id);
  }
  public int getClassMemberId(){
    return this.getIntColumnValue(MEMBER);
  }
	public User getStudent() {
		return (User) getColumnValue(MEMBER);	
	}	
  public void setSchoolClassId(int id){
    this.setColumn(SCHOOLCLASS,id);
  }
  public int getSchoolClassId(){
    return this.getIntColumnValue(SCHOOLCLASS);
  }
  public void setRegisterDate(Timestamp stamp){
    this.setColumn(REGISTER_DATE,stamp);
  }
  public Timestamp getRegisterDate(){
    return (Timestamp) this.getColumnValue(REGISTER_DATE);
  }
  public void setRegistratorId(int id){
    this.setColumn(REGISTRATOR,id);
  }
  public int getRegistratorId(){
    return this.getIntColumnValue(REGISTRATOR);
  }
  public void setNotes(String notes) {
  	this.setColumn(NOTES, notes);
  }
  public String getNotes() {
  	return this.getStringColumnValue(NOTES);
  }

  public void setNeedsSpecialAttention(boolean needsAttention) {
  	this.setColumn(NEEDS_SPECIAL_ATTENTION, needsAttention);
  }
  public boolean getNeedsSpecialAttention() {
  	return getBooleanColumnValue(NEEDS_SPECIAL_ATTENTION,false);
  }

  public void setSpeciallyPlaced(boolean speciallyPlaced) {
  	this.setColumn(SPECIALLY_PLACED, speciallyPlaced);
  }
  public boolean getSpeciallyPlaced() {
  	return getBooleanColumnValue(SPECIALLY_PLACED,false);
  }

  public void setLanguage(String language) {
  	this.setColumn(LANGUAGE, language);
  }
  public String getLanguage() {
  	return this.getStringColumnValue(LANGUAGE);
  }

  public Collection ejbFindBySchoolClass(SchoolClass schoolClass) throws FinderException,RemoteException {
  	return ejbFindBySchoolClass(((Integer)schoolClass.getPrimaryKey()).intValue());	
  }
  
  public Collection ejbFindBySchoolClass(int schoolClassID) throws FinderException,RemoteException {
  	IDOQuery sql = idoQuery();
  	sql.appendSelectAllFrom(getEntityName()).appendWhere();
  	sql.append(this.SCHOOLCLASS).appendEqualSign().append(schoolClassID);
  	
  	return super.idoFindPKsBySQL(sql.toString());	
  }
  
  public Collection ejbFindByStudent(User student) throws FinderException,RemoteException {
  	return ejbFindByStudent(((Integer)student.getPrimaryKey()).intValue());	
  }
  
  public Collection ejbFindByStudent(int studentID) throws FinderException,RemoteException {
  	IDOQuery sql = idoQuery();
  	sql.appendSelectAllFrom(getEntityName()).appendWhere().append(MEMBER).appendEqualSign().append(studentID);
  	
  	return super.idoFindPKsBySQL(sql.toString());	
  }
  
  public Integer ejbFindByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException, RemoteException{
		return ejbFindByUserAndSchoolClass(((Integer)user.getPrimaryKey()).intValue(),((Integer)schoolClass.getPrimaryKey()).intValue());
  }

  public Integer ejbFindByUserAndSchoolClass(int userID, int schoolClassID) throws FinderException, RemoteException{
  	IDOQuery sql = idoQuery();
  	sql.appendSelectAllFrom(this).appendWhere().append(MEMBER).appendEqualSign().append(userID)
  	.appendAnd().append(SCHOOLCLASS).appendEqualSign().append(schoolClassID);

  	return (Integer)this.idoFindOnePKBySQL(sql.toString());
  }

  public Integer ejbFindByUserAndSeason(User user, SchoolSeason season) throws FinderException, RemoteException{
		return ejbFindByUserAndSeason(((Integer)user.getPrimaryKey()).intValue(),((Integer)season.getPrimaryKey()).intValue());
  }
  
  public Integer ejbFindByUserAndSeason(int userID, int seasonID) throws FinderException, RemoteException{
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
    .appendWhere().append(" mb."+MEMBER).appendEqualSign().append(userID)
    .appendAnd().append("cl."+SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID)
    .appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
    .appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
    //System.err.println(sql.toString());
    return (Integer)this.idoFindOnePKBySQL(sql.toString());
  }
  
	public Integer ejbFindByUserAndSchool(int userID, int schoolID) throws FinderException, RemoteException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
		.appendWhere().append(" mb."+MEMBER).appendEqualSign().append(userID)
		.appendAnd().append("cl."+SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID)
		.appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
		.appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
		//System.err.println(sql.toString());
		return (Integer)this.idoFindOnePKBySQL(sql.toString());
	}
  
	public Collection ejbFindAllByUserAndSeason(User user, SchoolSeason season) throws FinderException, RemoteException{
		return ejbFindAllByUserAndSeason(((Integer)user.getPrimaryKey()).intValue(),((Integer)season.getPrimaryKey()).intValue());
	}
  
  public Collection ejbFindAllByUserAndSeason(int userID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
		.appendWhere().append(" mb."+MEMBER).appendEqualSign().append(userID)
		.appendAnd().append("cl."+SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID)
		.appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
		.appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
  	return idoFindPKsBySQL(sql.toString());
  }
  
	public Integer ejbFindByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
		.appendWhere().append(" mb."+MEMBER).appendEqualSign().append(userID)
		.appendAnd().append("cl."+SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID)
		.appendAnd().append("cl."+SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID)
		.appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
		.appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
		return (Integer) idoFindOnePKBySQL(sql.toString());
	}
  
  public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws FinderException, RemoteException{
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
    .appendWhere().append(" cl."+SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID)
    .appendAnd().append("cl."+SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID)
    .appendAnd().append("cl."+SchoolClassBMPBean.SCHOOLYEAR).appendEqualSign().append(yearID)
    .appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
    .appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
    return super.idoFindPKsBySQL(sql.toString());
  }

  public Collection ejbFindBySchoolAndSeason(int schoolID, int seasonID) throws FinderException, RemoteException{
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
    .appendWhere().append(" cl."+SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID)
    .appendAnd().append("cl."+SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID)
    .appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
    .appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
    //System.err.println(sql.toString());
    return super.idoFindPKsBySQL(sql.toString());
  }
  
	public Collection ejbFindBySchool(int schoolID, int schoolClassID) throws FinderException, RemoteException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl, ic_user u")
		.appendWhere().append(" cl."+SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID)
		.appendAndEquals("u.ic_user_id", "mb."+this.MEMBER)
		.appendAnd().append("(cl."+SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl."+SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
		.appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
		if (schoolClassID != -1)
			sql.appendAndEquals("mb."+SCHOOLCLASS, schoolClassID);
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return super.idoFindPKsBySQL(sql.toString());
	}
  
  public Collection ejbFindAllBySeasonAndMaximumAge(int seasonID,int maxAge)throws FinderException,RemoteException{
        final StringBuffer sql = new StringBuffer ();
        sql.append ("select student.*");
        sql.append (" from sch_school_sch_school_year school,sch_school_year schoolyear, sch_school_class class, " + getTableName () + " student");
        sql.append (" where school.sch_school_year_id = schoolyear.sch_school_year_id");
        sql.append (" and class.sch_school_year_id = school.sch_school_year_id");
        sql.append (" and class.school_id = school.sch_school_id");
        sql.append (" and class.sch_school_season_id = " + seasonID);
        sql.append (" and class.sch_school_class_id = student." + SCHOOLCLASS);
        sql.append (" and exists");
        sql.append (" (");
        sql.append (" select s.sch_school_id, max (y.year_age)");
        sql.append (" from sch_school_sch_school_year s, sch_school_year y");
        sql.append (" where s.sch_school_year_id = y.sch_school_year_id");
        sql.append (" group by s.sch_school_id");
        sql.append (" having max (y.year_age) <=").append(maxAge);
        sql.append (" and schoolyear.year_age = max (y.year_age)");
        //sql.append (" and school.sch_school_year_id =s.sch_school_year_id");
        sql.append (" and school.sch_school_id = s.sch_school_id");
        sql.append (" )");
        return idoFindIDsBySQL (sql.toString ());
  }



}