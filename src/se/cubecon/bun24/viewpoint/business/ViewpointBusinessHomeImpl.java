package se.cubecon.bun24.viewpoint.business;

import com.idega.business.IBOHomeImpl;
import javax.ejb.CreateException;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public class ViewpointBusinessHomeImpl extends IBOHomeImpl
    implements ViewpointBusinessHome {

    protected Class getBeanInterfaceClass(){
        return ViewpointBusiness.class;
    }

    public ViewpointBusiness create() throws CreateException{
        return (ViewpointBusiness) createIBO();
    }
}
