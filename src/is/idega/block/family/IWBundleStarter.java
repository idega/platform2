package is.idega.block.family;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.user.data.GroupRelationType;
import com.idega.user.data.GroupRelationTypeHome;

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
		insertStartData();
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}

	protected void insertStartData() {
		/*
		 * @todo Move to user plugin system
		 **/
		insertGroupRelationType("GROUP_FAMILY");
		insertGroupRelationType("FAM_CHILD");
		insertGroupRelationType("FAM_PARENT");
		insertGroupRelationType("FAM_SPOUSE");
		insertGroupRelationType("FAM_COHABITANT");
	}
	
	private void insertGroupRelationType(String groupRelationType) {
		/**
		 * @todo Move this to a more appropriate place
		 **/
		try {
			GroupRelationTypeHome grtHome = (GroupRelationTypeHome) com.idega.data.IDOLookup.getHome(GroupRelationType.class);
			GroupRelationType grType;
			try {
				grType = grtHome.findByPrimaryKey(groupRelationType);
			}
			catch (FinderException fe) {
				try {
					grType = grtHome.create();
					grType.setType(groupRelationType);
					grType.store();
//					sendStartMessage("Registered Group relation type: '" + groupRelationType + "'");
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
			}
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
	}
	
}
