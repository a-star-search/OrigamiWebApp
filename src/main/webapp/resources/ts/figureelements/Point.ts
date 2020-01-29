
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

class Point {
    public tolerance: 0.00001;

    private readonly _x: number;
    private readonly _y: number;
    private readonly _z: number;

    constructor(x: number, y: number, z: number) {
        this._x = x;
        this._y = y;
        this._z = z;
    }

    get x(): number { return this._x; }
    get y(): number { return this._y; }
    get z(): number { return this._z; }

    /*
    Compares two values within a reasonable epsilon.

    What I mean by reasonable is that in the context of this application, two points that are so close together
    are actually occupying the same position.

    In theory, in this application, I should be able to use equals to compare points, but using equality for
    floating point numbers makes me too uncomfortable.

    Precision is not a big deal in the front end anyway.
     */
    public epsilonEquals(other: Point): boolean {
        if (Math.abs(other._x - this._x) > this.tolerance)
            return false;
        if (Math.abs(other._y - this._y) > this.tolerance)
            return false;
        return Math.abs(other._z - this._z) <= this.tolerance;

    }
}