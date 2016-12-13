package no.shz.projjava.coordinateSystems.transformations;

import no.shz.projjava.coordinateSystems.ICoordinateSystem;

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
 * Creates coordinate transformations.
 */
public interface ICoordinateTransformationFactory {
    /**
     * Creates a transformation between two coordinate systems.
     * <p>
     * This method will examine the coordinate systems in order to construct
     * a transformation between them. This method may fail if no path between
     * the coordinate systems is found, using the normal failing behavior of
     * the DCP (e.g. throwing an exception).
     *
     * @param sourceCS Source coordinate system
     * @param targetCS Target coordinate system
     * @return
     */
    ICoordinateTransformation createFromCoordinateSystems(ICoordinateSystem sourceCS, ICoordinateSystem targetCS);
}