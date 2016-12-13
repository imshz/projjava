package no.shz.projjava.coordinateSystems;

/**
 * Copyright 2005 - 2009 - Morten Nielsen (www.sharpgis.net)
 * This file is part of ProjNet.
 * ProjNet is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProjNet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ProjNet; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * ProjJava - ported by imshz (https://github.com/imshz/projjava)
 *
 * ProjJava is a android compatible point-to-point coordinate conversions java library.
 *
 * This library is a port of Proj.NET - ProjNet is free software published under
 * the terms of GNU Lesser General Public License, as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProjJava is published under the same license and will allways follow the same
 * restrictions as the original project.
 */

import java.util.Locale;

import no.shz.utilities.StringUtility;

/**
 * Horizontal datum defining the standard datum information.
 */
public class HorizontalDatum extends Datum implements IHorizontalDatum {
    private IEllipsoid _ellipsoid;
    private Wgs84ConversionInfo _wgs84ConversionInfo;

    /**
     * Initializes a new instance of a horizontal datum
     *
     * @param ellipsoid    Ellipsoid
     * @param toWgs84      Parameters for a Bursa Wolf transformation into WGS84
     * @param type         Datum type
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param abbreviation Abbreviation
     * @param remarks      Provider-supplied remarks
     */
    public HorizontalDatum(IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84, DatumType type, String name, String authority, long code, String alias, String remarks, String abbreviation) {
        super(type, name, authority, code, alias, remarks, abbreviation);
        _ellipsoid = ellipsoid;
        _wgs84ConversionInfo = toWgs84;
    }

    /**
     * EPSG's WGS 84 datum has been the then current realisation. No distinction is made between the original WGS 84
     * frame, WGS 84 (G730), WGS 84 (G873) and WGS 84 (G1150). Since 1997, WGS 84 has been maintained within 10cm of
     * the then current ITRF.
     * <p>
     * <p>Area of use: World</p>
     * <p>Origin description: Defined through a consistent set of station coordinates. These have changed with time: by 0.7m
     * on 29/6/1994 [WGS 84 (G730)], a further 0.2m on 29/1/1997 [WGS 84 (G873)] and a further 0.06m on
     * 20/1/2002 [WGS 84 (G1150)].</p>
     */
    public static HorizontalDatum getWGS84() {
        return new HorizontalDatum(Ellipsoid.getWGS84(), null, DatumType.HD_Geocentric, "World Geodetic System 1984", "EPSG", 6326, "", "EPSG's WGS 84 datum has been the then current realisation. No distinction is made between the original WGS 84 frame, WGS 84 (G730), WGS 84 (G873) and WGS 84 (G1150). Since 1997, WGS 84 has been maintained within 10cm of the then current ITRF.", "");
    }

    /**
     * World Geodetic System 1972
     * <p>
     * <p>Used by GPS before 1987. For Transit satellite positioning see also WGS 72BE. Datum code 6323 reserved for southern hemisphere ProjCS's.</p>
     * <p>Area of use: World</p>
     * <p>Origin description: Developed from a worldwide distribution of terrestrial and
     * geodetic satellite observations and defined through a set of station coordinates.</p>
     */
    public static HorizontalDatum getWGS72() {
        HorizontalDatum datum = new HorizontalDatum(Ellipsoid.getWGS72(), null, DatumType.HD_Geocentric, "World Geodetic System 1972", "EPSG", 6322, "", "Used by GPS before 1987. For Transit satellite positioning see also WGS 72BE. Datum code 6323 reserved for southern hemisphere ProjCS's.", "");
        datum.setWgs84Parameters(new Wgs84ConversionInfo(0, 0, 4.5, 0, 0, 0.554, 0.219));
        return datum;
    }

    /**
     * European Terrestrial Reference System 1989
     * <p>
     * <p>Area of use:
     * Europe: Albania; Andorra; Austria; Belgium; Bosnia and Herzegovina; Bulgaria; Croatia;
     * Cyprus; Czech Republic; Denmark; Estonia; Finland; Faroe Islands; France; Germany; Greece;
     * Hungary; Ireland; Italy; Latvia; Liechtenstein; Lithuania; Luxembourg; Malta; Netherlands;
     * Norway; Poland; Portugal; Romania; San Marino; Serbia and Montenegro; Slovakia; Slovenia;
     * Spain; Svalbard; Sweden; Switzerland; United Kingdom (UK) including Channel Islands and
     * Isle of Man; Vatican City State.</p>
     * <p>Origin description: Fixed to the stable part of the Eurasian continental
     * plate and consistent with ITRS at the epoch 1989.0.</p>
     */
    public static HorizontalDatum getETRF89() {
        HorizontalDatum datum = new HorizontalDatum(Ellipsoid.getGRS80(), null, DatumType.HD_Geocentric, "European Terrestrial Reference System 1989", "EPSG", 6258, "ETRF89", "The distinction in usage between ETRF89 and ETRS89 is confused: although in principle conceptually different in practice both are used for the realisation.", "");
        datum.setWgs84Parameters(new Wgs84ConversionInfo());
        return datum;
    }

    /**
     * European Datum 1950
     * <p>
     * <p>Area of use:
     * Europe - west - Denmark; Faroe Islands; France offshore; Israel offshore; Italy including San
     * Marino and Vatican City State; Ireland offshore; Netherlands offshore; Germany; Greece (offshore);
     * North Sea; Norway; Spain; Svalbard; Turkey; United Kingdom UKCS offshore. Egypt - Western Desert.
     * </p>
     * <p>Origin description: Fundamental point: Potsdam (Helmert Tower).
     * Latitude: 52 deg 22 min 51.4456 sec N; Longitude: 13 deg  3 min 58.9283 sec E (of Greenwich).</p>
     */
    public static HorizontalDatum getED50() {
        return new HorizontalDatum(Ellipsoid.getInternational1924(), new Wgs84ConversionInfo(-87, -98, -121, 0, 0, 0, 0), DatumType.HD_Geocentric, "European Datum 1950", "EPSG", 6230, "ED50", "", "");
    }

    /**
     * Gets or sets the ellipsoid of the datum
     */
    public final IEllipsoid getEllipsoid() {
        return _ellipsoid;
    }

    public final void setEllipsoid(IEllipsoid value) {
        _ellipsoid = value;
    }

    /**
     * Gets preferred parameters for a Bursa Wolf transformation into WGS84
     */
    public final Wgs84ConversionInfo getWgs84Parameters() {
        return _wgs84ConversionInfo;
    }

    public final void setWgs84Parameters(Wgs84ConversionInfo value) {
        _wgs84ConversionInfo = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DATUM[\"%1$s\", %2$s", getName(), _ellipsoid.getWKT()));
        if (_wgs84ConversionInfo != null) {
            sb.append(String.format(", %1$s", _wgs84ConversionInfo.getWKT()));
        }
        if (!StringUtility.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0) {
            sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    @Override
    public String getXML() {
        return String.format(Locale.US, "<CS_HorizontalDatum DatumType=\"%1$s\">%2$s%3$s%4$s</CS_HorizontalDatum>", getDatumType().getValue(), getInfoXml(), getEllipsoid().getXML(), (getWgs84Parameters() == null ? "" : getWgs84Parameters().getXML()));
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    @Override
    public boolean equalParams(Object obj) {
        if (!(obj instanceof HorizontalDatum)) {
            return false;
        }
        HorizontalDatum datum = (HorizontalDatum) ((obj instanceof HorizontalDatum) ? obj : null);
        if (datum.getWgs84Parameters() == null && this.getWgs84Parameters() != null) {
            return false;
        }
        if (datum.getWgs84Parameters() != null && !datum.getWgs84Parameters().equals(this.getWgs84Parameters())) {
            return false;
        }
        return (datum != null && this.getEllipsoid() != null && datum.getEllipsoid().equalParams(this.getEllipsoid()) || datum == null && this.getEllipsoid() == null) && this.getDatumType() == datum.getDatumType();
    }
}