 package is.idega.idegaweb.golf.service;

 import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.Union;

import com.idega.data.IDOLookup;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author
 * @version 1.0
 */
 public class MemberInsertion{

	private Member m_oMember;
        private Group[] m_oGroupArr;
	private Address[] m_oAddressArr;
	private Union m_oUnion;
	private Family m_oFamily;
	private Phone[] m_oPhoneArr;
        private MemberInfo m_oMemInfo;
        private String m_strMembershipType;
        private String[] m_strPhoneTypes;
        private String strActiveMember = "A";

        public MemberInsertion(Member member, Address[] addressArr, Union union, Phone[] phoneArr, MemberInfo handicap, Group[] group){

            m_oMember = member;
            m_oAddressArr = addressArr;
            m_oUnion = union;
            m_oGroupArr = group;
            m_oMemInfo = handicap;
            m_oPhoneArr = phoneArr;
            m_strMembershipType = "main";
            m_strPhoneTypes = new String[]{"Sími", "GSM", "Vinnusími"};
        }

        public void setMembershipType(String mstype) {
            m_strMembershipType = mstype;
        }

        public void setPhoneTypes(String[] phoneTypes) {
            m_strPhoneTypes = phoneTypes;
        }

        public void setMemberStatus(String activeMember) {
            this.strActiveMember = activeMember;
        }

	public String insert() {
            int memberID = -1;
            if(m_oMember == null || m_oAddressArr == null || m_oUnion == null || m_oPhoneArr == null
                || m_oMemInfo == null)
                return "nullari";

            try {
                m_oMember.setimage_id(1);
                m_oMember.insert();
                memberID = m_oMember.getID();
                m_oMember.addTo(m_oUnion, "MEMBERSHIP_TYPE", m_strMembershipType);
                m_oMember.addTo(m_oUnion, "ACTIVE_MEMBER", strActiveMember);
                for(int i = 0; i < m_oGroupArr.length; i++) {
                    m_oMember.addTo(m_oGroupArr[i]);
                    //m_oGroupArr[i].addTo(m_oMember);
                }

                for(int i = 0; i < m_oAddressArr.length; i++) {
                    if(m_oAddressArr[i] != null && m_oAddressArr[i].getCountryId() != -1) {
                        m_oAddressArr[i].insert();
                        m_oAddressArr[i].addTo(m_oMember);
                    }
                }

                for(int i = 0; i < m_oPhoneArr.length; i++) {
                    if(m_oPhoneArr[i] != null && m_oPhoneArr[i].getCountryId() != -1){
                        m_oPhoneArr[i].insert();
                        m_oPhoneArr[i].addTo(m_oMember);
                    }
                }
                m_oMemInfo.setMemberId(memberID);
                m_oMemInfo.insert();
            }
            catch(Exception e) {
                e.printStackTrace(System.err);
                return e.getMessage();
             }

             return ""+memberID;
    }

    public String update() {

            if(m_oMember == null || m_oAddressArr == null || m_oUnion == null || m_oPhoneArr == null
                || m_oMemInfo == null)
                return "Óskilgreint";
            int counter = 0;

            try {
                m_oMember.update();
                for(int i = 0; i < m_oGroupArr.length; i++) {
                    m_oMember.addTo(m_oGroupArr[i]);
                }

                if(m_oAddressArr[0] != null)
                    counter = m_oMember.getNumberOfRecordsReverseRelated((Address) IDOLookup.instanciateEntity(Address.class));

                //System.out.println("\n\nAddress counter: "+counter+" Address array length: "+m_oPhoneArr.length+"\n\n");
                for(int i = 0; i < m_oAddressArr.length; i++) {
                    if(m_oAddressArr[i] != null && m_oAddressArr[i].getCountryId() != -1) {
                        if(i > 2)
                            break;
                        else if(i < counter) {
                            m_oAddressArr[i].update();
                            System.out.println("\n\nfer i Address update\n\n");
                        }
                        else {
                            m_oAddressArr[i].insert();
                            m_oAddressArr[i].addTo(m_oMember);
                            System.out.println("\n\nfer i Address insert\n\n");
                        }
                    }
                }
                counter = 0;
                if(m_oPhoneArr[0] != null)
                    counter = m_oMember.getNumberOfRecordsReverseRelated((Phone) IDOLookup.instanciateEntity(Phone.class));
                    //counter = m_oPhoneArr[0].getNumberOfRecordsRelated(new Member());

                System.out.println("\n\ncounter: "+counter+" array length: "+m_oPhoneArr.length+"\n\n");

                for(int i = 0; i < m_oPhoneArr.length; i++) {
                    System.out.println("\n\ncounter: "+counter+" array length: "+m_oPhoneArr.length+"\n\n");
                    if(m_oPhoneArr[i] != null && m_oPhoneArr[i].getCountryId() != -1){
                        if(i > 3)
                            break;
                        if(i < counter) {
                            m_oPhoneArr[i].update();
                            System.out.println("\n\nfer i phone update\n\n");
                        }
                        else {
                            m_oPhoneArr[i].insert();
                            m_oPhoneArr[i].addTo(m_oMember);
                            System.out.println("\n\nfer i phone insert\n\n");
                        }
                    }
                }
                m_oMemInfo.update();
            }
            catch(Exception e) {
                e.printStackTrace(System.err);
                return e.getMessage();
             }

             return "";
    }
}