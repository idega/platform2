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
			System.out.println("Elementary semaphore refused");
			return false;
		}else{
			elementaryRunFlag = true;
			System.out.println("Elementary semaphore gotten");
			return true;
		}
	}
	
	public static void releaseElementaryRunSemaphore(){
		System.out.println("Elementary semaphore released");
		elementaryRunFlag = false;
	}
	
	private static boolean highRunFlag = false;
	public static synchronized boolean getHighRunSemaphore(){
		if(highRunFlag){
			System.out.println("Highschool semaphore refused");
			return false;
		}else{
			highRunFlag = true;
			System.out.println("Highschool semaphore gotten");
			return true;
		}
	}
	
	public static void releaseHighRunSemaphore(){
		System.out.println("Highschool semaphore released");
		highRunFlag = false;
	}
	

	private static boolean adultRunFlag = false;
	public static synchronized boolean getAdultRunSemaphore(){
		if(adultRunFlag){
			System.out.println("Adult education semaphore refused");
			return false;
		}else{
			adultRunFlag = true;
			System.out.println("Adult education semaphore gotten");
			return true;
		}
	}
	
	public static void releaseAdultRunSemaphore(){
		System.out.println("Adult education semaphore released");
		adultRunFlag = false;
	}

	/**
	 * @return Returns the childcareRunFlag.
	 */
	public static boolean isChildcareRunFlag() {
		return childcareRunFlag;
	}

	/**
	 * @return Returns the elementaryRunFlag.
	 */
	public static boolean isElementaryRunFlag() {
		return elementaryRunFlag;
	}

	/**
	 * @return Returns the highRunFlag.
	 */
	public static boolean isHighRunFlag() {
		return highRunFlag;
	}

	/**
	 * @return Returns the highRunFlag.
	 */
	public static boolean isAdultRunFlag() {
		return adultRunFlag;
	}	
}