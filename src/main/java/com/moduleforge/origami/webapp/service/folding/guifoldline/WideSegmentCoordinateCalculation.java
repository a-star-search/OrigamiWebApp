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

import com.moduleforge.libraries.geometry._3d.Line;
import com.moduleforge.libraries.geometry._3d.Plane;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;
import java.util.HashMap;
import java.util.Map;
import static com.moduleforge.libraries.geometry._3d.Line.linePassingBy;

public class WideSegmentCoordinateCalculation {

   /**
    * This method certainly requires an explanation (method's name is probably not enough).
    *
    * Imagine a segment, and that segment is embedded in a plane and that plane is in 3D space.
    *
    * Imagine that segment as a thick line, instead of a theoretical
    * line of no width.
    *
    * That "wide" segment is actually a rectangle defined by four points in a 3D space.
    *
    * The rectangle is embedded in the same
    * plane as the line.
    *
    */
   public static WideSegment calculateWideSegmentCoordinates(Plane plane, Point p1, Point p2, double segmentWidth){
      Line line = linePassingBy(p1, p2);
      Point randomPointOnPlane = getRandomPointOnPlaneFarEnoughFromLine(plane, p1, line);
      Vector shift = calculateShiftVector(line, randomPointOnPlane, segmentWidth/2);
      return calculateWideSegmentCoordinates(p1, p2, shift);
   }
   private static WideSegment calculateWideSegmentCoordinates(Point p1, Point p2, Vector shift) {
      Map<Point, WideSegmentEnd> result = new HashMap<>();
      result.put(p1, calculateWideSegmentEndCoordinates(p1, shift) );
      result.put(p2, calculateWideSegmentEndCoordinates(p2, shift) );
      return new WideSegment(result);
   }
   /**
    * Refer to the similarly named method's documentation to understand this
    */
   private static WideSegmentEnd calculateWideSegmentEndCoordinates(Point segmentEnd, Vector shift ){
      Vector shiftOppositeDirection = shift.negate();
      return new WideSegmentEnd(segmentEnd.translate(shift), segmentEnd.translate(shiftOppositeDirection));
   }
   /**
    * A vector that applied to the segment's ends will help compute the four points of the "wide" segment
    * by moving from each segment end in either direction using the vector
    *
    * Direction of vector returned is of no importance in this function.
    */
   private static Vector calculateShiftVector(Line line, Point randomPointOnPlane, double vectorLength) {
      Point randomPointOnPlaneAndInLine = line.closestPoint(randomPointOnPlane);
      return randomPointOnPlane.vectorTo(randomPointOnPlaneAndInLine).withLength(vectorLength);
   }
   private static Point getRandomPointOnPlaneFarEnoughFromLine(Plane plane, Point arbitraryPointInTheLine, Line line) {
      double inc = 1;
      Point aRandomPoint = new Point(arbitraryPointInTheLine.x() + inc,
              arbitraryPointInTheLine.y() + inc, arbitraryPointInTheLine.z() + inc);
      Point randomPointOnPlane = plane.closestPoint(aRandomPoint);
      if(line.contains(randomPointOnPlane))
         //If we chose a point whose perpendicular to the plane falls exactly on the line
         //then recursive call with a different point. I think it should do the trick
         return getRandomPointOnPlaneFarEnoughFromLine(plane,
                 new Point(aRandomPoint.x(), aRandomPoint.y() + inc, aRandomPoint.z() + 2*inc), line);
      return randomPointOnPlane;
   }
}
