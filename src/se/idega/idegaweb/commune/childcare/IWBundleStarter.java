package se.idega.idegaweb.commune.childcare;

import java.util.Iterator;
import java.util.List;
import se.idega.idegaweb.commune.accounting.userinfo.presentation.ChildContractHistoryWindow;
import se.idega.idegaweb.commune.childcare.business.ChildCareDeceasedUserBusiness;
import se.idega.idegaweb.commune.childcare.presentation.ChildContractsWindow;
import se.idega.idegaweb.commune.user.business.DeceasedUserBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public static void test() {
		IWBundleStarter starter = new IWBundleStarter();
		starter.start(null);
		ImplementorRepository repository = ImplementorRepository.getInstance();
		List list = repository.newInstances(DeceasedUserBusiness.class, IWBundleStarter.class);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			DeceasedUserBusiness business = (DeceasedUserBusiness) iterator.next();
			System.out.println(business);
		}
		
	}

	public void start(IWBundle starterBundle) {
		ImplementorRepository repository = ImplementorRepository.getInstance();
		repository.addImplementor(DeceasedUserBusiness.class, ChildCareDeceasedUserBusiness.class);
		repository.addImplementor(ChildContractHistoryWindow.class, ChildContractsWindow.class);
	}

	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
	
}
