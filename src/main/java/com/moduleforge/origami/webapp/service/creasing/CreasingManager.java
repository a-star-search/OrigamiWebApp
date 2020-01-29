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

package com.moduleforge.origami.webapp.service.creasing;

import com.moduleforge.libraries.geometry._3d.LineSegment;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;
import com.moduleforge.origami.webapp.guidata.input.figure.BabylonJSCreaseSet;
import com.moduleforge.origami.webapp.guidata.output.FoldEdge;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;

import static com.moduleforge.origami.webapp.service.Util.calculateNormalOfSideFacingCamera;
import static com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold.ALL;

public class CreasingManager {
   public static Figure creaseFigure(Figure figure, Point p1, Point p2, Point cameraPosition, FlapsToFold ftf) {
      LineSegment creaseSegment = new LineSegment(p1, p2);
      Vector normalOfSideFacingCamera = calculateNormalOfSideFacingCamera(figure, cameraPosition, p1, p2);
      Vector guiUserLookingDirection = normalOfSideFacingCamera.negate();
      if(ftf.equals(ALL))
         return figure.doAMAPCrease(creaseSegment, guiUserLookingDirection);
      return figure.doFirstFlapCrease(creaseSegment, guiUserLookingDirection); //TODO get the picked point if necessary
   }
   public static BabylonJSCreaseSet makeGUICreasePoints(Figure figure, FoldEdge edge1, FoldEdge edge2,
                                                        Point cameraPosition, FlapsToFold ftf) {
      //TODO...
      return null;
   }
   public static BabylonJSCreaseSet makeGUICreasePoints(Figure figure, Point p1, Point p2,
                                                        Point cameraPosition, FlapsToFold ftf) {
      //TODO...
      return null;
   }
}
