/**
 * KrofurWSSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public interface KrofurWSSoap_PortType extends java.rmi.Remote {

    /**
     * Stofnar kröfu í innheimtukerfi Íslandsbanka.
     */
    public void stofnaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException;

    /**
     * Endurvekur kröfu sem hefur verið felld niður og breytir í samræmi
     * við kröfuna sem er send inn.
     */
    public void endurvekjaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException;

    /**
     * Fellir kröfu í innheimtukerfi Íslandsbanka.
     */
    public void fellaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException;

    /**
     * Breytir kröfu í innheimtukerfi Íslandsbanka.
     */
    public void breytaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException;

    /**
     * Stofnar kröfur sem kröfubunka í innheimtukerfi Íslandsbanka.
     */
    public java.math.BigDecimal stofnaKrofubunka(is.idega.block.finance.business.isb.ws2.Krafa[] krofur) throws java.rmi.RemoteException;

    /**
     * Sækir svör við kröfubunka sem stofnaður hefur verið í innheimtukerfi
     * Íslandsbanka.
     */
    public is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[] saekjaKrofubunkasvar(java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException;

    /**
     * Sækir greiðsluupplýsingar í innheimtukerfi Íslandsbanka. Sótt
     * er frá og með upphafsdagsetningu og til og með lokadagsetningu.
     */
    public is.idega.block.finance.business.isb.ws2.Greidsla[] saekjaGreidslurKrafnaTimabil(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar dagsetningFra, java.util.Calendar dagsetningTil, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    /**
     * Sækir greiðsluupplýsingar í innheimtukerfi Íslandsbanka. Einungis
     * koma þær greiðslur sem hafa borist frá því greiðslur voru sóttar síðast
     * með þessari aðgerð.
     */
    public is.idega.block.finance.business.isb.ws2.Greidsla[] saekjaGreidslurKrafna(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    /**
     * Sækir greiðsluupplýsingar í innheimtukerfi Íslandsbanka.
     */
    public is.idega.block.finance.business.isb.ws2.Greidsla[] saekjaGreidsluKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException;

    /**
     * Sækir kröfuupplýsingar í innheimtukerfi Íslandsbanka.
     */
    public is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[] saekjaKrofur(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar gjalddagiFra, java.util.Calendar gjalddagiTil, is.idega.block.finance.business.isb.ws2.AstandKrofu astand, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    /**
     * Sækir kröfuupplýsingar í innheimtukerfi Íslandsbanka.
     */
    public is.idega.block.finance.business.isb.ws2.UppreiknudKrafa saekjaKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException;

    /**
     * Stofna kröfuskrá í innheimtukerfi Íslandsbanka. Skráin er send
     * inn sem DIME attachment. Skilar bunkanúmeri skráarinnar sem er notað
     * til að sækja svarskeyti við stofnuninni.
     */
    public java.math.BigDecimal stofnaKrofuskra(is.idega.block.finance.business.isb.ws2.TegundKrofuskra tegund) throws java.rmi.RemoteException;

    /**
     * Sækir svarskrá við innsendri kröfuskrá með gefnu bunkanúmeri.
     * Skráin er send til baka sem DIME attachment.
     */
    public void saekjaKrofuskrasvar(is.idega.block.finance.business.isb.ws2.TegundKrofuskra tegund, java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException;

    /**
     * Sækir greiðsluupplýsingar á kröfum. Fyrirspurnin er send inn
     * sem DIME attachment. Greiðsluupplýsingarnar eru sendar til baka sem
     * DIME attachment.
     */
    public void saekjaKrofuupplysingaskra(is.idega.block.finance.business.isb.ws2.TegundKrofufyrirspurnar tegundFyrirspurnar) throws java.rmi.RemoteException;

    /**
     * Sækir allar beingreiðslubeiðnir kröfuhafa, óháð textalykli,
     * í innheimtukerfi Íslandsbanka.
     */
    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaAllarBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    /**
     * Sækir beingreiðslubeiðnir kröfuhafa fyrir ákveðinn textalykil
     * í innheimtukerfi Íslandsbanka.
     */
    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    /**
     * Sækir hreyfingar á beingreiðslubeiðnum kröfuhafa fyrir ákveðinn
     * textalykil í innheimtukerfi Íslandsbanka (nýjar og niðurfelldar beingreiðslubeiðnir
     * frá því kröfuhafi sótti beiðnirnar síðast)
     */
    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    /**
     * Sækir hreyfingar á beingreiðslubeiðnum kröfuhafa fyrir alla
     * textalykla í innheimtukerfi Íslandsbanka (nýjar og niðurfelldar beingreiðslubeiðnir
     * frá því kröfuhafi sótti beiðnirnar síðast)
     */
    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaAllarBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;
}
