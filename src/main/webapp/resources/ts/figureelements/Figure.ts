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

/**
 * A figure object is actually a pair of figures: the flat version and the 3D version.
 * These two latter objects are no more than sets of faces.
 *
 * It also includes the edges of the flat figure (which is very useful in the GUI) and
 * a set of creases of the flat figure also.
 */
class Figure {
  private readonly _flat: RibbonFigure;
  private readonly _threeD: RibbonFigure;
  /**
   * Set of edges of the figure that are visible to the user of the application
   */
  private readonly _visibleEdges: Set<Segment>;
  private readonly _creases: Set<Segment>;

  constructor(flat: RibbonFigure, threeD: RibbonFigure, edges: Set<Segment>, creases: Set<Segment>) {
    this._flat = flat;
    this._threeD = threeD;
    this._visibleEdges = edges;
    this._creases = creases;
  }

  get flat(): RibbonFigure {
    return this._flat;
  }

  get threeD(): RibbonFigure {
    return this._threeD;
  }

  get visibleEdges(): Set<Segment> {
    return this._visibleEdges;
  }

  get creases(): Set<Segment> {
    return this._creases;
  }

  public static fromAjaxData(flat: any, threed: any, edges: any[][], creases?: any[][]): Figure {
    if (creases) {
      return AjaxDataStructureConverter.Instance.convertFigure(flat, threed, edges, creases);
    }
    return AjaxDataStructureConverter.Instance.convertFigure(flat, threed, edges);
  }
}
