package no.shz.projjava.coordinateSystems;

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
 * Base interface for all coordinate systems
 * <p>
 * <p>A coordinate system is a mathematical space, where the elements of the space are called
 * positions. Each position is described by a list of numbers. The length of the list corresponds
 * to the dimension of the coordinate system. So in a 2D coordinate system each position is
 * described by a list containing 2 numbers.</p>
 * <p>
 * However, in a coordinate system, not all lists of numbers correspond to a position -
 * some lists may be outside the domain of the coordinate system. For example, in a 2D Lat/Lon
 * coordinate system, the list (91,91) does not correspond to a position.</p>
 * <p>
 * Some coordinate systems also have a mapping from the mathematical space into locations
 * in the real world. So in a Lat/Lon coordinate system, the mathematical position (lat, long)
 * corresponds to a location on the surface of the Earth. This mapping from the mathematical
 * space into real-world locations is called a Datum.</p>
 */
public interface ICoordinateSystem extends IInfo {
    /**
     * Dimension of the coordinate system.
     */
    int getDimension();

    /**
     * Gets axis details for dimension within coordinate system.
     *
     * @param dimension Dimension
     * @return Axis info
     */
    AxisInfo getAxis(int dimension);

    /**
     * Gets units for dimension within coordinate system.
     */
    IUnit getUnits(int dimension);

    /**
     * Gets default envelope of coordinate system.
     * <p>
     * <p>
     * Gets default envelope of coordinate system. Coordinate systems
     * which are bounded should return the minimum bounding box of their
     * domain. Unbounded coordinate systems should return a box which is
     * as large as is likely to be used. For example, a (lon,lat)
     * geographic coordinate system in degrees should return a box from
     * (-180,-90) to (180,90), and a geocentric coordinate system could
     * return a box from (-r,-r,-r) to (+r,+r,+r) where r is the
     * approximate radius of the Earth.
     */
    double[] getDefaultEnvelope();
}