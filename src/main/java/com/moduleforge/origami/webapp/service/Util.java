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

import com.moduleforge.libraries.geometry._3d.Plane;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;
import com.whitebeluga.origami.figure.Bundle;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.component.Edge;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.PI;

public class Util {
   /**
   This tolerance value is so high because the polygons might have been slightly
    shifted in order to be displayed in the interface, even in the flat rendering.
    (See about layered rendering.)

    Besides it's quite robust when using two different points, there just won't be any
   plane that near both points
   */
   private static final double PLANE_POINT_DISTANCE_TOLERANCE = 1e-2;

   /**
    * The fold points in this case can be either picked points or docked points. It shouldn't make any difference.
    */
   public static Vector calculateNormalOfSideFacingCamera(Figure figure,
                                                          Point cameraPosition,
                                                          Point foldPoint1, Point foldPoint2) {
      Plane plane = findApproximatePlane(figure, foldPoint1, foldPoint2);
      return calculateNormalOfSideFacingCamera(plane, foldPoint1, cameraPosition);
   }
   public static Vector calculateNormalOfSideFacingCamera(Plane plane, Point pointOnPlane, Point cameraPosition){
      Vector planeNormal = plane.getNormal();
      Vector normalizedCameraVector = cameraPosition.sub(pointOnPlane).asVector().normalize();
      boolean sameDirection = planeNormal.angle(normalizedCameraVector) < PI/2;
      if(sameDirection)
         return planeNormal;
      return planeNormal.negate();
   }
   /**
    * We call this function "approximate" because we test bundles that match with the points within a bigger
    * epsilon of error than usual.
    */
   public static Bundle findApproximateBundle(Figure figure, Point p1, Point p2){
      Set<Bundle> bundles = figure.getBundles();
      List<Bundle> bundlesInSamePlane = new ArrayList<>();
      for(Bundle bundle : bundles) {
         Point closestP1 = bundle.getPlane().closestPoint(p1);
         if( closestP1.distance(p1) > PLANE_POINT_DISTANCE_TOLERANCE)
            continue;
         Point closestP2 = bundle.getPlane().closestPoint(p2);
         if(closestP2.distance(p2) < PLANE_POINT_DISTANCE_TOLERANCE )
            bundlesInSamePlane.add(bundle);
      }
      if(bundlesInSamePlane.isEmpty())
         throw new RuntimeException("plane was not found");
      if(bundlesInSamePlane.size() == 1)
         return bundlesInSamePlane.get(0);
      //for the same plane there could theoretically be more than one bundle (although it is not usually the case)
      for(Bundle bundle: bundlesInSamePlane)
         for(Edge edge : bundle.getEdges())
            if( edge.distanceFrom(p1) < PLANE_POINT_DISTANCE_TOLERANCE ) //just use same tolerance, why not
               return bundle;
      //shouldn't reach this line
      assert(false);
      throw new RuntimeException("No bundle found.");
   }
   public static Plane findApproximatePlane(Figure figure, Point p1, Point p2){
      Set<Plane> planes = figure.getPlanes();
      for(Plane plane : planes) {
         Point closestP1 = plane.closestPoint(p1);
         double distanceClosestP1 = closestP1.distance(p1);
         Point closestP2 = plane.closestPoint(p2);
         double distanceClosestP2 = closestP2.distance(p2);
         if( distanceClosestP1 < PLANE_POINT_DISTANCE_TOLERANCE && distanceClosestP2 < PLANE_POINT_DISTANCE_TOLERANCE )
            return plane;
      }
      throw new RuntimeException("plane was not found");
   }
   public static boolean isADarkColor(Color c) {
      return (c.getRed() + c.getGreen() + c.getBlue()) < 220; //crude approximation, good enough for our purposes
   }
}
