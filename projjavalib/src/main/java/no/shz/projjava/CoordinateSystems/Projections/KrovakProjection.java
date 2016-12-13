package no.shz.projjava.coordinateSystems.projections;

import java.util.ArrayList;

import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.transformations.IMathTransform;

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
 * Implemetns the Krovak Projection.
 * <p>
 * <p>The normal case of the Lambert Conformal conic is for the axis of the cone
 * to be coincident with the minor axis of the ellipsoid, that is the axis of the cone
 * is normal to the ellipsoid at a pole. For the Oblique Conformal Conic the axis
 * of the cone is normal to the ellipsoid at a defined location and its extension
 * cuts the minor axis at a defined angle. This projection is used in the Czech Republic
 * and Slovakia under the name "Krovak" projection.</p>
 */
public class KrovakProjection extends MapProjection {

    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;
    /**
     * When to stop the iteration.
     */
    private static final double ITERATION_TOLERANCE = 1E-11;
    /**
     * Useful constant - 45� in radians.
     */
    private static final double S45 = 0.785398163397448;
    /**
     * Azimuth of the centre line passing through the centre of the projection.
     * This is equals to the co-latitude of the cone axis at point of intersection
     * with the ellipsoid.
     */
    protected double _azimuth;
    /**
     * Latitude of pseudo standard parallel.
     */
    protected double _pseudoStandardParallel;
    protected double _centralMeridian;
    protected double _latitudeOfOrigin;
    protected double _scaleFactor;
    protected double _excentricitySquared;
    protected double _excentricity;
    /**
     * Useful variables calculated from parameters defined by user.
     */
    private double _sinAzim, _cosAzim, _n, _tanS2, _alfa, _hae, _k1, _ka, _ro0, _rop;

    /**
     * Creates an instance of an LambertConformalConic2SPProjection projection object.
     * <p>
     * <p>
     * <p>The parameters this projection expects are listed below.</p>
     * <list type="table">
     * <listheader><term>Items</term><description>Descriptions</description></listheader>
     * <item><term>latitude_of_false_origin</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     * <item><term>longitude_of_false_origin</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     * <item><term>latitude_of_1st_standard_parallel</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
     * <item><term>latitude_of_2nd_standard_parallel</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
     * <item><term>easting_at_false_origin</term><description>The easting value assigned to the false origin.</description></item>
     * <item><term>northing_at_false_origin</term><description>The northing value assigned to the false origin.</description></item>
     * </list>
     *
     * @param parameters List of parameters to initialize the projection.
     */
    public KrovakProjection(ArrayList<ProjectionParameter> parameters) {
        this(parameters, false);
    }

    /**
     * Creates an instance of an Albers projection object.
     * <p>
     * <p>
     * <p>The parameters this projection expects are listed below.</p>
     * <list type="table">
     * <listheader><term>Parameter</term><description>Description</description></listheader>
     * <item><term>latitude_of_origin</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     * <item><term>central_meridian</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     * <item><term>standard_parallel_1</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
     * <item><term>standard_parallel_2</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
     * <item><term>false_easting</term><description>The easting value assigned to the false origin.</description></item>
     * <item><term>false_northing</term><description>The northing value assigned to the false origin.</description></item>
     * </list>
     *
     * @param parameters List of parameters to initialize the projection.
     * @param isInverse  Indicates whether the projection forward (meters to degrees or degrees to meters).
     */
    public KrovakProjection(ArrayList<ProjectionParameter> parameters, boolean isInverse) {
        super(parameters, isInverse);
        this.setName("Krovak");
        this.setAuthority("EPSG");
        this.setAuthorityCode(9819);

        ProjectionParameter par_latitude_of_center = getParameter("latitude_of_center");
        ProjectionParameter par_longitude_of_center = getParameter("longitude_of_center");
        ProjectionParameter par_azimuth = getParameter("azimuth");
        ProjectionParameter par_pseudo_standard_parallel_1 = getParameter("pseudo_standard_parallel_1");
        ProjectionParameter par_scale_factor = getParameter("scale_factor");
        ProjectionParameter par_false_easting = getParameter("false_easting");
        ProjectionParameter par_false_northing = getParameter("false_northing");

        if (par_latitude_of_center == null) {
            throw new IllegalArgumentException("Missing projection parameter 'latitude_of_center'");
        }
        if (par_longitude_of_center == null) {
            throw new IllegalArgumentException("Missing projection parameter 'longitude_of_center'");
        }
        if (par_azimuth == null) {
            throw new IllegalArgumentException("Missing projection parameter 'azimuth'");
        }
        if (par_pseudo_standard_parallel_1 == null) {
            throw new IllegalArgumentException("Missing projection parameter 'pseudo_standard_parallel_1'");
        }
        if (par_false_easting == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_easting'");
        }
        if (par_false_northing == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_northing'");
        }

        _latitudeOfOrigin = degrees2Radians(par_latitude_of_center.getValue());
        _centralMeridian = degrees2Radians(24 + (50.0 / 60));
        _azimuth = degrees2Radians(par_azimuth.getValue());
        _pseudoStandardParallel = degrees2Radians(par_pseudo_standard_parallel_1.getValue());
        _scaleFactor = par_scale_factor.getValue();

        double _falseEasting = par_false_easting.getValue() * _metersPerUnit;
        double _falseNorthing = par_false_northing.getValue() * _metersPerUnit;

        _excentricitySquared = 1.0 - (super._semiMinor * super._semiMinor) / (super._semiMajor * super._semiMajor);
        _excentricity = Math.sqrt(_excentricitySquared);

        _sinAzim = Math.sin(_azimuth);
        _cosAzim = Math.cos(_azimuth);
        _n = Math.sin(_pseudoStandardParallel);
        _tanS2 = Math.tan(_pseudoStandardParallel / 2 + S45);

        double sinLat = Math.sin(_latitudeOfOrigin);
        double cosLat = Math.cos(_latitudeOfOrigin);
        double cosL2 = cosLat * cosLat;
        _alfa = Math.sqrt(1 + ((_excentricitySquared * (cosL2 * cosL2)) / (1 - _excentricitySquared))); // parameter B
        _hae = _alfa * _excentricity / 2;
        double u0 = Math.asin(sinLat / _alfa);

        double esl = _excentricity * sinLat;
        double g = Math.pow((1 - esl) / (1 + esl), (_alfa * _excentricity) / 2);
        _k1 = Math.pow(Math.tan(_latitudeOfOrigin / 2 + S45), _alfa) * g / Math.tan(u0 / 2 + S45);
        _ka = Math.pow(1 / _k1, -1 / _alfa);

        double radius = Math.sqrt(1 - _excentricitySquared) / (1 - (_excentricitySquared * (sinLat * sinLat)));

        _ro0 = _scaleFactor * radius / Math.tan(_pseudoStandardParallel);
        _rop = _ro0 * Math.pow(_tanS2, _n);
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    @Override
    public double[] degreesToMeters(double[] lonlat) {
        double lambda = degrees2Radians(lonlat[0]) - _centralMeridian;
        double phi = degrees2Radians(lonlat[1]);

        double esp = _excentricity * Math.sin(phi);
        double gfi = Math.pow(((1.0 - esp) / (1.0 + esp)), _hae);
        double u = 2 * (Math.atan(Math.pow(Math.tan(phi / 2 + S45), _alfa) / _k1 * gfi) - S45);
        double deltav = -lambda * _alfa;
        double cosU = Math.cos(u);
        double s = Math.asin((_cosAzim * Math.sin(u)) + (_sinAzim * cosU * Math.cos(deltav)));
        double d = Math.asin(cosU * Math.sin(deltav) / Math.cos(s));
        double eps = _n * d;
        double ro = _rop / Math.pow(Math.tan(s / 2 + S45), _n);

        double y = -(ro * Math.cos(eps)) * this._semiMajor;
        double x = -(ro * Math.sin(eps)) * this._semiMajor;

        return new double[]{x, y};
    }

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param p Point in meters
     * @return Transformed point in decimal degrees
     */
    @Override
    public double[] metersToDegrees(double[] p) {
        double x = p[0] / this._semiMajor;
        double y = p[1] / this._semiMajor;

        double ro = Math.sqrt(x * x + y * y);
        double eps = Math.atan2(-x, -y);
        double d = eps / _n;
        double s = 2 * (Math.atan(Math.pow(_ro0 / ro, 1 / _n) * _tanS2) - S45);
        double cs = Math.cos(s);
        double u = Math.asin((_cosAzim * Math.sin(s)) - (_sinAzim * cs * Math.cos(d)));
        double kau = _ka * Math.pow(Math.tan((u / 2.0) + S45), 1 / _alfa);
        double deltav = Math.asin((cs * Math.sin(d)) / Math.cos(u));
        double lambda = -deltav / _alfa;
        double phi = 0;
        double fi1 = u;

        for (int i = MAXIMUM_ITERATIONS; ; ) {
            fi1 = phi;
            double esf = _excentricity * Math.sin(fi1);
            phi = 2.0 * (Math.atan(kau * Math.pow((1.0 + esf) / (1.0 - esf), _excentricity / 2.0)) - S45);
            if (Math.abs(fi1 - phi) <= ITERATION_TOLERANCE) {
                break;
            }

            if (--i < 0) {
                break;
            }
        }

        return new double[]{radians2Degrees(lambda + _centralMeridian), radians2Degrees(phi)};
    }

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    @Override
    public IMathTransform inverse() {
        if (_inverse == null) {
            _inverse = new KrovakProjection(this._parameters, !_isInverse);
        }

        return _inverse;
    }
}