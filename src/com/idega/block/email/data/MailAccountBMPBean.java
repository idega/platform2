package com.idega.block.email.data;



import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.block.email.business.MailFinder;



/**

 *  Title: Description: Copyright: Copyright (c) 2001 Company: idega.is

 *

 * @author     2000 - idega team - <br>

 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>

 *

 * @created    14. mars 2002

 * @version    1.0

 */



public class MailAccountBMPBean extends com.idega.data.GenericEntity implements com.idega.block.email.data.MailAccount,com.idega.block.email.business.EmailAccount {





  public final static String TABLE_NAME = "em_account";

  public final static String NAME = "name";

  public final static String HOST = "host";

  public final static String PORT = "port";

  public final static String LOGIN_NAME = "login_name";

  public final static String PASSWORD = "login_passwd";

  public final static String PROTOCOL = "protocol";

  public final static String CREATED = "created";





  /**  Constructor for the MailAccount object */

  public MailAccountBMPBean() {

    super();

  }





  /**

   *  Constructor for the MailAccount object

   *

   * @param  id                record id

   * @exception  SQLException

   */

  public MailAccountBMPBean(int id) throws SQLException {

    super(id);

  }





  /**

   * @todo    Description of the Method

   */

  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(NAME, "name", true, true, String.class);

    addAttribute(HOST, "server", true, true, String.class);

    addAttribute(PORT, "port", true, true, Integer.class);

    addAttribute(LOGIN_NAME, "login", true, true, String.class);

    addAttribute(PASSWORD, "password", true, true, String.class);

    addAttribute(PROTOCOL, "protocol", true, true, Integer.class);

    addAttribute(CREATED, "creation date", true, false, Timestamp.class);



  }





  /**

   *  Gets the entityName of the MailAccount object

   *

   * @return    The entity name value

   */

  public String getEntityName() {

    return TABLE_NAME;

  }





  /**

   *  Gets the host of the MailAccount object

   *

   * @return    The host value

   */

  public String getHost() {

    return getStringColumnValue(HOST);

  }





  /**

   *  Gets the port of the MailAccount object

   *

   * @return    The port value

   */

  public int getPort() {

    return getIntColumnValue(PORT);

  }





  /**

   *  Gets the loginName of the MailAccount object

   *

   * @return    The login name value

   */

  public String getUser() {

    return getStringColumnValue(LOGIN_NAME);

  }





  /**

   *  Gets the password of the MailAccount object

   *

   * @return    The password value

   */

  public String getPassword() {

    return getStringColumnValue(PASSWORD);

  }





  /**

   *  Gets the creationDate of the MailAccount object

   *

   * @return    The creation date value

   */

  public Timestamp getCreated() {

    return (Timestamp) getColumnValue(CREATED);

  }





  /**

   *  Sets the host attribute of the MailAccount object

   *

   * @param  host  The new host value

   */

  public void setHost(String host) {

    setColumn(HOST, host);

  }





  /**

   *  Sets the port attribute of the MailAccount object

   *

   * @param  port  The new port value

   */

  public void setPort(int port) {

    setColumn(PORT, port);

  }







  /**

   *  Sets the user attribute of the MailAccount object

   *

   * @param  LoginName  The new user value

   */

  public void setUser(String LoginName) {

    setColumn(LOGIN_NAME, LoginName);

  }





  /**

   *  Sets the password attribute of the MailAccount object

   *

   * @param  Password  The new password value

   */

  public void setPassword(String Password) {

    setColumn(PASSWORD, Password);

  }







  /**

   *  Sets the created attribute of the MailAccount object

   *

   * @param  creationDate  The new created value

   */

  public void setCreated(Timestamp creationDate) {

    setColumn(CREATED, creationDate);

  }





  /**

   *  Gets the protocol of the MailAccount object

   *

   * @return    The protocol value

   */

  public int getProtocol() {

    return getIntColumnValue(PROTOCOL);

  }





  /**

   *  Sets the protocol attribute of the MailAccount object

   *

   * @param  protocol  The new protocol value

   */

  public void setProtocol(int protocol) {

    setColumn(PROTOCOL, protocol);

  }





  /**

   *  Gets the name of the MailAccount object

   *

   * @return    The name value

   */

  public String getName() {

    return getStringColumnValue(NAME);

  }





  /**

   *  Sets the name attribute of the MailAccount object

   *

   * @param  name  The new name value

   */

  public void setName(String name) {

    setColumn(NAME, name);

  }



  public String getProtocolName(){

    return MailFinder.getInstance().getProtocolName(getProtocol());

  }



	/* (non-Javadoc)
	 * @see com.idega.block.email.business.EmailAccount#getIdentifier()
	 */
	public Integer getIdentifier() {
		return (Integer) getPrimaryKey();
	}

}

