package com.back.util.captcha;

import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaNoise extends Configurable implements NoiseProducer {
    public CaptchaNoise() {
    }

    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
        int width = image.getWidth();
        int height = image.getHeight();
        Random rand = new Random();

        // 随机增加点的噪音
        Graphics2D graph = (Graphics2D) image.getGraphics();
        for (int i = 0; i < width * height * 0.005; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (rand.nextInt(2)==1){
                graph.setColor(Color.green);
                graph.fillRect(x, y, 1, 3);
            }else {
                graph.setColor(Color.blue);
                graph.fillRect(x, y, 3, 1);
            }

        }
        graph.dispose();
    }
}

