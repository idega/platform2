/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;

import java.sql.Timestamp;

import javax.ejb.CreateException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class AssessmentRoundThread extends Thread {
	private String _name = null;
	private Group _club = null;
	private Group _division = null;
	private Group _group = null;
	private User _user = null;
	private Timestamp _start = null;
	private boolean _useParent = false;
	private boolean _includeChildren = false;
	private IWApplicationContext _iwac = null;
	
	
	public AssessmentRoundThread(String name, Group club, Group division, Group group, Timestamp start, User user, boolean useParent, boolean includeChildren, IWApplicationContext iwac) {
		_name = name;
		_club = club;
		_division = division;
		_group = group;
		_start = start;
		_user = user;
		_useParent = useParent;
		_includeChildren = includeChildren;
		_iwac = iwac;
	}
	
	public void run() {
		IWTimestamp now = IWTimestamp.RightNow();
		try {
			AssessmentRound round = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class)).create();
			round.setName(_name);
			round.setClub(_club);
			round.setGroup(_group);
			round.setStartTime(now.getTimestamp());
			round.setExecutedBy(_user);
			round.setUseParentTariff(_useParent);
			round.setIncludeChildren(_includeChildren);
			
			round.store();
		}
		catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}