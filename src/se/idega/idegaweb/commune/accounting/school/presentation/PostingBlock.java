/*
 * $Id: PostingBlock.java,v 1.2 2003/10/27 10:22:54 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.presentation;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.posting.presentation.PostingParameterListEditor;

import com.idega.presentation.IWContext;

/** 
 * This block is a subclass of PostingParameterListEditor  
 * used for editing own posting and double posting strings.
 * <p>
 * Last modified: $Date: 2003/10/27 10:22:54 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 * @see se.idega.idegaweb.commune.accounting.posting.presentation.PostingParameterListEditor
 */
public class PostingBlock extends PostingParameterListEditor {

	private String ownPosting = null;
	private String doublePosting = null;
	
	/**
	 * Constructs posting block with fields for own and double posting strings.
	 */
	public PostingBlock(String ownPosting, String doublePosting) {
		this.ownPosting = ownPosting;
		this.doublePosting = doublePosting;
	}

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void init(final IWContext iwc) {
		setDefaultParameters();
		addTempFieldParameters(iwc, new Date(System.currentTimeMillis()));
		add(getPostingParameterForm(iwc, getThisPostingParameter(iwc), ownPosting, doublePosting));
	}
}
