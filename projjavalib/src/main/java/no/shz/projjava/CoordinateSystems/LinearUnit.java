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
 * Definition of linear units.
 */
public class LinearUnit extends Info implements ILinearUnit {
    private double _metersPerUnit;

    /**
     * Creates an instance of a linear unit
     *
     * @param metersPerUnit Number of meters per <see cref="LinearUnit" />
     * @param name          Name
     * @param authority     Authority name
     * @param authorityCode Authority-specific identification code.
     * @param alias         Alias
     * @param abbreviation  Abbreviation
     * @param remarks       Provider-supplied remarks
     */
    public LinearUnit(double metersPerUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _metersPerUnit = metersPerUnit;
    }

    /**
     * Returns the meters linear unit.
     * Also known as International metre. SI standard unit.
     */
    public static ILinearUnit getMetre() {
        return new LinearUnit(1.0, "metre", "EPSG", 9001, "m", "", "Also known as International metre. SI standard unit.");
    }

    /**
     * Returns the foot linear unit (1ft = 0.3048m).
     */
    public static ILinearUnit getFoot() {
        return new LinearUnit(0.3048, "foot", "EPSG", 9002, "ft", "", "");
    }

    /**
     * Returns the US Survey foot linear unit (1ftUS = 0.304800609601219m).
     */
    public static ILinearUnit getUSSurveyFoot() {
        return new LinearUnit(0.304800609601219, "US survey foot", "EPSG", 9003, "American foot", "ftUS", "Used in USA.");
    }

    /**
     * Returns the Nautical Mile linear unit (1NM = 1852m).
     */
    public static ILinearUnit getNauticalMile() {
        return new LinearUnit(1852, "nautical mile", "EPSG", 9030, "NM", "", "");
    }

    /**
     * Returns Clarke's foot.
     * <p>
     * <p>
     * Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre.
     * Used in older Australian, southern African &amp; British West Indian mapping.
     */
    public static ILinearUnit getClarkesFoot() {
        return new LinearUnit(0.3047972654, "Clarke's foot", "EPSG", 9005, "Clarke's foot", "", "Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre. Used in older Australian, southern African & British West Indian mapping.");
    }

    /**
     * Gets or sets the number of meters per <see cref="LinearUnit"/>.
     */
    public final double getMetersPerUnit() {
        return _metersPerUnit;
    }

    public final void setMetersPerUnit(double value) {
        _metersPerUnit = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "UNIT[\"%1$s\", %2$s", getName(), _nfi.format(getMetersPerUnit())));
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
        return String.format(Locale.US, "<CS_LinearUnit MetersPerUnit=\"%1$s\">%2$s</CS_LinearUnit>", _nfi.format(getMetersPerUnit()), getInfoXml());
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
        if (!(obj instanceof LinearUnit)) {
            return false;
        }
        return ((LinearUnit) ((obj instanceof LinearUnit) ? obj : null)).getMetersPerUnit() == this.getMetersPerUnit();
    }
}