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
import com.moduleforge.origami.webapp.guidata.input.GUIFoldLine;
import com.moduleforge.origami.webapp.guidata.input.figure.FigureData;
import com.moduleforge.origami.webapp.guidata.output.FoldEdge;
import com.moduleforge.origami.webapp.guidata.output.FoldPoint;
import com.moduleforge.origami.webapp.guidata.output.linefold.PointPairLineFoldParameterSet;
import com.moduleforge.origami.webapp.service.OrigamiManager;
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;

import java.util.HashMap;
import java.util.Map;

import static com.moduleforge.origami.webapp.controller.OrigamiControllerUtil.makeFigureDataMapWithSnapshots;

public class OrigamiControllerFoldingImplementation {
   private OrigamiManager manager = null;

   public OrigamiControllerFoldingImplementation(OrigamiManager manager) {
      this.manager = manager;
   }

   public Map<String, Object> makeGUIFoldLinePoints(LineFoldType foldDirection, Point p1, Point p2, Point cameraPosition) {
      GUIFoldLine dashes = manager.makeGUIFoldLinePoints(p1, p2, foldDirection, cameraPosition);
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("facingsidedashes", dashes.visibleSideRibbons);
      responseBody.put("hiddensidedashes", dashes.hiddenSideRibbons);
      return responseBody;
   }
   public Map<String, Object> makeGUIFoldLinePoints(LineFoldType foldDirection, FoldEdge foldEdge1, FoldEdge foldEdge2,
                                                    Point cameraPosition) {
      //TODO calculate the fold segment from a couple of visibleEdges.
      Map<String, Object> responseBody = new HashMap<>();
      return responseBody;
   }
   public Map<String, Object> makeFold(FoldPoint foldPoint1, FoldPoint foldPoint2, Point cameraPosition, LineFoldType foldType,
                                       int angle, boolean isAMAPFold) {
      FlapsToFold ftf = OrigamiControllerUtil.flapsToFold(isAMAPFold);
      PointPairLineFoldParameterSet foldData =
              new PointPairLineFoldParameterSet(foldPoint1, foldPoint2, angle, cameraPosition, foldType, ftf);
      FigureData figureData = manager.foldFigure(foldData);
      return makeFigureDataMapWithSnapshots(figureData);
   }
}
