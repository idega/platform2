package com.idega.block.building.presentation;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingBMPBean;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class ApartmentChooser extends BuildingEditor {
	
	public String zebraColorOne = "#942829";
	public String zebraColorTwo = "#21304a";
	public String fontColor = "#FFFFFF";
	
	public void main(IWContext iwc) {
		try {
			String apartment = iwc.getParameter("dr_id");
			if (apartment != null) {
				iwc.setSessionAttribute("dr_id", iwc.getParameter("dr_id"));
				this.getParentPage().setParentToReload();
				this.getParentPage().close();
			}
			else {
				this.getParentPage().setTitle("Appartment Chooser");
				this.getParentPage().setAllMargins(0);
				add(getApartments());
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	private PresentationObject getApartments() {
		int border = 0;
		int padding = 6;
		int spacing = 1;
		try {
			//Complex[] C =
			// (Complex[])(((ComplexHome)IDOLookup.getHomeLegacy(Complex.class)).createLegacy()).findAll();
			Collection complexes = ((ComplexHome) IDOLookup.getHome(Complex.class)).findAll();
			//int clen = Cs.size();
			int b = 1, f = 1;
			Table T = new Table();
			if (complexes != null && !complexes.isEmpty()) {
				T.setRowVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
				T.setCellpadding(padding);
				T.setCellspacing(spacing);
				T.setVerticalZebraColored(zebraColorOne, zebraColorTwo);
				T.setBorder(border);
				int col = 0;
				for (Iterator iter = complexes.iterator(); iter.hasNext();) {
					Complex complex = (Complex) iter.next();
					col++;
					T.add(getHeaderText(complex.getName()), col, 1);
					Collection buildings = ((BuildingHome) IDOLookup.getHome(Building.class)).findByComplex(complex);
					//Building[] B =
					// (Building[])(((BuildingHome)IDOLookup.getHomeLegacy(Building.class)).createLegacy()).findAllByColumnOrdered(BuildingBMPBean.getComplexIdColumnName(),String.valueOf(C[i].getID()),BuildingBMPBean.getNameColumnName());
					//int blen = B.length;
					if (buildings != null && !buildings.isEmpty()) {
						Table BuildingTable = new Table();
						BuildingTable.setCellpadding(padding);
						BuildingTable.setCellspacing(spacing);
						BuildingTable.setBorder(border);
						T.add(BuildingTable, col, 2);
						b = 1;
						//for (int j = 0; j < blen; j++) {
						for (Iterator biterator = buildings.iterator(); biterator.hasNext();) {
							Building building = (Building) biterator.next();
							BuildingTable.add(getHeaderText(building.getName()), 1, b++);
							//Floor[] F =
							// (Floor[])(((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.FloorBMPBean.getBuildingIdColumnName(),String.valueOf(B[j].getID()),com.idega.block.building.data.FloorBMPBean.getNameColumnName());
							Collection floors = ((FloorHome) IDOLookup.getHome(Floor.class)).findByBuilding(building);
							//int flen = F.length;
							if (floors != null && !floors.isEmpty()) {
								Table FloorTable = new Table();
								FloorTable.setCellpadding(padding);
								FloorTable.setCellspacing(spacing);
								FloorTable.setBorder(border);
								BuildingTable.add(FloorTable, 1, b++);
								f = 1;
								//for (int k = 0; k < flen; k++) {
								for (Iterator fiterator = floors.iterator(); fiterator.hasNext();) {
									Floor floor = (Floor) fiterator.next();
									FloorTable.add(getHeaderText(floor.getName()), 1, f++);
									//Apartment[] A =
									// (Apartment[])(((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.ApartmentBMPBean.getFloorIdColumnName(),String.valueOf(F[k].getID()),com.idega.block.building.data.ApartmentBMPBean.getNameColumnName());
									Collection apartments =
										((ApartmentHome) IDOLookup.getHome(Apartment.class)).findByFloor(floor);
									if (apartments != null && !apartments.isEmpty()) {
										Table ApartmentTable = new Table();
										ApartmentTable.setCellpadding(padding);
										ApartmentTable.setBorder(border);
										ApartmentTable.setCellspacing(spacing);
										FloorTable.add(ApartmentTable, 1, f++);
										int arow = 1;
										for (Iterator aiterator = apartments.iterator(); aiterator.hasNext();) {
											Apartment apartment = (Apartment) aiterator.next();
											ApartmentTable.add(
												getApLink(apartment.getPrimaryKey().toString(), apartment.getName()),
												1,
												arow++);
										}
									}
								}
							}
						}
					}
				}
			}
			T.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
			T.setVerticalZebraColored(zebraColorOne, zebraColorTwo);
			return T;
		}
		catch (Exception sql) {
			return new Text("Database didn't return  anything");
		}
	}
	private Text getHeaderText(String s) {
		Text T = new Text(s);
		T.setBold();
		T.setFontColor(fontColor);
		return T;
	}
	private Link getATLink(int id, String name) {
		Link L = new Link(name);
		L.setFontColor(fontColor);
		L.addParameter("dr_id", id);
		//L.addParameter(sAction,TYPE);
		//L.addParameter("bm_choice",TYPE);
		return L;
	}
	private Link getApLink(String id, String name) {
		Link L = new Link(name);
		L.setFontColor(fontColor);
		L.addParameter("dr_id", id);
		//L.addParameter(sAction,APARTMENT);
		//L.addParameter("bm_choice",APARTMENT);
		return L;
	}
} // class AppartmentChooser
