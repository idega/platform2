/*
 * Created on 5.7.2004
 */
package is.idega.idegaweb.golf.startingtime.business;

import is.idega.idegaweb.golf.GolfField;

import java.util.List;

import com.idega.business.IBOSessionBean;

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
	private String date = null;
	private int sublistSize = 6;
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
	
	public void cachResult(List result, GolfField info,String date){
		this.result = result;
		this.info = info;
		this.date = date;
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
		index -= (2*sublistSize);
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
	
	public String getDate(){
		return date;
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
