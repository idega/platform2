/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;

import com.idega.idegaweb.IWApplicationContext;

/**
 * @author palli
 */
public class AssessmentRoundThread extends Thread {
//	private String _name = null;
//	private Group _club = null;
//	private Group _division = null;
//	private Group _group = null;
//	private User _user = null;
//	private Timestamp _start = null;
//	private boolean _useParent = false;
//	private boolean _includeChildren = false;
	private AssessmentRound _round = null;
	private IWApplicationContext _iwac = null;
	
	
	public AssessmentRoundThread(AssessmentRound assessmentRound, IWApplicationContext iwac) {
//		_name = name;
//		_club = club;
//		_division = division;
//		_group = group;
//		_start = start;
//		_user = user;
//		_useParent = useParent;
//		_includeChildren = includeChildren;
		_round = assessmentRound;
		_iwac = iwac;
	}
	
	public void run() {
	}
}