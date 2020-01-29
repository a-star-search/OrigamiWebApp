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

import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.controller.OrigamiControllerUtil;
import com.moduleforge.origami.webapp.guidata.input.figure.BabylonJSCreaseSet;
import com.moduleforge.origami.webapp.guidata.input.figure.FigureData;
import com.moduleforge.origami.webapp.guidata.output.FoldEdge;
import com.moduleforge.origami.webapp.service.OrigamiManager;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;

import java.util.HashMap;
import java.util.Map;

public class OrigamiControllerCreasingImplementation {
   private OrigamiManager manager = null;

   public OrigamiControllerCreasingImplementation(OrigamiManager manager) {
      this.manager = manager;
   }

   public Map<String, Object> makeGUICreaseFromPoints(Point p1, Point p2, Point cameraPosition, boolean isAMAPFold) {
      FlapsToFold ftf = OrigamiControllerUtil.flapsToFold(isAMAPFold);
      Map<String, Object> responseBody = new HashMap<>();
      BabylonJSCreaseSet guiCrease = manager.makeGUICreasePoints(p1, p2, cameraPosition, ftf);
      responseBody.put("creases", guiCrease.creases);
      return responseBody;
   }
   public Map<String, Object> makeGUICreaseFromEdges(FoldEdge foldEdge1, FoldEdge foldEdge2, Point cameraPosition, boolean isAMAPFold) {
      Map<String, Object> responseBody = new HashMap<>();
      FlapsToFold ftf = OrigamiControllerUtil.flapsToFold(isAMAPFold);
      BabylonJSCreaseSet guiCrease = manager.makeGUICreasePoints(foldEdge1, foldEdge2, cameraPosition, ftf);
      responseBody.put("creases", guiCrease.creases);
      return responseBody;
   }
   public Map<String,Object> makeCrease(Point p1, Point p2, Point cameraPosition, boolean isAMAPFold) {
      Map<String, Object> responseBody = new HashMap<>();
      FlapsToFold ftf = OrigamiControllerUtil.flapsToFold(isAMAPFold);
      FigureData creased = manager.creaseFigure(p1, p2, cameraPosition, ftf);
      //only the flat figure and the creases need to be updated
      responseBody.put("flat", creased.getFlatFigure());
      responseBody.put("creases", creased.getCreases());
      return responseBody;
   }
}
