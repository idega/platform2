/*
 * $Id: Request.java,v 1.7 2002/04/06 19:11:14 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.data;

import java.sql.Timestamp;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public interface Request {
  public static final String REQUEST_COMPUTER = "C";
  public static final String REQUEST_REPAIR = "R";

  public static final String REQUEST_STATUS_SENT = "S";
  public static final String REQUEST_STATUS_RECEIVED = "R";
  public static final String REQUEST_STATUS_IN_PROGRESS = "P";
  public static final String REQUEST_STATUS_DONE = "D";
  public static final String REQUEST_STATUS_DENIED = "X";

  /**
   *
   */
  public String getRequestType();

  /**
   *
   */
  public void setRequestType(String type);

  /**
   *
   */
  public int getUserId();

  /**
   *
   */
  public void setUserId(int id);

  /**
   *
   */
  public void setUserId(Integer id);

  /**
   *
   */
  public String getDescription();

  /**
   *
   */
  public void setDescription(String description);

  /**
   *
   */
  public Timestamp getDateSent();

  /**
   *
   */
  public void setDateSent(Timestamp sent);

  /**
   *
   */
  public Timestamp getDateProcessed();

  /**
   *
   */
  public void setDateProcessed(Timestamp processed);

  /**
   *
   */
  public String getStatus();

  /**
   *
   */
  public void setStatus(String status);

  /**
   *
   */
  public Timestamp getDateFailure();

  /**
   *
   */
  public void setDateFailure(Timestamp failure);

  /**
   *
   */
  public String getSpecialTime();

  /**
   *
   */
  public void setSpecialTime(String time);
}
