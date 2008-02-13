package com.idega.block.finance.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.finance.data.TariffIndex;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffIndexEditor extends Finance {

	public static String strAction = "ti_action";
	public static String RentType = com.idega.block.finance.data.TariffIndexBMPBean.RENT_TYPE_A;
	public static String ElType = com.idega.block.finance.data.TariffIndexBMPBean.RENT_TYPE_B;
	public static String HeatType = com.idega.block.finance.data.TariffIndexBMPBean.RENT_TYPE_C;
	public static String[] Types = { RentType, ElType, HeatType };
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;

	public String getLocalizedNameKey() {
		return "indices";
	}

	public String getLocalizedNameValue() {
		return "Indices";
	}

	protected void control(IWContext iwc) {
		if (this.isAdmin) {
			try {
				PresentationObject MO = new Text("nothing");
				if (iwc.getParameter(strAction) == null) {
					MO = getMainTable(iwc, this.iCategoryId);
				}
				if (iwc.getParameter(strAction) != null) {
					String sAct = iwc.getParameter(strAction);
					int iAct = Integer.parseInt(sAct);

					switch (iAct) {
					case ACT1:
						MO = getMainTable(iwc, this.iCategoryId);
						break;
					case ACT2:
						MO = getChangeTable(iwc, this.iCategoryId);
						break;
					case ACT3:
						MO = doUpdate(iwc, this.iCategoryId);
						break;
					default:
						MO = getMainTable(iwc, this.iCategoryId);
						break;
					}
				}

				setLocalizedTitle("tariff_index_editor", "Tariff index editor");
				setSearchPanel(makeLinkTable(1, this.iCategoryId));
				setMainPanel(MO);

			} catch (Exception S) {
				S.printStackTrace();
			}
		} else {
			add(this.iwrb.getLocalizedString("access_denied", "Access denies"));
		}
	}

	protected PresentationObject makeLinkTable(int menuNr, int iCategoryId) {
		Table LinkTable = new Table(3, 1);
		int last = 3;
		LinkTable.setWidth("100%");
		LinkTable.setCellpadding(2);
		LinkTable.setCellspacing(1);

		LinkTable.setWidth(last, "100%");
		Link Link1 = new Link(getHeader(this.iwrb.getLocalizedString("view",
				"View")));
		Link1.addParameter(TariffIndexEditor.strAction, String
				.valueOf(this.ACT1));
		// Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
		Link Link2 = new Link(getHeader(this.iwrb.getLocalizedString("change",
				"Change")));

		Link2.addParameter(TariffIndexEditor.strAction, String
				.valueOf(this.ACT2));
		// Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
		if (this.isAdmin) {
			LinkTable.add(Link1, 1, 1);
			LinkTable.add(Link2, 2, 1);
		}
		return LinkTable;
	}

	protected PresentationObject getMainTable(IWContext iwc, int iCategoryId) {
		DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG, iwc
				.getCurrentLocale());
		Collection L = getIndices(iCategoryId);
		int count = 0;
		if (L != null) {
			count = L.size();
		}
		Table keyTable = new Table(6, count + 1);
		keyTable.setWidth("100%");
		keyTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		keyTable.setRowColor(1, getHeaderColor());
		keyTable.setCellpadding(2);
		keyTable.setCellspacing(1);
		// keyTable.setColumnAlignment(3, "right");
		keyTable.add(getHeader("Nr"), 1, 1);
		keyTable.add(getHeader(this.iwrb.getLocalizedString("name", "Name")),
				2, 1);
		keyTable.add(getHeader(this.iwrb.getLocalizedString("info", "Info")),
				3, 1);
		keyTable.add(getHeader(this.iwrb.getLocalizedString("index", "Index")),
				4, 1);
		keyTable.add(getHeader(this.iwrb.getLocalizedString("date", "date")),
				5, 1);
		keyTable.add(getHeader(this.iwrb.getLocalizedString("type", "Type")),
				6, 1);
		if (this.isAdmin) {
			if (count > 0) {
				int row = 2;
				int rowcount = 1;
				for (Iterator iter = L.iterator(); iter.hasNext();) {
					TariffIndex ti = (TariffIndex) iter.next();
					keyTable.add(getText(String.valueOf(rowcount++)), 1, row);
					keyTable.add(getText(ti.getName()), 2, row);
					keyTable.add(getText(ti.getInfo()), 3, row);
					keyTable.add(getText(Double.toString(ti.getIndex())), 4,
							row);
					keyTable.add(getText(dfLong.format(ti.getDate())), 5, row);
					keyTable.add(getText(ti.getType()), 6, row);
					row++;
				}
			}
		}
		return (keyTable);
	}

	protected PresentationObject getChangeTable(IWContext iwc, int iCategoryId)
			throws SQLException {

		Collection L = getIndices(iCategoryId);
		String t = com.idega.block.finance.data.TariffIndexBMPBean.indexType;
		int count = 0;
		if (L != null) {
			count = L.size();
		}
		int inputcount = count + 5;
		DataTable inputTable = getDataTable();
		inputTable.setUseBottom(false);
		inputTable.setUseTop(false);
		inputTable.setTitlesHorizontal(true);
		inputTable.setWidth("100%");
		inputTable.add(getHeader("Nr"), 1, 1);
		inputTable.add(getHeader(localize("name", "Name")), 2, 1);
		inputTable.add(getHeader(localize("description", "Description")), 3, 1);
		inputTable.add(getHeader(localize("index", "Index")), 4, 1);
		inputTable.add(getHeader(localize("type", "Type")), 5, 1);
		inputTable.add(getHeader(localize("delete", "Delete")), 6, 1);
		Iterator iter = L.iterator();
		for (int i = 1; i <= inputcount; i++) {
			String rownum = String.valueOf(i);
			TextInput nameInput, infoInput, indexInput;
			HiddenInput idInput;
			CheckBox delCheck;
			DropdownMenu typeDrp;
			// int pos;
			if (i <= count && iter.hasNext()) {
				// pos = i-1;
				TariffIndex ti = (TariffIndex) iter.next();
				nameInput = getTextInput("ti_nameinput" + i, (ti.getName()));
				infoInput = getTextInput("ti_infoinput" + i, (ti.getInfo()));
				indexInput = getTextInput("ti_indexinput" + i, (String
						.valueOf(ti.getIndex())));
				typeDrp = typeDrop("ti_typedrp" + i, ti.getType());
				idInput = new HiddenInput("ti_idinput" + i, ti.getPrimaryKey()
						.toString());
				delCheck = getCheckBox("ti_delcheck" + i, "true");

				inputTable.add(delCheck, 6, i + 1);
			} else {
				nameInput = getTextInput("ti_nameinput" + i);
				infoInput = getTextInput("ti_infoinput" + i);
				indexInput = getTextInput("ti_indexinput" + i);
				typeDrp = typeDrop("ti_typedrp" + i, String.valueOf(t
						.charAt(i - 1)));

				idInput = new HiddenInput("ti_idinput" + i, "-1");
			}
			typeDrp = (DropdownMenu) setStyle(typeDrp, STYLENAME_INTERFACE);
			nameInput.setSize(20);
			infoInput.setSize(40);
			indexInput.setSize(10);

			inputTable.add(getText(rownum), 1, i + 1);
			inputTable.add(nameInput, 2, i + 1);
			inputTable.add(infoInput, 3, i + 1);
			inputTable.add(indexInput, 4, i + 1);
			inputTable.add(typeDrp, 5, i + 1);
			inputTable.add(idInput);

		}

		inputTable.add(new HiddenInput("ti_count", String.valueOf(inputcount)));
		inputTable.add(new HiddenInput(TariffIndexEditor.strAction, String
				.valueOf(this.ACT3)));
		inputTable.add(Finance.getCategoryParameter(iCategoryId));

		inputTable.addButton(new SubmitButton("save", "Save"));

		return (inputTable);
	}

	protected PresentationObject doUpdate(IWContext iwc, int iCategoryId)
			throws SQLException {
		int count = Integer.parseInt(iwc.getParameter("ti_count"));
		String sName, sInfo, sDel, sIndex, sType;
		Integer ID;
		double findex = 0;
		TariffIndex ti = null;
		for (int i = 1; i < count + 1; i++) {
			sName = iwc.getParameter("ti_nameinput" + i).trim();
			sInfo = iwc.getParameter("ti_infoinput" + i).trim();
			sIndex = iwc.getParameter("ti_indexinput" + i).trim();
			sDel = iwc.getParameter("ti_delcheck" + i);
			sType = iwc.getParameter("ti_typedrp" + i).trim();
			ID = Integer.valueOf(iwc.getParameter("ti_idinput" + i));
			java.sql.Timestamp stamp = IWTimestamp.getTimestampRightNow();
			if (!"".equals(sIndex)) {
				sIndex = sIndex.replace(',', '.');
				findex = Double.parseDouble(sIndex);
			}

			try {
				if (ID != null && ID.intValue() > 0) {
					// ti = FinanceFinder.getInstance().getTariffIndex(ID) ;
					ti = getFinanceService().getTariffIndexHome()
							.findByPrimaryKey(ID);
					double oldvalue = ti.getNewValue();
					if (sDel != null && sDel.equalsIgnoreCase("true")) {
						// FinanceBusiness.deleteTariffIndex(ID);
						getFinanceService().removeTariffIndex(ID);
					} else if (!sName.equalsIgnoreCase(ti.getName())
							|| !sInfo.equalsIgnoreCase(ti.getInfo())
							|| !sType.equalsIgnoreCase(ti.getType())
							|| !(findex == ti.getIndex())) {
						// FinanceBusiness.saveTariffIndex(-1,sName,sInfo,sType,findex,oldvalue,stamp,iCategoryId);
						getFinanceService().createOrUpdateTariffIndex(null,
								sName, sInfo, sType, findex, oldvalue, stamp,
								getFinanceCategoryId());
					}
				} else if (!"".equalsIgnoreCase(sName) && !"".equals(sIndex)) {
					getFinanceService().createOrUpdateTariffIndex(null, sName,
							sInfo, sType, findex, findex, stamp,
							getFinanceCategoryId());
					// FinanceBusiness.saveTariffIndex(-1,sName,sInfo,sType,findex,findex,stamp,iCategoryId);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (RemoveException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}// for loop

		return getMainTable(iwc, iCategoryId);
	}

	private DropdownMenu typeDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		String s = com.idega.block.finance.data.TariffIndexBMPBean.indexType;
		int len = s.length();
		for (int i = 0; i < len; i++) {
			drp.addMenuElement(String.valueOf(s.charAt(i)));
		}
		drp.setSelectedElement(selected);
		return drp;
	}

	private Collection getIndices(int iCategoryId) {
		try {
			/*
			 * Vector V = new Vector(); for (int i = 0; i <
			 * com.idega.block.finance.data.TariffIndexBMPBean.indexType.length();
			 * i++) { TariffIndex ti=
			 * FinanceFinder.getInstance().getTariffIndex(String.valueOf(com.idega.block.finance.data.TariffIndexBMPBean.indexType.charAt(i)),iCategoryId);
			 * if(ti!= null) V.add(ti); } return V;
			 */
			return getFinanceService().getTariffIndexHome()
					.findLastTypeGrouped();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return new Vector();
	}

	public void main(IWContext iwc) {
		control(iwc);
	}

}// class TariffKeyEditor
