package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

/**
 * Этот класс позволяет исследовать различные области фрактала с помощью
 * создания и отображения графического интерфейса Swing и обработки событий, вызванных
 * взаимодействием приложения с пользователем.
 */
public class FractalExplorer {

    private int displaySize; //Целочисленный размер дисплея

    //Для обновления отображения в разных
    // методах в процессе вычисления фрактала
    private JImageDisplay display;

    //Для использования ссылки на базовый
    //класс для отображения других видов фракталов в будущем
    private FractalGenerator fractal;

    //Для  указания диапазона комплексной
    //плоскости, которая выводится на экран
    private Rectangle2D.Double range;

    /*
     * Конструктор, который принимает значение
     * размера отображения в качестве аргумента, затем сохраняет это значение в
     * соответствующем поле, а также инициализирует объекты диапазона и
     * фрактального генератора
     */
    public FractalExplorer(int size) {
        displaySize = size;
        //Инициализация
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    /*
    * Метод, который инициализирует
    * графический интерфейс Swing: JFrame, содержащий объект JimageDisplay, и
    * кнопку для сброса отображения
     */
    public void createAndShowGUI()
    {
        // Установливает рамку, чтобы использовать java.awt.BorderLayout для содержимого окна
        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");

        //Добавляет объект отображения изображения в позицию в BorderLayout.CENTER
        frame.add(display, BorderLayout.CENTER);

        //Кнопка сброса
        JButton resetButton = new JButton("Reset Display");

        //Экземпляр обработчика сброса в кнопке сброса.
        Reset handler = new Reset();
        resetButton.addActionListener(handler);

        //Добавление кнопки сброса в позицию BorderLayout.SOUTH
        frame.add(resetButton, BorderLayout.SOUTH);

        //Экзмепляр MouseHandler
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        //Обеспечение операции закрытия окна по умолчанию
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * Правильно разместит содержимое окна, сделает ее видимой и
         * запретит изменение размера окна.
         */
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /*
     * Вспомогательный метод с типом доступа private для отображения фрактала. Этот метод циклически проходит
     * через каждый пиксель в отображении и вычисляет количество
     * итераций для соответствующих координат.
     * Если число итераций равно -1, устанавливает цвет пикселя в черный цвет.
     * В противном случае выберает значение, основанное на количестве итераций.
     * Обновляет дисплей цветом для каждого пикселя и перекрашивает его.
     * Отображает изображение, когда все пиксели были закрашены.
     */
    private void drawFractal()
    {
        // Цикл через каждый пиксель
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){

                // Нахождение соответствующих координат (xCoord и yCoord)
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                //Вычисление количества итераций в области отображения фрактала
                int iter = fractal.numIterations(xCoord, yCoord);

                //Если число итераций равно -1, устанавливает пиксель в черный цвет
                if (iter == -1){
                    display.drawPixel(x, y, 0);
                }

                else {
                    // В ином случае устанавливает значение оттенка на основе числа итераций (из методички)
                    float hue = 0.7f + (float) iter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    //Обновление дисплея
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        //Отображает изображение, когда все пиксели закрашены
        display.repaint();
    }

    /* Для обработки событий
     * java.awt.event.ActionListener от кнопки сброса
     */
    private class Reset implements ActionListener {
        // Обработчик сбрасывает диапазон к начальному, определенному генератором,
        // а затем перерисовывает фрактал
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }

    /* Внутренний класс для обработки событий
     * java.awt.event.MouseListener с дисплея
     */
     private class MouseHandler extends MouseAdapter {
        // Обработывает события от мыши. При получении события о щелчке мышью,
        // отображает пиксельные кооринаты щелчка в область фрактала, а затем вызывает
        // метод генератора recenterAndZoomRange() с координатами, по которым
        // щелкнули, и масштабом 0.5. Таким образом, нажимая на какое-либо место на
        // фрактальном отображении, вы увеличиваете его

        public void mouseClicked(MouseEvent e)
        {
            // Получает координату x в области щелчка мыши
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            // Получает координату y в области щелчка мыши
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            // Вызовает метод recenterAndZoomRange() с помощью координат и масштаб 0,5
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            // Перерисовывает фрактал
            drawFractal();
        }
    }

    /**
     * Инициализирует новый экземпляр класса FractalExplorer с
     * размером отображения 800. Вызовает метод createAndShowGUI () класса FractalExplorer.
     * Вызовает метод drawFractal() класса FractalExplorer для отображения начального представления.
     */
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(800);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
