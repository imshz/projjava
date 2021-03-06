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
 * A set of quantities from which other quantities are calculated.
 * <p>
 * For the OGC abstract model, it can be defined as a set of real points on the earth
 * that have coordinates. EG. A datum can be thought of as a set of parameters
 * defining completely the origin and orientation of a coordinate system with respect
 * to the earth. A textual description and/or a set of parameters describing the
 * relationship of a coordinate system to some predefined physical locations (such
 * as center of mass) and physical directions (such as axis of spin). The definition
 * of the datum may also include the temporal behavior (such as the rate of change of
 * the orientation of the coordinate axes).
 */
public abstract class Datum extends Info implements IDatum {
    private DatumType _DatumType = DatumType.values()[0];

    /**
     * Initializes a new instance of a Datum object
     *
     * @param type         Datum type
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param abbreviation Abbreviation
     * @param remarks      Provider-supplied remarks
     */
    public Datum(DatumType type, String name, String authority, long code, String alias, String remarks, String abbreviation) {
        super(name, authority, code, alias, abbreviation, remarks);
        _DatumType = type;
    }

    /**
     * Gets or sets the type of the datum as an enumerated code.
     */
    public final DatumType getDatumType() {
        return _DatumType;
    }

    public final void setDatumType(DatumType value) {
        _DatumType = value;
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    @Override
    public boolean equalParams(Object obj) {
        if (!(obj instanceof Ellipsoid)) {
            return false;
        }
        return ((Datum) ((obj instanceof Datum) ? obj : null)).getDatumType() == this.getDatumType();
    }
}