/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import com.idega.data.IDOLookup;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.block.image.presentation.GolfImage;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class GolfBagPrinter extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		Page jPage = getParentPage();
		jPage.setMarginHeight(0);
		jPage.setMarginWidth(0);
		jPage.setLeftMargin(0);
		jPage.setTopMargin(0);
		jPage.setTitle("Prenta pokamerki");

		String[] members = modinfo.getParameterValues("member_id");

		Table myTable = new Table(3, 3);
		//myTable.setColor("#000000");
		myTable.setCellpadding(0);
		myTable.setCellspacing(1);
		myTable.setBorder(0);
		myTable.setHeight("100%");
		myTable.setWidth("100%");
		myTable.setRowAlignment(1, "center");
		myTable.setRowAlignment(2, "center");
		myTable.setRowAlignment(3, "center");
		myTable.setRowVerticalAlignment(1, "bottom");
		myTable.setRowVerticalAlignment(2, "bottom");
		myTable.setRowVerticalAlignment(3, "bottom");
		myTable.setWidth(1, "32%");
		myTable.setWidth(2, "36%");
		myTable.setWidth(3, "32%");
		myTable.setHeight(1, "33%");
		myTable.setHeight(2, "33%");
		myTable.setHeight(3, "33%");

		for (int a = 0; a < members.length; a++) {

			Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(members[a]));
			UnionMemberInfo[] unionMember = (UnionMemberInfo[]) ((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class)).findAllByColumn("member_id", members[a], "member_status", "A");

			Image memberImage = new GolfImage(member.getImageId());
			memberImage.setHeight(160);
			memberImage.setWidth(107);
			memberImage.setVerticalSpacing(6);
			memberImage.setHorizontalSpacing(6);
			Text memberText = new Text(member.getName());
			memberText.setFontSize(3);
			memberText.setBold();
			Text memberNumber = new Text("");
			memberNumber.setFontSize(3);
			memberNumber.setBold();

			if (unionMember.length > 0) {
				memberNumber.setText("nr. " + unionMember[0].getMemberNumber());
			}
			else {
				memberNumber.setText("nr. 0");
			}

			Table memberTable = new Table(1, 2);
			memberTable.setColor("#FFFFFF");
			memberTable.setWidth("90%");
			memberTable.setHeight("100%");
			memberTable.setHeight(1, "100%");
			memberTable.setRowAlignment(1, "center");
			memberTable.setRowAlignment(2, "center");
			memberTable.setVerticalAlignment(1, 1, "middle");
			memberTable.setVerticalAlignment(1, 2, "middle");

			memberTable.add(memberImage, 1, 2);
			memberTable.add(memberText, 1, 1);
			memberTable.addBreak(1, 1);
			memberTable.add(memberNumber, 1, 1);

			if (a < 3) {
				myTable.setColor(a + 1, 1, "#FFFFFF");
				myTable.add(memberTable, a + 1, 1);
			}
			if (a > 2 && a < 6) {
				myTable.setColor(a - 2, 2, "#FFFFFF");
				myTable.add(memberTable, a - 2, 2);
			}
			if (a > 5) {
				myTable.setColor(a - 5, 3, "#FFFFFF");
				myTable.add(memberTable, a - 5, 3);
			}

		}

		add(myTable);
	}
}