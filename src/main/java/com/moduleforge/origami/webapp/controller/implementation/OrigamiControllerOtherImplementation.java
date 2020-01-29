/*
 *    This file is part of "Origami".
 *
 *     Origami is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Origami is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Origami.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright (c) 2018.  This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */

package com.moduleforge.origami.webapp.controller.implementation;

import com.moduleforge.libraries.geometry._3d.LineSegment;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.guidata.input.figure.FigureData;
import com.moduleforge.origami.webapp.service.OrigamiManager;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.OrigamiBase;
import org.javatuples.Pair;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.moduleforge.origami.webapp.controller.OrigamiControllerUtil.asString;
import static com.moduleforge.origami.webapp.controller.OrigamiControllerUtil.makeFigureDataMapWithoutSnapshots;

public class OrigamiControllerOtherImplementation {
   private OrigamiManager manager = null;

   public OrigamiControllerOtherImplementation(OrigamiManager manager) {
      this.manager = manager;
   }
   public FigureData getFigureData() {
      return manager.getOrMakeAndUpdateFigureData();
   }
   public String reset() {
      manager.reset();
      return "";
   }
   public Map<String, Object> undoLastStep() {
      Figure previous = manager.undoLastStep();
      FigureData data = new FigureData(previous);
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("flat", data.getFlatFigure());
      responseBody.put("threed", data.getThreeDFigure());
      responseBody.put("visibleEdges", data.getSegments());
      boolean thereIsNoHistory = manager.thereIsNoHistory();
      responseBody.put("nohistory", thereIsNoHistory);
      return responseBody;
   }
   public String baseLoaded(String baseName) {
      for (OrigamiBase base : OrigamiBase.values())
         if(base.toString().equalsIgnoreCase(baseName))
            manager.baseLoaded(base);
      return "";
   }
   public String squareLoaded() {
      manager.squareLoaded();
      return "";
   }
   public Map<String, String> recalculateVisibleEdges(Point cameraPosition) throws IOException {
      Pair<Set<LineSegment>, Set<LineSegment>> segments = manager.recalculateVisibleEdges(cameraPosition);
      Map<String, String> responseBody = new HashMap<>();
      String all = asString(segmentsToPoints(segments.getValue0()));
      responseBody.put("all", all);
      String dark = asString(segmentsToPoints(segments.getValue1()));
      responseBody.put("dark", dark);
      return responseBody;
   }
   /**
    * A set of segments is transformed into a list of list of points. Each list of points is made of two points
    * (the two ends of the segments).
    *
    * The purpose of this transformation is to pass the simplest possible structure to the frontend.
    */
   private static List<List<Point>> segmentsToPoints(Set<LineSegment> segments){
      List<List<Point>> points = new ArrayList<>();
      for(LineSegment segment3D : segments){
         Pair<Point, Point> ends = segment3D.getPoints();
         List<Point> segmentPoints = new ArrayList<>();
         segmentPoints.add(new Point(ends.getValue0()));
         segmentPoints.add(new Point(ends.getValue1()));
         points.add(segmentPoints);
      }
      return points;
   }
   public Map<String, Object> rotateFigure(double angle) {
      FigureData figureData = manager.rotateFigureAroundZAxis(angle);
      return makeFigureDataMapWithoutSnapshots(figureData);
   }
   public Map<String, Object> updateFaceColor(Color color, int index) {
      FigureData data = manager.updateFaceColor(index, color);
      return makeFigureDataMapWithoutSnapshots(data);
   }
   public Map<String,Object> thereIsNoHistory() {
      boolean thereIsNoHistory = manager.thereIsNoHistory();
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("thereisnohistory", thereIsNoHistory);
      return responseBody;
   }
}
