/*
 * This file is part of JFlowMap.
 *
 * Copyright 2009 Ilya Boyandin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jflowmap.views.flowmap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jflowmap.FlowMapGraph;
import jflowmap.data.FlowMapStats;
import jflowmap.data.SeqStat;

/**
 * @author Ilya Boyandin
 */
public class VisualFlowMapModel {

  public static final String DEFAULT_NODE_X_ATTR_NAME = "x";
  public static final String DEFAULT_NODE_Y_ATTR_NAME = "y";
  public static final String DEFAULT_EDGE_WEIGHT_ATTR_NAME = "value";
  public static final String DEFAULT_NODE_LABEL_ATTR_NAME = "label";

  private boolean autoAdjustColorScale;
  private boolean useLogColorScale = true;
  private boolean useLogWidthScale = false;
  private boolean showNodes = true;
  private boolean showDirectionMarkers = true;
  private boolean fillEdgesWithGradient = true;
  private boolean useProportionalDirectionMarkers = true;


//  private int edgeAlpha = 150;
  private int edgeAlpha = 100;  //50;
//  private int directionMarkerAlpha = 200;
  private int directionMarkerAlpha = 245; //210;

  private double edgeWeightFilterMin;
  private double edgeWeightFilterMax;

  private double edgeLengthFilterMin;
  private double edgeLengthFilterMax;

  private boolean autoAdjustEdgeColorScale;
  private double maxEdgeWidth = 1.0;
  private double directionMarkerSize = .17; //0.1;
  private double nodeSize = 4;
  private double visualLegendScale = 1.0;

  private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
  private final FlowMapGraph flowMapGraph;

  public VisualFlowMapModel(FlowMapGraph flowMapGraph) {
    this.flowMapGraph = flowMapGraph;
    initFromStats();
  }

  private void initFromStats() {
    FlowMapStats stats = flowMapGraph.getStats();

    SeqStat lengthStats = stats.getEdgeLengthStats();
    this.edgeLengthFilterMin = lengthStats.getMin();
    this.edgeLengthFilterMax = lengthStats.getMax();

    SeqStat weightStats = stats.getEdgeWeightStats();
    this.edgeWeightFilterMin = weightStats.getMin();
    this.edgeWeightFilterMax = weightStats.getMax();

    if (weightStats.getMax() - weightStats.getMin() > 7.0) {
      maxEdgeWidth = 7.0;
    } else {
      maxEdgeWidth = Math.floor(weightStats.getMax() - weightStats.getMin());
    }

//    MinMax xStats = stats.getNodeXStats();
//    MinMax yStats = stats.getNodeYStats();
//    nodeSize = (Math.min(xStats.getMax() - xStats.getMin(), yStats.getMax() - yStats.getMin()) / 250);
//    nodeSize = Math.max(
//        lenStats.getAvg() / 70,
//        Math.min(xStats.getMax() - xStats.getMin(), yStats.getMax() - yStats.getMin()) / 100);

//    nodeSize = lengthStats.getAvg() / 50;
  }

  public void setVisualLegendScale(double scale) {
    double old = this.visualLegendScale;
    this.visualLegendScale = scale;
    changes.firePropertyChange(PROPERTY_AUTO_VISUAL_LEGEND_SCALE, old, scale);
  }

  public double getVisualLegendScale() {
    return visualLegendScale;
  }

  public FlowMapGraph getFlowMapGraph() {
    return flowMapGraph;
  }

  public boolean getAutoAdjustColorScale() {
    return autoAdjustColorScale;
  }

  public void setAutoAdjustColorScale(final boolean autoAdjustColorScale) {
    boolean old = this.autoAdjustColorScale;
    this.autoAdjustColorScale = autoAdjustColorScale;
    changes.firePropertyChange(PROPERTY_AUTO_ADJUST_COLOR_SCALE, old, autoAdjustColorScale);
  }

  public boolean getUseLogColorScale() {
    return useLogColorScale;
  }

  public void setUseLogColorScale(final boolean useLogColorScale) {
    boolean old = this.useLogColorScale;
    this.useLogColorScale = useLogColorScale;
    changes.firePropertyChange(PROPERTY_USE_LOG_COLOR_SCALE, old, useLogColorScale);
  }

  public boolean getUseLogWidthScale() {
    return useLogWidthScale;
  }

  public void setUseLogWidthScale(final boolean useLogWidthScale) {
    boolean old = this.useLogWidthScale;
    this.useLogWidthScale = useLogWidthScale;
    changes.firePropertyChange(PROPERTY_USE_LOG_WIDTH_SCALE, old, useLogWidthScale);
  }

  public int getEdgeAlpha() {
    return edgeAlpha;
  }

  public int getDirectionMarkerAlpha() {
    return directionMarkerAlpha;
  }

  public double getEdgeWeightFilterMin() {
    return edgeWeightFilterMin;
  }

  public double getEdgeWeightFilterMax() {
    return edgeWeightFilterMax;
  }

  public double getEdgeLengthFilterMin() {
    return edgeLengthFilterMin;
  }

  public double getEdgeLengthFilterMax() {
    return edgeLengthFilterMax;
  }

  @Deprecated
  public boolean getAutoAdjustEdgeColorScale() {
    return autoAdjustEdgeColorScale;
  }

  public double getMaxEdgeWidth() {
    return maxEdgeWidth;
  }

  public void setDirectionMarkerAlpha(int edgeMarkerAlpha) {
    int old = this.directionMarkerAlpha;
    this.directionMarkerAlpha = edgeMarkerAlpha;
    changes.firePropertyChange(PROPERTY_DIRECTION_MARKER_ALPHA, old, edgeMarkerAlpha);
  }

  public void setEdgeWeightFilterMin(double valueFilterMin) {
    if (this.edgeWeightFilterMin != valueFilterMin) {
      double old = this.edgeWeightFilterMin;
      this.edgeWeightFilterMin = valueFilterMin;
      changes.firePropertyChange(PROPERTY_VALUE_FILTER_MIN, old, valueFilterMin);
    }
  }

  public void setEdgeWeightFilterMax(double valueFilterMax) {
    if (this.edgeWeightFilterMax != valueFilterMax) {
      double old = this.edgeWeightFilterMax;
      this.edgeWeightFilterMax = valueFilterMax;
      changes.firePropertyChange(PROPERTY_VALUE_FILTER_MAX, old, valueFilterMax);
    }
  }

  public void setEdgeLengthFilterMin(double edgeLengthFilterMin) {
    double old = this.edgeLengthFilterMin;
    this.edgeLengthFilterMin = edgeLengthFilterMin;
    changes.firePropertyChange(PROPERTY_EDGE_LENGTH_FILTER_MIN, old, edgeLengthFilterMin);
  }

  public void setEdgeLengthFilterMax(double edgeLengthFilterMax) {
    double old = this.edgeLengthFilterMax;
    this.edgeLengthFilterMax = edgeLengthFilterMax;
    changes.firePropertyChange(PROPERTY_EDGE_LENGTH_FILTER_MAX, old, edgeLengthFilterMax);
  }

  public void setAutoAdjustEdgeColorScale(boolean autoAdjustEdgeColorScale) {
    boolean old = this.autoAdjustEdgeColorScale;
    this.autoAdjustEdgeColorScale = autoAdjustEdgeColorScale;
    changes.firePropertyChange(PROPERTY_AUTO_ADJUST_EDGE_COLOR_SCALE, old, autoAdjustEdgeColorScale);
  }

  public void setMaxEdgeWidth(double maxEdgeWidth) {
    double old = this.maxEdgeWidth;
    this.maxEdgeWidth = maxEdgeWidth;
    changes.firePropertyChange(PROPERTY_MAX_EDGE_WIDTH, old, maxEdgeWidth);
  }

  public void setEdgeAlpha(int edgeAlpha) {
    int old = this.edgeAlpha;
    this.edgeAlpha = edgeAlpha;
    changes.firePropertyChange(PROPERTY_EDGE_ALPHA, old, edgeAlpha);
  }

  public boolean getShowNodes() {
    return showNodes;
  }

  public void setShowNodes(boolean value) {
    if (showNodes != value) {
      showNodes = value;
      changes.firePropertyChange(PROPERTY_SHOW_NODES, !value, value);
    }
  }

  public double getNodeSize() {
    return nodeSize;
  }

  public void setNodeSize(double size) {
    if (nodeSize != size) {
      double old = nodeSize;
      nodeSize = size;
      changes.firePropertyChange(PROPERTY_NODE_SIZE, old, nodeSize);
    }
  }

  public boolean getShowDirectionMarkers() {
    return showDirectionMarkers;
  }

  public void setShowDirectionMarkers(boolean value) {
    if (showDirectionMarkers != value) {
      showDirectionMarkers = value;
      changes.firePropertyChange(PROPERTY_SHOW_DIRECTION_MARKERS, !value, value);
    }
  }

  public boolean getFillEdgesWithGradient() {
    return fillEdgesWithGradient;
  }

  public void setFillEdgesWithGradient(boolean value) {
    if (fillEdgesWithGradient != value) {
      fillEdgesWithGradient = value;
      changes.firePropertyChange(PROPERTY_FILL_EDGES_WITH_GRADIENT, !value, value);
    }
  }

  public boolean getUseProportionalDirectionMarkers() {
    return useProportionalDirectionMarkers;
  }

  public void setUseProportionalDirectionMarkers(boolean value) {
    if (useProportionalDirectionMarkers != value) {
      useProportionalDirectionMarkers = value;
      changes.firePropertyChange(PROPERTY_USE_PROPORTIONAL_DIRECTION_MARKERS, !value, value);
    }
  }

  public double getDirectionMarkerSize() {
    return directionMarkerSize;
  }

  /**
   * @param markerSize
   *          A double value between 0 and 0.5. When useProportionalDirectionMarkers is true, the
   *          size of the marker for a certain edge E will be {@code markerSize * lengthOf(E)}.
   *          Otherwise it's {@code markerSize * lengthOfTheLongestEdge}
   */
  public void setDirectionMarkerSize(double markerSize) {
    if (markerSize < 0 || markerSize > .5) {
      throw new IllegalArgumentException(
          "Direction marker size must be between 0.0 and 0.5: attempted to set " + markerSize);
    }
    if (this.directionMarkerSize != markerSize) {
      double old = directionMarkerSize;
      this.directionMarkerSize = markerSize;
      changes.firePropertyChange(PROPERTY_DIRECTION_MARKER_SIZE, old, markerSize);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    changes.addPropertyChangeListener(listener);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    changes.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    changes.removePropertyChangeListener(listener);
  }

  public static final String PROPERTY_AUTO_VISUAL_LEGEND_SCALE = "visualLegendScale";
  public static final String PROPERTY_AUTO_ADJUST_COLOR_SCALE = "autoAdjustColorScale";
  public static final String PROPERTY_USE_LOG_COLOR_SCALE = "useLogColorScale";
  public static final String PROPERTY_USE_LOG_WIDTH_SCALE = "useLogWidthScale";
  public static final String PROPERTY_MAX_LENGTH_FILTER = "lengthFilterMax";
  public static final String PROPERTY_MIN_LENGTH_FILTER = "lengthFilterMin";
  public static final String PROPERTY_VALUE_FILTER_MIN = "weightFilterMin";
  public static final String PROPERTY_VALUE_FILTER_MAX = "valueFilterMax";
  public static final String PROPERTY_DIRECTION_MARKER_ALPHA = "directionMarkerAlpha";
  public static final String PROPERTY_DIRECTION_MARKER_SIZE = "directionMarkerSize";
  public static final String PROPERTY_EDGE_LENGTH_FILTER_MIN = "edgeLengthFilterMin";
  public static final String PROPERTY_EDGE_LENGTH_FILTER_MAX = "edgeLengthFilterMax";
  public static final String PROPERTY_AUTO_ADJUST_EDGE_COLOR_SCALE = "autoAdjustEdgeColorScale";
  public static final String PROPERTY_MAX_EDGE_WIDTH = "maxEdgeWidth";
  public static final String PROPERTY_EDGE_ALPHA = "edgeAlpha";
  public static final String PROPERTY_USE_PROPORTIONAL_DIRECTION_MARKERS = "proportionalDirMarkers";
  public static final String PROPERTY_FILL_EDGES_WITH_GRADIENT = "fillEdgesWithGradient";
  public static final String PROPERTY_SHOW_DIRECTION_MARKERS = "showDirectionMarkers";
  public static final String PROPERTY_SHOW_NODES = "showNodes";
  public static final String PROPERTY_NODE_SIZE = "nodeSize";


  public double normalize(double edgeWeight, boolean useLogValue) {
    SeqStat ws = getFlowMapGraph().getStats().getEdgeWeightStats();
    if (useLogValue) {
      return ws.normalizer().normalizeLog(edgeWeight);
    } else {
      return ws.normalizer().normalize(edgeWeight);
    }
  }

  public double normalizeEdgeWeightForColorScale(double edgeWeight) {
    return normalize(edgeWeight, useLogColorScale);
  }

  public double normalizeEdgeWeightForWidthScale(double edgeWeight) {
    return normalize(edgeWeight, useLogWidthScale);
  }
}
