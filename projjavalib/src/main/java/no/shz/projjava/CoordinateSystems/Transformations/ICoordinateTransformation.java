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
 * Describes a coordinate transformation. This interface only describes a
 * coordinate transformation, it does not actually perform the transform
 * operation on points. To transform points you must use a math transform.
 */
public interface ICoordinateTransformation {
    /**
     * Human readable description of domain in source coordinate system.
     */
    String getAreaOfUse();

    /**
     * Authority which defined transformation and parameter values.
     * <p>
     * <p>
     * An Authority is an organization that maintains definitions of Authority Codes. For example the European Petroleum Survey Group (EPSG) maintains a database of coordinate systems, and other spatial referencing objects, where each object has a code number ID. For example, the EPSG code for a WGS84 Lat/Lon coordinate system is �4326�
     */
    String getAuthority();

    /**
     * Code used by authority to identify transformation. An empty string is used for no code.
     * <p>
     * The AuthorityCode is a compact string defined by an Authority to reference a particular spatial reference object. For example, the European Survey Group (EPSG) authority uses 32 bit integers to reference coordinate systems, so all their code strings will consist of a few digits. The EPSG code for WGS84 Lat/Lon is �4326�.
     */
    long getAuthorityCode();

    /**
     * Gets math transform.
     */
    IMathTransform getMathTransform();

    /**
     * Name of transformation.
     */
    String getName();

    /**
     * Gets the provider-supplied remarks.
     */
    String getRemarks();

    /**
     * Source coordinate system.
     */
    ICoordinateSystem getSourceCS();

    /**
     * Target coordinate system.
     */
    ICoordinateSystem getTargetCS();

    /**
     * Semantic type of transform. For example, a datum transformation or a coordinate conversion.
     */
    TransformType getTransformType();
}