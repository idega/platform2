/*
 * Created on 11.8.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package com.idega.block.application.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author aron
 *
 * ApplicationServiceHomeImpl TODO Describe this type
 */
public class ApplicationServiceHomeImpl extends IBOHomeImpl implements
        ApplicationServiceHome {
    protected Class getBeanInterfaceClass() {
        return ApplicationServiceHomeImpl.class;
    }

    public ApplicationService create() throws javax.ejb.CreateException {
        return (ApplicationService) super.createIBO();
    }

}
