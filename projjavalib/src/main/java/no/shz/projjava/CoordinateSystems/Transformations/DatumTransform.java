package no.shz.projjava.coordinateSystems.transformations;

import java.util.ArrayList;

import no.shz.projjava.coordinateSystems.Wgs84ConversionInfo;

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
 * Transformation for applying
 */
public class DatumTransform extends MathTransform {
    protected IMathTransform _inverse;
    private Wgs84ConversionInfo _ToWgs94;
    private double[] v;

    private boolean _isInverse = false;

    /**
     * Initializes a new instance of the <see cref="DatumTransform"/> class.
     *
     * @param towgs84
     */
    public DatumTransform(Wgs84ConversionInfo towgs84) {
        this(towgs84, false);
    }

    private DatumTransform(Wgs84ConversionInfo towgs84, boolean isInverse) {
        _ToWgs94 = towgs84;
        v = _ToWgs94.GetAffineTransform();
        _isInverse = isInverse;
    }

    /**
     * Gets a Well-Known text representation of this object.
     * <p>
     * <value></value>
     */
    @Override
    public String getWKT() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an XML representation of this object.
     * <p>
     * <value></value>
     */
    @Override
    public String getXML() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the inverse transform of this object.
     *
     * @return This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
     */
    @Override
    public IMathTransform inverse() {
        if (_inverse == null) {
            _inverse = new DatumTransform(_ToWgs94, !_isInverse);
        }
        return _inverse;
    }

    private double[] apply(double[] p) {
        return new double[]{v[0] * p[0] - v[3] * p[1] + v[2] * p[2] + v[4], v[3] * p[0] + v[0] * p[1] - v[1] * p[2] + v[5], -v[2] * p[0] + v[1] * p[1] + v[0] * p[2] + v[6]};
    }

    private double[] applyInverted(double[] p) {
        return new double[]{v[0] * p[0] + v[3] * p[1] - v[2] * p[2] - v[4], -v[3] * p[0] + v[0] * p[1] + v[1] * p[2] - v[5], v[2] * p[0] - v[1] * p[1] + v[0] * p[2] - v[6]};
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
            return apply(point);
        } else {
            return applyInverted(point);
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
        ArrayList<double[]> pnts = new ArrayList<>(points.size());
        for (double[] p : points) {
            pnts.add(transform(p));
        }
        return pnts;
    }

    /**
     * Reverses the transformation
     */
    @Override
    public void invert() {
        _isInverse = !_isInverse;
    }
}