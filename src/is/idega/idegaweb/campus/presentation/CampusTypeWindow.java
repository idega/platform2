package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.business.CampusSettings;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.ejb.FinderException;

import com.idega.block.building.presentation.ApartmentTypeViewer;
import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.Tariff;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;
import com.idega.util.Property;

/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusTypeWindow extends Window {

	public CampusTypeWindow() {
		setHeight(515);
		setWidth(420);
		// setResizable(true);
	}

	public String getBundleIdentifier() {
		return CampusSettings.IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		int id = Integer.parseInt(iwc
				.getParameter(ApartmentTypeViewer.PARAMETER_STRING));
		ApartmentTypeViewer BE = new ApartmentTypeViewer(id);
		String attributeName = iwrb.getLocalizedString("tariffs", "Tariffs");
		String today = com.idega.util.IWTimestamp.RightNow().getLocaleDate(
				iwc.getCurrentLocale());
		java.util.Collection typeTariffs = null;
		try {
			typeTariffs = ((FinanceService) IBOLookup.getServiceInstance(iwc,
					FinanceService.class)).getKeySortedTariffsByAttribute("t_"
					+ id);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		boolean showPrice = Boolean.valueOf(iwc.getApplicationSettings().getProperty("SHOW_PRICE", String.valueOf(true))).booleanValue();

		// FinanceFinder.getInstance().getKeySortedTariffsByAttribute("t_"+id);
		if (showPrice && typeTariffs != null) {
			NumberFormat format = DecimalFormat.getCurrencyInstance(iwc
					.getIWMainApplication().getSettings().getDefaultLocale());
			// String rentString = format.format((long)room.getRent());
			ArrayList list = new ArrayList();
			ArrayList publicList = new ArrayList();
			boolean usePublic = Boolean.valueOf(iwc.getApplicationSettings().getProperty("USE_PUBLIC_PRICING", String.valueOf(false))).booleanValue();
			
			java.util.Iterator iter = typeTariffs.iterator();
			float total = 0;
			float totalPublic = 0;
			
			
			while (iter.hasNext()) {
				Tariff tariff = (Tariff) iter.next();
				StringBuffer tariffString = new StringBuffer(format.format(tariff
						.getPrice()));
				StringBuffer publicTariffString = new StringBuffer(format.format(tariff
						.getPublicPrice()));
				list.add(new Property(tariff.getName(), tariffString.toString()));
				publicList.add(new Property(tariff.getName(), publicTariffString.toString()));
				total += tariff.getPrice();
				totalPublic += tariff.getPublicPrice();
			}
			if (total > 0) {
				list.add(new Property("-----------", "------------"));
				if (usePublic) {
					publicList.add(new Property("-----------", "------------"));					
				}
				String sTotalName = iwrb.getLocalizedString("total", "Total");
				StringBuffer sTotalValue = new StringBuffer(format.format(total));
				StringBuffer sPublicTotalValue = new StringBuffer(format.format(totalPublic));
				list.add(new Property(sTotalName, sTotalValue.toString()));
				publicList.add(new Property(sTotalName, sPublicTotalValue.toString()));
				BE.setSpecialAttributes(attributeName + "  " + today, list);
				if (usePublic) {
					BE.setExtraSpecialAttributes(iwrb.getLocalizedString("public", "Public") + "  " + today, publicList);
				}
			}
		}

		add(BE);
		setTitle("Apartment  Viewer");
		// addTitle("Building Editor");
	}
}
