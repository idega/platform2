/**
 * 
 */
package is.idega.idegaweb.campus.block.allocation.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class ContractServiceHomeImpl extends IBOHomeImpl implements
		ContractServiceHome {
	protected Class getBeanInterfaceClass() {
		return ContractService.class;
	}

	public ContractService create() throws javax.ejb.CreateException {
		return (ContractService) super.createIBO();
	}

}
