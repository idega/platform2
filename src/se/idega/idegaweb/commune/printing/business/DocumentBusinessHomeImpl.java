/*
 * $Id: DocumentBusinessHomeImpl.java 1.1 7.10.2004 aron Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.business;




import com.idega.business.IBOHomeImpl;

/**
 * 
 *  Last modified: $Date: 7.10.2004 00:06:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class DocumentBusinessHomeImpl extends IBOHomeImpl implements
        DocumentBusinessHome {
    protected Class getBeanInterfaceClass() {
        return DocumentBusiness.class;
    }

    public DocumentBusiness create() throws javax.ejb.CreateException {
        return (DocumentBusiness) super.createIBO();
    }

}
