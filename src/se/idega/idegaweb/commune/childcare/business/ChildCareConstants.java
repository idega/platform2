/*
 * $Id: ChildCareConstants.java,v 1.1 2004/09/09 13:25:20 aron Exp $
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
 *  Last modified: $Date: 2004/09/09 13:25:20 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareConstants {
    public final static char STATUS_ACCEPTED = 'C';
	public final static char STATUS_CANCELLED = 'V';
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
}
