/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

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
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.Target;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.VisionIdle;

/**
 *
 * @author User
 */
public class CameraTarget extends Subsystem {

    public static final String[] pos = {"TOP", "RGT", "BOT", "LFT"};
    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOT = 2;
    public static final int LEFT = 3;
    String angleHistory;
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
    double current_turn = 0;
    double search = 1;
    double search_incr = 0;

    public CameraTarget() {
        super("CameraTarget");

        camera = AxisCamera.getInstance();

        cc = new CriteriaCollection();
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);

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

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new VisionIdle());
    }

    public void updateDash() {
        boolean debug = CommandBase.oi.isDebugOn();

        SmartDashboard.putInt("h_low", rlow);
        SmartDashboard.putInt("h_high", rhigh);
        SmartDashboard.putInt("s_low", glow);
        SmartDashboard.putInt("s_high", ghigh);
        SmartDashboard.putInt("l_low", blow);
        SmartDashboard.putInt("l_high", bhigh);

        // SmartDashboard.putInt("THETA", VIEW_ANGLE);

        SmartDashboard.putString(pos[TOP], sorted[TOP].getDashString(debug));
        SmartDashboard.putString(pos[RIGHT], sorted[RIGHT].getDashString(debug));
        SmartDashboard.putString(pos[BOT], sorted[BOT].getDashString(debug));
        SmartDashboard.putString(pos[LEFT], sorted[LEFT].getDashString(debug));
        SmartDashboard.putDouble("TURN", current_turn);
        SmartDashboard.putDouble("DIST", RobotMap.roundtoTwo(getDistance()));
        String target_widget = "";
        if (sorted[TOP] != none) {
            target_widget += "1";
        }
        if (sorted[LEFT] != none) {
            target_widget += "2";
        }
        if (sorted[BOT] != none) {
            target_widget += "3";
        }
        if (sorted[RIGHT] != none) {
            target_widget += "4";
        }
        SmartDashboard.putString("targets", target_widget);
        SmartDashboard.putString("angleHist", angleHistory);
    }

    public void resetLastAngle() {
        angleHistory = "";
    }

    public void addLastAngle(double degrees) {
        angleHistory += ":" + degrees;
    }

    public void printTargets() {
        double pdelay = .2;
        for (int i = 0; i < sorted.length; i++) {
            System.out.println(pos[i] + ": " + sorted[i]);
            Timer.delay(pdelay);
        }
    }

    private void sort2(Target[] targets) {
        if (targets == null || sorted == null) {
            return;
        }
        if (targets.length == 0) {
            return;
        }
        sorted[TOP] = targets[0];
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].getYCoM() < sorted[TOP].getYCoM() || sorted[TOP] == none) {
                sorted[TOP] = targets[i];
            }
        }
        sorted[LEFT] = none;
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].getXCoM() < sorted[LEFT].getXCoM() || sorted[LEFT] == none) {
                sorted[LEFT] = targets[i];
            }
        }
        sorted[RIGHT] = none;
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].getXCoM() > sorted[RIGHT].getXCoM() || sorted[RIGHT] == none) {
                sorted[RIGHT] = targets[i];
            }
        }
        sorted[BOT] = none;
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].getYCoM() > sorted[BOT].getYCoM() || sorted[BOT] == none) {
                sorted[BOT] = targets[i];
            }
        }

    }

    private void analyzeImage(BinaryImage img) {
        Target[] targets = null;
        try {
            ParticleAnalysisReport[] reports = img.getOrderedParticleAnalysisReports();
            System.out.println("num particles " + reports.length);
            targets = new Target[4];
            for (int i = 0; i < targets.length; i++) {
                targets[i] = none;
            }
            Target[] tmp = new Target[reports.length];
            int valid = 0;
            for (int i = 0; i < reports.length; i++) {
                Target t = new Target(reports[i], VIEW_ANGLE);
                t.analyze();
                if (t.isValid()) {
                    tmp[i] = t;
                    valid++;
                } else {
                    System.out.println("invalid: " + t);
                    tmp[i] = none;
                }
            }
            targets = new Target[valid];
            for (int i = 0, j = 0; i < tmp.length && j < valid; i++) {
                if (tmp[i] != none) {
                    targets[j] = tmp[i];
                    j++;
                }
            }
            sorted = new Target[4];
            for (int i = 0; i < sorted.length; i++) {
                sorted[i] = none;
            }

            sort2(targets);
            current_turn = getBestTarget().getTurnDegrees();
            updateDash();

        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }

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

    public void findTargets(boolean saveImage) {
        filter_running = true;
        try {
            resetTargets();
            if (camera.freshImage() == false) {
                filter_running = false;
                return;
            }

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
            if (saveImage) {
                System.out.println("R=" + rlow + "," + rhigh
                        + " G=" + glow + "," + ghigh
                        + " B=" + blow + "," + bhigh);
            }
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
            if (saveImage) {
                printParticleReports(filteredImage);
            }

            analyzeImage(filteredImage);

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
    private Target getBestTarget() {
        Target t = none;
        if (sorted != null) {
            if (sorted[TOP] != none) {
                t = sorted[TOP];
            } else if (sorted[BOT] != none) {
                t = sorted[BOT];
            } else if (sorted[LEFT] != none) {
                t = sorted[LEFT];
            } else if (sorted[RIGHT] != none) {
                t = sorted[RIGHT];
            }
        }
        return t;
    }

    public void resetTargets() {
        if (sorted != null) {
            for (int i = 0; i < sorted.length; i++) {
                sorted[i] = none;
            }
        }
    }

    public double getTurnAmount() {
        Target t = getBestTarget();
        if (t == none) {
            search_incr++;
            if (search > 0) {
                search = 25 * search_incr * -1;
            } else {
                search = 25 * search_incr;
            }
            if (search > 180) {
                search = 0;
            }
            current_turn = search;
        } else {
            search_incr = 0;
            current_turn = getBestTarget().getTurnDegrees();
        }
        return current_turn;
    }

    public double getDistance() {
        return getBestTarget().getDistance();
    }
}
