package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.block.application.data.Application;
import javax.ejb.FinderException;

public interface RejectionHistoryHome extends IDOHome {
	public RejectionHistory create() throws CreateException;

	public RejectionHistory findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByApplication(Application application)
			throws FinderException;
}