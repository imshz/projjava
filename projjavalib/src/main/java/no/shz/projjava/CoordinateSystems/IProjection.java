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
 * The IProjection interface defines the standard information stored with projection
 * objects. A projection object implements a coordinate transformation from a geographic
 * coordinate system to a projected coordinate system, given the ellipsoid for the
 * geographic coordinate system. It is expected that each coordinate transformation of
 * interest, e.g., Transverse Mercator, Lambert, will be implemented as a COM class of
 * coType Projection, supporting the IProjection interface.
 */
public interface IProjection extends IInfo {
    /**
     * Gets number of parameters of the projection.
     */
    int getNumParameters();

    /**
     * Gets the projection classification name (e.g. 'Transverse_Mercator').
     */
    String getClassName();

    /**
     * Gets an indexed parameter of the projection.
     *
     * @param n Index of parameter
     * @return n'th parameter
     */
    ProjectionParameter getParameter(int n);

    /**
     * Gets an named parameter of the projection.
     * <p>
     * The parameter name is case insensitive
     *
     * @param name Name of parameter
     * @return parameter or null if not found
     */
    ProjectionParameter getParameter(String name);
}