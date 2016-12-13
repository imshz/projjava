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
 * A 2D cartographic coordinate system.
 */
public class ProjectedCoordinateSystem extends HorizontalCoordinateSystem implements IProjectedCoordinateSystem {
    private IGeographicCoordinateSystem _geographicCoordinateSystem;
    private ILinearUnit _linearUnit;
    private IProjection _projection;

    /**
     * Initializes a new instance of a projected coordinate system
     *
     * @param datum                      Horizontal datum
     * @param geographicCoordinateSystem Geographic coordinate system
     * @param linearUnit                 Linear unit
     * @param projection                 Projection
     * @param axisInfo                   Axis info
     * @param name                       Name
     * @param authority                  Authority name
     * @param code                       Authority-specific identification code.
     * @param alias                      Alias
     * @param abbreviation               Abbreviation
     * @param remarks                    Provider-supplied remarks
     */
    public ProjectedCoordinateSystem(IHorizontalDatum datum, IGeographicCoordinateSystem geographicCoordinateSystem, ILinearUnit linearUnit, IProjection projection, ArrayList<AxisInfo> axisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation) {
        super(datum, axisInfo, name, authority, code, alias, abbreviation, remarks);

        _geographicCoordinateSystem = geographicCoordinateSystem;
        _linearUnit = linearUnit;
        _projection = projection;
    }

    /**
     * Universal Transverse Mercator - WGS84
     *
     * @param Zone        UTM zone
     * @param ZoneIsNorth true of Northern hemisphere, false if southern
     * @return UTM/WGS84 coordsys
     */
    public static ProjectedCoordinateSystem WGS84_UTM(int Zone, boolean ZoneIsNorth) {
        ArrayList<ProjectionParameter> pInfo = new ArrayList<ProjectionParameter>();
        pInfo.add(new ProjectionParameter("latitude_of_origin", 0));
        pInfo.add(new ProjectionParameter("central_meridian", Zone * 6 - 183));
        pInfo.add(new ProjectionParameter("scale_factor", 0.9996));
        pInfo.add(new ProjectionParameter("false_easting", 500000));
        pInfo.add(new ProjectionParameter("false_northing", ZoneIsNorth ? 0 : 10000000));
        //IProjection projection = cFac.createProjection("UTM" + Zone.ToString() + (ZoneIsNorth ? "N" : "S"), "Transverse_Mercator", parameters);
        Projection proj = new Projection("Transverse_Mercator", pInfo, "UTM" + (new Integer(Zone)).toString() + (ZoneIsNorth ? "N" : "S"), "EPSG", 32600 + Zone + (ZoneIsNorth ? 0 : 100), "", "", "");
        ArrayList<AxisInfo> axes = new ArrayList<AxisInfo>();
        axes.add(new AxisInfo("East", AxisOrientationEnum.East));
        axes.add(new AxisInfo("North", AxisOrientationEnum.North));
        return new ProjectedCoordinateSystem(HorizontalDatum.getWGS84(), GeographicCoordinateSystem.getWGS84(), LinearUnit.getMetre(), proj, axes, "WGS 84 / UTM zone " + (new Integer(Zone)).toString() + (ZoneIsNorth ? "N" : "S"), "EPSG", 32600 + Zone + (ZoneIsNorth ? 0 : 100), "", "Large and medium scale topographic mapping and engineering survey.", "");
    }

    /**
     * Gets or sets the GeographicCoordinateSystem.
     */
    public final IGeographicCoordinateSystem getGeographicCoordinateSystem() {
        return _geographicCoordinateSystem;
    }

    public final void setGeographicCoordinateSystem(IGeographicCoordinateSystem value) {
        _geographicCoordinateSystem = value;
    }

    /**
     * Gets or sets the <see cref="LinearUnit">LinearUnits</see>. The linear unit must be the same as the <see cref="CoordinateSystem"/> units.
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
     * Gets or sets the projection
     */
    public final IProjection getProjection() {
        return _projection;
    }

    public final void setProjection(IProjection value) {
        _projection = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("PROJCS[\"%1$s\", %2$s, %3$s", getName(), getGeographicCoordinateSystem().getWKT(), getProjection().getWKT()));
        for (int i = 0; i < getProjection().getNumParameters(); i++) {
            sb.append(String.format(Locale.US, ", %1$s", getProjection().getParameter(i).getWKT()));
        }
        sb.append(String.format(", %1$s", getLinearUnit().getWKT()));
        //Skip axis info if they contain default values
        if (getAxisInfo().size() != 2 || !getAxisInfo().get(0).getName().equals("X") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(1).getName().equals("Y") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.North) {
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
     * Gets an XML representation of this object.
     */
    @Override
    public String getXML() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "<CS_CoordinateSystem Dimension=\"%1$s\"><CS_ProjectedCoordinateSystem>%2$s", this.getDimension(), getInfoXml()));
        for (AxisInfo ai : this.getAxisInfo()) {
            sb.append(ai.getXML());
        }

        sb.append(String.format("%1$s%2$s%3$s</CS_ProjectedCoordinateSystem></CS_CoordinateSystem>", getGeographicCoordinateSystem().getXML(), getLinearUnit().getXML(), getProjection().getXML()));
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
        if (!(obj instanceof ProjectedCoordinateSystem)) {
            return false;
        }
        ProjectedCoordinateSystem pcs = (ProjectedCoordinateSystem) ((obj instanceof ProjectedCoordinateSystem) ? obj : null);
        if (pcs.getDimension() != this.getDimension()) {
            return false;
        }
        for (int i = 0; i < pcs.getDimension(); i++) {
            if (pcs.getAxis(i).getOrientation() != this.getAxis(i).getOrientation()) {
                return false;
            }
            if (!pcs.getUnits(i).equalParams(this.getUnits(i))) {
                return false;
            }
        }

        return pcs.getGeographicCoordinateSystem().equalParams(this.getGeographicCoordinateSystem()) && pcs.getHorizontalDatum().equalParams(this.getHorizontalDatum()) && pcs.getLinearUnit().equalParams(this.getLinearUnit()) && pcs.getProjection().equalParams(this.getProjection());
    }
}