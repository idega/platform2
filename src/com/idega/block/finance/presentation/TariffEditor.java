package com.idega.block.finance.presentation;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffGroup;
import com.idega.block.finance.data.TariffIndex;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Anchor;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class TariffEditor extends Finance {

	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;
	protected final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;
	public String strAction = "te_action";
	public final int YEAR = 1, MONTH = 2, WEEK = 3, DAY = 4;
	
	private int iNumberOfDecimals = 0;
	private boolean bRoundAmounts = true;
	private boolean hasUpdatedIndices = false;
	private Integer groupId;
	

	private static String prmGroup = "taed_grp";

	public String getLocalizedNameKey() {
		return "tariffs";
	}

	public String getLocalizedNameValue() {
		return "Tariffs";
	}
	public void setRoundAmounts(boolean round) {
		bRoundAmounts = round;
	}

	public void setNumberOfDecimals(int decimals) {
		iNumberOfDecimals = decimals;
	}

	protected void control(IWContext iwc) {
		
		Collection groups =null;
		try {
			groups = getFinanceService().getTariffGroupHome().findByCategory(getFinanceCategoryId());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		//List groups = FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
		TariffGroup group = null;
		if (iwc.isParameterSet(prmGroup))
			groupId = Integer.valueOf(iwc.getParameter(prmGroup));
		if (groupId!=null && groupId.intValue() > 0) {
			try {
				//group = FinanceFinder.getInstance().getTariffGroup(iGroupId);
				group = getFinanceService().getTariffGroupHome().findByPrimaryKey(groupId);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		} else if (groups != null) {
			//group = (TariffGroup) groups.get(0);
			//iGroupId = group.getID();
		}

		if (isAdmin) {
			try {
				PresentationObject MO = new Text();

				if (iwc.getParameter("updateindex") != null) {
					MO = doUpdateIndex(iwc,group);
				} else if (iwc.getParameter("savetariffs") != null) {
					MO = doUpdate(iwc,group);
				} else if (iwc.getParameter(strAction) != null) {
					String sAct = iwc.getParameter(strAction);
					int iAct = Integer.parseInt(sAct);
					switch (iAct) {
						case ACT1 :
							MO = getMain(iwc,group);
							break;
						case ACT2 :
							MO = getSingleLineChange(iwc, false, false,group);
							break;
						case ACT3 :
							MO = doUpdate(iwc,group);
							break;
						case ACT4 :
							MO = getSingleLineChange(iwc, true, false,group);
							break;
						default :
							MO = getMain(iwc,group);
							break;
					}
				} else {
					MO = getMain(iwc, group);
				}
				
				String groupName = group != null ? group.getName() : "";
				setTitle(localize("tariffs", "Tariffs") + "  " + groupName);
				setTabPanel(getGroupLinks( groups));
				setSearchPanel(makeLinkTable(1, group));
				setMainPanel(MO);
				
			} catch (Exception S) {
				S.printStackTrace();
			}
		} else
			add(localize("access_denied", "Access denies"));
	}
	protected PresentationObject makeLinkTable(int menuNr, TariffGroup group) {
		Table LinkTable = new Table(4, 1);
		int last = 4;

		LinkTable.setWidth(Table.HUNDRED_PERCENT);
		LinkTable.setCellpadding(2);
		LinkTable.setCellspacing(1);
		//LinkTable.setColor(Edit.colorDark);
		LinkTable.setWidth(last, "100%");
		if (group != null) {
			Link Link1 = new Link(getHeader(localize("view", "View")));
			//Link1.setFontColor(Edit.colorLight);
			Link1.addParameter(this.strAction, String.valueOf(this.ACT1));
			Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
			Link1.addParameter(prmGroup, group.getPrimaryKey().toString());
			Link Link2 = new Link(getHeader(localize("change", "Change")));
			//Link2.setFontColor(Edit.colorLight);
			Link2.addParameter(this.strAction, String.valueOf(this.ACT2));
			Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
			Link2.addParameter(prmGroup, group.getPrimaryKey().toString());
			/*
			Link Link3 = new Link(localize("new","New"));
			Link3.setFontColor(Edit.colorLight);
			Link3.addParameter(this.strAction,String.valueOf(this.ACT4));
			*/
			if (isAdmin) {
				LinkTable.add(Link1, 1, 1);
				LinkTable.add(Link2, 2, 1);
				//LinkTable.add(Link3,3,1);
			}
		}
		return LinkTable;
	}
	
	private PresentationObject getGroupLinks(Collection groups) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		int col = 1;
		if (groups != null) {
			java.util.Iterator I = groups.iterator();
			TariffGroup group;
			Link tab;
			while (I.hasNext()) {
				group = (TariffGroup) I.next();
				tab = new Link(iwb.getImageTab(group.getName(), false));
				tab.addParameter(Finance.getCategoryParameter(iCategoryId));
				tab.addParameter(prmGroup, group.getPrimaryKey().toString());
				T.add(tab, col++, 1);
			}
		}
		Link edit = new Link(iwrb.getLocalizedImageTab("edit", "Edit", false));
		edit.setWindowToOpen(TariffGroupWindow.class);
		edit.addParameter(Finance.getCategoryParameter(iCategoryId));
		T.add(edit, col, 1);
		return T;
	}

	protected PresentationObject getMain(IWContext iwc, TariffGroup group)
		throws java.rmi.RemoteException {
		FinanceHandler handler = null;
		Map attributeMap = null;
		boolean ifAttributes = false;
		int attcount = 0;
		if (group != null) {
			groupId = (Integer)group.getPrimaryKey();
			if (group.getHandlerId() > 0) {
				//handler = FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
				handler = getFinanceService().getFinanceHandler(new Integer(group.getHandlerId()));
				if (handler != null) {
					attributeMap = handler.getAttributeMap();
					List list = handler.listOfAttributes();
					ifAttributes = attributeMap != null && list != null;
					
				}
			}
		}
		Collection tariffs = null;
		Map tariffMap = null;
		Map hAK = null;
		if(groupId!=null){
		try {
			
			tariffs = getFinanceService().getTariffHome().findByTariffGroup(groupId); 
				//FinanceFinder.getInstance().listOfTariffs(iGroupId);
			tariffMap = sortTariffsIntoMapOfLists(tariffs);
			attcount = tariffMap.size();
			Collection AK = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId());
				//FinanceFinder.getInstance().listOfAccountKeys(iCategoryId);

			hAK = getKeys(AK);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		}
		int count = 0;
		if (tariffs != null)
			count = tariffs.size();
		Table outerTable = new Table(1, 2);
		outerTable.setCellpadding(0);
		outerTable.setCellspacing(0);
		outerTable.setWidth(Table.HUNDRED_PERCENT);
		int tableRows = (count+attcount*2+1);
		Table innerTable = new Table(5,tableRows);
		
		innerTable.setWidth(Table.HUNDRED_PERCENT);
		innerTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		innerTable.setRowColor(1, getHeaderColor());
		innerTable.setCellpadding(2);
		innerTable.setCellspacing(1);
		int col = 1;
		innerTable.add(getHeader("Nr"), col++, 1);
		if (ifAttributes) {
			innerTable.add(getHeader(localize("connection", "Connection")), col++, 1);
		}
		innerTable.add(getHeader(localize("name", "Name")), col++, 1);
		innerTable.add(getHeader(localize("amount", "Amount")), col++, 1);
		//T.add(getHeader(localize("info","Info")),5,1);
		innerTable.add(getHeader(localize("account_key", "Account key")), col++, 1);

		if (isAdmin) {
			if (count != 0) {
			    if(ifAttributes){
				Tariff tariff;
				//Iterator iter = tariffs.iterator();
				Iterator iter = tariffMap.entrySet().iterator();
				int row = 2;
				int i = 0;
				
		
				while (iter.hasNext()) {
					col = 1;
					Map.Entry entry = (Map.Entry) iter.next();
					String tatt = (String)entry.getKey();
					String val = "";
					BigDecimal attTotal = new BigDecimal(0);
					
					List tariffsInMap = (List) entry.getValue();
					for (Iterator iterator = tariffsInMap.iterator(); iterator.hasNext();) {
					    col=1;
                        tariff = (Tariff) iterator.next();
                        
                        innerTable.add(getText(String.valueOf(i + 1)), 1, row);
						
					   if (tatt != null && attributeMap.containsKey(tatt))
					       val = (String) attributeMap.get(tatt);
					   innerTable.add(getText(val), 2, row);
					   attTotal = attTotal.add(new BigDecimal(tariff.getPrice()));
						
	
						
						innerTable.add(getText(tariff.getName()), 3, row);
						innerTable.add(getAmountText((tariff.getPrice())), 4, row);
						
						Integer I = new Integer(tariff.getAccountKeyId());
						if (hAK.containsKey(I))
							innerTable.add(getText((String) hAK.get(I)), 5, row);
						row++;
						i++;
					}
					if(attTotal!=null){
					   
					    innerTable.mergeCells(1,row,2,row);
					    innerTable.setColor(1,row,getBackgroundColor());
					    
					    innerTable.setColor(5,row,getBackgroundColor());
					    innerTable.add(getText("="),3,row);
					    innerTable.setColor(3,row,getHeaderColor());
					    innerTable.setColor(4,row,getHeaderColor());
					    innerTable.add(getAmountText(attTotal.doubleValue()),4,row++);
					    
					    innerTable.mergeCells(1,row,5,row);
					    innerTable.setRowColor(row,getBackgroundColor());
					    row++;
					}
				}
				innerTable.setColumnAlignment(4,Table.HORIZONTAL_ALIGN_RIGHT);
				innerTable.setColumnAlignment(5,Table.HORIZONTAL_ALIGN_RIGHT);
			}
			else{
			    Tariff tariff;
				Iterator iter = tariffs.iterator();
				int row = 2;
				int i = 0;
				while (iter.hasNext()) {
					col = 1;
					tariff = (Tariff) iter.next();
					innerTable.add(getText(String.valueOf(i + 1)), col++, row);
					if (ifAttributes) {
						String tatt = tariff.getTariffAttribute();
						String val = "";
						if (tatt != null && attributeMap.containsKey(tatt))
							val = (String) attributeMap.get(tatt);
						innerTable.add(getText(val), col++, row);
					}

					//  T.add(getText(((IDOLegacyEntity)hLodgings.get(tatt)).getName()),col++,i+2);
					innerTable.add(getText(tariff.getName()), col++, row);
					innerTable.add(getAmountText((tariff.getPrice())), col++, row);
					//T.add(getText(tariffs[i].getInfo()),col++,i+2);
					Integer I = new Integer(tariff.getAccountKeyId());
					if (hAK.containsKey(I))
					    innerTable.add(getText((String) hAK.get(I)), col++, row);
					row++;
					i++;
				}
			}
			int r = ifAttributes?4:3;
			innerTable.setColumnAlignment(r,Table.HORIZONTAL_ALIGN_RIGHT);
			innerTable.setColumnAlignment(r+1,Table.HORIZONTAL_ALIGN_RIGHT);
			}
			
			
			
		}

		outerTable.add(innerTable, 1, 1);
		return outerTable;

	}
	
	private Map sortTariffsIntoMapOfLists(Collection tariffs){
	    Map map = new LinkedHashMap();
	    String key;
	    List list;
	    for (Iterator iter = tariffs.iterator(); iter.hasNext();) {
            Tariff element = (Tariff) iter.next();
            key = element.getTariffAttribute();
            if(map.containsKey(key)){
                list = (List)map.get(key);
            }
            else{
                list = new ArrayList();
            }
            list.add(element);
            map.put(key,list);
            
        }
	    return map;
	}

	private PresentationObject doUpdateIndex(IWContext iwc, TariffGroup group)
		throws java.rmi.RemoteException {
		/** @todo  *
		 *
		 */
		hasUpdatedIndices = true;
		return getSingleLineChange(iwc, false, true, group);
	}

	private PresentationObject getSingleLineChange(
		IWContext iwc,
		boolean ifnew,
		boolean factor,
		TariffGroup group)
		throws java.rmi.RemoteException {
		int iEditID = -1;
		String sEditID = iwc.getParameter("the_edit_id");
		if(sEditID!=null)
			iEditID = Integer.parseInt(sEditID);

		
		boolean updateIndex = factor;
		Collection listOfTariffs = null ;
		//FinanceFinder.getInstance().listOfTariffs(group.getID());
		//List listOfIndices = Finder.listOfTypeGroupedIndices();
		//Collection listOfIndices = getFinanceService().getTariffIndexHome().findLastTypeGrouped();
		//Map mapOfIndices = Finder.mapOfIndicesByTypes(listOfIndices);
		Map mapOfIndices = null;
		//List listOfAccountKeys = FinanceFinder.getInstance().listOfAccountKeys(iCategoryId);
		Collection listOfAccountKeys = null;
		Map mapOfAccountKeys= null;
		try {
			listOfTariffs = getFinanceService().getTariffHome().findByTariffGroup((Integer)group.getPrimaryKey());
			listOfAccountKeys = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId());
			mapOfAccountKeys = getKeys(listOfAccountKeys);
			mapOfIndices = getFinanceService().mapOfTariffIndicesByTypes();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			
		}
		FinanceHandler financeHandler = null;
		Map attributeMap = null;
		DropdownMenu attDrp = null;
		boolean ifAttributes = false;
		boolean ifIndices = false;
		if (group != null) {
			ifIndices = group.getUseIndex();
			if (group.getHandlerId() > 0) {
				financeHandler = getFinanceService().getFinanceHandler(new Integer(group.getHandlerId()));
					//FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
				if (financeHandler != null) {
					attributeMap = financeHandler.getAttributeMap();
					List list = financeHandler.listOfAttributes();
					ifAttributes = attributeMap != null && list != null;
					attDrp = drpAttributes(list, attributeMap, "attdrp", "");
				}
			}
		}
		////////////////////////////
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.setTitlesHorizontal(true);
		SubmitButton save = new SubmitButton("savetariffs", localize("save", "Save"));
		T.addButton(save);
		
		
		int row = 1;
		int col = 1;
		
		T.add(getHeader("Nr"), col++, row);
		if (ifAttributes) {
			T.add(getHeader(localize("connection", "Connection")), col++, row);
		}
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("amount", "Amount")), col++, row);
		T.add(getHeader(localize("account_key", "Account key")), col++, row);
		if (ifIndices) {
			T.add(getHeader(localize("index", "Index")), col++, row);
			T.add(getHeader(localize("updated", "Updated")), col++, row);
		}
		T.add(getHeader(localize("delete", "Delete")), col++, row);
		row++;

		////////////////////////////

		TextInput nameInput = getTextInput("te_nameinput");
		TextInput priceInput = getTextInput("te_priceinput");
		TextInput infoInput = getTextInput("te_infoinput");
		DropdownMenu drpAK = null;
		DropdownMenu drpAtt = null, drpIx = null;
/*
		if (ifIndices)
			drpIx = drpIndicesByType(listOfIndices, "te_ixdrp");
		if (ifAttributes) {
			drpAtt = (DropdownMenu) attDrp.clone();
			drpAtt.setName("te_attdrp");
		}
*/
		CheckBox delCheck = getCheckBox("te_delcheck","true");
		
		nameInput.setSize(20);
		priceInput.setSize(8);
		infoInput.setSize(30);

		
		

		Tariff tariff;
		boolean formAdded = false;
		int i = 0;
		if (listOfTariffs != null) {
			
			Iterator iter = listOfTariffs.iterator();
			Integer tariffID = null;
			while (iter.hasNext()) {
				col = 1;
				tariff = (Tariff) iter.next();
				tariffID = (Integer)tariff.getPrimaryKey();
				float iPrice = tariff.getPrice();
				float oldPrice = iPrice;
				// form part:
				T.add(new Anchor("row"+i),col,row);
				T.add((getText(String.valueOf(++i))), col++, row);
				
				String ixType = tariff.getIndexType();
				IWTimestamp ixdate = null;
				if (ifIndices) {

					String ixDate = iwc.getParameter("te_ixdate"+i);

					if (ixDate != null) {
						ixdate = new IWTimestamp(ixDate);
					} else if (tariff.getIndexUpdated() != null) {
						ixdate = new IWTimestamp(tariff.getIndexUpdated());
						T.add(new HiddenInput("te_ixdate"+i, ixdate.toString()),1,row);
					}

					if (updateIndex 	&& ixType != null	&& mapOfIndices != null	&& mapOfIndices.containsKey(ixType)) {
						TariffIndex ti = (TariffIndex) mapOfIndices.get(ixType);
						java.sql.Timestamp stamp = ti.getDate();
						if (ixdate != null) {
							if (!stamp.equals(ixdate.getTimestamp())) {
								iPrice = iPrice * getAddFactor(ti.getNewValue(), ti.getOldValue());
							}
						} else
							iPrice = iPrice * getAddFactor(ti.getNewValue(), ti.getOldValue());
					}
				}
				iPrice = new Float(TextSoap.decimalFormat((double) iPrice, iNumberOfDecimals)).floatValue();
				oldPrice = new Float(TextSoap.decimalFormat((double) oldPrice, iNumberOfDecimals)).floatValue();
				if (bRoundAmounts){
					iPrice = Math.round((double) iPrice);
					oldPrice = Math.round((double)oldPrice);
				}
				T.add(new HiddenInput("te_oldprice"+i,Float.toString(oldPrice)),1,row);
				priceInput = getTextInput("te_priceinput" + i,String.valueOf(iPrice));
								
				delCheck = new CheckBox("te_delcheck" + i, "true");
				delCheck.setValue(tariffID.intValue());
				
				HiddenInput idInput =	new HiddenInput("te_idinput"+i,tariffID.toString());
				
				T.add(idInput,1,row);
				
				// SINGLE EDIT LINE
				if (iEditID == tariffID.intValue()) {
					drpAK = drpAccountKeys(listOfAccountKeys, ("te_akdrp"+i));
					nameInput = getTextInput("te_nameinput" + i,"");
					infoInput = getTextInput("te_infoinput" + i,"");
					
					if (ifAttributes){
						drpAtt = (DropdownMenu) attDrp.clone();
						drpAtt.setName("te_attdrp" + i);
						drpAtt.setSelectedElement(tariff.getTariffAttribute());
					}
					
					nameInput.setContent(tariff.getName());
					if (tariff.getInfo() != null)
						infoInput.setContent(tariff.getInfo());

					//drpAtt.setSelectedElement(tariff.getTariffAttribute());
					drpAK.setSelectedElement(String.valueOf(tariff.getAccountKeyId()));
					
					if (ifAttributes) {
						drpAtt = (DropdownMenu)setStyle(drpAtt,STYLENAME_INTERFACE);
						T.add(drpAtt, col++, row);
						
					}
					T.add(nameInput, col++, row);
					T.add(priceInput, col++, row);

					T.add(drpAK, col++, row);
					if (ifIndices) {
						drpIx = drpIndicesByType(mapOfIndices.values(), "te_ixdrp" + i);
						drpIx = (DropdownMenu) setStyle(drpIx,STYLENAME_INTERFACE);
						drpIx.setSelectedElement(ixType);
						
						T.add(drpIx, col++, row);
						if(ixdate!=null)
							T.add(getText(ixdate.toString()),col++,row);
						else
							T.add(getText(""),col++,row);
					}
					
				}
				// OTHER LINES
				else {
					if (ifAttributes) {
						String tatt = tariff.getTariffAttribute();
						String val = "";
						if (tatt != null && attributeMap.containsKey(tatt))
							val = (String) attributeMap.get(tatt);
						T.add(getText(val), col++, row);
					}
					Link lineChangeLink = new Link(getText(tariff.getName()));
					lineChangeLink.addParameter("the_edit_id",tariffID.intValue());
					lineChangeLink.addParameter(this.strAction, String.valueOf(this.ACT2));
					lineChangeLink.addParameter(Finance.getCategoryParameter(iCategoryId));
					lineChangeLink.addParameter(prmGroup, group.getPrimaryKey().toString());
					
					
					T.add(lineChangeLink, col++, row);
					T.add(priceInput, col++, row);
					//T.add(getText(String.valueOf(iPrice)), col++, row);

					Integer I = new Integer(tariff.getAccountKeyId());
					if (mapOfAccountKeys.containsKey(I))
						T.add(getText((String) mapOfAccountKeys.get(I)), col++, row);
					
					if (ifIndices) {
						if (mapOfIndices.containsKey(tariff.getIndexType()))
							T.add(	getText(((TariffIndex) mapOfIndices.get(tariff.getIndexType())).getName() ),	col++,	row);
						else
							T.add(getText(""),col++,row);
							
						if(ixdate!=null)
							T.add(getText(ixdate.toString()),col++,row);
						else
							T.add(getText(""),col++,row);
					}
					
					T.add(new HiddenInput("te_ixdrp"+i,ixType),1,row);
	

				}
				T.add(delCheck, col++, row);
				row++;
			}
			
		}
		if (!formAdded) {
			for(int k = 0 ; k < 5 ; k++){
			
				col = 1;
				T.add(getText(String.valueOf(++i)), col++, row);
	
				
				T.add(new HiddenInput("te_idinput"+i,"-1"),1,row);
				if (ifAttributes) {
					drpAtt = (DropdownMenu) attDrp.clone();
					drpAtt.setName("te_attdrp" + i);
					drpAtt = (DropdownMenu) setStyle(drpAtt,STYLENAME_INTERFACE);
					T.add(drpAtt, col++, row);
					
				}
				nameInput = getTextInput("te_nameinput" + i);
				priceInput = getTextInput("te_priceinput" + i);
				infoInput = getTextInput("te_infoinput" + i);
				
				T.add(nameInput, col++, row);
				//T.add(infoInput, col++, row);
				T.add(priceInput, col++, row);
				drpAK = drpAccountKeys(listOfAccountKeys, ("te_akdrp"+i));
				drpAK = (DropdownMenu) setStyle(drpAK,STYLENAME_INTERFACE);
				T.add(drpAK, col++, row);
				if (ifIndices) {
					drpIx = drpIndicesByType(mapOfIndices.values(), "te_ixdrp" + i);
					drpIx = (DropdownMenu)setStyle(drpIx,STYLENAME_INTERFACE_BUTTON);
					T.add(drpIx, col++, row);
				}
				row++;
			}

		}
		
		Table T3 = new Table(8, 1);
		T3.setWidth(T3.HUNDRED_PERCENT);
		T3.setWidth(5, 1, T3.HUNDRED_PERCENT);
		T3.setColumnAlignment(6, "right");
		T3.setColumnAlignment(7, "right");
		
		if (ifIndices) {
			SubmitButton update = new SubmitButton("updateindex", localize("update_indexes", "Update indexes"));
			//Edit.setStyle(update);
			T3.add(update, 8, 1);
			if(hasUpdatedIndices){
				T3.add( getErrorText(localize("unsaved_data","You have unsaved data !!")),6,1);
				
				T3.add(save, 7, 1);
			}
		}
		
		Table T4 = new Table();
		T4.setCellpadding(0);
		T4.setCellpadding(0);
		T4.setWidth(T4.HUNDRED_PERCENT);
		T4.add(T3,1,1);
		T4.add(T,1,2);

		T4.add(new HiddenInput(prmGroup,group.getPrimaryKey().toString()));
		T4.add(new HiddenInput("te_count", String.valueOf( i )));
		T4.add(Finance.getCategoryParameter(iCategoryId));
		
		return (T4);
	}

	private PresentationObject getChange(
		IWContext iwc,
		boolean ifnew,
		boolean factor,
		TariffGroup group)
		throws java.rmi.RemoteException {
		
		
		boolean updateIndex = factor;
		//FinanceFinder.getInstance().listOfTariffs((Integer)group.getPrimaryKey());
		Collection tariffs = null;
		Collection AK = null;
		//FinanceFinder.getInstance().listOfAccountKeys(iCategoryId);
		//List indices = Finder.listOfTypeGroupedIndices();
		//Map M = Finder.mapOfIndicesByTypes(indices);
		Map M = null;
		try {
			 tariffs = getFinanceService().getTariffHome().findByTariffGroup((Integer)group.getPrimaryKey()) ;
				AK = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId()); 
				M = getFinanceService().mapOfTariffIndicesByTypes();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		FinanceHandler handler = null;
		Map attributeMap = null;
		DropdownMenu attDrp = null;
		boolean ifAttributes = false;
		boolean ifIndices = false;
		if (group != null) {
			ifIndices = group.getUseIndex();
			if (group.getHandlerId() > 0) {
				//handler = FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
				handler = getFinanceService().getFinanceHandler(new Integer (group.getHandlerId()));
				if (handler != null) {
					attributeMap = handler.getAttributeMap();
					List list = handler.listOfAttributes();
					ifAttributes = attributeMap != null && list != null;
					attDrp = drpAttributes(list, attributeMap, "attdrp", "");
				}
			}
		}

		int count = 0;
		if (tariffs != null)
			count = tariffs.size();
		int inputcount = count + 5;
		Table BorderTable = new Table();
		BorderTable.setCellpadding(1);
		BorderTable.setCellspacing(0);
		//BorderTable.setColor(Edit.colorDark);
		BorderTable.setWidth("100%");
		Table T2 = new Table(1, 3);
		//T2.setColor(Edit.colorWhite);
		T2.setCellpadding(0);
		T2.setCellpadding(0);
		T2.setWidth("100%");
		Table T = new Table(8, inputcount + 1);
		T.setWidth("100%");
		T.setCellpadding(2);
		T.setCellspacing(1);
		T.setColumnAlignment(1, "right");
		T.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		T.setRowColor(1, getHeaderColor());
		int col = 1;
		T.add(getHeader("Nr"), col++, 1);
		if (ifAttributes) {
			T.add(getHeader(localize("connection", "Connection")), col++, 1);
		}
		T.add(getHeader(localize("name", "Name")), col++, 1);
		T.add(getHeader(localize("amount", "Amount")), col++, 1);
		//T.add(getHeader(localize("info","Info")),5,1);
		T.add(getHeader(localize("account_key", "Account key")), col++, 1);
		if (ifIndices) {
			T.add(getHeader(localize("index", "Index")), col++, 1);
			T.add(getHeader(localize("updated", "Updated")), col++, 1);
		}
		T.add(getHeader(localize("delete", "Delete")), col++, 1);

		Tariff tariff;
		Iterator iter = tariffs.iterator();
		for (int i = 1; i <= inputcount; i++) {
			col = 1;
			String rownum = String.valueOf(i);
			String hid = "-1";
			TextInput nameInput, priceInput, infoInput;
			DropdownMenu drpAtt = null, drpAK, drpIx = null;
			HiddenInput idInput;
			CheckBox delCheck;

			drpAK = drpAccountKeys(AK, ("te_akdrp" + i));
			if (ifIndices)
				drpIx = drpIndicesByType(M.values(), "te_ixdrp" + i);

			if (ifAttributes) {
				drpAtt = (DropdownMenu) attDrp.clone();
				drpAtt.setName("te_attdrp" + i);
			}

			nameInput = getTextInput("te_nameinput" + i);
			priceInput = getTextInput("te_priceinput" + i);
			infoInput = getTextInput("te_infoinput" + i);

			//drpAtt = this.drpLodgings("te_attdrp"+i,"",XL,BL,FL,CL,TL);
			//drpAK = this.drpAccountKeys(AK,"te_akdrp"+i,"");

			//int pos;
			if (i <= count && !ifnew && iter.hasNext()) {
				//pos = i - 1;
				tariff = (Tariff) iter.next();
				float iPrice = tariff.getPrice();

				if (ifAttributes)
					drpAtt.setSelectedElement(tariff.getTariffAttribute());

				if (ifIndices) {
					String ixType = tariff.getIndexType();
					String ixDate = iwc.getParameter("te_ixdate" + i);
					IWTimestamp ixdate = null;

					if (ixDate != null) {
						ixdate = new IWTimestamp(ixDate);
					} else if (tariff.getIndexUpdated() != null) {
						ixdate = new IWTimestamp(tariff.getIndexUpdated());
						T.add(new HiddenInput("te_ixdate" + i, ixdate.toString()));
					}

					if (updateIndex && ixType != null && M != null && M.containsKey(ixType)) {
						TariffIndex ti = (TariffIndex) M.get(ixType);
						java.sql.Timestamp stamp = ti.getDate();
						if (ixdate != null) {
							if (!stamp.equals(ixdate.getTimestamp())) {
								iPrice = iPrice * getAddFactor(ti.getNewValue(), ti.getOldValue());
							}
							//System.err.println(stamp.toString() +" "+ixdate.toString());
						} else
							iPrice = iPrice * getAddFactor(ti.getNewValue(), ti.getOldValue());
					}
					drpIx.setSelectedElement(ixType);
				}
				iPrice =
					new Float(TextSoap.decimalFormat((double) iPrice, iNumberOfDecimals))
						.floatValue();

				if (bRoundAmounts)
					iPrice = Math.round((double) iPrice);

				nameInput.setContent(tariff.getName());
				if (tariff.getInfo() != null)
					infoInput.setContent(tariff.getInfo());

				priceInput.setContent(String.valueOf(iPrice));

				//drpAtt.setSelectedElement(tariff.getTariffAttribute());
				System.out.println("i = " + i);
				System.out.println(
					"String.valueOf(tariff.getAccountKeyId())"
						+ String.valueOf(tariff.getAccountKeyId()));

				drpAK.setSelectedElement(String.valueOf(tariff.getAccountKeyId()));

				delCheck = getCheckBox("te_delcheck" + i, "true");
				hid = tariff.getPrimaryKey().toString();
				

				T.add(delCheck, 8, i + 1);
			}

			idInput = new HiddenInput("te_idinput" + i, (hid));

			nameInput.setSize(20);
			priceInput.setSize(8);
			infoInput.setSize(30);

			

			T.add(getHeader(rownum), col++, i + 1);
			if (ifAttributes) {
				T.add(drpAtt, col++, i + 1);
				drpAtt = (DropdownMenu) setStyle(drpAtt,STYLENAME_INTERFACE);
			}
			T.add(nameInput, col++, i + 1);
			T.add(priceInput, col++, i + 1);
			//T.add(infoInput,col++,i+1);
			T.add(drpAK, col++, i + 1);
			if (ifIndices) {
				
				T.add(drpIx, col++, i + 1);
			}
			//T.add(indexCheck,col++,i+1);
			T.add(idInput);
		}
		Table T3 = new Table(8, 1);
		T3.setWidth(Table.HUNDRED_PERCENT);
		T3.setWidth(5, 1, "100%");
		T3.setColumnAlignment(6, "right");
		T3.setColumnAlignment(7, "right");

		if (ifIndices) {
			SubmitButton update =
				new SubmitButton("updateindex", localize("update", "Update"));
			update = (SubmitButton)setStyle(update,STYLENAME_INTERFACE_BUTTON);
			T3.add(update, 8, 1);
		}
		SubmitButton save =
			new SubmitButton("savetariffs", localize("save", "Save"));
		save = (SubmitButton) setStyle(save,STYLENAME_INTERFACE_BUTTON);
		Table T4 = new Table();
		T4.add(save);
		T2.add(T3, 1, 1);
		T2.add(T, 1, 2);
		T2.setAlignment(1,3,"right");
		T2.add(T4, 1, 3);
		BorderTable.add(T2);
		BorderTable.add(new HiddenInput(prmGroup,group.getPrimaryKey().toString()));
		BorderTable.add(new HiddenInput("te_count", String.valueOf(inputcount)));
		//myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
		BorderTable.add(BorderTable);
		BorderTable.add(Finance.getCategoryParameter(iCategoryId));
		//myForm.maintainAllParameters();
		return (BorderTable);
	}

	private PresentationObject doUpdate(IWContext iwc, TariffGroup group)
		throws java.rmi.RemoteException ,FinderException{
		//Map mapOfIndices = Finder.mapOfIndicesByTypes(Finder.listOfTypeGroupedIndices());
		Map mapOfIndices = getFinanceService().mapOfTariffIndicesByTypes();
		int tariffCount = Integer.parseInt(iwc.getParameter("te_count"));
		String sName, sInfo, sDel, sPrice,sOldPrice, sAtt, sAK, sIndex, sIndexStamp;
		Integer ID;
		boolean bIndex;
		
		Integer tariffGroupID = Integer.valueOf(iwc.getParameter(prmGroup));

		for (int i = 1; i < tariffCount + 1; i++) {
			
			ID = Integer.valueOf(iwc.getParameter("te_idinput" + i));
			//System.out.println("processing "+ID);
			sName = iwc.getParameter("te_nameinput" + i);
			sPrice = (iwc.getParameter("te_priceinput" + i));
			sOldPrice = (iwc.getParameter("te_oldprice"+i));
			sDel = iwc.getParameter("te_delcheck" + i);
			sIndex = (iwc.getParameter("te_ixdrp" + i));
			
			sIndexStamp = iwc.getParameter("te_ixdate" + i);
			IWTimestamp stamp = sIndexStamp != null ? new IWTimestamp(sIndexStamp) : null;
			if (stamp == null && sIndex != null && mapOfIndices != null && mapOfIndices.containsKey(sIndex)) {
				stamp = new IWTimestamp(((TariffIndex) mapOfIndices.get(sIndex)).getDate());
			}
			// SHALL WE DELETE ?
			if (sDel != null ) {
				//System.err.println("deletion");
				try {
					getFinanceService().removeTariff(ID);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				} catch (RemoveException e) {
					e.printStackTrace();
				}
				//FinanceBusiness.deleteTariff(ID);
			}
			// IF SINGLE EDIT LINE, THEN UPDATE
			else if(sName !=null && !"".equals(sName)){
				//System.err.println("full form save");
				sInfo = iwc.getParameter("te_infoinput" + i);
				sAtt = iwc.getParameter("te_attdrp" + i);
				sAK = (iwc.getParameter("te_akdrp" + i));
				
				
				if (sIndex != null && !sIndex.equals("-1")) {
					bIndex = true;
				} else {
					bIndex = false;
					sIndex = "";
				}
				java.sql.Timestamp indexStamp = stamp != null ? stamp.getTimestamp() : null;
				try {
					//FinanceBusiness.saveTariff(	ID,sName,	sInfo,sAtt,sIndex,bIndex,indexStamp,Float.parseFloat(sPrice),Integer.parseInt(sAK),	tariffGroupID);
					getFinanceService().createOrUpdateTariff(ID,sName,	sInfo,sAtt,sIndex,bIndex,indexStamp,Float.parseFloat(sPrice),Integer.valueOf(sAK),	tariffGroupID);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				} catch (CreateException e) {
					e.printStackTrace();
				}
			}
			// IF PRICE UPDATE
			else if(sPrice!=null && sOldPrice!=null){
				//System.out.println("prices not null");
				if(!sPrice.equals(sOldPrice)){
					//System.out.println("prices not same");
					java.sql.Timestamp indexStamp = stamp != null ? stamp.getTimestamp() : null;
					getFinanceService().updateTariffPrice(ID,Float.valueOf(sPrice),indexStamp);
					//FinanceBusiness.updateTariffPrice(ID,Float.parseFloat(sPrice),indexStamp);
				}
			}
			

	
		} // for loop

		return getSingleLineChange(iwc, false, false, group);
	}

	private Hashtable getKeys(Collection AK) {
		Hashtable h = new Hashtable();
		if (AK != null) {
			for (Iterator iter = AK.iterator(); iter.hasNext();) {
				AccountKey T = (AccountKey) iter.next();
				h.put(new Integer(T.getID()), T.getName());
			}
		}
		return h;

	}

	private DropdownMenu drpAccountKeys(Collection AK, String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		if (AK != null) {
			drp.addMenuElements(AK);
		}
		return drp;
	}

	private DropdownMenu drpAttributes(Collection list, Map map, String name, String selected) {

		DropdownMenu drp = new DropdownMenu(name);
		if (list != null) {
			Iterator I = list.iterator();
			String me;
			while (I.hasNext()) {
				me = (String) I.next();
				if (map.containsKey(me))
					drp.addMenuElement(me, (String) map.get(me));
			}
			drp.setSelectedElement(selected);
		}
		return drp;
	}

	private DropdownMenu drpIndicesByType(Collection L, String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElementFirst("-1", "--"); //localize("index","Index"));
		if (L != null) {
			for (Iterator iter = L.iterator(); iter.hasNext();) {
				TariffIndex ti = (TariffIndex) iter.next();
				drp.addMenuElement(ti.getType(), ti.getName());
			}
			drp.setSelectedElement("-1");
		}
		return drp;
	}

	private float getDifferenceFactor(float now, float then) {
		float factor = (now - then) / then;
		return factor;
	}

	private float getAddFactor(float now, float then) {
		return 1 + getDifferenceFactor(now, then);
	}

	private float findIndexDifferenceFactor(TariffIndex[] ti) {
		float now = 1;
		float then = 1;
		try {

			if (ti.length > 0) {
				now = ti[0].getIndex();
				if (ti.length > 0) {
					then = ti[1].getIndex();
				}
			}

		} catch (Exception ex) {
		}
		float factor = (now - then) / then;
		return factor;
	}

	private float findLastTariffIndex(TariffIndex[] ti) {
		float f = 1;
		if (ti.length > 0)
			f = ti[0].getIndex();
		return f;
	}

	public void main(IWContext iwc) {
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		control(iwc);
	}

}
