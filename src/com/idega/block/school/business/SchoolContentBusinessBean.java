package com.idega.block.school.business;

import com.idega.block.school.data.School;
import com.idega.business.IBOSessionBean;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWUserContext;


public class SchoolContentBusinessBean extends IBOSessionBean implements SchoolContentBusiness {
	public static String PARAMETER_SCHOOL_ID = "pr_schl_id";
	

	public String getParameterSchoolId() {
		return PARAMETER_SCHOOL_ID;	
	}
	
	public boolean hasEditPermission(School school) {
		User user = this.getUserContext().getUser();
		/** IMPLEMENTA */
		/** Temp ... ad skoda bara 
		try {
			if (user != null ) {
				List groups = UserGroupBusiness....
				if (groups != null) {
					Group group;
					Iterator iter = groups.iterator();
					table.add("User groups", 1, row);
					while (iter.hasNext()) {
						++row;
						group = (Group) iter.next();
						table.add(group.getName(), 1, row);
					}
				}else {
					table.add("Groups == null", 1, row);	
				}
			}else {
				table.add("User == null", 1, row);	
			}
		} catch (SQLException e) {
			table.add("Error !", 1, row);	
			e.printStackTrace(System.err);
		}
*/	
		return false;
	}	
}
