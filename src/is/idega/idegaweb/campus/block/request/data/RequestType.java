/*
 * $Id: RequestType.java,v 1.1 2001/12/29 14:03:15 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestType extends GenericEntity {
  private static final String ENTITY_NAME = "re_request_type";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String EMAIL = "email";

  /**
   *
   */
  public RequestType() {
    super();
  }

  /**
   *
   */
  public RequestType(int id) throws SQLException {
    super(id);
  }

  /**
   *
   */
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnName(),"Name",true,true,String.class);
    addAttribute(getColumnDescription(),"Description",true,true,String.class);
    addAttribute(getColumnEmail(),"E-mail",true,true,String.class);
    setMaxLength(getColumnName(),255);
    setMaxLength(getColumnDescription(),4000);
    setMaxLength(getColumnEmail(),255);
  }

  /**
   *
   */
  public String getEntityName() {
    return(ENTITY_NAME);
  }

  /**
   *
   */
  public String getColumnName() {
    return(NAME);
  }

  /**
   *
   */
  public String getColumnDescription() {
    return(DESCRIPTION);
  }

  /**
   *
   */
  public String getColumnEmail() {
    return(EMAIL);
  }

  /**
   *
   */
  public String getName() {
    return(getStringColumnValue(getColumnName()));
  }

  /**
   *
   */
  public void setName(String name) {
    setColumn(getColumnName(),name);
  }

  /**
   *
   */
  public String getDescription() {
    return(getStringColumnValue(getColumnDescription()));
  }

  /**
   *
   */
  public void setDescription(String description) {
    setColumn(getColumnDescription(),description);
  }

  /**
   *
   */
  public String getEmail() {
    return(getStringColumnValue(getColumnEmail()));
  }

  /**
   *
   */
  public void setEmail(String email) {
    setColumn(getColumnEmail(),email);
  }
}