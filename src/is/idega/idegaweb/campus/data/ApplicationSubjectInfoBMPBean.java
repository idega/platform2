/*
 * $Id: ApplicationSubjectInfoBMPBean.java,v 1.3 2004/06/05 07:34:56 aron Exp $
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

import javax.ejb.FinderException;


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

  public void initializeAttributes() {
    addAttribute(SUBJECTID,"Subjed Id",true,true,Integer.class);
    setAsPrimaryKey(SUBJECTID,true);
    addAttribute(NAME,"Name",true,true,String.class);
    addAttribute(STATUS,"status",true,true,String.class);
    setAsPrimaryKey(STATUS,true);
    addAttribute(NUMBER,"Number",true,true,Integer.class);
    addAttribute(LASTSUBMISSION,"LASTSUBMISSION",true,true,Timestamp.class);
    addAttribute(FIRSTSUBMISSION,"FIRSTSUBMISSION",true,true,Timestamp.class);
    addAttribute(LASTCHANGE,"LASTCHANGE",true,true,Timestamp.class);
    addAttribute(FIRSTCHANGE,"FIRSTCHANGE",true,true,Timestamp.class);

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

	/* (non-Javadoc)
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql =new StringBuffer();
		sql.append("CREATE VIEW V_APP_SUBJECT_INFO( ");
		sql.append(" SUBJECT_ID, ");
		sql.append(" NAME, ");
		sql.append(" STATUS, ");
		sql.append(" NUMBER, ");
		sql.append(" LAST_SUBMISSION, ");
		sql.append(" FIRST_SUBMISSION, ");
		sql.append(" LAST_CHANGE, ");
		sql.append(" FIRST_CHANGE) ");
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