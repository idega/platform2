/*
 * Created on Feb 2, 2005
 */
package is.idega.idegaweb.member;

import is.idega.idegaweb.member.presentation.LinkToUserStatsImpl;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.presentation.LinkToUserStats;

/**
 * @author Sigtryggur
 */
public class IWBundleStarter implements IWBundleStartable {

    public void start(IWBundle starterBundle) {
		ImplementorRepository repository = ImplementorRepository.getInstance();
		repository.addImplementor(LinkToUserStats.class, LinkToUserStatsImpl.class);
    }

    public void stop(IWBundle starterBundle) {
 		
    }
}
