package com.wuja6.android.getimageedgeapp.param;

/**
 * Created by Scenery on 2016/8/9.
 */
public class CannyParam {
    private String input;
    private String outputinput;
    private double low_thresholdinput;
    private double high_thresholdinput;
    private int kernel_size;

    public CannyParam(String input, String outputinput, double low_thresholdinput, double high_thresholdinput, int kernel_size) {
        this.input = input;
        this.outputinput = outputinput;
        this.low_thresholdinput = low_thresholdinput;
        this.high_thresholdinput = high_thresholdinput;
        this.kernel_size = kernel_size;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return outputinput;
    }

    public void setOutput(String outputinput) {
        this.outputinput = outputinput;
    }

    public double getLow_threshold() {
        return low_thresholdinput;
    }

    public void setLow_threshold(double low_thresholdinput) {
        this.low_thresholdinput = low_thresholdinput;
    }

    public double getHigh_threshold() {
        return high_thresholdinput;
    }

    public void setHigh_threshold(double high_thresholdinput) {
        this.high_thresholdinput = high_thresholdinput;
    }

    public int getKernel_size() {
        return kernel_size;
    }

    public void setKernel_size(int kernel_size) {
        this.kernel_size = kernel_size;
    }
}
