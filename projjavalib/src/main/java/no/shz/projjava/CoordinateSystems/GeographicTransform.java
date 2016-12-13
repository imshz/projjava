package no.shz.projjava.coordinateSystems;

import java.util.ArrayList;

// Copyright 2005 - 2009 - Morten Nielsen (www.sharpgis.net)
//
// This file is part of ProjNet.
// ProjNet is free software; you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// ProjNet is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.

// You should have received a copy of the GNU Lesser General Public License
// along with ProjNet; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 

/*
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
public class GeographicTransform extends Info implements IGeographicTransform {
    private IGeographicCoordinateSystem _sourceGCS;
    private IGeographicCoordinateSystem _targetGCS;

    public GeographicTransform(String name, String authority, long code, String alias, String remarks, String abbreviation, IGeographicCoordinateSystem sourceGCS, IGeographicCoordinateSystem targetGCS) {
        super(name, authority, code, alias, abbreviation, remarks);

        _sourceGCS = sourceGCS;
        _targetGCS = targetGCS;
    }

    /**
     * Gets or sets the source geographic coordinate system for the transformation.
     */
    public final IGeographicCoordinateSystem getSourceGCS() {
        return _sourceGCS;
    }

    public final void setSourceGCS(IGeographicCoordinateSystem value) {
        _sourceGCS = value;
    }

    /**
     * Gets or sets the target geographic coordinate system for the transformation.
     */
    public final IGeographicCoordinateSystem getTargetGCS() {
        return _targetGCS;
    }

    public final void setTargetGCS(IGeographicCoordinateSystem value) {
        _targetGCS = value;
    }

    /**
     * Returns an accessor interface to the parameters for this geographic transformation.
     */
    public final IParameterInfo getParameterInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Transforms an array of points from the source geographic coordinate
     * system to the target geographic coordinate system.
     *
     * @param points On input points in the source geographic coordinate system
     * @return Output points in the target geographic coordinate system
     */
    public final ArrayList<double[]> forward(ArrayList<double[]> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * Transforms an array of points from the target geographic coordinate
     * system to the source geographic coordinate system.
     *
     * @param points Input points in the target geographic coordinate system,
     * @return Output points in the source geographic coordinate system
     */
    public final ArrayList<double[]> inverse(ArrayList<double[]> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
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
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    @Override
    public boolean equalParams(Object obj) {
        if (!(obj instanceof GeographicTransform)) {
            return false;
        }
        GeographicTransform gt = (GeographicTransform) ((obj instanceof GeographicTransform) ? obj : null);
        return gt.getSourceGCS().equalParams(this.getSourceGCS()) && gt.getTargetGCS().equalParams(this.getTargetGCS());
    }
}