package no.shz.projjava.converters.wellKnownText;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

import no.shz.projjava.coordinateSystems.AngularUnit;
import no.shz.projjava.coordinateSystems.AxisInfo;
import no.shz.projjava.coordinateSystems.AxisOrientationEnum;
import no.shz.projjava.coordinateSystems.DatumType;
import no.shz.projjava.coordinateSystems.Ellipsoid;
import no.shz.projjava.coordinateSystems.GeographicCoordinateSystem;
import no.shz.projjava.coordinateSystems.HorizontalDatum;
import no.shz.projjava.coordinateSystems.IAngularUnit;
import no.shz.projjava.coordinateSystems.ICoordinateSystem;
import no.shz.projjava.coordinateSystems.IEllipsoid;
import no.shz.projjava.coordinateSystems.IGeographicCoordinateSystem;
import no.shz.projjava.coordinateSystems.IHorizontalDatum;
import no.shz.projjava.coordinateSystems.IInfo;
import no.shz.projjava.coordinateSystems.ILinearUnit;
import no.shz.projjava.coordinateSystems.IPrimeMeridian;
import no.shz.projjava.coordinateSystems.IProjectedCoordinateSystem;
import no.shz.projjava.coordinateSystems.IProjection;
import no.shz.projjava.coordinateSystems.IUnit;
import no.shz.projjava.coordinateSystems.LinearUnit;
import no.shz.projjava.coordinateSystems.PrimeMeridian;
import no.shz.projjava.coordinateSystems.ProjectedCoordinateSystem;
import no.shz.projjava.coordinateSystems.Projection;
import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.Unit;
import no.shz.projjava.coordinateSystems.Wgs84ConversionInfo;
import no.shz.utilities.Ref;
import no.shz.utilities.TextReader;

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
 * SOURCECODE IS MODIFIED FROM ANOTHER WORK AND IS ORIGINALLY BASED ON GeoTools.NET:
 *
 *  Copyright (C) 2002 Urban Science Applications, Inc. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
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
 * Creates an object based on the supplied Well Known Text (WKT).
 */
public class CoordinateSystemWktReader {
    /**
     * Reads and parses a WKT-formatted projection string.
     *
     * @param wkt String containing WKT.
     * @return Object representation of the WKT.
     * @throws IllegalArgumentException If a token is not recognised.
     */
    public static IInfo parse(String wkt) throws IOException {
        IInfo returnObject;
        StringReader stringReader = new StringReader(wkt);
        TextReader reader = new TextReader(stringReader);
        WktStreamTokenizer tokenizer = new WktStreamTokenizer(reader);
        tokenizer.nextToken();
        String objectName = tokenizer.getStringValue();
        switch (objectName) {
            case "UNIT":
                returnObject = readUnit(tokenizer);
                break;
            case "SPHEROID":
                returnObject = readEllipsoid(tokenizer);
                break;
            case "DATUM":
                returnObject = readHorizontalDatum(tokenizer);
                break;
            case "PRIMEM":
                returnObject = readPrimeMeridian(tokenizer);
                break;
            case "VERT_CS":
            case "GEOGCS":
            case "PROJCS":
            case "COMPD_CS":
            case "GEOCCS":
            case "FITTED_CS":
            case "LOCAL_CS":
                returnObject = readCoordinateSystem(wkt, tokenizer);
                break;
            default:
                throw new IllegalArgumentException(String.format("'%1$s' is not recognized.", objectName));

        }
        reader.close();
        stringReader.close();

        return returnObject;
    }

    /**
     * Returns a IUnit given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An object that implements the IUnit interface.
     */
    private static IUnit readUnit(WktStreamTokenizer tokenizer) throws IOException {
        tokenizer.readToken("[");
        String unitName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double unitsPerUnit = tokenizer.getNumericValue();
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            Ref<String> tempRef_authority = new Ref<>(authority);
            Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
            tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
            authorityCode = tempRef_authorityCode.value;
            authority = tempRef_authority.value;
            tokenizer.readToken("]");
        }
        return new Unit(unitsPerUnit, unitName, authority, authorityCode, "", "", "");
    }

    /**
     * Returns a <see cref="LinearUnit"/> given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An object that implements the IUnit interface.
     */
    private static ILinearUnit readLinearUnit(WktStreamTokenizer tokenizer) throws IOException {
        tokenizer.readToken("[");
        String unitName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double unitsPerUnit = tokenizer.getNumericValue();
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            Ref<String> tempRef_authority = new Ref<>(authority);
            Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
            tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
            authorityCode = tempRef_authorityCode.value;
            authority = tempRef_authority.value;
            tokenizer.readToken("]");
        }
        return new LinearUnit(unitsPerUnit, unitName, authority, authorityCode, "", "", "");
    }

    /**
     * Returns a <see cref="AngularUnit"/> given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An object that implements the IUnit interface.
     */
    private static IAngularUnit readAngularUnit(WktStreamTokenizer tokenizer) throws IOException {
        tokenizer.readToken("[");
        String unitName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double unitsPerUnit = tokenizer.getNumericValue();
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            Ref<String> tempRef_authority = new Ref<>(authority);
            Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
            tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
            authorityCode = tempRef_authorityCode.value;
            authority = tempRef_authority.value;
            tokenizer.readToken("]");
        }
        return new AngularUnit(unitsPerUnit, unitName, authority, authorityCode, "", "", "");
    }

    /**
     * Returns a <see cref="AxisInfo"/> given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An AxisInfo object.
     */
    private static AxisInfo readAxis(WktStreamTokenizer tokenizer) throws IOException {
        if (!tokenizer.getStringValue().equals("AXIS")) {
            tokenizer.readToken("AXIS");
        }
        tokenizer.readToken("[");
        String axisName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        String unitname = tokenizer.getStringValue();
        tokenizer.readToken("]");
        switch (unitname.toUpperCase(Locale.US)) {
            case "DOWN":
                return new AxisInfo(axisName, AxisOrientationEnum.Down);
            case "EAST":
                return new AxisInfo(axisName, AxisOrientationEnum.East);
            case "NORTH":
                return new AxisInfo(axisName, AxisOrientationEnum.North);
            case "OTHER":
                return new AxisInfo(axisName, AxisOrientationEnum.Other);
            case "SOUTH":
                return new AxisInfo(axisName, AxisOrientationEnum.South);
            case "UP":
                return new AxisInfo(axisName, AxisOrientationEnum.Up);
            case "WEST":
                return new AxisInfo(axisName, AxisOrientationEnum.West);
            default:
                throw new IllegalArgumentException("Invalid axis name '" + unitname + "' in WKT");
        }
    }

    private static ICoordinateSystem readCoordinateSystem(String coordinateSystem, WktStreamTokenizer tokenizer) throws IOException {
        switch (tokenizer.getStringValue()) {
            case "GEOGCS":
                return readGeographicCoordinateSystem(tokenizer);
            case "PROJCS":
                return readProjectedCoordinateSystem(tokenizer);
            case "COMPD_CS":
            case "VERT_CS":
            case "GEOCCS":
            case "FITTED_CS":
            case "LOCAL_CS":
                throw new UnsupportedOperationException(String.format("%1$s coordinate system is not supported.", coordinateSystem));
            default:
                throw new IllegalStateException(String.format("%1$s coordinate system is not recognized.", coordinateSystem));
        }
    }

    /**
     * Reads either 3, 6 or 7 parameter Bursa-Wolf values from TOWGS84 token
     *
     * @param tokenizer WktStreamTokenizer
     * @return Wgs84ConversionInfo
     */
    private static Wgs84ConversionInfo readWGS84ConversionInfo(WktStreamTokenizer tokenizer) throws IOException {

        tokenizer.readToken("[");
        Wgs84ConversionInfo info = new Wgs84ConversionInfo();
        tokenizer.nextToken();
        info.Dx = tokenizer.getNumericValue();
        tokenizer.readToken(",");

        tokenizer.nextToken();
        info.Dy = tokenizer.getNumericValue();
        tokenizer.readToken(",");

        tokenizer.nextToken();
        info.Dz = tokenizer.getNumericValue();
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            info.Ex = tokenizer.getNumericValue();

            tokenizer.readToken(",");
            tokenizer.nextToken();
            info.Ey = tokenizer.getNumericValue();

            tokenizer.readToken(",");
            tokenizer.nextToken();
            info.Ez = tokenizer.getNumericValue();

            tokenizer.nextToken();
            if (tokenizer.getStringValue().equals(",")) {
                tokenizer.nextToken();
                info.Ppm = tokenizer.getNumericValue();
            }
        }
        if (!tokenizer.getStringValue().equals("]")) {
            tokenizer.readToken("]");
        }
        return info;
    }

    /**
     * @param tokenizer WktStreamTokenizer
     * @return IEllipsoid
     */
    private static IEllipsoid readEllipsoid(WktStreamTokenizer tokenizer) throws IOException {
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();

        double majorAxis = tokenizer.getNumericValue();
        tokenizer.readToken(",");
        tokenizer.nextToken();

        double e = tokenizer.getNumericValue();
        tokenizer.nextToken();
        String authority = "";

        long authorityCode = -1;

        if (tokenizer.getStringValue().equals(","))
        {
            Ref<String> tempRef_authority = new Ref<>(authority);
            Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
            tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
            authorityCode = tempRef_authorityCode.value;
            authority = tempRef_authority.value;
            tokenizer.readToken("]");
        }
        return new Ellipsoid(majorAxis, 0.0, e, true, LinearUnit.getMetre(), name, authority, authorityCode, "", "", "");
    }

    /**
     * @param tokenizer WktStreamTokenizer
     * @return IProjection
     */
    private static IProjection readProjection(WktStreamTokenizer tokenizer) throws IOException {

        tokenizer.readToken("PROJECTION");
        tokenizer.readToken("[");
        String projectionName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken("]");
        tokenizer.readToken(",");
        tokenizer.readToken("PARAMETER");
        ArrayList<ProjectionParameter> paramList = new ArrayList<>();
        while (tokenizer.getStringValue().equals("PARAMETER")) {
            tokenizer.readToken("[");
            String paramName = tokenizer.readDoubleQuotedWord();
            tokenizer.readToken(",");
            tokenizer.nextToken();
            double paramValue = tokenizer.getNumericValue();
            tokenizer.readToken("]");
            tokenizer.readToken(",");
            paramList.add(new ProjectionParameter(paramName, paramValue));
            tokenizer.nextToken();
        }
        String authority = "";
        long authorityCode = -1;

        return new Projection(projectionName, paramList, projectionName, authority, authorityCode, "", "", "");
    }

    /**
     * @param tokenizer WktStreamTokenizer
     * @return IProjectedCoordinateSystem
     */
    private static IProjectedCoordinateSystem readProjectedCoordinateSystem(WktStreamTokenizer tokenizer) throws IOException {
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("GEOGCS");
        IGeographicCoordinateSystem geographicCS = readGeographicCoordinateSystem(tokenizer);
        tokenizer.readToken(",");
        IProjection projection = readProjection(tokenizer);
        IUnit unit = readLinearUnit(tokenizer);

        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        ArrayList<AxisInfo> axes = new ArrayList<>(2);
        if (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            while (tokenizer.getStringValue().equals("AXIS")) {
                axes.add(readAxis(tokenizer));
                tokenizer.nextToken();
                if (tokenizer.getStringValue().equals(",")) {
                    tokenizer.nextToken();
                }
            }
            if (tokenizer.getStringValue().equals(",")) {
                tokenizer.nextToken();
            }
            if (tokenizer.getStringValue().equals("AUTHORITY")) {
                Ref<String> tempRef_authority = new Ref<>(authority);
                Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
                tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
                authorityCode = tempRef_authorityCode.value;
                authority = tempRef_authority.value;
                tokenizer.readToken("]");
            }
        }

        if (axes.isEmpty()) {
            axes.add(new AxisInfo("X", AxisOrientationEnum.East));
            axes.add(new AxisInfo("Y", AxisOrientationEnum.North));
        }
        return new ProjectedCoordinateSystem(geographicCS.getHorizontalDatum(), geographicCS, ((unit instanceof LinearUnit) ? (LinearUnit) unit : null), projection, axes, name, authority, authorityCode, "", "", "");
    }

    /**
     * @param tokenizer WktStreamTokenizer
     * @return IGeographicCoordinateSystem
     */
    private static IGeographicCoordinateSystem readGeographicCoordinateSystem(WktStreamTokenizer tokenizer) throws IOException {
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("DATUM");
        IHorizontalDatum horizontalDatum = readHorizontalDatum(tokenizer);
        tokenizer.readToken(",");
        tokenizer.readToken("PRIMEM");
        IPrimeMeridian primeMeridian = readPrimeMeridian(tokenizer);
        tokenizer.readToken(",");
        tokenizer.readToken("UNIT");
        IAngularUnit angularUnit = readAngularUnit(tokenizer);

        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        ArrayList<AxisInfo> info = new ArrayList<>(2);
        if (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            while (tokenizer.getStringValue().equals("AXIS")) {
                info.add(readAxis(tokenizer));
                tokenizer.nextToken();
                if (tokenizer.getStringValue().equals(",")) {
                    tokenizer.nextToken();
                }
            }
            if (tokenizer.getStringValue().equals(",")) {
                tokenizer.nextToken();
            }
            if (tokenizer.getStringValue().equals("AUTHORITY")) {
                Ref<String> tempRef_authority;
                tempRef_authority = new Ref<>(authority);
                Ref<Long> tempRef_authorityCode;
                tempRef_authorityCode = new Ref<>(authorityCode);
                tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
                authorityCode = tempRef_authorityCode.value;
                authority = tempRef_authority.value;
                tokenizer.readToken("]");
            }
        }

        if (info.isEmpty()) {
            info.add(new AxisInfo("Lon", AxisOrientationEnum.East));
            info.add(new AxisInfo("Lat", AxisOrientationEnum.North));
        }

        return new GeographicCoordinateSystem(angularUnit, horizontalDatum, primeMeridian, info, name, authority, authorityCode, "", "", "");
    }

    /**
     * @param tokenizer WktStreamTokenizer
     * @return IHorizontalDatum
     */
    private static IHorizontalDatum readHorizontalDatum(WktStreamTokenizer tokenizer) throws IOException {
        Wgs84ConversionInfo wgsInfo = null;
        String authority = "";
        long authorityCode = -1;

        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("SPHEROID");
        IEllipsoid ellipsoid = readEllipsoid(tokenizer);
        tokenizer.nextToken();
        while (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            if (tokenizer.getStringValue().equals("TOWGS84")) {
                wgsInfo = readWGS84ConversionInfo(tokenizer);
                tokenizer.nextToken();
            } else if (tokenizer.getStringValue().equals("AUTHORITY")) {
                Ref<String> tempRef_authority = new Ref<>(authority);
                Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
                tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
                authorityCode = tempRef_authorityCode.value;
                authority = tempRef_authority.value;
                tokenizer.readToken("]");
            }
        }

        return new HorizontalDatum(ellipsoid, wgsInfo, DatumType.HD_Geocentric, name, authority, authorityCode, "", "", "");
    }

    /**
     * @param tokenizer WktStreamTokenizer
     * @return IPrimeMeridian
     */
    private static IPrimeMeridian readPrimeMeridian(WktStreamTokenizer tokenizer) throws IOException {

        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double longitude = tokenizer.getNumericValue();

        tokenizer.nextToken();
        String authority = "";
        long authorityCode = -1;
        if (tokenizer.getStringValue().equals(",")) {
            Ref<String> tempRef_authority;
            tempRef_authority = new Ref<>(authority);
            Ref<Long> tempRef_authorityCode = new Ref<>(authorityCode);
            tokenizer.readAuthority(tempRef_authority, tempRef_authorityCode);
            authorityCode = tempRef_authorityCode.value;
            authority = tempRef_authority.value;
            tokenizer.readToken("]");
        }

        return new PrimeMeridian(longitude, AngularUnit.getDegrees(), name, authority, authorityCode, "", "", "");
    }
}