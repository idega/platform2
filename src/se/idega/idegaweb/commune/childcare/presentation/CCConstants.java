package se.idega.idegaweb.commune.childcare.presentation;

/**
 * enclosing_type
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: CCConstants.java,v 1.7 2003/05/28 18:16:18 roar Exp $
 * @since 14.2.2003 
 */
interface CCConstants {

//Test for gummi 
	final static String YES = "1";   
	final static String NO_NEW_DATE = "2";
	final static String NO = "3";	
	
	final static String[] OK = {"cc_ok", "OK"};	
	final static String[] CANCEL = {"cc_cancel", "Cancel"};	
		
	final static String[] TEXT_OFFER_ACCEPTED_SUBJECT = 
		new String[]{"cc_oas", "Offer accepted"};
	final static String[] TEXT_OFFER_ACCEPTED_MESSAGE = 
		new String[]{"cc_oam", "Your offer were accepted."};	
		final static String[] TEXT_OFFER_REJECTED_SUBJECT = 
	new String[]{"cc_oas", "Offer rejected"};
	final static String[] TEXT_OFFER_REJECTED_MESSAGE = 
	new String[]{"cc_oam", "Your offer were rejected."};
	
	final static String[] TEXT_DETAILS = 
		new String[]{"cc_det", "Details"};
	final static String[] TEXT_CUSTOMER = 
		new String[]{"cc_cust", "Customer"};
	final static String[] TEXT_CHILD = 
		new String[]{"cc_child", "Child"};
	final static String[] TEXT_FROM = 
		new String[]{"cc_from", "From"};
		
	final static String APPID = "APPID";
	final static String PROVIDER_ID = "PROVIDER_ID";
	final static String USER_ID = "USER_ID";
	final static String ACCEPT_OFFER = "ACCEPT_OFFER";	
	final static String KEEP_IN_QUEUE = "KEEP_IN_QUEUE";
	final static String NEW_DATE = "NEW_DATE";

	
	final static String SESSION_ACCEPTED_STATUS = "SESSION_ACCEPTED_STATUS";
	final static String SESSION_KEEP_IN_QUEUE = "SESSION_KEEP_IN_QUEUE";
	
	final static String ACTION = "ACTION";
		
	final static int NO_ACTION = 0;	
	final static int ACTION_SUBMIT_1 = 1;	
	final static int ACTION_CANCEL_1 = 2;	
	final static int ACTION_SUBMIT_CONFIRM = 3;
	final static int ACTION_SUBMIT_2 = 4;	
	final static int ACTION_CANCEL_2 = 5;	
//	final static int ACTION_SUBMIT_3 = 6;	
	final static int ACTION_CANCEL_3 = 7;		
	final static int ACTION_REQUEST_INFO = 8;
	final static int ACTION_DELETE = 9;

}
