/*
 * $Id: CommuneUserBusinessHomeImpl.java,v 1.2 2004/09/18 17:25:27 aron Exp $
 * Created on 18.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;





import com.idega.business.IBOHomeImpl;

/**
 * 
 *  Last modified: $Date: 2004/09/18 17:25:27 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public class CommuneUserBusinessHomeImpl extends IBOHomeImpl implements
        CommuneUserBusinessHome {
    protected Class getBeanInterfaceClass() {
        return CommuneUserBusiness.class;
    }

    public CommuneUserBusiness create() throws javax.ejb.CreateException {
        return (CommuneUserBusiness) super.createIBO();
    }

}
