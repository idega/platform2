/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;





import com.idega.business.IBOHomeImpl;

/**
 * @author Dainis
 *
 */
public class ChildCareBusinessHomeImpl extends IBOHomeImpl implements
        ChildCareBusinessHome {
    protected Class getBeanInterfaceClass() {
        return ChildCareBusiness.class;
    }

    public ChildCareBusiness create() throws javax.ejb.CreateException {
        return (ChildCareBusiness) super.createIBO();
    }

}
