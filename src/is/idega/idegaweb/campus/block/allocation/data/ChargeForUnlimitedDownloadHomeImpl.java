package is.idega.idegaweb.campus.block.allocation.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ChargeForUnlimitedDownloadHomeImpl extends IDOFactory implements
		ChargeForUnlimitedDownloadHome {
	public Class getEntityInterfaceClass() {
		return ChargeForUnlimitedDownload.class;
	}

	public ChargeForUnlimitedDownload create() throws CreateException {
		return (ChargeForUnlimitedDownload) super.createIDO();
	}

	public ChargeForUnlimitedDownload findByPrimaryKey(Object pk)
			throws FinderException {
		return (ChargeForUnlimitedDownload) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ChargeForUnlimitedDownloadBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCharged() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ChargeForUnlimitedDownloadBMPBean) entity)
				.ejbFindAllCharged();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ChargeForUnlimitedDownload findByUser(User user)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ChargeForUnlimitedDownloadBMPBean) entity)
				.ejbFindByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}