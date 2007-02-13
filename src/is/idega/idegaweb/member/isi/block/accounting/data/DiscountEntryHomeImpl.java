package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.util.IWTimestamp;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class DiscountEntryHomeImpl extends IDOFactory implements DiscountEntryHome {
	public Class getEntityInterfaceClass() {
		return DiscountEntry.class;
	}

	public DiscountEntry create() throws CreateException {
		return (DiscountEntry) super.createIDO();
	}

	public DiscountEntry findByPrimaryKey(Object pk) throws FinderException {
		return (DiscountEntry) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByClubAndDivisionAndGroupAndSerial(Group club, Group division, Group group, int fromSerialNumber) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((DiscountEntryBMPBean) entity).ejbFindAllByClubAndDivisionAndGroupAndSerial(club, division, group, fromSerialNumber);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByClubAndDivisionAndGroupAndDate(Group club, Group division, Group group, IWTimestamp fromDate) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((DiscountEntryBMPBean) entity).ejbFindAllByClubAndDivisionAndGroupAndDate(club, division, group, fromDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}