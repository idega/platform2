package com.idega.block.email.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.email.business.EmailLetter;
import com.idega.block.email.business.EmailTopic;
import com.idega.block.email.business.MailBusiness;
import com.idega.block.email.business.MailFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.text.TextSoap;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class NewsLetterArchive extends CategoryBlock {

	private int group = -1;
	private int topic = -1;
	private int letter = -1;

	private String prmLetter = "em_nla_let";
	private String prmTopic = "em_nla_tpc";
	private String prmLetterDelete = "em_nla_ldel";

	private IWBundle iwb, core;
	private IWResourceBundle iwrb;

	private String LinkStyle = "";
	private String InfoStyle = "";

	private Collection topics;
	private Collection groups;
	private Collection letters;

	private EmailLetter theLetter;
	private DateFormat df;

	public static String EMAIL_BUNDLE_IDENTIFIER = "com.idega.block.email";

	public NewsLetterArchive() {
		setAutoCreate(false);
	}
	public String getCategoryType() {
		return "Newsletter";
	}
	public boolean getMultible() {
		return true;
	}

	public String getBundleIdentifier() {
		return EMAIL_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		iwb = getBundle(iwc);
		core = iwc.getIWMainApplication().getCoreBundle();
		iwrb = getResourceBundle(iwc);
		Table T = new Table();
		int row = 1;
		df = DateFormat.getDateInstance(DateFormat.LONG, iwc.getCurrentLocale());

		if (iwc.isParameterSet(prmTopic)) {
			topic = Integer.parseInt(iwc.getParameter(prmTopic));
		}
		if (iwc.isParameterSet(prmLetter)) {
			letter = Integer.parseInt(iwc.getParameter(prmLetter));
		}
		
		if(iwc.isParameterSet(prmLetterDelete) && letter >0){
			MailBusiness.getInstance().deleteLetter(letter);
		}

		if (getCategoryId() > 0) {
			topics = MailFinder.getInstance().getTopics(this.getICObjectInstanceID());
			if (topic > 0)
				letters = MailFinder.getInstance().getEmailLetters(topic);

		}
		if (iwc.hasEditPermission(this)) {
			T.add(getAdminView(iwc), 1, row);
			T.setAlignment(1, row, "left");
			T.mergeCells(1, row, 2, row);
			row++;
		}
		if (topics != null && !topics.isEmpty()) {
			T.add(getTopics(), 1, row++);
			if (topic < 0)
				topic =(((EmailTopic) topics.iterator().next()).getIdentifier().intValue());
		}

		if (theLetter != null)
			T.add(getLetter(iwc), 2, row);

		T.setAlignment(1, 2, T.VERTICAL_ALIGN_TOP);
		T.setAlignment(2, 2, T.VERTICAL_ALIGN_TOP);

		Form F = new Form();
		F.add(T);
		add(F);
	}
	
	private PresentationObject getAdminView(IWContext iwc) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellpadding(0);
		T.add(getCategoryLink(core.getImage("/shared/detach.gif")), 1, 1);

		return T;
	}

	private Link getCategoryLink(Image image) {
		Link L = getCategoryLink();
		L.setImage(image);
		return L;
	}

	private PresentationObject getTopics() {
		Table T = new Table();
		//T.add(getStyleText(iwrb.getLocalizedString("topic","Topic"),SN_TITLE),1,1);
		//T.add(getStyleText(iwrb.getLocalizedString("issue","Issue"),SN_TITLE),2,1);
		if (topics != null && topics.size() > 0) {
			Iterator iter = topics.iterator();
			EmailTopic tpc;
			Link link;
			int row = 2;
			while (iter.hasNext()) {
				tpc = (EmailTopic) iter.next();
				int id = tpc.getIdentifier().intValue();
				if (letter > 0) {
					if (topic == id) {
						link = getStyleLink(tpc.getName(), SN_TOPIC);
						link.addParameter(prmTopic, tpc.getIdentifier().toString());
						T.add(link, 1, row++);
						if (letters != null && letters.size() > 0) {
							Iterator iter2 = letters.iterator();
							EmailLetter let;
							//Link subject;
							while (iter2.hasNext()) {
								let = (EmailLetter) iter2.next();
								int lid = let.getIdentifier().intValue();
								if (lid == letter)
									theLetter = let;
							}
						}
					}
				} else {

					link = getStyleLink(tpc.getName(), SN_TOPIC);
					link.addParameter(prmTopic,tpc.getIdentifier().toString());

					T.add(link, 1, row++);
					if (topic > 0 && topic == id) {

						//T.add(getLettersHeads(),2,row++);
						if (letters != null && letters.size() > 0) {
							Iterator iter2 = letters.iterator();
							EmailLetter let;
							//int row = 1;
							//T.add(getStyleText(iwrb.getLocalizedString("from","From"),SN_TITLE),1,row);
							//T.add(getStyleText(iwrb.getLocalizedString("subject","Subject"),SN_TITLE),2,row);
							//row++;
							Link subject;
							while (iter2.hasNext()) {
								let = (EmailLetter) iter2.next();
								int lid = let.getIdentifier().intValue();
								if (lid == letter)
									theLetter = let;
								subject = getStyleLink(let.getSubject(), SN_SUBJ);
								subject.addParameter(prmTopic, String.valueOf(topic));
								subject.addParameter(prmLetter, let.getIdentifier().toString());
								T.add(subject, 2, row);
								//T.add(getStyleText(let.getFromName(),SN_FROM),2,row);
								row++;
							}
						}
					}
				}
			}
		}
		return T;
	}

	public PresentationObject getLetter(IWContext iwc) {
		Table T = new Table();
		T.setWidth(T.HUNDRED_PERCENT);
		int row = 1;
		T.add(getStyleText(theLetter.getSubject(), SN_SUBJ), 1, row);
		T.mergeCells(1, row, 2, row);
		row++;
		T.add(getStyleText(iwrb.getLocalizedString("from", "From") + ":", SN_TITLE), 1, row);
		T.add(getStyleText(theLetter.getFromName() + "[ " + theLetter.getFromAddress() + " ]",	SN_FROM),2,row++);
		T.add(getStyleText(iwrb.getLocalizedString("date", "Date") + ":", SN_TITLE), 1, row);
		T.add(getStyleText(df.format(theLetter.getCreated()), SN_DATE), 2, row++);
				
		T.add(Text.getBreak(), 1, row++);

		String body = TextSoap.findAndReplace(theLetter.getBody(), "\n", "<br>");
		T.add(getStyleText(body, SN_BODY), 1, row);
		T.mergeCells(1, row, 2, row);
		row++;
		
		if(iwc.hasEditPermission(this)) {
			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString("delete","Delete"));
			delete.setSubmitConfirm(iwrb.getLocalizedString("delete_confirm","Are you sure you want to delete this letter ?"));
			T.add(new HiddenInput(prmLetterDelete,"true"));
			T.add(new HiddenInput(prmTopic,String.valueOf(topic)));
			T.add(new HiddenInput(prmLetter,String.valueOf(letter)));
			T.add(delete,1,row);
		}
		return T;
	}

	public PresentationObject getLettersHeads() {
		Table T = new Table();
		if (letters != null && letters.size() > 0) {
			Iterator iter = letters.iterator();
			EmailLetter let;
			int row = 1;
			//T.add(getStyleText(iwrb.getLocalizedString("from","From"),SN_TITLE),1,row);
			//T.add(getStyleText(iwrb.getLocalizedString("subject","Subject"),SN_TITLE),2,row);
			row++;
			Link link;
			while (iter.hasNext()) {
				let = (EmailLetter) iter.next();
				int id = let.getIdentifier().intValue();
				if (id == letter)
					theLetter = let;
				link = getStyleLink(let.getSubject(), SN_SUBJ);
				link.addParameter(prmTopic, String.valueOf(topic));
				link.addParameter(prmLetter, String.valueOf(let.getIdentifier().toString()));
				T.add(link, 1, row);
				//T.add(getStyleText(let.getFromName(),SN_FROM),2,row);
				row++;
			}
		}
		return T;
	}

	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { SN_TOPIC, SN_FROM, SN_SUBJ, SN_DATE, SN_BODY, SN_TITLE };
		String[] styleValues = { DEF_TOPIC, DEF_FROM, DEF_SUBJ, DEF_DATE, DEF_BODY, DEF_TITLE };
		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}

	public final static String SN_TOPIC = "Topic";
	public final static String SN_FROM = "From";
	public final static String SN_SUBJ = "Subject";
	public final static String SN_DATE = "Date";
	public final static String SN_BODY = "Body";
	public final static String SN_TITLE = "Title";

	public final static String DEF_TOPIC =
		"font-style:normal;color:#000000;font-size:13px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	public final static String DEF_FROM =
		"font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight;";
	public final static String DEF_SUBJ =
		"font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight;";
	public final static String DEF_DATE =
		"font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight;";
	public final static String DEF_BODY = "font-weight:plain;";
	public final static String DEF_TITLE =
		"font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";

}
