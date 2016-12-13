package no.shz.projjava.coordinateSystems.transformations;

import java.util.ArrayList;

import no.shz.projjava.coordinateSystems.ProjectionParameter;

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

/**
 * <p>Latitude, Longitude and ellipsoidal height in terms of a 3-dimensional geographic system
 * may by expressed in terms of a geocentric (earth centered) Cartesian coordinate reference system
 * X, Y, Z with the Z axis corresponding to the earth's rotation axis positive northwards, the X
 * axis through the intersection of the prime meridian and equator, and the Y axis through
 * the intersection of the equator with longitude 90 degrees east. The geographic and geocentric
 * systems are based on the same geodetic datum.</p>
 * <p>Geocentric coordinate reference systems are conventionally taken to be defined with the X
 * axis through the intersection of the Greenwich meridian and equator. This requires that the equivalent
 * geographic coordinate reference systems based on a non-Greenwich prime meridian should first be
 * transformed to their Greenwich equivalent. Geocentric coordinates X, Y and Z take their units from
 * the units of the ellipsoid axes (a and b). As it is conventional for X, Y and Z to be in metres,
 * if the ellipsoid axis dimensions are given in another linear unit they should first be converted
 * to metres.</p>
 */
public class GeocentricTransform extends MathTransform {
    private static final double COS_67P5 = 0.38268343236508977;
    private static final double AD_C = 1.0026000;

    protected boolean _isInverse = false;
    protected ArrayList<ProjectionParameter> _parameters;
    protected MathTransform _inverse;

    private double _es;
    private double _semiMajor;
    private double _semiMinor;
    private double _ses;

    /**
     * Initializes a geocentric projection object
     *
     * @param parameters List of parameters to initialize the projection.
     * @param isInverse  Indicates whether the projection forward (meters to degrees or degrees to meters).
     */
    public GeocentricTransform(ArrayList<ProjectionParameter> parameters, boolean isInverse) {
        this(parameters);
        _isInverse = isInverse;
    }

    /**
     * Initializes a geocentric projection object
     *
     * @param parameters List of parameters to initialize the projection.
     */
    public GeocentricTransform(ArrayList<ProjectionParameter> parameters) {
        _parameters = parameters;

        for (ProjectionParameter pp : _parameters) {
            if (pp.getName().equalsIgnoreCase("semi_major")) {
                _semiMajor = pp.getValue();
            } else if (pp.getName().equalsIgnoreCase("semi_minor")) {
                _semiMinor = pp.getValue();
            }
        }

        _es = 1.0 - (_semiMinor * _semiMinor) / (_semiMajor * _semiMajor); //e^2
        _ses = (Math.pow(_semiMajor, 2) - Math.pow(_semiMinor, 2)) / Math.pow(_semiMinor, 2);
        double ba = _semiMinor / _semiMajor;
        double ab = _semiMajor / _semiMinor;
    }


    /**
     * Returns the inverse of this conversion.
     *
     * @return IMathTransform that is the reverse of the current conversion.
     */
    @Override
    public IMathTransform inverse() {
        if (_inverse == null) {
            _inverse = new GeocentricTransform(this._parameters, !_isInverse);
        }
        return _inverse;
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    private double[] degreesToMeters(double[] lonlat) {
        double lon = degrees2Radians(lonlat[0]);
        double lat = degrees2Radians(lonlat[1]);
        double h = lonlat.length < 3 ? 0 : (new Double(lonlat[2])).equals(Double.NaN) ? 0 : lonlat[2];
        double v = _semiMajor / Math.sqrt(1 - _es * Math.pow(Math.sin(lat), 2));
        double x = (v + h) * Math.cos(lat) * Math.cos(lon);
        double y = (v + h) * Math.cos(lat) * Math.sin(lon);
        double z = ((1 - _es) * v + h) * Math.sin(lat);
        return new double[]{x, y, z};
    }

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param pnt Point in meters
     * @return Transformed point in decimal degrees
     */
    private double[] metersToDegrees(double[] pnt) {
        boolean At_Pole = false;
        double Z = pnt.length < 3 ? 0 : (new Double(pnt[2])).equals(Double.NaN) ? 0 : pnt[2];

        double lon = 0;
        double lat = 0;
        double Height = 0;
        if (pnt[0] != 0.0) {
            lon = Math.atan2(pnt[1], pnt[0]);
        } else {
            if (pnt[1] > 0) {
                lon = Math.PI / 2;
            } else if (pnt[1] < 0) {
                lon = -Math.PI * 0.5;
            } else {
                At_Pole = true;
                lon = 0.0;
                if (Z > 0.0) {
                    lat = Math.PI * 0.5;
                } else if (Z < 0.0) {
                    lat = -Math.PI * 0.5;
                } else {
                    return new double[]{radians2Degrees(lon), radians2Degrees(Math.PI * 0.5), -_semiMinor};
                }
            }
        }

        double W2 = pnt[0] * pnt[0] + pnt[1] * pnt[1]; // Square of distance from Z axis
        double W = Math.sqrt(W2); // distance from Z axis
        double T0 = Z * AD_C; // initial estimate of vertical component
        double S0 = Math.sqrt(T0 * T0 + W2); //initial estimate of horizontal component
        double Sin_B0 = T0 / S0; //sin(B0), B0 is estimate of Bowring aux variable
        double Cos_B0 = W / S0; //cos(B0)
        double Sin3_B0 = Math.pow(Sin_B0, 3);
        double T1 = Z + _semiMinor * _ses * Sin3_B0; //corrected estimate of vertical component
        double Sum = W - _semiMajor * _es * Cos_B0 * Cos_B0 * Cos_B0; //numerator of cos(phi1)
        double S1 = Math.sqrt(T1 * T1 + Sum * Sum); //corrected estimate of horizontal component
        double Sin_p1 = T1 / S1; //sin(phi1), phi1 is estimated latitude
        double Cos_p1 = Sum / S1; //cos(phi1)
        double Rn = _semiMajor / Math.sqrt(1.0 - _es * Sin_p1 * Sin_p1); //Earth radius at location
        if (Cos_p1 >= COS_67P5) {
            Height = W / Cos_p1 - Rn;
        } else if (Cos_p1 <= -COS_67P5) {
            Height = W / -Cos_p1 - Rn;
        } else {
            Height = Z / Sin_p1 + Rn * (_es - 1.0);
        }
        if (!At_Pole) {
            lat = Math.atan(Sin_p1 / Cos_p1);
        }
        return new double[]{radians2Degrees(lon), radians2Degrees(lat), Height};
    }

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point
     * @return
     */
    @Override
    public double[] transform(double[] point) {
        if (!_isInverse) {
            return this.degreesToMeters(point);
        } else {
            return this.metersToDegrees(point);
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     *
     * @param points
     * @return This method is provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values. For example, if the source
     * dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
     * The size of the passed array must be an integer multiple of DimSource. The returned
     * ordinal values are packed in a similar way. In some DCPs. the ordinals may be
     * transformed in-place, and the returned array may be the same as the passed array.
     * So any client code should not attempt to reuse the passed ordinal values (although
     * they can certainly reuse the passed array). If there is any problem then the server
     * implementation will throw an exception. If this happens then the client should not
     * make any assumptions about the state of the ordinal values.
     */
    @Override
    public ArrayList<double[]> transformList(ArrayList<double[]> points) {
        ArrayList<double[]> result = new ArrayList<>(points.size());
        for (int i = 0; i < points.size(); i++) {
            double[] point = points.get(i);
            result.add(transform(point));
        }
        return result;
    }

    /**
     * Reverses the transformation
     */
    @Override
    public void invert() {
        _isInverse = !_isInverse;
    }

    /**
     * Gets a Well-Known text representation of this object.
     * <p>
     * <value></value>
     */
    @Override
    public String getWKT() {
        throw new UnsupportedOperationException("The method or operation is not implemented.");
    }

    /**
     * Gets an XML representation of this object.
     * <p>
     * <value></value>
     */
    @Override
    public String getXML() {
        throw new UnsupportedOperationException("The method or operation is not implemented.");
    }
}