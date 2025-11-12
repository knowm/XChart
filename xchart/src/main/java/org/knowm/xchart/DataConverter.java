package org.knowm.xchart;

import java.util.ArrayList;
import java.util.List;

public class DataConverter {
    /**
     * @param stringData
     * @return
     */
    public static List<Number> getAxisData(String stringData) {

        List<Number> axisData = new ArrayList<Number>();
        String[] stringDataArray = stringData.split(",");
        for (String dataPoint : stringDataArray) {
            try {
                Double value = Double.parseDouble(dataPoint);
                axisData.add(value);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing >" + dataPoint + "< !");
                throw (e);
            }
        }
        return axisData;
    }

}
