import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import no.shz.projjava.SpatialReferences;
import no.shz.projjava.coordinateSystems.AngularUnit;
import no.shz.projjava.coordinateSystems.AxisInfo;
import no.shz.projjava.coordinateSystems.AxisOrientationEnum;
import no.shz.projjava.coordinateSystems.CoordinateSystemFactory;
import no.shz.projjava.coordinateSystems.DatumType;
import no.shz.projjava.coordinateSystems.HorizontalDatum;
import no.shz.projjava.coordinateSystems.ICoordinateSystem;
import no.shz.projjava.coordinateSystems.IEllipsoid;
import no.shz.projjava.coordinateSystems.IGeocentricCoordinateSystem;
import no.shz.projjava.coordinateSystems.IGeographicCoordinateSystem;
import no.shz.projjava.coordinateSystems.IHorizontalDatum;
import no.shz.projjava.coordinateSystems.IProjectedCoordinateSystem;
import no.shz.projjava.coordinateSystems.IProjection;
import no.shz.projjava.coordinateSystems.LinearUnit;
import no.shz.projjava.coordinateSystems.PrimeMeridian;
import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.Wgs84ConversionInfo;
import no.shz.projjava.coordinateSystems.transformations.CoordinateTransformationFactory;
import no.shz.projjava.coordinateSystems.transformations.ICoordinateTransformation;

import static org.junit.Assert.assertTrue;

public class CoordinateTransformTests {

    @Test
    public final void TestAlbersProjection()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Clarke 1866", 6378206.4, 294.9786982138982, LinearUnit.getUSSurveyFoot());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Clarke 1866", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Clarke 1866", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("central_meridian", -96));
        parameters.add(new ProjectionParameter("latitude_of_center", 23));
        parameters.add(new ProjectionParameter("standard_parallel_1", 29.5));
        parameters.add(new ProjectionParameter("standard_parallel_2", 45.5));
        parameters.add(new ProjectionParameter("false_easting", 0));
        parameters.add(new ProjectionParameter("false_northing", 0));
        IProjection projection = cFac.createProjection("Albers Conical Equal Area", "albers", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("Albers Conical Equal Area", gcs, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {-75, 35};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {1885472.7, 1535925};

        assertTrue(String.format("Albers forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], pUtm[0], pUtm[1]), ToleranceLessThan(pUtm, expected, 0.05));
        assertTrue(String.format("Albers reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    @Test
    public final void TestAlbersProjectionFeet()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Clarke 1866", 6378206.4, 294.9786982138982, LinearUnit.getUSSurveyFoot());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Clarke 1866", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Clarke 1866", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("central_meridian", -96));
        parameters.add(new ProjectionParameter("latitude_of_center", 23));
        parameters.add(new ProjectionParameter("standard_parallel_1", 29.5));
        parameters.add(new ProjectionParameter("standard_parallel_2", 45.5));
        parameters.add(new ProjectionParameter("false_easting", 0));
        parameters.add(new ProjectionParameter("false_northing", 0));
        IProjection projection = cFac.createProjection("Albers Conical Equal Area", "albers", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("Albers Conical Equal Area", gcs, projection, LinearUnit.getFoot(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {-75, 35};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {1885472.7 / LinearUnit.getFoot().getMetersPerUnit(), 1535925 / LinearUnit.getFoot().getMetersPerUnit()};

        assertTrue(String.format("Albers forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], pUtm[0], pUtm[1]), ToleranceLessThan(pUtm, expected, 0.1));
        assertTrue(String.format("Albers reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    @Test
    public final void TestMercator_1SP_Projection()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Bessel 1840", 6377397.155, 299.15281, LinearUnit.getMetre());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Bessel 1840", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Bessel 1840", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<>(5);
        parameters.add(new ProjectionParameter("latitude_of_origin", 0));
        parameters.add(new ProjectionParameter("central_meridian", 110));
        parameters.add(new ProjectionParameter("scale_factor", 0.997));
        parameters.add(new ProjectionParameter("false_easting", 3900000));
        parameters.add(new ProjectionParameter("false_northing", 900000));
        IProjection projection = cFac.createProjection("Mercator_1SP", "Mercator_1SP", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("Makassar / NEIEZ", gcs, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {120, -3};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {5009726.58, 569150.82};

        assertTrue(String.format("Mercator_1SP forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], pUtm[0], pUtm[1]), ToleranceLessThan(pUtm, expected, 0.02));
        assertTrue(String.format("Mercator_1SP reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    @Test
    public final void TestMercator_1SP_Projection_Feet()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Bessel 1840", 6377397.155, 299.15281, LinearUnit.getMetre());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Bessel 1840", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Bessel 1840", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("latitude_of_origin", 0));
        parameters.add(new ProjectionParameter("central_meridian", 110));
        parameters.add(new ProjectionParameter("scale_factor", 0.997));
        parameters.add(new ProjectionParameter("false_easting", 3900000 / LinearUnit.getFoot().getMetersPerUnit()));
        parameters.add(new ProjectionParameter("false_northing", 900000 / LinearUnit.getFoot().getMetersPerUnit()));
        IProjection projection = cFac.createProjection("Mercator_1SP", "Mercator_1SP", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("Makassar / NEIEZ", gcs, projection, LinearUnit.getFoot(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {120, -3};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {5009726.58 / LinearUnit.getFoot().getMetersPerUnit(), 569150.82 / LinearUnit.getFoot().getMetersPerUnit()};

        assertTrue(String.format("Mercator_1SP forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0],expected[1], pUtm[0],pUtm[1]), ToleranceLessThan(pUtm, expected, 0.02));
        assertTrue(String.format("Mercator_1SP reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    @Test
    public final void TestMercator_2SP_Projection()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Krassowski 1940", 6378245.0, 298.3, LinearUnit.getMetre());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Krassowski 1940", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Krassowski 1940", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("latitude_of_origin", 42));
        parameters.add(new ProjectionParameter("central_meridian", 51));
        parameters.add(new ProjectionParameter("false_easting", 0));
        parameters.add(new ProjectionParameter("false_northing", 0));
        IProjection projection = cFac.createProjection("Mercator_2SP", "Mercator_2SP", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("Pulkovo 1942 / Mercator Caspian Sea", gcs, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {53, 53};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {165704.29, 5171848.07};

        assertTrue(String.format("Mercator_2SP forward transformation outside tolerance, Expected %1$s, got %2$s", expected.toString(), pUtm.toString()), ToleranceLessThan(pUtm, expected, 0.02));
        assertTrue(String.format("Mercator_2SP reverse transformation outside tolerance, Expected %1$s, got %2$s", pGeo.toString(), pGeo2.toString()), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    @Test
    public final void TestTransverseMercator_Projection()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Airy 1830", 6377563.396, 299.32496, LinearUnit.getMetre());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Airy 1830", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Airy 1830", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("latitude_of_origin", 49));
        parameters.add(new ProjectionParameter("central_meridian", -2));
        parameters.add(new ProjectionParameter("scale_factor", 0.9996012717));
        parameters.add(new ProjectionParameter("false_easting", 400000));
        parameters.add(new ProjectionParameter("false_northing", -100000));
        IProjection projection = cFac.createProjection("Transverse Mercator", "Transverse_Mercator", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("OSGB 1936 / British National Grid", gcs, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {0.5, 50.5};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {577274.99, 69740.50};

        assertTrue(String.format("TransverseMercator forward transformation outside tolerance, Expected %1$s, got %2$s", expected.toString(), pUtm.toString()), ToleranceLessThan(pUtm, expected, 0.02));
        assertTrue(String.format("TransverseMercator reverse transformation outside tolerance, Expected %1$s, got %2$s", pGeo.toString(), pGeo2.toString()), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    @Test
    public final void TestLambertConicConformal2SP_Projection()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Clarke 1866", 20925832.16, 294.97470, LinearUnit.getUSSurveyFoot());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Clarke 1866", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Clarke 1866", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("latitude_of_origin", 27.833333333));
        parameters.add(new ProjectionParameter("central_meridian", -99));
        parameters.add(new ProjectionParameter("standard_parallel_1", 28.3833333333));
        parameters.add(new ProjectionParameter("standard_parallel_2", 30.2833333333));
        parameters.add(new ProjectionParameter("false_easting", 2000000 / LinearUnit.getUSSurveyFoot().getMetersPerUnit()));
        parameters.add(new ProjectionParameter("false_northing", 0));
        IProjection projection = cFac.createProjection("Lambert Conic Conformal (2SP)", "lambert_conformal_conic_2sp", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("NAD27 / Texas South Central", gcs, projection, LinearUnit.getUSSurveyFoot(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        double[] pGeo = new double[] {-96, 28.5};
        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        double[] expected = new double[] {2963503.91 / LinearUnit.getUSSurveyFoot().getMetersPerUnit(), 254759.80 / LinearUnit.getUSSurveyFoot().getMetersPerUnit()};

        assertTrue(String.format("LambertConicConformal2SP forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], pUtm[0], pUtm[1]), ToleranceLessThan(pUtm, expected, 0.05));
        assertTrue(String.format("LambertConicConformal2SP reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));

    }

    @Test
    public final void TestGeocentric()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("ETRF89 Geographic", AngularUnit.getDegrees(), HorizontalDatum.getETRF89(), PrimeMeridian.getGreenwich(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));
        IGeocentricCoordinateSystem gcenCs = cFac.createGeocentricCoordinateSystem("ETRF89 Geocentric", HorizontalDatum.getETRF89(), LinearUnit.getMetre(), PrimeMeridian.getGreenwich());
        CoordinateTransformationFactory gtFac = new CoordinateTransformationFactory();
        ICoordinateTransformation ct = gtFac.createFromCoordinateSystems(gcs, gcenCs);
        double[] pExpected = new double[] {2 + 7.0 / 60 + 46.38 / 3600, 53 + 48.0 / 60 + 33.82 / 3600}; // Point.FromDMS(2, 7, 46.38, 53, 48, 33.82);
        double[] pExpected3D = new double[] {pExpected[0], pExpected[1], 73.0};
        double[] p0 = new double[] {3771793.97, 140253.34, 5124304.35};
        Object tempVar = ct.getMathTransform().transform(pExpected3D);
        double[] p1 = (double[])((tempVar instanceof double[]) ? tempVar : null);
        Object tempVar2 = ct.getMathTransform().inverse().transform(p1);
        double[] p2 = (double[])((tempVar2 instanceof double[]) ? tempVar2 : null);
        assertTrue(ToleranceLessThan(p1, p0, 0.01));
        assertTrue(ToleranceLessThan(p2, pExpected, 0.00001));
    }

    @Test
    public final void TestDatumTransform()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();
        //Define datums
        HorizontalDatum wgs72 = HorizontalDatum.getWGS72();
        HorizontalDatum ed50 = HorizontalDatum.getED50();

        //Define geographic coordinate systems
        IGeographicCoordinateSystem gcsWGS72 = cFac.createGeographicCoordinateSystem("WGS72 Geographic", AngularUnit.getDegrees(), wgs72, PrimeMeridian.getGreenwich(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        IGeographicCoordinateSystem gcsWGS84 = cFac.createGeographicCoordinateSystem("WGS84 Geographic", AngularUnit.getDegrees(), HorizontalDatum.getWGS84(), PrimeMeridian.getGreenwich(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        IGeographicCoordinateSystem gcsED50 = cFac.createGeographicCoordinateSystem("ED50 Geographic", AngularUnit.getDegrees(), ed50, PrimeMeridian.getGreenwich(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        //Define geocentric coordinate systems
        IGeocentricCoordinateSystem gcenCsWGS72 = cFac.createGeocentricCoordinateSystem("WGS72 Geocentric", wgs72, LinearUnit.getMetre(), PrimeMeridian.getGreenwich());
        IGeocentricCoordinateSystem gcenCsWGS84 = cFac.createGeocentricCoordinateSystem("WGS84 Geocentric", HorizontalDatum.getWGS84(), LinearUnit.getMetre(), PrimeMeridian.getGreenwich());
        IGeocentricCoordinateSystem gcenCsED50 = cFac.createGeocentricCoordinateSystem("ED50 Geocentric", ed50, LinearUnit.getMetre(), PrimeMeridian.getGreenwich());

        //Define projections
        ArrayList<ProjectionParameter> parameters = new ArrayList<>(5);
        parameters.add(new ProjectionParameter("latitude_of_origin", 0));
        parameters.add(new ProjectionParameter("central_meridian", 9));
        parameters.add(new ProjectionParameter("scale_factor", 0.9996));
        parameters.add(new ProjectionParameter("false_easting", 500000));
        parameters.add(new ProjectionParameter("false_northing", 0));
        IProjection projection = cFac.createProjection("Transverse Mercator", "Transverse_Mercator", parameters);
        IProjectedCoordinateSystem utmED50 = cFac.createProjectedCoordinateSystem("ED50 UTM Zone 32N", gcsED50, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));
        IProjectedCoordinateSystem utmWGS84 = cFac.createProjectedCoordinateSystem("WGS84 UTM Zone 32N", gcsWGS84, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        //Set TOWGS84 parameters
        wgs72.setWgs84Parameters(new Wgs84ConversionInfo(0, 0, 4.5, 0, 0, 0.554, 0.219));
        ed50.setWgs84Parameters(new Wgs84ConversionInfo(-81.0703, -89.3603, -115.7526, -0.48488, -0.02436, -0.41321, -0.540645)); //Parameters for Denmark

        //Set up coordinate transformations
        CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
        ICoordinateTransformation ctForw = ctFac.createFromCoordinateSystems(gcsWGS72, gcenCsWGS72); //Geographic->Geocentric (WGS72)
        ICoordinateTransformation ctWGS84_Gcen2Geo = ctFac.createFromCoordinateSystems(gcenCsWGS84, gcsWGS84); //Geocentric->Geographic (WGS84)
        ICoordinateTransformation ctWGS84_Geo2UTM = ctFac.createFromCoordinateSystems(gcsWGS84, utmWGS84); //UTM ->Geographic (WGS84)
        ICoordinateTransformation ctED50_UTM2Geo = ctFac.createFromCoordinateSystems(utmED50, gcsED50); //UTM ->Geographic (ED50)
        ICoordinateTransformation ctED50_Geo2Gcen = ctFac.createFromCoordinateSystems(gcsED50, gcenCsED50); //Geographic->Geocentric (ED50)

        //Test datum-shift from WGS72 to WGS84
        //Point3D pGeoCenWGS72 = ctForw.MathTransform.transform(pLongLatWGS72) as Point3D;
        double[] pGeoCenWGS72 = new double[] {3657660.66, 255768.55, 5201382.11};
        ICoordinateTransformation geocen_ed50_2_Wgs84 = ctFac.createFromCoordinateSystems(gcenCsWGS72, gcenCsWGS84);
        double[] pGeoCenWGS84 = geocen_ed50_2_Wgs84.getMathTransform().transform(pGeoCenWGS72);
        //Point3D pGeoCenWGS84 = wgs72.Wgs84Parameters.Apply(pGeoCenWGS72);

        assertTrue(ToleranceLessThan(new double[] {3657660.78, 255778.43, 5201387.75}, pGeoCenWGS84, 0.01));

        ICoordinateTransformation utm_ed50_2_Wgs84 = ctFac.createFromCoordinateSystems(utmED50, utmWGS84);
        double[] pUTMED50 = new double[] {600000, 6100000};
        double[] pUTMWGS84 = utm_ed50_2_Wgs84.getMathTransform().transform(pUTMED50);
        assertTrue(ToleranceLessThan(new double[] {599928.6, 6099790.2}, pUTMWGS84, 0.1));
        //Perform reverse
        ICoordinateTransformation utm_Wgs84_2_Ed50 = ctFac.createFromCoordinateSystems(utmWGS84, utmED50);
        pUTMED50 = utm_Wgs84_2_Ed50.getMathTransform().transform(pUTMWGS84);
        assertTrue(ToleranceLessThan(new double[] {600000, 6100000}, pUTMED50, 0.1));
        //Assert.IsTrue(Math.Abs((pUTMWGS84 as Point3D).Z - 36.35) < 0.5);
        //Point pExpected = Point.FromDMS(2, 7, 46.38, 53, 48, 33.82);
        //ED50_to_WGS84_Denmark: datum.Wgs84Parameters = new Wgs84ConversionInfo(-89.5, -93.8, 127.6, 0, 0, 4.5, 1.2);

    }

    @Test
    public final void TestKrovak_Projection()
    {
        CoordinateSystemFactory cFac = new CoordinateSystemFactory();

        IEllipsoid ellipsoid = cFac.createFlattenedSphere("Bessel 1840", 6377397.155, 299.15281, LinearUnit.getMetre());

        IHorizontalDatum datum = cFac.createHorizontalDatum("Bessel 1840", DatumType.HD_Geocentric, ellipsoid, null);
        IGeographicCoordinateSystem gcs = cFac.createGeographicCoordinateSystem("Bessel 1840", AngularUnit.getDegrees(), datum, PrimeMeridian.getGreenwich(), new AxisInfo("Lon", AxisOrientationEnum.East), new AxisInfo("Lat", AxisOrientationEnum.North));
        ArrayList<ProjectionParameter> parameters = new ArrayList<ProjectionParameter>(5);
        parameters.add(new ProjectionParameter("latitude_of_center", 49.5));
        parameters.add(new ProjectionParameter("longitude_of_center", 42.5));
        parameters.add(new ProjectionParameter("azimuth", 30.28813972222222));
        parameters.add(new ProjectionParameter("pseudo_standard_parallel_1", 78.5));
        parameters.add(new ProjectionParameter("scale_factor", 0.9999));
        parameters.add(new ProjectionParameter("false_easting", 0));
        parameters.add(new ProjectionParameter("false_northing", 0));
        IProjection projection = cFac.createProjection("Krovak", "Krovak", parameters);

        IProjectedCoordinateSystem coordsys = cFac.createProjectedCoordinateSystem("WGS 84", gcs, projection, LinearUnit.getMetre(), new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North));

        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(gcs, coordsys);

        // test case 1
        double[] pGeo = new double[] {12, 48};
        double[] expected = new double[] {-953172.26, -1245573.32};

        double[] pUtm = trans.getMathTransform().transform(pGeo);
        double[] pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        assertTrue(String.format("Krovak forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], pUtm[0], pUtm[1]), ToleranceLessThan(pUtm, expected, 0.02));
        assertTrue(String.format("Krovak reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));

        // test case 2
        pGeo = new double[] {18, 49};
        expected = new double[] {-499258.06, -1192389.16};

        pUtm = trans.getMathTransform().transform(pGeo);
        pGeo2 = trans.getMathTransform().inverse().transform(pUtm);

        assertTrue(String.format("Krovak forward transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], pUtm[0], pUtm[1]), ToleranceLessThan(pUtm, expected, 0.02));
        assertTrue(String.format("Krovak reverse transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", pGeo[0], pGeo[1], pGeo2[0], pGeo2[1]), ToleranceLessThan(pGeo, pGeo2, 0.0000001));
    }

    private boolean ToleranceLessThan(double[] p1, double[] p2, double tolerance)
    {
        if (p1.length > 2 && p2.length > 2)
        {
            return Math.abs(p1[0] - p2[0]) < tolerance && Math.abs(p1[1] - p2[1]) < tolerance && Math.abs(p1[2] - p2[2]) < tolerance;
        }
        else
        {
            return Math.abs(p1[0] - p2[0]) < tolerance && Math.abs(p1[1] - p2[1]) < tolerance;
        }
    }


    @Test
    public final void TestUnitTransforms() throws IOException {
        ICoordinateSystem nadUTM = SpatialReferences.getByEpsg(2868); //UTM Arizona Central State Plane using Feet as units
        ICoordinateSystem wgs84GCS = SpatialReferences.getByEpsg(4326); //GCS WGS84
        ICoordinateTransformation trans = (new CoordinateTransformationFactory()).createFromCoordinateSystems(wgs84GCS, nadUTM);

        double[] p0 = new double[] {-111.89, 34.165};
        double[] expected = new double[] {708066.190579, 1151426.44638};

        double[] p1 = trans.getMathTransform().transform(p0);
        double[] p2 = trans.getMathTransform().inverse().transform(p1);

        assertTrue(String.format("Transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", expected[0], expected[1], p1[0],p1[1]), ToleranceLessThan(p1, expected, 0.013));
        //WARNING: This accuracy is too poor!
        assertTrue(String.format("Transformation outside tolerance, Expected [%1$s,%2$s], got [%3$s,%4$s]", p0[0], p0[1], p2[0], p2[1]), ToleranceLessThan(p0, p2, 0.0000001));
    }

}
