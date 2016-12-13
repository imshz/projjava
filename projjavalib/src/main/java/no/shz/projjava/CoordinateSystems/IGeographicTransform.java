package no.shz.projjava.coordinateSystems;

import java.util.ArrayList;

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
 * The IGeographicTransform interface is implemented on geographic transformation
 * objects and implements datum transformations between geographic coordinate systems.
 */
public interface IGeographicTransform extends IInfo {
    /**
     * Gets or sets source geographic coordinate system for the transformation.
     */
    IGeographicCoordinateSystem getSourceGCS();

    void setSourceGCS(IGeographicCoordinateSystem value);

    /**
     * Gets or sets the target geographic coordinate system for the transformation.
     */
    IGeographicCoordinateSystem getTargetGCS();

    void setTargetGCS(IGeographicCoordinateSystem value);

    /**
     * Returns an accessor interface to the parameters for this geographic transformation.
     */
    IParameterInfo getParameterInfo();

    /**
     * Transforms an array of points from the source geographic coordinate system
     * to the target geographic coordinate system.
     *
     * @param points Points in the source geographic coordinate system
     * @return Points in the target geographic coordinate system
     */
    ArrayList<double[]> forward(ArrayList<double[]> points);

    /**
     * Transforms an array of points from the target geographic coordinate system
     * to the source geographic coordinate system.
     *
     * @param points Points in the target geographic coordinate system
     * @return Points in the source geographic coordinate system
     */
    ArrayList<double[]> inverse(ArrayList<double[]> points);
}