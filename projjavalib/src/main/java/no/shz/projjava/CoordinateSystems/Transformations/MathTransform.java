package no.shz.projjava.coordinateSystems.transformations;

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
 * Abstract class for creating multi-dimensional coordinate points transformations.
 * <p>
 * If a client application wishes to query the source and target coordinate
 * systems of a transformation, then it should keep hold of the
 * <see cref="ICoordinateTransformation"/> interface, and use the contained
 * math transform object whenever it wishes to perform a transform.
 */
public abstract class MathTransform implements IMathTransform {

    protected static final double R2D = 180 / Math.PI;
    protected static final double D2R = Math.PI / 180;

    /**
     * To convert degrees to radians, multiply degrees by pi/180.
     */
    protected static double degrees2Radians(double deg) {
        return (D2R * deg);

    }

    /**
     * @param rad
     * @return
     */
    protected static double radians2Degrees(double rad) {
        return (R2D * rad);
    }

    /**
     * Gets the dimension of input points.
     */
    public int getDimSource() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the dimension of output points.
     */
    public int getDimTarget() {
        throw new UnsupportedOperationException();
    }

    /**
     * Tests whether this transform does not move any points.
     *
     * @return
     */
    public boolean getIdentity() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a Well-Known text representation of this object.
     */
    public abstract String getWKT();

    /**
     * Gets an XML representation of this object.
     */
    public abstract String getXML();

    /**
     * Gets the derivative of this transform at a point. If the transform does
     * not have a well-defined derivative at the point, then this function should
     * fail in the usual way for the DCP. The derivative is the matrix of the
     * non-translating portion of the approximate affine map at the point. The
     * matrix will have dimensions corresponding to the source and target
     * coordinate systems. If the input dimension is M, and the output dimension
     * is N, then the matrix will have size [M][N]. The elements of the matrix
     * {elt[n][m] : n=0..(N-1)} form a vector in the output space which is
     * parallel to the displacement caused by a small change in the m'th ordinate
     * in the input space.
     *
     * @param point
     * @return
     */
    public double[][] derivative(double[] point) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets transformed convex hull.
     * <p>
     * <p>
     * <p>The supplied ordinates are interpreted as a sequence of points, which generates a convex
     * hull in the source space. The returned sequence of ordinates represents a convex hull in the
     * output space. The number of output points will often be different from the number of input
     * points. Each of the input points should be inside the valid domain (this can be checked by
     * testing the points' domain flags individually). However, the convex hull of the input points
     * may go outside the valid domain. The returned convex hull should contain the transformed image
     * of the intersection of the source convex hull and the source domain.</p>
     * <p>A convex hull is a shape in a coordinate system, where if two positions A and B are
     * inside the shape, then all positions in the straight line between A and B are also inside
     * the shape. So in 3D a cube and a sphere are both convex hulls. Other less obvious examples
     * of convex hulls are straight lines, and single points. (A single point is a convex hull,
     * because the positions A and B must both be the same - i.e. the point itself. So the straight
     * line between A and B has zero length.)</p>
     * <p>Some examples of shapes that are NOT convex hulls are donuts, and horseshoes.</p>
     *
     * @param points
     * @return
     */
    public ArrayList<Double> getCodomainConvexHull(ArrayList<Double> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets flags classifying domain points within a convex hull.
     * <p>
     * <p>
     * The supplied ordinates are interpreted as a sequence of points, which
     * generates a convex hull in the source space. Conceptually, each of the
     * (usually infinite) points inside the convex hull is then tested against
     * the source domain. The flags of all these tests are then combined. In
     * practice, implementations of different transforms will use different
     * short-cuts to avoid doing an infinite number of tests.
     *
     * @param points
     * @return
     */
    public DomainFlags getDomainFlags(ArrayList<Double> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the inverse transform of this object.
     * <p>
     * This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
     *
     * @return
     */
    public abstract IMathTransform inverse();

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point
     * @return
     */
    public abstract double[] transform(double[] point);

    /**
     * Transforms a list of coordinate point ordinal values.
     * <p>
     * <p>
     * This method is provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values. For example, if the source
     * dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
     * The size of the passed array must be an integer multiple of DimSource. The returned
     * ordinal values are packed in a similar way. In some DCPs. the ordinals may be
     * transformed in-place, and the returned array may be the same as the passed array.
     * So any client code should not attempt to reuse the passed ordinal values (although
     * they can certainly reuse the passed array). If there is any problem then the server
     * implementation will throw an exception. If this happens then the client should not
     * make any assumptions about the state of the ordinal values.
     *
     * @param points
     * @return
     */
    public abstract ArrayList<double[]> transformList(ArrayList<double[]> points);

    /**
     * Reverses the transformation
     */
    public abstract void invert();
}