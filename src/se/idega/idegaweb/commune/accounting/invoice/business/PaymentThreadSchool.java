package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.BruttoIncomeException;
import se.idega.idegaweb.commune.accounting.regulations.business.LowIncomeException;
import se.idega.idegaweb.commune.accounting.regulations.business.MissingConditionTypeException;
import se.idega.idegaweb.commune.accounting.regulations.business.MissingFlowTypeException;
import se.idega.idegaweb.commune.accounting.regulations.business.MissingRegSpecTypeException;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.TooManyRegulationsException;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.care.data.ProviderType;
import se.idega.idegaweb.commune.care.data.ProviderTypeHome;
import se.idega.idegaweb.commune.care.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMember;
import se.idega.util.ErrorLogger;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Abstract class that holds all the logic that is common for the shool billing
 * 
 * Last modified: $Date: 2004/10/15 10:36:38 $ by $Author: thomas $
 *
 * @author <a href="mailto:joakim@idega.com">Joakim Johnson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.140 $
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public abstract class PaymentThreadSchool extends BillingThread {
	Logger log = Logger.getLogger(this.getClass().getName());
	PaymentHeader paymentHeader;
	protected Provider currentProvider = null;

	//This is a horrible solution... This class should not have to know the
	// localization keys!!!
	private static final String OPPEN_VERKSAMHET = "sch_type.school_type_oppen_verksamhet";
	private static final String FRITIDSKLUBB = "sch_type.school_type_fritidsklubb";
	
	private static final String FRITIDSKLUBB_YEAR_PREFIX = "Fr";
	private static final HashSet validFritidsklubbYears = new HashSet();
	{
		validFritidsklubbYears.add("4");
		validFritidsklubbYears.add("5");
		validFritidsklubbYears.add("6");
		validFritidsklubbYears.add("S4");
		validFritidsklubbYears.add("S5");
		validFritidsklubbYears.add("S6");
	}

	private static final HashSet fritidsklubbYears = new HashSet();
	{
		fritidsklubbYears.add("Fr4");
		fritidsklubbYears.add("Fr5");
		fritidsklubbYears.add("Fr6");
	}

	private static final HashSet validOppenVerksamhet = new HashSet();
	{
		validOppenVerksamhet.add("1");
		validOppenVerksamhet.add("2");
		validOppenVerksamhet.add("3");
		validOppenVerksamhet.add("S1");
		validOppenVerksamhet.add("S2");
		validOppenVerksamhet.add("S3");
		validOppenVerksamhet.add("G1");
		validOppenVerksamhet.add("G2");
		validOppenVerksamhet.add("G3");
	}

	public PaymentThreadSchool(Date month, IWContext iwc) {
		super(month, iwc);
	}
	
	public PaymentThreadSchool(Date month, IWContext iwc, School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}		

	public boolean isInDefaultCommune(User user) throws RemoteException, FinderException {
		Address address = getCommuneUserBusiness().getUsersMainAddress(user);
		Commune commmune = getCommuneHome().findByPrimaryKey(new Integer(address.getCommuneID()));
		return commmune.getIsDefault();
	}

	/**
	 * 
	 *  
	 */
	protected void contracts() throws NotEmptyException {
		School school = getSchool();
		int validSchoolSeasonId = -1;

		try {
			//Set the category parameter to ElementarySchool
			categoryPosting = getCategoryPosting();
			ProviderTypeHome providerTypeHome = (ProviderTypeHome) IDOLookup.getHome(ProviderType.class);
			ProviderType providerType = providerTypeHome.findPrivateType();
			if (hasPlacements()) {
				throw new NotEmptyException(getLocalizedString("invoice.must_first_empty_old_data","Must first empty old data"));
			}
  			validSchoolSeasonId = ((Integer)getSchoolSeasonHome().findCurrentSeason().getPrimaryKey()).intValue();

			int privateType = ((Integer) providerType.getPrimaryKey()).intValue();

			RegulationsBusiness regBus = getRegulationsBusiness();

			//Go through all schools

			if (isTestRun() && school != null){
				contractForProvider(school,	validSchoolSeasonId, privateType, regBus);
			}else{
				Iterator i = getSchools().iterator();
				while(i.hasNext()){
					school = (School) i.next();
					contractForProvider(school,	validSchoolSeasonId, privateType, regBus);
					if(!running){
						return;

					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				errorRelated.append(e);
				createNewErrorMessage(errorRelated, getLocalizedString("invoice.RemoteException","Remote Exception"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.PaymentSchool","Payment School"), getLocalizedString("invoice.RemoteException","Remote Exception"));
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, getLocalizedString("invoice.Severe_CouldNotFindSchoolCategory","Severe. Could not find school category"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.PaymentSchool","Payment School"), getLocalizedString("invoice.Severe_CouldNotFindSchoolCategory","Severe Could not find school category"));
			}
		}
		catch (EJBException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, getLocalizedString("invoice.Severe_CouldNotFindHomeCommune","Severe Could not find  home commune"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.PaymentSchool","Payment School"), getLocalizedString("invoice.Severe_CouldNotFindHomeCommune","Severe Could not find  home commune"));
			}
		}
		catch (IDOException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				errorRelated.append(e);
				createNewErrorMessage(errorRelated, getLocalizedString("invoice.Severe_IDOException","Severe IDO Exception"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.PaymentSchool","Payment School"), getLocalizedString("invoice.Severe_IDOException","Severe IDO Exception"));
			}
		}
	}

	private void contractForProvider(School school,	int validSchoolSeasonId, int privateType, RegulationsBusiness regBus) {
		try {
//			school = getSchoolHome().findByPrimaryKey(new Integer(8));
			errorRelated = new ErrorLogger();
			errorRelated.append(getLocalizedString("invoice.School","School")+":" + school.getName(),1);
			final boolean schoolIsInDefaultCommune;
			final boolean schoolIsPrivate;
			Provider provider = null;
			try{
				provider = new Provider(Integer.parseInt(school.getPrimaryKey().toString()));
				//Only look at those not "payment by invoice"
				//Check if it is private or in Nacka
				errorRelated.append(getLocalizedString("invoice.SchoolCommune","School commune")+":" + school.getCommune().getCommuneName());
				schoolIsInDefaultCommune = school.getCommune().getIsDefault();
				schoolIsPrivate = provider.getProviderTypeId() == privateType;
			}
			catch (NullPointerException e) {
//				errorRelated.append(e);
				throw new SchoolMissingVitalDataException("");
			}
//			errorRelated.logToConsole();
			if ((schoolIsInDefaultCommune || schoolIsPrivate) && !provider.getPaymentByInvoice()) {
				ErrorLogger tmpErrorRelated = new ErrorLogger(errorRelated);
				Collection pupils = getSchoolClassMembers(school);
				Iterator j = pupils.iterator();
//				for (Iterator j = getSchoolClassMembers(school).iterator(); j.hasNext();) {
//				System.out.println("Found " + pupils.size() + " class members");
				for (; j.hasNext();) {
					try {
						errorRelated = new ErrorLogger(tmpErrorRelated);
						SchoolClassMember schoolClassMember = (SchoolClassMember) j.next();
						if(schoolClassMember.getSchoolClass().getSchoolSeasonId() == validSchoolSeasonId){
							createPaymentForSchoolClassMember(regBus, provider, schoolClassMember, 
									schoolIsInDefaultCommune, schoolIsPrivate);
						}
					}
					catch (NullPointerException e) {
						e.printStackTrace();
						errorRelated.append(e);
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.NullPointer","Nullpointer"));
					}
					catch (MissingFlowTypeException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.ErrorFindingFlowType","Error Finding FlowType"));
					}
					catch (MissingConditionTypeException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.ErrorFindingConditionType","Error Finding ConditionType"));
					}
					catch (MissingRegSpecTypeException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.ErrorFindingRegSpecType","Error Finding RegSpecType"));
					}
					catch (TooManyRegulationsException e) {
						e.printStackTrace();
						errorRelated.append(getLocalizedString("invoice.Regulations_found","Regulations found")+":"+e.getRegulationNamesString());
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingTooManyRegulations","Error Finding too many regulations"));
					}
					catch (PostingException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, 
								getLocalizedString("invoice.PostingString","Posting String"));
					}
					catch (RegulationException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.RegulationException","Regulation Exception"));
					}
					catch (RemoteException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.RemoteException","Remote Exception"));
					}
					catch (FinderException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.FinderException","Finder Exception"));
					}
					catch (EJBException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.EJBException","EJB Exception"));
					}
					catch (CreateException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.CreateException","Create Exception"));
					} catch (NotDefaultCommuneException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated, getLocalizedString("invoice.BothStudentAndSchoolOutsideDefaultCommmune","Both student and school outside default commmune"));
					}
				}
			}else{
				log.info("School "+school.getName()+" is not in home commune and not private or gets payment by invoice");
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, getLocalizedString("invoice.DBError_Creating_contracts_for_school","DBError, Creating contracts for school"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.school","School"), getLocalizedString("invoice.DBError_Creating_contracts_for_school","DBError, Creating contracts for school"));
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, getLocalizedString("invoice.CouldNotFindContractForSchool","Could not find contract for school"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.school",""), getLocalizedString("invoice.CouldNotFindContractForSchool","Could not find contract for school"));
			}
		}
		catch (SchoolMissingVitalDataException e) {
			e.printStackTrace();
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.SchoolMissingVitalData","School is missing vital data"));
		}
		catch (NullPointerException e) {
					e.printStackTrace();
/*
					java.io.StringWriter sw = new java.io.StringWriter();
					e.printStackTrace(new java.io.PrintWriter(sw, true));
					errorRelated.append("</br>" + sw + "</br>");
					if (errorRelated.toString().length() > 900)
						errorRelated = new ErrorLogger(errorRelated.toString().substring(1, 900));
*/
					errorRelated.append(e);
					createNewErrorMessage(errorRelated, getLocalizedString("invoice.NullpointerException","Nullpointer exception"));
		}
	}
	
	protected PostingDetail getCheck(RegulationsBusiness regBus, Collection conditions,SchoolClassMember placement) throws RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		return regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(), /*The ID that selects barnomsorg in the regulation */ 
				PaymentFlowConstant.OUT, //The payment flow is out
				calculationDate, //Current date to select the correct date range
				RuleTypeConstant.DERIVED, //The conditiontype
				RegSpecConstant.CHECK, //The ruleSpecType shall be Check
				conditions, //The conditions that need to fulfilled
				0, //Sent in to be used for "Specialutrakning"
				null, //Contract not used here
				placement); //Sent in to be used for e.g. VAT
	}
	

	protected void createPaymentForSchoolClassMember(RegulationsBusiness regBus, 
			Provider provider, SchoolClassMember schoolClassMember, 
			boolean schoolIsInDefaultCommune, boolean schoolIsPrivate)
			throws FinderException, EJBException, PostingException, CreateException, 
			RegulationException, MissingFlowTypeException, MissingConditionTypeException, 
			MissingRegSpecTypeException, TooManyRegulationsException, RemoteException, 
			NotDefaultCommuneException {

		errorRelated.append(getLocalizedString("invoice.SchoolClassMemeber","SchoolClassMemeber")+":"+schoolClassMember.getPrimaryKey());
		if (null != schoolClassMember.getStudent()) {
			errorRelated.append(getLocalizedString("invoice.Student","Student")+":"+schoolClassMember.getStudent().getName()+
					"; "+getLocalizedString("invoice.PersonalID","PersonalID")+":"+schoolClassMember.getStudent().getPersonalID());
		}
//		errorRelated.logToConsole();
		final boolean placementIsInPeriod = isPlacementInPeriod(schoolClassMember);
		final boolean userIsInDefaultCommune = getCommuneUserBusiness().isInDefaultCommune(schoolClassMember.getStudent());
		final boolean placementIsInValidGroup = schoolClassMember.getSchoolClass().getValid();
		final boolean comp_by_agreement = schoolClassMember.getHasCompensationByAgreement();
//		errorRelated.append("Default Commune "+userIsInDefaultCommune);
//		errorRelated.append("Valid group "+placementIsInValidGroup);
//		errorRelated.append("Comp by agreement "+comp_by_agreement);
		if(!schoolIsInDefaultCommune && !userIsInDefaultCommune){
			throw new NotDefaultCommuneException(getLocalizedString("invoice.School","School")+":"+provider.getSchool().getName()+
					"; "+getLocalizedString("invoice.Student","Student")+":"+schoolClassMember.getStudent().getName());
		}
		if (placementIsInValidGroup && placementIsInPeriod
				&& (userIsInDefaultCommune || (schoolIsInDefaultCommune && !schoolIsPrivate))
				&& !comp_by_agreement) {

			ArrayList conditions = getConditions(schoolClassMember, provider);
			School school = schoolClassMember.getSchoolClass().getSchool();
			errorRelated.append(getLocalizedString("invoice.Date","Date")+":" + calculationDate.toString());
			//Get the check
			currentProvider = provider;
			PostingDetail postingDetail = getCheck(regBus, conditions,schoolClassMember); 
			RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
			String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
			PlacementTimes placementTimes = getPlacementTimes(schoolClassMember);
			final PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
			createVATPaymentRecord(record,postingDetail,placementTimes.getMonths(), school,schoolClassMember.getSchoolType(),schoolClassMember.getSchoolYear());
			createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes);
			SchoolType schoolType = null;
			//Find the oppen verksamhet and fritidsklubb
			try {
				String schoolYearName = schoolClassMember.getSchoolYear().getName();
//				int len = schoolYearName.length();
//				int schoolYearInt = Integer.parseInt(schoolYearName.substring(len-1,len));
//				int schoolYear = Integer.parseInt(schoolYearName);
				
				if (validOppenVerksamhet.contains(schoolYearName)) {
					for (Iterator i = getSchoolTypes(schoolClassMember).iterator(); i.hasNext();) {
						schoolType = (SchoolType) i.next();
						if (schoolType.getLocalizationKey().equalsIgnoreCase(OPPEN_VERKSAMHET)) {
							createPaymentsForOppenVerksamhet(regBus, provider, schoolClassMember, conditions, placementTimes, schoolType);
							break;
						}
					}
				}
				else if (validFritidsklubbYears.contains(schoolYearName)) {
					for (Iterator i = getSchoolTypes(schoolClassMember).iterator(); i.hasNext();) {
						schoolType = (SchoolType) i.next();
						if (schoolType.getLocalizationKey().equalsIgnoreCase(FRITIDSKLUBB)) {
							createPaymentsForFritidsklubb(regBus, provider, schoolClassMember, conditions, placementTimes, schoolType);
							break;
						}
					}
				}
			} catch (IDORelationshipException e) {
				e.printStackTrace();
				if (errorRelated != null) {
					createNewErrorMessage(errorRelated, "invoice.DBRelationshipError");
				}
				else {
					createNewErrorMessage(createRelatedString(category, schoolClassMember.getSchoolType(), school, schoolClassMember.getStudent()), "invoice.DBRelationshipError");
				}
			}
			//Get all the resources for the child
			Collection resources = getResourceBusiness().getResourcePlacementsByMemberId((Integer) schoolClassMember.getPrimaryKey());
// 			errorRelated.append("Found " + resources.size() + " resources");
			ErrorLogger tmpErrorRelated = new ErrorLogger(errorRelated);
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ResourceClassMember resource = (ResourceClassMember) i.next();
				errorRelated = new ErrorLogger(tmpErrorRelated);
				try {
					createPaymentsForResource(regBus, provider, schoolClassMember, conditions, resource);
				}
				catch (NullPointerException e) {
					e.printStackTrace();
//					errorRelated = new ErrorLogger();
					User user = null != schoolClassMember ? schoolClassMember.getStudent() : null;
					String studentInfo = null != user ? user.getName() + " " + user.getPersonalID() : "";
					errorRelated.append(getLocalizedString("invoice.Student","Student")+":" + studentInfo);
					errorRelated.append(getLocalizedString("invoice.Conditions","Conditions")+":"+ conditions);
					errorRelated.append(getLocalizedString("invoice.Resource","Resource")+":" + resource);
//					java.io.StringWriter sw = new java.io.StringWriter();
//					e.printStackTrace(new java.io.PrintWriter(sw, true));
					errorRelated.append(e);
//					if (errorRelated.toString().length() > 900)
//						errorRelated = new StringBuffer(errorRelated.substring(1, 900));
					createNewErrorMessage(errorRelated, getLocalizedString("invoice.createPaymentsForResourceError","Create payments for resource error"));
				}
			}
		}
	}

	private PlacementTimes getPlacementTimes(SchoolClassMember schoolClassMember) {
		Date sDate = null;
		Date eDate = null;
		if (schoolClassMember.getRegisterDate() != null) {
			sDate = new Date(schoolClassMember.getRegisterDate().getTime());
		}
		if (schoolClassMember.getRemovedDate() != null) {
			eDate = new Date(schoolClassMember.getRemovedDate().getTime());
		}
		PlacementTimes placementTimes = calculateTime(sDate, eDate);
		return placementTimes;
	}

	private boolean isPlacementInPeriod(SchoolClassMember schoolClassMember) {
		IWTimestamp placementStart = null;
		IWTimestamp placementEnd = null;
		if (schoolClassMember.getRegisterDate() != null) {
			placementStart = new IWTimestamp(schoolClassMember.getRegisterDate().getTime());
		}
		if (schoolClassMember.getRemovedDate() != null) {
			placementEnd = new IWTimestamp(schoolClassMember.getRemovedDate().getTime());
		}
		IWTimestamp periodStart = new IWTimestamp(startPeriod);
		startPeriod.setAsDate();
		IWTimestamp periodEnd = new IWTimestamp(endPeriod);
		endPeriod.setAsDate();
		final boolean placementIsInPeriod = !placementStart.isLaterThan(periodEnd) && (null == placementEnd || !periodStart.isLaterThan(placementEnd));
		return placementIsInPeriod;
	}

	protected Collection getRegulationForResourceArray(RegulationsBusiness regBus, SchoolClassMember schoolClassMember, ResourceClassMember resource, Provider provider) throws RemoteException {
		Collection resourceConditions = new ArrayList();
		//Just a safety precation to trace down null pointer error
		if(null==regBus || null==schoolClassMember || null==resource || null == provider){
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.NullpointerInCallToGetRegulationForResourceArray","Nullpointer in call to get regulation for resource array"));
			return resourceConditions;
		}
		if(null!=schoolClassMember.getSchoolType()){
		}else{
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.NullpointerInSchoolType","Nullpointer in schooltype"));
			return resourceConditions;
		}
		if(null!=resource.getResource()){
			errorRelated.append(getLocalizedString("invoice.Resource","Resource")+":"+resource.getResource().getResourceName());
		}else{
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.NullpointerInResource","Nullpointer in resource"));
			return resourceConditions;
		}
		if(null==schoolClassMember.getSchoolYear()){
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.NullpointerInSchoolYear","Nullpointer in schoolyear"));
			return resourceConditions;
		}

		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_RESOURCE, resource.getResource().getResourceName()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));
		Collection regulationForResourceArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
				category.getCategory(),
				PaymentFlowConstant.OUT,
				calculationDate,
				RuleTypeConstant.DERIVED,
				RegSpecConstant.RESOURCE, resourceConditions
		);
		
		return regulationForResourceArray;
	}
	
	private void createPaymentsForResource(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, ResourceClassMember resource) throws EJBException, FinderException, PostingException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		final Date startDate = resource.getStartDate();
		final Date endDate = resource.getEndDate();
		final PlacementTimes placementTimes = calculateTime(startDate, endDate, false);
		if(placementTimes.getDays() > 0){
			School school = schoolClassMember.getSchoolClass().getSchool();
			
			Collection regulationForResourceArray = getRegulationForResourceArray(regBus, schoolClassMember, resource, provider);
	//		int regSize = regulationForResourceArray.size();
	
	//		errorRelated.append("# of Regulations "+regSize);
	
			for (Iterator i = regulationForResourceArray.iterator(); i.hasNext();) {
				try {
					Regulation regulation = (Regulation) i.next();
					errorRelated.append(getLocalizedString("invoice.Regulation","Regulation")+":"+regulation.getName());
					PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, calculationDate, conditions, placementTimes);
					RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
					String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
					PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
					createVATPaymentRecord(record, postingDetail,placementTimes.getMonths(),school,schoolClassMember.getSchoolType(),schoolClassMember.getSchoolYear());
					createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes, startDate, endDate);
				}
				catch (BruttoIncomeException e) {
					//Who cares!!!
				}
				catch (LowIncomeException e) {
	
				}
			}
		}
	}

	private void createPaymentsForFritidsklubb(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, PlacementTimes placementTimes, SchoolType schoolType) throws PostingException, EJBException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException, FinderException, IDORelationshipException {
		School school = schoolClassMember.getSchoolClass().getSchool();

		//Try to figure out the schoolyear stuff for fritidsklubb. Not sure this is right...
		String schoolYearName = schoolClassMember.getSchoolYear().getName();
		int len = schoolYearName.length();
		String schoolYearCM = schoolYearName.substring(len-1,len);
//		System.out.println("SchoolYear:"+schoolYearName+"  or "+schoolYearCM);
		
		Iterator yearIter = school.findRelatedSchoolYears().iterator();
		//check if school has a fritidsklubb year same as the school year for the placement
		boolean yearFound = false;
		while(yearIter.hasNext()){
			SchoolYear schoolYear = (SchoolYear) yearIter.next();
//			System.out.println("SchoolYear to test if it is fritidsklub year:"+schoolYearName+"  or "+schoolYearCM);
			if(fritidsklubbYears.contains(schoolYear.getName())){
				schoolYearName = schoolYear.getName();
				len = schoolYearName.length();
				String schoolYearS = schoolYearName.substring(len-1,len);
				if(schoolYearCM.equalsIgnoreCase(schoolYearS)){
					yearFound = true;
					break;
				}
			}
		}
		if(!yearFound){
			return;
		}

		ArrayList oppenConditions = new ArrayList();
		oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, FRITIDSKLUBB));
		Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(),
			PaymentFlowConstant.OUT,
			calculationDate,
			RuleTypeConstant.DERIVED,
			null, oppenConditions
		);
		if(regulationForTypeArray.size()!=1){
			errorRelated.append(getLocalizedString("invoice.Number_of_regulations_found_for_fritidsklubb","Number of regulations found for fritidsklubb")+":"+regulationForTypeArray.size());
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.Number_of_regulations_for_fritidsklubb_not_correct","Number of regulations for fritidsklubb not correct"));
		}
		for (Iterator i = regulationForTypeArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, calculationDate, conditions, placementTimes);
//				RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				RegulationSpecType regSpecType = regulation.getRegSpecType();
				errorRelated.append("RegSpecType from regulation "+regulation.getRegSpecType(),1);
				errorRelated.append("RegSpecType from posting detail"+(getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType())).getLocalizationKey(),1);
				if(!regulation.getRegSpecType().getLocalizationKey().equalsIgnoreCase(
						(getRegulationSpecTypeHome().findByRegulationSpecType(
								postingDetail.getRuleSpecType())).getLocalizationKey())){
					createNewErrorMessage(errorRelated, 
							getLocalizedString("invoice.WarningConflictingRegSpecTypesGivenForFritidsKlubb","Warning: Conflicting RegSpecTypes given for fritidsklubb"));
				}
				
				schoolYearName = schoolClassMember.getSchoolYear().getName();
				len = schoolYearName.length();
				try {
//					errorRelated.append("Schoolyear:" + schoolYearName);
					int schoolYearInt = Integer.parseInt(schoolYearName.substring(len-1,len));
					SchoolYear schoolYear = ((SchoolYearHome) IDOLookup.getHome(SchoolYear.class)).
							findByYearName(FRITIDSKLUBB_YEAR_PREFIX + schoolYearInt);
					errorRelated.append(getLocalizedString("invoice.FritidsklubbSchoolyear","Fritidsklubb schoolyear")+":" + FRITIDSKLUBB_YEAR_PREFIX + schoolYearInt);
					
					String[] postings =  getPostingStrings(category, schoolType, 
							((Integer) regSpecType.getPrimaryKey()).intValue(), provider, calculationDate, 
							((Integer)schoolYear.getPrimaryKey()).intValue(), schoolClassMember.getStudyPathId());
				
					PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], 
							placementTimes.getMonths(), school);
//					errorRelated.append("created payment info for fritidsklubb:" + schoolClassMember.getStudent().getName());
//					createVATPaymentRecord(record,postingDetail,placementTimes.getMonths(),school,schoolClassMember.getSchoolType(),schoolClassMember.getSchoolYear());
					createVATPaymentRecord(record,postingDetail,placementTimes.getMonths(),school,schoolType,schoolYear);
					createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes);
				} catch (FinderException e1) {
					e1.printStackTrace();
					createNewErrorMessage(errorRelated, 
							getLocalizedString("invoice.CouldNotFindSchoolYearForFritidsklubb","Could not find schoolyear for fritidsklubb"));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
					createNewErrorMessage(errorRelated, 
							getLocalizedString("invoice.CouldNotParseSchoolYearForFritidsklubb","Could not parse schoolyear for fritidsklubb"));
				}
			}
			catch (BruttoIncomeException e) {
				//Who cares!!!
			}
			catch (LowIncomeException e) {

			}
		}
	}

	private void createPaymentsForOppenVerksamhet(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, PlacementTimes placementTimes, SchoolType schoolType) throws FinderException, PostingException, EJBException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		ArrayList oppenConditions = new ArrayList();
		School school = schoolClassMember.getSchoolClass().getSchool();

		oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, OPPEN_VERKSAMHET));
		Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(),
			PaymentFlowConstant.OUT, //The payment flow is out
			calculationDate, //Current date to select the correct date range
			RuleTypeConstant.DERIVED, //The conditiontype
			null, oppenConditions //The conditions that need to fulfilled
		);
		if(regulationForTypeArray.size()!=1){
			errorRelated.append(getLocalizedString("invoice.Number_of_regulations_found_for_oppen_verksamhet","Number of regulations found for oppen verksamhet")+":"+regulationForTypeArray.size());
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.NumberOfRegulationsForOppenVerksamhetNotCorrect","Number of regulations for oppen verksamhet not correct"));
		}
		for (Iterator i = regulationForTypeArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, calculationDate, conditions,placementTimes);
				RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				String[] postings =  getPostingStrings(category, schoolType, ((Integer) regSpecType.getPrimaryKey()).intValue(), provider, calculationDate, ((Integer) schoolClassMember.getSchoolYear().getPrimaryKey()).intValue(), schoolClassMember.getStudyPathId());

				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				createVATPaymentRecord(record, postingDetail,placementTimes.getMonths(),school,schoolClassMember.getSchoolType(),schoolClassMember.getSchoolYear());
//				errorRelated.append("created payment info for Oppen verksamhet:" + schoolClassMember.getStudent().getName());
				createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes);
			}
			catch (BruttoIncomeException e) {
				//Who cares!!!
			}
			catch (LowIncomeException e) {

			}
		}
	}

	/**
	 * Used to create a description of every entity that is related
	 * 
	 * @param cat
	 * @param type
	 * @param sch
	 * @param child
	 * @return
	 */
	private String createRelatedString(SchoolCategory cat, SchoolType type, School sch, User child) {
		StringBuffer ret = new StringBuffer();
		if (cat != null) {
			ret.append(cat.getCategory());
		}
		if (type != null) {
			if (ret.length() != 0) {
				ret.append("<br>");
			}
			ret.append(type.getName());
		}
		if (sch != null) {
			if (ret.length() != 0) {
				ret.append("<br>");
			}
			ret.append(sch.getName());
		}
		if (child != null) {
			if (ret.length() != 0) {
				ret.append("<br>");
			}
			ret.append(child.getName());
		}
		return ret.toString();
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and
	 * payment records for the Regular payments
	 */
	protected void regularPayment() {
		PostingDetail postingDetail = null;
		PlacementTimes placementTimes = null;
		Iterator regularPaymentIter = null;
		School school = getSchool();

		try {
			if (isTestRun() && school != null){
				regularPaymentIter = getRegularPaymentBusiness().findRegularPaymentsForPeriodeAndSchool(startPeriod.getDate(), endPeriod.getDate(), school).iterator();
			}else{
				regularPaymentIter = getRegularPaymentBusiness().findRegularPaymentsForPeriodeAndCategory(startPeriod.getDate(), category).iterator();
			}
			School regPaymentSchool;

			//Go through all the regular payments
			while (regularPaymentIter.hasNext()) {
				RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry) regularPaymentIter.next();
				errorRelated = new ErrorLogger(getLocalizedString("invoice.RegularPaymentEntryID","RegularPaymentEntry ID")+":" + regularPaymentEntry.getPrimaryKey());
				errorRelated.append(getLocalizedString("invoice.Placing","Placing")+":" + regularPaymentEntry.getPlacing());
				errorRelated.append(getLocalizedString("invoice.Amount","Amount")+":" + regularPaymentEntry.getAmount());
				errorRelated.append(getLocalizedString("invoice.School","School")+":" + regularPaymentEntry.getSchool());
				postingDetail = new PostingDetail(regularPaymentEntry);
				regPaymentSchool = regularPaymentEntry.getSchool();
				placementTimes = calculateTime(regularPaymentEntry.getFrom(), regularPaymentEntry.getTo());
				try {
					PaymentRecord paymentRecord = createPaymentRecord(postingDetail, regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(), regPaymentSchool, regularPaymentEntry.getNote());
					createVATPaymentRecord(paymentRecord,postingDetail,placementTimes.getMonths(),regPaymentSchool,regularPaymentEntry.getSchoolType(),null);
					User classMember = regularPaymentEntry.getUser();
					if (classMember != null){
						try{
							SchoolClassMember schoolClassMember = getSchoolClassMemberHome().findLatestByUser(classMember);
							createInvoiceRecord(paymentRecord, schoolClassMember, postingDetail, placementTimes);					
						}catch (FinderException e) {
							createNewErrorMessage(errorRelated, getLocalizedString("invoice.schoolClassMemberNotFound","The regular invoice pointed to a user, but the accordingly school class member was not found."));
						}
					}
				}
				catch (IDOLookupException e) {
					createNewErrorMessage(errorRelated, getLocalizedString("invoice.IDOLookup","IDOLookup"));
					e.printStackTrace();
				}
				catch (CreateException e) {
					createNewErrorMessage(errorRelated, getLocalizedString("invoice.Create","Create"));
					e.printStackTrace();
				}
				if(!running){
					return;
				}
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
			if (postingDetail != null) {
				createNewErrorMessage(postingDetail.getTerm(), "payment.DBSetupProblem");
			}
			else {
				createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			}
		}
		catch (IDOLookupException e) {
			createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			e.printStackTrace();
		}
		catch (RemoteException e) {
			createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			e.printStackTrace();
		}
		

	}

	private ArrayList getConditions(SchoolClassMember schoolClassMember, Provider provider) {
		ArrayList conditions = new ArrayList();
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STADSBIDRAG, new Boolean(provider.getStateSubsidyGrant())));
		if(null!=schoolClassMember){
//			errorRelated.append("SchoolType:" + schoolClassMember.getSchoolType().getName());
			errorRelated.append("School Year:" + schoolClassMember.getSchoolYear().getName());
		}
		if(null!=provider){
			errorRelated.append("StateSubsidyGrant:" + provider.getStateSubsidyGrant());
		}
		setStudyPath(schoolClassMember, conditions);
		return conditions;
	}
	
/*
	//Temporary code to meassure the performance of the batchrun
	long start, stop, time;
	private void timerStart() {
		start = System.currentTimeMillis();
		stop = start;
	}

	private void dispTime(String s) {
		long t = System.currentTimeMillis();
		long tt;
		tt = t - start;
		time = t - stop;
		log.info(s + "  total time:" + (tt / 1000f) + "  from last stop, time:" + (time / 1000f));
		stop = t;
	}
*/
	/**
	 * Do nothing
	 */
	protected void setStudyPath(SchoolClassMember schoolClassMember, ArrayList conditions){
	}
	
	private ExportDataMapping getCategoryPosting() throws FinderException, IDOLookupException, EJBException {
		return (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(category.getPrimaryKey());
	}

	private Collection getSchools() throws IDOLookupException, EJBException, FinderException, RemoteException {
		return getSchoolHome().findAllByCategory(category);
	}

	private Collection getSchoolClassMembers(School school) throws FinderException, RemoteException, EJBException {
//		Object o = school.getPrimaryKey();
//		Class c = o.getClass();
//		Integer i = (Integer) o;
		int i = Integer.parseInt(school.getPrimaryKey().toString());
		return getSchoolClassMemberHome().findBySchool(i, -1, category.getCategory(), calculationDate);
	}

	private Collection getSchoolTypes(SchoolClassMember schoolClassMember) throws IDORelationshipException {
		return schoolClassMember.getSchoolClass().getSchool().getSchoolTypes();
	}

	protected String[] getPostingStrings(Provider provider, SchoolClassMember schoolClassMember, RegulationSpecType regSpecType) throws PostingException, RemoteException, EJBException {
		int id = schoolClassMember.getStudyPathId();
		return getPostingStrings(category, schoolClassMember.getSchoolType(), ((Integer) regSpecType.getPrimaryKey()).intValue(), provider, calculationDate, ((Integer) schoolClassMember.getSchoolYear().getPrimaryKey()).intValue(), id);
	}
	
	protected String[] getPostingStrings(SchoolCategory category, SchoolType schoolType, int regSpecTypeId, Provider provider, Date calculationDate, int schoolYearId, int studyPathId) throws PostingException, RemoteException {
		if (studyPathId == -1)
			studyPathId = -1;
		return getPostingBusiness().getPostingStrings(category, schoolType, regSpecTypeId, provider, calculationDate,schoolYearId, -1, false);		
	}

	protected CommuneHome getCommuneHome() throws RemoteException {
		return (CommuneHome) IDOLookup.getHome(Commune.class);
	}

	protected Collection getRegularPayments() throws RemoteException {
		return getRegularPaymentBusiness().findRegularPaymentsForPeriode(startPeriod.getDate(), endPeriod.getDate());
	}

	private SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}

	protected SchoolTypeHome getSchoolTypeHome() throws RemoteException {
		return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
	}

	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}

	private ResourceBusiness getResourceBusiness() throws RemoteException {
		return (ResourceBusiness) IBOLookup.getServiceInstance(iwc, ResourceBusiness.class);
	}

	private SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	private SchoolSeasonHome getSchoolSeasonHome() throws RemoteException {
		return (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
	}

	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	protected ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}
}
