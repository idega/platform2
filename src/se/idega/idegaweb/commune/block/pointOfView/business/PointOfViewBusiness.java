/*
 * $Id: PointOfViewBusiness.java,v 1.1 2004/09/29 11:31:40 thomas Exp $
 * Created on Sep 27, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.block.pointOfView.business;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.block.pointOfView.data.PointOfView;
import com.idega.business.IBOService;
import com.idega.presentation.text.Link;
import com.idega.repository.data.ImplementorPlaceholder;


/**
 * 
 *  Last modified: $Date: 2004/09/29 11:31:40 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface PointOfViewBusiness extends IBOService, ImplementorPlaceholder {
	
	String getCaseCodeKeyForPointOfView() throws RemoteException;
	
	Link getLinkToPageForPointOfView(int pageID, PointOfView pointOfView) throws RemoteException;
	
	PointOfView findPointOfView(int primarykey) throws RemoteException, FinderException;
	
	Collection findUnhandledPointOfViewsInGroups(Collection groups) throws RemoteException, FinderException;
}
