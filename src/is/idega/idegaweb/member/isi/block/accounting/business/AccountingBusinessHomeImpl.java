/*
 * Created on Sep 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import com.idega.business.IBOHomeImpl;

/**
 * @author IBM
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AccountingBusinessHomeImpl extends IBOHomeImpl implements
        AccountingBusinessHome {
    protected Class getBeanInterfaceClass() {
        return AccountingBusiness.class;
    }

    public AccountingBusiness create() throws javax.ejb.CreateException {
        return (AccountingBusiness) super.createIBO();
    }

}
