/*
 * Created on 24.8.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.member.business;

import com.idega.business.IBOHomeImpl;

/**
 * @author aron
 *
 * MemberFamilyLogicHomeImpl TODO Describe this type
 */
public class MemberFamilyLogicHomeImpl extends IBOHomeImpl implements
        MemberFamilyLogicHome {
    protected Class getBeanInterfaceClass() {
        return MemberFamilyLogic.class;
    }

    public MemberFamilyLogic create() throws javax.ejb.CreateException {
        return (MemberFamilyLogic) super.createIBO();
    }

}
