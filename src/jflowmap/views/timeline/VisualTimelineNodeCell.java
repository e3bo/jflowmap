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

package jflowmap.views.timeline;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import jflowmap.FlowDirection;
import jflowmap.FlowMapAttrSpec;
import jflowmap.data.FlowMapNodeTotals;
import jflowmap.data.SeqStat;
import jflowmap.util.ArrayUtils;
import jflowmap.util.ColorUtils;
import jflowmap.util.piccolo.PNodes;
import jflowmap.views.ColorCodes;
import jflowmap.views.IFlowMapColorScheme;
import jflowmap.views.flowmap.FlowMapView;
import prefuse.data.Node;
import prefuse.util.ColorLib;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;

/**
 * @author Ilya Boyandin
 */
public class VisualTimelineNodeCell extends PNode {

  private static final double LEFT_RIGHT_HALF_CIRCLE_GAP = 0; //.5;
  private static final Stroke HALF_CIRCLE_STROKE =
//    new PFixedWidthStroke(1);
//    new BasicStroke(1f);
    null;
//  private static final Color VALUE_COLOR_MIN_NEG = new Color(26, 152, 80);
//  private static final Color VALUE_COLOR_ZERO =  new Color(255, 255, 191);
//  private static final Color VALUE_COLOR_MAX_POS = new Color(215, 48, 39);

//  private static final Color INTRAREG_VALUE_COLOR_MIN_NEG = new Color(102, 189, 99);
//  private static final Color INTRAREG_VALUE_COLOR_ZERO = new Color(255, 255, 191);
//  private static final Color INTRAREG_VALUE_COLOR_MAX_POS = new Color(244, 109, 67);



  private static final boolean FILL_HALF_CIRCLES_WITH_QTY_GRADIENT_COLOR = false;

//  private static final Color VALUE_COLOR_NAN = FlowTimelineView.CANVAS_BACKGROUND_COLOR; // new Color(200, 200, 200);
  private static final boolean SHOW_NODE_LABEL = false;
  private static final boolean SHOW_VALUE_TEXT = false;
  private static final boolean SHOW_DIFF = false;
//  private static final boolean FILL_RECT_WITH_VALUE_COLOR = false;
//  private static final boolean FILL_RECT_WITH_REGION_COLOR = false;
  private static final boolean SHOW_HALF_CIRCLES = true;
  private static final boolean SHOW_INTRAREG_HALF_CIRCLES = true;

  public static final int[] DIFF_COLORS = ArrayUtils.reverse(new int[] {
    ColorLib.rgb(165, 0, 38), ColorLib.rgb(215, 48, 39), ColorLib.rgb(244, 109, 67), ColorLib.rgb(253, 174, 97), ColorLib.rgb(254, 224, 139),
    ColorLib.rgb(255, 255, 191),
    ColorLib.rgb(217, 239, 139), ColorLib.rgb(166, 217, 106), ColorLib.rgb(102, 189, 99), ColorLib.rgb(26, 152, 80), ColorLib.rgb(0, 104, 55),
  });
//  private static final Color DIFF_COLOR_MIN = new Color(26, 152, 80);
//  private static final Color DIFF_COLOR_ZERO = new Color(255, 255, 191);
//  private static final Color DIFF_COLOR_MAX = new Color(215, 48, 39);
//  private static final Color DIFF_COLOR_NAN = new Color(100, 100, 100); //FlowTimelineView.CANVAS_BACKGROUND_COLOR;

  private static final double DIFF_BOX_WIDTH = 10;
  private static final double DIFF_BOX_GAP = 0;

  private static final Color NODE_NAME_COLOR1 = new Color(0, 0, 0);
  private static final Color NODE_NAME_COLOR2 = new Color(255, 255, 255);

  private static final Color SELECTION_STROKE_PAINT = new Color(200, 100, 30);
  private static final PFixedWidthStroke SELECTION_STROKE = new PFixedWidthStroke(4);
  private static final Font CELL_CAPTION_FONT = new Font("Arial", Font.PLAIN, 18);
  private static final Font CELL_VALUE_FONT = new Font("Arial", Font.PLAIN, 10);
  private final Node node;
  private final VisualTimeline timeline;
  private final PPath rect;
  private PPath diffRect;
  private final String weightAttrName;

  public VisualTimelineNodeCell(VisualTimeline timeline, Node node, String weightAttrName, double x, double y, double width, double height) {
    this.node = node;
    this.weightAttrName = weightAttrName;
    this.timeline = timeline;

    FlowMapAttrSpec specs = timeline.getAttrSpecs();

    setBounds(x, y, width, height);

    double inValue = FlowMapNodeTotals.getTotalWeight(node, weightAttrName, FlowDirection.INCOMING);
    double outValue = FlowMapNodeTotals.getTotalWeight(node, weightAttrName, FlowDirection.OUTGOING);


    double inLocalValue = node.getDouble(FlowMapNodeTotals.NODE_COLUMN__SUM_INCOMING_INTRAREG);
    double outLocalValue = node.getDouble(FlowMapNodeTotals.NODE_COLUMN__SUM_OUTGOING_INTRAREG);

    final SeqStat vstats = timeline.getValueStats();

    rect = new PPath(new Rectangle2D.Double(
        x + (SHOW_DIFF ? DIFF_BOX_WIDTH + DIFF_BOX_GAP : 0), y,
        width - (SHOW_DIFF ? DIFF_BOX_WIDTH + DIFF_BOX_GAP : 0), height));

//    rect.setStrokePaint(Color.white);
    rect.setStroke(null);
//    rectNode.setPaint(Color.white);
    addChild(rect);


    Color rectColor = null;
//    if (FILL_RECT_WITH_VALUE_COLOR) {
//      if (!Double.isNaN(outValue)) {
//        double normalizedLogValue =
//          vstats.normalizeLog(outValue);
//  //        vstats.normalize(value);
//        rectColor = ColorUtils.colorBetween(
//            VALUE_COLOR_MIN_NEG,
//            VALUE_COLOR_MAX_POS,
//            normalizedLogValue, 255
//        );
//      } else {
//        rectColor = VALUE_COLOR_NAN;
//      }
//    }

//    if (FILL_RECT_WITH_REGION_COLOR) {
//      rectColor = ColorLib.getColor(node.getInt(FlowTimelineView.NODE_COLUMN__REGION_COLOR));
//    }

    if (rectColor == null) {
      rect.setPaint(Color.white);
    } else {
      rect.setPaint(rectColor);
    }

    Color textColor;
    if (rectColor == null)
      textColor = NODE_NAME_COLOR1;
    else
      textColor = ColorUtils.farthestColor(NODE_NAME_COLOR1, NODE_NAME_COLOR2, rectColor);

    PText nodeLabelText = null;
    if (SHOW_NODE_LABEL) {
      nodeLabelText = new PText(node.getString(specs.getNodeLabelAttr()));
      nodeLabelText.setFont(CELL_CAPTION_FONT);
  //    text.setBounds(rect.getBounds());
  //    text.setJustification(JComponent.CENTER_ALIGNMENT);
      nodeLabelText.setX(rect.getX() + 3);
      nodeLabelText.setY(rect.getY() + 2);
      addChild(nodeLabelText);
      nodeLabelText.setTextPaint(textColor);
    }

    if (SHOW_VALUE_TEXT) {
      PText valueText = new PText(FlowMapView.NUMBER_FORMAT.format(outValue));
      valueText.setFont(CELL_VALUE_FONT);
      valueText.setJustification(JComponent.RIGHT_ALIGNMENT);
      valueText.setTextPaint(Color.gray);
      valueText.setBounds(
          rect.getX() - valueText.getWidth() + rect.getWidth(), rect.getY() - valueText.getHeight() + rect.getHeight(),
          valueText.getWidth(), valueText.getHeight());
      addChild(valueText);
      valueText.setTextPaint(textColor);
    }



//    if (SHOW_DIFF) {
//      diffRect = new PPath(new Rectangle2D.Double(x, y, DIFF_BOX_WIDTH, height));
//      diffRect.setPaint(new Color(0, 68, 27));
//      diffRect.setStroke(null);
//      addChild(diffRect);
//
//      Color diffRectColor;
//      double diff = node.getDouble(FlowMapSummaries.NODE_COLUMN__SUM_OUTGOING_DIFF_TO_NEXT_YEAR);
//
//      MinMax diffStats = timeline.getGlobalStats().getNodeAttrStats(
//          FlowMapSummaries.NODE_COLUMN__SUM_OUTGOING_DIFF_TO_NEXT_YEAR);
//      if (!Double.isNaN(diff)) {
//        // TODO: Use ColorMap instead
//  //      double normalizedDiff =
//  ////        diffStats.normalizeLog(diff);
//  //        diffStats.normalize(diff);
//  //      diffRectColor = ColorUtils.colorBetween(
//  //          DIFF_COLOR_MIN,
//  //          DIFF_COLOR_MAX,
//  //          normalizedDiff, 255
//  //      );
//        diffRectColor = ColorLib.getColor(timeline.getSumOutgoingDiffColorMap().getColor(diff));
//      } else {
//        diffRectColor = DIFF_COLOR_NAN;
//      }
//      diffRect.setPaint(diffRectColor);
//    }



    if (SHOW_HALF_CIRCLES) {
      double normalizedIn = vstats.normalizer().normalizeAroundZero(inValue);
      double normalizedOut = vstats.normalizer().normalizeAroundZero(outValue);

      double normalizedInLocal = vstats.normalizer().normalizeAroundZero(inLocalValue); // ! normalize using the non-local stats
      double normalizedOutLocal = vstats.normalizer().normalizeAroundZero(outLocalValue);

      addChild(colorizeHalfCircle(createHalfCircle(x, y, true, normalizedIn), normalizedIn, true, false));
      addChild(colorizeHalfCircle(createHalfCircle(x, y, false, normalizedOut), normalizedOut, false, false));

      if (SHOW_INTRAREG_HALF_CIRCLES) {
        addChild(colorizeHalfCircle(createHalfCircle(x, y, true, normalizedInLocal), normalizedInLocal, true, true));
        addChild(colorizeHalfCircle(createHalfCircle(x, y, false, normalizedOutLocal), normalizedOutLocal, false, true));
      }
    }

    addInputEventListener(inputEventListener);
  }

  public String getWeightAttrName() {
    return weightAttrName;
  }

  private PPath createHalfCircle(double x, double y, boolean leftNotRight, double normalizedValue) {
    PBounds b = getBoundsReference();
    double wh = Math.min(b.width, b.height);
    double r = Math.sqrt(Math.abs(normalizedValue)) * wh;
    double off = (wh - r)/2;
    PPath ppath = new PPath(new Arc2D.Double(
        x + off + (leftNotRight ? -1 : +1) * LEFT_RIGHT_HALF_CIRCLE_GAP,   y + off,
        r, r, (leftNotRight ? 90 : -90), 180, Arc2D.PIE));
    ppath.setStroke(HALF_CIRCLE_STROKE);
    return ppath;
  }

  private PPath colorizeHalfCircle(PPath halfCircle, double normalizedValue, boolean leftNotRight, boolean intrareg) {
    IFlowMapColorScheme cs = timeline.getColorScheme();

    Color cmax = cs.get(leftNotRight ? ColorCodes.EDGE_GRADIENT_END_MAX_WEIGHT : ColorCodes.EDGE_GRADIENT_START_MAX_WEIGHT);

//    if (!intrareg) {
//      // TODO: cache this color (in ColorLib) or add it to the scheme
//      cmax = new Color(cmax.getRed()/2, cmax.getGreen()/2, cmax.getBlue()/2);
//    }
    if (intrareg) {
      // TODO: cache this color (in ColorLib) or add it to the scheme
      cmax = new Color(
          cmax.getRed() + (255 - cmax.getRed())/2,
          cmax.getGreen() + (255 - cmax.getGreen())/2,
          cmax.getBlue() + (255 - cmax.getBlue())/2);
    } else {
      cmax = new Color(cmax.getRed()/2, cmax.getGreen()/2, cmax.getBlue()/2);
    }

    if (FILL_HALF_CIRCLES_WITH_QTY_GRADIENT_COLOR) {
      if (normalizedValue > 0) {
        Color cmin = cs.get(leftNotRight ? ColorCodes.EDGE_GRADIENT_END_MIN_WEIGHT : ColorCodes.EDGE_GRADIENT_START_MIN_WEIGHT);
        halfCircle.setPaint(ColorUtils.colorBetween(
            cmin,
            cmax,
//            VALUE_COLOR_ZERO,
//            VALUE_COLOR_MAX_POS,
            normalizedValue, 255
        ));
//      } else {
//        halfCircle.setPaint(ColorUtils.colorBetween(
//            VALUE_COLOR_MIN_NEG,
//            VALUE_COLOR_ZERO,
//            normalizedValue, 255
//        ));
      }
//    } else if (FILL_HALF_CIRCLES_WITH_MAX_COLOR) {
//      if (normalizedValue > 0) {
//        halfCircle.setPaint(VALUE_COLOR_MAX_POS);
//      } else if (normalizedValue == 0) {
//        halfCircle.setPaint(VALUE_COLOR_ZERO);
//      } else {
//        halfCircle.setPaint(VALUE_COLOR_MIN_NEG);
//      }
    } else {
      halfCircle.setPaint(cmax);
    }
    return halfCircle;
  }

//  private PPath colorizeIntraregHalfCircle(PPath halfCircle, double normalizedValue) {
//    if (FILL_HALF_CIRCLES_WITH_QTY_COLOR) {
//      if (normalizedValue > 0) {
//        halfCircle.setPaint(ColorUtils.colorBetween(
//            INTRAREG_VALUE_COLOR_ZERO,
//            INTRAREG_VALUE_COLOR_MAX_POS,
//            normalizedValue, 255
//        ));
//      } else {
//        halfCircle.setPaint(ColorUtils.colorBetween(
//            INTRAREG_VALUE_COLOR_MIN_NEG,
//            INTRAREG_VALUE_COLOR_ZERO,
//            normalizedValue, 255
//        ));
//      }
//    } else if (FILL_HALF_CIRCLES_WITH_MAX_COLOR) {
//      if (normalizedValue > 0) {
//        halfCircle.setPaint(INTRAREG_VALUE_COLOR_MAX_POS);
//      } else if (normalizedValue == 0) {
//        halfCircle.setPaint(INTRAREG_VALUE_COLOR_ZERO);
//      } else {
//        halfCircle.setPaint(INTRAREG_VALUE_COLOR_MIN_NEG);
//      }
//    }
//    return halfCircle;
//  }


  public Node getNode() {
    return node;
  }

  private static final PInputEventListener inputEventListener = new PBasicInputEventHandler() {
    @Override
    public void mouseEntered(PInputEvent event) {
      VisualTimelineNodeCell vc = PNodes.getAncestorOfType(event.getPickedNode(), VisualTimelineNodeCell.class);
      if (vc != null) {
        vc.rect.setStroke(SELECTION_STROKE);
        vc.rect.setStrokePaint(SELECTION_STROKE_PAINT);
        vc.timeline.showTooltip(vc);
      }
    }

    @Override
    public void mouseExited(PInputEvent event) {
      VisualTimelineNodeCell vc = PNodes.getAncestorOfType(event.getPickedNode(), VisualTimelineNodeCell.class);
      if (vc != null) {
        vc.rect.setStroke(null);
        vc.rect.setStrokePaint(null);
        vc.timeline.hideTooltip();
      }
    }
  };

}
