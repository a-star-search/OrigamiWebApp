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

import com.moduleforge.libraries.geometry._3d.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType.MOUNTAIN;
import static com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType.VALLEY;

class WideSegmentToWideDashes {
   private static final double GAP_RATIO = 0.33;

   public static List<WideDash> makeDashes(LineFoldType foldType, WideSegment segment, double dashLength){
      List<Point> lineSegmentPoints = new ArrayList<>(segment.ends.keySet());
      Point startPoint = lineSegmentPoints.get(0);
      WideSegmentEnd end = segment.getEnd(startPoint);
      Point startEndPoint1 = end.points.getValue0();
      Point startEndPoint2 = end.points.getValue1();
      end = segment.getEnd(lineSegmentPoints.get(1));
      Point endEndPoint1 = end.points.getValue0();
      Point endEndPoint2 = end.points.getValue1();
      double segmentLength = startEndPoint1.distance(endEndPoint1);
      return makeDashes(foldType, dashLength, startEndPoint1, startEndPoint2, endEndPoint1, endEndPoint2, segmentLength);
   }
   private static List<WideDash> makeDashes(LineFoldType foldType, double dashLength,
                                            Point startEndPoint1, Point startEndPoint2,
                                            Point endEndPoint1, Point endEndPoint2, double segmentLength) {
      if(foldType.equals(MOUNTAIN)){
         double segmentWidth = startEndPoint1.distance(startEndPoint2);
         return makeMountainDashes(dashLength, startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2, segmentLength, segmentWidth);
      }
      if(foldType.equals(VALLEY))
         return makeValleyDashes(dashLength, startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2, segmentLength);
      throw new RuntimeException("unknown or wrong fold type");
   }
   private static List<WideDash> makeValleyDashes(double dashLength,
                                                  Point startEndPoint1, Point endEndPoint1,
                                                  Point startEndPoint2, Point endEndPoint2, double segmentLength){
      List<WideDash> dashes = new ArrayList<>();
      int dashesMade = 0;
      double repetitionPatternLength = dashLength * (1.0 + GAP_RATIO);
      double ratioStartDash;
      while(canMakeAnotherDash(segmentLength, dashesMade, repetitionPatternLength)){
         ratioStartDash = (repetitionPatternLength * dashesMade) / segmentLength;
         double ratioEndDash = ratioStartDash + (dashLength / segmentLength);
         WideDash dash = makeDash(startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2, ratioStartDash, ratioEndDash);
         dashes.add(dash);
         dashesMade++;
      }
      WideDash lastDash = makeDash(startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2,
              (repetitionPatternLength * dashesMade) / segmentLength, 1.0);
      dashes.add(lastDash);
      return dashes;
   }
   private static List<WideDash> makeMountainDashes(double dashLength,
                                                    Point startEndPoint1, Point endEndPoint1,
                                                    Point startEndPoint2, Point endEndPoint2,
                                                    double segmentLength, double segmentWidth){
      List<WideDash> dashes = new ArrayList<>();
      int i = 0;
      double repetitionPatternLength = dashLength * (1.0 + (GAP_RATIO * 2)) + segmentWidth;
      double ratioStartDash;
      while(canMakeAnotherDash(segmentLength, i, repetitionPatternLength)){
         ratioStartDash = (repetitionPatternLength * i) / segmentLength;
         double ratioEndDash = ratioStartDash + (dashLength / segmentLength);
         //make the dash
         WideDash dash = makeDash(startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2, ratioStartDash, ratioEndDash);
         dashes.add(dash);
         //make the dot
         double ratioStartDot = ratioEndDash + (dashLength * GAP_RATIO) / segmentLength;
         double ratioEndDot = ratioStartDot + (segmentWidth / segmentLength); //dot length == segment width
         dash = makeDash(startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2, ratioStartDot, ratioEndDot);
         dashes.add(dash);
         i++;
      }
      WideDash lastDash = makeDash(startEndPoint1, endEndPoint1, startEndPoint2, endEndPoint2,
              (repetitionPatternLength * i) / segmentLength, 1.0);
      dashes.add(lastDash);
      return dashes;
   }
   @NotNull
   private static WideDash makeDash(Point startEndPoint1, Point endEndPoint1, Point startEndPoint2, Point endEndPoint2,
                                    double ratioStartDash, double ratioEndDash) {
      Point p11 = pointWithinSegment(startEndPoint1, endEndPoint1, ratioStartDash);
      Point p12 = pointWithinSegment(startEndPoint2, endEndPoint2, ratioStartDash);
      WideSegmentEnd end1 = new WideSegmentEnd (p11, p12);
      Point p21 = pointWithinSegment(startEndPoint1, endEndPoint1, ratioEndDash);
      Point p22 = pointWithinSegment(startEndPoint2, endEndPoint2, ratioEndDash);
      WideSegmentEnd end2 = new WideSegmentEnd (p21, p22);
      return new WideDash(end1, end2);
   }
   private static boolean canMakeAnotherDash(double segmentLength, int dashesMade, double dashPatternLength) {
      return (dashPatternLength * (dashesMade + 1)) < segmentLength;
   }
   private static Point pointWithinSegment(Point first, Point second, double ratioFromFirst){
      double deltaX = (second.x() - first.x()) * ratioFromFirst;
      double deltaY = (second.y() - first.y()) * ratioFromFirst;
      double deltaZ = (second.z() - first.z()) * ratioFromFirst;
      return first.add(new Point(deltaX, deltaY, deltaZ));
   }
}
