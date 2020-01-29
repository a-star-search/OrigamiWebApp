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

package com.moduleforge.origami.webapp.guidata.input.figure;

import com.moduleforge.libraries.geometry._3d.Point;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.component.Edge;
import com.whitebeluga.origami.figure.component.Face;
import com.whitebeluga.origami.rendering.FoldStepData;
import com.whitebeluga.origami.rendering.JSDisplayableFigure;
import com.whitebeluga.origami.rendering.OneSidedPolygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.whitebeluga.origami.rendering.FigureRendering.layeredRendering;
import static com.whitebeluga.origami.rendering.FigureRendering.threeDRendering;

/**
 * Holds all the information that is passed to the front end
 */
public class FigureData {
   /**
    * In the flat or layered rendering of a figure, the bundles are pushed to avoid problems displaying them in
    * the same plane in the GUI.
    */
   public static final double LAYER_SEPARATION_BETWEEN_SIDES = 2e-3;
   /**
    * The flat figure is used to make the fold, since it allows for precision.
    */
   private final BabylonJSRibbonFigure flatFigure;
   /**
    * The 3D figure is used for display only, and provides the user with a realistic
    * representation of the folded figure
    */
   private final BabylonJSRibbonFigure threeDFigure;
   /**
    * Segments belong to the flat figure
    */
   private final List<List<Point>> segments;
   /**
    * The creases of the faces.
    */
   private final BabylonJSCreaseSet creases;
   /**
    * Snapshots belong to a fold and they represent the flat figure.
    * This variable may not be present (ie empty)
    */
   private final List<BabylonJSRibbonFigure> foldSnapshots;
   /**
    * After a fold there might be a translation to the center
    */
   private final List<BabylonJSRibbonFigure> translationSnapshots;

   /**
    * This information is sometimes useful in the GUI in order to display some graphic elements.
    *
    * Since the "flatter" rendering which is the layer rendering is not perfectly flat and we know how some graphical
    * elements such as fold lines would look on a flat rendering. We need a way to translate to the layered rendering
    * in order to display those graphical elements correctly.
    */
   private final Map<Face, Set<OneSidedPolygon>> originalFaceToPolygonsOfLayeredRendering;

   public FigureData(Figure figure) {
      JSDisplayableFigure layerRendered = layeredRendering(figure, LAYER_SEPARATION_BETWEEN_SIDES);
      originalFaceToPolygonsOfLayeredRendering = layerRendered.getFaceToPolygons();
      creases = makeCreaseSet(layerRendered);
      flatFigure = new BabylonJSRibbonFigure(layerRendered);
      JSDisplayableFigure threeDRendering = threeDRendering(figure, LAYER_SEPARATION_BETWEEN_SIDES);
      threeDFigure = new BabylonJSRibbonFigure(threeDRendering);
      segments = makeSegments(figure);
      foldSnapshots = new ArrayList<>();
      translationSnapshots = new ArrayList<>();
   }
   private static BabylonJSCreaseSet makeCreaseSet(JSDisplayableFigure layerRendered) {
      Set<OneSidedPolygon> facesColor1 = layerRendered.getFacesColorOne();
      Set<OneSidedPolygon> facesColor2 = layerRendered.getFacesColorTwo();
      List<List<Point>> creases = new ArrayList<>();
      for(OneSidedPolygon pol : facesColor1)
         creases.addAll(pol.getCreases());
      for(OneSidedPolygon pol : facesColor2)
         creases.addAll(pol.getCreases());
      return new BabylonJSCreaseSet(creases);
   }
   private FigureData(Figure figure, List<Figure> foldSnapshots, List<Figure> translationSnapshots){
      this(figure);
      for(Figure f : foldSnapshots){
         JSDisplayableFigure flatRendering = layeredRendering(f, LAYER_SEPARATION_BETWEEN_SIDES);
         BabylonJSRibbonFigure flat = new BabylonJSRibbonFigure(flatRendering);
         this.foldSnapshots.add(flat);
      }
      for(Figure f : translationSnapshots){
         JSDisplayableFigure flatRendering = layeredRendering(f, LAYER_SEPARATION_BETWEEN_SIDES);
         BabylonJSRibbonFigure flat = new BabylonJSRibbonFigure(flatRendering);
         this.translationSnapshots.add(flat);
      }
   }
   public FigureData(FoldStepData foldStep) {
      this(foldStep.getFoldedFigure(), foldStep.getFoldFrames(), foldStep.getTranslationFrames());
   }
   public BabylonJSRibbonFigure getFlatFigure() {
      return flatFigure;
   }
   public BabylonJSRibbonFigure getThreeDFigure() {
      return threeDFigure;
   }
   public List<List<Point>> getSegments() {
      return segments;
   }
   public List<BabylonJSRibbonFigure> getFoldSnapshots() {
      return foldSnapshots;
   }
   public BabylonJSCreaseSet getCreases() {
      return creases;
   }

   private static List<List<Point>> makeSegments(Figure figure) {
      List<List<Point>> result = new ArrayList<>();
      for(Edge oSegment: figure.getEdges()) {
         List<Point> points = new ArrayList<>();
         points.add(oSegment.getVertices().get(0));
         points.add(oSegment.getVertices().get(1));
         result.add(points);
      }
      return result;
   }
   public List<BabylonJSRibbonFigure> getTranslationSnapshots() {
      return translationSnapshots;
   }

   public Map<Face, Set<OneSidedPolygon>> getOriginalFaceToPolygonsOfLayeredRendering() {
      return originalFaceToPolygonsOfLayeredRendering;
   }
}
