package com.idega.block.email.data;



import java.sql.SQLException;
import java.sql.Timestamp;



/**

 *  Title: Description: Copyright: Copyright (c) 2001 Company:

 *

 * @author     <br>

 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>

 *

 * @created    14. mars 2002

 * @version    1.0

 */



public class MailListBMPBean extends com.idega.data.GenericEntity implements com.idega.block.email.data.MailList,com.idega.block.email.business.EmailList {



  private final static String TABLE_NAME = "EM_LIST";

  private final static String NAME = "NAME";

  private final static String DESCRIPTION = "DESCRIPTION";

  private final static String CREATED = "CREATED_DATE";





  /**  Constructor for the MailList object */

  public MailListBMPBean() {

    super();

  }





  /**

   *  Constructor for the MailList object

   *

   * @param  id                Description of the Parameter

   * @exception  SQLException  Description of the Exception

   */

  public MailListBMPBean(int id) throws SQLException {

    super(id);

  }





  /**  @todo Description of the Method */

  public void initializeAttributes() {

    addAttribute(this.getIDColumnName());

    addAttribute(NAME, "Name", true, true, String.class);

    addAttribute(DESCRIPTION, "Description", true, true, String.class);

    addAttribute(CREATED, "Created", true, true, Timestamp.class);

    addManyToManyRelationShip(com.idega.core.contact.data.Email.class);

  }





  /**

   *  Gets the entityName of the MailList object

   *

   * @return    The entity name value

   */

  public String getEntityName() {

    return TABLE_NAME;

  }





  /**

   *  Gets the name of the MailList object

   *

   * @return    The name value

   */

  public String getName() {

    return getStringColumnValue(NAME);

  }





  /**

   *  Sets the name attribute of the MailList object

   *

   * @param  name  The new name value

   */

  public void setName(String name) {

    setColumn(NAME, name);

  }





  /**

   *  Gets the description of the MailList object

   *

   * @return    The description value

   */

  public String getDescription() {

    return getStringColumnValue(DESCRIPTION);

  }





  /**

   *  Sets the description attribute of the MailList object

   *

   * @param  name  The new description value

   */

  public void setDescription(String name) {

    setColumn(DESCRIPTION, name);

  }





  /**

   *  Gets the created of the MailList object

   *

   * @return    The created value

   */

  public Timestamp getCreated() {

    return (Timestamp) getColumnValue(CREATED);

  }





  /**

   *  Sets the created attribute of the MailList object

   *

   * @param  created  The new created value

   */

  public void setCreated(Timestamp created) {

    setColumn(CREATED, created);

  }

}

