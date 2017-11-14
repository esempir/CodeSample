/**
 * Created by rh727 on 10/1/17.
 */

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

public class TrainStationTravelTime {

    /**
     * The HashMap stores a route with its commute time.
     */
    static HashMap<CommuteRoute, Integer> map = new HashMap<>();

    /**
     * CommuteRoute represents a route between start and end.
     */
    static class CommuteRoute {
        String start;
        String end;

        public CommuteRoute(String startIn, String endIn) {
            start = startIn;
            end = endIn;
        }

        @Override
        public boolean equals(Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(!(o instanceof CommuteRoute))
            {
                return false;
            }
            CommuteRoute commuteRoute = (CommuteRoute) o;
            return commuteRoute.start.equals(start) && commuteRoute.end.equals(end);
        }

        @Override
        public int hashCode()
        {
            int result = 17;
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            return result;
        }
    }

    public static void main(String[] args) throws IOException {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, Charset.defaultCharset()))) {

            String line;
            int n = -1, m = -1;

            while ((line = reader.readLine()) != null) {

                map.clear();
                n = Integer.parseInt(line); // number of stations

                HashSet<String> set = new HashSet<>();
                String[] stations = new String[n];// station names array
                int count = 0;

                // input travel time from A to B: (Ai, Bi, Ti)
                for (int i = 0; i < n - 1; i++) {
                    line = reader.readLine();
                    String[] tuple = line.split(" ");

                    // init HashMap
                    map.put(new CommuteRoute(tuple[0], tuple[0]), 0);
                    map.put(new CommuteRoute(tuple[1], tuple[1]), 0);
                    map.put(new CommuteRoute(tuple[0], tuple[1]), Integer.parseInt(tuple[2]));

                    if (!set.contains(tuple[0])) {
                        set.add(tuple[0]);
                        stations[count++] = tuple[0];
                    }
                    if (!set.contains(tuple[1])) {
                        set.add(tuple[1]);
                        stations[count++] = tuple[1];
                    }
                }


                // processing commute time with Floyd
                for (int t = 0; t < n; t++) {
                    for (int i = 0; i < n; i++) {
                        if (t == i) {
                            continue;
                        }
                        for (int j = 0; j < n; j++) {
                            if (t == j || i == j) {
                                continue;
                            }
                            String start = stations[i];
                            String end = stations[j];

                            String transfer = stations[t];
                            if (containsRoute(start, transfer) && containsRoute(transfer, end)) {
                                int time = -1;
                                if (containsRoute(start, end)) {
                                    // if this is the best transfer
                                    if (getCommuteTime(start, transfer) >= getCommuteTime(start, end) || getCommuteTime(transfer, end) >= getCommuteTime(start, end)){
                                        continue;
                                    } else {
                                        time = Math.min(getCommuteTime(start, transfer) + getCommuteTime(transfer, end), getCommuteTime(start, end));
                                    }
                                } else {
                                    time = getCommuteTime(start, transfer) + getCommuteTime(transfer, end);
                                }
                                map.put(new CommuteRoute(start, end), time);
                            }
                        }
                    }
                }


                // output
                line = reader.readLine();
                m = Integer.parseInt(line);

                for (int i = 0; i < m; i++) {
                    line = reader.readLine();
                    String[] tuple = line.split(" ");
                    System.out.println(getCommuteTime(tuple[0], tuple[1]));
                }

            }


            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If HashMap contains route between startIn and endIn.
     * @param startIn One station of the route.
     * @param endIn Another station of the route.
     * @return True or false.
     */
    public static boolean containsRoute(String startIn, String endIn) {
        if (map.containsKey(new CommuteRoute(startIn, endIn)) || map.containsKey(new CommuteRoute(endIn, startIn))) {
            return true;
        }
        return false;
    }

    /**
     * Get the commute time of a route between startIn and endIn.
     * @param startIn One station of the route.
     * @param endIn Another station of the route.
     * @return The commute time.
     */
    public static int getCommuteTime(String startIn, String endIn) {
        if (map.containsKey(new CommuteRoute(startIn, endIn))) {
            return map.get(new CommuteRoute(startIn, endIn));
        } else if (map.containsKey(new CommuteRoute(endIn, startIn))) {
            return map.get(new CommuteRoute(endIn, startIn));
        }
        return -1;
    }

}

