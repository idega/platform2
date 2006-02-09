/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.data.IDOStoreException;
import com.idega.exception.IWBundleDoesNotExist;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.database.ConnectionBroker;
import com.idega.util.text.Name;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;


/**
 * <p>
 * TODO Dainis Describe Type ChildCareBusinessHome
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareBusinessHome extends IBOHome {

	public ChildCareBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
