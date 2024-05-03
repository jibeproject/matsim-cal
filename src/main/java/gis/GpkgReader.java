package gis;

/*-
 * #%L
 * Example Project
 * %%
 * Copyright (C) 2020 - 2024 by its authors.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import resources.Properties;
import resources.Resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Tools for reading edges and nodes files produced by the JIBE WP2 team

public class GpkgReader {

    public static Map<Integer, SimpleFeature> readNodes() {

        final File nodesFile = Resources.instance.getFile(Properties.NETWORK_NODES);

        Map<Integer,SimpleFeature> nodes = new HashMap<>();

        try{
            GeoPackage geopkg = new GeoPackage(nodesFile);
            SimpleFeatureReader r = geopkg.reader(geopkg.features().get(0), null,null);
            while(r.hasNext()) {
                SimpleFeature node = r.next();
                nodes.put((int) node.getAttribute("nodeID"),node);
            }
            r.close();
            geopkg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodes;

    }

    public static Map<Integer, SimpleFeature> readEdges() {

        File edgesFile = Resources.instance.getFile(Properties.NETWORK_LINKS);

        Map<Integer,SimpleFeature> edges = new HashMap<>();

        try{
            GeoPackage geopkg = new GeoPackage(edgesFile);
            SimpleFeatureReader r = geopkg.reader(geopkg.features().get(0), null,null);
            while(r.hasNext()) {
                SimpleFeature edge = r.next();
                edges.put((int) edge.getAttribute("edgeID"),edge);
            }
            r.close();
            geopkg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return edges;

    }

    public static Geometry readRegionBoundary() throws IOException {
        return readBoundary(Resources.instance.getString(Properties.REGION_BOUNDARY));
    }

    public static Geometry readNetworkBoundary() throws IOException {
        return readBoundary(Resources.instance.getString(Properties.NETWORK_BOUNDARY));
    }

    public static Geometry readBoundary(String filePath) throws IOException {
        GeoPackage geopkg = new GeoPackage(openFile(filePath));
        SimpleFeatureReader r = geopkg.reader(geopkg.features().get(0), null,null);
        SimpleFeature f = r.next();
        Geometry boundary = (Geometry) f.getDefaultGeometry();
        r.close();
        geopkg.close();
        return boundary;
    }

    private static File openFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            throw new RuntimeException("File " + filePath + " not found!");
        }
        return file;
    }

}
