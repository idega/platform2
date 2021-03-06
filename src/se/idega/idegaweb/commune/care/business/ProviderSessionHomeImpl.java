/*
 * $Id: ProviderSessionHomeImpl.java,v 1.1 2004/10/12 18:05:19 thomas Exp $
 * Created on 28.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;




import com.idega.business.IBOHomeImpl;

/**
 * 
 *  Last modified: $Date: 2004/10/12 18:05:19 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class ProviderSessionHomeImpl extends IBOHomeImpl implements
        ProviderSessionHome {
    protected Class getBeanInterfaceClass() {
        return ProviderSession.class;
    }

    public ProviderSession create() throws javax.ejb.CreateException {
        return (ProviderSession) super.createIBO();
    }

}
