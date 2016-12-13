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
 * A coordinate system based on latitude and longitude.
 * <p>
 * Some geographic coordinate systems are Lat/Lon, and some are Lon/Lat.
 * You can find out which this is by examining the axes. You should also
 * check the angular units, since not all geographic coordinate systems
 * use degrees.
 */
public class GeographicCoordinateSystem extends HorizontalCoordinateSystem implements IGeographicCoordinateSystem {

    private IAngularUnit _angularUnit;
    private IPrimeMeridian _primeMeridian;
    private ArrayList<Wgs84ConversionInfo> _wgs84ConversionInfo;

    /**
     * Creates an instance of a Geographic Coordinate System
     *
     * @param angularUnit     Angular units
     * @param horizontalDatum Horizontal datum
     * @param primeMeridian   Prime meridian
     * @param axisInfo        Axis info
     * @param name            Name
     * @param authority       Authority name
     * @param authorityCode   Authority-specific identification code.
     * @param alias           Alias
     * @param abbreviation    Abbreviation
     * @param remarks         Provider-supplied remarks
     */
    public GeographicCoordinateSystem(IAngularUnit angularUnit, IHorizontalDatum horizontalDatum, IPrimeMeridian primeMeridian, ArrayList<AxisInfo> axisInfo, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) {
        super(horizontalDatum, axisInfo, name, authority, authorityCode, alias, abbreviation, remarks);

        _angularUnit = angularUnit;
        _primeMeridian = primeMeridian;
    }

    /**
     * Creates a decimal degrees geographic coordinate system based on the WGS84 ellipsoid, suitable for GPS measurements
     */
    public static GeographicCoordinateSystem getWGS84() {
        ArrayList<AxisInfo> axes = new ArrayList<>(2);

        axes.add(new AxisInfo("Lon", AxisOrientationEnum.East));
        axes.add(new AxisInfo("Lat", AxisOrientationEnum.North));

        return new GeographicCoordinateSystem(AngularUnit.getDegrees(), HorizontalDatum.getWGS84(), PrimeMeridian.getGreenwich(), axes, "WGS 84", "EPSG", 4326, "", "", "");
    }

    /**
     * Gets or sets the angular units of the geographic coordinate system.
     */
    public final IAngularUnit getAngularUnit() {
        return _angularUnit;
    }

    public final void setAngularUnit(IAngularUnit value) {
        _angularUnit = value;
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
        return _angularUnit;
    }

    /**
     * Gets or sets the prime meridian of the geographic coordinate system.
     */
    public final IPrimeMeridian getPrimeMeridian() {
        return _primeMeridian;
    }

    public final void setPrimeMeridian(IPrimeMeridian value) {
        _primeMeridian = value;
    }

    /**
     * Gets the number of available conversions to WGS84 coordinates.
     */
    public final int getNumConversionToWGS84() {
        return _wgs84ConversionInfo.size();
    }

    public final ArrayList<Wgs84ConversionInfo> getWGS84ConversionInfo() {
        return _wgs84ConversionInfo;
    }

    public final void setWGS84ConversionInfo(ArrayList<Wgs84ConversionInfo> value) {
        _wgs84ConversionInfo = value;
    }

    /**
     * Gets details on a conversion to WGS84.
     */
    public final Wgs84ConversionInfo getWgs84ConversionInfo(int index) {
        return _wgs84ConversionInfo.get(index);
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("GEOGCS[\"%1$s\", %2$s, %3$s, %4$s", getName(), getHorizontalDatum().getWKT(), getPrimeMeridian().getWKT(), getAngularUnit().getWKT()));

        if (getAxisInfo().size() != 2 || !getAxisInfo().get(0).getName().equals("Lon") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(1).getName().equals("Lat") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.North) {
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
        sb.append(String.format(Locale.US, "<CS_CoordinateSystem Dimension=\"%1$s\"><CS_GeographicCoordinateSystem>%2$s", this.getDimension(), getInfoXml()));
        for (AxisInfo ai : this.getAxisInfo()) {
            sb.append(ai.getXML());
        }
        sb.append(String.format("%1$s%2$s%3$s</CS_GeographicCoordinateSystem></CS_CoordinateSystem>", getHorizontalDatum().getXML(), getAngularUnit().getXML(), getPrimeMeridian().getXML()));
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
        if (!(obj instanceof GeographicCoordinateSystem)) {
            return false;
        }
        GeographicCoordinateSystem gcs = (GeographicCoordinateSystem) ((obj instanceof GeographicCoordinateSystem) ? obj : null);
        if (gcs.getDimension() != this.getDimension()) {
            return false;
        }
        if (this.getWGS84ConversionInfo() != null && gcs.getWGS84ConversionInfo() == null) {
            return false;
        }
        if (this.getWGS84ConversionInfo() == null && gcs.getWGS84ConversionInfo() != null) {
            return false;
        }
        if (this.getWGS84ConversionInfo() != null && gcs.getWGS84ConversionInfo() != null) {
            if (this.getWGS84ConversionInfo().size() != gcs.getWGS84ConversionInfo().size()) {
                return false;
            }
            for (int i = 0; i < this.getWGS84ConversionInfo().size(); i++) {
                if (!gcs.getWGS84ConversionInfo().get(i).equals(this.getWGS84ConversionInfo().get(i))) {
                    return false;
                }
            }
        }
        if (this.getAxisInfo().size() != gcs.getAxisInfo().size()) {
            return false;
        }
        for (int i = 0; i < gcs.getAxisInfo().size(); i++) {
            if (gcs.getAxisInfo().get(i).getOrientation() != this.getAxisInfo().get(i).getOrientation()) {
                return false;
            }
        }
        return gcs.getAngularUnit().equalParams(this.getAngularUnit()) && gcs.getHorizontalDatum().equalParams(this.getHorizontalDatum()) && gcs.getPrimeMeridian().equalParams(this.getPrimeMeridian());
    }
}