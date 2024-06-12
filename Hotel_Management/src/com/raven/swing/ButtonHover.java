package com.raven.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class ButtonHover extends JButton {

    private final Animator expandAnimator;
    private final Animator collapseAnimator;
    private int targetSize;
    private float animatSize;
    private float currentAnimatSize;  // Lưu trữ kích thước hiện tại của hiệu ứng
    private Point pressedPoint;
    private float alpha;
    private final Color effectColor = new Color(10, 10, 255);
    private Color originalForeground;

    public ButtonHover() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        originalForeground = getForeground(); // Lưu trữ màu chữ gốc
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                startAnimation(me.getPoint());
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                startAnimation(me.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent me) {
                reverseAnimation();
            }
        });

        TimingTarget expandTarget = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animatSize = fraction * targetSize;
                currentAnimatSize = animatSize;
                setForeground(new Color(255, 255, 255, (int) (255 * fraction))); // Đổi màu chữ trong khi lan
                repaint();
            }

            @Override
            public void end() {
                setForeground(Color.WHITE); // Giữ màu trắng khi lan hết
            }
        };

        TimingTarget collapseTarget = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animatSize = (1 - fraction) * currentAnimatSize;  // Sử dụng kích thước hiện tại của hiệu ứng
                alpha = 0.5f * (1 - fraction);
                setForeground(new Color(
                        (int) (originalForeground.getRed() * fraction + 255 * (1 - fraction)),
                        (int) (originalForeground.getGreen() * fraction + 255 * (1 - fraction)),
                        (int) (originalForeground.getBlue() * fraction + 255 * (1 - fraction))
                )); // Đổi màu chữ trong khi thu lại
                repaint();
            }
        };

        expandAnimator = new Animator(800, expandTarget);
        expandAnimator.setResolution(0);

        collapseAnimator = new Animator(800, collapseTarget);
        collapseAnimator.setResolution(0);
    }

    private void startAnimation(Point point) {
        targetSize = Math.max(getWidth(), getHeight()) * 2;
        animatSize = 0;
        pressedPoint = point;
        alpha = 0.5f;
        if (expandAnimator.isRunning()) {
            expandAnimator.stop();
        }
        if (collapseAnimator.isRunning()) {
            collapseAnimator.stop();
        }
        expandAnimator.start();
    }

    private void reverseAnimation() {
        if (expandAnimator.isRunning()) {
            expandAnimator.stop();
        }
        if (collapseAnimator.isRunning()) {
            collapseAnimator.stop();
        }
        collapseAnimator.start();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        int height = getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        
        // Set rendering hints for better graphics quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw button background
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, height, height);

        // Draw border
        g2.setColor(new Color(92, 59, 197)); // Set border color
        g2.drawRoundRect(0, 0, width - 1, height - 1, height, height); // Draw border with a thickness of 1 pixel
        
        // Draw ripple effect
        if (pressedPoint != null) {
            g2.setColor(effectColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g2.fillOval((int) (pressedPoint.x - animatSize / 2), (int) (pressedPoint.y - animatSize / 2), (int) animatSize, (int) animatSize);
        }
        
        g2.dispose();
        grphcs.drawImage(img, 0, 0, null);
        
        // Draw the text with the current foreground color
        super.paintComponent(grphcs);
    }
}
