/*
 * $Id: CitizenAccountBusinessHome.java,v 1.2 2004/11/02 21:20:21 aron Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.account.citizen.business;





import com.idega.business.IBOHome;

/**
 * 
 *  Last modified: $Date: 2004/11/02 21:20:21 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface CitizenAccountBusinessHome extends IBOHome {
    public CitizenAccountBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
