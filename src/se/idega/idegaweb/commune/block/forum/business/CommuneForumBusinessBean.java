package se.idega.idegaweb.commune.block.forum.business;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

public class CommuneForumBusinessBean extends IBOServiceBean implements CommuneForumBusiness{

	public User getModerator(ICCategory category) {
		int ownerGroupId = category.getOwnerGroupId();
		if (ownerGroupId > 0) {
			UserHome uHome;
			try {
				uHome = (UserHome) IDOLookup.getHome(User.class);
				return uHome.findByPrimaryKey(new Integer(ownerGroupId));
			} catch (FinderException e1) {
				e1.printStackTrace(System.err);
			} catch (IDOLookupException e) {
				e.printStackTrace(System.err);
			}
		}
		return null;
	}
	
	public boolean isModerator(ICCategory cat, User currentUser) {
		User user = getModerator(cat);
		int userId = -1; 
		int currUserId = -2;
		if (user != null) {
			userId = ((Integer)user.getPrimaryKey()).intValue();
		}
		if (currentUser != null) {
			currUserId = ((Integer)currentUser.getPrimaryKey()).intValue();	
		}
				
		return userId == currUserId;	
	}
	
	public boolean isModerator(int topicID, User currentUser) {
		ICCategory cat = CategoryFinder.getInstance().getCategory(topicID);
		User user = getModerator(cat);
		int userId = -1; 
		int currUserId = -2;
		if (user != null) {
			userId = ((Integer)user.getPrimaryKey()).intValue();
		}
		if (currentUser != null) {
			currUserId = ((Integer)currentUser.getPrimaryKey()).intValue();	
		}
				
		return userId == currUserId;	
	}
	
	public int getFileCount(ICCategory cat) {
		Collection coll;
		try {
			coll = cat.getFiles();
			if (coll != null && !coll.isEmpty()) {
				return coll.size();
			}
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}
		return 0;	
	}
	
	public Collection convertFilePKsToFileCollection(Collection filePKs) {
		Vector v = new Vector();
		if (filePKs != null && !filePKs.isEmpty()) {
			Iterator iter = filePKs.iterator();
			while (iter.hasNext()) {
				v.add(iter.next());	
			}
		}
		return v;	
	}
	
	public ICCategoryHome getICCategoryHome() throws IDOLookupException {
		return (ICCategoryHome) IDOLookup.getHome(ICCategory.class);	
	}
	
	public ICFileHome getICFileHome() throws IDOLookupException {
		return (ICFileHome) IDOLookup.getHome(ICFile.class);	
	}

}
