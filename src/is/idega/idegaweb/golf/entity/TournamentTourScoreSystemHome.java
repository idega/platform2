package is.idega.idegaweb.golf.entity;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface TournamentTourScoreSystemHome extends IDOHome {
	public TournamentTourScoreSystem create() throws CreateException;

	public TournamentTourScoreSystem findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;
}