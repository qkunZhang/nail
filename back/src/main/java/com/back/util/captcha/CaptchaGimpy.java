package com.back.util.captcha;

import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

//验证码干扰器
public class CaptchaGimpy extends Configurable implements GimpyEngine {
    public CaptchaGimpy() {
    }

    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        Graphics2D graph = (Graphics2D) baseImage.getGraphics();
        int imageHeight = baseImage.getHeight();
        int imageWidth = baseImage.getWidth();
        int horizontalLines = imageHeight / 7;
        int verticalLines = imageWidth / 7;
        int horizontalGaps = imageHeight / (horizontalLines + 1);
        int verticalGaps = imageWidth / (verticalLines + 1);

        int i;
        int a0 = horizontalGaps * 3;
        int a1 = horizontalGaps * 4;
        Random random = new Random();
        for (i = horizontalGaps; i < imageHeight; i += horizontalGaps) {
            if ((i / 2) % 2 == 0) {
                graph.setColor(Color.white);
            } else {
                graph.setColor(Color.red);
            }
            if (i == a0 || i == a1) {
                graph.setColor(new Color(random.nextInt(255) , random.nextInt(255) , random.nextInt(255) ));
            }
            graph.setStroke(new BasicStroke(0.1f));
            graph.drawLine(0, i, imageWidth, i);
        }


        int b1 = verticalGaps * 10;
        int b2 = verticalGaps * 11;
        int b3 = verticalGaps * 12;
        int b4 = verticalGaps * 13;
        for (i = verticalGaps; i < imageWidth; i += verticalGaps) {
            if ((i / 2) % 2 == 0) {
                graph.setColor(Color.green);
            } else {
                graph.setColor(Color.blue);
            }
            if (i == b1 || i == b2 || i == b3 || i == b4) {
                graph.setColor(new Color(random.nextInt(255) , random.nextInt(255) , random.nextInt(255) ));
            }
            graph.setStroke(new BasicStroke(0.1f));
            graph.drawLine(i, 0, i, imageHeight);
        }

        int[] pix = new int[imageHeight * imageWidth];
        int j = 0;

        for (int j1 = 0; j1 < imageWidth; ++j1) {
            for (int k1 = 0; k1 < imageHeight; ++k1) {
                pix[j] = baseImage.getRGB(j1, k1);
                ++j;
            }
        }

        double distance = (double) this.ranInt(imageWidth / 4, imageWidth / 3);
        int widthMiddle = baseImage.getWidth() / 2;
        int heightMiddle = baseImage.getHeight() / 2;

        for (int x = 0; x < baseImage.getWidth(); ++x) {
            for (int y = 0; y < baseImage.getHeight(); ++y) {
                int relX = x - widthMiddle;
                int relY = y - heightMiddle;
                double d1 = Math.sqrt((double) (relX * relX + relY * relY));
                if (d1 < distance) {
                    int j2 = widthMiddle + (int) (this.fishEyeFormula(d1 / distance) * distance / d1 * (double) (x - widthMiddle));
                    int k2 = heightMiddle + (int) (this.fishEyeFormula(d1 / distance) * distance / d1 * (double) (y - heightMiddle));
                    baseImage.setRGB(x, y, pix[j2 * imageHeight + k2]);
                }
            }
        }

        NoiseProducer noiseProducer = new CaptchaNoise();
        noiseProducer.makeNoise(baseImage, 0.1F, 0.1F, 0.25F, 0.25F);
        return baseImage;
    }

    private int ranInt(int i, int j) {
        double d = Math.random();
        return (int) ((double) i + (double) (j - i + 1) * d);
    }

    private double fishEyeFormula(double s) {
        if (s < 0.0) {
            return 0.0;
        } else {
            return s > 1.0 ? s : -0.75 * s * s * s + 1.5 * s * s + 0.25 * s;
        }
    }
}
