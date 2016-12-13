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
 * Orientation of axis. Some coordinate systems use non-standard orientations.
 * For example, the first axis in South African grids usually points West,
 * instead of East. This information is obviously relevant for algorithms
 * converting South African grid coordinates into Lat/Long.
 */
public enum AxisOrientationEnum {
    /**
     * Unknown or unspecified axis orientation. This can be used for local or fitted coordinate systems.
     */
    Other(0),
    /**
     * Increasing ordinates values go North. This is usually used for Grid Y coordinates and Latitude.
     */
    North(1),
    /**
     * Increasing ordinates values go South. This is rarely used.
     */
    South(2),
    /**
     * Increasing ordinates values go East. This is rarely used.
     */
    East(3),
    /**
     * Increasing ordinates values go West. This is usually used for Grid X coordinates and Longitude.
     */
    West(4),
    /**
     * Increasing ordinates values go up. This is used for vertical coordinate systems.
     */
    Up(5),
    /**
     * Increasing ordinates values go down. This is used for vertical coordinate systems.
     */
    Down(6);

    private static java.util.HashMap<Integer, AxisOrientationEnum> mappings;
    private int intValue;

    AxisOrientationEnum(int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    private static java.util.HashMap<Integer, AxisOrientationEnum> getMappings() {
        if (mappings == null) {
            synchronized (AxisOrientationEnum.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<>();
                }
            }
        }
        return mappings;
    }

    public static AxisOrientationEnum forValue(int value) {
        return getMappings().get(value);
    }

    public int getValue() {
        return intValue;
    }
}