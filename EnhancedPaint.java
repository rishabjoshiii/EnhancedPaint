import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EnhancedPaint extends JFrame {
    private final DrawPanel drawPanel;
    private Color currentColor = Color.BLACK;
    private int currentStrokeSize = 5;

    public EnhancedPaint() {
        setTitle("Enhanced Paint Application");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> drawPanel.clear());

        JButton colorButton = new JButton("Color");
        colorButton.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(this, "Choose a color", currentColor);
            if (chosenColor != null) {
                currentColor = chosenColor;
                drawPanel.setCurrentColor(chosenColor);
            }
        });

        Integer[] strokeSizes = {1, 2, 3, 4, 5, 10, 15, 20};
        JComboBox<Integer> strokeSizeComboBox = new JComboBox<>(strokeSizes);
        strokeSizeComboBox.setSelectedItem(currentStrokeSize);
        strokeSizeComboBox.addActionListener(e -> {
            currentStrokeSize = (Integer) strokeSizeComboBox.getSelectedItem();
            drawPanel.setCurrentStrokeSize(currentStrokeSize);
        });

        controlPanel.add(clearButton);
        controlPanel.add(colorButton);
        controlPanel.add(new JLabel("Stroke Size:"));
        controlPanel.add(strokeSizeComboBox);
        add(controlPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedPaint enhancedPaint = new EnhancedPaint();
            enhancedPaint.setVisible(true);
        });
    }

    class DrawPanel extends JPanel {
        private final List<ColoredLine> lines = new ArrayList<>();
        private int startX, startY, endX, endY;
        private Color currentColor = Color.BLACK;
        private int currentStrokeSize = 5;

        public DrawPanel() {
            setBackground(Color.WHITE);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startX = e.getX();
                    startY = e.getY();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    endX = e.getX();
                    endY = e.getY();
                    lines.add(new ColoredLine(startX, startY, endX, endY, currentColor, currentStrokeSize));
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    endX = e.getX();
                    endY = e.getY();
                    lines.add(new ColoredLine(startX, startY, endX, endY, currentColor, currentStrokeSize));
                    startX = endX;
                    startY = endY;
                    repaint();
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        public void clear() {
            lines.clear();
            repaint();
        }

        public void setCurrentColor(Color color) {
            this.currentColor = color;
        }

        public void setCurrentStrokeSize(int strokeSize) {
            this.currentStrokeSize = strokeSize;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (ColoredLine line : lines) {
                g2d.setColor(line.color);
                g2d.setStroke(new BasicStroke(line.strokeSize));
                g2d.drawLine(line.startX, line.startY, line.endX, line.endY);
            }
        }
    }

    class ColoredLine {
        final int startX, startY, endX, endY;
        final Color color;
        final int strokeSize;

        public ColoredLine(int startX, int startY, int endX, int endY, Color color, int strokeSize) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.color = color;
            this.strokeSize = strokeSize;
        }
    }
}
