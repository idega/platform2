/*
 * $Id: VATException.java,v 1.2 2003/08/24 22:35:38 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

/**
 * Exception for data input errors in VATBusiness.  
 * <p>
 * Last modified: $Date: 2003/08/24 22:35:38 $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 */
public class VATException extends Exception {

	private final static String KP = "vat_error."; // key prefix 

	public final static String KEY_DATE_FORMAT = KP + "date_format";
	public final static String KEY_FROM_DATE_MISSING = KP + "from_date_missing";
	public final static String KEY_TO_DATE_MISSING = KP + "to_date_missing";
	public final static String KEY_DESCRIPTION_MISSING = KP + "description_missing";
	public final static String KEY_VAT_PERCENT_MISSING = KP + "vat_percent_missing";
	public final static String KEY_VAT_PERCENT_VALUE = KP + "vat_percent_value";
	public final static String KEY_PAYMENT_FLOW_TYPE_MISSING = KP + "payment_flow_type_missing";
	public final static String KEY_PROVIDER_TYPE_MISSING = KP + "provider_type_missing";
	public final static String KEY_CANNOT_SAVE_VAT_REGULATION = KP + "cannot_save_vat_regulation";
	public final static String KEY_CANNOT_DELETE_VAT_REGULATION = KP + "cannot_delete_vat_regulation";

	public final static String DEFAULT_DATE_FORMAT = "Datum måste anges på formen ÅÅMM, ÅÅMMDD, eller ÅÅÅÅMMDD.";
	public final static String DEFAULT_FROM_DATE_MISSING = "Periodens startdatum måste fyllas i.";
	public final static String DEFAULT_TO_DATE_MISSING = "Periodens slutdatum måste fyllas i.";
	public final static String DEFAULT_DESCRIPTION_MISSING = "Benämning av momssatsen måste fyllas i.";
	public final static String DEFAULT_VAT_PERCENT_MISSING = "Procentsats måste fyllas i.";
	public final static String DEFAULT_VAT_PERCENT_VALUE = "Procentsatsen måste vara mellan 0 och 100.";
	public final static String DEFAULT_PAYMENT_FLOW_TYPE_MISSING = "Ström måste väljas.";
	public final static String DEFAULT_PROVIDER_TYPE_MISSING = "Anordnartyp måste väljas.";
	public final static String DEFAULT_CANNOT_SAVE_VAT_REGULATION = "Momssatsen kunde inte sparas på grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_DELETE_VAT_REGULATION = "Momssatsen kunde inte tas bort på grund av tekniskt fel.";

	private String textKey = null;
	private String defaultText = null;
	
	/**
	 * Constructs a VAT exception with the specified text key and default text.
	 * @param textKey the text key for the error message
	 * @param defaultText the default text for the error message
	 */
	public VATException(String textKey, String defaultText) {
		this.textKey = textKey;
		this.defaultText = defaultText;
	}
	
	/**
	 * Returns the error message text key.
	 */
	public String getTextKey() {
		return textKey; 
	}
	
	/**
	 * Returns the default error message text.
	 */
	public String getDefaultText() {
		return defaultText; 
	}
}
