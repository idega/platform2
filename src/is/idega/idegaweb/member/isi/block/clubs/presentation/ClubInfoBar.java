/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.clubs.presentation;

import is.idega.idegaweb.member.isi.block.clubs.business.ClubInfoBusinessBean;
import is.idega.idegaweb.member.util.IWMemberConstants;

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
import com.idega.presentation.CSSMultiLevelMenu;
import com.idega.presentation.IWContext;
import com.idega.presentation.CSSMultiLevelMenu.CSSMenu;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * @author <a href="mailto:jonas@idega.is>Jonas</a>
 */
public class ClubInfoBar extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private String pageId = null;
	
	public static final String PARAM_NAME_GROUP_ID = "group_id";
	
	public void main(IWContext iwc) {
	    
		pageId = iwc.getParameter(ClubPageIncluder.PARAM_CALLING_PAGE_ID);
		
		_collator = Collator.getInstance(iwc.getLocale());
		Group club = getClub(iwc);
		if(club==null) {
			System.out.println("No club to show division menu for");
			return;
		}
		
		_biz = getBusiness(iwc);
		Collection divisions = _biz.getDivisionsForClub(club);
		Iterator divIter = divisions.iterator();
		CSSMultiLevelMenu menuBar = new CSSMultiLevelMenu();
		
		while(divIter.hasNext()) {
			Group division = (Group) divIter.next();
			
			addDivisionToMenuBar(menuBar, division);
		}
		add(menuBar);
	}
	
	private void addDivisionToMenuBar(CSSMultiLevelMenu menuBar, Group division) {
		List playerGroups = new ArrayList(division.getChildGroups());
		
		Collections.sort(playerGroups, new Comparator() {
			
			public int compare(Object arg0, Object arg1) {
				Group g0 = (Group) arg0;
				Group g1 = (Group) arg1;
				String t0 = g0.getGroupType();
				String t1 = g1.getGroupType();
				int result = _collator.compare( g0.getName(), g1.getName());
				if(!t0.equals(t1)) {
					if(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(t0)) {
						result = 1;
					} else if(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(t1)) {
						result = -1;
					} else {
						if(IWMemberConstants.GROUP_TYPE_CLUB_TRAINER.equals(t0)) {
							result = -1;
						} else {
							result = 1;
						}
					}
				}
				return result;
			}
			
		});
		
		String divName = division.getName();
		
		CSSMenu topLevelMenu = menuBar.createCSSMenu(division.getName());
		menuBar.add(topLevelMenu);
		
		Iterator playerGroupIter = playerGroups.iterator();
		boolean flockInserted = false;
		while(playerGroupIter.hasNext()) {
			Group playerGroup = (Group) playerGroupIter.next();
			boolean isFlock = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(playerGroup.getGroupType());
			if(isFlock && !flockInserted) {
				topLevelMenu.add(new HorizontalRule());
				flockInserted = true;
			}
			if(showGroup(playerGroup)) {
				Link link = new Link(playerGroup.getName());
				link.addParameter(PARAM_NAME_GROUP_ID, playerGroup.getPrimaryKey().toString());
				if(pageId!=null) {
				  link.setPage(Integer.parseInt(pageId));
				}
				link.addParameter(IWMemberConstants.REQUEST_PARAMETER_SELECTED_GROUP_ID,playerGroup.getPrimaryKey().toString());
				topLevelMenu.add(link);
			}
		}

	}
	
	private boolean showGroup(Group group) {
		String type = group.getGroupType();
		boolean show = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(type) || 
		               IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TRAINER.equals(type) || 
		               IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_COMMITTEE.equals(type);
		return show;
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