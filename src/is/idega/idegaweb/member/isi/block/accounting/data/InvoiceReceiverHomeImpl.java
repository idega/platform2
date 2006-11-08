package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class InvoiceReceiverHomeImpl extends IDOFactory implements InvoiceReceiverHome {
	public Class getEntityInterfaceClass() {
		return InvoiceReceiver.class;
	}

	public InvoiceReceiver create() throws CreateException {
		return (InvoiceReceiver) super.createIDO();
	}

	public InvoiceReceiver findByPrimaryKey(Object pk) throws FinderException {
		return (InvoiceReceiver) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((InvoiceReceiverBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((InvoiceReceiverBMPBean) entity).ejbFindByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByInvoiceReceiver(User invoiceReceiver) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((InvoiceReceiverBMPBean) entity).ejbFindByInvoiceReceiver(invoiceReceiver);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByClub(Group club) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((InvoiceReceiverBMPBean) entity).ejbFindByClub(club);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByDivision(Group division) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((InvoiceReceiverBMPBean) entity).ejbFindByDivision(division);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByGroup(Group group) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((InvoiceReceiverBMPBean) entity).ejbFindByGroup(group);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}