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
 * A vertical datum of geoid model derived heights, also called GPS-derived heights.
 * These heights are approximations of orthometric heights (H), constructed from the
 * ellipsoidal heights (h) by the use of the given geoid undulation model (N) through
 * the equation: H=h-N.
 */
public enum DatumType {
    /**
     * Lowest possible value for horizontal datum types
     */
    HD_Min(1000),
    /**
     * Unspecified horizontal datum type. Horizontal datums with this type should never
     * supply a conversion to WGS84 using Bursa Wolf parameters.
     */
    HD_Other(1000),
    /**
     * These datums, such as ED50, NAD27 and NAD83, have been designed to support
     * horizontal positions on the ellipsoid as opposed to positions in 3-D space. These datums were designed mainly to support a horizontal component of a position in a domain of limited extent, such as a country, a region or a continent.
     */
    HD_Classic(1001),
    /**
     * A geocentric datum is a "satellite age" modern geodetic datum mainly of global
     * extent, such as WGS84 (used in GPS), PZ90 (used in GLONASS) and ITRF. These
     * datums were designed to support both a horizontal component of position and
     * a vertical component of position (through ellipsoidal heights). The regional
     * realizations of ITRF, such as ETRF, are also included in this category.
     */
    HD_Geocentric(1002),
    /**
     * Highest possible value for horizontal datum types.
     */
    HD_Max(1999),
    /**
     * Lowest possible value for vertical datum types.
     */
    VD_Min(2000),
    /**
     * Unspecified vertical datum type.
     */
    VD_Other(2000),
    /**
     * A vertical datum for orthometric heights that are measured along the plumb line.
     */
    VD_Orthometric(2001),
    /**
     * A vertical datum for ellipsoidal heights that are measured along the normal to
     * the ellipsoid used in the definition of horizontal datum.
     */
    VD_Ellipsoidal(2002),
    /**
     * The vertical datum of altitudes or heights in the atmosphere. These are
     * approximations of orthometric heights obtained with the help of a barometer or
     * a barometric altimeter. These values are usually expressed in one of the
     * following units: meters, feet, millibars (used to measure pressure levels), or
     * theta value (units used to measure geopotential height).
     */
    VD_AltitudeBarometric(2003),
    /**
     * A normal height system.
     */
    VD_Normal(2004),
    /**
     * A vertical datum of geoid model derived heights, also called GPS-derived heights.
     * These heights are approximations of orthometric heights (H), constructed from the
     * ellipsoidal heights (h) by the use of the given geoid undulation model (N)
     * through the equation: H=h-N.
     */
    VD_GeoidModelDerived(2005),
    /**
     * This attribute is used to support the set of datums generated for hydrographic
     * engineering projects where depth measurements below sea level are needed. It is
     * often called a hydrographic or a marine datum. Depths are measured in the
     * direction perpendicular (approximately) to the actual equipotential surfaces of
     * the earth's gravity field, using such procedures as echo-sounding.
     */
    VD_Depth(2006),
    /**
     * Highest possible value for vertical datum types.
     */
    VD_Max(2999),
    /**
     * Lowest possible value for local datum types.
     */
    LD_Min(10000),
    /**
     * Highest possible value for local datum types.
     */
    LD_Max(32767);

    private static java.util.HashMap<Integer, DatumType> mappings;
    private int intValue;

    DatumType(int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    private static java.util.HashMap<Integer, DatumType> getMappings() {
        if (mappings == null) {
            synchronized (DatumType.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<>();
                }
            }
        }
        return mappings;
    }

    public static DatumType forValue(int value) {
        return getMappings().get(value);
    }

    public int getValue() {
        return intValue;
    }
}