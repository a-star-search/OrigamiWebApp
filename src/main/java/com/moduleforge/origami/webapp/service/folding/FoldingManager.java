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

package com.moduleforge.origami.webapp.service.folding;

import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.guidata.input.GUIFoldLine;
import com.moduleforge.origami.webapp.guidata.output.linefold.PointPairLineFoldParameterSet;
import com.moduleforge.origami.webapp.service.folding.guifoldline.GUIFoldLineMaker;
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.component.Face;
import com.whitebeluga.origami.rendering.FoldStepData;
import com.whitebeluga.origami.rendering.OneSidedPolygon;

import java.util.Map;
import java.util.Set;

public class FoldingManager {
   public static FoldStepData foldFigure(Figure figure, PointPairLineFoldParameterSet foldData) {
      FigureFolder folder = new FigureFolder(figure);
      return folder.foldFigure(foldData);
   }
   public static GUIFoldLine makeGUIFoldLinePoints(
           Figure figure,
           double paperSideLength,
           Point p1, Point p2,
           LineFoldType foldDirection,
           Point cameraPosition,
            /** This is necessary to calculate the fold segments at their correct positions. */
           Map<Face, Set<OneSidedPolygon>> originalFaceToPolygons) {
      GUIFoldLineMaker foldLineMaker = new GUIFoldLineMaker(figure, paperSideLength);
      return foldLineMaker.makeGUIFoldLineRibbons(p1, p2, foldDirection, cameraPosition, originalFaceToPolygons);
   }
}
