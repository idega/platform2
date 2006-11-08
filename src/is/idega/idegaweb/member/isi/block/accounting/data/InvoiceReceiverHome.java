package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface InvoiceReceiverHome extends IDOHome {
	public InvoiceReceiver create() throws CreateException;

	public InvoiceReceiver findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByUser(User user) throws FinderException;

	public Collection findByInvoiceReceiver(User invoiceReceiver) throws FinderException;

	public Collection findByClub(Group club) throws FinderException;

	public Collection findByDivision(Group division) throws FinderException;

	public Collection findByGroup(Group group) throws FinderException;
}