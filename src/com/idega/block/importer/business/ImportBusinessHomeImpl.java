/*
 * Created on Sep 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.idega.block.importer.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author IBM
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImportBusinessHomeImpl extends IBOHomeImpl implements
        ImportBusinessHome {
    protected Class getBeanInterfaceClass() {
        return ImportBusiness.class;
    }

    public ImportBusiness create() throws javax.ejb.CreateException {
        return (ImportBusiness) super.createIBO();
    }

}
