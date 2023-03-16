package com.example.roadioapp;

import java.util.ArrayList;

/**
 * Extracts Z-Values from recorded data.
 */
public class FeatureExtractionService {


    /**
     * Computes the Z-Mean of the array containing the vertical readings
     * @param arr
     * @return
     */
    public double computeZMean(ArrayList<Double> arr){
        // Check if the array is empty
        if (arr.size() == 0) {
            return 0.0;
        }

        // Compute the sum of the array
        double sum = 0.0;
        for (double num : arr) {
            sum += num;
        }

        // Compute and return the mean
        return sum / arr.size();
    }

    /**
     * Computes the Z-Variance of the array containing the vertical readings
     * @param arr
     * @return
     */
    public double computeZVariance(ArrayList<Double> arr){
        // Compute the mean of the array
        double mean = computeZMean(arr);

        // Compute the variance of the array
        double variance = 0.0;
        for (double num : arr) {
            variance += Math.pow(num - mean, 2);
        }
        variance /= arr.size();

        // Return the Z Variance value
        return variance;

    }

    /**
     *  Computes the Z-standard deviation of the array containing the vertical readings
     * @param arr
     * @return
     */
    public double computeZSDV(ArrayList<Double> arr){
        // Compute the variance of the array
        double variance = computeZVariance(arr);

        // Compute and return the standard deviation of the array
        return Math.sqrt(variance);
    }

    /**
     *  Computes the highest z-value of the array containing the vertical readings
     * @param arr
     * @return
     */
    public double computeZHigh(ArrayList<Double> arr){
        // Find the maximum Z Value
        double max = arr.get(0);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) > max) {
                max = arr.get(i);
            }
        }
        // Return the highest Z Value
        return max;
    }

    /**
     * Computes the lowest z-value of the array containing the vertical readings
     * @param arr
     * @return
     */
    public double computeZLow(ArrayList<Double> arr){
        // Find the minimum Z Value
        double min = arr.get(0);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) < min) {
                min = arr.get(i);
            }
        }

        // Return the lowest Z Value
        return min;
    }
}
