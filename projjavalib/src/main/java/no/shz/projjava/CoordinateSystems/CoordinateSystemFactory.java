package no.shz.projjava.coordinateSystems;

import java.io.IOException;
import java.util.ArrayList;

import no.shz.projjava.converters.wellKnownText.CoordinateSystemWktReader;
import no.shz.utilities.StringUtility;

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
public class CoordinateSystemFactory implements ICoordinateSystemFactory {
    /**
     * Creates a coordinate system object from an XML string.
     *
     * @param xml XML representation for the spatial reference
     * @return The resulting spatial reference object
     */
    public final ICoordinateSystem createFromXml(String xml) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a spatial reference object given its Well-known text representation.
     * The output object may be either a <see cref="IGeographicCoordinateSystem"/> or
     * a <see cref="IProjectedCoordinateSystem"/>.
     *
     * @param WKT The Well-known text representation for the spatial reference
     * @return The resulting spatial reference object
     */
    public final ICoordinateSystem createFromWkt(String WKT) throws IOException {
        IInfo tempVar = CoordinateSystemWktReader.parse(WKT);
        return (ICoordinateSystem) ((tempVar instanceof ICoordinateSystem) ? tempVar : null);
    }

    /**
     * Creates a <see cref="ICompoundCoordinateSystem"/> [NOT IMPLEMENTED].
     *
     * @param name Name of compound coordinate system.
     * @param head Head coordinate system
     * @param tail Tail coordinate system
     * @return Compound coordinate system
     */
    public final ICompoundCoordinateSystem createCompoundCoordinateSystem(String name, ICoordinateSystem head, ICoordinateSystem tail) {
        throw new UnsupportedOperationException();
    }

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
    public final IFittedCoordinateSystem createFittedCoordinateSystem(String name, ICoordinateSystem baseCoordinateSystem, String toBaseWkt, ArrayList<AxisInfo> arAxes) {
        throw new UnsupportedOperationException();
    }

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
    public final ILocalCoordinateSystem createLocalCoordinateSystem(String name, ILocalDatum datum, IUnit unit, ArrayList<AxisInfo> axes) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an <see cref="Ellipsoid"/> from radius values.
     *
     * @param name          Name of ellipsoid
     * @param semiMajorAxis
     * @param semiMinorAxis
     * @param linearUnit
     * @return Ellipsoid
     */
    public final IEllipsoid createEllipsoid(String name, double semiMajorAxis, double semiMinorAxis, ILinearUnit linearUnit) {
        double ivf = 0;
        if (semiMajorAxis != semiMinorAxis) {
            ivf = semiMajorAxis / (semiMajorAxis - semiMinorAxis);
        }
        return new Ellipsoid(semiMajorAxis, semiMinorAxis, ivf, false, linearUnit, name, "", -1, "", "", "");
    }

    /**
     * Creates an <see cref="Ellipsoid"/> from an major radius, and inverse flattening.
     *
     * @param name              Name of ellipsoid
     * @param semiMajorAxis     Semi major-axis
     * @param inverseFlattening inverse flattening
     * @param linearUnit        Linear unit
     * @return Ellipsoid
     */
    public final IEllipsoid createFlattenedSphere(String name, double semiMajorAxis, double inverseFlattening, ILinearUnit linearUnit) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }

        return new Ellipsoid(semiMajorAxis, -1, inverseFlattening, true, linearUnit, name, "", -1, "", "", "");
    }

    /**
     * Creates a <see cref="ProjectedCoordinateSystem"/> using a projection object.
     *
     * @param name       Name of projected coordinate system
     * @param gcs        Geographic coordinate system
     * @param projection Projection
     * @param linearUnit Linear unit
     * @param axis0      Primary axis
     * @param axis1      Secondary axis
     * @return Projected coordinate system
     */
    public final IProjectedCoordinateSystem createProjectedCoordinateSystem(String name, IGeographicCoordinateSystem gcs, IProjection projection, ILinearUnit linearUnit, AxisInfo axis0, AxisInfo axis1) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (gcs == null) {
            throw new IllegalArgumentException("Geographic coordinate system was null");
        }
        if (projection == null) {
            throw new IllegalArgumentException("Projection was null");
        }
        if (linearUnit == null) {
            throw new IllegalArgumentException("Linear unit was null");
        }
        ArrayList<AxisInfo> info = new ArrayList<AxisInfo>(2);
        info.add(axis0);
        info.add(axis1);
        return new ProjectedCoordinateSystem(null, gcs, linearUnit, projection, info, name, "", -1, "", "", "");
    }

    /**
     * Creates a <see cref="Projection"/>.
     *
     * @param name               Name of projection
     * @param wktProjectionClass Projection class
     * @param parameters         Projection parameters
     * @return Projection
     */
    public final IProjection createProjection(String name, String wktProjectionClass, ArrayList<ProjectionParameter> parameters) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (parameters == null || parameters.isEmpty()) {
            throw new IllegalArgumentException("Invalid projection parameters");
        }
        return new Projection(wktProjectionClass, parameters, name, "", -1, "", "", "");
    }

    /**
     * Creates <see cref="HorizontalDatum"/> from ellipsoid and Bursa-World parameters.
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
    public final IHorizontalDatum createHorizontalDatum(String name, DatumType datumType, IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (ellipsoid == null) {
            throw new IllegalArgumentException("Ellipsoid was null");
        }

        return new HorizontalDatum(ellipsoid, toWgs84, datumType, name, "", -1, "", "", "");
    }

    /**
     * Creates a <see cref="PrimeMeridian"/>, relative to Greenwich.
     *
     * @param name        Name of prime meridian
     * @param angularUnit Angular unit
     * @param longitude   Longitude
     * @return Prime meridian
     */
    public final IPrimeMeridian createPrimeMeridian(String name, IAngularUnit angularUnit, double longitude) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        return new PrimeMeridian(longitude, angularUnit, name, "", -1, "", "", "");
    }

    /**
     * Creates a <see cref="GeographicCoordinateSystem"/>, which could be Lat/Lon or Lon/Lat.
     *
     * @param name          Name of geographical coordinate system
     * @param angularUnit   Angular units
     * @param datum         Horizontal datum
     * @param primeMeridian Prime meridian
     * @param axis0         First axis
     * @param axis1         Second axis
     * @return Geographic coordinate system
     */
    public final IGeographicCoordinateSystem createGeographicCoordinateSystem(String name, IAngularUnit angularUnit, IHorizontalDatum datum, IPrimeMeridian primeMeridian, AxisInfo axis0, AxisInfo axis1) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        ArrayList<AxisInfo> info = new ArrayList<AxisInfo>(2);
        info.add(axis0);
        info.add(axis1);
        return new GeographicCoordinateSystem(angularUnit, datum, primeMeridian, info, name, "", -1, "", "", "");
    }

    /**
     * Creates a <see cref="ILocalDatum"/>.
     *
     * @param name      Name of datum
     * @param datumType Datum type
     * @return
     */
    public final ILocalDatum createLocalDatum(String name, DatumType datumType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a <see cref="IVerticalDatum"/> from an enumerated type value.
     *
     * @param name      Name of datum
     * @param datumType Type of datum
     * @return Vertical datum
     */
    public final IVerticalDatum createVerticalDatum(String name, DatumType datumType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a <see cref="IVerticalCoordinateSystem"/> from a <see cref="IVerticalDatum">datum</see> and <see cref="LinearUnit">linear units</see>.
     *
     * @param name         Name of vertical coordinate system
     * @param datum        Vertical datum
     * @param verticalUnit Unit
     * @param axis         Axis info
     * @return Vertical coordinate system
     */
    public final IVerticalCoordinateSystem createVerticalCoordinateSystem(String name, IVerticalDatum datum, ILinearUnit verticalUnit, AxisInfo axis) {
        throw new UnsupportedOperationException();
    }


    /**
     * Creates a <see cref="createGeocentricCoordinateSystem"/> from a <see cref="IHorizontalDatum">datum</see>,
     * <see cref="ILinearUnit">linear unit</see> and <see cref="IPrimeMeridian"/>.
     *
     * @param name          Name of geocentric coordinate system
     * @param datum         Horizontal datum
     * @param linearUnit    Linear unit
     * @param primeMeridian Prime meridian
     * @return Geocentric Coordinate System
     */
    public final IGeocentricCoordinateSystem createGeocentricCoordinateSystem(String name, IHorizontalDatum datum, ILinearUnit linearUnit, IPrimeMeridian primeMeridian) {
        if (StringUtility.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        ArrayList<AxisInfo> info = new ArrayList<AxisInfo>(3);

        info.add(new AxisInfo("X", AxisOrientationEnum.Other));
        info.add(new AxisInfo("Y", AxisOrientationEnum.Other));
        info.add(new AxisInfo("Z", AxisOrientationEnum.Other));
        return new GeocentricCoordinateSystem(datum, linearUnit, primeMeridian, info, name, "", -1, "", "", "");
    }
}