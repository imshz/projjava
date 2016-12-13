package no.shz.projjava.coordinateSystems.transformations;

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
 * Flags indicating parts of domain covered by a convex hull.
 * <p>
 * These flags can be combined. For example, the value 3
 * corresponds to a combination of <see cref="Inside"/> and <see cref="Outside"/>,
 * which means that some parts of the convex hull are inside the
 * domain, and some parts of the convex hull are outside the domain.
 */
public enum DomainFlags {
    /**
     * At least one point in a convex hull is inside the transform's domain.
     */
    Inside(1),
    /**
     * At least one point in a convex hull is outside the transform's domain.
     */
    Outside(2),
    /**
     * At least one point in a convex hull is not transformed continuously.
     * <p>
     * As an example, consider a "Longitude_Rotation" transform which adjusts
     * longitude coordinates to take account of a change in Prime Meridian. If
     * the rotation is 5 degrees east, then the point (Lat=175,Lon=0) is not
     * transformed continuously, since it is on the meridian line which will
     * be split at +180/-180 degrees.
     */
    Discontinuous(4);

    private static java.util.HashMap<Integer, DomainFlags> mappings;
    private int intValue;

    DomainFlags(int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    private static java.util.HashMap<Integer, DomainFlags> getMappings() {
        if (mappings == null) {
            synchronized (DomainFlags.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<>();
                }
            }
        }
        return mappings;
    }

    public static DomainFlags forValue(int value) {
        return getMappings().get(value);
    }

    public int getValue() {
        return intValue;
    }
}