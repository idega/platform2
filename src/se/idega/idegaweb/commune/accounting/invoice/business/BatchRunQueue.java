package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

/**
 * Class to facilitate that batchruns can be executed one after another. 
 * 
 * @author Joakim
 */
public class BatchRunQueue {
	//The queue holding the list of batch runs waiing to get executed. Holds objects of type BatchRunObject
	private static ArrayList queue = new ArrayList();
	
	private static Logger log = Logger.getLogger("BatchRunQueue");

	/**
	 * Call this funcion to add a batchrun to the queue of batchruns to be executed
	 * All batches are executed one at a time and in the order they were added.
	 * 
	 * @param month
	 * @param readDate
	 * @param schoolCategory
	 * @param iwc
	 * @throws SchoolCategoryNotFoundException
	 */
	public static void addBatchRunToQueue(Date month, Date readDate, String schoolCategory, IWContext iwc) throws SchoolCategoryNotFoundException{
		BatchRunObject batchRunObject = new BatchRunObject(month, readDate, schoolCategory, iwc);
		queue.add(batchRunObject);
		log.info("Added "+batchRunObject+" to queue at place "+(queue.size()-1));
		if(queue.size()==1){
			try {
				log.info("About to start "+batchRunObject);
				startBatch(batchRunObject);
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (BatchAlreadyRunningException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calling this lets the queue know that it is possible to start another batch
	 */
	public static void BatchRunDone(){
		queue.remove(0);
		log.info("Done with one batchrun");
		if(queue.size()>0){
			try {
				log.info("About to start "+queue.get(0));
				startBatch((BatchRunObject)queue.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts a new batchrun
	 * 
	 * @param batchRunObject
	 * @throws FinderException
	 * @throws BatchAlreadyRunningException
	 * @throws SchoolCategoryNotFoundException
	 * @throws IDOLookupException
	 */
	private static void startBatch(BatchRunObject batchRunObject) throws FinderException, BatchAlreadyRunningException, SchoolCategoryNotFoundException, IDOLookupException{
		//Select correct thread to start
		SchoolCategoryHome sch = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		if (sch.findChildcareCategory().getCategory().equals(batchRunObject.schoolCategory)) {
			if(BatchRunSemaphore.getChildcareRunSemaphore()){
				new InvoiceChildcareThread(batchRunObject.month, batchRunObject.iwc).start();
			}else{
				throw new BatchAlreadyRunningException("Childcare");
			}
		} else if (sch.findElementarySchoolCategory().getCategory().equals(batchRunObject.schoolCategory)) {
			if(BatchRunSemaphore.getElementaryRunSemaphore()){
				new PaymentThreadElementarySchool(batchRunObject.month, batchRunObject.iwc).start();
			}else{
				throw new BatchAlreadyRunningException("ElementarySchool");
			}
		} else if (sch.findHighSchoolCategory().getCategory().equals(batchRunObject.schoolCategory)) {
			if(BatchRunSemaphore.getHighRunSemaphore()){
				new PaymentThreadHighSchool(batchRunObject.readDate, batchRunObject.iwc).start();
			}else{
				throw new BatchAlreadyRunningException("HighSchool");
			}
		} else {
			log.warning("Error: couldn't find any Schoolcategory for billing named " + batchRunObject.schoolCategory);
			throw new SchoolCategoryNotFoundException("Couldn't find any Schoolcategory for billing named " + batchRunObject.schoolCategory);
		}
	}
	
	public static Iterator iterator(){
		return queue.iterator();
	}
	
	public static class BatchRunObject{
		Date month;
		Date readDate;
		String schoolCategory;
		IWContext iwc;
		
		public BatchRunObject(Date month, Date readDate, String schoolCategory, IWContext iwc){
			this.month = month;
			this.readDate = readDate;
			this.schoolCategory = schoolCategory;
			this.iwc = iwc;
		}
		
		public String toString(){
			IWTimestamp m = new IWTimestamp(month);
			return schoolCategory+" "+m.getDateString("MMM yyyy");
		}
	}
}
