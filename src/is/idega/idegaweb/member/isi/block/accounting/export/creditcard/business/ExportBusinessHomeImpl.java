/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author palli
 */
public class ExportBusinessHomeImpl extends IBOHomeImpl implements
        ExportBusinessHome {
    protected Class getBeanInterfaceClass() {
        return ExportBusiness.class;
    }

    public ExportBusiness create() throws javax.ejb.CreateException {
        return (ExportBusiness) super.createIBO();
    }

}
