/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.block.forum.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.block.forum.business.CommuneForumBusiness;

import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryHome;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
// TODO extend IWAdminWindow
public class CommuneForumTopicFiles extends Window {

	protected final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";
	
	public static String prmTopicId = "cm_f_tf_i";
	private String prmAddFil = "cm_f_af";
	private String prmFileId = "cm_f_fi_d";
	private String prmRmFil = "cm_f_rf";
	private String prmClose = "cm_f_pr_mc";
	private ICCategory cat;
	private int catId;
	private boolean canEdit = false;
	private IWResourceBundle iwrb;
	private Collection files;
	private ICFileHome fileHome;
	
	public CommuneForumTopicFiles() {
		super("Files",400,200);
		super.setTitle("Topic attachments");
		setResizable(true);
	}
	
	private void init(IWContext iwc) throws RemoteException {
		iwrb = getResourceBundle(iwc);
		String sCatId = iwc.getParameter(prmTopicId);
		if (sCatId != null) {
			try {
				fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
				ICCategoryHome cHome = (ICCategoryHome) IDOLookup.getHome(ICCategory.class);
				cat = cHome.findByPrimaryKey(new Integer(sCatId));
				catId = Integer.parseInt(sCatId);
				canEdit = getCommuneForumBusiness(iwc).isModerator(cat, iwc.getCurrentUser());
				if (!canEdit) {
					canEdit = iwc.hasEditPermission(this);	
				}
			}catch (IDOLookupException e) {
				e.printStackTrace(System.err);
			}catch (FinderException e) {
				e.printStackTrace(System.err);
			} 
		}
	}
	
	public void main(IWContext iwc) throws RemoteException{
		init(iwc);

		boolean addList = true;

		if (iwc.isParameterSet(prmClose)) {
			super.setParentToReload();
			this.close();	
		}

		String sDelFile = iwc.getParameter(prmRmFil);
		if (sDelFile != null) {
			if (!removeFile(Integer.parseInt(sDelFile))) {
				add(iwrb.getLocalizedString("could_not_remove_file","Could not remove file"));
			}	
		}
		
		String sAddFile = iwc.getParameter(prmAddFil);
		if (sAddFile != null) {
			addList = false;
			insertFileForm(iwc);
		}
		
		String sFileId = iwc.getParameter(prmFileId);
		if (sFileId != null) {
			if (!addFile(Integer.parseInt(sFileId))) {
				add(iwrb.getLocalizedString("could_not_add_file","Could not add file"));
			}
		}
		
		if (addList && cat != null) {
			try {
				files = cat.getFiles();	// done here, do not move to init...
			} catch (IDORelationshipException e) {
				e.printStackTrace(System.err);
			} 
			printList(iwc);
		}else if (cat == null){
			add(iwrb.getLocalizedString("topic_not_found","Topic not found"));	
		}
	}
	
	public void insertFileForm(IWContext iwc) {
		Form form = new Form();
		String prmSubmitted = "prm_sbm_tf";
		String sSub = iwc.getParameter(prmSubmitted);
		
		form.add(new HiddenInput(prmTopicId, Integer.toString(catId)));
		SimpleFileChooser sfc = new SimpleFileChooser(form, prmFileId);
		form.add(sfc);

		if (sSub == null) {
			form.add(new HiddenInput(prmSubmitted, "true"));	
			form.add(new HiddenInput(prmAddFil, "true"));
		}else {
			form.add(Text.BREAK);
			form.add(new SubmitButton(iwrb.getLocalizedImageButton("submit","Submit")));	
		}

		add(form);

	}
	
	private boolean addFile(int fileId) {
		try {
			ICFile file = fileHome.findByPrimaryKey(new Integer(fileId));
			cat.addFile(file);
			return true;
		}catch (Exception e) {
			e.printStackTrace(System.err);
			return false;	
		}
	}
	
	private boolean removeFile(int fileId) {
		try {
			ICFile file = fileHome.findByPrimaryKey(new Integer(fileId));
			cat.removeFile(file);
			file.remove();
			return true;
		}catch (Exception e) {
			e.printStackTrace(System.err);
			return false;	
		}	
	}
	
	private void printList(IWContext iwc) {
		Text header = new Text(iwrb.getLocalizedString("topic_files","Topic files")+" :");
		header.setBold(true);
		Table t = new Table();
		int row = 1;
		t.add(header, 1, row);
		++row;
		
		if (files == null || files.isEmpty()) {
			t.add(new Text(iwrb.getLocalizedString("no_files_attached_to_topic","No files are attached to this topic")), 1, row++);
		}	else {
			Iterator iter = files.iterator();
			ICFile file;
			Link link;
			while (iter.hasNext()) {
				try {
					file = fileHome.findByPrimaryKey(iter.next());
					Link preview = new Link(new Text(file.getName()));
					preview.setURL(MediaBusiness.getMediaURL(file,iwc.getIWMainApplication()));
					preview.setTarget(Link.TARGET_NEW_WINDOW);
					t.add(preview, 1, row);

					if (canEdit) {
						link = getLink(iwrb.getLocalizedImageButton("delete", "delete"));
						link.addParameter(prmRmFil, file.getPrimaryKey().toString());
						t.add(link, 2, row);
					}
					++row;
				} catch (FinderException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		++row;
		if (canEdit) {
			Link link = getLink(iwrb.getLocalizedImageButton("add_file", "Add file"));
			link.addParameter(prmAddFil, "true");
			t.add(link, 1, row);
		}
		Link close = getLink(iwrb.getLocalizedImageButton("close", "Close"));
		close.addParameter(prmClose, "true");
		t.add(new Text(Text.NON_BREAKING_SPACE), 1, row);
		t.add(close, 1, row);
		add(t);

	}
	
	private Link getLink(PresentationObject obj) {
		Link link = new Link(obj);
			link.addParameter(prmTopicId, catId);
		return link;	
	}
	
	protected CommuneForumBusiness getCommuneForumBusiness(IWContext iwc) throws RemoteException {
		return (CommuneForumBusiness) IBOLookup.getServiceInstance(iwc, CommuneForumBusiness.class);
	}		
	
}
