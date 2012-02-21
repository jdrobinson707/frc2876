/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.VisionTrackingIdle;

/**
 *
 * @author User
 */
public class CameraTarget extends Subsystem {

    AxisCamera camera;          // the axis camera object (connected to the switch)
    CriteriaCollection cc;
    int counter = 0;
    ColorImage image;
    double distance = 0.0;
    boolean isDone;
    final double CAMERA_VIEW_ANGLE = 54;
    final double TARGET_WIDTH_INCH = 24.0;
    final double TIMER_PRINT_DELAY = 0.5;
    final int X_RES = 320;
    boolean isDoneFilter = false;
    double difference = 0.0;

    public CameraTarget() {
        super("CameraTarget");

        camera = AxisCamera.getInstance();  // get an instance ofthe camera
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        image = null;
        isDoneFilter = false;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new VisionTrackingIdle());
    }

    public double GetAngleDif(ParticleAnalysisReport r) {
        double app;
        app = CAMERA_VIEW_ANGLE / X_RES;

        return ((X_RES / 2.0) - r.center_mass_x) * app;
    }

    public double roundtoTwo(double num) {
        return Math.floor(num * 100.0) / 100.0;
    }

    public double getDistance(ParticleAnalysisReport r) {
        double x = (r.imageWidth * TARGET_WIDTH_INCH) / r.boundingRectWidth;
        double x2 = r.imageWidth / (r.boundingRectWidth * TARGET_WIDTH_INCH);
        double y = x / 2;
        double radians = Math.toRadians(CAMERA_VIEW_ANGLE / 2);
        double result = y / Math.tan(radians);

        double a = (r.imageHeight * 18) / r.boundingRectHeight;
        double b = a / 2;
        double c = b / Math.tan(radians);
        SmartDashboard.putDouble("DistanceAway: ", roundtoTwo(result));
        return result;
    }

    public void SortParticles(ParticleAnalysisReport[] r) {
        final int TOP = 0;
        final int LEFT = 1;
        final int BOTTOM = 2;
        final int RIGHT = 3;
        ParticleAnalysisReport[] newOrder = new ParticleAnalysisReport[r.length];
        newOrder[TOP] = r[TOP];
        for (int i = 1; i < r.length; i++) {
            if (r[i].center_mass_y < newOrder[TOP].center_mass_y) {
                ParticleAnalysisReport temp = newOrder[TOP];
                newOrder[TOP] = r[i];
                r[i] = temp;
            }
        }
        newOrder[LEFT] = r[LEFT];
        for (int i = 2; i < r.length; i++) {
            if (r[i].center_mass_x < newOrder[LEFT].center_mass_x) {
                ParticleAnalysisReport temp = newOrder[LEFT];
                newOrder[LEFT] = r[i];
                r[i] = temp;
            }
        }
        newOrder[BOTTOM] = r[BOTTOM];
        for (int i = 3; i < r.length; i++) {
            if (r[i].center_mass_y > newOrder[BOTTOM].center_mass_y) {
                ParticleAnalysisReport temp = newOrder[BOTTOM];
                newOrder[BOTTOM] = r[i];
                r[i] = temp;
            }
        }
        newOrder[RIGHT] = r[RIGHT];
        for (int i = 4; i < r.length; i++) {
            if (r[i].center_mass_y > newOrder[RIGHT].center_mass_y) {
                ParticleAnalysisReport temp = newOrder[RIGHT];
                newOrder[RIGHT] = r[i];
                r[i] = temp;
            }
        }


        String[] labels = {"TOP", "LEFT", "BOTTOM", "RIGHT"};
        for (int i = 0; i < r.length; i++) {
            System.out.println(labels[i] + ": " + newOrder[i].center_mass_x + ", " + newOrder[i].center_mass_y);
            Timer.delay(TIMER_PRINT_DELAY);
        }
    }

    public double getDifference() {
        return difference;
    }

    public void filter() {
        try {
            image = camera.getImage();
            image.write("/tmp/rawImage.png");
            BinaryImage thresholdImage = image.thresholdHSL(88, 126, 47, 255, 37, 100);   // keep only red objects
            thresholdImage.write("/tmp/threshImage.png");
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 1);  //2  // remove small artifacts
            bigObjectsImage.write("/tmp/bigObjectsImage.png");
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            convexHullImage.write("/tmp/convexImage.png");
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);
            filteredImage.write("/tmp/filteredImage.png");

            double topMost = 0.0;
            int particleNumber = -1;
            double aspectRatio = 0.0;

            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
            ParticleAnalysisReport wanted;
            for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                aspectRatio = (double) r.boundingRectWidth / (double) r.boundingRectHeight;
                System.out.println("P=" + i + " AR=" + aspectRatio
                        + " PQ=" + r.particleQuality
                        + " CM=" + r.center_mass_x + "," + r.center_mass_y);

                if (r.particleQuality > 90 && aspectRatio > 1.0 && aspectRatio < 1.5) {
                    System.out.println("Particle: " + (i + 1));
                    System.out.println("location: " + r.center_mass_x + ", " + r.center_mass_y);
                    System.out.println(getDistance(r));

                    //distance = getDistance(r);

                    if (reports.length == 1) {
                        topMost = r.center_mass_y;
                        particleNumber = 0;
                    }
                    if (r.center_mass_y_normalized < topMost) {
                        topMost = r.center_mass_y;
                        particleNumber = i;
                    }
                    System.out.println();
                    System.out.println();


                }
            }
            if (particleNumber != -1) {
                //SortParticles(reports);
                wanted = reports[particleNumber];
                difference = GetAngleDif(wanted);
                SmartDashboard.putDouble("angle error", RobotMap.roundtoTwo(difference));
                //SmartDashboard.putDouble("distance", RobotMap.roundtoTwo(getDistance(wanted)));
            }


            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
            isDoneFilter = true;
        } catch (NIVisionException e) {
        } catch (AxisCameraException e) {
        }
    }

    public boolean isDoneFiltered() {
        return isDoneFilter;
    }
}
