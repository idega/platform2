/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business;




import com.idega.business.IBOHome;

/**
 * @author palli
 */
public interface ExportBusinessHome extends IBOHome {
    public ExportBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
