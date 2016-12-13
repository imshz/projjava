package wkt;

import org.junit.Test;

import java.io.IOException;

import no.shz.projjava.SpatialReferences;
import no.shz.projjava.converters.wellKnownText.CoordinateSystemWktReader;
import no.shz.projjava.coordinateSystems.CoordinateSystemFactory;
import no.shz.projjava.coordinateSystems.GeographicCoordinateSystem;
import no.shz.projjava.coordinateSystems.ICoordinateSystem;
import no.shz.projjava.coordinateSystems.IProjectedCoordinateSystem;
import no.shz.projjava.coordinateSystems.ProjectedCoordinateSystem;
import no.shz.projjava.coordinateSystems.ProjectionParameter;
import no.shz.projjava.coordinateSystems.Wgs84ConversionInfo;
import no.shz.projjava.coordinateSystems.transformations.CoordinateTransformationFactory;
import no.shz.projjava.coordinateSystems.transformations.ICoordinateTransformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class WKTCoordSysParserTests {
    @Test
    public final void parseCoordSys() throws IOException {
        CoordinateSystemFactory fac = new CoordinateSystemFactory();
        String wkt = "PROJCS[\"NAD83(HARN) / Texas Central (ftUS)\", GEOGCS[\"NAD83(HARN)\", DATUM[\"NAD83_High_Accuracy_Regional_Network\", SPHEROID[\"GRS 1980\", 6378137, 298.257222101, AUTHORITY[\"EPSG\", \"7019\"]], TOWGS84[725, 685, 536, 0, 0, 0, 0], AUTHORITY[\"EPSG\", \"6152\"]], PRIMEM[\"Greenwich\", 0, AUTHORITY[\"EPSG\", \"8901\"]], UNIT[\"degree\", 0.0174532925199433, AUTHORITY[\"EPSG\", \"9122\"]], AUTHORITY[\"EPSG\", \"4152\"]], PROJECTION[\"Lambert_Conformal_Conic_2SP\"], PARAMETER[\"standard_parallel_1\", 31.883333333333], PARAMETER[\"standard_parallel_2\", 30.1166666667], PARAMETER[\"latitude_of_origin\", 29.6666666667], PARAMETER[\"central_meridian\", -100.333333333333], PARAMETER[\"false_easting\", 2296583.333], PARAMETER[\"false_northing\", 9842500], UNIT[\"US survey foot\", 0.304800609601219, AUTHORITY[\"EPSG\", \"9003\"]], AUTHORITY[\"EPSG\", \"2918\"]]";
        Object tempVar = CoordinateSystemWktReader.parse(wkt);
        ProjectedCoordinateSystem pcs = (ProjectedCoordinateSystem) ((tempVar instanceof ProjectedCoordinateSystem) ? tempVar : null);

        assertNotNull("Could not parse WKT: " + wkt, pcs);

        assertEquals("NAD83(HARN) / Texas Central (ftUS)", pcs.getName());
        assertEquals("NAD83(HARN)", pcs.getGeographicCoordinateSystem().getName());
        assertEquals("NAD83_High_Accuracy_Regional_Network", pcs.getGeographicCoordinateSystem().getHorizontalDatum().getName());
        assertEquals("GRS 1980", pcs.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid().getName());
        assertEquals(6378137, pcs.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid().getSemiMajorAxis(), 0.00001d);
        assertEquals(298.257222101, pcs.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid().getInverseFlattening(), 0.00001d);
        assertEquals("EPSG", pcs.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid().getAuthority());
        assertEquals(7019, pcs.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid().getAuthorityCode());
        assertEquals("EPSG", pcs.getGeographicCoordinateSystem().getHorizontalDatum().getAuthority());
        assertEquals(6152, pcs.getGeographicCoordinateSystem().getHorizontalDatum().getAuthorityCode());
        assertEquals(new Wgs84ConversionInfo(725, 685, 536, 0, 0, 0, 0), pcs.getGeographicCoordinateSystem().getHorizontalDatum().getWgs84Parameters());
        assertEquals("Greenwich", pcs.getGeographicCoordinateSystem().getPrimeMeridian().getName());
        assertEquals(0, pcs.getGeographicCoordinateSystem().getPrimeMeridian().getLongitude(), 0.00001d);
        assertEquals("EPSG", pcs.getGeographicCoordinateSystem().getPrimeMeridian().getAuthority());
        assertEquals(8901, pcs.getGeographicCoordinateSystem().getPrimeMeridian().getAuthorityCode());
        assertEquals("degree", pcs.getGeographicCoordinateSystem().getAngularUnit().getName());
        assertEquals(0.0174532925199433, pcs.getGeographicCoordinateSystem().getAngularUnit().getRadiansPerUnit(), 0.00001d);
        assertEquals("EPSG", pcs.getGeographicCoordinateSystem().getAngularUnit().getAuthority());
        assertEquals(9122, pcs.getGeographicCoordinateSystem().getAngularUnit().getAuthorityCode());
        assertEquals("EPSG", pcs.getGeographicCoordinateSystem().getAuthority());
        assertEquals(4152, pcs.getGeographicCoordinateSystem().getAuthorityCode());
        assertEquals("Lambert_Conformal_Conic_2SP", pcs.getProjection().getClassName());

        ProjectionParameter latitude_of_origin = pcs.getProjection().getParameter("latitude_of_origin");
        assertNotNull(latitude_of_origin);
        assertEquals(29.6666666667, latitude_of_origin.getValue(), 0.00001d);
        ProjectionParameter central_meridian = pcs.getProjection().getParameter("central_meridian");
        assertNotNull(central_meridian);
        assertEquals(-100.333333333333, central_meridian.getValue(), 0.00001d);
        ProjectionParameter standard_parallel_1 = pcs.getProjection().getParameter("standard_parallel_1");
        assertNotNull(standard_parallel_1);
        assertEquals(31.883333333333, standard_parallel_1.getValue(), 0.00001d);
        ProjectionParameter standard_parallel_2 = pcs.getProjection().getParameter("standard_parallel_2");
        assertNotNull(standard_parallel_2);
        assertEquals(30.1166666667, standard_parallel_2.getValue(), 0.00001d);
        ProjectionParameter false_easting = pcs.getProjection().getParameter("false_easting");
        assertNotNull(false_easting);
        assertEquals(2296583.333, false_easting.getValue(), 0.00001d);
        ProjectionParameter false_northing = pcs.getProjection().getParameter("false_northing");
        assertNotNull(false_northing);
        assertEquals(9842500, false_northing.getValue(), 0.00001d);

        assertEquals("US survey foot", pcs.getLinearUnit().getName());
        assertEquals(0.304800609601219, pcs.getLinearUnit().getMetersPerUnit(), 0.00001d);
        assertEquals("EPSG", pcs.getLinearUnit().getAuthority());
        assertEquals(9003, pcs.getLinearUnit().getAuthorityCode());
        assertEquals("EPSG", pcs.getAuthority());
        assertEquals(2918, pcs.getAuthorityCode());
        assertEquals(wkt, pcs.getWKT());
    }

    /**
     * This test reads in a file with 2671 pre-defined coordinate systems and projections,
     * and tries to parse them.
     */
    @Test
    public final void parseAllWKTs() throws IOException {
        CoordinateSystemFactory fac = new CoordinateSystemFactory();
        int parsecount = 0;

        for (String wkt : SpatialReferences.getAllWkt()) {
            Object tempVar = CoordinateSystemWktReader.parse(wkt);
            ICoordinateSystem cs = (ICoordinateSystem) ((tempVar instanceof ICoordinateSystem) ? tempVar : null);
            assertNotNull("Could not parse WKT: " + wkt, cs);
            parsecount++;
        }

        assertEquals("Not all WKT was parsed", parsecount, 2671);
    }

    @Test
    public final void testTransformAllWKTs() throws IOException {

        CoordinateTransformationFactory fact = new CoordinateTransformationFactory();
        CoordinateSystemFactory fac = new CoordinateSystemFactory();

        int parseCount = 0;

        for (String wkt : SpatialReferences.getAllWkt()) {
            Object tempVar = CoordinateSystemWktReader.parse(wkt);
            ICoordinateSystem cs = (ICoordinateSystem) ((tempVar instanceof ICoordinateSystem) ? tempVar : null);

            if (cs == null) {
                continue; //We check this in another test.
            }

            if (cs instanceof IProjectedCoordinateSystem) {
                switch (((IProjectedCoordinateSystem) ((cs instanceof IProjectedCoordinateSystem) ? cs : null)).getProjection().getClassName()) {
                    //Skip not supported projections
                    case "Oblique_Stereographic":
                    case "Transverse_Mercator_South_Orientated":
                    case "Hotine_Oblique_Mercator":
                    case "Lambert_Conformal_Conic_1SP":
                    case "Krovak":
                    case "Cassini_Soldner":
                    case "Lambert_Azimuthal_Equal_Area":
                    case "Tunisia_Mining_Grid":
                    case "New_Zealand_Map_Grid":
                    case "Polyconic":
                    case "Lambert_Conformal_Conic_2SP_Belgium":
                    case "Polar_Stereographic":
                        continue;
                    default:
                        break;
                }
            }

            try {
                ICoordinateTransformation trans = fact.createFromCoordinateSystems(GeographicCoordinateSystem.getWGS84(), cs);
            } catch (RuntimeException ex) {
                if (cs instanceof IProjectedCoordinateSystem) {
                    fail("Could not create transformation from:\r\n" + wkt + "\r\n" + ex.getMessage() + "\r\nClass name:" + ((IProjectedCoordinateSystem) ((cs instanceof IProjectedCoordinateSystem) ? cs : null)).getProjection().getClassName());
                } else {
                    fail("Could not create transformation from:\r\n" + wkt + "\r\n" + ex.getMessage());
                }
            }
            parseCount++;
        }

        assertEquals("Not all WKT was processed", parseCount, 2536);
    }

    private boolean toleranceLessThan(double[] p1, double[] p2, double tolerance)
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
}