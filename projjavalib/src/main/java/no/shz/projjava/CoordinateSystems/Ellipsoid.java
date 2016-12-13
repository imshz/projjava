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
 * The IEllipsoid interface defines the standard information stored with ellipsoid objects.
 */
public class Ellipsoid extends Info implements IEllipsoid {
    private double _semiMajorAxis;
    private double _semiMinorAxis;
    private double _inverseFlattening;
    private ILinearUnit _axisUnit;
    private boolean _isIvfDefinitive;

    /**
     * Initializes a new instance of an Ellipsoid
     *
     * @param semiMajorAxis     Semi major axis
     * @param semiMinorAxis     Semi minor axis
     * @param inverseFlattening inverse flattening
     * @param isIvfDefinitive   inverse Flattening is definitive for this ellipsoid (Semi-minor axis will be overridden)
     * @param axisUnit          Axis unit
     * @param name              Name
     * @param authority         Authority name
     * @param code              Authority-specific identification code.
     * @param alias             Alias
     * @param abbreviation      Abbreviation
     * @param remarks           Provider-supplied remarks
     */
    public Ellipsoid(double semiMajorAxis, double semiMinorAxis, double inverseFlattening, boolean isIvfDefinitive, ILinearUnit axisUnit, String name, String authority, long code, String alias, String abbreviation, String remarks) {
        super(name, authority, code, alias, abbreviation, remarks);
        _semiMajorAxis = semiMajorAxis;
        _inverseFlattening = inverseFlattening;
        _axisUnit = axisUnit;
        _isIvfDefinitive = isIvfDefinitive;

        if (isIvfDefinitive && (inverseFlattening == 0 || Double.isInfinite(inverseFlattening))) {
            _semiMinorAxis = semiMajorAxis;
        } else if (isIvfDefinitive) {
            _semiMinorAxis = (1.0 - (1.0 / _inverseFlattening)) * semiMajorAxis;
        } else {
            _semiMinorAxis = semiMinorAxis;
        }
    }

    /**
     * WGS 84 ellipsoid
     * <p>
     * inverse flattening derived from four defining parameters
     * (semi-major axis;
     * C20 = -484.16685*10e-6;
     * earth's angular velocity w = 7292115e11 rad/sec;
     * gravitational constant GM = 3986005e8 m*m*m/s/s).
     */
    public static Ellipsoid getWGS84() {
        return new Ellipsoid(6378137, 0, 298.257223563, true, LinearUnit.getMetre(), "WGS 84", "EPSG", 7030, "WGS84", "", "inverse flattening derived from four defining parameters (semi-major axis; C20 = -484.16685*10e-6; earth's angular velocity w = 7292115e11 rad/sec; gravitational constant GM = 3986005e8 m*m*m/s/s).");
    }

    /**
     * WGS 72 Ellipsoid
     */
    public static Ellipsoid getWGS72() {
        return new Ellipsoid(6378135.0, 0, 298.26, true, LinearUnit.getMetre(), "WGS 72", "EPSG", 7043, "WGS 72", "", "");
    }

    /**
     * GRS 1980 / International 1979 ellipsoid
     * <p>
     * Adopted by IUGG 1979 Canberra.
     * inverse flattening is derived from
     * geocentric gravitational constant GM = 3986005e8 m*m*m/s/s;
     * dynamic form factor J2 = 108263e8 and Earth's angular velocity = 7292115e-11 rad/s.")
     */
    public static Ellipsoid getGRS80() {
        return new Ellipsoid(6378137, 0, 298.257222101, true, LinearUnit.getMetre(), "GRS 1980", "EPSG", 7019, "International 1979", "", "Adopted by IUGG 1979 Canberra.  inverse flattening is derived from geocentric gravitational constant GM = 3986005e8 m*m*m/s/s; dynamic form factor J2 = 108263e8 and Earth's angular velocity = 7292115e-11 rad/s.");
    }

    /**
     * International 1924 / Hayford 1909 ellipsoid
     * <p>
     * Described as a=6378388 m. and b=6356909m. from which 1/f derived to be 296.95926.
     * The figure was adopted as the International ellipsoid in 1924 but with 1/f taken as
     * 297 exactly from which b is derived as 6356911.946m.
     */
    public static Ellipsoid getInternational1924() {
        return new Ellipsoid(6378388, 0, 297, true, LinearUnit.getMetre(), "International 1924", "EPSG", 7022, "Hayford 1909", "", "Described as a=6378388 m. and b=6356909 m. from which 1/f derived to be 296.95926. The figure was adopted as the International ellipsoid in 1924 but with 1/f taken as 297 exactly from which b is derived as 6356911.946m.");
    }

    /**
     * Clarke 1880
     * <p>
     * Clarke gave a and b and also 1/f=293.465 (to 3 decimal places).  1/f derived from a and b = 293.4663077
     */
    public static Ellipsoid getClarke1880() {
        return new Ellipsoid(20926202, 0, 297, true, LinearUnit.getClarkesFoot(), "Clarke 1880", "EPSG", 7034, "Clarke 1880", "", "Clarke gave a and b and also 1/f=293.465 (to 3 decimal places).  1/f derived from a and b = 293.4663077�");
    }

    /**
     * Clarke 1866
     * <p>
     * Original definition a=20926062 and b=20855121 (British) feet. Uses Clarke's 1865 inch-metre ratio of 39.370432 to obtain metres. (Metric value then converted to US survey feet for use in the United States using 39.37 exactly giving a=20925832.16 ft US).
     */
    public static Ellipsoid getClarke1866() {
        return new Ellipsoid(6378206.4, 6356583.8, Double.POSITIVE_INFINITY, false, LinearUnit.getMetre(), "Clarke 1866", "EPSG", 7008, "Clarke 1866", "", "Original definition a=20926062 and b=20855121 (British) feet. Uses Clarke's 1865 inch-metre ratio of 39.370432 to obtain metres. (Metric value then converted to US survey feet for use in the United States using 39.37 exactly giving a=20925832.16 ft US).");
    }

    /**
     * Sphere
     * <p>
     * Authalic sphere derived from GRS 1980 ellipsoid (code 7019).  (An authalic sphere is
     * one with a surface area equal to the surface area of the ellipsoid). 1/f is infinite.
     */
    public static Ellipsoid getSphere() {
        return new Ellipsoid(6370997.0, 6370997.0, Double.POSITIVE_INFINITY, false, LinearUnit.getMetre(), "GRS 1980 Authalic Sphere", "EPSG", 7048, "Sphere", "", "Authalic sphere derived from GRS 1980 ellipsoid (code 7019).  (An authalic sphere is one with a surface area equal to the surface area of the ellipsoid). 1/f is infinite.");
    }

    /**
     * Gets or sets the value of the semi-major axis.
     */
    public final double getSemiMajorAxis() {
        return _semiMajorAxis;
    }

    public final void setSemiMajorAxis(double value) {
        _semiMajorAxis = value;
    }

    /**
     * Gets or sets the value of the semi-minor axis.
     */
    public final double getSemiMinorAxis() {
        return _semiMinorAxis;
    }

    public final void setSemiMinorAxis(double value) {
        _semiMinorAxis = value;
    }

    /**
     * Gets or sets the value of the inverse of the flattening constant of the ellipsoid.
     */
    public final double getInverseFlattening() {
        return _inverseFlattening;
    }

    public final void setInverseFlattening(double value) {
        _inverseFlattening = value;
    }

    /**
     * Gets or sets the value of the axis unit.
     */
    public final ILinearUnit getAxisUnit() {
        return _axisUnit;
    }

    public final void setAxisUnit(ILinearUnit value) {
        _axisUnit = value;
    }

    /**
     * Tells if the inverse Flattening is definitive for this ellipsoid. Some ellipsoids use
     * the IVF as the defining value, and calculate the polar radius whenever asked. Other
     * ellipsoids use the polar radius to calculate the IVF whenever asked. This
     * distinction can be important to avoid floating-point rounding errors.
     */
    public final boolean getIsIvfDefinitive() {
        return _isIvfDefinitive;
    }

    public final void setIsIvfDefinitive(boolean value) {
        _isIvfDefinitive = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "SPHEROID[\"%1$s\", %2$s, %3$s", getName(), _nfi.format(getSemiMajorAxis()), _nfi.format(getInverseFlattening())));
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
        return String.format(Locale.US, "<CS_Ellipsoid SemiMajorAxis=\"%1$s\" SemiMinorAxis=\"%2$s\" InverseFlattening=\"%3$s\" IvfDefinitive=\"%4$s\">%5$s%6$s</CS_Ellipsoid>", _nfi.format(getSemiMajorAxis()), _nfi.format(getSemiMinorAxis()), _nfi.format(getInverseFlattening()), (getIsIvfDefinitive() ? 1 : 0), getInfoXml(), getAxisUnit().getXML());
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
        if (!(obj instanceof Ellipsoid)) {
            return false;
        }
        Ellipsoid e = (Ellipsoid) ((obj instanceof Ellipsoid) ? obj : null);
        return (e.getInverseFlattening() == this.getInverseFlattening() && e.getIsIvfDefinitive() == this.getIsIvfDefinitive() && e.getSemiMajorAxis() == this.getSemiMajorAxis() && e.getSemiMinorAxis() == this.getSemiMinorAxis() && e.getAxisUnit().equalParams(this.getAxisUnit()));
    }
}