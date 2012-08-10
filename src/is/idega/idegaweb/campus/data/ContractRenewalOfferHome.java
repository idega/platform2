package is.idega.idegaweb.campus.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import java.util.Collection;

public interface ContractRenewalOfferHome extends IDOHome {
	public ContractRenewalOffer create() throws CreateException;

	public ContractRenewalOffer findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;

	public ContractRenewalOffer findByContract(Contract contract)
			throws FinderException;

	public Collection findAllOpen() throws FinderException;

	public Collection findAllUnanswered() throws FinderException;

	public ContractRenewalOffer findByUUID(String uuid, boolean showClosed)
			throws FinderException;

	public Collection findAllUnsentContracts() throws FinderException;
}