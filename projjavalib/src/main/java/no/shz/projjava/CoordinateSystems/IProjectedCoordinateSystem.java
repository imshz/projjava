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
 * The IProjectedCoordinateSystem interface defines the standard information stored with
 * projected coordinate system objects. A projected coordinate system is defined using a
 * geographic coordinate system object and a projection object that defines the
 * coordinate transformation from the geographic coordinate system to the projected
 * coordinate systems. The instances of a single ProjectedCoordinateSystem COM class can
 * be used to model different projected coordinate systems (e.g., UTM Zone 10, Albers)
 * by associating the ProjectedCoordinateSystem instances with Projection instances
 * belonging to different Projection COM classes (Transverse Mercator and Albers,
 * respectively).
 */
public interface IProjectedCoordinateSystem extends IHorizontalCoordinateSystem {
    /**
     * Gets or sets the geographic coordinate system associated with the projected
     * coordinate system.
     */
    IGeographicCoordinateSystem getGeographicCoordinateSystem();

    void setGeographicCoordinateSystem(IGeographicCoordinateSystem value);

    /**
     * Gets or sets the linear (projected) units of the projected coordinate system.
     */
    ILinearUnit getLinearUnit();

    void setLinearUnit(ILinearUnit value);

    /**
     * Gets or sets the projection for the projected coordinate system.
     */
    IProjection getProjection();

    void setProjection(IProjection value);
}