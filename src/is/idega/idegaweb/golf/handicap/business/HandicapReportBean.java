/*
 * $Id: HandicapReportBean.java,v 1.3 2005/02/07 14:20:44 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.business;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;
import is.idega.idegaweb.golf.entity.Union;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;


/**
 * Last modified: $Date: 2005/02/07 14:20:44 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class HandicapReportBean extends IBOSessionBean  implements HandicapReport{

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";
	
	private final static String PREFIX = "handicap_report.";
	private static final String FIELD_CLUB = "golf_club";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_PERSONAL_ID = "personal_id";
	private static final String FIELD_HANDICAP = "handicap";
	private static final String FIELD_DATE = "date";
	private static final String FIELD_POINTS = "points";
	private static final String FIELD_COURSE = "course";

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;

	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(getUserContext().getCurrentLocale());
	}

	private String getLocalizedString(String key, String defaultValue) {
		return _iwrb.getLocalizedString(PREFIX + key, defaultValue);
	}

	public ReportableCollection getClubReport(Union union, String gender, Integer yearFrom, Integer yearTo, Float handicapFrom, Float handicapTo) {
		initializeBundlesIfNeeded();
		Locale currentLocale = getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();

		ReportableField club = new ReportableField(FIELD_CLUB, String.class);
		club.setLocalizedName(getLocalizedString(FIELD_CLUB, "Club"), currentLocale);
		reportCollection.addField(club);

		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		reportCollection.addField(name);

		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		reportCollection.addField(personalID);

		ReportableField handicap = new ReportableField(FIELD_HANDICAP, String.class);
		handicap.setLocalizedName(getLocalizedString(FIELD_HANDICAP, "Handicap"), currentLocale);
		reportCollection.addField(handicap);

		try {
			MemberHome home = (MemberHome) IDOLookup.getHomeLegacy(Member.class);
			Collection members = home.findAllByUnion(union, gender);
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				Member member = (Member) iter.next();
				
				MemberInfo memberInfo = member.getMemberInfo();
				if (memberInfo == null) {
					continue;
				}
				
				Union memberUnion = null;
				try {
					memberUnion = member.getMainUnion();
				}
				catch (SQLException sql) {
					continue;
				}
				
				if (member.getDateOfBirth() != null) {
					IWTimestamp dateOfBirth = new IWTimestamp(member.getDateOfBirth());
					if (yearFrom != null && dateOfBirth.getYear() < yearFrom.intValue()) {
						continue;
					}
					if (yearTo != null && dateOfBirth.getYear() > yearTo.intValue()) {
						continue;
					}
				}
				else if (yearFrom != null || yearTo != null) {
					continue;
				}
				
				if (handicapFrom != null && memberInfo.getHandicap() < handicapFrom.floatValue()) {
					continue;
				}
				if (handicapTo != null && memberInfo.getHandicap() > handicapTo.floatValue()) {
					continue;
				}
				
				ReportableData data = new ReportableData();

				data.addData(club, memberUnion.getName());
				data.addData(name, member.getName());
				data.addData(personalID, member.getSocialSecurityNumber());
				data.addData(handicap, TextSoap.decimalFormat(memberInfo.getHandicap(), 1));

				reportCollection.add(data);
			}
		}
		catch (FinderException fe) {
			log(fe);
		}

		return reportCollection;
	}

	public ReportableCollection getGolferReport(String personalID, Date dateFrom, Date dateTo) {
		initializeBundlesIfNeeded();
		Locale currentLocale = getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();

		ReportableField date = new ReportableField(FIELD_DATE, String.class);
		date.setLocalizedName(getLocalizedString(FIELD_DATE, "Date"), currentLocale);
		reportCollection.addField(date);

		ReportableField handicap = new ReportableField(FIELD_HANDICAP, String.class);
		handicap.setLocalizedName(getLocalizedString(FIELD_HANDICAP, "Handicap"), currentLocale);
		reportCollection.addField(handicap);

		ReportableField points = new ReportableField(FIELD_POINTS, String.class);
		points.setLocalizedName(getLocalizedString(FIELD_POINTS, "Points"), currentLocale);
		reportCollection.addField(points);

		ReportableField course = new ReportableField(FIELD_COURSE, String.class);
		course.setLocalizedName(getLocalizedString(FIELD_COURSE, "Course"), currentLocale);
		reportCollection.addField(course);
		
		try {
			MemberHome memberHome = (MemberHome) IDOLookup.getHomeLegacy(Member.class);
			Member member = memberHome.findBySSN(personalID);
			
			ScorecardHome home = (ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class);
			Collection scorecards = home.findAllByGolfer(member.getID(), dateFrom, dateTo);
			Iterator iter = scorecards.iterator();
			while (iter.hasNext()) {
				Scorecard scorecard = (Scorecard) iter.next();
				Field field = scorecard.getField();
				IWTimestamp stamp = new IWTimestamp(scorecard.getScorecardDate());
				
				ReportableData data = new ReportableData();

				data.addData(date, stamp.getLocaleDateAndTime(currentLocale, IWTimestamp.SHORT, IWTimestamp.SHORT));
				data.addData(handicap, TextSoap.decimalFormat(scorecard.getHandicapAfter(), 1));
				data.addData(points, String.valueOf(scorecard.getTotalPoints()));
				data.addData(course, field.getName() != null ? field.getName() : getLocalizedString(PREFIX + "correction", "Handicap correction"));

				reportCollection.add(data);
			}
		}
		catch (FinderException fe) {
			log(fe);
		}

		return reportCollection;
	}
}