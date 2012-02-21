package is.idega.idegaweb.campus.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ContractRenewalOfferHome extends IDOHome {
	public ContractRenewalOffer create() throws CreateException;

	public ContractRenewalOffer findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAllOpen() throws FinderException;

	public ContractRenewalOffer findByUUID(String uuid) throws FinderException;
}