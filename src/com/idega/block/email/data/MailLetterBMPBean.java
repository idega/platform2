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



public class MailLetterBMPBean extends com.idega.data.GenericEntity implements com.idega.block.email.data.MailLetter,com.idega.block.email.business.EmailLetter {



  /**  @todo Description of the Field */

  public final static String TABLE_NAME = "em_letter";

  /**  @todo Description of the Field */

  public final static String SUBJECT = "subject";

  /**  @todo Description of the Field */

  public final static String BODY = "body";

  /**  @todo Description of the Field */

  public final static String FROM_ADDRESS = "from_email";

  /**  @todo Description of the Field */

  public final static String FROM_NAME = "from_name";

  /**  @todo Description of the Field */

  public final static String CREATED = "created";

  /**  @todo Description of the Field */

  public final static String TYPE = "letter_type";











  /**  Constructor for the MailLetter object */

  public MailLetterBMPBean() {

    super();

  }





  /**

   *  Constructor for the MailLetter object

   *

   * @param  id                Description of the Parameter

   * @exception  SQLException  Description of the Exception

   */

  public MailLetterBMPBean(int id) throws SQLException {

    super(id);

  }



  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(SUBJECT, "Subject", true, true, String.class);

    addAttribute(BODY, "Body", true, true, String.class, 4000);

    addAttribute(FROM_ADDRESS, "From email", true, true, String.class);

    addAttribute(FROM_NAME, "From name", true, true, String.class);

    addAttribute(TYPE, "Type", true, true, Integer.class);

    addAttribute(CREATED, "created", true, true, Timestamp.class);

    addManyToManyRelationShip(com.idega.core.file.data.ICFile.class);

  }





  /**

   *  Gets the entityName of the MailLetter object

   *

   * @return    The entity name value

   */

  public String getEntityName() {

    return TABLE_NAME;

  }





  /**

   *  Gets the subject of the MailLetter object

   *

   * @return    The subject value

   */

  public String getSubject() {

    return getStringColumnValue(SUBJECT);

  }





  /**

   *  Gets the body of the MailLetter object

   *

   * @return    The body value

   */

  public String getBody() {

    return getStringColumnValue(BODY);

  }





  /**

   *  Gets the fromEmail of the MailLetter object

   *

   * @return    The from email value

   */

  public String getFromAddress() {

    return getStringColumnValue(FROM_ADDRESS);

  }





  /**

   *  Gets the fromName of the MailLetter object

   *

   * @return    The from name value

   */

  public String getFromName() {

    return getStringColumnValue(FROM_NAME);

  }





  /**

   *  Gets the type of the MailLetter object

   *

   * @return    The type value

   */

  public int getType() {

    return getIntColumnValue(TYPE);

  }





  /**

   *  Gets the created of the MailLetter object

   *

   * @return    The created value

   */

  public Timestamp getCreated() {

    return (Timestamp) getColumnValue(CREATED);

  }





  /**

   *  Sets the subject attribute of the MailLetter object

   *

   * @param  subject  The new subject value

   */

  public void setSubject(String subject) {

    setColumn(SUBJECT, subject);

  }





  /**

   *  Sets the body attribute of the MailLetter object

   *

   * @param  body  The new body value

   */

  public void setBody(String body) {

    setColumn(BODY, body);

  }





  /**

   *  Sets the fromEmail attribute of the MailLetter object

   *

   * @param  fromEmail  The new fromEmail value

   */

  public void setFromAddress(String fromAddress) {

    setColumn(FROM_ADDRESS, fromAddress);

  }





  /**

   *  Sets the fromName attribute of the MailLetter object

   *

   * @param  fromName  The new fromName value

   */

  public void setFromName(String fromName) {

    setColumn(FROM_NAME, fromName);

  }





  /**

   *  Sets the type attribute of the MailLetter object

   *

   * @param  type  The new type value

   */

  public void setType(int type) {

    setColumn(TYPE, type);

  }





  /**

   *  Sets the created attribute of the MailLetter object

   *

   * @param  created  The new created value

   */

  public void setCreated(Timestamp created) {

    setColumn(CREATED, created);

  }



	/* (non-Javadoc)
	 * @see com.idega.block.email.data.MailLetter#getIdentifier()
	 */
	public Integer getIdentifier() {
		return (Integer) getPrimaryKey();
	}

}

