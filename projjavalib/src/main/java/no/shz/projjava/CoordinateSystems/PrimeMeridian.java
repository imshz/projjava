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
 * A meridian used to take longitude measurements from.
 */
public class PrimeMeridian extends Info implements IPrimeMeridian {
    private double _longitude;
    private IAngularUnit _angularUnit;

    /**
     * Initializes a new instance of a prime meridian
     *
     * @param longitude     Longitude of prime meridian
     * @param angularUnit   Angular unit
     * @param name          Name
     * @param authority     Authority name
     * @param authorityCode Authority-specific identification code.
     * @param alias         Alias
     * @param abbreviation  Abbreviation
     * @param remarks       Provider-supplied remarks
     */
    public PrimeMeridian(double longitude, IAngularUnit angularUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _longitude = longitude;
        _angularUnit = angularUnit;
    }

    /**
     * Greenwich prime meridian
     */
    public static PrimeMeridian getGreenwich() {
        return new PrimeMeridian(0.0, AngularUnit.getDegrees(), "Greenwich", "EPSG", 8901, "", "", "");
    }

    /**
     * Lisbon prime meridian
     */
    public static PrimeMeridian getLisbon() {
        return new PrimeMeridian(-9.0754862, AngularUnit.getDegrees(), "Lisbon", "EPSG", 8902, "", "", "");
    }

    /**
     * Paris prime meridian.
     * Value adopted by IGN (Paris) in 1936. Equivalent to 2 deg 20min 14.025sec. Preferred by EPSG to earlier value of 2deg 20min 13.95sec (2.596898 grads) used by RGS London.
     */
    public static PrimeMeridian getParis() {
        return new PrimeMeridian(2.5969213, AngularUnit.getDegrees(), "Paris", "EPSG", 8903, "", "", "Value adopted by IGN (Paris) in 1936. Equivalent to 2 deg 20min 14.025sec. Preferred by EPSG to earlier value of 2deg 20min 13.95sec (2.596898 grads) used by RGS London.");
    }

    /**
     * Bogota prime meridian
     */
    public static PrimeMeridian getBogota() {
        return new PrimeMeridian(-74.04513, AngularUnit.getDegrees(), "Bogota", "EPSG", 8904, "", "", "");
    }

    /**
     * Madrid prime meridian
     */
    public static PrimeMeridian getMadrid() {
        return new PrimeMeridian(-3.411658, AngularUnit.getDegrees(), "Madrid", "EPSG", 8905, "", "", "");
    }

    /**
     * Rome prime meridian
     */
    public static PrimeMeridian getRome() {
        return new PrimeMeridian(12.27084, AngularUnit.getDegrees(), "Rome", "EPSG", 8906, "", "", "");
    }

    /**
     * Bern prime meridian.
     * 1895 value. Newer value of 7 deg 26 min 22.335 sec E determined in 1938.
     */
    public static PrimeMeridian getBern() {
        return new PrimeMeridian(7.26225, AngularUnit.getDegrees(), "Bern", "EPSG", 8907, "", "", "1895 value. Newer value of 7 deg 26 min 22.335 sec E determined in 1938.");
    }

    /**
     * Jakarta prime meridian
     */
    public static PrimeMeridian getJakarta() {
        return new PrimeMeridian(106.482779, AngularUnit.getDegrees(), "Jakarta", "EPSG", 8908, "", "", "");
    }

    /**
     * Ferro prime meridian.
     * Used in Austria and former Czechoslovakia.
     */
    public static PrimeMeridian getFerro() {
        return new PrimeMeridian(-17.4, AngularUnit.getDegrees(), "Ferro", "EPSG", 8909, "", "", "Used in Austria and former Czechoslovakia.");
    }

    /**
     * Brussels prime meridian
     */
    public static PrimeMeridian getBrussels() {
        return new PrimeMeridian(4.220471, AngularUnit.getDegrees(), "Brussels", "EPSG", 8910, "", "", "");
    }

    /**
     * Stockholm prime meridian
     */
    public static PrimeMeridian getStockholm() {
        return new PrimeMeridian(18.03298, AngularUnit.getDegrees(), "Stockholm", "EPSG", 8911, "", "", "");
    }

    /**
     * Athens prime meridian.
     * Used in Greece for older mapping based on Hatt projection.
     */
    public static PrimeMeridian getAthens() {
        return new PrimeMeridian(23.4258815, AngularUnit.getDegrees(), "Athens", "EPSG", 8912, "", "", "Used in Greece for older mapping based on Hatt projection.");
    }

    /**
     * Oslo prime meridian.
     * Formerly known as Kristiania or Christiania.
     */
    public static PrimeMeridian getOslo() {
        return new PrimeMeridian(10.43225, AngularUnit.getDegrees(), "Oslo", "EPSG", 8913, "", "", "Formerly known as Kristiania or Christiania.");
    }

    /**
     * Gets or sets the longitude of the prime meridian (relative to the Greenwich prime meridian).
     */
    public final double getLongitude() {
        return _longitude;
    }

    public final void setLongitude(double value) {
        _longitude = value;
    }

    /**
     * Gets or sets the AngularUnits.
     */
    public final IAngularUnit getAngularUnit() {
        return _angularUnit;
    }

    public final void setAngularUnit(IAngularUnit value) {
        _angularUnit = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "PRIMEM[\"%1$s\", %2$s", getName(), _nfi.format(getLongitude())));
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
        return String.format(Locale.US, "<CS_PrimeMeridian Longitude=\"%1$s\" >%2$s%3$s</CS_PrimeMeridian>", _nfi.format(getLongitude()), getInfoXml(), getAngularUnit().getXML());
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
        if (!(obj instanceof PrimeMeridian)) {
            return false;
        }
        PrimeMeridian prime = (PrimeMeridian) ((obj instanceof PrimeMeridian) ? obj : null);
        return prime.getAngularUnit().equalParams(this.getAngularUnit()) && prime.getLongitude() == this.getLongitude();
    }
}