/*
 * Created on Jul 7, 2003
 *
 */
package com.idega.block.dataquery.presentation;

import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TreeViewerSelection;

/**
 * The <code> QuerySelector </code> is used to choose
 * a query from a folder and fill in dynamic query fields if a 
 * dynamic template is chosen
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QuerySelector extends Block {
	private IWBundle iwb = null;
	private IWResourceBundle iwrb = null;
	private static final String IWBUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	private boolean hasPermission = false;
	private Image templateIcon = null;
	private Image staticIcon = null;
	private Image userIcon = null;
	private ICFile templateFolder = null;
	private ICFile staticFolder = null;
	private ICFile userFolder = null;
	private ParameterEngine pengine =null;
	private int queryID = -1;
	
	private static String PRM_QUERY_ID = "dqs_qid";
	
	
	public QuerySelector(){
		pengine = new ParameterEngine("qrysel");
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IWBUNDLE_IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		hasPermission = hasEditPermission();
		initiate(iwc);
		presentate(iwc);
	}
	
	public void initiate(IWContext iwc)throws Exception{
		pengine.parse(iwc);
	}
	
	public PresentationObject presentFolderQueries(ICFile folder){
		
		
		TreeViewerSelection tree = new TreeViewerSelection();
		tree.setSelectionKey(PRM_QUERY_ID);
		tree.setSelectedNode(queryID);
		if(folder!=null){
		tree.setRootNode(folder);
		tree.setDefaultOpenLevel(1);
		
		tree.setSelectionKey("fo_"+folder.getPrimaryKey().toString());
		}
	
		return tree;
		
	}
	
	public PresentationObject presentFolders(IWContext iwc){
		Table T = new Table();
		int col = 1;
		if(userFolder!=null){
			if(userIcon!=null)
				T.add(userIcon,col,1);
			T.add(iwrb.getLocalizedString("qsel.my_queries","My queries"),col,1);
			T.add(presentFolderQueries(userFolder),col,2);
			col++;
		}
		if(templateFolder!=null){
			if(templateIcon!=null)
				T.add(templateIcon,col,1);
			T.add(iwrb.getLocalizedString("qsel.template_queries","Template queries"),col,1);
			T.add(presentFolderQueries(templateFolder),col,2);
			col++;
		}
		if(staticFolder!=null){
			if(staticIcon!=null)
				T.add(templateIcon,col,1);
			T.add(iwrb.getLocalizedString("qsel.static_queries","Static queries"),col,1);
			T.add(presentFolderQueries(templateFolder),col,2);
			col++;
		}
		
		return T;
	}
	
	public void presentate(IWContext iwc){
		add(presentFolders(iwc));
	
	}
	
	public void proccess(IWContext iwc){
		
	}
	
	

	/**
	 * @return
	 */
	public ICFile getStaticFolder() {
		return staticFolder;
	}

	/**
	 * @return
	 */
	public Image getStaticIcon() {
		return staticIcon;
	}

	/**
	 * @return
	 */
	public ICFile getTemplateFolder() {
		return templateFolder;
	}

	/**
	 * @return
	 */
	public Image getTemplateIcon() {
		return templateIcon;
	}

	/**
	 * @return
	 */
	public ICFile getUserFolder() {
		return userFolder;
	}

	/**
	 * @return
	 */
	public Image getUserIcon() {
		return userIcon;
	}

	/**
	 * @param file
	 */
	public void setStaticFolder(ICFile file) {
		staticFolder = file;
	}

	/**
	 * @param image
	 */
	public void setStaticIcon(Image image) {
		staticIcon = image;
	}

	/**
	 * @param file
	 */
	public void setTemplateFolder(ICFile file) {
		templateFolder = file;
	}

	/**
	 * @param image
	 */
	public void setTemplateIcon(Image image) {
		templateIcon = image;
	}

	/**
	 * @param file
	 */
	public void setUserFolder(ICFile file) {
		userFolder = file;
	}

	/**
	 * @param image
	 */
	public void setUserIcon(Image image) {
		userIcon = image;
	}

}

