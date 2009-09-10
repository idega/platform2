package com.idega.block.email.business;
/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 * @created    14. mars 2002
 * @version    1.0

 */
public interface EmailLetter {
	/**  @todo Description of the Field */
	public final static int TYPE_RECEIVED = 0;
	/**  @todo Description of the Field */
	public final static int TYPE_SENT = 1;
	/**  @todo Description of the Field */
	public final static int TYPE_SUBSCRIPTION = 2;
	/**  @todo Description of the Field */
	public final static int TYPE_UNSUBSCRIBE = 3;
	public final static int TYPE_DRAFT = 4;
	public final static int TYPE_LIST = 5;
	/**
	 *  Gets the body of the EmailLetter object
	 *
	 * @return    The body value
	 */
	public String getBody();
	/**
	 *  Gets the subject of the EmailLetter object
	 *
	 * @return    The subject value
	 */
	public String getSubject();
	/**
	 *  Gets the fromAddress of the EmailLetter object
	 *
	 * @return    The from address value
	 */
	public String getFromAddress();
	/**
	 *  Gets the fromName of the EmailLetter object
	 *
	 * @return    The from name value
	 */
	public String getFromName();
	/**
	 *  Gets the type of the EmailLetter object
	 *
	 * @return    The type value
	 */
	public int getType();
	/**
	 *  Gets the created of the EmailLetter object
	 *
	 * @return    The created value
	 */
	public java.sql.Timestamp getCreated();
	
	public Integer getIdentifier();
}
