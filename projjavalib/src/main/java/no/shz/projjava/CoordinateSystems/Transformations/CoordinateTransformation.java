package no.shz.projjava.coordinateSystems.transformations;

import no.shz.projjava.coordinateSystems.ICoordinateSystem;

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
 * Describes a coordinate transformation. This class only describes a
 * coordinate transformation, it does not actually perform the transform
 * operation on points. To transform points you must use a <see cref="MathTransform"/>.
 */
public class CoordinateTransformation implements ICoordinateTransformation {
    private String _AreaOfUse;
    private String _Authority;
    private long _AuthorityCode;
    private IMathTransform _MathTransform;
    private String _Name;
    private String _Remarks;
    private ICoordinateSystem _SourceCS;
    private ICoordinateSystem _TargetCS;
    private TransformType _TransformType = TransformType.values()[0];

    /**
     * Initializes an instance of a CoordinateTransformation
     *
     * @param sourceCS      Source coordinate system
     * @param targetCS      Target coordinate system
     * @param transformType Transformation type
     * @param mathTransform Math transform
     * @param name          Name of transform
     * @param authority     Authority
     * @param authorityCode Authority code
     * @param areaOfUse     Area of use
     * @param remarks       Remarks
     */
    public CoordinateTransformation(ICoordinateSystem sourceCS, ICoordinateSystem targetCS, TransformType transformType, IMathTransform mathTransform, String name, String authority, long authorityCode, String areaOfUse, String remarks) {
        super();
        _TargetCS = targetCS;
        _SourceCS = sourceCS;
        _TransformType = transformType;
        _MathTransform = mathTransform;
        _Name = name;
        _Authority = authority;
        _AuthorityCode = authorityCode;
        _AreaOfUse = areaOfUse;
        _Remarks = remarks;
    }

    /**
     * Human readable description of domain in source coordinate system.
     */
    public final String getAreaOfUse() {
        return _AreaOfUse;
    }

    /**
     * Authority which defined transformation and parameter values.
     * <p>
     * <p>
     * An Authority is an organization that maintains definitions of Authority Codes. For example the European Petroleum Survey Group (EPSG) maintains a database of coordinate systems, and other spatial referencing objects, where each object has a code number ID. For example, the EPSG code for a WGS84 Lat/Lon coordinate system is �4326�
     */
    public final String getAuthority() {
        return _Authority;
    }

    /**
     * Code used by authority to identify transformation. An empty string is used for no code.
     * <p>
     * The AuthorityCode is a compact string defined by an Authority to reference a particular spatial reference object. For example, the European Survey Group (EPSG) authority uses 32 bit integers to reference coordinate systems, so all their code strings will consist of a few digits. The EPSG code for WGS84 Lat/Lon is �4326�.
     */
    public final long getAuthorityCode() {
        return _AuthorityCode;
    }

    /**
     * Gets math transform.
     */
    public final IMathTransform getMathTransform() {
        return _MathTransform;
    }

    /**
     * Name of transformation.
     */
    public final String getName() {
        return _Name;
    }

    /**
     * Gets the provider-supplied remarks.
     */
    public final String getRemarks() {
        return _Remarks;
    }

    /**
     * Source coordinate system.
     */
    public final ICoordinateSystem getSourceCS() {
        return _SourceCS;
    }

    /**
     * Target coordinate system.
     */
    public final ICoordinateSystem getTargetCS() {
        return _TargetCS;
    }

    /**
     * Semantic type of transform. For example, a datum transformation or a coordinate conversion.
     */
    public final TransformType getTransformType() {
        return _TransformType;
    }
}