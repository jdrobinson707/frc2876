/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author user
 */
public class Target {

    ParticleAnalysisReport r;
    double theta = 0;
    int h_target = 24;
    int w_target = 18;
    double w_distance = -1;
    double w_inches = 0;
    double h_distance = -1;
    double h_inches = 0;
    double x_aim = 0;
    double y_aim = 0;
    double turnDegrees = 0;
    double rectScore = 0;
    double aspectScore = 0;
    int imageWidth = 0;
    int imageHeight = 0;
    int center_mass_x;
    int center_mass_y;
    int boundingRectWidth;
    int boundingRectHeight;
    Preferences pfs = Preferences.getInstance();
    int rectScoreThresh = 50;
    int aspectScoreThresh = 0;

    public Target(ParticleAnalysisReport r, double theta) {
        this.r = r;
        this.theta = theta;
        if (r != null) {
            imageWidth = r.imageWidth;
            imageHeight = r.imageHeight;
            center_mass_x = r.center_mass_x;
            center_mass_y = r.center_mass_y;
            boundingRectHeight = r.boundingRectHeight;
            boundingRectWidth = r.boundingRectWidth;
        }
        //aspectScoreThresh = pfs.getInt("aspectScoreThrest", 6000);
        //rectScoreThresh = pfs.getInt("rectScoreThresh", 70);
    }

    public boolean isValid() {
        if (rectScore > rectScoreThresh
                && aspectScore > aspectScoreThresh) {
            return true;
        }
        return false;
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
        return h_distance;
    }

    public void analyze() {
        if (r != null) {
            calcWidthDistance();
            calcHeightDistance();
            calcAim();
            calcScores();
        }
    }

    public int getXCoM() {
        return center_mass_x;
    }

    public int getYCoM() {
        return center_mass_y;
    }

    public String getDashString(boolean debug) {
        String str = "";

        str += " cm=(" + center_mass_x + "," + center_mass_y + ")";
        str += " turn="  + RobotMap.roundtoTwo(turnDegrees);
        str += " Rscore=" + rectScore + " Ascore=" + aspectScore;
        str += " W:" + " distance=" + RobotMap.roundtoTwo(w_distance);
        if (debug) {
            
            str += " H:" + " distance=" + RobotMap.roundtoTwo(h_distance);
        }
        return str;
    }

    public String debugString() {
        String str = "theta=" + theta;
        if (w_distance > 0) {
            str += " W:"
                    + " tgt=" + w_target
                    + " imgpix=" + imageWidth
                    + " pix=" + boundingRectWidth
                    + " inches=" + RobotMap.roundtoTwo(w_inches)
                    + " distance=" + RobotMap.roundtoTwo(w_distance)
                    + "\n";
        }
        if (h_distance > 0) {
            str += " H:"
                    + " tgt=" + h_target
                    + " imgpix=" + imageHeight
                    + " pix=" + boundingRectHeight
                    + " inches=" + RobotMap.roundtoTwo(h_inches)
                    + " distance=" + RobotMap.roundtoTwo(h_distance)
                    + "\n";
        }
        str += "cm=(" + center_mass_x + "," + center_mass_y + ")"
                + " aim=("
                + RobotMap.roundtoTwo(x_aim) + "," + RobotMap.roundtoTwo(y_aim)
                + ")"
                + " turn=" + RobotMap.roundtoTwo(turnDegrees)
                + "\n";

        str += " Rscore=" + rectScore + " Ascore=" + aspectScore;
        return str;
    }

    public String toString() {
        String str = "";
        str += " cm=(" + center_mass_x + "," + center_mass_y + ")";
        str += " turn=" + RobotMap.roundtoTwo(turnDegrees);
        str += " Rscore=" + rectScore + " Ascore=" + aspectScore;
        return str;
    }

    private void calcWidthDistance() {
        try {
            int target_w_pix = imageWidth;
            int image_w_pix = boundingRectWidth;
            w_inches = (target_w_pix / image_w_pix) * w_target;
            w_distance = (w_inches / 2) / (Math.tan(Math.toRadians(theta / 2)));
        } catch (ArithmeticException ex) {
            // System.out.println(ex);
        }
    }

    private void calcHeightDistance() {
        try {
            int target_h_pix = imageHeight;
            int image_h_pix = boundingRectHeight;
            h_inches = (target_h_pix / image_h_pix) * h_target;
            h_distance = (h_inches / 2) / (Math.tan(Math.toRadians(theta / 2)));
        } catch (ArithmeticException ex) {
            // System.out.println(ex);
        }
    }

    private void calcAim() {
        try {
            x_aim = (center_mass_x - (imageWidth / 2)) / (imageWidth / 2);
            y_aim = (center_mass_y - (imageHeight / 2)) / (imageHeight / 2);
            // delta = pixel delta * pixels/deg
            // image width, probably 320 divided by camera field of view
            double degPerPixel = imageWidth / theta;
            double imgCenter = imageWidth / 2;

            double delta = center_mass_x - imgCenter;
            turnDegrees = delta / degPerPixel;
            turnDegrees = RobotMap.roundtoTwo(turnDegrees);
        } catch (ArithmeticException ex) {
            // System.out.println(ex);
        }
    }

    private void calcScores() {
        try {
            rectScore = r.particleArea / (boundingRectHeight * boundingRectWidth) * 100;
            rectScore = RobotMap.roundtoTwo(rectScore);

            aspectScore = boundingRectWidth * boundingRectHeight;
            aspectScore = RobotMap.roundtoTwo(aspectScore);
        } catch (ArithmeticException ex) {
            // System.out.println(ex);
        }
    }
}
