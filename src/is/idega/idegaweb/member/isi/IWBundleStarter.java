package is.idega.idegaweb.member.isi;

import is.idega.idegaweb.member.util.IWMemberConstants;
import com.idega.block.dataquery.business.DataqueryConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.user.presentation.UserConstants;
import com.idega.user.util.ICUserConstants;

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

	public void start(IWBundle starterBundle) {
		// changing constants, setting the right help bundle in the com.idega.user bundle
		UserConstants.HELP_BUNDLE_IDENTFIER = "is.idega.idegaweb.member.isi";
		// changing constants, setting the right help bundle in the com.idega.core.bundle
		ICUserConstants.HELP_BUNDLE_IDENTFIER = "is.idega.idegaweb.member.isi";
		// setting the group types of the highest top nodes for the query builder
		DataqueryConstants.highestTopNodeGroupTypes = new String[] {
				IWMemberConstants.GROUP_TYPE_FEDERATION,
				IWMemberConstants.GROUP_TYPE_UNION,
				IWMemberConstants.GROUP_TYPE_REGIONAL_UNION,
				IWMemberConstants.GROUP_TYPE_LEAGUE,
				IWMemberConstants.GROUP_TYPE_CLUB,
				IWMemberConstants.GROUP_TYPE_CLUB_DIVISION};

	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
