/**
 * 
 */
package se.idega.idegaweb.commune.accounting.school.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author Dainis
 *
 */
public class ProviderBusinessHomeImpl extends IBOHomeImpl implements
        ProviderBusinessHome {
    protected Class getBeanInterfaceClass() {
        return ProviderBusiness.class;
    }

    public ProviderBusiness create() throws javax.ejb.CreateException {
        return (ProviderBusiness) super.createIBO();
    }

}
