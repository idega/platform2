/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data.xml;

import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public interface QueryPart {
	
	public XMLElement getQueryElement();
	public String encode();
	/**
	 *  
	 * @return true if element contains a lock element
	 */
	public boolean isLocked();
	
	/**
	 * Defines if element is locked or not
	 * @param locked
	 */
	public void setLocked(boolean locked);
}
