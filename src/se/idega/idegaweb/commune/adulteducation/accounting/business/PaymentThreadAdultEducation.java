package se.idega.idegaweb.commune.adulteducation.accounting.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.business.BillingThread;
import se.idega.idegaweb.commune.accounting.invoice.business.PlacementTimes;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
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
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParameters;
import se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersHome;
import se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.util.ErrorLogger;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberGrade;
import com.idega.block.school.data.SchoolClassMemberGradeHome;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

public abstract class PaymentThreadAdultEducation extends BillingThread {

	protected BatchParameters parameters = null;

	protected SchoolType schoolType = null;

	protected IWTimestamp periodFrom = null;

	protected IWTimestamp periodTo = null;

	private static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.adulteducation";

	public PaymentThreadAdultEducation(Date month, IWContext iwc) {
		super(month, iwc);
	}

	public PaymentThreadAdultEducation(Date month, IWContext iwc,
			School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}

	protected void setStudyPath(SchoolClassMember schoolClassMember,
			ArrayList conditions) {
	}

	public IWBundle getBundle() {
		return iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
	}

	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	// Common stuff for the batches

	protected void placements() {
		RegulationsBusiness regBus = null;
		Collection placements = null;
		try {
			regBus = getRegulationsBusiness();
			placements = getPlacements();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		// Go through all placements
		Iterator i = placements.iterator();
		while (i.hasNext()) {
			SchoolClassMember member = (SchoolClassMember) i.next();
			Provider provider = null;
			try {
				provider = new Provider(Integer.parseInt(member
						.getSchoolClass().getSchool().getPrimaryKey()
						.toString()));
				createPaymentForSchoolClassMember(regBus, provider, member);
			} catch (NullPointerException e) {
				// throw new SchoolMissingVitalDataException("");
				e.printStackTrace();
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (PostingException e) {
				e.printStackTrace();
			} catch (RegulationException e) {
				e.printStackTrace();
			} catch (MissingFlowTypeException e) {
				e.printStackTrace();
			} catch (MissingConditionTypeException e) {
				e.printStackTrace();
			} catch (MissingRegSpecTypeException e) {
				e.printStackTrace();
			} catch (TooManyRegulationsException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}

			if (!running) {
				return;

			}
		}

		/*
		 * catch (RemoteException e) { e.printStackTrace(); if (errorRelated !=
		 * null) { errorRelated.append(e); createNewErrorMessage(errorRelated,
		 * getLocalizedString( "invoice.RemoteException", "Remote Exception")); }
		 * else { createNewErrorMessage(getLocalizedString(
		 * "invoice.PaymentSchool", "Payment School"),
		 * getLocalizedString("invoice.RemoteException", "Remote Exception")); } }
		 * catch (FinderException e) { e.printStackTrace(); if (errorRelated !=
		 * null) { createNewErrorMessage(errorRelated, getLocalizedString(
		 * "invoice.Severe_CouldNotFindSchoolCategory", "Severe. Could not find
		 * school category")); } else {
		 * createNewErrorMessage(getLocalizedString( "invoice.PaymentSchool",
		 * "Payment School"), getLocalizedString(
		 * "invoice.Severe_CouldNotFindSchoolCategory", "Severe Could not find
		 * school category")); } } catch (EJBException e) { e.printStackTrace();
		 * if (errorRelated != null) { createNewErrorMessage(errorRelated,
		 * getLocalizedString( "invoice.Severe_CouldNotFindHomeCommune", "Severe
		 * Could not find home commune")); } else {
		 * createNewErrorMessage(getLocalizedString( "invoice.PaymentSchool",
		 * "Payment School"), getLocalizedString(
		 * "invoice.Severe_CouldNotFindHomeCommune", "Severe Could not find home
		 * commune")); } } catch (IDOException e) { e.printStackTrace(); if
		 * (errorRelated != null) { errorRelated.append(e);
		 * createNewErrorMessage(errorRelated, getLocalizedString(
		 * "invoice.Severe_IDOException", "Severe IDO Exception")); } else {
		 * createNewErrorMessage(getLocalizedString( "invoice.PaymentSchool",
		 * "Payment School"), getLocalizedString("invoice.Severe_IDOException",
		 * "Severe IDO Exception")); } }
		 */
	}

	protected Collection getPlacements() throws RemoteException,
			FinderException {
		IWTimestamp theDayAfterPeriodTo = new IWTimestamp(periodTo);
		theDayAfterPeriodTo.addDays(1);
		return getSchoolClassMemberHome()
				.findPlacementsBySchoolTypeAndRegisterDateAndGradeInPeriod(
						schoolType, periodFrom, theDayAfterPeriodTo);
	}

	protected void createPaymentForSchoolClassMember(
			RegulationsBusiness regBus, Provider provider,
			SchoolClassMember member) throws FinderException, EJBException,
			PostingException, CreateException, RegulationException,
			MissingFlowTypeException, MissingConditionTypeException,
			MissingRegSpecTypeException, TooManyRegulationsException,
			RemoteException {

		errorRelated = new ErrorLogger();
		errorRelated.append(getLocalizedString("adult.SchoolClassMember",
				"SchoolClassMember")
				+ ":" + member.getPrimaryKey());
		if (null != member.getStudent()) {
			errorRelated.append(getLocalizedString("adult.Student", "Student")
					+ ":" + member.getStudent().getName() + "; "
					+ getLocalizedString("adult.PersonalID", "PersonalID")
					+ ":" + member.getStudent().getPersonalID());
		}

		// final boolean placementIsInValidGroup =
		// schoolClassMember.getSchoolClass().getValid();
		// final boolean comp_by_agreement =
		// schoolClassMember.getHasCompensationByAgreement();

		// if (placementIsInValidGroup && !comp_by_agreement) {

		AdultEducationCourse course = null;
		SchoolStudyPath path = null;
		SchoolClass group = member.getSchoolClass();
		if (group.getCode() != null && !"".equals(group.getCode())) {
			try {
				course = getAdultEducationBusiness().getCourse(
						group.getSchoolSeason().getPrimaryKey(),
						group.getCode());
				path = course.getStudyPath();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		ArrayList conditions = getConditions(member, path);
		School school = member.getSchoolClass().getSchool();
		errorRelated.append(getLocalizedString("adult.Date", "Date") + ":"
				+ calculationDate.toString());
		// Get the check
		PostingDetail postingDetail = getCheck(regBus, conditions, member);
		System.out.println("check = " + postingDetail.toString());
		RegulationSpecType regSpecType = getRegulationSpecTypeHome()
				.findByRegulationSpecType(postingDetail.getRuleSpecType());
		System.out.println("regSpecType = " + regSpecType.toString());
		String[] postings = getPostingStrings(provider, member, regSpecType);
		System.out.println("postings = " + postings.toString());
		System.out.println("Member = " + member.getStudent().getName());
		System.out.println("Member id = " + member.getPrimaryKey().toString());
		float newAmount = getCheckAmount(postingDetail.getAmount(), member,
				course);
		System.out.println("new amount = " + newAmount);

		postingDetail.setAmount(newAmount);
		final PaymentRecord record = createPaymentRecord(postingDetail,
				postings[0], postings[1], 1.0f, school);
		System.out.println("paymentrecord = "
				+ record.getPrimaryKey().toString());
		createVATPaymentRecord(record, postingDetail, 1.0f, school, member.getSchoolType(), member
				.getSchoolYear());
		createInvoiceRecord(record, member, postingDetail, null);
	}

	protected InvoiceRecord createInvoiceRecord(
			final PaymentRecord paymentRecord,
			final SchoolClassMember placement,
			final PostingDetail postingDetail, PlacementTimes checkPeriod)
			throws RemoteException, CreateException {
		 return createInvoiceRecord(paymentRecord, placement, postingDetail,
				checkPeriod, periodFrom.getDate(), periodTo.getDate());
	}

	protected InvoiceRecord createInvoiceRecord(
			final PaymentRecord paymentRecord,
			final SchoolClassMember placement,
			final PostingDetail postingDetail, PlacementTimes checkPeriod,
			final Date startPlacementDate, final Date endPlacementDate)
			throws RemoteException, CreateException {
		return getInvoiceBusiness().createInvoiceRecord(paymentRecord,
				placement, postingDetail, checkPeriod, startPlacementDate,
				endPlacementDate, BATCH_TEXT);
	}

	private float getCheckAmount(float originalAmount,
			SchoolClassMember member, AdultEducationCourse course) {
		if (course.getLength() > 5) {
			int days = getDays(member, course);
			int weeks = days / 7;
			if (weeks < parameters.getWeekLimit()) {
				return 0.0f;
			} else {
				if (member.getSchoolClass().getSchoolSeasonId() != parameters
						.getFlexPeriodId()) {
					return getNonFlexiblePayment(originalAmount, member, course);
				} else {
					return getFlexiblePayment(originalAmount, member, course);
				}
			}
		} else {
			if (member.getRemovedDate() != null) {
				if (!isFirstGradeForMemberInPeriod(member)) {
					if (getInvoiceRecordCountForMember(member) == 0) {
						return originalAmount
								* (parameters.getShortPercentage() / 100.0f);
					} else {
						return 0.0f;
					}
				} else {
					if (getInvoiceRecordCountForMember(member) == 0) {
						return originalAmount;
					} else {
						return originalAmount
								* (parameters.getShortGradePercentage() / 100.0f);
					}
				}
			} else {
				return 0.0f;
			}
		}
	}

	private float getNonFlexiblePayment(float originalAmount,
			SchoolClassMember member, AdultEducationCourse course) {
		IWTimestamp removedStamp = null;
		if (member.getRemovedDate() != null) {
			removedStamp = new IWTimestamp(member.getRemovedDate());
		}
		IWTimestamp courseStartStamp = new IWTimestamp(course.getStartDate());
		IWTimestamp courseEndStamp = new IWTimestamp(course.getEndDate());

		int courseDays = getDayDiff(courseStartStamp,
				courseEndStamp);
		float paymentPrDay = (originalAmount * (parameters.getLongPercentage() / 100.0f))
				/ courseDays;

		if (removedStamp == null) {
			if (getInvoiceRecordCountForMember(member) == 0) {
				if (courseEndStamp.isLaterThan(periodTo)) {
					return paymentPrDay
							* getDayDiff(courseStartStamp,
									periodTo);
				} else {
					return paymentPrDay * courseDays;
				}
			} else {
				if (courseEndStamp.isLaterThan(periodTo)) {
					return paymentPrDay
							* getDayDiff(periodFrom, periodTo);
				} else {
					return paymentPrDay
							* getDayDiff(periodFrom,
									courseEndStamp);
				}
			}
		} else {
			if (!isFirstGradeForMemberInPeriod(member)) {
				if (getInvoiceRecordCountForMember(member) == 0) {
					if (removedStamp.isEarlierThan(courseEndStamp)) {
						if (courseEndStamp.isEarlierThanOrEquals(periodTo)) {
							return paymentPrDay
									* getDayDiff(
											courseStartStamp, removedStamp);
						} else {
							return paymentPrDay
									* getDayDiff(
											courseStartStamp, periodTo);
						}
					} else {
						if (courseEndStamp.isEarlierThanOrEquals(periodTo)) {
							return originalAmount
									* (parameters.getLongPercentage() / 100.0f);
						} else {
							return paymentPrDay
									* getDayDiff(
											courseStartStamp, periodTo);
						}
					}
				} else {
					if (removedStamp.isEarlierThan(courseEndStamp)) {
						if (courseEndStamp.isEarlierThanOrEquals(periodTo)
								&& courseStartStamp
										.isEarlierThanOrEquals(periodFrom)) {
							return paymentPrDay
									* getDayDiff(periodFrom,
											removedStamp);
						} else if (courseEndStamp.isLaterThan(periodTo)
								&& courseStartStamp
										.isEarlierThanOrEquals(periodFrom)) {
							return paymentPrDay
									* getDayDiff(periodFrom,
											periodTo);
						} else if (courseEndStamp.isLaterThan(periodTo)
								&& courseStartStamp.isLaterThan(periodFrom)) {
							return paymentPrDay
									* getDayDiff(
											courseStartStamp, periodTo);
						} else {
							return paymentPrDay
									* getDayDiff(
											courseStartStamp, removedStamp);
						}
					} else {
						if (courseEndStamp.isEarlierThanOrEquals(periodTo)) {
							return paymentPrDay
									* getDayDiff(periodFrom,
											courseEndStamp);
						} else {
							return paymentPrDay
									* getDayDiff(periodFrom,
											periodTo);
						}
					}
				}
			} else {
				if (getInvoiceRecordCountForMember(member) == 0) {
					return originalAmount;
				} else {
					if (courseEndStamp.isEarlierThanOrEquals(periodTo)) {
						return originalAmount
								* (parameters.getLongGradePercentage() / 100.0f);
					} else {
						return paymentPrDay
								* getDayDiff(periodFrom,
										courseEndStamp)
								+ originalAmount
								* (parameters.getLongGradePercentage() / 100.0f);
					}
				}
			}

		}
	}

	private int getDayDiff(IWTimestamp from, IWTimestamp to) {
		return AccountingUtil.getDayDiff(from, to) + 1;
	}
	
	private float getFlexiblePayment(float originalAmount,
			SchoolClassMember member, AdultEducationCourse course) {
		IWTimestamp registerStamp = new IWTimestamp(member.getRegisterDate());
		IWTimestamp removedStamp = null;
		if (member.getRemovedDate() != null) {
			removedStamp = new IWTimestamp(member.getRemovedDate());
		}

		int courseDays = course.getLength() * 7;
		float paymentPrDay = (originalAmount * (parameters.getLongPercentage() / 100.0f))
				/ courseDays;

		if (removedStamp == null) {
			if (getInvoiceRecordCountForMember(member) == 0) {
				if (getDayDiff(registerStamp, periodTo) < courseDays) {
					return paymentPrDay
							* AccountingUtil
									.getDayDiff(registerStamp, periodTo);
				} else {
					return originalAmount
							* (parameters.getLongPercentage() / 100.0f);
				}
			} else {
				if (getDayDiff(registerStamp, periodTo) < courseDays) {
					return paymentPrDay
							* getDayDiff(periodFrom, periodTo);
				} else {
					return paymentPrDay
							* (courseDays - getDayDiff(
									periodFrom, periodTo));
				}
			}
		} else {
			if (!isFirstGradeForMemberInPeriod(member)) {
				if (getInvoiceRecordCountForMember(member) == 0) {
					if (getDayDiff(registerStamp, removedStamp) / 7 >= course
							.getLength()) {
						return originalAmount
								* (parameters.getLongPercentage() / 100.0f);
					} else {
						return paymentPrDay
								* getDayDiff(registerStamp,
										removedStamp);
					}
				} else {
					if (getDayDiff(registerStamp, removedStamp) >= courseDays
							&& getDayDiff(registerStamp,
									periodFrom) < courseDays) {
						return paymentPrDay
								* getDayDiff(registerStamp,
										periodFrom);
					} else {
						return paymentPrDay
								* (getDayDiff(registerStamp,
										removedStamp) - AccountingUtil
										.getDayDiff(registerStamp, periodFrom));
					}
				}
			} else {
				if (getInvoiceRecordCountForMember(member) == 0) {
					return originalAmount;
				} else {
					if (getDayDiff(registerStamp, periodFrom) >= courseDays) {
						return originalAmount
								* (parameters.getLongPercentage() / 100.0f);
					} else {
						return paymentPrDay
								* (getDayDiff(registerStamp,
										removedStamp) - AccountingUtil
										.getDayDiff(registerStamp, periodFrom))
								+ (originalAmount * (parameters
										.getLongPercentage() / 100.0f));
					}
				}
			}
		}
	}

	private int getDays(SchoolClassMember member, AdultEducationCourse course) {
		int days = 0;

		IWTimestamp registerStamp = new IWTimestamp(member.getRegisterDate());
		IWTimestamp removedStamp = null;
		if (member.getRemovedDate() != null) {
			removedStamp = new IWTimestamp(member.getRemovedDate());
		}
		IWTimestamp courseEndStamp = new IWTimestamp(course.getEndDate());

		if (removedStamp == null
				&& member.getSchoolClass().getSchoolSeasonId() != parameters
						.getFlexPeriodId()) {
			if (courseEndStamp.isLaterThanOrEquals(periodTo)) {
				days = getDayDiff(registerStamp, periodTo);
			} else {
				days = getDayDiff(registerStamp, courseEndStamp);
			}
		} else if (removedStamp != null
				&& member.getSchoolClass().getSchoolSeasonId() != parameters
						.getFlexPeriodId()) {
			if (removedStamp.isLaterThanOrEquals(periodTo)) {
				days = getDayDiff(registerStamp, periodTo);
			} else {
				days = getDayDiff(registerStamp, removedStamp);
			}
		} else if (removedStamp != null
				&& member.getSchoolClass().getSchoolSeasonId() == parameters
						.getFlexPeriodId()) {
			if (removedStamp.isLaterThanOrEquals(periodTo)) {
				days = getDayDiff(registerStamp, periodTo);
			} else {
				days = getDayDiff(registerStamp, removedStamp);
			}
		} else if (removedStamp == null
				&& member.getSchoolClass().getSchoolSeasonId() == parameters
						.getFlexPeriodId()) {
			days = getDayDiff(registerStamp, periodTo);
		}

		return days;
	}

	private boolean isFirstGradeForMemberInPeriod(SchoolClassMember member) {
		SchoolClassMemberGrade grade = getFirstGradeForMember(member);
		if (grade == null) {
			return false;
		}

		IWTimestamp creationStamp = new IWTimestamp(grade.getCreated());
		return creationStamp.isBetween(periodFrom, periodTo);
	}

	private SchoolClassMemberGrade getFirstGradeForMember(
			SchoolClassMember member) {
		SchoolClassMemberGrade grade = null;
		try {
			grade = getSchoolClassMemberGradeHome()
					.findFirstGradeSetForStudent(member);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return grade;
	}

	private int getInvoiceRecordCountForMember(SchoolClassMember member) {
		int count = 0;
		try {
			count = getInvoiceRecordHome()
					.getNumberOfInvoicesForStudent(member);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IDOException e) {
			e.printStackTrace();
		}

		return count;
	}

	private ArrayList getConditions(SchoolClassMember schoolClassMember,
			SchoolStudyPath path) {
		ArrayList conditions = new ArrayList();
		conditions.add(new ConditionParameter(
				RuleTypeConstant.CONDITION_ID_SCHOOL_TYPE, schoolClassMember
						.getSchoolType().getLocalizationKey()));
		conditions
				.add(new ConditionParameter(
						RuleTypeConstant.CONDITION_ID_STUDY_PATH, path
								.getPrimaryKey()));

		if (null != schoolClassMember) {
			errorRelated.append("School type:"
					+ schoolClassMember.getSchoolType().getName());
		}
		if (null != path) {
			errorRelated.append("Study path:" + path.getCode());
		}
		setStudyPath(schoolClassMember, conditions);
		return conditions;
	}

	protected PostingDetail getCheck(RegulationsBusiness regBus,
			Collection conditions, SchoolClassMember placement)
			throws RegulationException, MissingFlowTypeException,
			MissingConditionTypeException, MissingRegSpecTypeException,
			TooManyRegulationsException, RemoteException {
		return regBus
				.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
						category.getCategory(), /*
												 * The ID that selects
												 * barnomsorg in the regulation
												 */
						PaymentFlowConstant.OUT, // The payment flow is out
						calculationDate, // Current date to select the
						// correct date
						// range
						RuleTypeConstant.DERIVED, // The conditiontype
						RegSpecConstant.CHECK, // The ruleSpecType shall be
						// Check
						conditions, // The conditions that need to fulfilled
						0, // Sent in to be used for "Specialutrakning"
						null, // Contract not used here
						placement); // Sent in to be used for e.g. VAT
	}

	protected String[] getPostingStrings(Provider provider,
			SchoolClassMember schoolClassMember, RegulationSpecType regSpecType)
			throws PostingException, RemoteException, EJBException {
		return getPostingStrings(category, schoolClassMember.getSchoolType(),
				((Integer) regSpecType.getPrimaryKey()).intValue(), provider,
				calculationDate, -1, -1);
	}

	protected String[] getPostingStrings(SchoolCategory category,
			SchoolType schoolType, int regSpecTypeId, Provider provider,
			Date calculationDate, int schoolYearId, int studyPathId)
			throws PostingException, RemoteException {
		if (studyPathId == -1)
			studyPathId = -1;
		return getPostingBusiness().getPostingStrings(category, schoolType,
				regSpecTypeId, provider, calculationDate, schoolYearId, -1,
				false);
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and
	 * payment records for the Regular payments
	 */
	/*
	 * protected void regularPayment() { PostingDetail postingDetail = null;
	 * PlacementTimes placementTimes = null; Iterator regularPaymentIter = null;
	 * School school = getSchool();
	 * 
	 * try { if (isTestRun() && school != null) { regularPaymentIter =
	 * getRegularPaymentBusiness() .findRegularPaymentsForPeriodeAndSchool(
	 * startPeriod.getDate(), endPeriod.getDate(), school).iterator(); } else {
	 * regularPaymentIter = getRegularPaymentBusiness()
	 * .findRegularPaymentsForPeriodeAndCategory( startPeriod.getDate(),
	 * category).iterator(); } School regPaymentSchool; // Go through all the
	 * regular payments while (regularPaymentIter.hasNext()) {
	 * RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry)
	 * regularPaymentIter .next(); errorRelated = new
	 * ErrorLogger(getLocalizedString( "invoice.RegularPaymentEntryID",
	 * "RegularPaymentEntry ID") + ":" + regularPaymentEntry.getPrimaryKey());
	 * errorRelated.append(getLocalizedString("invoice.Placing", "Placing") +
	 * ":" + regularPaymentEntry.getPlacing());
	 * errorRelated.append(getLocalizedString("invoice.Amount", "Amount") + ":" +
	 * regularPaymentEntry.getAmount());
	 * errorRelated.append(getLocalizedString("invoice.School", "School") + ":" +
	 * regularPaymentEntry.getSchool()); postingDetail = new
	 * PostingDetail(regularPaymentEntry); regPaymentSchool =
	 * regularPaymentEntry.getSchool(); placementTimes =
	 * calculateTime(regularPaymentEntry.getFrom(),
	 * regularPaymentEntry.getTo()); try { PaymentRecord paymentRecord =
	 * createPaymentRecord( postingDetail, regularPaymentEntry.getOwnPosting(),
	 * regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(),
	 * regPaymentSchool, regularPaymentEntry.getNote());
	 * createVATPaymentRecord(paymentRecord, postingDetail,
	 * placementTimes.getMonths(), regPaymentSchool,
	 * regularPaymentEntry.getSchoolType(), null); User classMember =
	 * regularPaymentEntry.getUser(); if (classMember != null) { try {
	 * SchoolClassMember schoolClassMember = getSchoolClassMemberHome()
	 * .findLatestByUser(classMember); createInvoiceRecord(paymentRecord,
	 * schoolClassMember, postingDetail, placementTimes); } catch
	 * (FinderException e) { createNewErrorMessage( errorRelated,
	 * getLocalizedString( "invoice.schoolClassMemberNotFound", "The regular
	 * invoice pointed to a user, but the accordingly school class member was
	 * not found.")); } } } catch (IDOLookupException e) {
	 * createNewErrorMessage(errorRelated, getLocalizedString(
	 * "invoice.IDOLookup", "IDOLookup")); e.printStackTrace(); } catch
	 * (CreateException e) { createNewErrorMessage(errorRelated,
	 * getLocalizedString( "invoice.Create", "Create")); e.printStackTrace(); }
	 * if (!running) { return; } } } catch (FinderException e) {
	 * e.printStackTrace(); if (postingDetail != null) {
	 * createNewErrorMessage(postingDetail.getTerm(), "payment.DBSetupProblem"); }
	 * else { createNewErrorMessage("payment.severeError",
	 * "payment.DBSetupProblem"); } } catch (IDOLookupException e) {
	 * createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
	 * e.printStackTrace(); } catch (RemoteException e) {
	 * createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
	 * e.printStackTrace(); } }
	 */

	protected ExportDataMapping getCategoryPosting() throws FinderException,
			IDOLookupException, EJBException {
		return (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class)
				.findByPrimaryKeyIDO(category.getPrimaryKey());
	}

	protected BatchParameters getBatchParameters(int schoolTypeId)
			throws FinderException, IDOLookupException, EJBException {
		return ((BatchParametersHome) IDOLookup.getHome(BatchParameters.class))
				.findBySchoolTypeId(schoolTypeId);
	}

	protected SchoolType getSchoolType(int schoolTypeId)
			throws FinderException, IDOLookupException, EJBException {
		return (SchoolType) IDOLookup.getHome(SchoolType.class)
				.findByPrimaryKeyIDO(new Integer(schoolTypeId));
	}

	protected SchoolClassMemberHome getSchoolClassMemberHome()
			throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup
				.getHome(SchoolClassMember.class);
	}

	protected SchoolClassMemberGradeHome getSchoolClassMemberGradeHome()
			throws RemoteException {
		return (SchoolClassMemberGradeHome) IDOLookup
				.getHome(SchoolClassMemberGrade.class);
	}

	protected InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}

	private AdultEducationBusiness getAdultEducationBusiness() {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(iwc,
					AdultEducationBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}