/**
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class PostingBusinessHomeImpl extends IBOHomeImpl implements
		PostingBusinessHome {
	protected Class getBeanInterfaceClass() {
		return PostingBusiness.class;
	}

	public PostingBusiness create() throws javax.ejb.CreateException {
		return (PostingBusiness) super.createIBO();
	}

}
