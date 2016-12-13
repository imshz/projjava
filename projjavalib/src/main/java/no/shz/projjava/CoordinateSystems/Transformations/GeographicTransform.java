package no.shz.projjava.coordinateSystems.transformations;

import java.util.ArrayList;

import no.shz.projjava.coordinateSystems.IGeographicCoordinateSystem;

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
 * The GeographicTransform class is implemented on geographic transformation objects and
 * implements datum transformations between geographic coordinate systems.
 */
public class GeographicTransform extends MathTransform {
    private IGeographicCoordinateSystem _SourceGCS;
    private IGeographicCoordinateSystem _TargetGCS;

    public GeographicTransform(IGeographicCoordinateSystem sourceGCS, IGeographicCoordinateSystem targetGCS) {
        _SourceGCS = sourceGCS;
        _TargetGCS = targetGCS;
    }

    /**
     * Gets or sets the source geographic coordinate system for the transformation.
     */
    public final IGeographicCoordinateSystem getSourceGCS() {
        return _SourceGCS;
    }

    public final void setSourceGCS(IGeographicCoordinateSystem value) {
        _SourceGCS = value;
    }

    /**
     * Gets or sets the target geographic coordinate system for the transformation.
     */
    public final IGeographicCoordinateSystem getTargetGCS() {
        return _TargetGCS;
    }

    public final void setTargetGCS(IGeographicCoordinateSystem value) {
        _TargetGCS = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification. [NOT IMPLEMENTED].
     */
    @Override
    public String getWKT() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an XML representation of this object [NOT IMPLEMENTED].
     */
    @Override
    public String getXML() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the inverse transform of this object.
     * <p>
     * This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
     *
     * @return
     */
    @Override
    public IMathTransform inverse() {
        throw new UnsupportedOperationException();
    }

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point
     * @return
     */
    @Override
    public double[] transform(double[] point) {
        double[] pOut = point.clone();
        pOut[0] /= getSourceGCS().getAngularUnit().getRadiansPerUnit();
        pOut[0] -= getSourceGCS().getPrimeMeridian().getLongitude() / getSourceGCS().getPrimeMeridian().getAngularUnit().getRadiansPerUnit();
        pOut[0] += getTargetGCS().getPrimeMeridian().getLongitude() / getTargetGCS().getPrimeMeridian().getAngularUnit().getRadiansPerUnit();
        pOut[0] *= getSourceGCS().getAngularUnit().getRadiansPerUnit();
        return pOut;
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     * <p>
     * <p>
     * This method is provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values. For example, if the source
     * dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
     * The size of the passed array must be an integer multiple of DimSource. The returned
     * ordinal values are packed in a similar way. In some DCPs. the ordinals may be
     * transformed in-place, and the returned array may be the same as the passed array.
     * So any client code should not attempt to reuse the passed ordinal values (although
     * they can certainly reuse the passed array). If there is any problem then the server
     * implementation will throw an exception. If this happens then the client should not
     * make any assumptions about the state of the ordinal values.
     *
     * @param points
     * @return
     */
    @Override
    public ArrayList<double[]> transformList(ArrayList<double[]> points) {
        ArrayList<double[]> trans = new ArrayList<>(points.size());
        for (double[] p : points) {
            trans.add(transform(p));
        }
        return trans;
    }

    /**
     * Reverses the transformation
     */
    @Override
    public void invert() {
        throw new UnsupportedOperationException();
    }
}