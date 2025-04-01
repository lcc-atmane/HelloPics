package com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.edge;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.edge.Edge;

public class EdgePair {
    // Member Variables ////////////////////////////////////////////////////////

    public Edge primary;
    public Edge secondary;

    // Constructor /////////////////////////////////////////////////////////////

    public EdgePair(Edge edge1, Edge edge2) {
        primary = edge1;
        secondary = edge2;
    }
}
