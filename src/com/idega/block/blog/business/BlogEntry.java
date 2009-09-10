/*
 * Created on 19.2.2004
 */
package com.idega.block.blog.business;

import com.idega.block.blog.data.BlogEntity;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOEntity;

/**
 * Title:		BlogEntry
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class BlogEntry extends PresentableData {
	
	private static final String KEY_BLOG = "Blog body";
	private static final String KEY_TITLE = "Blog title";
	private static final String KEY_AUTHOR = BlogEntity.UNIQUE_ENTITY_NAME+BlogEntity.UFN_AUTHOR;
	private static final String KEY_SOURCE = BlogEntity.UNIQUE_ENTITY_NAME+BlogEntity.UFN_SOURCE;
	private static final String KEY_FOLDER = BlogEntity.UNIQUE_ENTITY_NAME+BlogEntity.UFN_FOLDER;
	private static final String KEY_CATEGORY = BlogEntity.UNIQUE_ENTITY_NAME+BlogEntity.UFN_CATEGORY;

	private String blogPrimaryKey = null;

	public BlogEntry(ICLocale locale){
		super(locale);
	}
	
	public void load(IDOEntity entry){
		if(entry instanceof BlogEntity){
			load((BlogEntity)entry);
		} else {
			throw new RuntimeException("["+this.getClass().getName()+"]: this PresentableData object does not handle "+entry.getClass().getName());
		}
	}
	
	public void load(BlogEntity entry){
		this.blogPrimaryKey = entry.getPrimaryKey().toString();
		
		setAuthor(entry.getAuthor());
		setSource(entry.getSource());
//		setAuthor(entry.getAuthor(getLocale()));
//		setSource(entry.getSource(getLocale()));
		
//		setTitle(entry.getTitle(getLocale()));
//		setBlog(entry.getBlog(getLocale()));

		IDOEntity folder = entry.getBlogFolder();
		if(folder != null) {
			setBlogFolderPrimaryKey(folder.getPrimaryKey().toString());
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.block.blog.business.DataEntryWrapper#getPrimaryKey()
	 */
	public Object getPrimaryKey() {
		if(this.blogPrimaryKey != null){
			return this.blogPrimaryKey;
		} else {
			return null;
		}
		
	}
	
	
	//BlogEntity related begins


	public String getBlog(){
		return getData(KEY_BLOG);
	}
	public String getTitle(){
		return getData(KEY_TITLE);
	}
	public String getAuthor(){
		return getData(KEY_AUTHOR);
	}
	public String getBlogFolderPrimaryKey(){
		return getData(KEY_FOLDER);
	}
	public String getBlogCategoryPrimaryKey(){
		return getData(KEY_CATEGORY);
	}
	public String getSource(){
		return getData(KEY_SOURCE);
	}


	public void setBlog(String blog){
		setData(KEY_BLOG,blog);
	}
	public void setTitle(String title){
		setData(KEY_TITLE, title);
	}
	public void setAuthor(String author){
		setData(KEY_AUTHOR,author);
	}
	public void setBlogFolderPrimaryKey(String pk){
		setData(KEY_FOLDER, pk);
	}	
	public void setBlogCategoryPrimaryKey(String pk){
		setData(KEY_CATEGORY, pk);
	}
	public void setSource(String source){
		setData(KEY_SOURCE, source);
	}
	
	//BlogEntity related ends
	

}
