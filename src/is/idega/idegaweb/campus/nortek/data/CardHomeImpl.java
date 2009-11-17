package is.idega.idegaweb.campus.nortek.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import javax.ejb.FinderException;
import java.util.Collection;

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

	public Card findByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CardBMPBean) entity).ejbFindByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByValid(boolean valid) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CardBMPBean) entity).ejbFindAllByValid(valid);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}