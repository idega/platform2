/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.clubs.presentation;

import is.idega.idegaweb.member.isi.block.clubs.business.ClubInfoBusinessBean;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.Block;
import com.idega.presentation.DynamicJSMenu;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ClubInfoBar extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public void main(IWContext iwc) {
		_collator = Collator.getInstance(iwc.getLocale());
		Group club = getClub(iwc);
		if(club==null) {
			System.out.println("No club to show division menu for");
			return;
		}
		System.out.println("creating link bar for club " + club.getName());
		_biz = getBusiness(iwc);
		Collection divisions = _biz.getDivisionsForClub(club);
		Iterator divIter = divisions.iterator();
		DynamicJSMenu divMenu = new DynamicJSMenu();
		while(divIter.hasNext()) {
			Group division = (Group) divIter.next();
			
			addDivisionToMenu(divMenu, division);
		}
		add(divMenu);
	}
	
	private void addDivisionToMenu(DynamicJSMenu menu, Group division) {
		List flocks = new ArrayList(division.getChildGroups());
		
		Collections.sort(flocks, new Comparator() {
			
			public int compare(Object arg0, Object arg1) {
				return _collator.compare( ((Group) arg0).getName(), ((Group) arg1).getName());
			}
			
		});
		
		String divName = division.getName();
		menu.addLinkToMenu(_menuCount, divName, null);
		
		Iterator flockIter = flocks.iterator();
		while(flockIter.hasNext()) {
			Group flock = (Group) flockIter.next();
			menu.addLinkToMenu(_menuCount, flock.getName(), null);
		}
		
		_menuCount++;
	}
		
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private Group getClub(IWContext iwc) {
		String clubId = iwc.getParameter(ClubPageIncluder.PARAM_ROOT_CLUB_ID);
		Group club = null;
		if(clubId!=null) {
			try {
				GroupBusiness groupBiz = getGroupBusiness(iwc);
				club = groupBiz.getGroupByGroupID(Integer.parseInt(clubId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("no club id found");
		}
		return club;
	}
		
	private ClubInfoBusinessBean getBusiness(IWContext iwc) {
		try {
			return (ClubInfoBusinessBean) IBOLookup.getServiceInstance(iwc.getApplicationContext(), ClubInfoBusinessBean.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), GroupBusiness.class);
		} catch (IBOLookupException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//private int _clubId = 330185;
	private int _menuCount = 0;
	private ClubInfoBusinessBean _biz;
	private static Collator _collator = null;
}