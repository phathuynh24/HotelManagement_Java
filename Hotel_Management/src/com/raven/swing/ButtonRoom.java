package com.raven.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class ButtonRoom extends JButton {

    private final Animator expandAnimator;
    private final Animator collapseAnimator;
    private int targetSize;
    private float animatSize;
    private float currentAnimatSize;  // Lưu trữ kích thước hiện tại của hiệu ứng
    private Point pressedPoint;
    private float alpha;
    private final Color effectColor = new Color(10, 10, 255);
    private Color originalForeground;
    private final Font originalFont;
    private final Font boldFont;
    private boolean isBooked;
    private Color defaultBackground;
    private Color hoverBackground;

    public ButtonRoom(boolean isBooked) {
        this.isBooked = isBooked;
        if (isBooked) {
            defaultBackground = new Color(255, 75, 75); // Màu mặc định khi đã đặt chỗ
            hoverBackground = new Color(255, 100, 100); // Màu hover khi đã đặt chỗ
        } else {
            defaultBackground = new Color(75, 255, 75); // Màu mặc định khi chưa đặt chỗ
            hoverBackground = new Color(100, 255, 100); // Màu hover khi chưa đặt chỗ
        }
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        originalForeground = getForeground(); // Lưu trữ màu chữ gốc
        originalFont = getFont(); // Lưu trữ font gốc
        boldFont = originalFont.deriveFont(Font.BOLD); // Tạo font đậm

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
                setFont(boldFont); // Đổi sang font đậm
                repaint();
            }

            @Override
            public void end() {
                setForeground(Color.white); // Giữ màu trắng khi lan hết
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
                setFont(originalFont); // Trở lại font gốc
                repaint();
            }

            @Override
            public void end() {
                setForeground(originalForeground); // Giữ màu chữ gốc khi thu hết
                setFont(originalFont); // Trở lại font gốc khi thu hết
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
        // Draw button background
        if (getModel().isPressed()) {
            g2.setColor(hoverBackground);
        } else {
            g2.setColor(defaultBackground);
        }
        g2.fillRoundRect(0, 0, width, height, 10, 10); // Fill a rectangle with rounded corners

        // Draw border
        g2.setColor(new Color(92, 59, 197)); // Set border color
        g2.drawRoundRect(0, 0, width - 1, height - 1, 10, 10); // Draw border with rounded corners

        // Draw ripple effect
        if (pressedPoint != null) {
            g2.setColor(effectColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            int diameter = (int) (animatSize * 1.35);
            int x = pressedPoint.x - diameter / 2;
            int y = pressedPoint.y - diameter / 2;
            g2.fillOval(x, y, diameter, diameter);
        }

        g2.dispose();
        grphcs.drawImage(img, 0, 0, null);

        // Draw the text with the current foreground color
        super.paintComponent(grphcs);
    }
}
