package com.idega.block.text.business;

import com.idega.block.text.data.*;
import java.util.Vector;
import java.util.List;
import java.util.Locale;

/**
 * 
 * Title:
 * 
 * Description:
 * 
 * Copyright: Copyright (c) 2000-2001 idega.is All Rights Reserved
 * 
 * Company: idega
 * 
 *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * 
 * @version 1.1
 */

public class ContentHelper {

	private Content eContent;
	private List lLocalizedText = null;
	private List icFiles = null;
	
	public Content getContent() {
		return this.eContent;
	}

	public LocalizedText getLocalizedText(Locale locale) {
		LocalizedText LT = null, lt = null;

		if (this.lLocalizedText != null) {
			int len = this.lLocalizedText.size();
			for (int i = 0; i < len; i++) {
				LT = (LocalizedText) this.lLocalizedText.get(i);
				if (LT.getLocaleId() == TextFinder.getLocaleId(locale)) {
					lt = LT;
				}
			}
			return lt;
		} else {
			return null;
		}
	}

	public LocalizedText getLocalizedText() {
		LocalizedText LT = null;
		if (this.lLocalizedText != null) {
			LT = (LocalizedText) this.lLocalizedText.get(0);
			return LT;
		} else {
			return null;
		}
	}

	public List getLocalizedTexts() {
		return this.lLocalizedText;
	}

	public void setLocalizedText(LocalizedText text) {
		if (text != null) {
			if (this.lLocalizedText != null) {
				this.lLocalizedText.add(text);
			} else {
				this.lLocalizedText = new Vector();
				this.lLocalizedText.add(text);
			}
		}
	}

	public void setLocalizedText(List text) {
		if (text != null) {
			this.lLocalizedText = text;
		}
	}

	public void setContent(Content content) {
		this.eContent = content;
	}

	/**
	 * 
	 * Sets a List of ICFile
	 */
	public void setFiles(List listOfFiles) {
		this.icFiles = listOfFiles;
	}

	/**
	 * 
	 * Returns a List of ICFile
	 */
	public List getFiles() {
		return this.icFiles;
	}

	public boolean hasFiles() {
		return this.icFiles != null && !this.icFiles.isEmpty();
	}
}