package resources;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Resources {

    public static Resources instance;
    private final Properties properties;
    private final Path baseDirectory;

    private Resources(Properties properties, String baseDirectory) {
        this.properties = properties;
        this.baseDirectory = Paths.get(baseDirectory).getParent();
    }

    public static void initializeResources(String propertiesFile) {
        try (FileInputStream in = new FileInputStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);
            instance = new Resources(properties, propertiesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String getString(String key) {
        return properties.getProperty(key);
    }

    public synchronized int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public synchronized File getFile(String key) {
        return new File(properties.getProperty(key));
    }

    public synchronized double getDouble(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }

    public synchronized double getMarginalCost(String mode, String type) {
        return getDouble("mc." + mode + "." + type);
    }





}
