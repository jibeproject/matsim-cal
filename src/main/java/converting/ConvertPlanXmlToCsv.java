package converting;

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

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.utils.misc.OptionalTime;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class ConvertPlanXmlToCsv {

    private static final String CSV_HEADER = "Person ID,Element Type,Activity Type,Link,X Coordinate,Y Coordinate," +
            "End Time,Mode,Dep Time,Trav Time,Route Type,Start Link,End Link,Route Trav Time,Distance, Route";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(" Program requires two arguments:" +
                    "1) input xml file path" +      // ./output_plans.xml
                    "2) output csv file path");     // ./output_plans.csv
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];

        Config config = ConfigUtils.createConfig();
        Scenario scenario = ScenarioUtils.createScenario(config);
        PopulationReader populationReader = new PopulationReader(scenario);
        populationReader.readFile(inputFilePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            writer.println(CSV_HEADER);
            for (Person person : scenario.getPopulation().getPersons().values()) {
                Plan selectedPlan = person.getSelectedPlan();
                if (selectedPlan != null) {
                    processPlan(writer, person.getId().toString(), selectedPlan);
                }
            }
            System.out.println("Finished writing to CSV");
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    private static void processPlan(PrintWriter writer, String personId, Plan plan) {
        for (PlanElement element : plan.getPlanElements()) {
            if (element instanceof Activity) {
                processActivity(writer, personId, (Activity) element);
            } else if (element instanceof Leg) {
                processLeg(writer, personId, (Leg) element);
            }
        }
    }

    private static void processActivity(PrintWriter writer, String personId, Activity activity) {
        writer.printf("%s,Activity,%s,%s,%s,%s,%s,,,,,,,,,,\n",
                personId,
                activity.getType(),
                activity.getLinkId(),
                activity.getCoord().getX(),
                activity.getCoord().getY(),
                formatOptionalTime(activity.getEndTime()));
    }

    private static void processLeg(PrintWriter writer, String personId, Leg leg) {
        Route route = leg.getRoute();
        writer.printf("%s,Leg,,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,\n",
                personId,
                leg.getMode(),
                formatOptionalTime(leg.getDepartureTime()),
                formatOptionalTime(leg.getTravelTime()),
                route.getRouteType(),
                route.getStartLinkId(),
                route.getEndLinkId(),
                formatOptionalTime(route.getTravelTime()),
                route.getDistance(),
                route.getRouteDescription());
    }

    private static String formatOptionalTime(OptionalTime optionalTime) {
        if (optionalTime.isDefined()) {
            long seconds = (long) optionalTime.seconds();
            return formatSecondsToHHMMSS(seconds);
        } else {
            return "";
        }
    }

    private static String formatSecondsToHHMMSS(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
