/*
 * Created on 1.5.2004
 */
package is.idega.idegaweb.golf.legacy.business;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.util.Converter;


/**
 * Title: GolfLegacyBusinessBean
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class GolfLegacyBusinessBean extends IBOServiceBean implements GolfLegacyBusiness{

	public static final String UNION_TYPE_UNION = "golf_union";
	public static final String GROUP_TYPE_UNION = IWMemberConstants.GROUP_TYPE_LEAGUE;
	public static final String UNION_TYPE_CLUB = "golf_club";
	public static final String GROUP_TYPE_CLUB = IWMemberConstants.GROUP_TYPE_CLUB;
	public static final String UNION_TYPE_EXTRA_CLUB = "extra_club";
	
	public static final String UNION_TYPE_NONE = "none";
	
	
	
	public GolfLegacyBusinessBean() {
		super();
	}	
	
	
	public Collection getLogin(is.idega.idegaweb.golf.block.login.data.LoginTable golfLogin) throws IDOLookupException, FinderException{
		return ((LoginTableHome)IDOLookup.getHome(LoginTable.class)).findLoginsForUser(golfLogin.getMember().getICUser());
	}
	
	public is.idega.idegaweb.golf.block.login.data.LoginTable getGolfLogin(LoginTable login) throws EJBException, IDOLookupException, FinderException {
		Member member = ((MemberHome)IDOLookup.getHome(Member.class)).findMemberByIWMemberSystemUser(Converter.convertToNewUser(login.getUser()));
		return ((is.idega.idegaweb.golf.block.login.data.LoginTableHome)IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.block.login.data.LoginTable.class)).findByMember(member);
	}
	
//	public Group getGroup(is.idega.idegaweb.golf.entity.Group golfGroup) {
//		return null;
//	}
//	
//	public is.idega.idegaweb.golf.entity.Group getGolfGroup(Group group){
//		return null;
//	}
	
	
	public void copyAllFromUnionToGroup() {
		System.out.println("[GOLF] Start: Copy all unions to Group...");
		System.out.println("[GOLF] finding all unions");
		
		try {

			Collection unions = ((UnionHome)IDOLookup.getHomeLegacy(Union.class)).findAllUnions();
			System.out.println("[GOLF] Start: create group for unions");
			GroupHome groupHome = (GroupHome)IDOLookup.getHome(Group.class);
			for (Iterator unionIter = unions.iterator(); unionIter.hasNext();) {
				System.out.print("[GOLF] next union > ");
				Union union = (Union) unionIter.next();
				System.out.println(union.getPrimaryKey());
				
				System.out.println("[GOLF] get group for union");
				
				
				
				Group group = union.getUnionFromIWMemberSystem();
				
				if(group == null) {
					System.out.println("[GOLF] create new group for union");
					group = groupHome.create();
				}
				
				group.setName(union.getName());
				group.setAbbrevation(union.getAbbrevation());
				group.setShortName(union.getAbbrevation());
				
				String unionType = union.getUnionType();
				
				if(UNION_TYPE_CLUB.equals(unionType)) {
					group.setGroupType(GROUP_TYPE_CLUB);
				} else if(UNION_TYPE_EXTRA_CLUB.equals(unionType)) {
					group.setGroupType(GROUP_TYPE_CLUB);
				} else if(UNION_TYPE_UNION.equals(unionType)) {
					group.setGroupType(GROUP_TYPE_UNION);
				} else {
					continue;
				}
				
				group.store();
				
				union.setICGroup(group);
				union.store();
				
				//System.out.println("[GOLF] create group.address for union.address");
				//address
				
				
				//System.out.println("[GOLF] create group.phone for union.phone");
				//Phone
				
				
				//System.out.println("[GOLF] create group.group for union.group");
				//Group
				
				
				
			}
			
			
			System.out.println("[GOLF] ... No exceptions");
		} catch (FinderException e) {
				e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		
		System.out.println("[GOLF] Finish: Copy all unions to Group");
		
	}
	
	

}
