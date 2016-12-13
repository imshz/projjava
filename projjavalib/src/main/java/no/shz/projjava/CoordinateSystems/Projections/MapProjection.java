package no.shz.projjava.coordinateSystems.projections;

import java.util.ArrayList;

import no.shz.projjava.coordinateSystems.IProjection;
import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.transformations.MathTransform;
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
 * projections inherit from this abstract class to get access to useful mathematical functions.
 */
public abstract class MapProjection extends MathTransform implements IProjection {

    protected static final double PI = Math.PI;
    protected static final double HALF_PI = (PI * 0.5);
    protected static final double TWO_PI = (PI * 2.0);
    protected static final double EPSLN = 1.0e-10;
    protected static final double S2R = 4.848136811095359e-6;
    protected static final double MAX_VAL = 4;
    protected static final double prjMAXLONG = 2147483647;
    protected static final double DBLLONG = 4.61168601e18;

    protected boolean _isInverse = false;
    protected double _es;
    protected double _semiMajor;
    protected double _semiMinor;
    protected double _metersPerUnit;
    protected ArrayList<ProjectionParameter> _parameters;
    protected MathTransform _inverse;
    private String _abbreviation;
    private String _alias;
    private String _authority;
    private long _code;
    private String _name;
    private String _remarks;

    /**
     * @param parameters ArrayList
     * @param isInverse boolean
     */
    protected MapProjection(ArrayList<ProjectionParameter> parameters, boolean isInverse) {
        this(parameters);
        _isInverse = isInverse;
    }

    /**
     * @param parameters ArrayList
     */
    protected MapProjection(ArrayList<ProjectionParameter> parameters) {
        _parameters = parameters;

        ProjectionParameter semimajor = getParameter("semi_major");
        ProjectionParameter semiminor = getParameter("semi_minor");

        if (semimajor == null) {
            throw new IllegalArgumentException("Missing projection parameter 'semi_major'");
        }
        if (semiminor == null) {
            throw new IllegalArgumentException("Missing projection parameter 'semi_minor'");
        }
        this._semiMajor = semimajor.getValue();
        this._semiMinor = semiminor.getValue();
        ProjectionParameter unit = getParameter("unit");
        _metersPerUnit = unit.getValue();

        this._es = 1.0 - (_semiMinor * _semiMinor) / (_semiMajor * _semiMajor);
    }

    /**
     * Returns the cube of a number.
     *
     * @param x double
     */
    protected static double cube(double x) {
        return Math.pow(x, 3); // x^3
    }

    /**
     * Returns the quad of a number.
     *
     * @param x double
     */
    protected static double quad(double x) {
        return Math.pow(x, 4); // x^4
    }

    /**
     * @param a double
     * @param b double
     * @return
     */
    protected static double gmax(Ref<Double> a, Ref<Double> b) {
        return Math.max(a.value, b.value); // assign maximum of a and b
    }

    /**
     * @param a
     * @param b
     * @return
     */
    protected static double gmin(Ref<Double> a, Ref<Double> b) {
        return ((a.value) < (b.value) ? (a.value) : (b.value)); // assign minimum of a and b
    }

    /**
     * imod
     *
     * @param a
     * @param b
     * @return
     */
    protected static double imod(double a, double b) {
        return (a) - (((a) / (b)) * (b)); // Integer mod function

    }

    /**
     * Function to return the sign of an argument
     */
    protected static double sign(double x) {
        if (x < 0.0) {
            return (-1);
        } else {
            return (1);
        }
    }

    /**
     * @param x
     * @return
     */
    protected static double adjustLon(double x) {
        long count = 0;
        for (; ; ) {
            if (Math.abs(x) <= PI) {
                break;
            } else {
                if (((long) Math.abs(x / Math.PI)) < 2) {
                    x = x - (sign(x) * TWO_PI);
                } else {
                    if (((long) Math.abs(x / TWO_PI)) < prjMAXLONG) {
                        x = x - (((long) (x / TWO_PI)) * TWO_PI);
                    } else {
                        if (((long) Math.abs(x / (prjMAXLONG * TWO_PI))) < prjMAXLONG) {
                            x = x - (((long) (x / (prjMAXLONG * TWO_PI))) * (TWO_PI * prjMAXLONG));
                        } else {
                            if (((long) Math.abs(x / (DBLLONG * TWO_PI))) < prjMAXLONG) {
                                x = x - (((long) (x / (DBLLONG * TWO_PI))) * (TWO_PI * DBLLONG));
                            } else {
                                x = x - (sign(x) * TWO_PI);
                            }
                        }
                    }
                }
            }
            count++;
            if (count > MAX_VAL) {
                break;
            }
        }
        return (x);
    }

    /**
     * Function to compute the constant small m which is the radius of
     * a parallel of latitude, phi, divided by the semimajor axis.
     */
    protected static double msfnz(double eccent, double sinphi, double cosphi) {
        double con;

        con = eccent * sinphi;
        return ((cosphi / (Math.sqrt(1.0 - con * con))));
    }

    /**
     * Function to compute constant small q which is the radius of a
     * parallel of latitude, phi, divided by the semimajor axis.
     */
    protected static double qsfnz(double eccent, double sinphi) {
        double con;

        if (eccent > 1.0e-7) {
            con = eccent * sinphi;
            return ((1.0 - eccent * eccent) * (sinphi / (1.0 - con * con) - (.5 / eccent) * Math.log((1.0 - con) / (1.0 + con))));
        } else {
            return 2.0 * sinphi;
        }
    }

    /**
     * Function to calculate the sine and cosine in one call.  Some computer
     * systems have implemented this function, resulting in a faster implementation
     * than calling each function separately.  It is provided here for those
     * computer systems which don`t implement this function
     */
    protected static void sincos(double val, Ref<Double> sinVal, Ref<Double> cosVal) {
        sinVal.value = Math.sin(val);
        cosVal.value = Math.cos(val);
    }

    /**
     * Function to compute the constant small t for use in the forward
     * computations in the Lambert Conformal Conic and the Polar
     * Stereographic projections.
     */
    protected static double tsfnz(double eccent, double phi, double sinphi) {
        double con;
        double com;
        con = eccent * sinphi;
        com = .5 * eccent;
        con = Math.pow(((1.0 - con) / (1.0 + con)), com);
        return (Math.tan(.5 * (HALF_PI - phi)) / con);
    }

    /**
     * @param eccent
     * @param qs
     * @param flag
     * @return
     */
    protected static double phi1z(double eccent, double qs, Ref<Long> flag) {
        double eccnts;
        double dphi;
        double con;
        double com;
        double sinpi = 0;
        double cospi = 0;
        double phi;
        flag.value = 0l;
        long i;

        phi = asinz(.5 * qs);
        if (eccent < EPSLN) {
            return (phi);
        }
        eccnts = eccent * eccent;
        for (i = 1; i <= 25; i++) {
            Ref<Double> tempRef_sinpi = new Ref<Double>(sinpi);
            Ref<Double> tempRef_cospi = new Ref<Double>(cospi);
            sincos(phi, tempRef_sinpi, tempRef_cospi);
            cospi = tempRef_cospi.value;
            sinpi = tempRef_sinpi.value;
            con = eccent * sinpi;
            com = 1.0 - con * con;
            dphi = .5 * com * com / cospi * (qs / (1.0 - eccnts) - sinpi / com + .5 / eccent * Math.log((1.0 - con) / (1.0 + con)));
            phi = phi + dphi;
            if (Math.abs(dphi) <= 1e-7) {
                return (phi);
            }
        }

        throw new IllegalArgumentException("Convergence error.");
    }

    /**
     * Function to eliminate roundoff errors in asin
     */
    protected static double asinz(double con) {
        if (Math.abs(con) > 1.0) {
            if (con > 1.0) {
                con = 1.0;
            } else {
                con = -1.0;
            }
        }
        return (Math.asin(con));
    }

    /**
     * Function to compute the latitude angle, phi2, for the inverse of the
     * Lambert Conformal Conic and Polar Stereographic projections.
     *
     * @param eccent Spheroid eccentricity
     * @param ts     Constant value t
     * @param flag   Error flag number
     */
    protected static double phi2z(double eccent, double ts, Ref<Long> flag) {
        double con;
        double dphi;
        double sinpi;
        long i;

        flag.value = 0l;
        double eccnth = .5 * eccent;
        double chi = HALF_PI - 2 * Math.atan(ts);
        for (i = 0; i <= 15; i++) {
            sinpi = Math.sin(chi);
            con = eccent * sinpi;
            dphi = HALF_PI - 2 * Math.atan(ts * (Math.pow(((1.0 - con) / (1.0 + con)), eccnth))) - chi;
            chi += dphi;
            if (Math.abs(dphi) <= .0000000001) {
                return (chi);
            }
        }
        throw new IllegalArgumentException("Convergence error - phi2z-conv");
    }

    /**
     * Functions to compute the constants e0, e1, e2, and e3 which are used
     * in a series for calculating the distance along a meridian.  The
     * input x represents the eccentricity squared.
     */
    protected static double e0fn(double x) {
        return (1.0 - 0.25 * x * (1.0 + x / 16.0 * (3.0 + 1.25 * x)));
    }

    /**
     * @param x
     * @return
     */
    protected static double e1fn(double x) {
        return (0.375 * x * (1.0 + 0.25 * x * (1.0 + 0.46875 * x)));
    }

    /**
     * @param x
     * @return
     */
    protected static double e2fn(double x) {
        return (0.05859375 * x * x * (1.0 + 0.75 * x));
    }

    // defines some usefull constants that are used in the projection routines

    /**
     * @param x
     * @return
     */
    protected static double e3fn(double x) {
        return (x * x * x * (35.0 / 3072.0));
    }

    /**
     * Function to compute the constant e4 from the input of the eccentricity
     * of the spheroid, x.  This constant is used in the Polar Stereographic
     * projection.
     */
    protected static double e4fn(double x) {
        double con;
        double com;
        con = 1.0 + x;
        com = 1.0 - x;
        return (Math.sqrt((Math.pow(con, con)) * (Math.pow(com, com))));
    }

    /**
     * Function computes the value of M which is the distance along a meridian
     * from the Equator to latitude phi.
     */
    protected static double mlfn(double e0, double e1, double e2, double e3, double phi) {
        return (e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi));
    }

    /**
     * Function to calculate UTM zone number--NOTE Longitude entered in DEGREES!!!
     */
    protected static long calcUtmZone(double lon) {
        return ((long) (((lon + 180.0) / 6.0) + 1.0));
    }

    /**
     * Converts a longitude value in degrees to radians.
     *
     * @param x    The value in degrees to convert to radians.
     * @param edge If true, -180 and +180 are valid, otherwise they are considered out of range.
     * @return
     */
    protected static double longitudeToRadians(double x, boolean edge) {
        if (edge ? (x >= -180 && x <= 180) : (x > -180 && x < 180)) {
            return degrees2Radians(x);
        }
        throw new IllegalArgumentException("x = " + (new Double(x)).toString() + " not a valid longitude in degrees.");
    }

    /**
     * Converts a latitude value in degrees to radians.
     *
     * @param y    The value in degrees to to radians.
     * @param edge If true, -90 and +90 are valid, otherwise they are considered out of range.
     * @return
     */
    protected static double latitudeToRadians(double y, boolean edge) {
        if (edge ? (y >= -90 && y <= 90) : (y > -90 && y < 90)) {
            return degrees2Radians(y);
        }
        throw new IllegalArgumentException("y = " + (new Double(y)).toString() + " not a valid latitude in degrees.");
    }

    /**
     * @param Index
     * @return
     */
    public final ProjectionParameter getParameter(int Index) {
        return this._parameters.get(Index);
    }

    /**
     * Gets an named parameter of the projection.
     * <p>
     * The parameter name is case insensitive
     *
     * @param name Name of parameter
     * @return parameter or null if not found
     */
    public final ProjectionParameter getParameter(String name) {

        for (ProjectionParameter pp : _parameters) {
            if (pp.getName().equalsIgnoreCase(name))
                return pp;
        }

        return null;
    }

    /**

     */
    public final int getNumParameters() {
        return this._parameters.size();
    }

    /**

     */
    public final String getClassName() {
        return this.getClassName();
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
     * Gets or sets the alias of the object.
     */
    public final String getAlias() {
        return _alias;
    }

    public final void setAlias(String value) {
        _alias = value;
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
     * Gets or sets the name of the object.
     */
    public final String getName() {
        return _name;
    }

    public final void setName(String value) {
        _name = value;
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
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        if (_isInverse) {
            sb.append("INVERSE_MT[");
        }
        sb.append(String.format("PARAM_MT[\"%1$s\"", this.getName()));
        for (int i = 0; i < this.getNumParameters(); i++) {
            sb.append(String.format(", %1$s", this.getParameter(i).getWKT()));
        }

        sb.append("]");
        if (_isInverse) {
            sb.append("]");
        }
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    @Override
    public String getXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<CT_MathTransform>");
        if (_isInverse) {
            sb.append(String.format("<CT_InverseTransform Name=\"%1$s\">", getClassName()));
        } else {
            sb.append(String.format("<CT_ParameterizedMathTransform Name=\"%1$s\">", getClassName()));
        }
        for (int i = 0; i < this.getNumParameters(); i++) {
            sb.append(this.getParameter(i).getXML());
        }
        if (_isInverse) {
            sb.append("</CT_InverseTransform>");
        } else {
            sb.append("</CT_ParameterizedMathTransform>");
        }
        sb.append("</CT_MathTransform>");
        return sb.toString();
    }

    public abstract double[] metersToDegrees(double[] p);

    public abstract double[] degreesToMeters(double[] lonlat);

    /**
     * Reverses the transformation
     */
    @Override
    public void invert() {
        _isInverse = !_isInverse;
    }

    /**
     * Returns true if this projection is inverted.
     * Most map projections define forward projection as "from geographic to projection", and backwards
     * as "from projection to geographic". If this projection is inverted, this will be the other way around.
     */
    public final boolean getIsInverse() {
        return _isInverse;
    }

    /**
     * Transforms the specified cp.
     *
     * @param cp The cp.
     * @return
     */
    @Override
    public double[] transform(double[] cp) {
        double[] projectedPoint = new double[]{0, 0, 0};
        if (!_isInverse) {
            return this.degreesToMeters(cp);
        } else {
            return this.metersToDegrees(cp);
        }
    }

    /**
     * @param ord ArrayList
     * @return ArrayList
     */
    @Override
    public ArrayList<double[]> transformList(ArrayList<double[]> ord) {
        ArrayList<double[]> result = new ArrayList<double[]>(ord.size());
        for (int i = 0; i < ord.size(); i++) {
            double[] point = ord.get(i);
            result.add(transform(point));
        }
        return result;
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj Object
     * @return True if equal
     */
    public final boolean equalParams(Object obj) {
        if (!(obj instanceof MapProjection)) {
            return false;
        }
        MapProjection proj = (MapProjection) ((obj instanceof MapProjection) ? obj : null);
        if (proj.getNumParameters() != this.getNumParameters()) {
            return false;
        }
        for (int i = 0; i < _parameters.size(); i++) {
            ProjectionParameter param = null;
            for (ProjectionParameter pp : _parameters) {
                if (pp.getName().equalsIgnoreCase(proj.getParameter(i).getName())) {
                    param = pp;
                    break;
                }
            }

            if (param == null) {
                return false;
            }
            if (param.getValue() != proj.getParameter(i).getValue()) {
                return false;
            }
        }
        return this.getIsInverse() == proj.getIsInverse();
    }
}