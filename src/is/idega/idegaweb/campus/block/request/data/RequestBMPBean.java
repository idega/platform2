/*
 * $Id: RequestBMPBean.java,v 1.4 2004/05/24 14:21:42 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.user.data.User;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.request.data.Request {
  protected static final String ENTITY_NAME = "re_request";
  protected static final String USER_ID = "user_id";
  protected static final String DESCRIPTION = "description";
  protected static final String DATE_SENT = "date_sent";
  protected static final String DATE_PROCESSED = "date_processed";
  protected static final String REQUEST_TYPE = "request_type";
  protected static final String DATE_FAILURE = "date_failure";
  protected static final String SPECIAL_REPAIR_TIME = "special_time";
  protected static final String STATUS = "status";

  public RequestBMPBean() {
    super();
  }

  public RequestBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnUserId(),"User id",true,true,Integer.class,com.idega.data.GenericEntity.MANY_TO_ONE,User.class);
    addAttribute(getColumnDescription(),"Description",true,true,String.class);
    addAttribute(getColumnDateSent(),"Date sent",true,true,Timestamp.class);
    addAttribute(getColumnDateProcessed(),"Date processed",true,true,Timestamp.class);
    addAttribute(getColumnRequestType(),"Request type",true,true,String.class);
    addAttribute(getColumnStatus(),"Status",true,true,String.class);
    addAttribute(getColumnDateFailure(),"Date of failure",true,true,Timestamp.class);
    addAttribute(getColumnSpecialTime(),"Request special time",true,true,String.class);
    setMaxLength(getColumnRequestType(),1);
    setMaxLength(getColumnStatus(),1);
    setMaxLength(getColumnDescription(),4000);
  }

  public String getEntityName() {
    return(ENTITY_NAME);
  }

  public static String getColumnRequestType() {
    return(REQUEST_TYPE);
  }

  public static String getColumnUserId() {
    return(USER_ID);
  }

  public static String getColumnDescription() {
    return(DESCRIPTION);
  }

  public static String getColumnDateSent() {
    return(DATE_SENT);
  }

  public static String getColumnDateProcessed() {
    return(DATE_PROCESSED);
  }

  public static String getColumnStatus() {
    return(STATUS);
  }

  public static String getColumnDateFailure() {
    return(DATE_FAILURE);
  }

  public static String getColumnSpecialTime() {
    return(SPECIAL_REPAIR_TIME);
  }

  public String getRequestType() {
    return(getStringColumnValue(getColumnRequestType()));
  }

  public void setRequestType(String type) {
    setColumn(getColumnRequestType(),type);
  }

  public int getUserId() {
    return(getIntColumnValue(getColumnUserId()));
  }

  public void setUserId(int id) {
    setColumn(getColumnUserId(),id);
  }

  public void setUserId(Integer id) {
    setColumn(getColumnUserId(),id);
  }

  public String getDescription() {
    return(getStringColumnValue(getColumnDescription()));
  }

  public void setDescription(String description) {
    setColumn(getColumnDescription(),description);
  }

  public Timestamp getDateSent() {
    return((Timestamp)getColumnValue(getColumnDateSent()));
  }

  public void setDateSent(Timestamp sent) {
    setColumn(getColumnDateSent(),sent);
  }

  public Timestamp getDateProcessed() {
    return((Timestamp)getColumnValue(getColumnDateProcessed()));
  }

  public void setDateProcessed(Timestamp processed) {
    setColumn(getColumnDateProcessed(),processed);
  }

  public String getStatus() {
    return(getStringColumnValue(getColumnStatus()));
  }

  public void setStatus(String status) {
    setColumn(getColumnStatus(),status);
  }

  public Timestamp getDateFailure() {
    return((Timestamp)getColumnValue(getColumnDateFailure()));
  }

  public void setDateFailure(Timestamp failure) {
    setColumn(getColumnDateFailure(),failure);
  }

  public String getSpecialTime() {
    return(getStringColumnValue(getColumnSpecialTime()));
  }

  public void setSpecialTime(String time) {
    setColumn(getColumnSpecialTime(),time);
  }
  
  public Collection ejbFindAll() throws FinderException{
	  return super.idoFindPKsByQuery(idoQueryGetSelect());
  }
  
  public Collection ejbFindByType(String type) throws FinderException{
  	return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEqualsQuoted(getColumnRequestType(),type).appendOrderByDescending(getColumnDateSent()));
  }
  
  public Collection ejbFindByStatus(String status) throws FinderException{
	  return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEqualsQuoted(getColumnStatus(),status).appendOrderByDescending(getColumnDateSent()));
	}
	
  public Collection ejbFindByUser(Integer user) throws FinderException{
     return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getColumnUserId(),user.intValue()).appendOrderByDescending(getColumnDateSent()));
  }
}
