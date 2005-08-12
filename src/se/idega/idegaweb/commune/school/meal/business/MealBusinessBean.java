/*
 * $Id: MealBusinessBean.java,v 1.3 2005/08/12 19:29:50 gimmi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import is.idega.block.family.business.NoCustodianFound;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.meal.data.MealChoice;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceHome;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceMonth;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceMonthHome;
import se.idega.idegaweb.commune.school.meal.data.MealPrice;
import se.idega.idegaweb.commune.school.meal.data.MealPriceHome;
import se.idega.idegaweb.commune.school.meal.data.MealVacationDay;
import se.idega.idegaweb.commune.school.meal.data.MealVacationDayHome;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/08/12 19:29:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class MealBusinessBean extends CaseBusinessBean implements CaseBusiness , MealBusiness{

	public SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private MessageBusiness getMessageBusiness() {
		try {
			return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private MealChoiceHome getChoiceHome() {
		try {
			return (MealChoiceHome) getIDOHome(MealChoice.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private MealChoiceMonthHome getChoiceMonthHome() {
		try {
			return (MealChoiceMonthHome) getIDOHome(MealChoiceMonth.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private MealPriceHome getPriceHome() {
		try {
			return (MealPriceHome) getIDOHome(MealPrice.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private MealVacationDayHome getVacationDayHome() {
		try {
			return (MealVacationDayHome) getIDOHome(MealVacationDay.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolSeason getOngoingSeason() throws FinderException {
		try {
			return getSchoolBusiness().getSchoolSeasonHome().findCurrentSeason(getSchoolBusiness().getCategoryElementarySchool());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolSeason getNextSeason() throws FinderException {
		try {
			return getSchoolBusiness().getSchoolSeasonHome().findNextSeason(getSchoolBusiness().getCategoryElementarySchool(), new IWTimestamp().getDate());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolClassMember getSchoolPlacing(User user, SchoolSeason season) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchCategoryAndSeason(user, getSchoolBusiness().getCategoryElementarySchool(), season);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return null;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private boolean hasPriceForDate(School school, Date date) {
		try {
			return getPriceHome().getCountBySchoolAndDate(school, date) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}
	
	public MealPrice getMealPrice(School school, Date date) throws FinderException {
		return getPriceHome().findBySchoolAndDate(school, date);
	}
	
	public void storePrices(School school, Date validFrom, Date validTo, float dayPrice, float monthPrice, float milkPrice, float fruitPrice) throws IDOCreateException {
		try {
			if (!hasPriceForDate(school, validFrom) && !hasPriceForDate(school, validTo)) {
				MealPrice price = getPriceHome().create();
				price.setSchool(school);
				price.setValidFrom(validFrom);
				price.setValidTo(validTo);
				price.setMealPricePerDay(dayPrice);
				price.setMealPricePerMonth(monthPrice);
				price.setMilkPrice(milkPrice);
				price.setFruitsPrice(fruitPrice);
				
				price.store();
			}
			else {
				throw new IDOCreateException("Price already exist in the period supplied.");
			}
		}
		catch (CreateException ce) {
			throw new IDOCreateException(ce);
		}
	}
	
	public void storeVacationDays(School school, Date fromDate, String type, int numberOfDays) throws IDOCreateException {
		try {
			IWTimestamp stamp = new IWTimestamp(fromDate);
			
			for (int a = 0; a < numberOfDays; a++) {
				if (a > 0) {
					stamp.addDays(1);
				}
				
				MealVacationDay day = null;
				try {
					day = getVacationDayHome().findBySchoolAndDate(school, stamp.getDate());
				}
				catch (FinderException fe) {
					day = getVacationDayHome().create();
					day.setSchool(school);
					day.setDate(stamp.getDate());
				}
				day.setType(type);
				
				day.store();
			}
		}
		catch (CreateException ce) {
			throw new IDOCreateException(ce);
		}
	}
	
	public float getPriceForMonth(Date month, School school, MonthValues values) {
		try {
			MealPrice price = getPriceHome().findBySchoolAndDate(school, month);
			
			float monthPrice = price.getMealPricePerMonth();
			if (monthPrice < 1) {
				float mealPricePerDay = price.getMealPricePerDay();
				IWCalendar cal = new IWCalendar(month);
				IWTimestamp stamp = new IWTimestamp(1, cal.getMonth(), cal.getYear());
				int monthLength = cal.getLengthOfMonth(cal.getMonth(), cal.getYear());
				for (int i = 1; i <= monthLength; i++) {
					int dow = stamp.getDayOfWeek();
					switch (dow) {
						case Calendar.MONDAY :
							if (values.isMonday()) {
								monthPrice += mealPricePerDay;
							}
							break;
						case Calendar.TUESDAY :
							if (values.isTuesday()) {
								monthPrice += mealPricePerDay;
							}
							break;
						case Calendar.WEDNESDAY :
							if (values.isWednesday()) {
								monthPrice += mealPricePerDay;
							}
							break;
						case Calendar.THURSDAY :
							if (values.isThursday()) {
								monthPrice += mealPricePerDay;
							}
							break;
						case Calendar.FRIDAY :
							if (values.isFriday()) {
								monthPrice += mealPricePerDay;
							}
							break;
					}
					stamp.addDays(1);
				}
			}
			
			if (values.isFruits()) {
				monthPrice += price.getFruitsPrice();
			}
			
			if (values.isMilk()) {
				monthPrice += price.getFruitsPrice();
			}
			return monthPrice;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		// Saekja MealPrice....
		// ath mealprice per day, mealprice per month
		// fr‡ 1 til 31... er virkur dagur... hefur barn m‡lt’? ?ennan dag...
		return 0;
	}
	
	public MealChoice storeChoice(MealChoice choice, User user, School school, SchoolSeason season, String comments, Date[] months, Map monthValues, User performer) throws IDOCreateException {
		try {
			if (choice == null) {
				choice = getChoiceHome().create();
			}
			
			choice.setUser(user);
			choice.setSchool(school);
			choice.setSeason(season);
			choice.setComments(comments);
			choice.setOwner(performer);
			choice.setEmployee(user.equals(performer));
			changeCaseStatus(choice, getCaseStatusOpenString(), performer);
			
			for (int i = 0; i < months.length; i++) {
				Date date = months[i];
				MonthValues values = (MonthValues) monthValues.get(date);
				IWTimestamp month = new IWTimestamp(date);
				
				MealChoiceMonth choiceMonth = null;
				try {
					choiceMonth = getChoiceMonthHome().findByChoice(choice, month.getMonth(), month.getYear());
				}
				catch (FinderException fe) {
					choiceMonth = getChoiceMonthHome().create();
				}
				choiceMonth.setMondays(values.isMonday());
				choiceMonth.setTuesdays(values.isTuesday());
				choiceMonth.setWednesdays(values.isWednesday());
				choiceMonth.setThursdays(values.isThursday());
				choiceMonth.setFridays(values.isFriday());
				choiceMonth.setMilk(values.isMilk());
				choiceMonth.setFruits(values.isFruits());
				choiceMonth.setAmount(values.getAmount());
				
				choiceMonth.store();
			}
			
			String subject = getLocalizedString("choice_sent_subject", "Meal choice sent");
			String body = getLocalizedString("choice_sent_body", "You have made a meal choice to {1} for {0}, {2}.");
			
			sendMessageToParents(choice, subject, body);

			return choice;
		}
		catch (CreateException ce) {
			throw new IDOCreateException(ce);
		}
	}

	public void sendMessageToParents(MealChoice application, String subject, String body) {
		try {
			User user = application.getUser();
			Object[] arguments = { new Name(user.getFirstName(), user.getMiddleName(), user.getLastName()).getName(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), true), application.getSchool().getSchoolName(), PersonalIDFormatter.format(user.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };

			User appParent = application.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(user, appParent)) {
				Message message = getMessageBusiness().createUserMessage(application, appParent, subject, MessageFormat.format(body, arguments), true);
				message.setParentCase(application);
				message.store();
			}
			else {
				try {
					Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(user);
					Iterator iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						if (!parent.equals(appParent)) {
							getMessageBusiness().createUserMessage(application, parent, subject, MessageFormat.format(body, arguments), true);
						}
					}
				}
				catch (NoCustodianFound ncf) {
					ncf.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}
}