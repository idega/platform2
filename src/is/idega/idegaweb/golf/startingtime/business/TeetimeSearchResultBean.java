/*
 * Created on 5.7.2004
 */
package is.idega.idegaweb.golf.startingtime.business;

import is.idega.idegaweb.golf.GolfField;

import java.util.List;

import com.idega.business.IBOSessionBean;
import com.idega.util.IWTimestamp;

/**
 * Title: TeetimeSearchResult
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class TeetimeSearchResultBean extends IBOSessionBean implements TeetimeSearchResult{

	
	private List result = null;
	private GolfField info = null;
	private IWTimestamp date = null;
	private IWTimestamp firstTime = null;
	private IWTimestamp lastTime = null;
	private int numberOfPlayers = 1;
	private int sublistSize = 100;
	private int index = 0;
	
	/**
	 * 
	 */
	public TeetimeSearchResultBean() {
		super();
	}

	public void setSublistSize(int size){
		if(size>1){
			sublistSize = size;
		} else {
			sublistSize=1;
		}
	}
	
	public void cachResult(List result, GolfField info,IWTimestamp date,IWTimestamp firstTime,IWTimestamp lastTime,int numberOfPlayers){
		this.result = result;
		this.info = info;
		this.date = date;
		this.firstTime = firstTime;
		this.lastTime = lastTime;
		this.numberOfPlayers = numberOfPlayers;
		index = -sublistSize;
	}
	
	public void throwCach(){
		result = null;
		info = null;
		date = null;
		index = 0;
	}
	
	
	public List next(){
		index += sublistSize;
		return current();
	}
	
	public List current(){
		if(index < 0){
			index=0;
		}
		return result.subList(index,Math.min((index+sublistSize),result.size()));
	}
	
	public List prev(){
		index -= (sublistSize);
		return current();
	}
	
	public int getResultSize(){
		if(result == null){
			return 0;
		} else {
			return result.size();
		}
	}
	
	public GolfField getFieldInfo(){
		return info;
	}
	
	public IWTimestamp getDate(){
		return date;
	}
	
	public IWTimestamp getFirstTime(){
		return firstTime;
	}
	
	public IWTimestamp getLastTime(){
		return lastTime;
	}
	
	public int getNumberOfPlayers(){
		return numberOfPlayers;
	}
	
	public boolean isInitialized(){
		return (result != null)&&(info != null)&&(date != null);
	}
	
	public boolean hasNext(){
		return getResultSize() > (index+sublistSize);
	}
	
	public boolean hasPrevious(){
		return index > 0;
	}
}
