/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Noise_Generator;

import classes.ImageAccess;
import classes.Noise;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antonio
 */
public class Noise_Generator_ implements PlugInFilter {
//TODO Create 16-bits output

    ImageProcessor ip;
    ImageAccess noise;
    ImageAccess img;
    ImagePlus stack;
    ImageStack noise_stack;
    boolean isStack;
    Noise n = new Noise();
    String[] noiseTypes = {"Uniform", "Salt and Pepper", "Gaussian", "Raylegh", "Pink", "Blue", "Purple", "Brown"};
    String[] pixelDepth = {"8-bits Gray", "32-bits Gray"};
    String imgTitle;

    @Override
    public int setup(String string, ImagePlus ip) {

        if (ip != null && ip.getNSlices() == 1) {
            imgTitle = ip.getTitle();
            img = new ImageAccess(ip.getProcessor());
            isStack = false;
            return DOES_8G + DOES_16 + DOES_32;
        } else if (ip != null && ip.getNSlices() > 1) {
            imgTitle = ip.getTitle();
            stack = ip.duplicate();
            noise_stack = new ImageStack(stack.getWidth(), stack.getHeight());
            isStack = true;
            return DOES_8G + DOES_16 + DOES_32;
        }
        return 0;
    }

    @Override
    public void run(ImageProcessor ip) {

        try {
            if (isStack) {
                useAddStackImageDialog();
            } else {
                useAddImageDialog();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Noise_Generator_.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void useAddImageDialog() throws InterruptedException {
        noise = new ImageAccess(WindowManager.getCurrentImage().getProcessor());
        GenericDialog gd = new GenericDialog("Noise Generator");
        gd.setOKLabel("Add Noise");
        gd.addChoice("Noise type", noiseTypes, noiseTypes[0]);
        gd.addNumericField("Intensity (%)", 5, 3);
        gd.addChoice("Pixel depth", pixelDepth, pixelDepth[0]);

        gd.showDialog();

        if (gd.wasCanceled()) {
            return;
        }

        if (gd.wasOKed()) {
            int nx = img.getWidth();
            int ny = img.getHeight();
            int nType = gd.getNextChoiceIndex();
            double nIntensity = gd.getNextNumber();
            String pixel = pixelDepth[gd.getNextChoiceIndex()];

            if (pixel.equals(pixelDepth[0])) {
                switch (nType) {
                    case 0:
                        nIntensity *= 2.55;
                        noise = n.generateUniformNoise(noise, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Uniform Noise", img.createByteProcessor()).show();
                        break;
                    case 1:
                        noise = n.generateSaltAndPepperNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Salt and Pepper Noise", img.createByteProcessor()).show();
                        break;
                    case 2:
                        noise = n.generateGaussianNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Gaussian Noise", img.createByteProcessor()).show();
                        break;
                    case 3:
                        noise = n.generateRayleighNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Rayleigh Noise", img.createByteProcessor()).show();
                        break;
                    case 4:
                        noise = n.generatefNoise(img, 1.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Pink Noise", img.createByteProcessor()).show();
                        break;
                    case 5:
                        noise = n.generatefNoise(img, -1.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Blue Noise", img.createByteProcessor()).show();
                        break;
                    case 6:
                        noise = n.generatefNoise(img, -2.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Purple Noise", img.createByteProcessor()).show();
                        break;
                    case 7:
                        noise = n.generatefNoise(img, 2.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Brown Noise", img.createByteProcessor()).show();
                        break;
                }

            } else if (pixel.equals(pixelDepth[1])) {
                switch (nType) {
                    case 0:
                        nIntensity *= 2.55;
                        noise = n.generateUniformNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Uniform Noise", img.createFloatProcessor()).show();
                        break;
                    case 1:
                        noise = n.generateSaltAndPepperNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Salt and Pepper Noise", img.createFloatProcessor()).show();
                        break;
                    case 2:
                        noise = n.generateGaussianNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Gaussian Noise", img.createFloatProcessor()).show();
                        break;
                    case 3:
                        noise = n.generateRayleighNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Rayleigh Noise", img.createFloatProcessor()).show();
                        break;
                    case 4:
                        noise = n.generatefNoise(img, 1.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Pink Noise", img.createFloatProcessor()).show();
                        break;
                    case 5:
                        noise = n.generatefNoise(img, -1.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Blue Noise", img.createFloatProcessor()).show();
                        break;
                    case 6:
                        noise = n.generatefNoise(img, -2.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Purple Noise", img.createFloatProcessor()).show();
                        break;
                    case 7:
                        noise = n.generatefNoise(img, 2.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Brown Noise", img.createFloatProcessor()).show();
                        break;
                }
            }

        }
    }

    private void useAddStackImageDialog() throws InterruptedException {
//        noise = new ImageAccess(WindowManager.getCurrentImage().getProcessor());
        GenericDialog gd = new GenericDialog("Noise Generator");
        gd.setOKLabel("Add Stack Noise");
        gd.addChoice("Noise type", noiseTypes, noiseTypes[0]);
        gd.addNumericField("Intensity (%)", 5, 3);
        gd.addChoice("Pixel depth", pixelDepth, pixelDepth[0]);

        gd.showDialog();

        if (gd.wasCanceled()) {
            return;
        }

        if (gd.wasOKed()) {
            int nx = stack.getWidth();
            int ny = stack.getHeight();
            int nType = gd.getNextChoiceIndex();
            double nIntensity = gd.getNextNumber();
            String pixel = pixelDepth[gd.getNextChoiceIndex()];

            if (pixel.equals(pixelDepth[0])) {
                switch (nType) {
                    case 0:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            nIntensity *= 2.55;
                            noise = n.generateUniformNoise(noise, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                            noise_stack.addSlice(img.createByteProcessor());
                        }
                        new ImagePlus(imgTitle + " plus Uniform Noise", noise_stack).show();
                        break;
                    case 1:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generateSaltAndPepperNoise(nx, ny, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                            noise_stack.addSlice(img.createByteProcessor());
                        }
                        new ImagePlus(imgTitle + " plus Salt and Pepper Noise", noise_stack).show();
                        break;
                    case 2:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generateGaussianNoise(nx, ny, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                            noise_stack.addSlice(img.createByteProcessor());
                        }
                        new ImagePlus(imgTitle + " plus Gaussian Noise", noise_stack).show();
                        break;
                    case 3:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generateRayleighNoise(nx, ny, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Rayleigh Noise", noise_stack).show();
                        break;
                    case 4:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, 1.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Pink Noise", noise_stack).show();
                        break;
                    case 5:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, -1.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Blue Noise", noise_stack).show();
                        break;
                    case 6:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, -2.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Purple Noise", noise_stack).show();
                        break;
                    case 7:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, 2.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Brown Noise", noise_stack).show();
                        break;
                }

            } else if (pixel.equals(pixelDepth[1])) {
                switch (nType) {
                    case 0:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            nIntensity *= 2.55;
                            noise = n.generateUniformNoise(noise, nIntensity).duplicate();
                            ip = noise.createFloatProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                            noise_stack.addSlice(img.createFloatProcessor());
                        }
                        new ImagePlus(imgTitle + " plus Uniform Noise", noise_stack).show();
                        break;
                    case 1:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generateSaltAndPepperNoise(nx, ny, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                            noise_stack.addSlice(img.createByteProcessor());
                        }
                        new ImagePlus(imgTitle + " plus Salt and Pepper Noise", noise_stack).show();
                        break;
                    case 2:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generateGaussianNoise(nx, ny, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                            noise_stack.addSlice(img.createByteProcessor());
                        }
                        new ImagePlus(imgTitle + " plus Gaussian Noise", noise_stack).show();
                        break;
                    case 3:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generateRayleighNoise(nx, ny, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Rayleigh Noise", noise_stack).show();
                        break;
                    case 4:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, 1.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Pink Noise", noise_stack).show();
                        break;
                    case 5:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, -1.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Blue Noise", noise_stack).show();
                        break;
                    case 6:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, -2.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Purple Noise", noise_stack).show();
                        break;
                    case 7:
                        for (int slice = 0; slice < stack.getNSlices(); slice++) {
                            IJ.showProgress(slice, stack.getNSlices());
                            stack.setSlice(slice);
                            noise = new ImageAccess(stack.getProcessor());
                            img = new ImageAccess(stack.getProcessor());

                            noise = n.generatefNoise(img, 2.0, nIntensity).duplicate();
                            ip = noise.createByteProcessor();
                            ip.abs();
                            noise = new ImageAccess(ip);
                            img.add(img, noise);
                        }
                        new ImagePlus(imgTitle + " plus Brown Noise", noise_stack).show();
                        break;
                }
            }

        }
    }

}
