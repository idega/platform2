/*
 * Created on Jul 23, 2003
 *
 */
package is.idega.idegaweb.campus.block.allocation.business;

import java.util.Date;

/**
 * Period
 * @author aron 
 * @version 1.0
 */
public class Period {
	
	private Date from;
	private Date to;
	
	public Period(){
		from = new Date();
		to = new Date();
	}
	
	public Period(Date from,Date to){
		this.from = from;
		this.to = to;
	}
	
	
	/**
	 * @return
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @return
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param date
	 */
	public void setFrom(Date date) {
		from = date;
	}

	/**
	 * @param date
	 */
	public void setTo(Date date) {
		to = date;
	}

}
