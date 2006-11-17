package is.idega.idegaweb.campus.nortek.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CardTransactionLogHome extends IDOHome {
	public CardTransactionLog create() throws CreateException;

	public CardTransactionLog findByPrimaryKey(Object pk) throws FinderException;
}