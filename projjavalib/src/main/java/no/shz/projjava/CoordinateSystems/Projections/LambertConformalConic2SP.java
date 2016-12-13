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
 * Implemetns the Lambert Conformal Conic 2SP Projection.
 * <p>
 * <p>The Lambert Conformal Conic projection is a standard projection for presenting maps
 * of land areas whose East-West extent is large compared with their North-South extent.
 * This projection is "conformal" in the sense that lines of latitude and longitude,
 * which are perpendicular to one another on the earth's surface, are also perpendicular
 * to one another in the projected domain.</p>
 */
public class LambertConformalConic2SP extends MapProjection {

    private double _falseEasting;
    private double _falseNorthing;

    private double e = 0; // eccentricity
    private double center_lon = 0; // center longituted
    private double ns = 0; // ratio of angle between meridian
    private double f0 = 0; // flattening of ellipsoid
    private double rh = 0; // height above ellipsoid

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
    public LambertConformalConic2SP(ArrayList<ProjectionParameter> parameters) {
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
    public LambertConformalConic2SP(ArrayList<ProjectionParameter> parameters, boolean isInverse) {
        super(parameters, isInverse);
        this.setName("Lambert_Conformal_Conic_2SP");
        this.setAuthority("EPSG");
        this.setAuthorityCode(9802);

        ProjectionParameter latitude_of_origin = getParameter("latitude_of_origin");
        ProjectionParameter central_meridian = getParameter("central_meridian");
        ProjectionParameter standard_parallel_1 = getParameter("standard_parallel_1");
        ProjectionParameter standard_parallel_2 = getParameter("standard_parallel_2");
        ProjectionParameter false_easting = getParameter("false_easting");
        ProjectionParameter false_northing = getParameter("false_northing");

        if (latitude_of_origin == null) {
            throw new IllegalArgumentException("Missing projection parameter 'latitude_of_origin'");
        }
        if (central_meridian == null) {
            throw new IllegalArgumentException("Missing projection parameter 'central_meridian'");
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

        double c_lat = degrees2Radians(latitude_of_origin.getValue());
        double c_lon = degrees2Radians(central_meridian.getValue());
        double lat1 = degrees2Radians(standard_parallel_1.getValue());
        double lat2 = degrees2Radians(standard_parallel_2.getValue());
        this._falseEasting = false_easting.getValue() * _metersPerUnit;
        this._falseNorthing = false_northing.getValue() * _metersPerUnit;

        double sin_po = 0;
        double cos_po = 0;
        double con;
        double ms1;
        double ms2;
        double ts0;
        double ts1;
        double ts2;

        if (Math.abs(lat1 + lat2) < EPSLN) {
            throw new IllegalArgumentException("Equal latitudes for St. Parallels on opposite sides of equator.");
        }

        double es = 1.0 - Math.pow(this._semiMinor / this._semiMajor, 2);
        e = Math.sqrt(es);

        center_lon = c_lon;
        Ref<Double> tempRef_sin_po = new Ref<>(sin_po);
        Ref<Double> tempRef_cos_po = new Ref<>(cos_po);
        sincos(lat1, tempRef_sin_po, tempRef_cos_po);
        cos_po = tempRef_cos_po.value;
        sin_po = tempRef_sin_po.value;
        con = sin_po;
        ms1 = msfnz(e, sin_po, cos_po);
        ts1 = tsfnz(e, lat1, sin_po);
        Ref<Double> tempRef_sin_po2 = new Ref<>(sin_po);
        Ref<Double> tempRef_cos_po2 = new Ref<>(cos_po);
        sincos(lat2, tempRef_sin_po2, tempRef_cos_po2);
        cos_po = tempRef_cos_po2.value;
        sin_po = tempRef_sin_po2.value;
        ms2 = msfnz(e, sin_po, cos_po);
        ts2 = tsfnz(e, lat2, sin_po);
        sin_po = Math.sin(c_lat);
        ts0 = tsfnz(e, c_lat, sin_po);

        if (Math.abs(lat1 - lat2) > EPSLN) {
            ns = Math.log(ms1 / ms2) / Math.log(ts1 / ts2);
        } else {
            ns = con;
        }
        f0 = ms1 / (ns * Math.pow(ts1, ns));
        rh = this._semiMajor * f0 * Math.pow(ts0, ns);
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

        double con;
        double rh1;
        double sinphi;
        double theta;
        double ts;

        con = Math.abs(Math.abs(dLatitude) - HALF_PI);
        if (con > EPSLN) {
            sinphi = Math.sin(dLatitude);
            ts = tsfnz(e, dLatitude, sinphi);
            rh1 = this._semiMajor * f0 * Math.pow(ts, ns);
        } else {
            con = dLatitude * ns;
            if (con <= 0) {
                throw new IllegalArgumentException();
            }
            rh1 = 0;
        }
        theta = ns * adjustLon(dLongitude - center_lon);
        dLongitude = rh1 * Math.sin(theta) + this._falseEasting;
        dLatitude = rh - rh1 * Math.cos(theta) + this._falseNorthing;
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
        double dLongitude;
        double dLatitude;

        double rh1;
        double con;
        double ts;
        double theta;
        long flag;

        flag = 0;
        double dX = p[0] * _metersPerUnit - this._falseEasting;
        double dY = rh - p[1] * _metersPerUnit + this._falseNorthing;
        if (ns > 0) {
            rh1 = Math.sqrt(dX * dX + dY * dY);
            con = 1.0;
        } else {
            rh1 = -Math.sqrt(dX * dX + dY * dY);
            con = -1.0;
        }
        theta = 0.0;
        if (rh1 != 0) {
            theta = Math.atan2((con * dX), (con * dY));
        }
        if ((rh1 != 0) || (ns > 0.0)) {
            con = 1.0 / ns;
            ts = Math.pow((rh1 / (this._semiMajor * f0)), con);
            Ref<Long> tempRef_flag = new Ref<>(flag);
            dLatitude = phi2z(e, ts, tempRef_flag);
            flag = tempRef_flag.value;
            if (flag != 0) {
                throw new IllegalArgumentException();
            }
        } else {
            dLatitude = -HALF_PI;
        }

        dLongitude = adjustLon(theta / ns + center_lon);
        if (p.length == 2) {
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
            _inverse = new LambertConformalConic2SP(this._parameters, !_isInverse);
        }
        return _inverse;
    }
}