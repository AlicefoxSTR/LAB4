package com.company;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

public class JImageDisplay extends JComponent{
    //Класс BufferedImage управляет изображением,
    // содержимое которого можно записать.
    private BufferedImage Image;

    //Конструктор JImageDisplay принимает целочисленные
    //значения ширины и высоты, и инициализирует объект BufferedImage новым
    //изображением с этой шириной и высотой, и типом изображения
    //TYPE_INT_RGB
    public JImageDisplay(int width, int height) {
        Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Вызов метода setpreferredsize родительского класса (с заданными шириной и высотой)
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        //Мы передаем значение null для ImageObserver,
        // поскольку данная функциональность не требуется
        g.drawImage(Image, 0, 0, Image.getWidth(), Image.getHeight(), null);
    }

    //Устанавливает все пиксели в данных изображения в черный цвет
    public void clearImage()
    {
        int[] blackArray = new int[getWidth() * getHeight()];
        Image.setRGB(0, 0, getWidth(), getHeight(), blackArray, 0, 1);
    }

    //Устанавливает пиксель в определенный цвет
    public void drawPixel(int x, int y, int rgbColor) {
        Image.setRGB(x, y, rgbColor);
    }


}
