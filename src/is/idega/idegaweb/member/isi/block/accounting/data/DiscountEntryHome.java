package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;
import javax.ejb.FinderException;

public interface DiscountEntryHome extends IDOHome {
	public DiscountEntry create() throws CreateException;

	public DiscountEntry findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByClubAndDivisionAndGroupAndSerial(Group club, Group division, Group group, int fromSerialNumber) throws FinderException;

	public Collection findAllByClubAndDivisionAndGroupAndDate(Group club, Group division, Group group, IWTimestamp fromDate) throws FinderException;
}