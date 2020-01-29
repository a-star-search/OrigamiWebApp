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

import com.moduleforge.libraries.geometry._3d.LineSegment;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;
import com.moduleforge.origami.webapp.guidata.output.FoldPoint;
import com.moduleforge.origami.webapp.guidata.output.linefold.PointPairLineFoldParameterSet;
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;
import com.whitebeluga.origami.figure.folding.linefolding.LineFoldParameters;
import com.whitebeluga.origami.rendering.FoldStepData;

import java.util.ArrayList;
import java.util.List;

import static com.moduleforge.origami.webapp.service.DockedTo.VERTEX;
import static com.moduleforge.origami.webapp.service.OrigamiManager.CENTER;
import static com.moduleforge.origami.webapp.service.Util.calculateNormalOfSideFacingCamera;
import static com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType.VALLEY;
import static com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold.ALL;
import static com.whitebeluga.origami.rendering.FoldStepData.amapLineFold;
import static com.whitebeluga.origami.rendering.FoldStepData.ffLineFold;
import static java.lang.Math.toRadians;

public class FigureFolder {
   private Figure figure;

   FigureFolder(Figure figure) {
      this.figure = figure;
   }

   FoldStepData foldFigure(PointPairLineFoldParameterSet foldData) {
      return foldFigure(foldData.foldPoint1, foldData.foldPoint2,
              foldData.angle,
              foldData.cameraPosition,
              foldData.foldType,
              foldData.flapsToFold);
   }
   private FoldStepData foldFigure(FoldPoint foldPoint1, FoldPoint foldPoint2,
                                   int angle,
                                   Point cameraPosition,
                                   LineFoldType foldType,
                                   FlapsToFold flapsToFold) {
      LineFoldParameters params = makeFoldParameters(foldPoint1, foldPoint2, angle, cameraPosition, foldType);
      boolean foldAll = flapsToFold == ALL;
      FoldStepData foldStep = foldAll? amapLineFold(figure, params) : ffLineFold(figure, params);
      return makeTranslatedToCenterFoldStepData(foldStep);
   }
   private LineFoldParameters makeFoldParameters(FoldPoint foldPoint1, FoldPoint foldPoint2, int angle,
                                                 Point cameraPosition, LineFoldType foldType) {
      Point dockedPoint1 = foldPoint1.dockedPoint;
      Point dockedPoint2 = foldPoint2.dockedPoint;
      Vector normalOfSideFacingCamera =
              calculateNormalOfSideFacingCamera(figure, cameraPosition, dockedPoint1, dockedPoint2);
      Vector normalOfValleySide = calculateNormalOfValleySide(foldType, normalOfSideFacingCamera);

      LineSegment segment = new LineSegment(dockedPoint1, dockedPoint2);
      double radians = toRadians( angle);
      List<Point> dockedToVertices = pointsDockedToVertices(foldPoint1, foldPoint2);
      Vector guiUserLookingDirection = normalOfSideFacingCamera.negate();
      return new LineFoldParameters(segment, normalOfValleySide, guiUserLookingDirection,
              dockedToVertices, radians);
   }
   private Vector calculateNormalOfValleySide(LineFoldType foldType, Vector normalOfSideFacingCamera) {
      boolean isValleyFold = foldType.equals(VALLEY);
      return isValleyFold ? normalOfSideFacingCamera : normalOfSideFacingCamera.negate();
   }
   private List<Point> pointsDockedToVertices(FoldPoint foldPoint1, FoldPoint foldPoint2) {
      List<Point> dockedToVertices = new ArrayList<>();
      if(foldPoint1.dockedTo == VERTEX)
         dockedToVertices.add(foldPoint1.dockedPoint);
      if(foldPoint2.dockedTo == VERTEX)
         dockedToVertices.add(foldPoint2.dockedPoint);
      return dockedToVertices;
   }
   private FoldStepData makeTranslatedToCenterFoldStepData(FoldStepData fsd){
      return new FoldStepData(
              fsd.getInitialFigure(),
              fsd.getFoldedFigure().translated(CENTER),
              fsd.getFoldFrames(),
              fsd.getTranslationFrames());
   }
}
