package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusinessBean;
import is.idega.idegaweb.golf.startingtime.data.TeeTime;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;
/**
 * @author gimmi
 */
public class TeeTimeReport extends GolfBlock {

	public final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";
	public static final String PARAMETER_DATE = "sr_prm_d";
	public static final String PARAMETER_FIELD_ID = "sr_prm_f";
	public static final String PARAMETER_VIEW = "sr_prm_v";
	
	private final String VIEW_DAY = "D";
	private final String VIEW_WEEK = "W";
	private String VIEW = VIEW_DAY;
	private int numberInGroup = 4;

	private TeeTimeBusinessBean ttBus;
	private IWResourceBundle iwrb;
	private IWTimestamp stamp;
	private Field field;
	private HashMap map;
	private NumberFormat nf = NumberFormat.getInstance();;

	public void main(IWContext modinfo) throws Exception{
		String sDate = modinfo.getParameter(PARAMETER_DATE);
		String sFieldId = modinfo.getParameter(PARAMETER_FIELD_ID);
		String sView = modinfo.getParameter(PARAMETER_VIEW);
		iwrb = getResourceBundle(modinfo);
		
		if (sFieldId != null) {
			ttBus = new TeeTimeBusinessBean();
			field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(sFieldId));
			nf.setMaximumFractionDigits(2);
			if (sView != null && sView.length() > 0) {
				VIEW = sView;	
			}
			if (sDate != null) {
				stamp = new IWTimestamp(sDate);
			}	else {
				stamp = IWTimestamp.RightNow();
			}
			report(modinfo);
				
		} else {
			add(getText(iwrb.getLocalizedString("startingtime.no_field_selected","No field selected")));
		}
	}
	
	private void report(IWContext modinfo) throws SQLException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_FIELD_ID);
		form.add(getNavigationTable());
		
		if (VIEW.equals(VIEW_DAY)) {
			addDayReport(modinfo, form);
		}else if (VIEW.equals(VIEW_WEEK)) {
			addWeekReport(modinfo, form);
		}else {
			add("VIEW = "+VIEW);
		}
		
		add(form);
	}
	
	private Table getNavigationTable() {
		Table table = getTable();
		
		Link day = getLink(iwrb.getLocalizedString("startingtime.day","Day"));
		day.addParameter(PARAMETER_VIEW, VIEW_DAY);
		day.addParameter(PARAMETER_FIELD_ID, field.getID());
		day.addParameter(PARAMETER_DATE, stamp.toSQLDateString());

		Link week = getLink(iwrb.getLocalizedString("startingtime.week","Week"));
		week.addParameter(PARAMETER_VIEW, VIEW_WEEK);
		week.addParameter(PARAMETER_FIELD_ID, field.getID());
		week.addParameter(PARAMETER_DATE, stamp.toSQLDateString());
		
		DateInput dateI = new DateInput(PARAMETER_DATE);
		dateI.setYearRange(2001, IWTimestamp.RightNow().getYear()+1);
		dateI.setDate(stamp.getSQLDate());
		
		GenericButton submit = getButton(new SubmitButton(localize("start.choose","Choose")));
		
		table.add(day, 1, 1);
		table.add(" ", 1, 1);
		table.add(week, 1, 1);
		table.mergeCells(1, 1, 2, 1);
		table.add(new HiddenInput(PARAMETER_VIEW, VIEW), 1, 1);
		table.add(dateI, 1, 2);
		table.add(submit, 2, 2);
		table.setAlignment(2, 2, "right");
		
		return table;
	}
	
	private void addDayReport(IWContext modinfo, Form form) throws SQLException {
		Table table = getTable();
		form.add(table);
		
		StartingtimeFieldConfig fConfig = ttBus.getFieldConfig(field.getID(), stamp);
		
		IWTimestamp begin = new IWTimestamp(fConfig.getOpenTime());
		IWTimestamp end = new IWTimestamp(fConfig.getCloseTime());
		
		int interval = fConfig.getMinutesBetweenStart();
		int currentHour = begin.getHour();
		
		int row = 1;
		int grupNum = 0;
		int[] count = new int[]{0,0};
		int totalCount = 0;
		int totalTurnUp = 0;
		int firstGrupNum = 0;
		
		int extraAverageStart = 16;
		int extraAverageEnd = 20;
		int extraCount = 0;
		int extraGrupNum = 0;
		int extraTurnUp = 0;
		
		table.mergeCells(1, row, 2, row);
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.daily_report_for","Daily report for")+" "+field.getName()+" - "+stamp.getLocaleDate(modinfo)), 1, row);

		++row;
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.time","Time")), 1, row);	
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.usage","Usage")), 2, row);
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.turnup","Turn-up")), 3, row);

		while (end.isLaterThan(begin)) {
			++row;
			firstGrupNum = grupNum;
			table.add(getText(getTimeString(begin)+" - "), 1, row);

			while (currentHour == begin.getHour()) {
				++grupNum;
				begin.addMinutes(interval);
			}
			count = getCount(stamp, (firstGrupNum+1), grupNum);
			begin.addMinutes(-interval);
			table.add(getText(getTimeString(begin)), 1, row);
			begin.addMinutes(interval);
			table.add(getText(getStatString(count[0], (grupNum-firstGrupNum) * numberInGroup)), 2, row);
			table.add(getText(getStatString(count[1], count[0])), 3, row);

			if ( currentHour >= extraAverageStart && currentHour < extraAverageEnd) {
				extraCount += count[0];
				extraTurnUp += count[1];
				extraGrupNum += (grupNum - firstGrupNum);
			}

			totalTurnUp += count[1];
			totalCount += count[0];
			++currentHour;
		}
		++row;
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.average","Average")), 1, row);
		table.add(getHeaderText(getStatString(totalCount, grupNum * numberInGroup)), 2, row);
		table.add(getHeaderText(getStatString(totalTurnUp, totalCount)), 3, row);
		
		++row;
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.average","Average")+" "+extraAverageStart+"-"+extraAverageEnd), 1, row);
		table.add(getHeaderText(getStatString(extraCount, extraGrupNum * numberInGroup)), 2, row);
		table.add(getHeaderText(getStatString(extraTurnUp, extraCount)), 3, row);
	}

	private void addWeekReport(IWContext modinfo, Form form) throws SQLException {
		Table table = getTable();
		form.add(table);
		
		int row = 1;
		int wDay = stamp.getDayOfWeek();

		IWTimestamp fromStamp = new IWTimestamp(stamp);
		fromStamp.addDays(-wDay);
		IWTimestamp toStamp = new IWTimestamp(stamp);
		toStamp.addDays(7-wDay);
		IWCalendar iCal = new IWCalendar();

		setupMap(fromStamp, toStamp);
		
		Link link;
		int[] count = new int[]{0,0};
		int totalCount = 0;
		int groups = 0;
		int totalGroups = 0;
		int totalTurnUp = 0;

		table.mergeCells(1, row, 2, row);
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.weekly_report_for","Weekly report for")+" "+field.getName()), 1, row);

		++row;
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.day","Day")), 1, row);	
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.usage","Usage")), 2, row);
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.turnup","Turn-up")), 3, row);

		for (int i = 1; i <= 7; i++) {
			++row;
			fromStamp.addDays(1);
			link = getLink(iCal.getDayName(i, modinfo.getCurrentLocale(), iCal.SHORT)+" "+fromStamp.getLocaleDate(modinfo));
			link.addParameter(PARAMETER_VIEW, VIEW_DAY);
			link.addParameter(PARAMETER_FIELD_ID, field.getID());
			link.addParameter(PARAMETER_DATE, fromStamp.toSQLDateString());
			table.add(link, 1, row);
			count = getCount(fromStamp, -1, -1);
			groups = getLastGroupNumber(fromStamp);
			totalCount += count[0];
			totalTurnUp += count[1];
			totalGroups += groups;
			table.add(getText(getStatString(count[0], groups * numberInGroup)), 2, row);
			table.add(getText(getStatString(count[1], count[0])), 3, row);
		}
		++row;
		table.add(getHeaderText(iwrb.getLocalizedString("startingtime.average","Average")), 1, row);
		table.add(getHeaderText(getStatString(totalCount, totalGroups * numberInGroup)), 2, row);
		table.add(getHeaderText(getStatString(totalTurnUp, totalCount)), 3, row);
			
	}

	/**
	 * returns int[], int[0] = count, int[1] = showed count
	 */
	private int[] getCount(IWTimestamp timeStamp, int startGroupNum, int endGroupNum) throws SQLException {
		setupMap(timeStamp, null);
		int[] value = new int[]{0,0};
		if (startGroupNum == -1) {
			startGroupNum = 1;	
		}
		if (endGroupNum == -1) {
			endGroupNum = getLastGroupNumber(timeStamp);
		}
		int arrayPos = 0;
		
		Integer tVal;
		String key;
		Object[] obj;
		for (int i = startGroupNum; i <= endGroupNum; i++) {
			key = timeStamp.toSQLDateString()+"_"+i;
			obj = (Object[]) map.get(key);
			if (obj != null) {
				value[0] += ((Integer) obj[0]).intValue(); 	
				value[1] += ((Integer) obj[1]).intValue(); 	
			}
		}
		return value;	
	}

	private int getLastGroupNumber(IWTimestamp timeStamp) throws SQLException {
		int endGroupNum;
		StartingtimeFieldConfig fConfig = ttBus.getFieldConfig(field.getID(), timeStamp);
		
		IWTimestamp begin = new IWTimestamp(fConfig.getOpenTime());
		IWTimestamp end = new IWTimestamp(fConfig.getCloseTime());
		
		int minutesBetween = IWTimestamp.getMinutesBetween(begin, end);
		endGroupNum = minutesBetween / fConfig.getMinutesBetweenStart();
		return endGroupNum;
	}

	private void setupMap(IWTimestamp from, IWTimestamp to) throws SQLException {
		if (map == null) {
			map = new HashMap();
			if (from != null && to == null) {
				insertStampToMap(from);
			}else if(from!= null && to != null) {
				IWTimestamp temp = new IWTimestamp(from);
				while (to.isLaterThan(temp)) {
					insertStampToMap(temp);
					temp.addDays(1);	
				}
			}
		}
	}

	private void insertStampToMap(IWTimestamp tempStamp) throws SQLException {
		List times = ttBus.getStartingtimeTableEntries(tempStamp, Integer.toString(field.getID()));
		if (times != null && !times.isEmpty()) {
			String tempStampString = tempStamp.toSQLDateString();
			Iterator iter = times.iterator();
			TeeTime tt;
			int grupNum;
			boolean showed;
			Object[] value;
			while (iter.hasNext() ){
				tt = (TeeTime) iter.next();
				grupNum = tt.getGroupNum();
				showed = tt.getShowedUp();
				value = (Object[]) map.get(tempStampString+"_"+grupNum);
				if (value == null) {
					value = new Object[2];
					value[0] = new Integer(1);
					if (showed) {
						value[1] = new Integer(1);	
					} else {
						value[1] = new Integer(0);	
					}
					map.put(tempStampString+"_"+grupNum,value);
				} else {
					value[0] = new Integer( ( (Integer) value[0]).intValue()+1);
					if (showed) {
						value[1] = new Integer( ( (Integer) value[1]).intValue()+1);
					}
					map.put(tempStampString+"_"+grupNum,value);
				}
			}
		}
	}

	private String getStatString(int count, int grupNum) {
		if (grupNum == 0) {
			return Integer.toString(count)+"/"+(grupNum)+" = 0 %";
		}
		return Integer.toString(count)+"/"+(grupNum)+" = "+(nf.format( ((float) count / ( grupNum) ) * 100 ) +" %");
	}

	private String getTimeString(IWTimestamp begin) {
		return TextSoap.addZero(begin.getHour())+":"+TextSoap.addZero(begin.getMinute());
	}
	
	private Table getTable() {
		Table table = new Table();
		table.setWidth("100%");
		return table;
	}
	
	private Text getHeaderText(String content) {
		Text text = getText(content);
		text.setBold(true);
		return text;	
	}
	
	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}
}
