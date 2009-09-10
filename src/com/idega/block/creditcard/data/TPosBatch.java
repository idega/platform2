/*
 *  $Id: TPosBatch.java,v 1.1 2004/04/22 21:40:27 gimmi Exp $
 *
 *  Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.creditcard.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class TPosBatch extends GenericEntity {
  private final static String ENTITY_NAME = "tpos_batch";
  private final static String BATCH_NUMBER = "batch_nr";
  private final static String OPENED = "batch_opened";
  private final static String CLOSED = "batch_closed";

  /**
   * Constructor for the TPosBatch object
   */
  public TPosBatch() {
  }

  public TPosBatch(int id) throws SQLException {
    super(id);
  }

  /**
   * Description of the Method
   */
  public void initializeAttributes() {
    /**
     * @todo:   implement this com.idega.data.GenericEntity abstract method
     */
  }

  /**
   * Gets the entityName attribute of the TPosBatch object
   *
   * @return   The entityName value
   */
  public String getEntityName() {
    return(ENTITY_NAME);
  }
}
