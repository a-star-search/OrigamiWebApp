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

package com.moduleforge.origami.webapp.service;

import com.moduleforge.libraries.geometry._3d.LineSegment;
import com.moduleforge.libraries.geometry._3d.Plane;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;
import com.whitebeluga.origami.figure.Bundle;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.component.Face;
import com.whitebeluga.origami.rendering.OneSidedPolygon;
import com.whitebeluga.origami.rendering.VisibleBundle;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.moduleforge.libraries.geometry.Geometry.facingEachOther;
import static com.moduleforge.origami.webapp.service.Util.calculateNormalOfSideFacingCamera;
import static com.moduleforge.origami.webapp.service.Util.isADarkColor;

public class VisibleEdgesCalculator {
   /**
    * First element of the pair is all visibleEdges, second is all visibleEdges with a dark color
    */
   public static Pair<Set<LineSegment>, Set<LineSegment>> calculateVisibleEdges(Figure figure, Point cameraPosition) {
      Set<LineSegment> segments = new HashSet<>();
      Set<LineSegment> darkSegments = new HashSet<>();
      for(Bundle bundle : figure.getBundles()){
         Plane plane = bundle.getPlane();
         Face randomFace = bundle.getFaces().iterator().next();
         Point randomPoint = randomFace.getVertices().get(0);
         Vector towardsObserver = calculateNormalOfSideFacingCamera(plane, randomPoint, cameraPosition);
         Vector normal = bundle.getNormal();
         boolean lookingAtTheTopOfLayer = facingEachOther(towardsObserver, normal);
         Set<OneSidedPolygon> visiblePolygons;
         //I know, the condition seems to be the other way around, but this is the way it actually works, whatevs
         if(lookingAtTheTopOfLayer)
            visiblePolygons = new VisibleBundle(bundle).getVisiblePolygonsFromBottom();
         else
            visiblePolygons = new VisibleBundle(bundle).getVisiblePolygonsFromTop();
         Pair<Set<LineSegment>, Set<LineSegment>> layerSegments = extractSegments(visiblePolygons);
         segments.addAll(layerSegments.getValue0());
         darkSegments.addAll(layerSegments.getValue1());
      }
      return new Pair(segments, darkSegments);
   }
   private static Pair<Set<LineSegment>, Set<LineSegment>> extractSegments(Set<OneSidedPolygon> polygons) {
      Set<LineSegment> segments = new HashSet<>();
      Set<LineSegment> darkSegments = new HashSet<>();
      for(OneSidedPolygon polygon : polygons){
         Pair<Set<LineSegment>, Set<LineSegment>> segmentsOfPol = extractSegments(polygon);
         segments.addAll(segmentsOfPol.getValue0());
         darkSegments.addAll(segmentsOfPol.getValue1());
      }
      return new Pair(segments, darkSegments);
   }
   private static Pair<Set<LineSegment>, Set<LineSegment>> extractSegments(OneSidedPolygon polygon) {
      Set<LineSegment> segments = new HashSet<>();
      List<Point> points = polygon.getBoundary();
      for(int i = 0; i < (points.size() - 1); i ++){
         LineSegment s = new LineSegment(points.get(i), points.get(i + 1));
         segments.add(s);
      }
      LineSegment last = new LineSegment(points.get(0), points.get(points.size() - 1));
      segments.add(last);
      Set<LineSegment> darkSegments;
      if(isADarkColor(polygon.getColor()))
         darkSegments = segments;
      else
         darkSegments = new HashSet<>();
      return new Pair(segments, darkSegments);
   }
}
