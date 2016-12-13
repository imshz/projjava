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
 * The Projection class defines the standard information stored with a projection
 * objects. A projection object implements a coordinate transformation from a geographic
 * coordinate system to a projected coordinate system, given the ellipsoid for the
 * geographic coordinate system. It is expected that each coordinate transformation of
 * interest, e.g., Transverse Mercator, Lambert, will be implemented as a class of
 * type Projection, supporting the IProjection interface.
 */
public class Projection extends Info implements IProjection {
    private ArrayList<ProjectionParameter> _parameters;
    private String _className;

    public Projection(String className, ArrayList<ProjectionParameter> parameters, String name, String authority, long code, String alias, String remarks, String abbreviation) {
        super(name, authority, code, alias, abbreviation, remarks);
        _parameters = parameters;
        _className = className;
    }

    /**
     * Gets the number of parameters of the projection.
     */
    public final int getNumParameters() {
        return _parameters.size();
    }

    /**
     * Gets or sets the parameters of the projection
     */
    public final ArrayList<ProjectionParameter> getParameters() {
        return _parameters;
    }

    public final void setParameters(ArrayList<ProjectionParameter> value) {
        _parameters = value;
    }

    /**
     * Gets an indexed parameter of the projection.
     *
     * @param n Index of parameter
     * @return n'th parameter
     */
    public final ProjectionParameter getParameter(int n) {
        return _parameters.get(n);
    }

    /**
     * Gets an named parameter of the projection.
     * <p>
     * The parameter name is case insensitive
     *
     * @param name Name of parameter
     * @return parameter or null if not found
     */
    public final ProjectionParameter getParameter(String name) {
        for (ProjectionParameter par : _parameters) {
            if (name.equalsIgnoreCase(par.getName())) {
                return par;
            }
        }
        return null;
    }

    /**
     * Gets the projection classification name (e.g. "Transverse_Mercator").
     */
    public final String getClassName() {
        return _className;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    @Override
    public String getWKT() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("PROJECTION[\"%1$s\"", getName()));
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
        sb.append(String.format(Locale.US, "<CS_Projection Classname=\"%1$s\">%2$s", getClassName(), getInfoXml()));
        for (ProjectionParameter param : getParameters()) {
            sb.append(param.getXML());
        }
        sb.append("</CS_Projection>");
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
        if (!(obj instanceof Projection)) {
            return false;
        }
        Projection proj = (Projection) ((obj instanceof Projection) ? obj : null);
        if (proj.getNumParameters() != this.getNumParameters()) {
            return false;
        }
        for (int i = 0; i < _parameters.size(); i++) {
            ProjectionParameter param = getParameter(proj.getParameter(i).getName());
            if (param == null) {
                return false;
            }
            if (param.getValue() != proj.getParameter(i).getValue()) {
                return false;
            }
        }
        return true;
    }
}