package resources;

public class Properties {

    // GIS Files (must be in .gpkg format)
    public static final String REGION_BOUNDARY = "region.boundary";
    public static final String NETWORK_BOUNDARY = "network.boundary";
    public static final String NETWORK_LINKS = "network.links";
    public static final String NETWORK_NODES = "network.nodes";
    public static final String COORDINATE_SYSTEM = "coordinate.system";

    // MATSim data (must be in .xml format)
    public static final String MATSIM_ROAD_NETWORK = "matsim.road.network";
    public static final String MATSIM_CAR_NETWORK = "matsim.car.network";
    public static final String MATSIM_TRANSIT_NETWORK = "matsim.transit.network";
    public static final String MATSIM_TRANSIT_SCHEDULE = "matsim.transit.schedule";

    // Other properties
    public static final String NUMBER_OF_THREADS = "number.of.threads";
    public static final String MAX_BIKE_SPEED = "max.bike.speed";
    public static final String DECAY_PERCENTILE = "decay.percentile";

}
