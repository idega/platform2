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

import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.ApartmentSubcategoryHome;
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
	protected int _subcatId; 
	protected int _total;
	protected int _avail;
	protected int _choice1;
	protected int _choice2;
	protected int _choice3;

	
	/**
	 * Constructor for AllocationView.
	 * 
	 * @param catId The apartment category id
	 * @param subcatId The apartment subcategory id
	 * @param totalAprt The total number of rentable apartments in with these id's
	 * @param availAprt The number of available apartments, ie free to rent out
	 * @param choice1 The number of applications on the waiting list with this apartmenttype as choice 1
	 * @param choice2 The number of applications on the waiting list with this apartmenttype as choice 2
	 * @param choice3 The number of applications on the waiting list with this apartmenttype as choice 3
	 */
	public AllocationView(int catId, int subcatId, int totalAprt, int availAprt, int choice1, int choice2, int choice3) {
		_catId = catId;
		_subcatId = subcatId;
		_total = totalAprt;
		_avail = availAprt;
		_choice1 = choice1;
		_choice2 = choice2;
		_choice3 = choice3;
	}

	public int getCategoryId() {
		return _catId;
	}

	public int getSubcategoryId() {
		return _subcatId;
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
	
	
	public ApartmentSubcategory getSubcategory() {
		if (_subcatId > 0) {
			try {
				ApartmentSubcategoryHome home =  (ApartmentSubcategoryHome) IDOLookup.getHome(ApartmentSubcategory.class);
				ApartmentSubcategory subcat = home.findByPrimaryKey(new Integer(_subcatId));
				
				return subcat;
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
create view v_available_aprt (bu_aprt_sub_cat_id, total_aprt) as 
select s.bu_aprt_sub_cat_id, count(*) from bu_apartment a,bu_aprt_type t,bu_aprt_sub_cat s 
where a.bu_aprt_type_id = t.bu_aprt_type_id 
and t.bu_aprt_subcat = s.bu_aprt_sub_cat_id 
group by s.bu_aprt_sub_cat_id;
//
create view v_rented_aprt (bu_aprt_sub_cat_id, rented_aprt) as 
select s.bu_aprt_sub_cat_id, count(con.cam_contract_id) 
from bu_apartment a left join cam_contract con on a.bu_apartment_id = con.bu_apartment_id
//and con.status = 'S', bu_aprt_type t, bu_aprt_sub_cat s
and con.status in ('S', 'T', 'E', 'U') and con.rented = 'Y', bu_aprt_type t, bu_aprt_sub_cat s
where a.bu_aprt_type_id = t.bu_aprt_type_id 
and t.bu_aprt_subcat = s.bu_aprt_sub_cat_id 
group by s.bu_aprt_sub_cat_id;
//
create view v_waiting_lists (bu_aprt_sub_cat_id, choice, applied) as 
select s.bu_aprt_sub_cat_id, wl.choice_number, count(distinct wl.cam_waiting_list_id) 
from bu_aprt_sub_cat s left join cam_waiting_list wl on s.bu_aprt_sub_cat_id = wl.bu_subcategory_id
group by s.bu_aprt_sub_cat_id, wl.choice_number;
//
create view v_waiting_list1 (bu_aprt_sub_cat_id, applied1) as 
select s.bu_aprt_sub_cat_id, sum(wl.applied) 
from bu_aprt_sub_cat s left join v_waiting_lists wl on s.bu_aprt_sub_cat_id = wl.bu_aprt_sub_cat_id and wl.choice = 1
group by s.bu_aprt_sub_cat_id;
//
create view v_waiting_list2 (bu_aprt_sub_cat_id, applied2) as 
select s.bu_aprt_sub_cat_id, sum(wl.applied) 
from bu_aprt_sub_cat s left join v_waiting_lists wl on s.bu_aprt_sub_cat_id = wl.bu_aprt_sub_cat_id and wl.choice = 2
group by s.bu_aprt_sub_cat_id;
//
create view v_waiting_list3 (bu_aprt_sub_cat_id, applied3) as 
select s.bu_aprt_sub_cat_id, sum(wl.applied) 
from bu_aprt_sub_cat s left join v_waiting_lists wl on s.bu_aprt_sub_cat_id = wl.bu_aprt_sub_cat_id and wl.choice = 3
group by s.bu_aprt_sub_cat_id;
//
create view v_allocation_view2 (bu_aprt_cat_id, bu_aprt_sub_cat_id, total_aprt, avail_aprt, choice1, choice2, choice3) as
select s.aprt_cat, s.bu_aprt_sub_cat_id, avail.total_aprt, avail.total_aprt - rented.rented_aprt,
l1.applied1, l2.applied2, l3.applied3
from bu_aprt_sub_cat s, v_available_aprt avail, v_rented_aprt rented, v_waiting_list1 l1,
v_waiting_list2 l2, v_waiting_list3 l3
where avail.bu_aprt_sub_cat_id = s.bu_aprt_sub_cat_id
and rented.bu_aprt_sub_cat_id = s.bu_aprt_sub_cat_id
and l1.bu_aprt_sub_cat_id = s.bu_aprt_sub_cat_id
and l2.bu_aprt_sub_cat_id = s.bu_aprt_sub_cat_id
and l3.bu_aprt_sub_cat_id = s.bu_aprt_sub_cat_id
*/