/*
 * $Id: DistanceBMPBean.java,v 1.4 2005/07/27 10:55:52 laddi Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.util.Locale;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.LocaleUtil;


/**
 * Last modified: $Date: 2005/07/27 10:55:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class DistanceBMPBean extends GroupBMPBean  implements Distance{

	private static final String METADATA_USE_CHIP = "use_chip";
	private static final String METADATA_PRICE_ISK = "price_isk";
	private static final String METADATA_PRICE_EUR = "price_eur";
	private static final String METADATA_FAMILY_DISCOUNT = "family_discount";
	private static final String METADATA_CHILDREN_PRICE_ISK = "children_price_isk";
	private static final String METADATA_CHILDREN_PRICE_EUR = "children_price_eur";
	private static final String METADATA_ALLOWS_GROUPS = "allows_groups";
	private static final String METADATA_NEXT_PARTICIPANT_NUMBER = "next_participant_number";

	public boolean isUseChip() {
		String useChip = this.getMetaData(METADATA_USE_CHIP);
		if (useChip != null) {
			return new Boolean(useChip).booleanValue();
		}
		return true;
	}

	public void setUseChip(boolean useChip) {
		setMetaData(METADATA_USE_CHIP, String.valueOf(useChip), "java.lang.Boolean");
	}
	
	public float getPrice(Locale locale) {
		String price = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			price = getMetaData(METADATA_PRICE_ISK);
		}
		else {
			price = getMetaData(METADATA_PRICE_EUR);
		}
		
		if (price != null) {
			return Float.parseFloat(price);
		}
		return 0;
	}
	
	public void setPriceInISK(float price) {
		setMetaData(METADATA_PRICE_ISK, String.valueOf(price), "java.lang.Float");
	}

	public void setPriceInEUR(float price) {
		setMetaData(METADATA_PRICE_EUR, String.valueOf(price), "java.lang.Float");
	}

	public float getChildrenPrice(Locale locale) {
		String price = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			price = getMetaData(METADATA_CHILDREN_PRICE_ISK);
		}
		else {
			price = getMetaData(METADATA_CHILDREN_PRICE_EUR);
		}
		
		if (price != null) {
			return Float.parseFloat(price);
		}
		else {
			return getPrice(locale);
		}
	}
	
	public void setChildrenPriceInISK(float price) {
		setMetaData(METADATA_CHILDREN_PRICE_ISK, String.valueOf(price), "java.lang.Float");
	}

	public void setChildrenPriceInEUR(float price) {
		setMetaData(METADATA_CHILDREN_PRICE_EUR, String.valueOf(price), "java.lang.Float");
	}

	public boolean isFamilyDiscount() {
		String discount = this.getMetaData(METADATA_FAMILY_DISCOUNT);
		if (discount != null) {
			return new Boolean(discount).booleanValue();
		}
		return false;
	}

	public void setFamilyDiscount(boolean discount) {
		setMetaData(METADATA_FAMILY_DISCOUNT, String.valueOf(discount), "java.lang.Boolean");
	}

	public boolean isAllowsGroups() {
		String allowsGroups = this.getMetaData(METADATA_ALLOWS_GROUPS);
		if (allowsGroups != null) {
			return new Boolean(allowsGroups).booleanValue();
		}
		return true;
	}

	public void setAllowsGroups(boolean allowsGroups) {
		setMetaData(METADATA_ALLOWS_GROUPS, String.valueOf(allowsGroups), "java.lang.Boolean");
	}
	
	public int getNextAvailableParticipantNumber() {
		String number = this.getMetaData(METADATA_NEXT_PARTICIPANT_NUMBER);
		if (number != null) {
			return Integer.parseInt(number);
		}
		return -1;
	}
	
	public void setNextAvailableParticipantNumber(int number) {
		setMetaData(METADATA_NEXT_PARTICIPANT_NUMBER, String.valueOf(number), "java.lang.Integer");
	}
}