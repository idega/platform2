/*
 * Created on 11.8.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.application.business;




import com.idega.business.IBOHome;

/**
 * @author aron
 *
 * ApplicationServiceHome TODO Describe this type
 */
public interface ApplicationServiceHome extends IBOHome {
    public ApplicationService create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
