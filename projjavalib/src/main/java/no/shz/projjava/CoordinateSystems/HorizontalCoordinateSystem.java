package no.shz.projjava.coordinateSystems;

import java.util.ArrayList;

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
 * A 2D coordinate system suitable for positions on the Earth's surface.
 */
public abstract class HorizontalCoordinateSystem extends CoordinateSystem implements IHorizontalCoordinateSystem {
    private IHorizontalDatum _horizontalDatum;

    /**
     * Creates an instance of HorizontalCoordinateSystem
     *
     * @param datum        Horizontal datum
     * @param axisInfo     Axis information
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param abbreviation Abbreviation
     * @param remarks      Provider-supplied remarks
     */
    public HorizontalCoordinateSystem(IHorizontalDatum datum, ArrayList<AxisInfo> axisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation) {
        super(name, authority, code, alias, abbreviation, remarks);
        _horizontalDatum = datum;
        if (axisInfo.size() != 2) {
            throw new IllegalArgumentException("Axis info should contain two axes for horizontal coordinate systems");
        }
        super.setAxisInfo(axisInfo);
    }

    /**
     * Gets or sets the HorizontalDatum.
     */
    public final IHorizontalDatum getHorizontalDatum() {
        return _horizontalDatum;
    }

    public final void setHorizontalDatum(IHorizontalDatum value) {
        _horizontalDatum = value;
    }
}