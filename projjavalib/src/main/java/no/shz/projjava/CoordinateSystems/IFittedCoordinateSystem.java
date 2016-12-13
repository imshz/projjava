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
 * A coordinate system which sits inside another coordinate system. The fitted
 * coordinate system can be rotated and shifted, or use any other math transform
 * to inject itself into the base coordinate system.
 */
public interface IFittedCoordinateSystem extends ICoordinateSystem {
    /**
     * Gets underlying coordinate system.
     */
    ICoordinateSystem getBaseCoordinateSystem();

    /**
     * Gets Well-Known Text of a math transform to the base coordinate system.
     * The dimension of this fitted coordinate system is determined by the source
     * dimension of the math transform. The transform should be one-to-one within
     * this coordinate system's domain, and the base coordinate system dimension
     * must be at least as big as the dimension of this coordinate system.
     *
     * @return
     */
    String toBase();
}