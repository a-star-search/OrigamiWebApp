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

class Face {
  private readonly _color: Color;
  private readonly _polygon: RibbonPolygon;

  constructor(color: Color, polygon: RibbonPolygon) {
    this._color = color;
    this._polygon = polygon;
  }

  get color(): Color {
    return this._color;
  }

  get polygon(): RibbonPolygon {
    return this._polygon;
  }
}