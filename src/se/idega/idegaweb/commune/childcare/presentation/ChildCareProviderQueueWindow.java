/*
 * Created on 6.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Window;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChildCareProviderQueueWindow extends Window {

	private CommuneBlock style = new CommuneBlock();
	private Text 
		HEADER_ORDER =
			style.getSmallHeader(style.localize("ccpqw_order", "Queue number")),
		HEADER_QUEUE_DATE =
			style.getSmallHeader(style.localize("ccpqw_queue_date", "Queue date")),	
		HEADER_FROM_DATE =
			style.getSmallHeader(style.localize("ccpqw_from_date", "Placement date")),
		PROVIDER = style.getSmallText(style.localize("ccpqw_provider", "Provider") + ":"),
		PROGNOSIS = style.getSmallText(style.localize("ccpqw_prognosisr", "Prognosis") + ":");
	private String
		CLOSE = style.localize("ccpqw_close", "Close");
		
				
	public void main(IWContext iwc) throws Exception {
		
		String providerId = iwc.getParameter(CCConstants.PROVIDER_ID);
		String appId = iwc.getParameter(CCConstants.APPID);		
		School school = getChildCareBusiness(iwc).getSchoolBusiness().getSchool(providerId);
		
		
		ChildCarePrognosis prognosis = getChildCareBusiness(iwc).getPrognosis(Integer.parseInt(providerId));
						
		//todo: (Roar) localize
		String prognosisText = prognosis == null ? "No prognosis available" :
			"Three months: " + prognosis.getThreeMonthsPrognosis()+
			"  One year: " + prognosis.getOneYearPrognosis() +
			"  Updated date: " + prognosis.getUpdatedDate();	
		
		Table appTbl = new Table();
		
//		add(new Text("ProviderId: " + providerId));
		if (providerId != null){
			Collection applications = getChildCareBusiness(iwc).getOpenAndGrantedApplicationsByProvider(new Integer(providerId).intValue());
			
			Iterator i = applications.iterator();
			
			appTbl.add(HEADER_ORDER, 1, 1);
			appTbl.add(HEADER_QUEUE_DATE, 2, 1);
			appTbl.add(HEADER_FROM_DATE, 3, 1);
			appTbl.setRowColor(1, style.getHeaderColor());			
	
			int row = 2;
			
			while(i.hasNext()){
				ChildCareApplication app = (ChildCareApplication) i.next();
				
				Text queueOrder = style.getSmallText("" + getChildCareBusiness(iwc).getNumberInQueue(app)),
					queueDate = style.getSmallText(app.getQueueDate().toString()),
					fromDate = style.getSmallText(app.getFromDate().toString());
//					currentAppId = style.getSmallText(""+app.getNodeID());   //debug only
				
				appTbl.add(queueOrder, 1, row);
				appTbl.add(queueDate, 2, row);
				appTbl.add(fromDate, 3, row);
//				appTbl.add(currentAppId, 4, row);  //debug only
			
				if (app.getNodeID() == new Integer(appId).intValue()){
					emphasizeText(queueOrder);
					emphasizeText(queueDate);
					emphasizeText(fromDate);
				}
				
				if (row % 2 == 0) {
					appTbl.setRowColor(row, style.getZebraColor1());
				} else {
					appTbl.setRowColor(row, style.getZebraColor2());
				}				
		
				row++;
				
			}
			
		}
		
		Table layoutTbl = new Table();		
		layoutTbl.add(PROVIDER, 1, 1);
		layoutTbl.add(style.getSmallText(school.getName()), 2, 1);	

		layoutTbl.setRowHeight(2, "20px");	
		
		layoutTbl.add(PROGNOSIS, 1, 3);
		layoutTbl.add(style.getSmallText(prognosisText), 2, 3);		
			
		layoutTbl.setRowHeight(4, "20px");
			
		layoutTbl.add(appTbl, 1, 5);
		layoutTbl.mergeCells(1, 5, 2, 5);
		
		CloseButton closeBtn = new CloseButton(CLOSE);
		closeBtn.setAsImageButton(true);
		layoutTbl.add(closeBtn, 2, 6);
		layoutTbl.setAlignment(2, 6, "right");
	

		add(layoutTbl);
	
	}
	
	private Text emphasizeText(Text t){
		t.setBold(true);
		t.setStyleAttribute("color:blue");	
		return t;
	}
	
	/**
	 * Method getChildCareBusiness returns the ChildCareBusiness object.
	 * @param iwc
	 * @return ChildCareBusiness
	 */
	ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (
				ChildCareBusiness) com
					.idega
					.business
					.IBOLookup
					.getServiceInstance(
				iwc,
				ChildCareBusiness.class);
		} catch (RemoteException e) {
			return null;
		}
	}	
}
