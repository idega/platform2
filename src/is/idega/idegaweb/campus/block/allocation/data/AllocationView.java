/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.data;

import javax.ejb.FinderException;

import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

/**
 * A wrapper class for the resultSet for select * from v_allocation_view
 * 
 * @author palli
 * @version 1.0
 */
public class AllocationView {
	protected int _catId;
	protected int _typeId; 
	protected int _complexId;
	protected String _typeName;
	protected String _complexName;
	protected int _total;
	protected int _avail;
	protected int _choice1;
	protected int _choice2;
	protected int _choice3;

	
	/**
	 * Constructor for AllocationView.
	 * 
	 * @param catId The apartment category id
	 * @param typeId The apartment type id
	 * @param complexId The apartment complex id
	 * @param typeName The apartment type name
	 * @param complexName The apartment complex name
	 * @param totalAprt The total number of rentable apartments in with these id's
	 * @param availAprt The number of available apartments, ie free to rent out
	 * @param choice1 The number of applications on the waiting list with this apartmenttype as choice 1
	 * @param choice2 The number of applications on the waiting list with this apartmenttype as choice 2
	 * @param choice3 The number of applications on the waiting list with this apartmenttype as choice 3
	 */
	public AllocationView(int catId, int typeId, int complexId, String typeName, String complexName, int totalAprt, int availAprt, int choice1, int choice2, int choice3) {
		_catId = catId;
		_typeId = typeId;
		_complexId = complexId;
		_typeName = typeName;
		_complexName = complexName;
		_total = totalAprt;
		_avail = availAprt;
		_choice1 = choice1;
		_choice2 = choice2;
		_choice3 = choice3;
	}

	public int getCategoryId() {
		return _catId;
	}

	public int getTypeId() {
		return _typeId;
	}

	public int getComplexId() {
		return _complexId;
	}
	
	public String getTypeName() {
		return _typeName;	
	}
	
	public String getComplexName() {
		return _complexName;	
	}
	
	public int getTotalNumberOfApartments() {
		return _total;
	}
	
	public int getNumberOfFreeApartments() {
		return _avail;
	}
	
	public int getNumberOfChoice1() {
		return _choice1;
	}

	public int getNumberOfChoice2() {
		return _choice2;
	}

	public int getNumberOfChoice3() {
		return _choice3;
	}
	
	public Complex getComplex() {
		if (_complexId > 0) {
			try {
				ComplexHome home =  (ComplexHome) IDOLookup.getHome(Complex.class);
				Complex complex = home.findByPrimaryKey(new Integer(_complexId));
				
				return complex;
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public ApartmentType getApartmentType() {
		if (_typeId > 0) {
			try {
				ApartmentTypeHome home =  (ApartmentTypeHome) IDOLookup.getHome(ApartmentType.class);
				ApartmentType type = home.findByPrimaryKey(new Integer(_typeId));
				
				return type;
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
}

/*
create view v_complex_type (bu_aprt_type_id, bu_complex_id) as 
SELECT DISTINCT a.bu_aprt_type_id, c.bu_complex_id 
FROM bu_aprt_type a, bu_complex c, bu_building bu, bu_floor fl, bu_apartment apa
WHERE a.bu_aprt_type_id = apa.bu_aprt_type_id
and apa.bu_floor_id = fl.bu_floor_id
and fl.bu_building_id = bu.bu_building_id
and bu.bu_complex_id = c.bu_complex_id;
//
create view v_cam_aprt_type_complex (bu_aprt_cat_id, bu_aprt_type_id,aprt_type_name,bu_complex_id,complex_name) as
SELECT DISTINCT a.bu_aprt_cat_id, a.bu_aprt_type_id, a.name, c.bu_complex_id, c.name
FROM bu_aprt_type a, bu_complex c, bu_building bu, bu_floor fl, bu_apartment apa
WHERE a.bu_aprt_type_id = apa.bu_aprt_type_id
and apa.bu_floor_id = fl.bu_floor_id
and fl.bu_building_id = bu.bu_building_id
and bu.bu_complex_id = c.bu_complex_id
//
create view v_available_aprt (bu_complex_id, bu_aprt_type_id, total_aprt) as 
select c.bu_complex_id, t.bu_aprt_type_id, count(*) from bu_apartment a,bu_floor f,bu_building b,bu_complex c,bu_aprt_type t 
where a.bu_aprt_type_id = t.bu_aprt_type_id 
and a.bu_floor_id = f.bu_floor_id 
and f.bu_building_id = b.bu_building_id 
and b.bu_complex_id = c.bu_complex_id 
group by c.bu_complex_id, t.bu_aprt_type_id;
//
create view v_rented_aprt (bu_complex_id, bu_aprt_type_id, rented_aprt) as 
select c.bu_complex_id, t.bu_aprt_type_id, count(con.cam_contract_id) 
from bu_apartment a left join cam_contract con on a.bu_apartment_id = con.bu_apartment_id
and con.status = 'S', bu_building b, bu_complex c, bu_aprt_type t, bu_floor f
where a.bu_aprt_type_id = t.bu_aprt_type_id 
and a.bu_floor_id = f.bu_floor_id 
and f.bu_building_id = b.bu_building_id 
and b.bu_complex_id = c.bu_complex_id 
group by c.bu_complex_id, t.bu_aprt_type_id;
//
create view v_waiting_lists (bu_complex_id, bu_aprt_type_id, choice, applied) as 
select v.bu_complex_id, v.bu_aprt_type_id, wl.choice_number, count(distinct wl.cam_waiting_list_id) 
from v_complex_type v left join cam_waiting_list wl on v.bu_complex_id = wl.bu_complex_id 
and v.bu_aprt_type_id = wl.bu_apartment_type_id
group by v.bu_complex_id, v.bu_aprt_type_id, wl.choice_number;
//
create view v_waiting_list1 (bu_complex_id, bu_aprt_type_id, applied1) as 
select v.bu_complex_id, v.bu_aprt_type_id, sum(wl.applied) 
from v_complex_type v left join v_waiting_lists wl on v.bu_complex_id = wl.bu_complex_id 
and v.bu_aprt_type_id = wl.bu_aprt_type_id and wl.choice = 1
group by v.bu_complex_id, v.bu_aprt_type_id;
//
create view v_waiting_list2 (bu_complex_id, bu_aprt_type_id, applied2) as 
select v.bu_complex_id, v.bu_aprt_type_id, sum(wl.applied) 
from v_complex_type v left join v_waiting_lists wl on v.bu_complex_id = wl.bu_complex_id 
and v.bu_aprt_type_id = wl.bu_aprt_type_id and wl.choice = 2
group by v.bu_complex_id, v.bu_aprt_type_id;
//
create view v_waiting_list3 (bu_complex_id, bu_aprt_type_id, applied3) as 
select v.bu_complex_id, v.bu_aprt_type_id, sum(wl.applied) 
from v_complex_type v left join v_waiting_lists wl on v.bu_complex_id = wl.bu_complex_id 
and v.bu_aprt_type_id = wl.bu_aprt_type_id and wl.choice = 3
group by v.bu_complex_id, v.bu_aprt_type_id;
//
create view v_allocation_view2 (bu_aprt_cat_id, bu_aprt_type_id, bu_complex_id, type_name, 
complex_name, total_aprt, avail_aprt, choice1, choice2, choice3) as
select v.bu_aprt_cat_id, v.bu_aprt_type_id,  v.bu_complex_id, 
v.aprt_type_name, v.complex_name, avail.total_aprt, avail.total_aprt - rented.rented_aprt,
l1.applied1, l2.applied2, l3.applied3
from v_cam_aprt_type_complex v, v_available_aprt avail, v_rented_aprt rented, v_waiting_list1 l1,
v_waiting_list2 l2, v_waiting_list3 l3
where avail.bu_complex_id = v.bu_complex_id
and avail.bu_aprt_type_id = v.bu_aprt_type_id
and rented.bu_complex_id = v.bu_complex_id
and rented.bu_aprt_type_id = v.bu_aprt_type_id
and l1.bu_complex_id = v.bu_complex_id
and l1.bu_aprt_type_id = v.bu_aprt_type_id
and l2.bu_complex_id = v.bu_complex_id
and l2.bu_aprt_type_id = v.bu_aprt_type_id
and l3.bu_complex_id = v.bu_complex_id
and l3.bu_aprt_type_id = v.bu_aprt_type_id;
*/