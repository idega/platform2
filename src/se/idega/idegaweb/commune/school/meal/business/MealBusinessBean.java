/*
 * $Id: MealBusinessBean.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
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
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
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