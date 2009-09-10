package com.idega.block.email.business;



/**

 *  Title: Description: Copyright: Copyright (c) 2001 Company:

 *

 * @author     <br>

 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>

 *

 * @created    14. mars 2002

 * @version    1.0

 */



public interface EmailList{



  /**

   *  Gets the name of the EmailList object

   *

   * @return    The name value

   */

  public String getName();





  /**

   *  Gets the description of the EmailList object

   *

   * @return    The description value

   */

  public String getDescription();





  /**

   *  Gets the creation date of the EmailList object

   *

   * @return    The created value

   */

  public java.sql.Timestamp getCreated();

}

