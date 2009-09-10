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



public interface EmailAccount {



  /**

   *  Gets the name of the EmailAccount object

   *

   * @return    The name value

   */

  public String getName();





  /**

   *  Gets the host of the EmailAccount object

   *

   * @return    The host value

   */

  public String getHost();





  /**

   *  Gets the user of the EmailAccount object

   *

   * @return    The user value

   */

  public String getUser();





  /**

   *  Gets the password of the EmailAccount object

   *

   * @return    The password value

   */

  public String getPassword();





  /**

   *  Gets the protocol of the EmailAccount object

   *

   * @return    The protocol value

   */

  public int getProtocol();





  /**

   *  Gets the protocol of the EmailAccount object

   *

   * @return    The protocol name value

   */

  public String getProtocolName();





  /**

   *  Gets the created of the EmailAccount object

   *

   * @return    The created value

   */

  public java.sql.Timestamp getCreated();
  
  public Integer getIdentifier();

}

