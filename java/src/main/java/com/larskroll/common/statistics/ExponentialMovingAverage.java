/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larskroll.common.statistics;

/**
 * Implements the simple Exponential Moving Average (EMA) with decay coefficient {@code alpha}.
 * 
 * {@code alpha} should be to be a finite number between 0.0 and 1.0;
 * 
 * @author lkroll
 */
public class ExponentialMovingAverage {
    private final double alpha; // decay
    private double mu = Double.NaN; // current mean approximation
    
    /**
     * 
     * @param alpha decay coefficient
     */
    public ExponentialMovingAverage(double alpha) {
        if (Double.isNaN(alpha) || Double.isInfinite(alpha)) {
            throw new RuntimeException("alpha must be a finite value!");
        }
        this.alpha = alpha;
    }
    
    /**
     * Add value to EMA.
     * 
     * @param x is added to the moving average
     */
    public void addValue(double x) {
        if (Double.isNaN(x)) {
            return; // can't add NaNs
        }
        if (Double.isNaN(mu)) {
            mu = x; // just take the new value as you can't calculate with NaN
            return;
        }
        mu = mu + alpha*(x-mu);
    }
    
    /**
     * Get the current EMA.
     * 
     * @return the mean estimate
     */
    public double getMean() {
        return mu;
    }
    
    @Override
    public String toString() {
        return "ExponentialMovingAverage(µ=" + mu + ")";
    }
}
