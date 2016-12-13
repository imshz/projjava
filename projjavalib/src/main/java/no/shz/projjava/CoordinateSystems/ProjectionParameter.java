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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * A named projection parameter value.
 * <p>
 * The linear units of parameters' values match the linear units of the containing
 * projected coordinate system. The angular units of parameter values match the
 * angular units of the geographic coordinate system that the projected coordinate
 * system is based on. (Notice that this is different from <see cref="Parameter"/>,
 * where the units are always meters and degrees.)
 */
public class ProjectionParameter {
    protected DecimalFormat _nfi = new DecimalFormat("0.################", new DecimalFormatSymbols(Locale.US));

    private String _name;
    private double _value;

    /**
     * Initializes an instance of a ProjectionParameter
     *
     * @param name  Name of parameter
     * @param value Parameter value
     */
    public ProjectionParameter(String name, double value) {
        _name = name;
        _value = value;
    }

    /**
     * Parameter name.
     */
    public final String getName() {
        return _name;
    }

    public final void setName(String value) {
        _name = value;
    }

    /**
     * Parameter value.
     * The linear units of a parameters' values match the linear units of the containing
     * projected coordinate system. The angular units of parameter values match the
     * angular units of the geographic coordinate system that the projected coordinate
     * system is based on.
     */
    public final double getValue() {
        return _value;
    }

    public final void setValue(double value) {
        _value = value;
    }


    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public final String getWKT() {
        return String.format(Locale.US, "PARAMETER[\"%1$s\", %2$s]", getName(), _nfi.format(getValue()));
    }

    /**
     * Gets an XML representation of this object
     */
    public final String getXML() {
        return String.format(Locale.US, "<CS_ProjectionParameter Name=\"%1$s\" Value=\"%2$s\"/>", getName(), _nfi.format(getValue()));
    }
}