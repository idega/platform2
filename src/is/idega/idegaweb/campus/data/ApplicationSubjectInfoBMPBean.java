/*
 * $Id: ApplicationSubjectInfoBMPBean.java,v 1.4 2004/06/09 17:07:36 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.ApplicationSubject;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ApplicationSubjectInfoBMPBean extends com.idega.data.GenericView implements ApplicationSubjectInfo {

/*
CREATE VIEW V_APP_SUBJECT_INFO(
    SUBJECT_ID,
    NAME,
    STATUS,
    NUMBER,
    LAST_SUBMISSION,
    FIRST_SUBMISSION,
    LAST_CHANGE,
    FIRST_CHANGE)
AS
select app1.app_subject_id subject_id, sub.description name, app1.status,
count(app1.status) number,
max(app1.submitted) last_submission,
min(app1.submitted) first_submission,
max(app1.status_changed )last_change,
min(app1.status_changed )first_change
from app_application app1,app_subject sub
where app1.app_subject_id = sub.app_subject_id
group by app1.app_subject_id, sub.description,app1.status;

*/

  public static String VIEWNAME = "V_APP_SUBJECT_INFO";
  public static String SUBJECTID = "SUBJECT_ID";
  public static String NAME =  "NAME";
  public static String STATUS =  "STATUS";
  public static String NUMBER =   "NUMBER";
  public static String LASTSUBMISSION = "LAST_SUBMISSION";
  public static String FIRSTSUBMISSION =  "FIRST_SUBMISSION";
  public static String LASTCHANGE =  "LAST_CHANGE";
  public static String FIRSTCHANGE =  "FIRST_CHANGE";


  public ApplicationSubjectInfoBMPBean() {

  }

  public ApplicationSubjectInfoBMPBean(int id) throws SQLException {
  }
  
  public Class getPrimaryKeyClass() {
	return ApplicationSubjectInfoKey.class;	
  }
  
  public Object ejbFindByPrimaryKey(ApplicationSubjectInfoKey primaryKey) throws FinderException {
	return super.ejbFindByPrimaryKey(primaryKey);
  }

  public Object ejbCreate(ApplicationSubjectInfoKey primaryKey) throws CreateException {
	setPrimaryKey(primaryKey);
	return super.ejbCreate();
  }

  public void initializeAttributes() {
    addAttribute(SUBJECTID,"Subjed Id",true,true,Integer.class);
    setAsPrimaryKey(SUBJECTID,true);
    addAttribute(NAME,"Name",true,true,String.class);
    addAttribute(STATUS,"status",true,true,String.class);
    setAsPrimaryKey(STATUS,true);
    addAttribute(NUMBER,"Number",true,true,Integer.class);
    addAttribute(LASTSUBMISSION,"LAST_SUBMISSION",true,true,Timestamp.class);
    addAttribute(FIRSTSUBMISSION,"FIRST_SUBMISSION",true,true,Timestamp.class);
    addAttribute(LASTCHANGE,"LAST_CHANGE",true,true,Timestamp.class);
    addAttribute(FIRSTCHANGE,"FIRST_CHANGE",true,true,Timestamp.class);

  }

  public String getEntityName() {
    return VIEWNAME;
  }
  public int getSubjectId() {
    return getIntColumnValue(SUBJECTID);
  }
  public String getSubjectName() {
    return getStringColumnValue(NAME);
  }
  public String getStatus() {
    return getStringColumnValue(STATUS);
  }
  public int getNumber() {
    return getIntColumnValue(NUMBER);
  }
  public Timestamp getLastSubmission() {
    return (Timestamp)getColumnValue(LASTSUBMISSION);
  }
  public Timestamp getFirstSubmission() {
    return (Timestamp)getColumnValue(FIRSTSUBMISSION);
  }
  public Timestamp getLastChange() {
    return (Timestamp)getColumnValue(LASTCHANGE);
  }
  public Timestamp getFirstChange() {
    return (Timestamp)getColumnValue(FIRSTCHANGE);
  }
  public void insert()throws SQLException{
  }
  public void delete()throws SQLException{
  }
  public Collection ejbFindAll() throws FinderException{
	   return super.idoFindPKsByQuery(super.idoQueryGetSelect());
	 }
  
  public Collection ejbFindAllNonExpired(java.sql.Date date) throws FinderException{
  	Table type =new Table(this);
	Table related = new Table(ApplicationSubject.class);
	SelectQuery query = new SelectQuery(type);
	query.addColumn(new WildCardColumn(type));
	query.addJoin(type,SUBJECTID,related,"app_subject_id");
	query.addCriteria(new MatchCriteria(related,"expires",MatchCriteria.GREATEREQUAL,date));
	return idoFindPKsBySQL(query.toString());
  }

	/* (non-Javadoc)
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql =new StringBuffer();
		sql.append("CREATE VIEW ").append(VIEWNAME).append(" ( ");
		sql.append(SUBJECTID).append(" , ");
		sql.append(NAME).append(" , ");
		sql.append(STATUS).append(" , ");
		sql.append(NUMBER).append(" , ");
		sql.append(LASTSUBMISSION).append(" , ");
		sql.append(FIRSTSUBMISSION).append(" , ");
		sql.append(LASTCHANGE).append(" , ");
		sql.append(FIRSTCHANGE).append(" ) ");
		sql.append(" AS ");
		sql.append(" select app1.app_subject_id subject_id, sub.description name, app1.status, ");
		sql.append(" count(app1.status) number, ");
		sql.append(" max(app1.submitted) last_submission, ");
		sql.append(" min(app1.submitted) first_submission, ");
		sql.append(" max(app1.status_changed ) last_change, ");
		sql.append(" min(app1.status_changed ) first_change ");
		sql.append(" from app_application app1,app_subject sub ");
		sql.append(" where app1.app_subject_id = sub.app_subject_id ");
		sql.append(" group by app1.app_subject_id, sub.description,app1.status; ");
		return sql.toString();
	}

}