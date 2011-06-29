package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractTariff;
import is.idega.idegaweb.campus.block.allocation.data.ContractTariffName;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.block.finance.data.TariffIndex;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class ContractTariffWindow extends CampusWindow {
	private static final String PARAM_COUNT = "te_count";
	private static final String PARAM_EDIT_ID = "edit_id";
	private static final String PARAM_DELETE = "del";
	private static final String PARAM_INDEX = "index";
	private static final String PARAM_ACCOUNT_KEY = "account_key";
	private static final String PARAM_AMOUNT = "amount";
	private static final String PARAM_NAME = "name";
	private static final String PARAM_ID_INPUT = "idinput";
	private static final String PARAMETER_SAVE = "save";
	private static final String PARAMETER_SAVE_TO_NAME = "save_to_name";

	private static final String PARAMETER_CONTRACT_TARIFF_NAME = "con_tar_name";

	public final static String prmContractId = "cam_contract_id";

	private Contract contract = null;
	private Integer contractId = new Integer(-1);

	public ContractTariffWindow() {
		setWidth(800);
		setHeight(500);
		setResizable(true);
	}

	protected void control(IWContext iwc) {
		init(iwc);
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			if (doSaveContractTariff(iwc, null)) {
				add(getHeader(localize("tariff_saved", "Tariff saved")));
			} else {
				add(getHeader(localize("tariff_not_saved", "Tariff not saved")));
			}
		} else if (iwc.isParameterSet(PARAMETER_SAVE_TO_NAME)) {
			if (iwc.isParameterSet(PARAMETER_CONTRACT_TARIFF_NAME)) {
				String contractTariffName = iwc
						.getParameter(PARAMETER_CONTRACT_TARIFF_NAME);
				if (doSaveContractTariff(iwc, contractTariffName)) {
					add(getHeader(localize("tariff_saved", "Tariff saved")));
				} else {
					add(getHeader(localize("tariff_not_saved",
							"Tariff not saved")));
				}
			}
		}

		add(getEditTable(iwc));

	}

	private void init(IWContext iwc) {
		contractId = Integer.valueOf(iwc.getParameter(prmContractId));
		try {
			contract = getContractService(iwc).getContractHome()
					.findByPrimaryKey(contractId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}

	private DropdownMenu getAccountKeyDropdown(Collection accoutKeys,
			String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		if (accoutKeys != null) {
			for (Iterator iter = accoutKeys.iterator(); iter.hasNext();) {
				AccountKey key = (AccountKey) iter.next();
				drp.addMenuElement(key.getPrimaryKey().toString(),
						key.getName() + " (" + key.getInfo() + ")");
			}
			drp.setSelectedElement(selected);
		}
		return drp;
	}

	private DropdownMenu getIndexDropdown(Collection indeces, String name,
			String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElementFirst("-1", "--"); // localize("index","Index"));
		if (indeces != null) {
			for (Iterator iter = indeces.iterator(); iter.hasNext();) {
				TariffIndex ti = (TariffIndex) iter.next();
				drp.addMenuElement(ti.getType(), ti.getName());
			}
			drp.setSelectedElement(selected);
		}

		return drp;
	}

	public FinanceService getFinanceService(IWApplicationContext iwac)
			throws RemoteException {
		return (FinanceService) IBOLookup.getServiceInstance(iwac,
				FinanceService.class);
	}

	private PresentationObject getEditTable(IWContext iwc) {
		int iEditID = -1;
		String sEditID = iwc.getParameter(PARAM_EDIT_ID);
		if (sEditID != null) {
			iEditID = Integer.parseInt(sEditID);
		}

		DataTable T = new DataTable();
		T.setWidth("100%");
		T.setTitlesHorizontal(true);
		SubmitButton save = new SubmitButton(PARAMETER_SAVE, localize("save",
				"Save"));
		SubmitButton saveToName = new SubmitButton(PARAMETER_SAVE_TO_NAME,
				localize("save_to_name", "Save as named"));


		int row = 1;
		int col = 1;

		T.add(getHeader("Nr"), col++, row);
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("amount", "Amount")), col++, row);
		T.add(getHeader(localize("account_key", "Account key")), col++, row);
		T.add(getHeader(localize("index", "Index")), col++, row);
		T.add(getHeader(localize("updated", "Updated")), col++, row);
		T.add(getHeader(localize("delete", "Delete")), col++, row);
		T.add(getHeader(localize("contractTariffName", "Contract tariff name")), col++, row);
		row++;

		ContractTariffName ctn = null;
		
		Collection contractTariffEntries = getContractTariffEntries(iwc);
		if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
			contractTariffEntries = createContractTariffEntries(iwc);
		}
		int count = 0;

		if (contractTariffEntries != null && !contractTariffEntries.isEmpty()) {
			Map mapOfIndices = null;
			Collection accountKeys = null;
			try {
				accountKeys = getFinanceService(iwc).getAccountKeyHome()
						.findByCategory(new Integer(36));
				mapOfIndices = getFinanceService(iwc)
						.mapOfTariffIndicesByTypes();
			} catch (Exception e) {
				e.printStackTrace();
			}

			count = contractTariffEntries.size() + 5;

			Iterator it = contractTariffEntries.iterator();
			int i = 0;
			while (it.hasNext()) {
				col = 1;
				ContractTariff t = (ContractTariff) it.next();
				HiddenInput idInput = new HiddenInput(PARAM_ID_INPUT + i, t
						.getPrimaryKey().toString());
				if (((Integer) t.getPrimaryKey()).intValue() == iEditID) {
					T.add(getText(Integer.toString(row - 1)), col++, row);
					TextInput nameInput = new TextInput(PARAM_NAME + i);
					nameInput.setValue(t.getName());
					T.add(nameInput, col++, row);
					FloatInput amountInput = new FloatInput(PARAM_AMOUNT + i);
					amountInput.setValue(t.getPrice());
					T.add(amountInput, col++, row);
					DropdownMenu accDrop = getAccountKeyDropdown(accountKeys,
							PARAM_ACCOUNT_KEY + i, t.getAccountKey()
									.getPrimaryKey().toString());
					T.add(accDrop, col++, row);

					String selIndex = "-1";
					if (t.getIndexType() != null) {
						selIndex = t.getIndexType();
					}

					DropdownMenu indexDrop = getIndexDropdown(
							mapOfIndices.values(), PARAM_INDEX + i, selIndex);
					T.add(indexDrop, col++, row);

					if (t.getIndexUpdated() != null) {
						T.add(getText(t.getIndexUpdated().toString()), col++,
								row);
					} else {
						T.add(getText(""), col++, row);
					}
					T.add(new CheckBox(PARAM_DELETE + i), col++, row);
					if (t.getContractTariffName() != null) {
						T.add(getText(t.getContractTariffName().getName()), col++, row++);
						ctn = t.getContractTariffName();
					} else {
						T.add(getText(""), col++, row++);												
					}

				} else {
					T.add(getText(Integer.toString(row - 1)), col++, row);
					Link lineChangeLink = new Link(getText(t.getName()));
					lineChangeLink.addParameter(PARAM_EDIT_ID,
							((Integer) t.getPrimaryKey()).intValue());
					lineChangeLink.addParameter(prmContractId, contract
							.getPrimaryKey().toString());

					T.add(lineChangeLink, col++, row);

					T.add(getText(Float.toString(t.getPrice())), col++, row);
					T.add(getText(t.getAccountKey().getName() + " ("
							+ t.getAccountKey().getInfo() + ")"), col++, row);
					if (t.getIndexType() != null) {
						if (mapOfIndices != null
								&& mapOfIndices.containsKey(t.getIndexType())) {
							TariffIndex ti = (TariffIndex) mapOfIndices.get(t
									.getIndexType());
							T.add(getText(ti.getName()), col++, row);
						} else {
							T.add(getText(t.getIndexType()), col++, row);
						}
					} else {
						T.add(getText(""), col++, row);
					}
					if (t.getIndexUpdated() != null) {
						T.add(getText(t.getIndexUpdated().toString()), col++,
								row);
					} else {
						T.add(getText(""), col++, row);
					}
					T.add(new CheckBox(PARAM_DELETE + i), col++, row);
					if (t.getContractTariffName() != null) {
						T.add(getText(t.getContractTariffName().getName()), col++, row++);
						ctn = t.getContractTariffName();
					} else {
						T.add(getText(""), col++, row++);												
					}

				}

				T.add(idInput);

				i++;
			}

			// add 5 empty lines
			for (; i < count; i++) {
				T.add(getText(Integer.toString(row - 1)), 1, row);
				T.add(new TextInput(PARAM_NAME + i), 2, row);
				T.add(new FloatInput(PARAM_AMOUNT + i), 3, row);
				T.add(getAccountKeyDropdown(accountKeys, PARAM_ACCOUNT_KEY + i,
						"-1"), 4, row);
				T.add(getIndexDropdown(mapOfIndices.values(), PARAM_INDEX + i,
						"-1"), 5, row);
				T.add(getText(""), 6, row);
				T.add(getText(""), 7, row);
				T.add(new HiddenInput(PARAM_ID_INPUT + i, "-1"));
				T.add(getText(""), 8, row++);
			}
				
			if (ctn == null) {
				row+=2;
				T.add(getText(localize("contractTariffName", "Contract tariff name")), 2, row);
				T.add(new TextInput(PARAMETER_CONTRACT_TARIFF_NAME), 3, row);	
			}			
		} else {
			T.add(getText("Unable to create entries for contract"));
		}
		
		T.addButton(save);
		if (ctn == null) {
			T.addButton(saveToName);
		}

		Form F = new Form();
		F.add(new HiddenInput(prmContractId, contract.getPrimaryKey()
				.toString()));
		F.add(new HiddenInput(PARAM_COUNT, String.valueOf(count)));
		F.add(T);
		return F;
	}

	private Collection getContractTariffEntries(IWContext iwc) {
		try {
			return getContractService(iwc).getContractTariffHome()
					.findByContract(contract);
		} catch (Exception e) {
		}

		return null;
	}

	private ContractTariff insertContractTariffEntry(IWContext iwc,
			Tariff tariff) {
		try {
			ContractTariff ct = getContractService(iwc).getContractTariffHome()
					.create();
			ct.setAccountKey(tariff.getAccountKeyId());
			ct.setContract(contract);
			ct.setIndexType(tariff.getIndexType());
			ct.setIndexUpdated(tariff.getIndexUpdated());
			ct.setIsDeleted(false);
			ct.setName(tariff.getName());
			ct.setPrice(tariff.getPrice());
			ct.setUseIndex(tariff.getUseIndex());
			ct.store();

			return ct;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Collection createContractTariffEntries(IWContext iwc) {
		Collection ret = new ArrayList();
		try {
			Collection tariffs = ((TariffHome) IDOLookup.getHome(Tariff.class))
					.findByTariffGroup(new Integer(9));
			Tariff tariff;
			char cAttribute;
			int attributeId = -1;

			// For each tariff (Inner loop)
			for (Iterator iter2 = tariffs.iterator(); iter2.hasNext();) {
				tariff = (Tariff) iter2.next();
				String sAttribute = tariff.getTariffAttribute();
				// If we have an tariff attribute
				if (sAttribute != null) {
					attributeId = -1;
					cAttribute = sAttribute.charAt(0);
					// If All
					if (cAttribute == BuildingCacher.CHARALL) {
						insertContractTariffEntry(iwc, tariff);
					}
					// other than all
					else {
						// attribute check
						if (sAttribute.length() >= 3) {
							attributeId = Integer.parseInt(sAttribute
									.substring(2));
							switch (cAttribute) {
							case BuildingCacher.CHARTYPE:
								// Apartment type
								if (attributeId == contract.getApartment()
										.getApartmentTypeId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARCATEGORY:
								// Apartment category
								if (attributeId == ((Integer) contract
										.getApartment().getApartmentType()
										.getApartmentSubcategory()
										.getApartmentCategory().getPrimaryKey())
										.intValue()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARBUILDING:
								// Building
								if (attributeId == contract.getApartment()
										.getFloor().getBuildingId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARFLOOR:
								// Floor
								if (attributeId == contract.getApartment()
										.getFloorId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARCOMPLEX:
								// Complex
								if (attributeId == contract.getApartment()
										.getFloor().getBuilding()
										.getComplexId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARAPARTMENT:
								// Apartment
								if (attributeId == contract.getApartmentId()
										.intValue()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							} // switch
						} // attribute check
					}
				}
			}
		} catch (Exception e) {
		}

		return ret;
	}

	private void deleteTariff(IWContext iwc, Integer id) {
		try {
			ContractTariff ct = getContractService(iwc).getContractTariffHome()
					.findByPrimaryKey(id);
			ct.setDeletedBy(iwc.getCurrentUser());
			ct.setIsDeleted(true);
			ct.store();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		}
	}

	private void updateOrCreateTariff(IWContext iwc, Integer id, String name,
			String price, String accKey, String index) {
		ContractTariff ct = null;
		if (id.intValue() > 0) {
			try {
				ct = getContractService(iwc).getContractTariffHome()
						.findByPrimaryKey(id);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		if (ct == null) {
			try {
				ct = getContractService(iwc).getContractTariffHome().create();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}

		if (ct != null) {
			ct.setAccountKey(Integer.valueOf(accKey).intValue());
			ct.setContract(contract);
			if ("-1".equals(index)) {
				ct.setIndexType(null);
				ct.setUseIndex(false);
			} else {
				ct.setIndexType(index);
				ct.setUseIndex(true);
			}
			ct.setIndexUpdated(IWTimestamp.getTimestampRightNow());
			ct.setIsDeleted(false);
			ct.setName(name);
			ct.setPrice(Float.parseFloat(price));
			//ct.setContractTariffName(ctn);
			ct.store();
		}
	}

	private boolean doSaveContractTariff(IWContext iwc,
			String contractTariffName) {
		int tariffCount = Integer.parseInt(iwc.getParameter(PARAM_COUNT));
		String name, delete, price, accKey, index;
		Integer id;
		boolean bIndex;

		ContractTariffName ctn = null;
		
		if (contractTariffName != null && !"".equals(contractTariffName)) {
			try {
				getContractService(iwc).getContractTariffNameHome().findByContract(contractTariffName);
				return false;
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
			
			try {
				ctn = getContractService(iwc).getContractTariffNameHome().create();
				ctn.setName(contractTariffName);
				ctn.store();
			} catch (Exception e) {
				return false;
			}
		}

		for (int i = 0; i < tariffCount; i++) {
			id = Integer.valueOf(iwc.getParameter(PARAM_ID_INPUT + i));
			name = iwc.getParameter(PARAM_NAME + i);
			price = (iwc.getParameter(PARAM_AMOUNT + i));
			delete = iwc.getParameter(PARAM_DELETE + i);
			index = (iwc.getParameter(PARAM_INDEX + i));

			if (delete != null && id != null && id.intValue() > 0) {
				deleteTariff(iwc, id);
			} else if (name != null && !"".equals(name)) {
				accKey = (iwc.getParameter(PARAM_ACCOUNT_KEY + i));

				updateOrCreateTariff(iwc, id, name, price, accKey, index);
			}
		}

		if (ctn != null) {
			Collection tariffs = getContractTariffEntries(iwc);
			if (tariffs != null) {
				Iterator it = tariffs.iterator();
				while (it.hasNext()) {
					ContractTariff t = (ContractTariff) it.next();
					t.setContractTariffName(ctn);
					t.store();
				}
			}
		}
		
		return true;
	}

	public void main(IWContext iwc) throws Exception {
		control(iwc);
	}
}