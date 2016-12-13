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
 * Creates spatial reference objects using codes.
 * The codes are maintained by an external authority. A commonly used authority is EPSG, which is also used in the GeoTIFF standard and in ProjNet.
 */
public interface ICoordinateSystemAuthorityFactory {
    /**
     * Returns the authority name for this factory (e.g., "EPSG" or "POSC").
     */
    String getAuthority();

    /**
     * Returns a projected coordinate system object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The projected coordinate system object with the given code.
     */
    IProjectedCoordinateSystem createProjectedCoordinateSystem(long code);

    /**
     * Returns a geographic coordinate system object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The geographic coordinate system object with the given code.
     */
    IGeographicCoordinateSystem createGeographicCoordinateSystem(long code);

    /**
     * Returns a horizontal datum object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The horizontal datum object with the given code.
     */
    IHorizontalDatum createHorizontalDatum(long code);

    /**
     * Returns an ellipsoid object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The ellipsoid object with the given code.
     */
    IEllipsoid createEllipsoid(long code);

    /**
     * Returns a prime meridian object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The prime meridian object with the given code.
     */
    IPrimeMeridian createPrimeMeridian(long code);

    /**
     * Returns a linear unit object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The linear unit object with the given code.
     */
    ILinearUnit createLinearUnit(long code);

    /**
     * Returns an <see cref="IAngularUnit">angular unit</see> object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The angular unit object for the given code.
     */
    IAngularUnit createAngularUnit(long code);

    /**
     * Creates a <see cref="IVerticalDatum"/> from a code.
     *
     * @param code Authority code
     * @return Vertical datum for the given code
     */
    IVerticalDatum createVerticalDatum(long code);

    /**
     * Create a <see cref="IVerticalCoordinateSystem">vertical coordinate system</see> from a code.
     *
     * @param code Authority code
     * @return
     */
    IVerticalCoordinateSystem createVerticalCoordinateSystem(long code);

    /**
     * Creates a 3D coordinate system from a code.
     *
     * @param code Authority code
     * @return Compound coordinate system for the given code
     */
    ICompoundCoordinateSystem createCompoundCoordinateSystem(long code);

    /**
     * Creates a <see cref="IHorizontalCoordinateSystem">horizontal co-ordinate system</see> from a code.
     * The horizontal coordinate system could be geographic or projected.
     *
     * @param code Authority code
     * @return Horizontal coordinate system for the given code
     */
    IHorizontalCoordinateSystem createHorizontalCoordinateSystem(long code);

    /**
     * Gets a description of the object corresponding to a code.
     */
    String getDescriptionText();

    /**
     * Gets the Geoid code from a WKT name.
     * <p>
     * <p>
     * In the OGC definition of WKT horizontal datums, the geoid is referenced
     * by a quoted string, which is used as a key value. This method converts
     * the key value string into a code recognized by this authority.
     *
     * @param wkt
     * @return
     */
    String geoidFromWktName(String wkt);

    /**
     * Gets the WKT name of a Geoid.
     * <p>
     * <p>
     * In the OGC definition of WKT horizontal datums, the geoid is referenced by
     * a quoted string, which is used as a key value. This method gets the OGC WKT
     * key value from a geoid code.
     *
     * @param geoid
     * @return
     */
    String wktGeoidName(String geoid);
}