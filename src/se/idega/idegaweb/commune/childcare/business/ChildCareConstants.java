/*
 * $Id: ChildCareConstants.java,v 1.8 2005/02/04 09:08:26 laddi Exp $
 * Created on 9.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;


/**
 * 
 *  Last modified: $Date: 2005/02/04 09:08:26 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.8 $
 */
public class ChildCareConstants {
  
	public final static char STATUS_ACCEPTED = 'C';
	public final static char STATUS_CANCELLED = 'V';
	public final static char STATUS_PARENT_TERMINATED = 'R';
	public final static char STATUS_WAITING = 'P';
	public final static char STATUS_CONTRACT = 'E';
	public final static char STATUS_DELETED = 'T';
	public final static char STATUS_DENIED = 'U';
	public final static char STATUS_MOVED = 'W';
	public final static char STATUS_NEW_CHOICE = 'X';
	public final static char STATUS_NOT_ANSWERED = 'Y';
	public final static char STATUS_PARENTS_ACCEPT = 'D';
	public final static char STATUS_PRIORITY = 'B';
	public final static char STATUS_READY = 'F';
	public final static char STATUS_REJECTED = 'Z';
	public final static char STATUS_SENT_IN = 'A';
	public final static char STATUS_TIMED_OUT = 'S';
	
	public final static String CLEAN_QUEUE_RUNNING = "clean_queue_running";
	
	public static final String PARAMETER_CHILD_ID = "ccc_child_id";
  public static final String PROPERTIES_CHILD_CARE = "child_care";
  public static final String PROPERTY_MAX_MONTHS_IN_QUEUE = "max_months_in_queue";
  public static final String PROPERTY_DAYS_TO_REPLY = "days_to_reply";
  public static final String PROPERTY_CLEAN_WITHOUT_PLACEMENT = "clean_children_without_placement";	
}