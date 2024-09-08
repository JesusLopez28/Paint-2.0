import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Paint2 extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    private Color currentColor = Color.BLACK;
    private String currentShape = "Línea";
    private BasicStroke currentStroke = new BasicStroke(2);
    private int startX, startY, endX, endY;
    private boolean drawing = false;
    private boolean eraseShape = false;
    private String rootPath = "src/icons/";

    private ArrayList<ShapeInfo> shapes;

    public Paint2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        setSize(800, 600);
        setResizable(false);
        setTitle("Paint 2.0");

        shapes = new ArrayList<>();

        JMenuBar menuBar = new JMenuBar();

        // agregar iconos a los menus de la carpeta icons

        JMenu menuFiguras = new JMenu("Figuras");
        JMenuItem linea = new JMenuItem("Línea", new ImageIcon(rootPath + "linea.png"));
        JMenuItem circulo = new JMenuItem("Círculo/Ovalo", new ImageIcon(rootPath + "circulo.png"));
        JMenuItem rectangulo = new JMenuItem("Rectángulo/Cuadrado", new ImageIcon(rootPath + "rectangulo.png"));
        linea.addActionListener(this);
        circulo.addActionListener(this);
        rectangulo.addActionListener(this);
        menuFiguras.add(linea);
        menuFiguras.add(circulo);
        menuFiguras.add(rectangulo);
        menuBar.add(menuFiguras);

        JMenu menuColores = new JMenu("Colores");
        JMenuItem colorRojo = new JMenuItem("Rojo", new ImageIcon(rootPath + "rojo.png"));
        JMenuItem colorVerde = new JMenuItem("Verde", new ImageIcon(rootPath + "verde.png"));
        JMenuItem colorAzul = new JMenuItem("Azul", new ImageIcon(rootPath + "azul.png"));
        colorRojo.addActionListener(this);
        colorVerde.addActionListener(this);
        colorAzul.addActionListener(this);
        menuColores.add(colorRojo);
        menuColores.add(colorVerde);
        menuColores.add(colorAzul);
        menuBar.add(menuColores);

        JMenu menuLineas = new JMenu("Tipos de Líneas");
        JMenuItem solida = new JMenuItem("Sólida", new ImageIcon(rootPath + "linea.png"));
        JMenuItem punteada = new JMenuItem("Punteada", new ImageIcon(rootPath + "linea_punteada.png"));
        JMenuItem discontinua = new JMenuItem("Discontinua", new ImageIcon(rootPath + "linea_discontinua.png"));
        solida.addActionListener(this);
        punteada.addActionListener(this);
        discontinua.addActionListener(this);
        menuLineas.add(solida);
        menuLineas.add(punteada);
        menuLineas.add(discontinua);
        menuBar.add(menuLineas);

        // Menú de borrar
        JMenu menuBorrar = new JMenu("Borrar");
        JMenuItem borrarPantalla = new JMenuItem("Borrar Pantalla", new ImageIcon(rootPath + "borrar_pantalla.png"));
        JMenuItem borrarFigura = new JMenuItem("Borrar Figura", new ImageIcon(rootPath + "borrar_figura.png"));
        borrarPantalla.addActionListener(this);
        borrarFigura.addActionListener(this);
        menuBorrar.add(borrarPantalla);
        menuBorrar.add(borrarFigura);
        menuBar.add(menuBorrar);

        setJMenuBar(menuBar);

        // Cambiar cursor a default cuando el mouse esté sobre el menú
        menuFiguras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        });

        menuColores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        });

        menuLineas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        });

        menuBorrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        });

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        for (ShapeInfo shapeInfo : shapes) {
            g2d.setColor(shapeInfo.color);
            g2d.setStroke(shapeInfo.stroke);

            switch (shapeInfo.shapeType) {
                case "Línea":
                    g2d.drawLine(shapeInfo.startX, shapeInfo.startY, shapeInfo.endX, shapeInfo.endY);
                    break;
                case "Círculo/Ovalo":
                    g2d.drawOval(Math.min(shapeInfo.startX, shapeInfo.endX), Math.min(shapeInfo.startY, shapeInfo.endY),
                            Math.abs(shapeInfo.endX - shapeInfo.startX), Math.abs(shapeInfo.endY - shapeInfo.startY));
                    break;
                case "Rectángulo/Cuadrado":
                    g2d.drawRect(Math.min(shapeInfo.startX, shapeInfo.endX), Math.min(shapeInfo.startY, shapeInfo.endY),
                            Math.abs(shapeInfo.endX - shapeInfo.startX), Math.abs(shapeInfo.endY - shapeInfo.startY));
                    break;
            }
        }

        if (drawing) {
            g2d.setColor(currentColor);
            g2d.setStroke(currentStroke);

            switch (currentShape) {
                case "Línea":
                    g2d.drawLine(startX, startY, endX, endY);
                    break;
                case "Círculo/Ovalo":
                    g2d.drawOval(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
                    break;
                case "Rectángulo/Cuadrado":
                    g2d.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Cambiar figura
        if (command.equals("Línea") || command.equals("Círculo/Ovalo") || command.equals("Rectángulo/Cuadrado")) {
            currentShape = command;
            ;
            eraseShape = false;
        }

        // Cambiar color
        if (command.equals("Rojo")) {
            currentColor = Color.RED;
            ;
            eraseShape = false;
        } else if (command.equals("Verde")) {
            currentColor = Color.GREEN;
            ;
            eraseShape = false;
        } else if (command.equals("Azul")) {
            currentColor = Color.BLUE;
            ;
            eraseShape = false;
        }

        // Cambiar tipo de línea
        if (command.equals("Sólida")) {
            currentStroke = new BasicStroke(2);
        } else if (command.equals("Punteada")) {
            float[] dashPattern = {10, 10};
            currentStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
        } else if (command.equals("Discontinua")) {
            float[] dashPattern = {20, 10, 5, 10};
            currentStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
        }

        // Borrar pantalla
        if (command.equals("Borrar Pantalla")) {
            shapes.clear(); // Eliminar todas las figuras
            repaint();
        }

        // Borrar figura
        if (command.equals("Borrar Figura")) {
            eraseShape = true; // Habilitar el modo para borrar figuras individuales
        }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (eraseShape) {
            // Intentar borrar figura
            ShapeInfo shapeToRemove = null;
            for (ShapeInfo shape : shapes) {
                if (shape.contains(e.getX(), e.getY())) {
                    shapeToRemove = shape;
                    break;
                }
            }

            if (shapeToRemove != null) {
                shapes.remove(shapeToRemove);
                repaint();
            }
        } else {
            // Al presionar el mouse, tomar las coordenadas iniciales
            startX = e.getX();
            startY = e.getY();
            drawing = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!eraseShape) {
            // Al soltar el mouse, tomar las coordenadas finales y guardar la figura
            endX = e.getX();
            endY = e.getY();
            drawing = false;

            shapes.add(new ShapeInfo(currentShape, startX, startY, endX, endY, currentColor, currentStroke));

            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!eraseShape) {
            // Durante el arrastre, actualizar las coordenadas finales
            endX = e.getX();
            endY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (eraseShape) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    public static void main(String[] args) {
        Paint2 ventana = new Paint2();
        ventana.setVisible(true);
    }
}

class ShapeInfo {
    String shapeType;
    int startX, startY, endX, endY;
    Color color;
    BasicStroke stroke;

    public ShapeInfo(String shapeType, int startX, int startY, int endX, int endY, Color color, BasicStroke stroke) {
        this.shapeType = shapeType;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.stroke = stroke;
    }

    public boolean contains(int x, int y) {
        switch (shapeType) {
            case "Línea":
                // Verificar si el clic está cerca de la línea
                return distanceFromLine(startX, startY, endX, endY, x, y) < 5;
            case "Círculo/Ovalo":
            case "Rectángulo/Cuadrado":
                Rectangle bounds = new Rectangle(Math.min(startX, endX), Math.min(startY, endY),
                        Math.abs(endX - startX), Math.abs(endY - startY));
                return bounds.contains(x, y);
            default:
                return false;
        }
    }

    private double distanceFromLine(int x1, int y1, int x2, int y2, int px, int py) {
        double normalLength = Math.hypot(x2 - x1, y2 - y1);
        return Math.abs((px - x1) * (y2 - y1) - (py - y1) * (x2 - x1)) / normalLength;
    }
}
