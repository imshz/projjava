package no.shz.projjava.coordinateSystems;

import java.io.IOException;
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
 * Builds up complex objects from simpler objects or values.
 * <p>
 * <p>ICoordinateSystemFactory allows applications to make coordinate systems that
 * cannot be created by a <see cref="ICoordinateSystemAuthorityFactory"/>. This factory is very
 * flexible, whereas the authority factory is easier to use.</p>
 * <p>So <see cref="ICoordinateSystemAuthorityFactory"/>can be used to make 'standard' coordinate
 * systems, and <see cref="CoordinateSystemFactory"/> can be used to make 'special'
 * coordinate systems.</p>
 * <p>For example, the EPSG authority has codes for USA state plane coordinate systems
 * using the NAD83 datum, but these coordinate systems always use meters. EPSG does not
 * have codes for NAD83 state plane coordinate systems that use feet units. This factory
 * lets an application create such a hybrid coordinate system.</p>
 */
public interface ICoordinateSystemFactory {
    /**
     * Creates a <see cref="ICompoundCoordinateSystem"/>.
     *
     * @param name Name of compound coordinate system.
     * @param head Head coordinate system
     * @param tail Tail coordinate system
     * @return Compound coordinate system
     */
    ICompoundCoordinateSystem createCompoundCoordinateSystem(String name, ICoordinateSystem head, ICoordinateSystem tail);

    /**
     * Creates an <see cref="IEllipsoid"/> from radius values.
     *
     * @param name          Name of ellipsoid
     * @param semiMajorAxis
     * @param semiMinorAxis
     * @param linearUnit
     * @return Ellipsoid
     */
    IEllipsoid createEllipsoid(String name, double semiMajorAxis, double semiMinorAxis, ILinearUnit linearUnit);

    /**
     * Creates a <see cref="IFittedCoordinateSystem"/>.
     * <p>
     * The units of the axes in the fitted coordinate system will be
     * inferred from the units of the base coordinate system. If the affine map
     * performs a rotation, then any mixed axes must have identical units. For
     * example, a (lat_deg,lon_deg,height_feet) system can be rotated in the
     * (lat,lon) plane, since both affected axes are in degrees. But you
     * should not rotate this coordinate system in any other plane.
     *
     * @param name                 Name of coordinate system
     * @param baseCoordinateSystem Base coordinate system
     * @param toBaseWkt
     * @param arAxes
     * @return Fitted coordinate system
     */
    IFittedCoordinateSystem createFittedCoordinateSystem(String name, ICoordinateSystem baseCoordinateSystem, String toBaseWkt, ArrayList<AxisInfo> arAxes);

    /**
     * Creates an <see cref="IEllipsoid"/> from an major radius, and inverse flattening.
     *
     * @param name              Name of ellipsoid
     * @param semiMajorAxis     Semi major-axis
     * @param inverseFlattening inverse flattening
     * @param linearUnit        Linear unit
     * @return Ellipsoid
     */
    IEllipsoid createFlattenedSphere(String name, double semiMajorAxis, double inverseFlattening, ILinearUnit linearUnit);


    /**
     * Creates a coordinate system object from an XML string.
     *
     * @param xml XML representation for the spatial reference
     * @return The resulting spatial reference object
     */
    ICoordinateSystem createFromXml(String xml);

    /**
     * Creates a spatial reference object given its Well-known text representation.
     * The output object may be either a <see cref="IGeographicCoordinateSystem"/> or
     * a <see cref="IProjectedCoordinateSystem"/>.
     *
     * @param WKT The Well-known text representation for the spatial reference
     * @return The resulting spatial reference object
     */
    ICoordinateSystem createFromWkt(String WKT) throws IOException;

    /**
     * Creates a <see cref="IGeographicCoordinateSystem"/>, which could be Lat/Lon or Lon/Lat.
     *
     * @param name          Name of geographical coordinate system
     * @param angularUnit   Angular units
     * @param datum         Horizontal datum
     * @param primeMeridian Prime meridian
     * @param axis0         First axis
     * @param axis1         Second axis
     * @return Geographic coordinate system
     */
    IGeographicCoordinateSystem createGeographicCoordinateSystem(String name, IAngularUnit angularUnit, IHorizontalDatum datum, IPrimeMeridian primeMeridian, AxisInfo axis0, AxisInfo axis1);

    /**
     * Creates <see cref="IHorizontalDatum"/> from ellipsoid and Bursa-World parameters.
     * <p>
     * <p>
     * Since this method contains a set of Bursa-Wolf parameters, the created
     * datum will always have a relationship to WGS84. If you wish to create a
     * horizontal datum that has no relationship with WGS84, then you can
     * either specify a <see cref="DatumType">horizontalDatumType</see> of <see cref="DatumType.HD_Other"/>, or create it via WKT.
     *
     * @param name      Name of ellipsoid
     * @param datumType Type of datum
     * @param ellipsoid Ellipsoid
     * @param toWgs84   Wgs84 conversion parameters
     * @return Horizontal datum
     */
    IHorizontalDatum createHorizontalDatum(String name, DatumType datumType, IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84);

    /**
     * Creates a <see cref="ILocalCoordinateSystem">local coordinate system</see>.
     * <p>
     * <p>
     * The dimension of the local coordinate system is determined by the size of
     * the axis array. All the axes will have the same units. If you want to make
     * a coordinate system with mixed units, then you can make a compound
     * coordinate system from different local coordinate systems.
     *
     * @param name  Name of local coordinate system
     * @param datum Local datum
     * @param unit  Units
     * @param axes  Axis info
     * @return Local coordinate system
     */
    ILocalCoordinateSystem createLocalCoordinateSystem(String name, ILocalDatum datum, IUnit unit, ArrayList<AxisInfo> axes);

    /**
     * Creates a <see cref="ILocalDatum"/>.
     *
     * @param name      Name of datum
     * @param datumType Datum type
     * @return
     */
    ILocalDatum createLocalDatum(String name, DatumType datumType);

    /**
     * Creates a <see cref="IPrimeMeridian"/>, relative to Greenwich.
     *
     * @param name        Name of prime meridian
     * @param angularUnit Angular unit
     * @param longitude   Longitude
     * @return Prime meridian
     */
    IPrimeMeridian createPrimeMeridian(String name, IAngularUnit angularUnit, double longitude);

    /**
     * Creates a <see cref="IProjectedCoordinateSystem"/> using a projection object.
     *
     * @param name       Name of projected coordinate system
     * @param gcs        Geographic coordinate system
     * @param projection Projection
     * @param linearUnit Linear unit
     * @param axis0      Primary axis
     * @param axis1      Secondary axis
     * @return Projected coordinate system
     */
    IProjectedCoordinateSystem createProjectedCoordinateSystem(String name, IGeographicCoordinateSystem gcs, IProjection projection, ILinearUnit linearUnit, AxisInfo axis0, AxisInfo axis1);

    /**
     * Creates a <see cref="IProjection"/>.
     *
     * @param name               Name of projection
     * @param wktProjectionClass Projection class
     * @param Parameters         Projection parameters
     * @return Projection
     */
    IProjection createProjection(String name, String wktProjectionClass, ArrayList<ProjectionParameter> Parameters);

    /**
     * Creates a <see cref="IVerticalCoordinateSystem"/> from a <see cref="IVerticalDatum">datum</see> and <see cref="ILinearUnit">linear units</see>.
     *
     * @param name         Name of vertical coordinate system
     * @param datum        Vertical datum
     * @param verticalUnit Unit
     * @param axis         Axis info
     * @return Vertical coordinate system
     */
    IVerticalCoordinateSystem createVerticalCoordinateSystem(String name, IVerticalDatum datum, ILinearUnit verticalUnit, AxisInfo axis);

    /**
     * Creates a <see cref="IVerticalDatum"/> from an enumerated type value.
     *
     * @param name      Name of datum
     * @param datumType Type of datum
     * @return Vertical datum
     */
    IVerticalDatum createVerticalDatum(String name, DatumType datumType);
}