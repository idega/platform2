package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.IWTimestamp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolSeasonBMPBean extends GenericEntity implements SchoolSeason{

  public final static String SCHOOLSEASON = "sch_school_season";
  public final static String START = "season_start";
  public final static String END = "season_end";
  public final static String DUE_DATE = "due_date";
  public final static String NAME = "name";

  public String getEntityName() {
    return SCHOOLSEASON;
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(NAME,"name",true,true,String.class);
    this.addAttribute(START,"Start",true,true,Date.class);
    this.addAttribute(END,"End",true,true,Date.class);
    this.addAttribute(DUE_DATE,"Duedate",true,true,Date.class);
  }

  public String getName(){
    return getSchoolSeasonName();
  }

  public String getSchoolSeasonName(){
    return this.getStringColumnValue(NAME);
  }

  public void setSchoolSeasonName(String name){
    this.setColumn(NAME,name);
  }


  public Date getSchoolSeasonEnd(){
    return (Date) this.getColumnValue(END);
  }

  public void setSchoolSeasonEnd(java.util.Date end){
    this.setColumn(END,end);
  }

  public Date getSchoolSeasonStart(){
    return (Date) this.getColumnValue(START);
  }

  public void setSchoolSeasonStart(java.util.Date start){
    this.setColumn(START,start);
  }

  public Date getSchoolSeasonDueDate(){
    return (Date) this.getColumnValue(DUE_DATE);
  }

  public void setSchoolSeasonDueDate(java.util.Date due){
    this.setColumn(DUE_DATE,due);
  }

  public java.util.Collection ejbFindAllSchoolSeasons()throws FinderException{
    return super.idoFindAllIDsBySQL();
  }
  
  /**
   * Gets the previous season to this one.
   * @throws FinderException if none is found
   */
  public SchoolSeason getPreviousSeason() throws FinderException{
  	return getSchoolSeasonHome().findPreviousSchoolSeason(this);
  }

	protected SchoolSeasonHome getSchoolSeasonHome(){
		return (SchoolSeasonHome) getEJBLocalHome();
	}

  /**
   * Find precisely the  schoolseasons that is previous to SchoolSeason schoolSeason
   * @param schoolSeason
   * @return
   * @throws FinderException If no season was found
   */
  public Integer ejbFindPreviousSchoolSeason(SchoolSeason schoolSeason)throws FinderException {
	Collection coll = ejbFindAllPreviousSchoolSeasons(schoolSeason);
	if(coll!=null){
		Iterator iter = coll.iterator();
		if(iter.hasNext()){
			return (Integer)iter.next();
		}
	}
	throw new FinderException("No previous SchoolSeasons to season "+schoolSeason+" found");
  }

  /**
   * Find all schoolseasons that start before SchoolSeason schoolSeason and order by start date
   * @param schoolSeason
   * @return
   * @throws FinderException If an error occured during the search
   */
  public java.util.Collection ejbFindAllPreviousSchoolSeasons(SchoolSeason schoolSeason)throws FinderException {
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(this.getEntityName()).appendWhere().append(START).append("<").appendWithinSingleQuotes(schoolSeason.getSchoolSeasonStart().toString()).appendOrderBy(START);
    return super.idoFindPKsBySQL(sql.toString());
  }

	public Integer ejbFindSeasonByDate(Date date)throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhere().append(START).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().append(END).appendGreaterThanOrEqualsSign().append(date);
		return (Integer) idoFindOnePKByQuery(sql);
	}
	
	public Integer ejbFindCurrentSeason() throws FinderException {
		return ejbFindSeasonByDate(new IWTimestamp().getDate());
	}
	
	public Collection ejbFindSchoolSeasonsActiveInTimePeriod(Date firstDateInPeriod, Date lastDateInPeriod) throws FinderException{
		
		IDOQuery query = idoQuery();
		
		query.appendSelectAllFrom(this);
		query.appendWhere();
		//Where
		query.append(START);
		query.appendLessThanOrEqualsSign();
		query.append(lastDateInPeriod);
		//and
		query.appendAnd();
		query.append(END);
		query.appendGreaterThanSign();
		query.append(firstDateInPeriod);

		return idoFindPKsByQuery(query);
		
	}
}