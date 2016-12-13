package no.shz.projjava.coordinateSystems.projections;

import java.util.ArrayList;

import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.transformations.IMathTransform;
import no.shz.utilities.Ref;

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
 * SOURCECODE IS MODIFIED FROM ANOTHER WORK AND IS ORIGINALLY BASED ON GeoTools.NET:
 *
 *  Copyright (C) 2002 Urban Science Applications, Inc. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
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
 * Summary description for MathTransform.
 * <p>
 * <p>Universal (UTM) and Modified (MTM) Transverses Mercator projections. This
 * is a cylindrical projection, in which the cylinder has been rotated 90�.
 * Instead of being tangent to the equator (or to an other standard latitude),
 * it is tangent to a central meridian. Deformation are more important as we
 * are going futher from the central meridian. The Transverse Mercator
 * projection is appropriate for region wich have a greater extent north-south
 * than east-west.</p>
 * <p>
 * <p>Reference: John P. Snyder (Map projections - A Working Manual,
 * U.S. Geological Survey Professional Paper 1395, 1987)</p>
 */
public class TransverseMercator extends MapProjection {
    private double _scaleFactor;
    private double _centralMeridian;
    private double _e0, e1, e2, e3;
    private double _es1;
    private double _esp;
    private double _ml0;
    private double _falseNorthing;
    private double _falseEasting;

    /**
     * Creates an instance of an TransverseMercatorProjection projection object.
     *
     * @param parameters List of parameters to initialize the projection.
     */
    public TransverseMercator(ArrayList<ProjectionParameter> parameters) {
        this(parameters, false);

    }

    /**
     * Creates an instance of an TransverseMercatorProjection projection object.
     *
     * @param parameters List of parameters to initialize the projection.
     * @param inverse    Flag indicating wether is a forward/projection (false) or an inverse projection (true).
     *                   <p>
     *                   <list type="bullet">
     *                   <listheader><term>Items</term><description>Descriptions</description></listheader>
     *                   <item><term>semi_major</term><description>Semi major radius</description></item>
     *                   <item><term>semi_minor</term><description>Semi minor radius</description></item>
     *                   <item><term>_scaleFactor</term><description></description></item>
     *                   <item><term>central meridian</term><description></description></item>
     *                   <item><term>latitude_origin</term><description></description></item>
     *                   <item><term>_falseEasting</term><description></description></item>
     *                   <item><term>_falseNorthing</term><description></description></item>
     *                   </list>
     */
    public TransverseMercator(ArrayList<ProjectionParameter> parameters, boolean inverse) {
        super(parameters, inverse);
        this.setName("Transverse_Mercator");
        this.setAuthority("EPSG");
        this.setAuthorityCode(9807);

        ProjectionParameter par_scale_factor = getParameter("scale_factor");
        ProjectionParameter par_central_meridian = getParameter("central_meridian");
        ProjectionParameter par_latitude_of_origin = getParameter("latitude_of_origin");
        ProjectionParameter par_false_easting = getParameter("false_easting");
        ProjectionParameter par_false_northing = getParameter("false_northing");

        if (par_scale_factor == null) {
            throw new IllegalArgumentException("Missing projection parameter 'scale_factor'");
        }
        if (par_central_meridian == null) {
            throw new IllegalArgumentException("Missing projection parameter 'central_meridian'");
        }
        if (par_latitude_of_origin == null) {
            throw new IllegalArgumentException("Missing projection parameter 'latitude_of_origin'");
        }
        if (par_false_easting == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_easting'");
        }
        if (par_false_northing == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_northing'");
        }

        _scaleFactor = par_scale_factor.getValue();
        _centralMeridian = degrees2Radians(par_central_meridian.getValue());
        double lat_origin = degrees2Radians(par_latitude_of_origin.getValue());
        _falseEasting = par_false_easting.getValue() * _metersPerUnit;
        _falseNorthing = par_false_northing.getValue() * _metersPerUnit;

        _es1 = 1.0 - Math.pow(this._semiMinor / this._semiMajor, 2);
        double e = Math.sqrt(_es1);
        _e0 = e0fn(_es1);
        e1 = e1fn(_es1);
        e2 = e2fn(_es1);
        e3 = e3fn(_es1);
        _ml0 = this._semiMajor * mlfn(_e0, e1, e2, e3, lat_origin);
        _esp = _es1 / (1.0 - _es1);
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    @Override
    public double[] degreesToMeters(double[] lonlat) {
        double lon = degrees2Radians(lonlat[0]);
        double lat = degrees2Radians(lonlat[1]);

        double delta_lon;
        double sin_phi = 0, cos_phi = 0;
        double al, als;
        double c, t, tq;
        double con, n, ml;

        delta_lon = adjustLon(lon - _centralMeridian);
        Ref<Double> tempRef_sin_phi = new Ref<>(sin_phi);
        Ref<Double> tempRef_cos_phi = new Ref<>(cos_phi);
        sincos(lat, tempRef_sin_phi, tempRef_cos_phi);
        cos_phi = tempRef_cos_phi.value;
        sin_phi = tempRef_sin_phi.value;

        al = cos_phi * delta_lon;
        als = Math.pow(al, 2);
        c = _esp * Math.pow(cos_phi, 2);
        tq = Math.tan(lat);
        t = Math.pow(tq, 2);
        con = 1.0 - _es1 * Math.pow(sin_phi, 2);
        n = this._semiMajor / Math.sqrt(con);
        ml = this._semiMajor * mlfn(_e0, e1, e2, e3, lat);

        double x = _scaleFactor * n * al * (1.0 + als / 6.0 * (1.0 - t + c + als / 20.0 * (5.0 - 18.0 * t + Math.pow(t, 2) + 72.0 * c - 58.0 * _esp))) + _falseEasting;
        double y = _scaleFactor * (ml - _ml0 + n * tq * (als * (0.5 + als / 24.0 * (5.0 - t + 9.0 * c + 4.0 * Math.pow(c, 2) + als / 30.0 * (61.0 - 58.0 * t + Math.pow(t, 2) + 600.0 * c - 330.0 * _esp))))) + _falseNorthing;
        if (lonlat.length < 3) {
            return new double[]{x / _metersPerUnit, y / _metersPerUnit};
        } else {
            return new double[]{x / _metersPerUnit, y / _metersPerUnit, lonlat[2]};
        }
    }

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param p Point in meters
     * @return Transformed point in decimal degrees
     */
    @Override
    public double[] metersToDegrees(double[] p) {
        double con, phi; // temporary angles
        double delta_phi; // difference between longitudes
        long i; // counter variable
        double sin_phi = 0, cos_phi = 0, tan_phi; // sin cos and tangent values
        double c, cs, t, ts, n, r, d, ds; // temporary variables
        long max_iter = 6; // maximun number of iterations


        double x = p[0] * _metersPerUnit - _falseEasting;
        double y = p[1] * _metersPerUnit - _falseNorthing;

        con = (_ml0 + y / _scaleFactor) / this._semiMajor;
        phi = con;
        for (i = 0; ; i++) {
            delta_phi = ((con + e1 * Math.sin(2.0 * phi) - e2 * Math.sin(4.0 * phi) + e3 * Math.sin(6.0 * phi)) / _e0) - phi;
            phi += delta_phi;
            if (Math.abs(delta_phi) <= EPSLN) {
                break;
            }
            if (i >= max_iter) {
                throw new IllegalArgumentException("Latitude failed to converge");
            }
        }
        if (Math.abs(phi) < HALF_PI) {
            Ref<Double> tempRef_sin_phi = new Ref<>(sin_phi);
            Ref<Double> tempRef_cos_phi = new Ref<>(cos_phi);
            sincos(phi, tempRef_sin_phi, tempRef_cos_phi);
            cos_phi = tempRef_cos_phi.value;
            sin_phi = tempRef_sin_phi.value;
            tan_phi = Math.tan(phi);
            c = _esp * Math.pow(cos_phi, 2);
            cs = Math.pow(c, 2);
            t = Math.pow(tan_phi, 2);
            ts = Math.pow(t, 2);
            con = 1.0 - _es1 * Math.pow(sin_phi, 2);
            n = this._semiMajor / Math.sqrt(con);
            r = n * (1.0 - _es1) / con;
            d = x / (n * _scaleFactor);
            ds = Math.pow(d, 2);

            double lat = phi - (n * tan_phi * ds / r) * (0.5 - ds / 24.0 * (5.0 + 3.0 * t + 10.0 * c - 4.0 * cs - 9.0 * _esp - ds / 30.0 * (61.0 + 90.0 * t + 298.0 * c + 45.0 * ts - 252.0 * _esp - 3.0 * cs)));
            double lon = adjustLon(_centralMeridian + (d * (1.0 - ds / 6.0 * (1.0 + 2.0 * t + c - ds / 20.0 * (5.0 - 2.0 * c + 28.0 * t - 3.0 * cs + 8.0 * _esp + 24.0 * ts))) / cos_phi));

            if (p.length < 3) {
                return new double[]{radians2Degrees(lon), radians2Degrees(lat)};
            } else {
                return new double[]{radians2Degrees(lon), radians2Degrees(lat), p[2]};
            }
        } else {
            if (p.length < 3) {
                return new double[]{radians2Degrees(HALF_PI * sign(y)), radians2Degrees(_centralMeridian)};
            } else {
                return new double[]{radians2Degrees(HALF_PI * sign(y)), radians2Degrees(_centralMeridian), p[2]};
            }

        }
    }

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    @Override
    public IMathTransform inverse() {
        if (_inverse == null) {
            _inverse = new TransverseMercator(this._parameters, !_isInverse);
        }
        return _inverse;
    }
}