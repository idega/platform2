/*
 * Created on 22.7.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package com.idega.block.finance.data;

/**
 * @author aron
 *
 * AssessmentStatus constants for assessments
 */
public class AssessmentStatus {
	 /**
	  * Status ASSESSED for all newly created assessments
	  */
	 public static final String ASSESSED = "A";
	 /**
	  * Status PUBLISHED used when invoices have been approved legal
	  */
	 public static final String PUBLISHED = "P";
	 /**
	  * Status SENT when invoices have been sent out of system boundary for processing in other systems
	  */
	 public static final String SENT = "S";
	 /**
	  * Status RECEIVED when invoices have been received from other system processing
	  */
	 public static final String RECEIVED = "R";
}
