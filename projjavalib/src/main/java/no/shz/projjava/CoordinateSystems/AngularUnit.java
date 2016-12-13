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
 * Definition of angular units.
 */
public class AngularUnit extends Info implements IAngularUnit {
    private double _radiansPerUnit;

    /**
     * Initializes a new instance of a angular unit
     *
     * @param radiansPerUnit Radians per unit
     */
    public AngularUnit(double radiansPerUnit) {
        this(radiansPerUnit, "", "", -1, "", "", "");
    }

    /**
     * Initializes a new instance of a angular unit
     *
     * @param radiansPerUnit Radians per unit
     * @param name           Name
     * @param authority      Authority name
     * @param authorityCode  Authority-specific identification code.
     * @param alias          Alias
     * @param abbreviation   Abbreviation
     * @param remarks        Provider-supplied remarks
     */
    public AngularUnit(double radiansPerUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _radiansPerUnit = radiansPerUnit;
    }

    /**
     * The angular degrees are PI/180 = 0.017453292519943295769236907684886 radians
     */
    public static AngularUnit getDegrees() {
        return new AngularUnit(0.017453292519943295769236907684886, "degree", "EPSG", 9102, "deg", "", "=pi/180 radians");
    }

    /**
     * SI standard unit
     */
    public static AngularUnit getRadian() {
        return new AngularUnit(1, "radian", "EPSG", 9101, "rad", "", "SI standard unit.");
    }

    /**
     * Pi / 200 = 0.015707963267948966192313216916398 radians
     */
    public static AngularUnit getGrad() {
        return new AngularUnit(0.015707963267948966192313216916398, "grad", "EPSG", 9105, "gr", "", "=pi/200 radians.");
    }

    /**
     * Pi / 200 = 0.015707963267948966192313216916398 radians
     */
    public static AngularUnit getGon() {
        return new AngularUnit(0.015707963267948966192313216916398, "gon", "EPSG", 9106, "g", "", "=pi/200 radians.");
    }

    /**
     * Gets or sets the number of radians per <see cref="AngularUnit"/>.
     */
    public final double getRadiansPerUnit() {
        return _radiansPerUnit;
    }

    public final void setRadiansPerUnit(double value) {
        _radiansPerUnit = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "UNIT[\"%1$s\", %2$s", getName(), _nfi.format(getRadiansPerUnit())));
        if (!StringUtility.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0) {
            sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object.
     */
    @Override
    public String getXML() {
        return String.format(Locale.US, "<CS_AngularUnit RadiansPerUnit=\"%1$s\">%2$s</CS_AngularUnit>", _nfi.format(getRadiansPerUnit()), getInfoXml());
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
        if (!(obj instanceof AngularUnit)) {
            return false;
        }
        return ((AngularUnit) ((obj instanceof AngularUnit) ? obj : null)).getRadiansPerUnit() == this.getRadiansPerUnit();
    }
}