/*
 * Created on 24.8.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.member.business;

import com.idega.business.IBOHome;

/**
 * @author aron
 *
 * MemberFamilyLogicHome TODO Describe this type
 */
public interface MemberFamilyLogicHome extends IBOHome {
    public MemberFamilyLogic create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
