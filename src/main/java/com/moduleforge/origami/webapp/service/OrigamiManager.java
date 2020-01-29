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
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.guidata.input.GUIFoldLine;
import com.moduleforge.origami.webapp.guidata.input.figure.BabylonJSCreaseSet;
import com.moduleforge.origami.webapp.guidata.input.figure.FigureData;
import com.moduleforge.origami.webapp.guidata.output.FoldEdge;
import com.moduleforge.origami.webapp.guidata.output.linefold.PointPairLineFoldParameterSet;
import com.moduleforge.origami.webapp.service.creasing.CreasingManager;
import com.moduleforge.origami.webapp.service.folding.FoldingManager;
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.OrigamiBase;
import com.whitebeluga.origami.figure.ZAxisFigureRotator;
import com.whitebeluga.origami.figure.component.Face;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;
import com.whitebeluga.origami.rendering.FoldStepData;
import com.whitebeluga.origami.rendering.OneSidedPolygon;
import org.javatuples.Pair;

import java.awt.*;
import java.util.*;

public class OrigamiManager {
   public static final Point CENTER = new Point(0.0, 0.0, 0.0);
   private Figure originalFigure;
   private Map<OrigamiBase, Figure> baseFigureMap;
   private final Deque<Figure> historyStack;
   private Figure figure;
   private Figure originalSquare;
   private Double paperSideLength;
   /**
    * This is used to retrieve the "figure data" corresponding to a figure, should it be needed.
    */
   private final Map<Figure, FigureData> figureToFigureData;

   public OrigamiManager(Figure squarePaper, Map<OrigamiBase, Figure> baseFigureMap, Double paperSideLength) {
      originalFigure = squarePaper;
      this.baseFigureMap = baseFigureMap;
      this.paperSideLength = paperSideLength;
      figure = squarePaper;
      originalSquare = squarePaper;
      historyStack = new ArrayDeque<>();
      historyStack.push(originalFigure);
      figureToFigureData = new HashMap<>();
   }
   public FigureData getOrMakeAndUpdateFigureData(){
      FigureData figureData = figureToFigureData.get(figure);
      if(figureData != null)
         return figureData;
      figureData = new FigureData(figure);
      figureToFigureData.put(figure, figureData);
      return figureData;
   }
   public FigureData updateFaceColor(int index, Color color){
      Figure colorChanged = index == 0 ? figure.changeFrontColor(color) : figure.changeBackColor(color);
      return updateFigureAndMakeFigureData(colorChanged);
   }
   /**
    * Rotates the figure around the Z axis.
    */
   public FigureData rotateFigureAroundZAxis(double angle) {
      Figure rotated = ZAxisFigureRotator.rotateFigure(figure, angle);
      return updateFigureAndMakeFigureData(rotated);
   }
   public void reset() {
      figure = originalFigure;
      resetHistory(figure);
   }
   public Figure undoLastStep(){
      if(historyStack.size() <= 1)
         return figure;
      Figure previous = historyStack.poll();
      if(previous != null)
         figure = previous;
      return figure;
   }
   public void squareLoaded(){
      Figure figureLoaded = this.originalSquare;
      originalFigure = figureLoaded;
      figure = figureLoaded;
      resetHistory(figureLoaded);
   }
   public void baseLoaded(OrigamiBase base) {
      Figure figureLoaded = baseFigureMap.get(base);
      originalFigure = figureLoaded;
      figure = figureLoaded;
      resetHistory(figureLoaded);
   }
   /**
    * The reason the visible visibleEdges have to be recalculated after a rotation of the figure in the frontend is that
    * each side of a bundle has its own internal visibleEdges, so given the camera position, it's a matter of
    * determining what side the user is looking at.
    */
   public Pair<Set<LineSegment>, Set<LineSegment>> recalculateVisibleEdges(Point cameraPosition) {
      return VisibleEdgesCalculator.calculateVisibleEdges(figure, cameraPosition);
   }
   public boolean thereIsNoHistory() {
      return historyStack.size() <= 1;
   }
   //--- Folding
   public FigureData foldFigure(PointPairLineFoldParameterSet foldData) {
      FoldStepData foldStepData = FoldingManager.foldFigure(figure, foldData);
      FigureData figureData = new FigureData(foldStepData);
      Figure foldedFigure = foldStepData.getFoldedFigure();
      updateFigure(foldedFigure);
      figureToFigureData.put(foldedFigure, figureData);
      return figureData;
   }
   /**
    * Each element of the pair returned correspond to a list of dashes to be displayed on each side of a face
    */
   public GUIFoldLine makeGUIFoldLinePoints(Point p1, Point p2, LineFoldType foldDirection, Point cameraPosition) {
      Map<Face, Set<OneSidedPolygon>> originalFaceToPolygons =
              getOrMakeAndUpdateFigureData().getOriginalFaceToPolygonsOfLayeredRendering();
      return FoldingManager.makeGUIFoldLinePoints(
              figure, paperSideLength, p1, p2, foldDirection, cameraPosition,
              originalFaceToPolygons);
   }
   //--- Creasing
   public FigureData creaseFigure(Point p1, Point p2, Point cameraPosition, FlapsToFold ftf) {
      Figure creased = CreasingManager.creaseFigure(figure, p1, p2, cameraPosition, ftf);
      return updateFigureAndMakeFigureData(creased);
   }
   public BabylonJSCreaseSet makeGUICreasePoints(Point p1, Point p2, Point cameraPosition, FlapsToFold ftf) {
      return CreasingManager.makeGUICreasePoints(figure, p1, p2, cameraPosition, ftf);
   }
   public BabylonJSCreaseSet makeGUICreasePoints(FoldEdge edge1, FoldEdge edge2, Point cameraPosition, FlapsToFold ftf) {
      return CreasingManager.makeGUICreasePoints(figure, edge1, edge2, cameraPosition, ftf);
   }
   //---
   private FigureData updateFigureAndMakeFigureData(Figure f) {
      updateFigure(f);
      return getOrMakeAndUpdateFigureData();
   }
   private void updateFigure(Figure newFigure){
      historyStack.push(figure);
      figure = newFigure;
   }
   private void resetHistory(Figure figure) {
      historyStack.clear();
      historyStack.push(figure);
      figureToFigureData.clear();
   }
}
