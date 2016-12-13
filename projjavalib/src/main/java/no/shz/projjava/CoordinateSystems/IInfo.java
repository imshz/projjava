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
 * The ISpatialReferenceInfo interface defines the standard
 * information stored with spatial reference objects. This
 * interface is reused for many of the spatial reference
 * objects in the system.
 */
public interface IInfo {
    /**
     * Gets or sets the name of the object.
     */
    String getName();

    /**
     * Gets or sets the authority name for this object, e.g., �POSC�,
     * is this is a standard object with an authority specific
     * getIdentity code. Returns �CUSTOM� if this is a custom object.
     */
    String getAuthority();

    /**
     * Gets or sets the authority specific identification code of the object
     */
    long getAuthorityCode();

    /**
     * Gets or sets the alias of the object.
     */
    String getAlias();

    /**
     * Gets or sets the abbreviation of the object.
     */
    String getAbbreviation();

    /**
     * Gets or sets the provider-supplied remarks for the object.
     */
    String getRemarks();

    /**
     * Returns the Well-known text for this spatial reference object
     * as defined in the simple features specification.
     */
    String getWKT();

    /**
     * Gets an XML representation of this object.
     */
    String getXML();

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    boolean equalParams(Object obj);
}