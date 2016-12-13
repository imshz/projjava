package no.shz.projjava.coordinateSystems;

import java.util.ArrayList;
import java.util.Locale;

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
 * A 3D coordinate system, with its origin at the center of the Earth.
 */
public class GeocentricCoordinateSystem extends CoordinateSystem implements IGeocentricCoordinateSystem {
    private IHorizontalDatum _horizontalDatum;
    private ILinearUnit _linearUnit;
    private IPrimeMeridian _primemeridan;

    public GeocentricCoordinateSystem(IHorizontalDatum datum, ILinearUnit linearUnit, IPrimeMeridian primeMeridian, ArrayList<AxisInfo> axisinfo, String name, String authority, long code, String alias, String remarks, String abbreviation) {
        super(name, authority, code, alias, abbreviation, remarks);

        _horizontalDatum = datum;
        _linearUnit = linearUnit;
        _primemeridan = primeMeridian;

        if (axisinfo.size() != 3) {
            throw new IllegalArgumentException("Axis info should contain three axes for geocentric coordinate systems");
        }
        super.setAxisInfo(axisinfo);
    }

    /**
     * Creates a geocentric coordinate system based on the WGS84 ellipsoid, suitable for GPS measurements
     */
    public static IGeocentricCoordinateSystem getWGS84() {
        return (new CoordinateSystemFactory()).createGeocentricCoordinateSystem("WGS84 Geocentric", HorizontalDatum.getWGS84(), LinearUnit.getMetre(), PrimeMeridian.getGreenwich());
    }

    /**
     * Returns the HorizontalDatum. The horizontal datum is used to determine where
     * the centre of the Earth is considered to be. All coordinate points will be
     * measured from the centre of the Earth, and not the surface.
     */
    public final IHorizontalDatum getHorizontalDatum() {
        return _horizontalDatum;
    }

    public final void setHorizontalDatum(IHorizontalDatum value) {
        _horizontalDatum = value;
    }

    /**
     * Gets the units used along all the axes.
     */
    public final ILinearUnit getLinearUnit() {
        return _linearUnit;
    }

    public final void setLinearUnit(ILinearUnit value) {
        _linearUnit = value;
    }

    /**
     * Gets units for dimension within coordinate system. Each dimension in
     * the coordinate system has corresponding units.
     *
     * @param dimension Dimension
     * @return Unit
     */
    @Override
    public IUnit getUnits(int dimension) {
        return _linearUnit;
    }

    /**
     * Returns the PrimeMeridian.
     */
    public final IPrimeMeridian getPrimeMeridian() {
        return _primemeridan;
    }

    public final void setPrimeMeridian(IPrimeMeridian value) {
        _primemeridan = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("GEOCCS[\"%1$s\", %2$s, %3$s, %4$s", getName(), getHorizontalDatum().getWKT(), getPrimeMeridian().getWKT(), getLinearUnit().getWKT()));
        //Skip axis info if they contain default values
        if (getAxisInfo().size() != 3 || !getAxisInfo().get(0).getName().equals("X") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.Other || !getAxisInfo().get(1).getName().equals("Y") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(2).getName().equals("Z") || getAxisInfo().get(2).getOrientation() != AxisOrientationEnum.North) {
            for (int i = 0; i < getAxisInfo().size(); i++) {
                sb.append(String.format(", %1$s", getAxis(i).getWKT()));
            }
        }
        if (!StringUtility.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0) {
            sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    @Override
    public String getXML() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "<CS_CoordinateSystem Dimension=\"%1$s\"><CS_GeocentricCoordinateSystem>%2$s", _nfi.format(this.getDimension()), getInfoXml()));
        for (AxisInfo ai : this.getAxisInfo()) {
            sb.append(ai.getXML());
        }
        sb.append(String.format("%1$s%2$s%3$s</CS_GeocentricCoordinateSystem></CS_CoordinateSystem>", getHorizontalDatum().getXML(), getLinearUnit().getXML(), getPrimeMeridian().getXML()));
        return sb.toString();
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
        if (!(obj instanceof GeocentricCoordinateSystem)) {
            return false;
        }
        GeocentricCoordinateSystem gcc = (GeocentricCoordinateSystem) ((obj instanceof GeocentricCoordinateSystem) ? obj : null);
        return gcc.getHorizontalDatum().equalParams(this.getHorizontalDatum()) && gcc.getLinearUnit().equalParams(this.getLinearUnit()) && gcc.getPrimeMeridian().equalParams(this.getPrimeMeridian());
    }
}