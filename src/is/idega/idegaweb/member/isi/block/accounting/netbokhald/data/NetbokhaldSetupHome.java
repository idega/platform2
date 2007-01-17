package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface NetbokhaldSetupHome extends IDOHome {
	public NetbokhaldSetup create() throws CreateException;

	public NetbokhaldSetup findByPrimaryKey(Object pk) throws FinderException;
}