/*
 * $Id: ProviderSessionHome.java,v 1.2 2004/10/11 14:32:03 aron Exp $
 * Created on 28.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.provider.business;




import com.idega.business.IBOHome;

/**
 * 
 *  Last modified: $Date: 2004/10/11 14:32:03 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface ProviderSessionHome extends IBOHome {
    public ProviderSession create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
