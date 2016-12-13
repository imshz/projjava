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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import no.shz.utilities.StringUtility;

/**
 * The Info object defines the standard information
 * stored with spatial reference objects
 */
public abstract class Info implements IInfo {
    protected DecimalFormat _nfi = new DecimalFormat("0.################", new DecimalFormatSymbols(Locale.US));

    private String _name;
    private String _authority;
    private long _code;
    private String _alias;
    private String _abbreviation;
    private String _remarks;

    /**
     * A base interface for metadata applicable to coordinate system objects.
     * <p>
     * <p>The metadata items �Abbreviation�, �Alias�, �Authority�, �AuthorityCode�, �Name� and �Remarks�
     * were specified in the Simple Features interfaces, so they have been kept here.</p>
     * <p>This specification does not dictate what the contents of these items
     * should be. However, the following guidelines are suggested:</p>
     * <p>When <see cref="ICoordinateSystemAuthorityFactory"/> is used to create an object, the �Authority�
     * and 'AuthorityCode' values should be set to the authority name of the factory object, and the authority
     * code supplied by the client, respectively. The other values may or may not be set. (If the authority is
     * EPSG, the implementer may consider using the corresponding metadata values in the EPSG tables.)</p>
     * <p>When <see cref="CoordinateSystemFactory"/> creates an object, the 'Name' should be set to the value
     * supplied by the client. All of the other metadata items should be left empty</p>
     *
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param abbreviation Abbreviation
     * @param remarks      Provider-supplied remarks
     */
    public Info(String name, String authority, long code, String alias, String abbreviation, String remarks) {
        _name = name;
        _authority = authority;
        _code = code;
        _alias = alias;
        _abbreviation = abbreviation;
        _remarks = remarks;
    }

    /**
     * Gets or sets the name of the object.
     */
    public final String getName() {
        return _name;
    }

    public final void setName(String value) {
        _name = value;
    }

    /**
     * Gets or sets the authority name for this object, e.g., "EPSG",
     * is this is a standard object with an authority specific
     * getIdentity code. Returns "CUSTOM" if this is a custom object.
     */
    public final String getAuthority() {
        return _authority;
    }

    public final void setAuthority(String value) {
        _authority = value;
    }

    /**
     * Gets or sets the authority specific identification code of the object
     */
    public final long getAuthorityCode() {
        return _code;
    }

    public final void setAuthorityCode(long value) {
        _code = value;
    }

    /**
     * Gets or sets the alias of the object.
     */
    public final String getAlias() {
        return _alias;
    }

    public final void setAlias(String value) {
        _alias = value;
    }

    /**
     * Gets or sets the abbreviation of the object.
     */
    public final String getAbbreviation() {
        return _abbreviation;
    }

    public final void setAbbreviation(String value) {
        _abbreviation = value;
    }

    /**
     * Gets or sets the provider-supplied remarks for the object.
     */
    public final String getRemarks() {
        return _remarks;
    }

    public final void setRemarks(String value) {
        _remarks = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String toString() {
        return getWKT();
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public abstract String getWKT();

    /**
     * Gets an XML representation of this object.
     */
    public abstract String getXML();

    /**
     * Returns an XML string of the info object
     */
    public final String getInfoXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<CS_Info");
        if (getAuthorityCode() > 0) {
            sb.append(String.format(" AuthorityCode=\"%1$s\"", getAuthorityCode()));
        }
        if (!StringUtility.isNullOrEmpty(getAbbreviation())) {
            sb.append(String.format(" Abbreviation=\"%1$s\"", getAbbreviation()));
        }
        if (!StringUtility.isNullOrEmpty(getAuthority())) {
            sb.append(String.format(" Authority=\"%1$s\"", getAuthority()));
        }
        if (!StringUtility.isNullOrEmpty(getName())) {
            sb.append(String.format(" Name=\"%1$s\"", getName()));
        }
        sb.append("/>");
        return sb.toString();
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    public abstract boolean equalParams(Object obj);
}