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
 * Implements the Mercator projection.
 * <p>
 * <p>This map projection introduced in 1569 by Gerardus Mercator. It is often described as a cylindrical projection,
 * but it must be derived mathematically. The meridians are equally spaced, parallel vertical lines, and the
 * parallels of latitude are parallel, horizontal straight lines, spaced farther and farther apart as their distance
 * from the Equator increases. This projection is widely used for navigation charts, because any straight line
 * on a Mercator-projection map is a line of constant true bearing that enables a navigator to plot a straight-line
 * course. It is less practical for world maps because the scale is distorted; areas farther away from the equator
 * appear disproportionately large. On a Mercator projection, for example, the landmass of Greenland appears to be
 * greater than that of the continent of South America; in actual area, Greenland is smaller than the Arabian Peninsula.
 * </p>
 */
public class Mercator extends MapProjection {
    private double _falseEasting;
    private double _falseNorthing;
    private double _lonCenter;
    private double _e, _e2;
    private double _k0;

    /**
     * Initializes the MercatorProjection object with the specified parameters to project points.
     *
     * @param parameters ParameterList with the required parameters.
     */
    public Mercator(ArrayList<ProjectionParameter> parameters) {
        this(parameters, false);
    }

    /**
     * Initializes the MercatorProjection object with the specified parameters.
     *
     * @param parameters List of parameters to initialize the projection.
     * @param isInverse  Indicates whether the projection forward (meters to degrees or degrees to meters).
     *                   <p>
     *                   <p>The parameters this projection expects are listed below.</p>
     *                   <list type="table">
     *                   <listheader><term>Items</term><description>Descriptions</description></listheader>
     *                   <item><term>central_meridian</term><description>The longitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the longitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).</description></item>
     *                   <item><term>latitude_of_origin</term><description>The latitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the latitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).</description></item>
     *                   <item><term>scale_factor</term><description>The factor by which the map grid is reduced or enlarged during the projection process, defined by its value at the natural origin.</description></item>
     *                   <item><term>false_easting</term><description>Since the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Easting, FE, is the easting value assigned to the abscissa (east).</description></item>
     *                   <item><term>false_northing</term><description>Since the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Northing, FN, is the northing value assigned to the ordinate.</description></item>
     *                   </list>
     */
    public Mercator(ArrayList<ProjectionParameter> parameters, boolean isInverse) {
        super(parameters, isInverse);
        this.setAuthority("EPSG");

        ProjectionParameter central_meridian = getParameter("central_meridian");
        ProjectionParameter latitude_of_origin = getParameter("latitude_of_origin");
        ProjectionParameter scale_factor = getParameter("scale_factor");
        ProjectionParameter false_easting = getParameter("false_easting");
        ProjectionParameter false_northing = getParameter("false_northing");

        if (central_meridian == null) {
            throw new IllegalArgumentException("Missing projection parameter 'central_meridian'");
        }
        if (latitude_of_origin == null) {
            throw new IllegalArgumentException("Missing projection parameter 'latitude_of_origin'");
        }
        if (false_easting == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_easting'");
        }
        if (false_northing == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_northing'");
        }

        _lonCenter = degrees2Radians(central_meridian.getValue());
        double lat_origin = degrees2Radians(latitude_of_origin.getValue());
        _falseEasting = false_easting.getValue() * _metersPerUnit;
        _falseNorthing = false_northing.getValue() * _metersPerUnit;

        double temp = this._semiMinor / this._semiMajor;
        _e2 = 1 - temp * temp;
        _e = Math.sqrt(_e2);
        if (scale_factor == null) {
            _k0 = Math.cos(lat_origin) / Math.sqrt(1.0 - _e2 * Math.sin(lat_origin) * Math.sin(lat_origin));
            this.setAuthorityCode(9805);
            this.setName("Mercator_2SP");
        } else {
            _k0 = scale_factor.getValue();
            this.setName("Mercator_1SP");
        }
        this.setAuthority("EPSG");
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     * <p>
     * <p>
     * <p>The parameters this projection expects are listed below.</p>
     * <list type="table">
     * <listheader><term>Items</term><description>Descriptions</description></listheader>
     * <item><term>longitude_of_natural_origin</term><description>The longitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the longitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).  Sometimes known as ""central meridian""."</description></item>
     * <item><term>latitude_of_natural_origin</term><description>The latitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the latitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).</description></item>
     * <item><term>scale_factor_at_natural_origin</term><description>The factor by which the map grid is reduced or enlarged during the projection process, defined by its value at the natural origin.</description></item>
     * <item><term>false_easting</term><description>Since the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Easting, FE, is the easting value assigned to the abscissa (east).</description></item>
     * <item><term>false_northing</term><description>Since the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Northing, FN, is the northing value assigned to the ordinate .</description></item>
     * </list>
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    @Override
    public double[] degreesToMeters(double[] lonlat) {
        if (Double.isNaN(lonlat[0]) || Double.isNaN(lonlat[1])) {
            return new double[]{Double.NaN, Double.NaN};
        }
        double dLongitude = degrees2Radians(lonlat[0]);
        double dLatitude = degrees2Radians(lonlat[1]);

        if (Math.abs(Math.abs(dLatitude) - HALF_PI) <= EPSLN) {
            throw new IllegalArgumentException("Transformation cannot be computed at the poles.");
        } else {
            double esinphi = _e * Math.sin(dLatitude);
            double x = _falseEasting + this._semiMajor * _k0 * (dLongitude - _lonCenter);
            double y = _falseNorthing + this._semiMajor * _k0 * Math.log(Math.tan(PI * 0.25 + dLatitude * 0.5) * Math.pow((1 - esinphi) / (1 + esinphi), _e * 0.5));
            if (lonlat.length < 3) {
                return new double[]{x / _metersPerUnit, y / _metersPerUnit};
            } else {
                return new double[]{x / _metersPerUnit, y / _metersPerUnit, lonlat[2]};
            }
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
        double dLongitude;
        double dLatitude;

        double dX = p[0] * _metersPerUnit - this._falseEasting;
        double dY = p[1] * _metersPerUnit - this._falseNorthing;
        double ts = Math.exp(-dY / (this._semiMajor * _k0)); //t

        double chi = HALF_PI - 2 * Math.atan(ts);
        double e4 = Math.pow(_e, 4);
        double e6 = Math.pow(_e, 6);
        double e8 = Math.pow(_e, 8);

        dLatitude = chi + (_e2 * 0.5 + 5 * e4 / 24 + e6 / 12 + 13 * e8 / 360) * Math.sin(2 * chi) + (7 * e4 / 48 + 29 * e6 / 240 + 811 * e8 / 11520) * Math.sin(4 * chi) + +(7 * e6 / 120 + 81 * e8 / 1120) * Math.sin(6 * chi) + +(4279 * e8 / 161280) * Math.sin(8 * chi);

        dLongitude = dX / (this._semiMajor * _k0) + _lonCenter;

        if (p.length < 3) {
            return new double[]{radians2Degrees(dLongitude), radians2Degrees(dLatitude)};
        } else {
            return new double[]{radians2Degrees(dLongitude), radians2Degrees(dLatitude), p[2]};
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
            _inverse = new Mercator(this._parameters, !_isInverse);
        }
        return _inverse;
    }
}