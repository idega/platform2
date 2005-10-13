/*
 * $Id: MealBusinessBean.java,v 1.7 2005/10/13 18:36:11 laddi Exp $
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import se.idega.idegaweb.commune.school.meal.data.MealChoice;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceHome;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceMonth;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceMonthHome;
import se.idega.idegaweb.commune.school.meal.data.MealPrice;
import se.idega.idegaweb.commune.school.meal.data.MealPriceHome;
import se.idega.idegaweb.commune.school.meal.data.MealVacationDay;
import se.idega.idegaweb.commune.school.meal.data.MealVacationDayHome;
import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryBMPBean;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.message.data.Message;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/10/13 18:36:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
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

	private CommuneMessageBusiness getMessageBusiness() {
		try {
			return (CommuneMessageBusiness) this.getServiceInstance(CommuneMessageBusiness.class);
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
	
	public Collection getVacationDays(School school) {
		try {
			return getVacationDayHome().findAllBySchool(school);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	public MealVacationDay getVacationDay(Object vacationDayPK) throws FinderException {
		return getVacationDayHome().findByPrimaryKey(vacationDayPK);
	}
	
	public void deleteVacationDay(Object vacationDayPK) throws RemoveException {
		try {
			MealVacationDay vacationDay = getVacationDay(vacationDayPK);
			vacationDay.remove();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
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
	
	public boolean hasChoiceForDate(User user, School school, SchoolSeason season, Date date) {
		IWTimestamp stamp = new IWTimestamp(date);
		try {
			return getChoiceMonthHome().getNumberOfChoicesForUser(user, school, season, stamp.getMonth(), stamp.getYear()) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}
	
	public MealPrice getMealPrice(Object pricePK) throws FinderException {
		return getPriceHome().findByPrimaryKey(pricePK);
	}
	
	public void deleteMealPrice(Object pricePK) throws RemoveException {
		try {
			MealPrice price = getMealPrice(pricePK);
			price.remove();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}
	
	public MealPrice getMealPrice(School school, Date date) throws FinderException {
		return getPriceHome().findBySchoolAndDate(school, date);
	}
	
	public void storePrices(Object pricePK, School school, Date validFrom, Date validTo, float dayPrice, float monthPrice, float milkPrice, float fruitPrice) throws IDOCreateException {
		try {
			if ((!hasPriceForDate(school, validFrom) && !hasPriceForDate(school, validTo)) || pricePK != null) {
				MealPrice price = null;
				if (pricePK != null) {
					price = getPriceHome().findByPrimaryKey(pricePK);
				}
				else {
					price = getPriceHome().create();
				}

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
		catch (FinderException fe) {
			fe.printStackTrace();
			throw new IDOCreateException(fe);
		}
		catch (CreateException ce) {
			throw new IDOCreateException(ce);
		}
	}
	
	public void storeVacationDays(Object vacationDayPK, School school, Date fromDate, Date toDate, String type, String name) throws IDOCreateException {
		try {
			MealVacationDay day = null;
			try {
				day = getVacationDayHome().findByPrimaryKey(vacationDayPK);
			}
			catch (FinderException fe) {
				day = getVacationDayHome().create();
				day.setSchool(school);
			}
			day.setValidFrom(fromDate);
			day.setValidTo(toDate);
			day.setType(type);
			day.setName(name);
			
			day.store();
		}
		catch (CreateException ce) {
			throw new IDOCreateException(ce);
		}
	}
	
	public Collection getSchoolPrices(School school) {
		try {
			return getPriceHome().findAllBySchool(school);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	public Collection getChoicesByClaimStatus(School school) {
		try {
			String[] statuses = { AccountEntryBMPBean.statusCreated, AccountEntryBMPBean.statusBilled };
			return getChoiceHome().findAllBySchoolAndClaimStatus(school, statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	public Collection getSchoolDiners(School school, Date date, Boolean showEmployees) {
		try {
			IWTimestamp stamp = new IWTimestamp(date);
			return getChoiceMonthHome().findAllBySchool(school, stamp.getMonth(), stamp.getYear(), showEmployees);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	private boolean isHoliday(Map holidays, Date date, boolean isEmployee) {
		if (holidays.containsKey(date)) {
			MealVacationDay day = (MealVacationDay) holidays.get(date);
			if (day.getType().equals(MealConstants.TYPE_TEACHER_WORK_DAY) && isEmployee) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public MonthValues calculatePrices(Date month, School school, MonthValues values, boolean isEmployee) throws FinderException {
		MealPrice price = getPriceHome().findBySchoolAndDate(school, month);
		
		float monthPrice = price.getMealPricePerMonth();
		if (monthPrice < 1) {
			float mealPricePerDay = price.getMealPricePerDay();
			Map holidays = getHolidaysForMonth(school, month);
			
			IWCalendar cal = new IWCalendar(month);
			IWTimestamp stamp = new IWTimestamp(1, cal.getMonth(), cal.getYear());
			stamp.setAsTime();
			
			int monthLength = cal.getLengthOfMonth(cal.getMonth(), cal.getYear());
			for (int i = 1; i <= monthLength; i++) {
				int dayOfWeek = stamp.getDayOfWeek();
				if (!isHoliday(holidays, stamp.getDate(), isEmployee)) {
					switch (dayOfWeek) {
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
				}
				stamp.addDays(1);
			}
		}
		values.setMealAmount(monthPrice);
		
		if (values.isFruits()) {
			values.setFruitAmount(price.getFruitsPrice());
		}
		
		if (values.isMilk()) {
			values.setMilkAmount(price.getMilkPrice());
		}

		return values;
	}
	
	private Map getHolidaysForMonth(School school, Date month) {
		Map holidays = new HashMap();
		
		IWCalendar calendar = new IWCalendar();
		IWTimestamp from = new IWTimestamp(month);
		from.setDay(1);
		IWTimestamp to = new IWTimestamp(month);
		to.setDay(calendar.getLengthOfMonth(to.getMonth(), to.getYear()));
		
		try {
			Collection vacationDays = getVacationDayHome().findAllBySchoolAndPeriod(school, from.getDate(), to.getDate());
			Iterator iter = vacationDays.iterator();
			while (iter.hasNext()) {
				MealVacationDay vacationDay = (MealVacationDay) iter.next();
				from = new IWTimestamp(vacationDay.getValidFrom());
				to = new IWTimestamp(vacationDay.getValidTo());
				
				while(from.isEarlierThan(to) || from.isEqualTo(to)) {
					holidays.put(from.getDate(), vacationDay);
					from.addDays(1);
				}
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
		return holidays;
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
			
			float totalAmount = 0;
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
				choiceMonth.setChoice(choice);
				choiceMonth.setMonth(month.getMonth());
				choiceMonth.setYear(month.getYear());
				choiceMonth.setMondays(values.isMonday());
				choiceMonth.setTuesdays(values.isTuesday());
				choiceMonth.setWednesdays(values.isWednesday());
				choiceMonth.setThursdays(values.isThursday());
				choiceMonth.setFridays(values.isFriday());
				choiceMonth.setMilk(values.isMilk());
				choiceMonth.setFruits(values.isFruits());
				choiceMonth.setAmount(values.getAmount());
				totalAmount += values.getAmount();
				
				choiceMonth.store();
			}
			
			try {
				AccountEntry entry = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).create();
				entry.setUserId(((Integer) user.getPrimaryKey()).intValue());
				entry.setTotal(totalAmount);
				entry.setStatus(AccountEntryBMPBean.statusCreated);
				entry.store();
				
				choice.setAccountEntry(entry);
				choice.store();
			}
			catch (IDOLookupException ile) {
				ile.printStackTrace();
			}
			
			if (!user.equals(performer)) {
				String subject = getLocalizedString("choice_sent_subject", "Meal choice sent");
				String body = getLocalizedString("choice_sent_body", "You have made a meal choice to {1} for {0}, {2}.");
				
				sendMessageToParents(choice, subject, body);
			}

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