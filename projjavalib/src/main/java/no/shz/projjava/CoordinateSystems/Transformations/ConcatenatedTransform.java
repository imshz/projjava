package no.shz.projjava.coordinateSystems.transformations;

import java.util.ArrayList;
import java.util.Collections;

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

 */
public class ConcatenatedTransform extends MathTransform {
    /**

     */
    protected IMathTransform _inverse;
    private ArrayList<ICoordinateTransformation> _coordinateTransformationList;

    /**

     */
    public ConcatenatedTransform() {
        this(new ArrayList<ICoordinateTransformation>());
    }

    /**
     * @param transformlist ArrayList
     */
    public ConcatenatedTransform(ArrayList<ICoordinateTransformation> transformlist) {
        _coordinateTransformationList = transformlist;
    }

    /**

     */
    public final ArrayList<ICoordinateTransformation> getCoordinateTransformationList() {
        return _coordinateTransformationList;
    }

    public final void setCoordinateTransformationList(ArrayList<ICoordinateTransformation> value) {
        _coordinateTransformationList = value;
        _inverse = null;
    }

    /**
     * Transforms a point
     *
     * @param point double
     * @return double
     */
    @Override
    public double[] transform(double[] point) {
        for (ICoordinateTransformation ct : _coordinateTransformationList) {
            point = ct.getMathTransform().transform(point);
        }
        return point;
    }

    /**
     * Transforms a list point
     *
     * @param points ArrayList
     * @return ArrayList
     */
    @Override
    public ArrayList<double[]> transformList(ArrayList<double[]> points) {
        ArrayList<double[]> pnts = new ArrayList<>(points.size());
        pnts.addAll(points);
        for (ICoordinateTransformation ct : _coordinateTransformationList) {
            pnts = ct.getMathTransform().transformList(pnts);
        }
        return pnts;
    }

    /**
     * Returns the inverse of this conversion.
     *
     * @return IMathTransform that is the reverse of the current conversion.
     */
    @Override
    public IMathTransform inverse() {
        if (_inverse == null) {
            _inverse = this.clone();
            _inverse.invert();
        }
        return _inverse;
    }

    /**
     * Reverses the transformation
     */
    @Override
    public void invert() {
        Collections.reverse(_coordinateTransformationList);
        for (ICoordinateTransformation ic : _coordinateTransformationList) {
            ic.getMathTransform().invert();
        }
    }

    public final ConcatenatedTransform clone() {
        ArrayList<ICoordinateTransformation> clonedList = new ArrayList<ICoordinateTransformation>(_coordinateTransformationList.size());
        for (ICoordinateTransformation ct : _coordinateTransformationList) {
            clonedList.add(ct);
        }
        return new ConcatenatedTransform(clonedList);
    }

    /**
     * Gets a Well-Known text representation of this object.
     * @return String
     */
    @Override
    public String getWKT() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an XML representation of this object.
     * @return String
     */
    @Override
    public String getXML() {
        throw new UnsupportedOperationException();
    }
}