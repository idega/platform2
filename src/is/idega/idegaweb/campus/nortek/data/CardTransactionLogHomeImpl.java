package is.idega.idegaweb.campus.nortek.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class CardTransactionLogHomeImpl extends IDOFactory implements CardTransactionLogHome {
	public Class getEntityInterfaceClass() {
		return CardTransactionLog.class;
	}

	public CardTransactionLog create() throws CreateException {
		return (CardTransactionLog) super.createIDO();
	}

	public CardTransactionLog findByPrimaryKey(Object pk) throws FinderException {
		return (CardTransactionLog) super.findByPrimaryKeyIDO(pk);
	}
}