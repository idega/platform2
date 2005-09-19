/*
 * $Id: ChildCareBusinessHomeImpl.java 1.1 Sep 19, 2005 bluebottle Exp $
 * Created on Sep 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;





import com.idega.business.IBOHomeImpl;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.data.IDOStoreException;
import com.idega.exception.IWBundleDoesNotExist;
import com.idega.idegaweb.IWBundle;
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
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: bluebottle $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareBusinessHomeImpl extends IBOHomeImpl implements ChildCareBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ChildCareBusiness.class;
	}

	public ChildCareBusiness create() throws javax.ejb.CreateException {
		return (ChildCareBusiness) super.createIBO();
	}

}
