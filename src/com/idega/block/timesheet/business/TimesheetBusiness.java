package com.idega.block.timesheet.business;

public class TimesheetBusiness {
	private String table_width;
	private String table_height;
	private int cellspacing;
	private int cellpadding;
	private String color_1;
	private String color_2;
	private String header_color;
	private String header_text_color;
	private int border;
	private boolean correct;
	
	
	
	private TimesheetBusiness() {
		initialize();
	}
	private void initialize() {
	}
	
//	
//	/**
//	 * 
//	 * Removes all application attributes concerning Timesheet
//	 *  
//	 */
//	public static void removeAllProjectApplicationAttributes(IWContext iwc) {
//		Enumeration enum = iwc.getServletContext().getAttributeNames();
//		String myString = "";
//		while (enum.hasMoreElements()) {
//			myString = (String) enum.nextElement();
//			if (myString.indexOf("i_timesheet_all_projects_array") != -1) {
//				iwc.removeApplicationAttribute(myString);
//			}
//		}
//	}
//	/**
//	 * 
//	 * Returns valid projects...
//	 *  
//	 */
//	public static TimesheetProject[] getAllProjects(IWContext iwc) throws SQLException {
//		TimesheetProject[] allProjects =(TimesheetProject[]) iwc.getApplicationAttribute("i_timesheet_all_projects_array");
//		if (allProjects == null) {
//			allProjects =(TimesheetProject[])	(((TimesheetProjectHome) IDOLookup.getHomeLegacy(TimesheetProject.class)).createLegacy()).findAllByColumn("valid","Y");
//			
//			iwc.setApplicationAttribute("i_timesheet_all_projects_array", allProjects);
//		}
//		return allProjects;
//	}
//	/**
//	 * 
//	 * Returns valid projects order by ProjectNumber
//	 *  
//	 */
//	public static TimesheetProject[] getAllProjectsOrderByProjectNumber(IWContext iwc) throws SQLException {
//		return getAllProjects(iwc, "project_number,name");
//	}
//	/**
//	 * 
//	 * Returns valid projects order by <i>order_by</i>
//	 *  
//	 */
//	public static TimesheetProject[] getAllProjects(IWContext iwc, String order_by) throws SQLException {
//		TimesheetProject[] allProjects =
//			(TimesheetProject[]) iwc.getApplicationAttribute("i_timesheet_all_projects_array_" + TextSoap.findAndCut(order_by, ","));		
//		if (allProjects == null) {
//			allProjects =
//				(TimesheetProject[])(((TimesheetProjectHome) IDOLookup.getHomeLegacy(TimesheetProject.class)).createLegacy()).findAllByColumnOrdered("valid","Y",order_by);
//			iwc.setApplicationAttribute("i_timesheet_all_projects_array_" + TextSoap.findAndCut(order_by, ","),allProjects);
//		}
//		return allProjects;
//	}
}
