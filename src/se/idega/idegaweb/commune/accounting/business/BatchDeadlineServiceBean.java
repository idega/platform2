/*
 * $Id: BatchDeadlineServiceBean.java,v 1.3 2004/11/25 12:05:08 aron Exp $
 * Created on 12.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.business;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadline;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineHome;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.TimePeriod;

/**
 * 
 *  Last modified: $Date: 2004/11/25 12:05:08 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.3 $
 */
public class BatchDeadlineServiceBean extends IBOServiceBean  implements BatchDeadlineService{
    
    /**
     * Gets a valid timeperiod for the current deadline
     * If the deadline is set to the 10th and
     * today is the 9th the period will be from
     * 1.mm.yyyy to 10.mm.yyyy where mm is current month and yyyy is current year
     * if today is 11th the period will be from 
     * 1.mm.yyyy to dd.mm.yyyy where dd is last day of next month, mm is next month, 
     * yyyy is the year the next month is in. 
     * @return Timeperiod, null if no current deadline is set
     */
    public TimePeriod getValidPeriod(){
        BatchDeadline deadline = getCurrentDeadline();
        if(deadline!=null){
            IWCalendar cal = new IWCalendar();
            int today = cal.getDay();
            int deadlineDay = deadline.getDeadlineDay();
            if(today<=deadlineDay ){
                return new TimePeriod(new IWTimestamp(1,cal.getMonth(),cal.getYear()),new IWTimestamp(deadlineDay,cal.getMonth(),cal.getYear()));
            }
            else{
              IWTimestamp stamp = new IWTimestamp();
              stamp.addMonths(1);
              return new TimePeriod(new IWTimestamp(1,stamp.getMonth(),stamp.getYear()),new IWTimestamp(cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear()),stamp.getMonth(),stamp.getYear()));
                
            }
            
        }
        return null;
    }
    
    /**
     * Check if today's date has passed the current deadline set
     * @return
     */
    public boolean hasDeadlinePassed(){
        BatchDeadline deadline = getCurrentDeadline();
        IWTimestamp today = new IWTimestamp();
        if(deadline!=null && today.getDay()<=deadline.getDeadlineDay())
            return false;
        return true;
    }
    
    /**
     * Gets the day of the current deadline, -1 if no current deadline is set.
     * @return
     */
    public int getCurrentDeadlineDay(){
        try {
            return getCurrentDeadline().getDeadlineDay();
        }
        catch(NullPointerException e){
          e.printStackTrace();   
        	}
        return -1;
    }
    
    /**
     * Gets the deadline marked as current null if nothing found
     * @return
     */
    public BatchDeadline getCurrentDeadline(){
        try {
            BatchDeadlineHome deadlineHome = (BatchDeadlineHome)IDOLookup.getHome(BatchDeadline.class);
            return deadlineHome.findCurrent();
        } catch (IDOLookupException e) {
            e.printStackTrace();
        } catch (FinderException e) {
           
        }
        return null;
    }
    
    /**
     * 
     * @param deadlineDay
     * @return
     */
    public BatchDeadline storeDeadline(int deadlineDay){
	    try {
	        BatchDeadlineHome deadlineHome = (BatchDeadlineHome)IDOLookup.getHome(BatchDeadline.class);
	        
            BatchDeadline deadLine = deadlineHome.create();
            deadLine.setDeadlineDay(deadlineDay);
            deadLine.setIsCurrent(true);
            deadLine.store();
            return deadLine;
	      
	    } catch (IDOLookupException e) {
	        e.printStackTrace();
	    }catch (CreateException e) {
            e.printStackTrace();
        }
	    return  null;
    }

}
