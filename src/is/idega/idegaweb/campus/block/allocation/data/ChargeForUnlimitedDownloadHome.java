package is.idega.idegaweb.campus.block.allocation.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface ChargeForUnlimitedDownloadHome extends IDOHome {
	public ChargeForUnlimitedDownload create() throws CreateException;

	public ChargeForUnlimitedDownload findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllCharged() throws FinderException;

	public ChargeForUnlimitedDownload findByUser(User user)
			throws FinderException;
}