/*
 * $Id: AccountBlock.java,v 1.1 2004/03/19 10:49:46 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.presentation;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.posting.presentation.PostingParameterListEditor;

import com.idega.presentation.IWContext;

/** 
 * This block is a subclass of PostingParameterListEditor  
 * used for editing own posting, double posting,
 * payable account and customer claim account strings.
 * <p>
 * Last modified: $Date: 2004/03/19 10:49:46 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 * @see se.idega.idegaweb.commune.accounting.posting.presentation.PostingParameterListEditor
 */
public class AccountBlock extends PostingParameterListEditor {

	private String ownPosting = null;
	private String doublePosting = null;
	private String payableAccount = null;
	private String customerClaimAccount = null;
	
	/**
	 * Constructs account block with empty fields for own and double posting strings
	 */	
	public AccountBlock() {
		setAddPayableAccount(true);
		setAddCustomerClaimAccount(true);
	}	
	
	/**
	 * Constructs account block with fields for own and double posting strings by calling generateStrings(IWContext)
	 */	
	public AccountBlock(IWContext iwc) throws PostingParametersException{
		this();
		generateStrings(iwc);
	}
		
	/**
	 * Constructs account block with fields for the specified posting strings.
	 */
	public AccountBlock(String ownPosting, String doublePosting, String payableAccount, String customerClaimAccount) {
		this();
		this.ownPosting = ownPosting;
		this.doublePosting = doublePosting;
		this.payableAccount = payableAccount;
		this.customerClaimAccount = customerClaimAccount;
	}

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void init(IWContext iwc) {
		setDefaultParameters();
		addTempFieldParameters(iwc, new Date(System.currentTimeMillis()));
		add(getPostingParameterForm(iwc, getThisPostingParameter(iwc), ownPosting, doublePosting, payableAccount, customerClaimAccount));
	}
}
