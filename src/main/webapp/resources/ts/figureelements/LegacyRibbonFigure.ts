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
 * This class is called "legacy" because it inherits a contrived design from before I was using Typescript, where
 * the faces are divided by color. This was done this way to try having the simplest possible data structures
 * in Javascript so I'd have to do as little data manipulations as possible.
 *
 * This data structure matches the data passed from the server.
 *
 * Since I'm using Typescript now, I'm concerned with having the right design and this class is now a intermediate step
 * to building the class I will use for a figure in the frontend.
 */
class LegacyRibbonFigure {
  private readonly _facesColorOne: Set<RibbonPolygon>;
  private readonly _facesColorTwo: Set<RibbonPolygon>;
  private readonly _colorOne: Color;
  private readonly _colorTwo: Color;

  constructor(facesColorOne: Set<RibbonPolygon>, facesColorTwo: Set<RibbonPolygon>, colorOne: Color, colorTwo: Color) {
    this._facesColorOne = facesColorOne;
    this._facesColorTwo = facesColorTwo;
    this._colorOne = colorOne;
    this._colorTwo = colorTwo;
  }

  get facesColorOne(): Set<RibbonPolygon> {
    return this._facesColorOne;
  }

  get facesColorTwo(): Set<RibbonPolygon> {
    return this._facesColorTwo;
  }

  get colorOne(): Color {
    return this._colorOne;
  }

  get colorTwo(): Color {
    return this._colorTwo;
  }

}