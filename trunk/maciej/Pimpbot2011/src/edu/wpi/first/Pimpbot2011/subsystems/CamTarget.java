/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.subsystems;

import edu.wpi.first.Pimpbot2011.RobotMap;
import edu.wpi.first.Pimpbot2011.Target;
import edu.wpi.first.Pimpbot2011.commands.CamDoNothing;
import edu.wpi.first.wpilibj.Preferences;
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

// TODO
// -Create a class called Target which holds particle report and scores info.
// -write func that sorts targets and finds which one is which and filters out
// particles with low scores
//
/**
 *
 * @author maciej
 */
public class CamTarget extends Subsystem {

    public static final String[] pos = {"TOP", "RGT", "BOT", "LFT"};
    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOT = 2;
    public static final int LEFT = 3;
    int rlow = 50;
    int rhigh = 255;
    int glow = 43;
    int ghigh = 255;
    int blow = 47;
    int bhigh = 255;
    int VIEW_ANGLE = 54;
    AxisCamera camera;
    CriteriaCollection cc;
    Preferences pfs = Preferences.getInstance();
    boolean filter_running = false;
    Target[] sorted;
    Target none;

    // Initialize your subsystem here
    public CamTarget() {
        super("CamTarget");
        camera = AxisCamera.getInstance();

        cc = new CriteriaCollection();
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);

        for (int i = 0; i < 5; i++) {
            SmartDashboard.putString("Particle-" + i, "");
        }

        pfs.putInt("rlow", rlow);
        pfs.putInt("rhigh", rhigh);
        pfs.putInt("glow", glow);
        pfs.putInt("ghigh", ghigh);
        pfs.putInt("blow", blow);
        pfs.putInt("bhigh", bhigh);
        pfs.putInt("theta", VIEW_ANGLE);
        pfs.save();
        System.out.println("R=" + rlow + "," + rhigh
                + " G=" + glow + "," + ghigh
                + " B=" + blow + "," + bhigh);

        none = new Target(null, 0);
    }

    private void updateDash() {
        SmartDashboard.putInt("rlow", rlow);
        SmartDashboard.putInt("rhigh", rhigh);
        SmartDashboard.putInt("glow", glow);
        SmartDashboard.putInt("ghigh", ghigh);
        SmartDashboard.putInt("blow", blow);
        SmartDashboard.putInt("bhigh", bhigh);
        SmartDashboard.putInt("THETA", VIEW_ANGLE);
        SmartDashboard.putString(pos[TOP], sorted[TOP].getDashString());
        SmartDashboard.putString(pos[RIGHT], sorted[RIGHT].getDashString());
        SmartDashboard.putString(pos[BOT], sorted[BOT].getDashString());
        SmartDashboard.putString(pos[LEFT], sorted[LEFT].getDashString());
    }

    public void printTargets() {
        double pdelay = .2;
        for (int i = 0; i < sorted.length; i++) {
            System.out.println(pos[i] + ": " + sorted[i]);
            Timer.delay(pdelay);
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new CamDoNothing());
    }

    private void analyzeImage(BinaryImage img) {
        Target[] targets = new Target[4];
        try {
            ParticleAnalysisReport[] reports = img.getOrderedParticleAnalysisReports();
            for (int i = 0, j = 0; i < reports.length; i++) {
                Target t = new Target(reports[i], VIEW_ANGLE);
                t.analyze();
                if (t.isValid()) {
                    targets[j] = t;
                    j++;
                }
            }
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }

        // top has smallest y
        // bot has largest y
        // left smallest x
        // right largest x
        sorted = new Target[targets.length];
        for (int i = 0; i < targets.length; i++) {
            sorted[i] = none;
            if (targets[i].getYCoM() < sorted[TOP].getYCoM()) {
                sorted[TOP] = targets[i];
            }
            if (targets[i].getYCoM() >= sorted[BOT].getYCoM()) {
                sorted[BOT] = targets[i];
            }
            if (targets[i].getXCoM() < sorted[LEFT].getXCoM()) {
                sorted[LEFT] = targets[i];
            }
            if (targets[i].getXCoM() >= sorted[RIGHT].getXCoM()) {
                sorted[RIGHT] = targets[i];
            }
        }
        updateDash();

    }

    private void printParticleReports(BinaryImage img) {
        double pdelay = .2;
        try {
            ParticleAnalysisReport[] reports =
                    img.getOrderedParticleAnalysisReports();
            for (int i = 0; i < reports.length && i < 5; i++) {
                ParticleAnalysisReport r = reports[i];
                System.out.println("Particle(" + (i + 1) + "/" + reports.length
                        + ")");
                Timer.delay(pdelay);
                System.out.println(" left=" + r.boundingRectLeft
                        + " top=" + r.boundingRectTop
                        + " qual=" + RobotMap.roundtoTwo(r.particleQuality)
                        + " %=" + RobotMap.roundtoTwo(r.particleToImagePercent));
                Timer.delay(pdelay);
            }
            System.out.println(img.getNumberParticles()
                    + "  " + Timer.getFPGATimestamp());
            Timer.delay(pdelay);
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isFilterRunning() {
        return filter_running;
    }

    public void testFilter(boolean saveImage) {
        try {
            if (camera.freshImage()) {
                return;
            }
            filter_running = true;
            rlow = pfs.getInt("rlow", 0);
            rhigh = pfs.getInt("rhigh", 0);
            glow = pfs.getInt("glow", 0);
            ghigh = pfs.getInt("ghigh", 0);
            blow = pfs.getInt("blow", 0);
            bhigh = pfs.getInt("bhigh", 0);
            VIEW_ANGLE = pfs.getInt("theta", 54);

            ColorImage image = camera.getImage();
            BinaryImage thresholdImage = image.thresholdHSL(rlow, rhigh,
                    glow, ghigh, blow, bhigh);
            System.out.println("R=" + rlow + "," + rhigh
                    + " G=" + glow + "," + ghigh
                    + " B=" + blow + "," + bhigh);

            if (saveImage) {
                thresholdImage.write("/tmp/_1thresh-image.png");
            } // remove small artifacts
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 1);
            if (saveImage) {
                bigObjectsImage.write("/tmp/_2big-image.png");
            } // fill in occluded rectangles
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);
            if (saveImage) {
                convexHullImage.write("/tmp/_3convex-image.png");
            } // find filled in rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);
            if (saveImage) {
                filteredImage.write("/tmp/_4filter-image.png");
            }
            analyzeImage(filteredImage);
            // printParticleReports(filteredImage);

            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
        filter_running = false;
    }

    // Get the first available target. If target is not found during image
    // analysis the sorted array gets set to 'none' Target object which
    // returns default values (probably zero) for distance and turn amount.
    private Target getFirst() {
        Target t = none;
        if (sorted[TOP] != none) {
            t = sorted[TOP];
        } else if (sorted[LEFT] != none) {
            t = sorted[LEFT];
        } else if (sorted[RIGHT] != none) {
            t = sorted[RIGHT];
        } else if (sorted[BOT] != none) {
            t = sorted[BOT];
        }
        return t;
    }

    public double getXAim() {
        return getFirst().getXAim();
    }

    public double getYAim() {
        return getFirst().getYAim();
    }

    public double getTurnAmount() {
        return getFirst().getTurnDegrees();
    }

    public double getDistance() {
        return getFirst().getDistance();
    }
}
