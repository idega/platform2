package com.idega.block.contract.presentation;
import com.idega.block.contract.business.ContractWriter;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.lowagie.text.Font;
/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ContractFilerWindow extends IWAdminWindow
{
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.contract";
	public static String prmOneId = "contract_id", prmTest = "test", prmManyIds = "many_ids";
	public static String prmSeperator = "_", prmFileName = "fname";
	public final static String prmCategoryId = "con_cat";
	public final static String prmContractId = "con_id";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	public ContractFilerWindow()
	{
		setWidth(570);
		setHeight(550);
		setResizable(true);
	}
	public String getBundleIdentifier()
	{
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc)
	{
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
		addTitle("Contract Filer");
		int iCategoryId = -1, iContractId = -1;
		if (iwc.isParameterSet(prmCategoryId) && iwc.isParameterSet(prmContractId))
		{
			iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId));
			iContractId = Integer.parseInt(iwc.getParameter(prmContractId));
		}
		if (iwc.isParameterSet("generate"))
		{
			int id = generate(iwc);
			if (id > 0)
			{
				close();
				setParentToReload();
				/*
				
				Link L = new Link(com.idega.block.media.servlet.MediaServlet.getMediaURL(id));
				
				L.setURL( com.idega.block.media.servlet.MediaServlet.getMediaURL(id));
				
				add(L);
				
				*/
			}
			else
				add("failed");
		}
		else
			add(printForm(iCategoryId, iContractId));
	}
	private Form printForm(int iCategoryId, int iContractId)
	{
		Table T = new Table(5, 7);
		T.add(iwrb.getLocalizedString("titlefont", "Title font"), 1, 2);
		T.add(iwrb.getLocalizedString("paragraphfont", "Paragraph font"), 1, 3);
		T.add(iwrb.getLocalizedString("namefont", "Name font"), 1, 4);
		T.add(iwrb.getLocalizedString("tagfont", "Tag font"), 1, 5);
		T.add(iwrb.getLocalizedString("textfont", "Text font"), 1, 6);
		T.add(iwrb.getLocalizedString("filename", "Filename"), 1, 6);
		DropdownMenu drpTitleFont = FontDrop("titlefont", Font.HELVETICA);
		DropdownMenu drpParagraphFont = FontDrop("paragraphfont", Font.HELVETICA);
		DropdownMenu drpNameFont = FontDrop("namefont", Font.HELVETICA);
		DropdownMenu drpTagFont = FontDrop("tagfont", Font.HELVETICA);
		DropdownMenu drpTextFont = FontDrop("textfont", Font.HELVETICA);
		DropdownMenu drpTitleSize = SizeDrop("titlesize", 16);
		DropdownMenu drpParagraphSize = SizeDrop("paragraphsize", 10);
		DropdownMenu drpNameSize = SizeDrop("namesize", 12);
		DropdownMenu drpTagSize = SizeDrop("tagsize", 9);
		DropdownMenu drpTextSize = SizeDrop("textsize", 8);
		DropdownMenu drpTitleType = StyleDrop("titlestyle", Font.BOLD);
		DropdownMenu drpParagraphType = StyleDrop("paragraphstyle", Font.BOLD);
		DropdownMenu drpNameType = StyleDrop("namestyle", Font.BOLDITALIC);
		DropdownMenu drpTagType = StyleDrop("tagstyle", Font.BOLDITALIC);
		DropdownMenu drpTextType = StyleDrop("textstyle", Font.NORMAL);
		T.add(drpTitleFont, 2, 2);
		T.add(drpTitleSize, 3, 2);
		T.add(drpTitleType, 4, 2);
		T.add(drpParagraphFont, 2, 3);
		T.add(drpParagraphSize, 3, 3);
		T.add(drpParagraphType, 4, 3);
		T.add(drpNameFont, 2, 4);
		T.add(drpNameSize, 3, 4);
		T.add(drpNameType, 4, 4);
		T.add(drpTagFont, 2, 5);
		T.add(drpTagSize, 3, 5);
		T.add(drpTagType, 4, 5);
		T.add(drpTextFont, 2, 6);
		T.add(drpTextSize, 3, 6);
		T.add(drpTextType, 4, 6);
		TextInput fileNameInput = new TextInput("filename", "Contract");
		SubmitButton mitsub = new SubmitButton("generate", "Generate");
		T.add(new HiddenInput(prmCategoryId, String.valueOf(iCategoryId)));
		T.add(new HiddenInput(prmContractId, String.valueOf(iContractId)));
		T.add(fileNameInput, 2, 7);
		T.add(mitsub, 4, 7);
		Form F = new Form();
		F.add(T);
		return F;
	}
	private int generate(IWContext iwc)
	{
		String sFileName = iwc.getParameter("filename");
		int iTitleFont = Integer.parseInt(iwc.getParameter("titlefont"));
		int iParagraphFont = Integer.parseInt(iwc.getParameter("paragraphfont"));
		int iTagFont = Integer.parseInt(iwc.getParameter("tagfont"));
		int iTextFont = Integer.parseInt(iwc.getParameter("textfont"));
		int iTitleSize = Integer.parseInt(iwc.getParameter("titlesize"));
		int iParagraphSize = Integer.parseInt(iwc.getParameter("paragraphsize"));
		int iTagSize = Integer.parseInt(iwc.getParameter("tagsize"));
		int iTextSize = Integer.parseInt(iwc.getParameter("textsize"));
		int iTitleType = Integer.parseInt(iwc.getParameter("titlestyle"));
		int iParagraphType = Integer.parseInt(iwc.getParameter("paragraphstyle"));
		int iTagType = Integer.parseInt(iwc.getParameter("tagstyle"));
		int iTextType = Integer.parseInt(iwc.getParameter("textstyle"));
		Font titleFont = new Font(iTitleFont, iTitleSize, iTitleType);
		Font paraFont = new Font(iParagraphFont, iParagraphSize, iParagraphType);
		Font tagFont = new Font(iTagFont, iTagSize, iTagType);
		Font textFont = new Font(iTextFont, iTextSize, iTextType);
		int iContractId = Integer.parseInt(iwc.getParameter(prmContractId));
		int iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId));
		int id = ContractWriter.writePDF(iContractId, iCategoryId, sFileName, titleFont, paraFont, tagFont, textFont);
		return id;
	}
	private DropdownMenu FontDrop(String name, int selected)
	{
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(String.valueOf(Font.HELVETICA), "HELVETICA");
		drp.addMenuElement(String.valueOf(Font.COURIER), "COURIER");
		drp.addMenuElement(String.valueOf(Font.TIMES_ROMAN), "TIMES_NEW_ROMAN");
		drp.setSelectedElement(String.valueOf(selected));
		return drp;
	}
	private DropdownMenu SizeDrop(String name, int selected)
	{
		DropdownMenu drp = new DropdownMenu(name);
		for (int i = 8; i < 21; i++)
		{
			drp.addMenuElement(String.valueOf(i));
		}
		drp.setSelectedElement(String.valueOf(selected));
		return drp;
	}
	private DropdownMenu StyleDrop(String name, int selected)
	{
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(String.valueOf(Font.NORMAL), "NORMAL");
		drp.addMenuElement(String.valueOf(Font.BOLD), "BOLD");
		drp.addMenuElement(String.valueOf(Font.BOLDITALIC), "BOLDITALIC");
		drp.addMenuElement(String.valueOf(Font.ITALIC), "ITALIC");
		drp.addMenuElement(String.valueOf(Font.STRIKETHRU), "STRIKETHRU");
		drp.addMenuElement(String.valueOf(Font.UNDERLINE), "UNDERLINE");
		drp.setSelectedElement(String.valueOf(selected));
		return drp;
	}
}
