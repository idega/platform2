/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.data;

import java.util.Date;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface NBSSignedEntity {
	void setXmlSignedData(String data);
	void setSignedBy(int userId);
	void setSignedDate(Date time);
	void setSignedFlag(boolean flag);
	void store();
	String getText();
}
