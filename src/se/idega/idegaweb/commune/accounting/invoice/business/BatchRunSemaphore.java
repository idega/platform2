package se.idega.idegaweb.commune.accounting.invoice.business;

/**
 * @author Joakim
 *
 */
public class BatchRunSemaphore {

	private static boolean childcareRunFlag = false;
	public static synchronized boolean getChildcareRunSemaphore(){
		if(childcareRunFlag){
			System.out.println("Childcare semaphore refused");
			return false;
		}else{
			childcareRunFlag = true;
			System.out.println("Childcare semaphore gotten");
			return true;
		}
	}
	
	public static void releaseChildcareRunSemaphore(){
		System.out.println("Childcare semaphore released");
		childcareRunFlag = false;
	}
	
	private static boolean elementaryRunFlag = false;
	public static synchronized boolean getElementaryRunSemaphore(){
		if(elementaryRunFlag){
			return false;
		}else{
			elementaryRunFlag = true;
			return true;
		}
	}
	
	public static void releaseElementaryRunSemaphore(){
		elementaryRunFlag = false;
	}
	
	private static boolean highRunFlag = false;
	public static synchronized boolean getHighRunSemaphore(){
		if(highRunFlag){
			return false;
		}else{
			highRunFlag = true;
			return true;
		}
	}
	
	public static void releaseHighRunSemaphore(){
		highRunFlag = false;
	}
	
}
