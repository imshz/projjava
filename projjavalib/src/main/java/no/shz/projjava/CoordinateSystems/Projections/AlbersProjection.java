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
 *  Copyright (_c) 2002 Urban Science Applications, Inc.
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
 * Implements the Albers projection.
 * <p>
 * <p>Implements the Albers projection. The Albers projection is most commonly
 * used to project the United States of America. It gives the northern
 * border with Canada a curved appearance.</p>
 * <p>
 * <p>The <a href="http://www.geog.mcgill.ca/courses/geo201/mapproj/naaeana.gif">Albers Equal Area</a>
 * projection has the property that the area bounded
 * by any pair of parallels and meridians is exactly reproduced between the
 * image of those parallels and meridians in the projected domain, that is,
 * the projection preserves the correct area of the earth though distorts
 * direction, distance and shape somewhat.</p>
 */
public class AlbersProjection extends MapProjection {
    private double _falseEasting;
    private double _falseNorthing;
    private double _c; //constant c
    private double _e; //eccentricity
    private double _eSq = 0;
    private double _ro0;
    private double _n;
    private double _lonCenter; //center longitude

    /**
     * Creates an instance of an Albers projection object.
     *
     * @param parameters List of parameters to initialize the projection.
     *                   <p>
     *                   <p>The parameters this projection expects are listed below.</p>
     *                   <list type="table">
     *                   <listheader><term>Items</term><description>Descriptions</description></listheader>
     *                   <item><term>latitude_of_false_origin</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     *                   <item><term>longitude_of_false_origin</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     *                   <item><term>latitude_of_1st_standard_parallel</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
     *                   <item><term>latitude_of_2nd_standard_parallel</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
     *                   <item><term>easting_at_false_origin</term><description>The easting value assigned to the false origin.</description></item>
     *                   <item><term>northing_at_false_origin</term><description>The northing value assigned to the false origin.</description></item>
     *                   </list>
     */
    public AlbersProjection(ArrayList<ProjectionParameter> parameters) {
        this(parameters, false);
    }

    /**
     * Creates an instance of an Albers projection object.
     * <p>
     * <p>
     * <p>The parameters this projection expects are listed below.</p>
     * <list type="table">
     * <listheader><term>Items</term><description>Descriptions</description></listheader>
     * <item><term>latitude_of_center</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     * <item><term>longitude_of_center</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
     * <item><term>standard_parallel_1</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
     * <item><term>standard_parallel_2</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
     * <item><term>false_easting</term><description>The easting value assigned to the false origin.</description></item>
     * <item><term>false_northing</term><description>The northing value assigned to the false origin.</description></item>
     * </list>
     *
     * @param parameters List of parameters to initialize the projection.
     * @param isInverse  Indicates whether the projection forward (meters to degrees or degrees to meters).
     */
    public AlbersProjection(ArrayList<ProjectionParameter> parameters, boolean isInverse) {
        super(parameters, isInverse);
        this.setName("Albers_Conic_Equal_Area");

        ProjectionParameter longitude_of_center = getParameter("longitude_of_center");
        ProjectionParameter latitude_of_center = getParameter("latitude_of_center");
        ProjectionParameter standard_parallel_1 = getParameter("standard_parallel_1");
        ProjectionParameter standard_parallel_2 = getParameter("standard_parallel_2");
        ProjectionParameter false_easting = getParameter("false_easting");
        ProjectionParameter false_northing = getParameter("false_northing");

        if (longitude_of_center == null) {
            longitude_of_center = getParameter("central_meridian"); //Allow for altenative name
            if (longitude_of_center == null) {
                throw new IllegalArgumentException("Missing projection parameter 'longitude_of_center'");
            }
        }
        if (latitude_of_center == null) {
            latitude_of_center = getParameter("latitude_of_origin"); //Allow for altenative name
            if (latitude_of_center == null) {
                throw new IllegalArgumentException("Missing projection parameter 'latitude_of_center'");
            }
        }
        if (standard_parallel_1 == null) {
            throw new IllegalArgumentException("Missing projection parameter 'standard_parallel_1'");
        }
        if (standard_parallel_2 == null) {
            throw new IllegalArgumentException("Missing projection parameter 'standard_parallel_2'");
        }
        if (false_easting == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_easting'");
        }
        if (false_northing == null) {
            throw new IllegalArgumentException("Missing projection parameter 'false_northing'");
        }

        _lonCenter = degrees2Radians(longitude_of_center.getValue());
        double lat0 = degrees2Radians(latitude_of_center.getValue());
        double lat1 = degrees2Radians(standard_parallel_1.getValue());
        double lat2 = degrees2Radians(standard_parallel_2.getValue());
        this._falseEasting = false_easting.getValue() * _metersPerUnit;
        this._falseNorthing = false_northing.getValue() * _metersPerUnit;

        if (Math.abs(lat1 + lat2) < Double.MIN_VALUE) {
            throw new IllegalArgumentException("Equal latitudes for standard parallels on opposite sides of Equator.");
        }

        _eSq = 1.0 - Math.pow(this._semiMinor / this._semiMajor, 2);
        _e = Math.sqrt(_eSq);

        double alpha1 = alpha(lat1);
        double alpha2 = alpha(lat2);

        double m1 = Math.cos(lat1) / Math.sqrt(1 - _eSq * Math.pow(Math.sin(lat1), 2));
        double m2 = Math.cos(lat2) / Math.sqrt(1 - _eSq * Math.pow(Math.sin(lat2), 2));

        _n = (Math.pow(m1, 2) - Math.pow(m2, 2)) / (alpha2 - alpha1);
        _c = Math.pow(m1, 2) + (_n * alpha1);

        _ro0 = ro(alpha(lat0));
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    @Override
    public double[] degreesToMeters(double[] lonlat) {
        double dLongitude = degrees2Radians(lonlat[0]);
        double dLatitude = degrees2Radians(lonlat[1]);

        double a = alpha(dLatitude);
        double ro = ro(a);
        double theta = _n * (dLongitude - _lonCenter);
        dLongitude = _falseEasting + ro * Math.sin(theta);
        dLatitude = _falseNorthing + _ro0 - (ro * Math.cos(theta));
        if (lonlat.length == 2) {
            return new double[]{dLongitude / _metersPerUnit, dLatitude / _metersPerUnit};
        } else {
            return new double[]{dLongitude / _metersPerUnit, dLatitude / _metersPerUnit, lonlat[2]};
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
        double theta = Math.atan((p[0] * _metersPerUnit - _falseEasting) / (_ro0 - (p[1] * _metersPerUnit - _falseNorthing)));
        double ro = Math.sqrt(Math.pow(p[0] * _metersPerUnit - _falseEasting, 2) + Math.pow(_ro0 - (p[1] * _metersPerUnit - _falseNorthing), 2));
        double q = (_c - Math.pow(ro, 2) * Math.pow(_n, 2) / Math.pow(this._semiMajor, 2)) / _n;
        double b = Math.sin(q / (1 - ((1 - _eSq) / (2 * _e)) * Math.log((1 - _e) / (1 + _e))));

        double lat = Math.asin(q * 0.5);
        double preLat = Double.MAX_VALUE;
        int iterationCounter = 0;
        while (Math.abs(lat - preLat) > 0.000001) {
            preLat = lat;
            double sin = Math.sin(lat);
            double e2sin2 = _eSq * Math.pow(sin, 2);
            lat += (Math.pow(1 - e2sin2, 2) / (2 * Math.cos(lat))) * ((q / (1 - _eSq)) - sin / (1 - e2sin2) + 1 / (2 * _e) * Math.log((1 - _e * sin) / (1 + _e * sin)));
            iterationCounter++;
            if (iterationCounter > 25) {
                throw new IllegalArgumentException("Transformation failed to converge in Albers backwards transformation");
            }
        }
        double lon = _lonCenter + (theta / _n);
        if (p.length == 2) {
            return new double[]{radians2Degrees(lon), radians2Degrees(lat)};
        } else {
            return new double[]{radians2Degrees(lon), radians2Degrees(lat), p[2]};
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
            _inverse = new AlbersProjection(this._parameters, !_isInverse);
        }
        return _inverse;
    }

    //private double ToAuthalic(double lat)
    //{
    //    return Math.Atan(Q(lat) / Q(Math.PI * 0.5));
    //}
    //private double Q(double angle)
    //{
    //    double sin = Math.Sin(angle);
    //    double esin = _e * sin;
    //    return Math.Abs(sin / (1 - Math.Pow(esin, 2)) - 0.5 * _e) * Math.Log((1 - esin) / (1 + esin)));
    //}
    private double alpha(double lat) {
        double sin = Math.sin(lat);
        double sinsq = Math.pow(sin, 2);
        return (1 - _eSq) * (((sin / (1 - _eSq * sinsq)) - 1 / (2 * _e) * Math.log((1 - _e * sin) / (1 + _e * sin))));
    }

    private double ro(double a) {
        return this._semiMajor * Math.sqrt((_c - _n * a)) / _n;
    }
}