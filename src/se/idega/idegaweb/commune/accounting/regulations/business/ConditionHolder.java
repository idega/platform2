/*
 * $Id: ConditionHolder.java,v 1.2 2003/09/06 22:43:32 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;


/**
 * ConditionHolder holds class info for a condition  
 * <p>
 * $Id: ConditionHolder.java,v 1.2 2003/09/06 22:43:32 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.2 $
 */
public class ConditionHolder {

	private String name = null;
	private String localizationKey = null;
	private String businessClassName = null;
	private String collectDataMethod = null;
	private String dataMethodName = null;
	private int pk = 0;
	
	public ConditionHolder(
			String pk, 
			String name, 
			String localizationKey, 
			String businessClassName, 
			String collectDataMethod,
			String dataMethodName) {
				
		this.pk = Integer.parseInt(pk);
		this.name = name;
		this.localizationKey = localizationKey;					
		this.businessClassName = businessClassName;					
		this.collectDataMethod = collectDataMethod;
		this.dataMethodName = dataMethodName;
	}
	 
	public void setName(String name) {
		this.name = name;
	}

	public void setLocalizationKey(String localizationKey) {
		this.localizationKey = localizationKey;
	}

	public void setBusinessClassName(String businessClassName) {
		this.businessClassName = businessClassName;
	}

	public void setCollectDataMethod(String method) {
		this.collectDataMethod = method;
	}

	public void setDataMethodName(String method) {
		this.dataMethodName = method;
	}

	public String getName() {
		return this.name;
	}

	public int getPrimaryKey() {
		return this.pk;
	}

	public String getLocalizationKey() {
		return this.localizationKey;
	}

	public String getBusinessClassName() {
		return this.businessClassName;
	}

	public String getCollectDataMethod() {
		return this.collectDataMethod;
	}

	public String getDataMethodName() {
		return this.dataMethodName;
	}

}
