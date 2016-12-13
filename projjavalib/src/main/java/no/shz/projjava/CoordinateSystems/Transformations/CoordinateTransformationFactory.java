package no.shz.projjava.coordinateSystems.transformations;

import java.util.ArrayList;
import java.util.Locale;

import no.shz.projjava.coordinateSystems.CoordinateSystemFactory;
import no.shz.projjava.coordinateSystems.GeocentricCoordinateSystem;
import no.shz.projjava.coordinateSystems.ICoordinateSystem;
import no.shz.projjava.coordinateSystems.IEllipsoid;
import no.shz.projjava.coordinateSystems.IGeocentricCoordinateSystem;
import no.shz.projjava.coordinateSystems.IGeographicCoordinateSystem;
import no.shz.projjava.coordinateSystems.ILinearUnit;
import no.shz.projjava.coordinateSystems.IProjectedCoordinateSystem;
import no.shz.projjava.coordinateSystems.IProjection;
import no.shz.projjava.coordinateSystems.LinearUnit;
import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.projections.AlbersProjection;
import no.shz.projjava.coordinateSystems.projections.KrovakProjection;
import no.shz.projjava.coordinateSystems.projections.LambertConformalConic2SP;
import no.shz.projjava.coordinateSystems.projections.Mercator;
import no.shz.projjava.coordinateSystems.projections.TransverseMercator;
import no.shz.utilities.Ref;

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
 * Creates coordinate transformations.
 */
public class CoordinateTransformationFactory implements ICoordinateTransformationFactory {

    private static void simplifyTrans(ConcatenatedTransform mtrans, Ref<ArrayList<ICoordinateTransformation>> MTs) {
        for (ICoordinateTransformation t : mtrans.getCoordinateTransformationList()) {
            if (t instanceof ConcatenatedTransform) {
                simplifyTrans(((t instanceof ConcatenatedTransform) ? (ConcatenatedTransform) t : null), MTs);
            } else {
                MTs.value.add(t);
            }
        }
    }

    private static ICoordinateTransformation geog2Geoc(IGeographicCoordinateSystem source, IGeocentricCoordinateSystem target) {
        IMathTransform geocMathTransform = createCoordinateOperation(target);
        return new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", "");
    }

    private static ICoordinateTransformation geoc2Geog(IGeocentricCoordinateSystem source, IGeographicCoordinateSystem target) {
        IMathTransform geocMathTransform = createCoordinateOperation(source).inverse();
        return new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", "");
    }

    private static ICoordinateTransformation proj2Proj(IProjectedCoordinateSystem source, IProjectedCoordinateSystem target) {
        ConcatenatedTransform ct = new ConcatenatedTransform();
        CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, source.getGeographicCoordinateSystem()));
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source.getGeographicCoordinateSystem(), target.getGeographicCoordinateSystem()));
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(target.getGeographicCoordinateSystem(), target));

        return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
    }

    private static ICoordinateTransformation geog2Proj(IGeographicCoordinateSystem source, IProjectedCoordinateSystem target) {
        if (source.equalParams(target.getGeographicCoordinateSystem())) {
            IMathTransform mathTransform = createCoordinateOperation(target.getProjection(), target.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid(), target.getLinearUnit());
            return new CoordinateTransformation(source, target, TransformType.Transformation, mathTransform, "", "", -1, "", "");
        } else {
            ConcatenatedTransform ct = new ConcatenatedTransform();
            CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, target.getGeographicCoordinateSystem()));
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(target.getGeographicCoordinateSystem(), target));
            return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
        }
    }

    private static ICoordinateTransformation proj2Geog(IProjectedCoordinateSystem source, IGeographicCoordinateSystem target) {
        if (source.getGeographicCoordinateSystem().equalParams(target)) {
            IMathTransform mathTransform = createCoordinateOperation(source.getProjection(), source.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid(), source.getLinearUnit()).inverse();
            return new CoordinateTransformation(source, target, TransformType.Transformation, mathTransform, "", "", -1, "", "");
        } else {
            ConcatenatedTransform ct = new ConcatenatedTransform();
            CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, source.getGeographicCoordinateSystem()));
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source.getGeographicCoordinateSystem(), target));
            return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
        }
    }

    /**
     * Geocentric to Geocentric transformation
     *
     * @param source
     * @param target
     * @return
     */
    private static ICoordinateTransformation createGeoc2Geoc(IGeocentricCoordinateSystem source, IGeocentricCoordinateSystem target) {
        ConcatenatedTransform ct = new ConcatenatedTransform();

        if (source.getHorizontalDatum().getWgs84Parameters() != null && !source.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) {
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(((target.getHorizontalDatum().getWgs84Parameters() == null || target.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) ? target : GeocentricCoordinateSystem.getWGS84()), source, TransformType.Transformation, new DatumTransform(source.getHorizontalDatum().getWgs84Parameters()), "", "", -1, "", ""));
        }

        if (target.getHorizontalDatum().getWgs84Parameters() != null && !target.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) {
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(((source.getHorizontalDatum().getWgs84Parameters() == null || source.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) ? source : GeocentricCoordinateSystem.getWGS84()), target, TransformType.Transformation, (new DatumTransform(target.getHorizontalDatum().getWgs84Parameters())).inverse(), "", "", -1, "", ""));
        }

        if (ct.getCoordinateTransformationList().size() == 1)
        {
            return new CoordinateTransformation(source, target, TransformType.ConversionAndTransformation, ct.getCoordinateTransformationList().get(0).getMathTransform(), "", "", -1, "", "");
        } else {
            return new CoordinateTransformation(source, target, TransformType.ConversionAndTransformation, ct, "", "", -1, "", "");
        }
    }

    private static IMathTransform createCoordinateOperation(IGeocentricCoordinateSystem geo) {
        ArrayList<ProjectionParameter> parameterList = new ArrayList<>(2);
        parameterList.add(new ProjectionParameter("semi_major", geo.getHorizontalDatum().getEllipsoid().getSemiMajorAxis()));
        parameterList.add(new ProjectionParameter("semi_minor", geo.getHorizontalDatum().getEllipsoid().getSemiMinorAxis()));
        return new GeocentricTransform(parameterList);
    }

    private static IMathTransform createCoordinateOperation(IProjection projection, IEllipsoid ellipsoid, ILinearUnit unit) {
        ArrayList<ProjectionParameter> parameterList = new ArrayList<>(projection.getNumParameters());
        for (int i = 0; i < projection.getNumParameters(); i++) {
            parameterList.add(projection.getParameter(i));
        }

        parameterList.add(new ProjectionParameter("semi_major", ellipsoid.getSemiMajorAxis()));
        parameterList.add(new ProjectionParameter("semi_minor", ellipsoid.getSemiMinorAxis()));
        parameterList.add(new ProjectionParameter("unit", unit.getMetersPerUnit()));
        IMathTransform transform = null;
        switch (projection.getClassName().toLowerCase(Locale.US).replace(' ', '_')) {
            case "mercator":
            case "mercator_1sp":
            case "mercator_2sp":
                //1SP
                transform = new Mercator(parameterList);
                break;
            case "transverse_mercator":
                transform = new TransverseMercator(parameterList);
                break;
            case "albers":
            case "albers_conic_equal_area":
                transform = new AlbersProjection(parameterList);
                break;
            case "krovak":
                transform = new KrovakProjection(parameterList);
                break;
            case "lambert_conformal_conic":
            case "lambert_conformal_conic_2sp":
            case "lambert_conic_conformal_(2sp)":
                transform = new LambertConformalConic2SP(parameterList);
                break;
            default:
                throw new UnsupportedOperationException(String.format("Projection %1$s is not supported.", projection.getClassName()));
        }
        return transform;
    }

    /**
     * Creates a transformation between two coordinate systems.
     * <p>
     * <p>
     * This method will examine the coordinate systems in order to construct
     * a transformation between them. This method may fail if no path between
     * the coordinate systems is found, using the normal failing behavior of
     * the DCP (e.g. throwing an exception).
     *
     * @param sourceCS Source coordinate system
     * @param targetCS Target coordinate system
     * @return
     */
    public final ICoordinateTransformation createFromCoordinateSystems(ICoordinateSystem sourceCS, ICoordinateSystem targetCS) {
        ICoordinateTransformation trans;
        if (sourceCS instanceof IProjectedCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem)
        {
            trans = proj2Geog((IProjectedCoordinateSystem) sourceCS, (IGeographicCoordinateSystem) targetCS);
        } else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IProjectedCoordinateSystem)
        {
            trans = geog2Proj((IGeographicCoordinateSystem) sourceCS, (IProjectedCoordinateSystem) targetCS);
        } else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IGeocentricCoordinateSystem)
        {
            trans = geog2Geoc((IGeographicCoordinateSystem) sourceCS, (IGeocentricCoordinateSystem) targetCS);
        } else if (sourceCS instanceof IGeocentricCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem)
        {
            trans = geoc2Geog((IGeocentricCoordinateSystem) sourceCS, (IGeographicCoordinateSystem) targetCS);
        } else if (sourceCS instanceof IProjectedCoordinateSystem && targetCS instanceof IProjectedCoordinateSystem)
        {
            trans = proj2Proj((((sourceCS instanceof IProjectedCoordinateSystem) ? (IProjectedCoordinateSystem) sourceCS : null)), (((targetCS instanceof IProjectedCoordinateSystem) ? (IProjectedCoordinateSystem) targetCS : null)));
        } else if (sourceCS instanceof IGeocentricCoordinateSystem && targetCS instanceof IGeocentricCoordinateSystem)
        {
            trans = createGeoc2Geoc((IGeocentricCoordinateSystem) sourceCS, (IGeocentricCoordinateSystem) targetCS);
        } else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem)
        {
            trans = createGeog2Geog(((sourceCS instanceof IGeographicCoordinateSystem) ? (IGeographicCoordinateSystem) sourceCS : null), ((targetCS instanceof IGeographicCoordinateSystem) ? (IGeographicCoordinateSystem) targetCS : null));
        } else {
            throw new UnsupportedOperationException("No support for transforming between the two specified coordinate systems");
        }

        return trans;
    }

    /**
     * Geographic to geographic transformation
     * Adds a datum shift if nessesary
     *
     * @param source
     * @param target
     * @return
     */
    private ICoordinateTransformation createGeog2Geog(IGeographicCoordinateSystem source, IGeographicCoordinateSystem target) {
        if (source.getHorizontalDatum().equalParams(target.getHorizontalDatum())) {
            return new CoordinateTransformation(source, target, TransformType.Conversion, new GeographicTransform(source, target), "", "", -1, "", "");
        } else {
            CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
            CoordinateSystemFactory cFac = new CoordinateSystemFactory();
            IGeocentricCoordinateSystem sourceCentric = cFac.createGeocentricCoordinateSystem(source.getHorizontalDatum().getName() + " Geocentric", source.getHorizontalDatum(), LinearUnit.getMetre(), source.getPrimeMeridian());
            IGeocentricCoordinateSystem targetCentric = cFac.createGeocentricCoordinateSystem(target.getHorizontalDatum().getName() + " Geocentric", target.getHorizontalDatum(), LinearUnit.getMetre(), source.getPrimeMeridian());
            ConcatenatedTransform ct = new ConcatenatedTransform();
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, sourceCentric));
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(sourceCentric, targetCentric));
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(targetCentric, target));
            return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
        }
    }
}