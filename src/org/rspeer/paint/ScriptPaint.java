package org.rspeer.paint;

import org.rspeer.CombatStore;
import org.rspeer.Fighter;
import org.rspeer.Stats;
import org.rspeer.config.Config;
import org.rspeer.config.ProgressiveSet;
import org.rspeer.framework.Node;
import org.rspeer.models.Progressive;
import org.rspeer.nodes.idle.IdleNode;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.ScriptMeta;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ScriptPaint implements RenderListener {

    private static final int BASE_X = 6;
    private static final int BASE_Y = 6;

    private static final int DEFAULT_WIDTH_INCR = 20;

    private static final int BASE_HEIGHT = 20;
    private static final int LINE_HEIGHT = 20;

    private static final Color FOREGROUND = Color.WHITE;
    private static final Color BACKGROUND = Color.BLACK;
    private static final Stroke STROKE = new BasicStroke(1.8f);

    private final Map<String, PaintStatistic> stats;

    private Color outline;

    public ScriptPaint(Fighter context) {
        stats = new LinkedHashMap<>();
        outline = new Color(240, 0, 73);

        ScriptMeta meta = context.getMeta();
        stats.put(meta.name(), new PaintStatistic(true, () -> "v" + meta.version() + " by " + meta.developer()));
        stats.put("Runtime", new PaintStatistic(() -> context.getRuntime().toElapsedString()));
        stats.put("Status", new PaintStatistic(() -> {
            Node active = context.getManager().getActive();
            return active == null ? "None" : active.getClass().getSimpleName() + " -> " + active.status();
        }));
        stats.put("Progressive", new PaintStatistic(() -> {
            Progressive set = ProgressiveSet.getCurrent();
            return set == null ? "None" : set.getName();
        }));
        stats.put("Killed", new PaintStatistic(() -> {
            HashMap<String, Integer> killed = Stats.getKilled();
            if(killed.size() == 0) {
                return "Nothing";
            }
            StringBuilder builder = new StringBuilder();
            killed.forEach((s, integer) -> builder.append(s).append(": ").append(integer).append(", "));
            String build = builder.toString();
            return build.substring(0, build.length() - 2);
        }));
        stats.put("Targeting Me", new PaintStatistic(() -> {
            return String.valueOf(CombatStore.getTargetingMe().size());
        }));
        stats.put("Attack Style", new PaintStatistic(() -> {
            Combat.AttackStyle style = Combat.getAttackStyle();
            return style.getName();
        }));
        stats.put("Prioritize Loot", new PaintStatistic(() -> {
           return String.valueOf(Config.getProgressive().isPrioritizeLooting());
        }));
        stats.put("Idle", new PaintStatistic(() -> {
           IdleNode node = (IdleNode) context.getSupplier().IDLE;
            String length = node.getIdleFor() + "s";
           if(node.getIdleFor() > 0) {
               return "For " + length;
           }
           return "In " + node.getKills() + " / " + node.getMax() + " Kills";
        }));
    }


    public Color getOutline() {
        return outline;
    }

    public void setOutline(Color outline) {
        this.outline = outline;
    }

    public void submit(String key, PaintStatistic tracker) {
        stats.put(key, tracker);
    }

    @Override
    public void notify(RenderEvent e) {
        Graphics2D g = (Graphics2D) e.getSource();
        Composite defaultComposite = g.getComposite();

        int width = 180;
        int currentX = BASE_X + (DEFAULT_WIDTH_INCR / 2);
        int currentY = BASE_Y + (LINE_HEIGHT / 2);

        g.setStroke(STROKE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(FOREGROUND);

        for (Map.Entry<String, PaintStatistic> entry : stats.entrySet()) {
            PaintStatistic stat = entry.getValue();
            String string = entry.getKey() + (stat.isHeading() ? " - " : ": ") + stat.toString();
            int currentWidth = g.getFontMetrics().stringWidth(string);
            if (currentWidth > width) {
                width = currentWidth;
            }
        }

        g.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g.setColor(BACKGROUND);
        g.fillRoundRect(BASE_X, BASE_Y, width + DEFAULT_WIDTH_INCR, (stats.size() * LINE_HEIGHT) + BASE_HEIGHT, 7, 7);

        g.setComposite(defaultComposite);
        g.setColor(outline);
        g.drawRoundRect(BASE_X, BASE_Y, width + DEFAULT_WIDTH_INCR, (stats.size() * LINE_HEIGHT) + BASE_HEIGHT, 7, 7);

        g.setColor(FOREGROUND);
        for (Map.Entry<String, PaintStatistic> entry : stats.entrySet()) {
            PaintStatistic stat = entry.getValue();

            String string = entry.getKey() + (stat.isHeading() ? " - " : ": ") + stat.toString();
            int drawX = currentX;
            if (stat.isHeading()) {
                drawX = BASE_X + ((width + DEFAULT_WIDTH_INCR) - g.getFontMetrics().stringWidth(string)) / 2;
                g.setColor(outline);
                g.drawRect(BASE_X, currentY + (LINE_HEIGHT / 2) - BASE_Y + 1, width + DEFAULT_WIDTH_INCR, LINE_HEIGHT);

                g.setComposite(AlphaComposite.SrcOver.derive(0.1f));
                g.fillRect(BASE_X, currentY + (LINE_HEIGHT / 2) - BASE_Y + 1, width + DEFAULT_WIDTH_INCR, LINE_HEIGHT);
                g.setComposite(defaultComposite);

                g.setFont(g.getFont().deriveFont(Font.BOLD));
            } else {
                g.setFont(g.getFont().deriveFont(Font.PLAIN));
            }

            g.setColor(FOREGROUND);
            g.drawString(string, drawX, currentY += LINE_HEIGHT);
        }
    }
}
