//idega 2000 - Tryggvi Larusson
/*
 * Copyright 2000 idega.is All Rights Reserved.
 */

package is.idega.idegaweb.golf.business;

import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import java.sql.SQLException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson </a>
 * @version 1.0
 */

public class AccessControl {

	public static final String ACCESSCONTROL_GROUP_PARAMETER = "iw_accesscontrol_group";
	
    public static final String USER_ATTRIBUTE_PARAMETER = "member_login";

	public static final String ADMIN_GROUP = "administrator";
	public static final String CLUB_ADMIN_GROUP = "club_admin";
	public static final String CLUB_WORKER_GROUP = "club_worker";
	public static final String TOURNAMENT_MANAGER_GROUP = "tournament_manager";

	public static final String CURRENT_GOLF_UNION_ID_ATTRIBUTE = "golf_union_id";
	public static final String CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE = "admin_golf_union_id";

	public static Member getMember(IWContext modinfo) {
		return (Member) modinfo.getSession().getAttribute(USER_ATTRIBUTE_PARAMETER);
	}

	public static String getAccesscontrolGroupForUser(IWContext modinfo) {
		return (String) modinfo.getSessionAttribute(ACCESSCONTROL_GROUP_PARAMETER);
	}

	public static void setCurrentGolfUnionID(IWContext modinfo, String unionID) {
		modinfo.setSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE, unionID);
	}

	public static String getCurrentGolfUnionID(IWContext modinfo) {
		return (String) modinfo.getSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE);
	}

	public static String getGolfUnionOfClubAdmin(IWContext modinfo) {
		return (String) modinfo.getSessionAttribute(CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE);
	}

	static void setGolfUnionOfClubAdmin(IWContext modinfo, String unionID) {
		modinfo.setSessionAttribute(CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE, unionID);
	}

	public static void setAccesscontrolGroupForUser(IWContext modinfo, String accessControlGroup) {
		modinfo.setSessionAttribute(ACCESSCONTROL_GROUP_PARAMETER, accessControlGroup);
	}

	public static boolean isAdmin(IWContext modinfo) throws SQLException {
		Member member = getMember(modinfo);
		if (member == null) {
			return false;
		}
		String acc_group = getAccesscontrolGroupForUser(modinfo);
		if (acc_group == null) {
			Group[] access = member.getGroups(); //  (member).getGenericGroups();
			for (int i = 0; i < access.length; i++) {
				if ("administrator".equals(access[i].getName())) {
					setAccesscontrolGroupForUser(modinfo, ADMIN_GROUP);
					return true;
				}
				if ("club_admin".equals(access[i].getName())) {
					int uni_id = member.getMainUnionID();
					setAccesscontrolGroupForUser(modinfo, CLUB_ADMIN_GROUP);
					setGolfUnionOfClubAdmin(modinfo, Integer.toString(uni_id));
					Object ID = modinfo.getSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE);
					if (ID != null) {
						if (uni_id == Integer.parseInt(((String) ID))) {
							return true;
						}
					}
				}
			}
			return false;
		}
		if (acc_group.equals(ADMIN_GROUP)) {
			return true;
		}
		if (acc_group.equals(CLUB_ADMIN_GROUP)) {
			String currentUnion = getCurrentGolfUnionID(modinfo);
			if (currentUnion != null) {
				if (currentUnion.equals(getGolfUnionOfClubAdmin(modinfo))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasPermission(String permissionType, PresentationObject obj, IWContext info) {
		try {
			return info.getAccessController().hasPermission(permissionType, obj, info);
			//return
			// com.idega.core.accesscontrol.business.AccessControl.hasPermission(permissionType,obj,info);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public static boolean isDeveloper(IWContext modinfo) {
		return (modinfo.getSession().getAttribute("member_access") != null) && (modinfo.getSession().getAttribute("member_access").equals("developer"));
	}

	public static boolean isClubAdmin(IWContext modinfo) {
		return (modinfo.getSession().getAttribute("member_access") != null) && (modinfo.getSession().getAttribute("member_access").equals("club_admin"));
	}

	public static boolean isUser(IWContext modinfo) {
		return (modinfo.getSession().getAttribute("member_access") != null) && (modinfo.getSession().getAttribute("member_access").equals("user"));
	}

	public static boolean isClubWorker(IWContext modinfo) throws SQLException {
		Member member = getMember(modinfo);
		if (member == null) {
			return false;
		}
		Group[] access = member.getGroups(); //  (member).getGenericGroups();
		for (int i = 0; i < access.length; i++) {
			if ("administrator".equals(access[i].getName())) {
				return true;
			}
			if ("club_worker".equals(access[i].getName())) {
				Object ID = modinfo.getSessionAttribute("golf_union_id");
				if (ID != null) {
					int uni_id = member.getMainUnionID();
					if (uni_id == Integer.parseInt(((String) ID))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isTournamentManager(IWContext modinfo) throws SQLException {
		Member member = getMember(modinfo);
		if (member == null) {
			return false;
		}
		Group[] access = member.getGroups(); //  (member).getGenericGroups();
		for (int i = 0; i < access.length; i++) {
			if ("administrator".equals(access[i].getName())) {
				return true;
			}
			if ("tournament_manager".equals(access[i].getName())) {
				return true;
			}
		}
		return false;

	}

}