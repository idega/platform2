/*
 * $Id: Request.java,v 1.3 2002/01/14 11:28:57 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.data;

import com.idega.data.GenericEntity;
import com.idega.core.user.data.User;
import is.idega.idegaweb.campus.block.request.data.RequestType;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Request extends GenericEntity {
  private static final String ENTITY_NAME = "re_request";
  private static final String USER_ID = "user_id";
  private static final String DESCRIPTION = "description";
  private static final String DATE_SENT = "date_sent";
  private static final String DATE_PROCESSED = "date_processed";
  private static final String REQUEST_TYPE = "re_request_type";
  private static final String STATUS = "status";

  public Request() {
    super();
  }

  public Request(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnUserId(),"User id",true,true,Integer.class,GenericEntity.MANY_TO_ONE,User.class);
    addAttribute(getColumnDescription(),"Description",true,true,String.class);
    addAttribute(getColumnDateSent(),"Date sent",true,true,Timestamp.class);
    addAttribute(getColumnDateProcessed(),"Date processed",true,true,Timestamp.class);
    addAttribute(getColumnRequestType(),"Request type",true,true,String.class);
    addAttribute(getColumnStatus(),"Status",true,true,String.class);
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

  public int getRequestType() {
    return(getIntColumnValue(getColumnRequestType()));
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
}