package is.idega.idegaweb.member.business;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJBException;

import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Description:	Use this business class to handle member information
 * Copyright:    Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */
public class MemberUserBusinessBean extends UserBusinessBean implements MemberUserBusiness, UserBusiness {
	
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.business.MemberUserBusiness#moveUserBetweenDivisions(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group, com.idega.util.IWTimestamp, com.idega.util.IWTimestamp)
	 */
	public void moveUserBetweenDivisions(User user, Group fromDiv, Group toDiv, IWTimestamp term, IWTimestamp init) {
		//TODO Eiki polish this impl.
		//this method get the parents of the user and finds out which is of the correct type and then uses that.
		//the division that the user is sent to must have a child group of type iwme_temporary
		Collection parentGroups = null;
		Collection children = null;
		List parentsToPassivate = new Vector();
		
		try {
			GroupBusiness groupBiz = getGroupBusiness();
			parentGroups = groupBiz.getParentGroupsRecursive(user);
			children = groupBiz.getChildGroups(toDiv);
			
			//find the player groups relations and set them to passive_pending
			if (parentGroups != null && !parentGroups.isEmpty() && children!=null && !children.isEmpty() ) {//user must have parents!
				Iterator iter = parentGroups.iterator();

				while (iter.hasNext()) {
					Group parent = (Group) iter.next();
					String type = parent.getGroupType();
					if (type.equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
						Collection par = groupBiz.getParentGroupsRecursive(parent);
						if (par.contains(fromDiv)) {
							Collection col = groupBiz.getGroupRelationHome().findGroupsRelationshipsContainingBiDirectional( ((Integer)fromDiv.getPrimaryKey()).intValue(), ((Integer)parent.getPrimaryKey()).intValue() , "ST_ACTIVE" ); //Status liklega otharfi
							if(col!=null && !col.isEmpty()){
								Iterator iterator = col.iterator();
								while (iterator.hasNext()) {
									GroupRelation rel = (GroupRelation) iterator.next();
									rel.setPassivePending();
									rel.setTerminationDate(term.getTimestamp());
									rel.store();
									//set passive by?	
								}
							}
						}
					}
				}
			
				//set the users relations to the new divisions temporary group to active_pending
				Iterator iter2 = children.iterator();

				while (iter2.hasNext()) {
					Group child = (Group) iter2.next();
					String type = child.getGroupType();
					if (type.equals(IWMemberConstants.GROUP_TYPE_TEMPORARY)) {
						
						GroupRelation rel = groupBiz.getGroupRelationHome().create();
						rel.setRelatedGroup(user);
						rel.setGroup(child);
						rel.setRelationshipType("GROUP_PARENT");
						rel.setActivePending();
						rel.setInitiationDate(init.getTimestamp());
						rel.store();
						
					}
				}
				

			
			}
			
			
			
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}