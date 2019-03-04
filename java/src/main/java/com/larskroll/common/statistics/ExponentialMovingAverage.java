/* 
* Copyright 2019 Lars Kroll
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
* associated documentation files (the "Software"), to deal in the Software without restriction, 
* including without limitation the rights to use, copy, modify, merge, publish, distribute, 
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
