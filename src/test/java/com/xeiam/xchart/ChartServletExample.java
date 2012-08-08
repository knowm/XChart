/**
 * Copyright 2011 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates, stores, and serves charts
 * 
 * @author timmolter
 */
@javax.servlet.annotation.WebServlet(name = "ChartServlet", urlPatterns = { "/chart" }, asyncSupported = true)
public class ChartServletExample extends HttpServlet {

  private static Map<String, Chart> chartMap = new HashMap<String, Chart>();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {

    String id = request.getParameter("id");

    Chart chart = chartMap.get(id);

    response.setContentType("image/png");
    ServletOutputStream out = response.getOutputStream();

    try {
      BitmapEncoder.streamPNG(out, chart);
    } catch (Exception e) {
      e.printStackTrace();
    }

    out.close();

    chart = null;

    chartMap.remove(id);
  }

  /**
   * Generates a chart and puts it in chartMap
   * 
   * @return UUID uniquely identifying chart in the chartMap
   */
  public static String generateDummyChart() {

    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", null, null, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
    String uuid = UUID.randomUUID().toString();
    chartMap.put(uuid, chart);

    return uuid;
  }
}
