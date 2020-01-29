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

package com.moduleforge.origami.webapp.service.folding.guifoldline;

import com.moduleforge.libraries.geometry._3d.LineSegment;
import com.moduleforge.libraries.geometry._3d.Plane;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;
import com.moduleforge.origami.webapp.guidata.input.GUIFoldLine;
import com.moduleforge.origami.webapp.guidata.input.figure.BabylonJSRibbonPolygon;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.component.Face;
import com.whitebeluga.origami.rendering.OneSidedPolygon;
import org.javatuples.Pair;

import java.util.*;

import static com.moduleforge.origami.webapp.service.Util.calculateNormalOfSideFacingCamera;
import static com.moduleforge.origami.webapp.service.Util.findApproximatePlane;
import static com.moduleforge.origami.webapp.service.folding.guifoldline.WideSegmentCoordinateCalculation.calculateWideSegmentCoordinates;
import static com.moduleforge.origami.webapp.service.folding.guifoldline.WideSegmentToWideDashes.makeDashes;

/**
 * The algorithm to make something as deceptively simple as the fold line requires several different
 * more or less complicated calculations, so it is encapsulated into this class.
 *
 * Transforms a segment (which is a line with no width) into a "wide" segment to be displayed on the GUI,
 * that is, into a rectangle
 *
 */
/*
 * In babylon's coordinate system, the z axis goes in a different direction as in my own system in the origami
 * library.
 *
 * If the viewer would be facing the XY plane, so that X grows to the right and Y grows upwards, then, in babylon js,
 * Z grows away from the viewer.
 *
 * I believe that's equivalent of using the application "through a mirror", which wouldn't change anything.
 *
 * So, I believe this is not a problem for the application. But it's worth keeping it in mind
 * as a possible source of errors.
 */
public class GUIFoldLineMaker {
   private static final double FOLD_LINE_WIDTH_TO_PAPER_SIDE_LENGTH_RATIO = 1.0/200.0;
   private Figure figure;
   private double dashWidth;
   private double dashLength;

   public GUIFoldLineMaker(Figure figure, double originalPaperSideLength) {
      this.figure = figure;
      dashWidth = FOLD_LINE_WIDTH_TO_PAPER_SIDE_LENGTH_RATIO * originalPaperSideLength;
      dashLength = dashWidth * 4;
   }
   /**
    * The first element in the pair is the one facing the camera (and thus, with the type of fold
    * passed as parameter.
    *
    * A pair of lists of ribbons is easy to process on the frontend, which doesn't even need to know
    * which list of ribbons is mountain and which is valley. It just needs to draw them.
    */
   public GUIFoldLine makeGUIFoldLineRibbons(Point p1, Point p2, LineFoldType foldDirection, Point cameraPosition,
                                             Map<Face, Set<OneSidedPolygon>> originalFaceToPolygons){

      Pair<WideSegment, WideSegment> wideSegments =
              makeGUIFoldLinePoints(figure, p1, p2, dashWidth, cameraPosition, originalFaceToPolygons);

      List<WideDash> facingSideDashes = makeDashes(foldDirection, wideSegments.getValue0(), dashLength);
      List<BabylonJSRibbonPolygon> facingSideRibbonDashes = convertWideSegmentPointsToRibbons(facingSideDashes);

      List<WideDash> hiddenSideDashes = makeDashes(foldDirection.opposite(), wideSegments.getValue1(), dashLength);
      List<BabylonJSRibbonPolygon> hiddenSideRibbonDashes = convertWideSegmentPointsToRibbons(hiddenSideDashes);
      return new GUIFoldLine(facingSideRibbonDashes, hiddenSideRibbonDashes);
   }
   /**
    * Same as the homonymous function but a more readable return type
    *
    * First in the pair returned is the facing side, second is the hidden side
    */
   private static Pair<WideSegment, WideSegment> makeGUIFoldLinePoints(
           Figure figure, Point p1, Point p2, double width, Point cameraPosition, Map<Face,
           Set<OneSidedPolygon>> originalFaceToPolygons){
      Plane plane = findApproximatePlane(figure, p1, p2);
      Vector facingSideShiftNormalVector = calculateNormalOfSideFacingCamera(plane, p1, cameraPosition);
      float shiftDistance  = calculateShiftDistance(figure, plane, p1, p2, originalFaceToPolygons);
      Vector facingSideShiftVector = facingSideShiftNormalVector.scale(shiftDistance);
      Vector hiddenSideShiftVector = facingSideShiftVector.negate();
      WideSegment wideSegment = calculateWideSegmentCoordinates(plane, p1, p2, width);
      return new Pair<>(shiftWideSegment(wideSegment, facingSideShiftVector), shiftWideSegment(wideSegment, hiddenSideShiftVector));
   }
   /**
    * Finds the distance from the plane to that face that is the farthest away from it.
    * That distance will be applied to both sides.
    */
   private static float calculateShiftDistance(Figure figure, Plane plane, Point p1, Point p2,
                                               Map<Face, Set<OneSidedPolygon>> originalFaceToPolygons) {
      LineSegment foldSegment = new LineSegment(p1, p2);
      double maxDistance = 0.0;
      for(Face face : figure.getFaces())
         if(face.intersectedAcrossBy(foldSegment)) {
            Set<OneSidedPolygon> polygons = originalFaceToPolygons.get(face);
            if(polygons != null)
               for(OneSidedPolygon polygon : polygons) {
                  Point arbitraryPoint = polygon.getBoundary().get(0);
                  double distance = plane.distanceFrom(arbitraryPoint);
                  if(distance > maxDistance)
                     maxDistance = distance;
               }
         }
      return (float)(maxDistance) * 1.03f; //  3% more for good measure, just a little more so it doesn't get hidden by the face
   }

   private static WideSegment shiftWideSegment(WideSegment wideSegment, Vector shiftVector){
      Map<Point, WideSegmentEnd> shifted = new HashMap<>();
      for (Map.Entry<Point, WideSegmentEnd> end : wideSegment.ends.entrySet()){
         Pair<Point, Point> points = end.getValue().points;
         Pair<Point, Point> shiftedPoints = new Pair<>(
                 shiftPoint(points.getValue0(), shiftVector), shiftPoint(points.getValue1(), shiftVector));
         WideSegmentEnd shiftedWideSegment = new WideSegmentEnd(shiftedPoints);
         shifted.put(end.getKey(), shiftedWideSegment);
      }
      return new WideSegment(shifted);
   }
   private static Point shiftPoint(Point point, Vector shift){
      Point shiftAsPoint = new Point(shift);
      return point.add(shiftAsPoint);
   }
   private static List<BabylonJSRibbonPolygon> convertWideSegmentPointsToRibbons(List<WideDash> dashes){
      List<BabylonJSRibbonPolygon> ribbons = new ArrayList<>();
      for(WideDash dash : dashes){
         //order of the "ends" is not really important as long as the connections are correct
         //the "ends" boundary do have an order that allows to connect all the boundary correctly
         Pair<WideSegmentEnd, WideSegmentEnd> ends = dash.ends;
         List<Point> pointList = new ArrayList<>();
         pointList.add(ends.getValue0().points.getValue0());
         pointList.add(ends.getValue0().points.getValue1());
         pointList.add(ends.getValue1().points.getValue1());
         pointList.add(ends.getValue1().points.getValue0());
         ribbons.add(new BabylonJSRibbonPolygon(pointList));
      }
      return ribbons;
   }
}
