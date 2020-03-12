Web front end for the Origami application.
==========================================

About the limitations of babylon js.
---

babylon js is simply a terrible library all around and in particular badly suited for representing a figure with the 
characteristics required. I only discovered that too late. It worked decently for the flat representation of the 
figure, but not for the three-dimensional one.

A flat representation of a "bundle" cannot be flat in two different ways.

1)
The last stable version of babylon js at the time of writing does not allow two polygons to occupy the same space facing
different directions. Even if each is invisible from one side, they collide and don't display properly. That means
they have to be positioned slightly separated from each other. That is, each side of a bundle has to be parallel to
each other and slightly separated apart.

For what is worth in the development version of babylon at the time of writing it is possible to show only one side of
a shape and if another shape is put
in the exact same plane, overlapping but showing the opposite side, then both sides will be displayed correctly with
no interference. However if we display the borders of the polygon, the borders will be seen through in the other side.

Solution implemented:

Make the two layers slightly apart from each other. Just enough so that the borders will not be seen at the other side.
It is worth mentioning that there are other solutions where it is not necessary to shift the shapes apart but they are
more complex and the new gap between the layers doesn't look too bad.

When picking a point for a fold, the picked point is docked to an unshifted segment (or a vertex) that we have passed as
parameter (note the slight inaccuracy of docking to a different plane when there are different layers in the figure, I
don't believe this is a serious problem, as long as the docking ball is big enough to not be hidden by the polygons).

It is not a problem for the precision of the point picked, as it will always be docked to the actual segments or
vertices.

Given the trash that babylon is I don't expect that in future implementation two
faces can occupy the same space with different orientations and the borders of a face not interfere with the opposite
facing face, which would make the "gap" unnecessary.

2)
Another big problem stemming from the trash that babylon js is, is that it's not (in general)
possible to draw concave polygons with ribbons, which as far as I know, is the only possible way of drawing polygons.

Luckily in origami there are no concave faces, only concave *visible* parts of faces.

That means that a workaround is to draw each whole face that is, at least, partially visible and any other visible
face on top of it.

The drawback is that a truly flat view of the side of a bundle cannot be drawn.

Drawing faces on top of each other is actually more realistic.

This might be preferable, as long as we still find a way to get the fold points with accuracy, and I believe this is the
case.

3)
There are no fucking shadows!!!! There are, in thoery, but it's overly complicated and I never managed.
I can't believe what trash babylon is.

It's really a big drawback to how realistic the 3D figure looks.

Fold line dashes
---
To display the fold line with dashes for valley and mountain on each side,
we need to also pass the position of the camera in order
to be able to find the vector and shift the fold line dashes so that they will displayed on top of the faces which are
shifted.

Layered vs Flat rendering
=========================
While it is possible to create a completely flat rendering of the figure, that possibility of dual flat and layered
rendering is discarded and only a layered rendering is considered in the project.

It might be possible to find "vestigial" code for flat rendering from an earlier phase, but for simplicity, throughtout
the project only the layered rendering is considered.

Deploy the war to heroku
========================
OrigamiWebApp>heroku war:deploy target\OrigamiWebApp.war --app murmuring-dawn-69778

In order to deploy the war to heroku, install a plugin

OrigamiWebApp>heroku plugins:install heroku-cli-deploy

otherwise you have to deploy from a git repo (http://www.alienfactory.co.uk/articles/using-mercurial-and-git-in-harmony)

https://murmuring-dawn-69778.herokuapp.com/
