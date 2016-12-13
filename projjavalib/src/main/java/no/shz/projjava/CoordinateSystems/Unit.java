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
 * Class for defining units
 */
public class Unit extends Info implements IUnit {
    private double _conversionFactor;

    /**
     * Initializes a new unit
     *
     * @param conversionFactor Conversion factor to base unit
     * @param name             Name of unit
     * @param authority        Authority name
     * @param authorityCode    Authority-specific identification code.
     * @param alias            Alias
     * @param abbreviation     Abbreviation
     * @param remarks          Provider-supplied remarks
     */
    public Unit(double conversionFactor, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _conversionFactor = conversionFactor;
    }

    /**
     * Initializes a new unit
     *
     * @param name             Name of unit
     * @param conversionFactor Conversion factor to base unit
     */
    public Unit(String name, double conversionFactor) {
        this(conversionFactor, name, "", -1, "", "", "");
    }

    /**
     * Gets or sets the number of units per base-unit.
     */
    public final double getConversionFactor() {
        return _conversionFactor;
    }

    public final void setConversionFactor(double value) {
        _conversionFactor = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "UNIT[\"%1$s\", %2$s", getName(), _nfi.format(_conversionFactor)));
        if (!StringUtility.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0) {
            sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object [NOT IMPLEMENTED].
     */
    @Override
    public String getXML() {
        throw new UnsupportedOperationException();
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
        if (!(obj instanceof Unit)) {
            return false;
        }
        return ((Unit) ((obj instanceof Unit) ? obj : null)).getConversionFactor() == this.getConversionFactor();
    }
}