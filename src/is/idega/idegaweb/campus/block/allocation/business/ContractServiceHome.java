/*
 * Created on 1.9.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.allocation.business;




import com.idega.business.IBOHome;

/**
 * @author aron
 *
 * ContractServiceHome TODO Describe this type
 */
public interface ContractServiceHome extends IBOHome {
    public ContractService create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
