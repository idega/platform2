/*
 * $Id: BatchDeadlineSetter.java,v 1.3 2004/11/26 13:41:44 aron Exp $
 * Created on 1.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.accounting.business.BatchDeadlineService;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2004/11/26 13:41:44 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.3 $
 */
public class BatchDeadlineSetter extends AccountingBlock {

    /* (non-Javadoc)
     * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
     */
    public void init(IWContext iwc) throws Exception {
        
        Form form = new Form();
        int deadlineDay = -1;
        IWTimestamp stamp = IWTimestamp.RightNow();
        int currentDay = stamp.getDay();
        IWCalendar cal = new IWCalendar();
        int daysInMonth = cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
        
        if(iwc.isParameterSet("deadline_day")){
            deadlineDay = Integer.parseInt(iwc.getParameter("deadline_day"));
            getDeadlineService(iwc).storeDeadline(deadlineDay);
        }
      
        
        // still valid this month
        stamp.setDay(deadlineDay);
        if(deadlineDay < currentDay){
            stamp.addMonths(1);
        }
        
        String date = getShortDateFormat(iwc.getCurrentLocale()).format(stamp.getDate());
        Text currentDeadlineText = getHeader(localize("batchdeadline.current_deadline", "Current deadline")+" : "+date);
        
        Text setDeadlineToText = getHeader(localize("batchdeadline.set_deadline_to","Set deadline to"));
       
        DropdownMenu days = (DropdownMenu)getStyledInterface(new DropdownMenu("deadline_day"));
        days.addMenuElement(localize("batchdeadline.day","D"));
        for (int i = 1; i <= daysInMonth; i++) {
            days.addMenuElement(String.valueOf(i));
        }
        
        form.add(currentDeadlineText);
        form.add(new Break());
        form.add(setDeadlineToText);
        form.add(Text.getNonBrakingSpace());
        
        form.add(days);
        form.add(Text.getNonBrakingSpace());
        
        form.add( getButton(new SubmitButton("save_dld",localize("batchdeadline.save_date","Save date"))));
        
        
        add(form);
    }
    
    public BatchDeadlineService getDeadlineService(IWContext iwc)throws RemoteException{
        return (BatchDeadlineService)IBOLookup.getServiceInstance(iwc, BatchDeadlineService.class);
    }
    
    

}
