/*
 * $Id: ChildCareBusinessHome.java 1.1 9.9.2004 aron Exp $
 * Created on 9.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;





import com.idega.business.IBOHome;

/**
 * 
 *  Last modified: $Date: 9.9.2004 12:48:47 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareBusinessHome extends IBOHome {
    public ChildCareBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
