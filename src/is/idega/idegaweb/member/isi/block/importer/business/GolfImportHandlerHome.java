/*
 * $Id: GolfImportHandlerHome.java,v 1.1 2004/10/12 14:52:33 eiki Exp $
 * Created on Oct 11, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.importer.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/10/12 14:52:33 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.1 $
 */
public interface GolfImportHandlerHome extends IBOHome {

	public GolfImportHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
