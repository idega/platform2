package com.idega.block.school.data;

import com.idega.data.*;

import java.util.Collection;

import javax.ejb.FinderException;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolYearBMPBean extends GenericEntity implements SchoolYear{

  public final static String SCHOOLYEAR = "sch_school_year";
  public final static String NAME = "year_name";
  public final static String INFO = "year_info";
  public final static String AGE = "year_age";
  public final static String SCHOOL_TYPE = "school_type";

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(NAME,"Name",true,true,String.class,3);
    addAttribute(INFO,"Info",true,true,String.class);
    addAttribute(AGE,"Age",true,true,Integer.class);
    setNullable(NAME,false);
    setUnique(NAME,true);
		this.addManyToOneRelationship(SCHOOL_TYPE, SchoolType.class);
  }

  public String getEntityName() {
    return SCHOOLYEAR;
  }
  public String getName(){
    return getSchoolYearName();
  }
  public void setSchoolYearName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolYearName(){
    return getStringColumnValue(NAME);
  }
  public void setSchoolYearInfo(String info){
    this.setColumn(INFO,info);
  }
  public String getSchoolYearInfo(){
    return getStringColumnValue(INFO);
  }
  public void setSchoolYearAge(int age){
    this.setColumn(AGE,age);
  }
  public int getSchoolYearAge(){
    return getIntColumnValue(AGE);
  }
	
	public int getSchoolTypeId() {
		int schoolTypeId = getIntColumnValue(SCHOOL_TYPE);
		
		if (schoolTypeId < 1) {
			System.out.println("SchoolYearBMPBean : SchoolTypeId = "+schoolTypeId+" ... trying to find a new one");
			String name = this.getSchoolYearName();
			if (name != null) {
				if (name.equalsIgnoreCase("f")) {
					System.out.println("... NEEDS backwards compatability ...");
				} else {
					System.out.println("... NEEDS backwards compatability ...");
				}
			} else {
				System.out.println("... SchoolYear has name = NULL (pk="+getPrimaryKey().toString()+")");
			}
		}
		
		return schoolTypeId;
	}
	
	public void setSchoolTypeId(int id) {
		setColumn(SCHOOL_TYPE, id);
	}
	
	public Collection getSchoolYears(SchoolType schoolType) throws FinderException {
		return this.idoFindAllIDsByColumnBySQL(SCHOOL_TYPE, schoolType.getPrimaryKey().toString());
	}
	
  public Collection ejbFindAllSchoolYears()throws javax.ejb.FinderException {
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(getEntityName()).appendOrderBy(AGE);
    return super.idoFindPKsBySQL(sql.toString());
  }
  
  public Collection ejbFindAllSchoolYearBySchoolType(int schoolTypeId) throws FinderException {
	IDOQuery sql = idoQuery();
	sql.appendSelectAllFrom(getEntityName());
	sql.appendWhereEquals(SCHOOL_TYPE, schoolTypeId);
	sql.appendOrderBy(getIDColumnName());
	return super.idoFindPKsByQuery(sql);
  }

	/**
	 * @deprecated
	 */
  public Collection ejbFindAllByAge(int age) throws javax.ejb.FinderException{
		return super.idoFindPKsBySQL("select * from "+getEntityName()+" where "+AGE+" like '"+age+"'");
  }
  
  public Collection ejbFindAllByAge(SchoolType schoolType, int age) throws javax.ejb.FinderException{
    return super.idoFindPKsBySQL("select * from "+getEntityName()+" where "+AGE+" like '"+age+"' AND "+SCHOOL_TYPE+" = '"+schoolType.getPrimaryKey().toString()+"'");
  }
  
  public Collection ejbFindAllByIDs(String[] schoolYearIDs) throws FinderException {
  	IDOQuery query = idoQuery();
  	query.appendSelectAllFrom(this).appendWhere().append(getIDColumnName()).appendInArray(schoolYearIDs);
  	query.appendOrderBy(AGE);
  	return idoFindPKsByQuery(query);
  }

  /**
   * @deprecated
   */
  public Integer ejbFindByYearName(String name) throws javax.ejb.FinderException{
    return (Integer) super.idoFindOnePKBySQL("select * from "+getEntityName()+" where "+NAME+" like '"+name+"'");
  }
  
  public Integer ejbFindByYearName(SchoolType schoolType, String name) throws javax.ejb.FinderException{
		return (Integer) super.idoFindOnePKBySQL("select * from "+getEntityName()+" where "+NAME+" like '"+name+"' AND "+SCHOOL_TYPE+" = '"+schoolType.getPrimaryKey().toString()+"'");
  }

}