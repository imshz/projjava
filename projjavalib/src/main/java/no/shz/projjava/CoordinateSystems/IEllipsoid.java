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
 * The IEllipsoid interface defines the standard information stored with ellipsoid objects.
 */
public interface IEllipsoid extends IInfo {
    /**
     * Gets or sets the value of the semi-major axis.
     */
    double getSemiMajorAxis();

    void setSemiMajorAxis(double value);

    /**
     * Gets or sets the value of the semi-minor axis.
     */
    double getSemiMinorAxis();

    void setSemiMinorAxis(double value);

    /**
     * Gets or sets the value of the inverse of the flattening constant of the ellipsoid.
     */
    double getInverseFlattening();

    void setInverseFlattening(double value);

    /**
     * Gets or sets the value of the axis unit.
     */
    ILinearUnit getAxisUnit();

    void setAxisUnit(ILinearUnit value);

    /**
     * Is the inverse Flattening definitive for this ellipsoid? Some ellipsoids use the
     * IVF as the defining value, and calculate the polar radius whenever asked. Other
     * ellipsoids use the polar radius to calculate the IVF whenever asked. This
     * distinction can be important to avoid floating-point rounding errors.
     */
    boolean getIsIvfDefinitive();

    void setIsIvfDefinitive(boolean value);
}