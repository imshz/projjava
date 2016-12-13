package no.shz.projjava.coordinateSystems;

import java.util.Locale;

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
 * Details of axis. This is used to label axes, and indicate the orientation.
 */
public class AxisInfo {
    private String _name;
    private AxisOrientationEnum _orientation = AxisOrientationEnum.values()[0];

    /**
     * Initializes a new instance of an AxisInfo.
     *
     * @param name        Name of axis
     * @param orientation Axis orientation
     */
    public AxisInfo(String name, AxisOrientationEnum orientation) {
        _name = name;
        _orientation = orientation;
    }

    /**
     * Human readable name for axis. Possible values are X, Y, Long, Lat or any other short string.
     */
    public final String getName() {
        return _name;
    }

    public final void setName(String value) {
        _name = value;
    }

    /**
     * Gets enumerated value for orientation.
     */
    public final AxisOrientationEnum getOrientation() {
        return _orientation;
    }

    public final void setOrientation(AxisOrientationEnum value) {
        _orientation = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public final String getWKT() {
        return String.format("AXIS[\"%1$s\", %2$s]", getName(), getOrientation().toString().toUpperCase(Locale.US));
    }

    /**
     * Gets an XML representation of this object
     */
    public final String getXML() {
        return String.format(Locale.US, "<CS_AxisInfo Name=\"%1$s\" Orientation=\"%2$s\"/>", getName(), getOrientation().toString().toUpperCase(Locale.US));
    }
}