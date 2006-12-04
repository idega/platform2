package is.idega.idegaweb.campus.nortek.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CardHomeImpl extends IDOFactory implements CardHome {
	public Class getEntityInterfaceClass() {
		return Card.class;
	}

	public Card create() throws CreateException {
		return (Card) super.createIDO();
	}

	public Card findByPrimaryKey(Object pk) throws FinderException {
		return (Card) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CardBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}