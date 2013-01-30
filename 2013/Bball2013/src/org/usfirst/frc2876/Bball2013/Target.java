/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Bball2013;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.LinearAverages;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author user
 */
public class Target {

    ParticleAnalysisReport report;
    BinaryImage filteredImage;
    BinaryImage threshImage;
    int particleNumber;
    //
    final double theta = 43.5;       //Axis 206 camera
//    final double theta = 48;       //Axis M1011 camera
    final int XMAXSIZE = 24;
    final int XMINSIZE = 24;
    final int YMAXSIZE = 24;
    final int YMINSIZE = 48;
    final double xMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    final double xMin[] = {.4, .6, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, 0.6, 0};
    final double yMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    final double yMin[] = {.4, .6, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
        .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
        .05, .05, .6, 0};
    final int RECTANGULARITY_LIMIT = 60;
    final int ASPECT_RATIO_LIMIT = 75;
    final int X_EDGE_LIMIT = 40;
    final int Y_EDGE_LIMIT = 60;
    //X Image resolution in pixels, should be 160, 320 or 640
    final int X_IMAGE_RES = 320;
//
    double distance = -1;
    double rectangularity;
    double aspectRatioOuter;
    double aspectRatioInner;
    double xEdge;
    double yEdge;
    //
    double x_aim = 0;
    double y_aim = 0;
    double turnDegrees = 0;
    Preferences pfs = Preferences.getInstance();
    //int rectScoreThresh = 75;
    int aspectScoreThresh = 900;
    int rectScoreThresh = 60;

    public Target(ParticleAnalysisReport r, int particleNumber, BinaryImage thresh, BinaryImage filter) {
        this.report = r;
        this.filteredImage = filter;
        this.threshImage = thresh;
        this.particleNumber = particleNumber;

        //aspectScoreThresh = pfs.getInt("aspectScoreThrest", 6000);
        //rectScoreThresh = pfs.getInt("rectScoreThresh", 70);
    }
    
    public void freeImages() throws NIVisionException {
        filteredImage.free();
        threshImage.free();
    }

    public double getTurnDegrees() {
        return turnDegrees;
    }

    public double getXAim() {
        return x_aim;
    }

    public double getYAim() {
        return y_aim;
    }

    public double getDistance() {
        return distance;
    }

    public String getDashString(boolean debug) {
        String str = "";

        str += " cm=(" + report.center_mass_x + "," + report.center_mass_y + ")";
        //str += " turn=" + RobotMap.roundtoTwo(turnDegrees);
        str += " Rscore=" + rectangularity + " aspectInner=" + RobotMap.roundtoTwo(aspectRatioInner)
                + " aspectOuter=" + RobotMap.roundtoTwo(aspectRatioOuter);
        str += " distance=" + RobotMap.roundtoTwo(distance);

        return str;
    }

    public String toString() {
        String str = "";
        str += " cm=(" + report.center_mass_x + "," + report.center_mass_y + ")";
        //str += " turn=" + RobotMap.roundtoTwo(turnDegrees);
        str += " Rscore=" + rectangularity + " aspectInner=" + RobotMap.roundtoTwo(aspectRatioInner)
                + " aspectOuter=" + RobotMap.roundtoTwo(aspectRatioOuter);
        str += " distance=" + RobotMap.roundtoTwo(distance);

        return str;
    }

    /**
     * Computes the estimated distance to a target using the height of the
     * particle in the image. For more information and graphics showing the math
     * behind this approach see the Vision Processing section of the
     * ScreenStepsLive documentation.
     *
     * @param image The image to use for measuring the particle estimated
     * rectangle
     * @param report The Particle Analysis Report for the particle
     * @param outer True if the particle should be treated as an outer target,
     * false to treat it as a center target
     * @return The estimated distance to the target in Inches.
     */
    public double computeDistance(boolean outer) throws NIVisionException {
        double rectShort, height;
        int targetHeight;

        rectShort = NIVision.MeasureParticle(threshImage.image, particleNumber, false,
                NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        // using the smaller of the estimated rectangle short side and the 
        // bounding rectangle height results in better performance
        // on skewed rectangles
        height = Math.min(report.boundingRectHeight, rectShort);
        targetHeight = outer ? 29 : 21;
        distance = X_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(theta * Math.PI / (180 * 2)));
        return distance;
    }

    /**
     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect
     * ratio for the target. This method uses the equivalent rectangle sides to
     * determine aspect ratio as it performs better as the target gets skewed by
     * moving to the left or right. The equivalent rectangle is the rectangle
     * with sides x and y where particle area= x*y and particle perimeter= 2x+2y
     *
     * @param image The image containing the particle to score, needed to
     * perform additional measurements
     * @param report The Particle Analysis Report for the particle, used for the
     * width, height, and particle number
     * @param outer	Indicates whether the particle aspect ratio should be
     * compared to the ratio for the inner target or the outer
     * @return The aspect ratio score (0-100)
     */
    public double scoreAspectRatio(boolean outer)
            throws NIVisionException {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(filteredImage.image, particleNumber, false,
                NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(filteredImage.image, particleNumber, false,
                NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        //Dimensions of goal opening + 4 inches on all 4 sides for reflective tape
        idealAspectRatio = outer ? (62 / 29) : (62 / 20);

        //Divide width by height to measure aspect ratio
        if (report.boundingRectWidth > report.boundingRectHeight) {
            //particle is wider than it is tall, divide long by short
            aspectRatio = 100 * (1 - Math.abs((1 - ((rectLong / rectShort) / idealAspectRatio))));
        } else {
            //particle is taller than it is wide, divide short by long
            aspectRatio = 100 * (1 - Math.abs((1 - ((rectShort / rectLong) / idealAspectRatio))));
        }
        double a = (Math.max(0, Math.min(aspectRatio, 100.0)));		//force to be in range 0-100
        if (outer) {
            aspectRatioOuter = a;
        } else {
            aspectRatioInner = a;
        }
        return a;
    }

    /**
     * Computes a score (0-100) estimating how rectangular the particle is by
     * comparing the area of the particle to the area of the bounding box
     * surrounding it. A perfect rectangle would cover the entire bounding box.
     *
     * @param report The Particle Analysis Report for the particle to score
     * @return The rectangularity score (0-100)
     */
    double scoreRectangularity() {
        if (report.boundingRectWidth * report.boundingRectHeight != 0) {
            rectangularity = 100 * report.particleArea
                    / (report.boundingRectWidth * report.boundingRectHeight);
        } else {
            rectangularity = 0;
        }
        return rectangularity;
    }

    public double scoreXEdge() throws NIVisionException {
        double total = 0;
        LinearAverages averages;

        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop,
                report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(threshImage.image,
                LinearAverages.LinearAveragesMode.IMAQ_COLUMN_AVERAGES, rect);
        float columnAverages[] = averages.getColumnAverages();
        for (int i = 0; i < (columnAverages.length); i++) {
            if (xMin[(i * (XMINSIZE - 1) / columnAverages.length)] < columnAverages[i]
                    && columnAverages[i] < xMax[i * (XMAXSIZE - 1) / columnAverages.length]) {
                total++;
            }
        }
        total = 100 * total / (columnAverages.length);
        xEdge = total;
        return total;
    }

    /**
     * Computes a score based on the match between a template profile and the
     * particle profile in the Y direction. This method uses the the row
     * averages and the profile defined at the top of the sample to look for the
     * solid horizontal edges with a hollow center
     *
     * @param image The image to use, should be the image before the convex hull
     * is performed
     * @param report The Particle Analysis Report for the particle
     *
     * @return The Y Edge score (0-100)
     *
     */
    public double scoreYEdge() throws NIVisionException {
        double total = 0;
        LinearAverages averages;

        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft,
                report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(threshImage.image, 
                LinearAverages.LinearAveragesMode.IMAQ_ROW_AVERAGES, rect);
        float rowAverages[] = averages.getRowAverages();
        for (int i = 0; i < (rowAverages.length); i++) {
            if (yMin[(i * (YMINSIZE - 1) / rowAverages.length)] < rowAverages[i]
                    && rowAverages[i] < yMax[i * (YMAXSIZE - 1) / rowAverages.length]) {
                total++;
            }
        }
        total = 100 * total / (rowAverages.length);
        yEdge = total;
        return total;
    }

//    private void calcAim() {
//        try {
//            x_aim = (center_mass_x - (imageWidth / 2)) / (imageWidth / 2);
//            y_aim = (center_mass_y - (imageHeight / 2)) / (imageHeight / 2);
//            // delta = pixel delta * pixels/deg
//            // image width, probably 320 divided by camera field of view
//            double degPerPixel = imageWidth / theta;
//            double imgCenter = imageWidth / 2;
//
//            double delta = center_mass_x - imgCenter;
//            turnDegrees = delta / degPerPixel;
//            turnDegrees = RobotMap.roundtoTwo(turnDegrees);
//        } catch (ArithmeticException ex) {
//            // System.out.println(ex);
//        }
//    }
//
//    private void calcScores() {
//        try {
//            rectScore = r.particleArea / (boundingRectHeight * boundingRectWidth) * 100;
//            rectScore = RobotMap.roundtoTwo(rectScore);
//
//            aspectScore = boundingRectWidth * boundingRectHeight;
//            aspectScore = RobotMap.roundtoTwo(aspectScore);
//        } catch (ArithmeticException ex) {
//            // System.out.println(ex);
//        }
//    }
    /**
     * Compares scores to defined limits and returns true if the particle
     * appears to be a target
     *
     * @param scores The structure containing the scores to compare
     * @param outer True if the particle should be treated as an outer target,
     * false to treat it as a center target
     *
     * @return True if the particle meets all limits, false otherwise
     */
    public boolean isValid(boolean outer) {
        boolean isTarget = true;

        isTarget &= rectangularity > RECTANGULARITY_LIMIT;
        if (outer) {
            isTarget &= aspectRatioOuter > ASPECT_RATIO_LIMIT;
        } else {
            isTarget &= aspectRatioInner > ASPECT_RATIO_LIMIT;
        }
        isTarget &= xEdge > X_EDGE_LIMIT;
        isTarget &= yEdge > Y_EDGE_LIMIT;

        return isTarget;
    }
    
    public void calcScores() throws NIVisionException {
        this.scoreAspectRatio(true);
        this.scoreAspectRatio(false);
        this.scoreRectangularity();
        this.scoreXEdge();
        this.scoreYEdge();
        
    }
}
