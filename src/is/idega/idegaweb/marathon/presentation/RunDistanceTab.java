/*
 * Created on Aug 17, 2004
 *
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.util.Locale;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.presentation.UserGroupTab;
import com.idega.util.LocaleUtil;


/**
 * @author birna
 *
 */
public class RunDistanceTab extends UserGroupTab{
	
	private static final String PARAMETER_USE_CHIP = "use_chip";
	private static final String PARAMETER_FAMILY_DISCOUNT = "family_discount";
	private static final String PARAMETER_ALLOWS_GROUPS = "allows_groups";

	private static final String PARAMETER_PRICE_ISK = "price_isk";
	private static final String PARAMETER_PRICE_EUR = "price_eur";
	private static final String PARAMETER_CHILDREN_PRICE_ISK = "children_price_isk";
	private static final String PARAMETER_CHILDREN_PRICE_EUR = "children_price_eur";
	
	private static final String PARAMETER_NUMBER_OF_SPLITS = "number_of_splits";

	private TextInput priceISK;
	private TextInput priceEUR;
	private TextInput childrenPriceISK;
	private TextInput childrenPriceEUR;
	
	private CheckBox useChip;
	private CheckBox familyDiscount;
	private CheckBox allowsGroups;
	
	private DropdownMenu numberOfSplits;
	
	private Text priceISKText;
	private Text priceEURText;
	private Text childrenPriceISKText;
	private Text childrenPriceEURText;
	private Text useChipText;
	private Text familyDiscountText;
	private Text allowsGroupsText;
	private Text numberOfSplitsText;
	
	public RunDistanceTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString("run_tab.distance_name", "Distance info"));
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			Boolean useChip = new Boolean(iwc.isParameterSet(PARAMETER_USE_CHIP));
			Boolean familyDiscount = new Boolean(iwc.isParameterSet(PARAMETER_FAMILY_DISCOUNT));
			Boolean allowsGroups = new Boolean(iwc.isParameterSet(PARAMETER_ALLOWS_GROUPS));
			
			String priceISK = iwc.getParameter(PARAMETER_PRICE_ISK);
			String priceEUR = iwc.getParameter(PARAMETER_PRICE_EUR);
			String childPriceISK = iwc.getParameter(PARAMETER_CHILDREN_PRICE_ISK);
			String childPriceEUR = iwc.getParameter(PARAMETER_CHILDREN_PRICE_EUR);
			
			String numberOfSplits = iwc.getParameter(PARAMETER_NUMBER_OF_SPLITS);
			
			fieldValues.put(PARAMETER_USE_CHIP, useChip);
			fieldValues.put(PARAMETER_FAMILY_DISCOUNT, familyDiscount);
			fieldValues.put(PARAMETER_ALLOWS_GROUPS, allowsGroups);

			if (priceISK != null) {
				fieldValues.put(PARAMETER_PRICE_ISK, new Float(priceISK));
			}
			if (priceEUR != null) {
				fieldValues.put(PARAMETER_PRICE_EUR, new Float(priceEUR));
			}
			if (childPriceISK != null) {
				fieldValues.put(PARAMETER_CHILDREN_PRICE_ISK, new Float(childPriceISK));
			}
			if(childPriceEUR != null){
				fieldValues.put(PARAMETER_CHILDREN_PRICE_EUR, new Float(childPriceEUR));
			}
			if(numberOfSplits != null){
				fieldValues.put(PARAMETER_NUMBER_OF_SPLITS, new Integer(numberOfSplits));
			}
			
			updateFieldsDisplayStatus();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
		try {
			Distance distance = ConverterUtility.getInstance().convertGroupToDistance(new Integer(getGroupId()));
			
			fieldValues.put(PARAMETER_USE_CHIP, new Boolean(distance.isUseChip()));
			fieldValues.put(PARAMETER_FAMILY_DISCOUNT, new Boolean(distance.isFamilyDiscount()));
			fieldValues.put(PARAMETER_ALLOWS_GROUPS, new Boolean(distance.isAllowsGroups()));
			fieldValues.put(PARAMETER_PRICE_ISK, new Float(distance.getPrice(LocaleUtil.getIcelandicLocale())));
			fieldValues.put(PARAMETER_PRICE_EUR, new Float(distance.getPrice(Locale.ENGLISH)));
			fieldValues.put(PARAMETER_CHILDREN_PRICE_ISK, new Float(distance.getChildrenPrice(LocaleUtil.getIcelandicLocale())));
			fieldValues.put(PARAMETER_CHILDREN_PRICE_EUR, new Float(distance.getChildrenPrice(Locale.ENGLISH)));
			fieldValues.put(PARAMETER_NUMBER_OF_SPLITS, new Integer(distance.getNumberOfSplits()));

			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			System.err.println("RunDistanceTab error initFieldContents, GroupId : " + getGroupId());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
		priceISK = new TextInput(PARAMETER_PRICE_ISK);
		priceISK.setAsFloat("Not a valid price");

		priceEUR = new TextInput(PARAMETER_PRICE_EUR);
		priceEUR.setAsFloat("Not a valid price");

		childrenPriceISK = new TextInput(PARAMETER_CHILDREN_PRICE_ISK);
		childrenPriceISK.setAsFloat("Not a valid price");

		childrenPriceEUR = new TextInput(PARAMETER_CHILDREN_PRICE_EUR);
		childrenPriceEUR.setAsFloat("Not a valid price");
		
		useChip = new CheckBox(PARAMETER_USE_CHIP);
		familyDiscount = new CheckBox(PARAMETER_FAMILY_DISCOUNT);
		allowsGroups = new CheckBox(PARAMETER_ALLOWS_GROUPS);
		
		numberOfSplits = new DropdownMenu(PARAMETER_NUMBER_OF_SPLITS);
		numberOfSplits.addMenuElement(0, "0");
		numberOfSplits.addMenuElement(1, "1");
		numberOfSplits.addMenuElement(2, "2");
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues.put(PARAMETER_USE_CHIP, new Boolean(false));
		fieldValues.put(PARAMETER_FAMILY_DISCOUNT, new Boolean(false));
		fieldValues.put(PARAMETER_ALLOWS_GROUPS, new Boolean(false));
		fieldValues.put(PARAMETER_PRICE_ISK, new Float(0));
		fieldValues.put(PARAMETER_PRICE_EUR, new Float(0));
		fieldValues.put(PARAMETER_CHILDREN_PRICE_ISK, new Float(0));
		fieldValues.put(PARAMETER_CHILDREN_PRICE_EUR, new Float(0));
		fieldValues.put(PARAMETER_NUMBER_OF_SPLITS, new Integer(0));
		
		updateFieldsDisplayStatus();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		priceISKText = new Text(iwrb.getLocalizedString("run_tab.price_ISK", "Price (ISK)"));
		priceISKText.setBold();
		
		priceEURText = new Text(iwrb.getLocalizedString("run_tab.price_EUR", "Price (EUR)"));
		priceEURText.setBold();
		
		childrenPriceISKText = new Text(iwrb.getLocalizedString("run_tab.children_price_ISK", "Children price (ISK)"));
		childrenPriceISKText.setBold();
		
		childrenPriceEURText = new Text(iwrb.getLocalizedString("run_tab.children_price_ISK", "Children price (EUR)"));
		childrenPriceEURText.setBold();
		
		useChipText = new Text(iwrb.getLocalizedString("run_tab.use_chip", "Uses chips"));
		useChipText.setBold();
		
		familyDiscountText = new Text(iwrb.getLocalizedString("run_tab.family_discount", "Uses family discount"));
		familyDiscountText.setBold();

		allowsGroupsText = new Text(iwrb.getLocalizedString("run_tab.allows_groups", "Allows groups"));
		allowsGroupsText.setBold();

		numberOfSplitsText = new Text(iwrb.getLocalizedString("run_tab.number_of_splits", "Number of splits"));
		numberOfSplitsText.setBold();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		resize(1, 1);
		setCellpadding(0);
		setCellspacing(0);
		
		Table table = new Table(2, 4);
		table.setCellpadding(5);
		table.setCellspacing(0);
		table.setWidth(1, "50%");
		table.setWidth(2, "50%");
		
		table.setWidth(Table.HUNDRED_PERCENT);
		table.add(priceISKText, 1, 1);
		table.add(Text.getBreak(), 1, 1);
		table.add(priceISK, 1, 1);
		
		table.add(childrenPriceISKText, 2, 1);
		table.add(Text.getBreak(), 2, 1);
		table.add(childrenPriceISK, 2, 1);
		
		table.add(priceEURText, 1, 2);
		table.add(Text.getBreak(), 1, 2);
		table.add(priceEUR, 1, 2);
		
		table.add(childrenPriceEURText, 2, 2);
		table.add(Text.getBreak(), 2, 2);
		table.add(childrenPriceEUR, 2, 2);
		
		table.mergeCells(1, 3, 2, 3);
		table.add(numberOfSplitsText, 1, 3);
		table.add(Text.getNonBrakingSpace(), 1, 3);
		table.add(numberOfSplits, 1, 3);

		table.mergeCells(1, 4, 2, 4);
		table.add(useChip, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(useChipText, 1, 4);
		
		table.add(Text.getBreak(), 1, 4);
		
		table.add(familyDiscount, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(familyDiscountText, 1, 4);

		table.add(Text.getBreak(), 1, 4);
		
		table.add(allowsGroups, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(allowsGroupsText, 1, 4);
		
		add(table, 1, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		try {
			if (getGroupId() > -1) {
				Distance distance = ConverterUtility.getInstance().convertGroupToDistance(new Integer(getGroupId()));
				
				distance.setUseChip(((Boolean) fieldValues.get(PARAMETER_USE_CHIP)).booleanValue());
				distance.setFamilyDiscount(((Boolean) fieldValues.get(PARAMETER_FAMILY_DISCOUNT)).booleanValue());
				distance.setAllowsGroups(((Boolean) fieldValues.get(PARAMETER_ALLOWS_GROUPS)).booleanValue());
				
				distance.setPriceInISK(((Float) fieldValues.get(PARAMETER_PRICE_ISK)).floatValue());
				distance.setPriceInEUR(((Float) fieldValues.get(PARAMETER_PRICE_EUR)).floatValue());
				distance.setChildrenPriceInISK(((Float) fieldValues.get(PARAMETER_CHILDREN_PRICE_ISK)).floatValue());
				distance.setChildrenPriceInEUR(((Float) fieldValues.get(PARAMETER_CHILDREN_PRICE_EUR)).floatValue());

				distance.setNumberOfSplits(((Integer) fieldValues.get(PARAMETER_NUMBER_OF_SPLITS)).intValue());

				distance.store();
			}
		}
		catch (Exception e) {
			//return false;
			e.printStackTrace(System.err);
			throw new RuntimeException("update group exception");
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		useChip.setChecked(((Boolean) fieldValues.get(PARAMETER_USE_CHIP)).booleanValue());
		familyDiscount.setChecked(((Boolean) fieldValues.get(PARAMETER_FAMILY_DISCOUNT)).booleanValue());
		allowsGroups.setChecked(((Boolean) fieldValues.get(PARAMETER_ALLOWS_GROUPS)).booleanValue());
		
		priceISK.setContent(((Float) fieldValues.get(PARAMETER_PRICE_ISK)).toString());
		priceEUR.setContent(((Float) fieldValues.get(PARAMETER_PRICE_EUR)).toString());
		childrenPriceISK.setContent(((Float) fieldValues.get(PARAMETER_CHILDREN_PRICE_ISK)).toString());
		childrenPriceEUR.setContent(((Float) fieldValues.get(PARAMETER_CHILDREN_PRICE_EUR)).toString());

		numberOfSplits.setSelectedElement(((Integer) fieldValues.get(PARAMETER_NUMBER_OF_SPLITS)).intValue());
	}
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
}