package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.application.business.ReferenceNumberFinder;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffBMPBean;
import com.idega.block.finance.data.TariffHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.util.IWTimestamp;
import com.idega.util.text.ContentParsable;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 * 
 * @created 9. mars 2002
 * @version 1.0
 */
public class LetterParser implements ContentParsable {

	public static String ANNOUNCEMENT = "ANNOUNCEMENT";

	public static String APPROVAL = "APPROVAL";
	public static String REJECTION = "REJECTION";
	public static String ALLOCATION = "ALLOCATION";
	public static String SIGNATURE = "SIGNATURE";
	public static String RESIGN = "RESIGN";
	public static String TERMINATION = "TERMINATION";
	public static String RETURN = "RETURN";
	public static String DELIVER = "DELIVER";
	public static String SUBMISSION = "SUBMISSION";
	public static String APPLICANT_ACCEPTS = "APPLICANT_ACCEPTS";
	public static String APPLICANT_DENIES = "APPLICANT_DENIES";
	public static String APPLICANT_QUITS = "APPLICANT_QUITS";
	public static String WAIT_LIST_REMOVED = "WAIT_LIST_REMOVED";

	public final static String tenant_name = "TENANT_NAME";
	public final static String tenant_address = "TENANT_ADDRESS";
	public final static String tenant_id = "TENANT_ID";
	public final static String contract_starts = "START_DATE";
	public final static String contract_ends = "END_DATE";
	public final static String con_deliver = "KEY_DELIVER_DATE";
	public final static String con_return = "KEY_RETURN_DATE";
	public final static String reference_number = "REFERENCE_NUMBER";
	public final static String today = "TODAY";
	
	public final static String TENANT_RESIGN_ADDRESS = "TENTANT_RESIGN_ADDRESS";
	public final static String TENANT_RESIGN_PO = "TENTANT_RESIGN_PO";
	public final static String TENANT_RESIGN_PHONE = "TENTANT_RESIGN_PHONE";
	public final static String TENANT_RESIGN_EMAIL = "TENTANT_RESIGN_EMAIL";

	public final static String aprt_name = "APRT_NAME";
	public final static String aprt_desc = "APRT_DESCR";
	public final static String floor_name = "FLOOR_NAME";
	public final static String bldg_name = "BUILDING_NAME";
	public final static String bldg_desc = "BUILDING_DESCR";
	public final static String camp_name = "CAMPUS_NAME";
	public final static String camp_info = "CAMPUS_DESCR";
	public final static String type_name = "TYPE_NAME";
	public final static String type_name_full = "TYPE_NAME_FULL";
	public final static String type_name_short = "TYPE_NAME_SHORT";
	public final static String type_desc = "TYPE_DESCR";
	public final static String type_area = "TYPE_AREA";
	public final static String cat_name = "CATEGORY_NAME";
	public final static String cat_desc = "CATEGORY_DESC";

	private static final String WL_COMPLEX = "WL_COMPLEXT";
	private static final String WL_APARTMENT_TYPE = "WL_APARTMENT_TYPE";
	private static final String WL_ORDER = "WL_ORDER";
	private static final String WL_TYPE = "WL_TYPE";
	private static final String WL_CHOICE_NUMBER = "WL_CHOICE_NUMBER";
	private static final String WL_LAST_CONFIRMATION = "WL_LAST_CONFIRM";
	private static final String WL_NUMBER_OF_REJECTIONS = "WL_NR_DENIALS";
	private static final String WL_REJECT_FLAG = "WL_IN_DENIAL";
	private static final String WL_REMOVED_FROM_LIST = "WL_WISHES_OFF";
	private static final String WL_PRIORITY_LEVEL = "WL_PRIORITY";
	private static final String WL_ACCEPTED_DATE = "WL_ACCEPTED_DATE";
	
	private static final String CURRENT_RENT = "CURRENT_RENT";

	public static String[] TAGS = { tenant_name, tenant_address, tenant_id,
			contract_starts, contract_ends, reference_number, today, aprt_name,
			aprt_desc, floor_name, bldg_name, bldg_desc, camp_name, camp_info,
			type_name, type_name_full, type_name_short, type_desc, type_area,
			cat_name, cat_desc, WL_ORDER, WL_TYPE, WL_CHOICE_NUMBER,
			WL_LAST_CONFIRMATION, WL_NUMBER_OF_REJECTIONS, WL_REJECT_FLAG,
			WL_REMOVED_FROM_LIST, WL_PRIORITY_LEVEL, WL_ACCEPTED_DATE,
			WL_COMPLEX, WL_APARTMENT_TYPE, CURRENT_RENT, TENANT_RESIGN_ADDRESS, TENANT_RESIGN_PO, TENANT_RESIGN_PHONE, TENANT_RESIGN_EMAIL };

	public static String[] types = { ANNOUNCEMENT, APPROVAL, REJECTION,
			ALLOCATION, SIGNATURE, RETURN, DELIVER, RESIGN, TERMINATION,
			SUBMISSION };

	private EntityHolder holder;

	/** Constructor for the LetterParser object */
	public LetterParser() {
	}

	/**
	 * Constructor for the LetterParser object
	 * 
	 * @param holder
	 *            Description of the Parameter
	 */
	public LetterParser(EntityHolder holder) {
		this.holder = holder;
	}

	/**
	 * Sets the entityHolder attribute of the LetterParser object
	 * 
	 * @param holder
	 *            The new entityHolder value
	 */
	public void setEntityHolder(EntityHolder holder) {
		this.holder = holder;
	}

	/**
	 * Gets the parseTags of the LetterParser object
	 * 
	 * @return The parse tags value
	 */
	public String[] getParseTags() {
		return TAGS;
	}

	/**
	 * Gets the parsedString of the LetterParser object
	 * 
	 * @param tag
	 *            Description of the Parameter
	 * @return The parsed string value
	 */
	public String getParsedString(IWApplicationContext iwac, String tag) {
		try {
			// Applicant part
			if (holder.getApplicant() != null) {
				if (tag.equals(tenant_name)) {
					return holder.getApplicant().getName();
				} else if (tag.equals(tenant_address)) {
					return holder.getApplicant().getResidence();
				} else if (tag.equals(tenant_id)) {
					return holder.getApplicant().getSSN();
				} else if (tag.equals(TENANT_RESIGN_ADDRESS)) {
					return holder.getApplicant().getResignationAddress();
				} else if (tag.equals(TENANT_RESIGN_PO)) {
					return holder.getApplicant().getResignationPO();					
				} else if (tag.equals(TENANT_RESIGN_PHONE)) {
					return holder.getApplicant().getResignationPhone();					
				} else if (tag.equals(TENANT_RESIGN_EMAIL)) {
					return holder.getApplicant().getResignationEmail();
				}
			}

			// Contract section
			if (holder.getContract() != null) {
				if (tag.equals(contract_starts)) {
					return new IWTimestamp(holder.getContract().getValidFrom())
							.getDateString("dd.MM.yyyy");
				} else if (tag.equals(contract_ends)) {
					return new IWTimestamp(holder.getContract().getValidTo())
							.getDateString("dd.MM.yyyy");
				} else if (tag.equals(today)) {
					return IWTimestamp.RightNow().getDateString("dd.MM.yyyy");
				}
			}

			// Apartment section
			if (holder.getApartmentHolder() != null) {
				String attribute = BuildingCacher.PREFIXTYPE
						+ holder.getApartmentHolder().getApartmentType()
								.getPrimaryKey().toString();
				Collection tariff = null;
				try {
					tariff = ((TariffHome) IDOLookup.getHome(Tariff.class))
							.findAllByColumn(
									TariffBMPBean.getColumnAttribute(),
									attribute);
				} catch (IDOLookupException e) {
				} catch (FinderException e) {
				}
				double cRent = 0.0d;

				double rentA = 0.0d;
				double rentB = 0.0d;
				double rentC = 0.0d;
				double rentD = 0.0d;
				double rentNotA = 0.0d;

				if (tariff != null) {
					Iterator it = tariff.iterator();
					while (it.hasNext()) {
						Tariff t = (Tariff) it.next();
						cRent += t.getPrice();
						if (null != t.getIndexType()) {
							String type = t.getIndexType();
							if ("A".equals(type)) {
								rentA += t.getPrice();
							} else if ("B".equals(type)) {
								rentNotA += t.getPrice();
								rentB += t.getPrice();
							} else if ("C".equals(type)) {
								rentNotA += t.getPrice();
								rentC += t.getPrice();
							} else if ("D".equals(type)) {
								rentNotA += t.getPrice();
								rentD += t.getPrice();
							} else {
								rentNotA += t.getPrice();
							}
						} else {
							rentNotA += t.getPrice();
						}
					}
				}
				
				NumberFormat format = NumberFormat.getInstance();
				format.setMaximumFractionDigits(0);
				format.setMinimumFractionDigits(0);
				format.setGroupingUsed(true);

				if (tag.equals(aprt_name)) {
					return holder.getApartmentHolder().getApartment().getName();
				} else if (tag.equals(aprt_desc)) {
					return holder.getApartmentHolder().getApartment().getInfo();
				} else if (tag.equals(floor_name)) {
					return holder.getApartmentHolder().getFloor().getName();
				} else if (tag.equals(bldg_name)) {
					return holder.getApartmentHolder().getBuilding().getName();
				} else if (tag.equals(bldg_desc)) {
					return holder.getApartmentHolder().getBuilding().getInfo();
				} else if (tag.equals(camp_name)) {
					return holder.getApartmentHolder().getComplex().getName();
				} else if (tag.equals(camp_info)) {
					return holder.getApartmentHolder().getComplex().getInfo();
				} else if (tag.equals(type_name)) {
					String type = holder.getApartmentHolder()
							.getApartmentType().getName();
					if (type != null) {
						StringTokenizer tok = new StringTokenizer(type);
						return tok.nextToken();
					}
					return holder.getApartmentHolder().getApartmentType()
							.getName();
				} else if (tag.equals(type_name_full)) {
					return holder.getApartmentHolder().getApartmentType()
							.getName();
				} else if (tag.equals(type_name_short)) {
					return holder.getApartmentHolder().getApartmentType()
							.getAbbreviation();
				} else if (tag.equals(type_desc)) {
					return holder.getApartmentHolder().getApartmentType()
							.getInfo();
				} else if (tag.equals(type_area)) {
					return String.valueOf(holder.getApartmentHolder()
							.getApartmentType().getArea());
				} else if (tag.equals(cat_name)) {
					return holder.getApartmentHolder().getApartmentCategory()
							.getName();
				} else if (tag.equals(cat_desc)) {
					return holder.getApartmentHolder().getApartmentCategory()
							.getInfo();
				} else if (tag.equals(CURRENT_RENT)) {
					return format.format(cRent);
				}
			}
			// Application part
			if (holder.getApplication() != null) {
				if (tag.equals(reference_number)) {
					int id = new Integer(holder.getApplication()
							.getPrimaryKey().toString()).intValue();
					return ReferenceNumberFinder.getInstance(iwac).lookup(id);
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the parseTypes of the LetterParser object
	 * 
	 * @return The parse types value
	 */
	public String[] getParseTypes() {
		return types;
	}

	/**
	 * Gets the parseObject of the LetterParser object
	 * 
	 * @return The parse object value
	 */
	public Object getParseObject() {
		return holder;
	}

	public String getDelimiters() {
		return "[]";
	}

	/**
	 * Static method to provide a formatted tag string with [ ] around the value
	 * 
	 * @param tag
	 *            Description of the Parameter
	 * @return The formatted tag value
	 */
	public static String getFormattedTag(String tag) {
		return "[" + tag + "]";
	}

	public String formatTag(String tag) {
		return getFormattedTag(tag);
	}
}
