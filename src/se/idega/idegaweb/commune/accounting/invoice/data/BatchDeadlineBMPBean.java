/*
 * $Id: BatchDeadlineBMPBean.java,v 1.2 2005/02/17 11:10:18 anders Exp $
 * Created on 1.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2005/02/17 11:10:18 $ by $Author: anders $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public class BatchDeadlineBMPBean extends GenericEntity  implements BatchDeadline{
    
    private final static String DEADLINE_DAY = "deadline_day";
    private final static String CREATED = "creation_date";
    private final static String CURRENT = "is_current";

    /* (non-Javadoc)
     * @see com.idega.data.GenericEntity#getEntityName()
     */
    public String getEntityName() {
        return "CACC_BATCH_DEADLINE";
    }

    /* (non-Javadoc)
     * @see com.idega.data.GenericEntity#initializeAttributes()
     */
    public void initializeAttributes() {
        addAttribute(getIDColumnName());
        addAttribute(DEADLINE_DAY, "", true, true, Integer.class);
        addAttribute(CREATED, "", true, true, java.sql.Timestamp.class);
        addAttribute(CURRENT, "", true, true, Boolean.class);
        addManyToManyRelationShip(BatchRun.class,"CACC_BATCH_DEADLINE_HISTORY");
    }
    
    public void setDeadlineDay(int day){
        setColumn(DEADLINE_DAY,day);
    }
    
    public int getDeadlineDay(){
        return getIntColumnValue(DEADLINE_DAY);
    }
    
   
    public Timestamp getCreated(){
        return getTimestampColumnValue(CREATED);
    }
    
    public void setCreated(Timestamp created){
        setColumn(CREATED,created);
    }
    
    public boolean isCurrent(){
        return getBooleanColumnValue(CURRENT);
    }
    
    public void setIsCurrent(boolean flag){
        setColumn(CURRENT,flag);
    }
    
    
    /* (non-Javadoc)
     * @see com.idega.data.GenericEntity#store()
     */
    public void store() throws IDOStoreException {
    		if (getCreated() == null) {
    			setCreated(IWTimestamp.RightNow().getTimestamp());
    		}
        if(isCurrent()){
	        try {
	            BatchDeadlineHome bdHome = (BatchDeadlineHome)IDOLookup.getHome(BatchDeadline.class);
	            Collection currents = bdHome.findAllCurrent();
	            for (Iterator iter = currents.iterator(); iter.hasNext();) {
	                BatchDeadline element = (BatchDeadline) iter.next();
	                element.setIsCurrent(false);
	                element.store();
	            }
	        } catch (IDOLookupException e) {
	            e.printStackTrace();
	        } catch (FinderException e) {
	        
	        }
        }
        super.store();
    }
    
    
    
    public Collection ejbFindAllCurrent()throws FinderException{
        Table table = new Table(this,"a");
        SelectQuery query = new SelectQuery(table);
        query.addColumn(new WildCardColumn());
        query.addCriteria(new MatchCriteria(table,CURRENT,MatchCriteria.EQUALS,true));
        return idoFindPKsByQuery(query);
    }
    
    public Object ejbFindCurrent()throws FinderException{
        Table table = new Table(this,"a");
        SelectQuery query = new SelectQuery(table);
        query.addColumn(new WildCardColumn());
        query.addCriteria(new MatchCriteria(table,CURRENT,MatchCriteria.EQUALS,true));
        return idoFindOnePKByQuery(query);
    }
    
    public void addBatch(BatchRun batchRun) throws IDOAddRelationshipException{
        idoAddTo(batchRun);
    }

}
