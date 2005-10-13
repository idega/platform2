/*
 * $Id: CommuneMessageBusinessHomeImpl.java,v 1.1 2005/10/13 18:36:11 laddi Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;




import com.idega.business.IBOHomeImpl;

/**
 * 
 *  Last modified: $Date: 2005/10/13 18:36:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class CommuneMessageBusinessHomeImpl extends IBOHomeImpl implements
        CommuneMessageBusinessHome {
    protected Class getBeanInterfaceClass() {
        return CommuneMessageBusiness.class;
    }

    public CommuneMessageBusiness create() throws javax.ejb.CreateException {
        return (CommuneMessageBusiness) super.createIBO();
    }

}
