package BreveCreatures;

import BaseObjects.Chromosome;
import BaseObjects.GeneticData;
import BaseObjects.StopWatch;
import GeneticAlgorithm.GeneticAlgorithm;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.testbed.framework.TestbedController.UpdateBehavior;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedSetting;
import static org.jbox2d.testbed.framework.TestbedSettings.Hz;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Seva
 */
public class BreveKnockOff extends javax.swing.JFrame implements Runnable {

    private Thread animation = null;
    private Graphics offScreen;
    private Image image;
    public GeneticAlgorithm ga;
    private boolean runTest = false;
    private boolean loopTest = false;
    private double distanceTraveled = 0;
    private ArrayList<Float> arenaOutcome;
    private FloppyElephant blobby0;
    private FloppyBlob blobby1;
    private FloppyBlob2 blobby2;
    private FloppyBlob3 blobby3;
    private FloppyFight arena;
    private Chromosome currBlobby;
    private Chromosome currBlobby2;
    private StopWatch timer = new StopWatch();
    //private TestPanelJ2D draw = new TestPanelJ2D(new TestbedModel());
    //TestbedModel wurm = new TestbedModel();
    private TestbedModel model;
    private TestPanelJ2D panel;
    //private TestbedController testController;
    private TestbedFrame testbed;
    private Point prevLocation;
    private Dimension dimensions;
    private FileBrowser fileBrowser;
    //private Physics phys;
    private int mode = 0;
    private boolean alphaDisplay = false;
    private int currGen = 1;
    private Random r;
    private ArrayList<ArrayList<Chromosome>> fightsFought = new ArrayList<>();

    public BreveKnockOff(boolean start) throws Exception {
        if (start) {
            r = new Random();
            initComponents();
            //phys = new Physics(new PApplet(),250,250);
            //phys = new Physics();
            model = new TestbedModel();
            model.addTest(new LoadingTest());
            
            prevLocation = this.getLocation();
            dimensions = this.getSize();
            panel = new TestPanelJ2D(model);
            model.setDebugDraw(panel.getDebugDraw());
            //testController = new TestbedController(model, panel, UpdateBehavior.UPDATE_CALLED);
            testbed = new TestbedFrame(model, panel, UpdateBehavior.UPDATE_CALLED);
            testbed.setLocation(600, 0);
            //testbed.setResizable(false);
            testbed.setVisible(true);
            //this.backGroundPanel.add((Component)panel);
            //pack();
            //testController.playTest();
            //panel.getDebugDraw().setCamera(0, 0, 0.1f);
            //testbed = new TestbedFrame(model, panel, TestbedController.UpdateBehavior.UPDATE_CALLED);
            //testbed.setUndecorated(true);
            //testbed.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            //testbed.setLocation(this.getX() + this.getWidth(), this.getY());
            //testbed.setVisible(true);
            this.image = createImage(1000, 1000);
            this.offScreen = this.image.getGraphics();
            CallofDuty();
            //this.currentParams.setText(this.getTitle() + " mode" + mode + ga.getStartingParameters());
            //untested.add(ga.getCurrIteration().get(0));
            fileBrowser = new FileBrowser(this);
            //fileBrowser.setVisible(false);
            
            start();
            /*
            System.out.println("WE MADE IT THIS FUCKING FAR 1");
            //NEVER EVER MAKE THIS TRUE
            model.getSettings().getSetting("Help").enabled = false;
            
            System.out.println("WE MADE IT THIS FUCKING FAR 2");
            model.getSettings().FuckingRemoveHz();
            System.out.println("WE MADE IT THIS FUCKING FAR 3");
            model.getSettings().addSetting(new TestbedSetting(Hz, TestbedSetting.SettingType.ENGINE, 60, 1, 400));
            
            System.out.println("WE MADE IT THIS FUCKING FAR 4");
            */
        }
    }

    public final void CallofDuty() throws Exception {
        if (mode == 0) {
            ga = new GeneticAlgorithm(10, true, 3, 19, 8, 8, 0);
        } else if (mode == 1) {
            ga = new GeneticAlgorithm(10, false, 3, 4);
        } else if (mode == 2) { //I may have undone the universe
            //System.out.println("attempting time travel");
            ga = new GeneticAlgorithm(10, true, 6, 2, 2, 5, 0, 2, 19, -1, -1, 1, 2, 0, 5, 5, 1); //blackest sorcery
        } else if (mode == 3) {
            ga = new GeneticAlgorithm(10, true, 6, 2, 2, 5, 0, 2, 25, -1, -1, 1, 2, 0, 5, 5, 1); //blackest sorcery
        } else if (mode == 4) {
            //System.out.println("THIS AIN'T SUPPORTED");
            ga = new GeneticAlgorithm(10, true, 6, 2, 2, 5, 0, 2, 25, -1, -1, 1, 2, 0, 5, 5, 1); //blackest sorcery
        }
        this.currentParams.setText(this.getTitle() + " mode" + mode + ga.getStartingParameters());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(offScreen);
        //System.out.println("Testing Chromosome: "+ga.getCurrIteration().get(0).toString());
        //panel.paintScreen();
        //if (runTest || loopTest || alphaDisplay) {
        try {
            //offScreen.drawString("Testing Chromosome: " + currBlobby, 15, 50);
            if (mode == 0) {
                jayLayble.setText("Displacement Traveled: " + (Math.abs(blobby0.getCenterOfMass().x) - distanceTraveled));
            } else if (mode == 1) {
                jayLayble.setText("Displacement Traveled: " + (Math.abs(blobby1.getPosition()) - distanceTraveled));
            } else if (mode == 2) {
                jayLayble.setText("Displacement Traveled: " + (Math.abs(blobby2.getCenterOfMass().x) - distanceTraveled));
            } else if (mode == 3) {
                jayLayble.setText("Displacement Traveled: " + (Math.abs(blobby3.getCenterOfMass().x) - distanceTraveled));
            } else if (mode == 4) {
                //System.out.println("THIS AIN'T SUPPORTED");
                jayLayble.setText(arena.getOutcome().get(0) + " vs " + arena.getOutcome().get(1));
            }
        } catch (NullPointerException e) {
            //System.out.println("no test running");
        }
        //}
        g.drawImage(this.image, 0, 0, this);
    }

    public void testCreature(Chromosome... c) {
        boolean go = true;
        model.getSettings().getSetting("Hz").value = 60;
        if (mode == 0) {
            blobby0 = new FloppyElephant(c[0].getGeneticDataByParts());
            //model.clearTestList();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreveKnockOff.class.getName()).log(Level.SEVERE, null, ex);
            }
            model.addTest(blobby0);
            distanceTraveled = Math.abs(blobby0.getCenterOfMass().x);
        } else if (mode == 1) {
            blobby1 = new FloppyBlob(c[0].getGeneticDataByParts());
            //model.clearTestList();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreveKnockOff.class.getName()).log(Level.SEVERE, null, ex);
            }
            model.addTest(blobby1);
            distanceTraveled = Math.abs(blobby1.getPosition());
        } else if (mode == 2) {
            blobby2 = new FloppyBlob2(c[0].getGeneticDataByParts());
            //model.clearTestList();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreveKnockOff.class.getName()).log(Level.SEVERE, null, ex);
            }
            model.addTest(blobby2);
            distanceTraveled = Math.abs(blobby2.getCenterOfMass().x);
        } else if (mode == 3) {
            blobby3 = new FloppyBlob3(c[0].getGeneticDataByParts());
            //model.clearTestList();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreveKnockOff.class.getName()).log(Level.SEVERE, null, ex);
            }
            model.addTest(blobby3);
            distanceTraveled = Math.abs(blobby3.getCenterOfMass().x);
        } else if (mode == 4) {
            //System.out.println("THIS AIN'T SUPPORTED");
            ArrayList<ArrayList<ArrayList<GeneticData>>> gladiators = new ArrayList<>();
            gladiators.add(c[0].getGeneticDataByParts());
            gladiators.add(c[1].getGeneticDataByParts());
            arena = new FloppyFight(gladiators);
            //model.clearTestList();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreveKnockOff.class.getName()).log(Level.SEVERE, null, ex);
            }
            model.addTest(arena);
            arenaOutcome = arena.getOutcome();
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(BreveKnockOff.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.setCurrTestIndex(1);
        timer.reset();
        timer.start();
        while (go) {
            adjustFrame();
            testingProgressBar.setValue((int) (timer.getTime() * 100));
            if (timer.getTime() > 10) {
                go = false;
            }
            repaint();
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e) {
            }
        }
        this.testingProgressBar.setValue(0);
        model.clearTestList();
        model.getSettings().getSetting("Hz").value = 60;
        model.addTest(new LoadingTest());
        model.getSettings().getSetting("Hz").value = 60;
    }

    public void update() {
        repaint(15L);
        //backgroundPanel.repaint();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public final void start() {
        if (this.animation == null) {
            this.animation = new Thread(this, "Thread");
            this.animation.start();
        }
    }

    public void adjustFrame() {
        if (!this.getSize().equals(dimensions) || !this.getLocation().equals(prevLocation)) {
            dimensions = this.getSize();
            //System.out.println("resized; is now " + dimensions.width + " by " + dimensions.height);
            //System.out.println("area is now " + dimensions.width * dimensions.height);
            prevLocation = this.getLocation();
            testbed.setLocation(this.getX() + this.getWidth(), this.getY());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                while (ga.containsUntested()) {
                    adjustFrame();
                    if (runTest || loopTest) {
                        this.CommandQueueTextArea.insert("TEST RUNNING\n", 0);
                        if (alphaDisplay) {
                            if (mode != 4) {
                                testCreature(ga.getCurrIteration().get(0));
                            } else {
                                System.out.println("THIS AIN'T SUPPORTED YET");
                                testCreature(ga.getCurrIteration().get(0));
                                testCreature(ga.getCurrIteration().get(1));
                            }
                            alphaDisplay = false;
                        } else {
                            ArrayList<Chromosome> neighborhood = ga.getCurrIteration();
                            //System.out.println("Pop length is " + city.size());
                            ArrayList<Chromosome> testList;
                            ArrayList<Chromosome> testList2;
                            int num1;
                            int num2;
                            do {
                                num1 = r.nextInt(neighborhood.size());
                                num2 = r.nextInt(neighborhood.size());
                                //System.out.println("NUM 1 IS :" + num1);
                                //System.out.println("NUM 2 IS :" + num2);
                                currBlobby = neighborhood.get(num1);
                                currBlobby2 = neighborhood.get(num2);
                                testList = new ArrayList<>();
                                testList.add(currBlobby);
                                testList.add(currBlobby2);
                                testList2 = new ArrayList<>();
                                testList2.add(currBlobby2);
                                testList2.add(currBlobby);
                            } while (num1 == num2
                                    || fightsFought.contains(testList)
                                    || fightsFought.contains(testList2));
                            fightsFought.add(testList); //don't want to fight same fight twice
                            fightsFought.add(testList2);
                            //System.out.println(currBlobby + " is attempting to enter the arena!");
                            //System.out.println(currBlobby2 + " is attempting to enter the arena!");
                            if (currBlobby != null) {
                                testCreature(currBlobby, currBlobby2);
                                this.CommandQueueTextArea.insert("DONE TESTING\n", 0);
                                //model.getSettings().pause = true;
                                if (mode == 0) {
                                    distanceTraveled = Math.abs(blobby0.getCenterOfMass().x) - distanceTraveled;
                                } else if (mode == 1) {
                                    distanceTraveled = Math.abs(blobby1.getPosition()) - distanceTraveled;
                                } else if (mode == 2) {
                                    distanceTraveled = Math.abs(blobby2.getCenterOfMass().x) - distanceTraveled;
                                } else if (mode == 3) {
                                    distanceTraveled = Math.abs(blobby3.getCenterOfMass().x) - distanceTraveled;
                                } else if (mode == 4) {
                                    //System.out.println("THIS AIN'T SUPPORTED");
                                    arenaOutcome = arena.getOutcome();;
                                }
                                if (mode != 4) {
                                    currBlobby.setFitness(distanceTraveled);
                                } else {
                                    //System.out.println("THIS AIN'T SUPPORTED");

                                    float fightRatio = 0.5f;
                                    float distanceRatio = 0.5f;
                                    if (arenaOutcome.get(1) != 0) {
                                        fightRatio = arenaOutcome.get(0) / arenaOutcome.get(1);
                                    }
                                    if (arenaOutcome.get(3) != 0) {
                                        distanceRatio = arenaOutcome.get(2) / arenaOutcome.get(3);
                                    }
                                    float distance1 = arenaOutcome.get(2);
                                    float distance2 = arenaOutcome.get(3); //at most .5
                                    System.out.println("SCORE 1: " + arenaOutcome.get(0));
                                    System.out.println("SCORE 2: " + arenaOutcome.get(1));
                                    System.out.println("DISTANCE 1: " + arenaOutcome.get(2));
                                    System.out.println("DISTANCE 2: " + arenaOutcome.get(3));
                                    //System.out.println("FIGHT RATIO: " + fightRatio);
                                    //System.out.println("DISTANCE RATIO: " + distanceRatio);
                                    System.out.println("Fitness for " + currBlobby + " was "
                                            + "\n" + currBlobby.getFitness());
                                    //left blob
                                    if (currBlobby.getFitness() == 0d
                                            && arenaOutcome.get(0).floatValue() > arenaOutcome.get(1).floatValue()) {
                                        currBlobby.setFitness(1);
                                        //System.out.println("CASE 1");
                                    } else if (currBlobby.getFitness() == 0d
                                            && arenaOutcome.get(0).floatValue() < arenaOutcome.get(1).floatValue()) {
                                        currBlobby.setFitness(0);
                                        //System.out.println("CASE 2");
                                    } else if (currBlobby.getFitness() == 0d
                                            && arenaOutcome.get(0).floatValue() == arenaOutcome.get(1).floatValue()) {
                                        currBlobby.setFitness(distance1);
                                        //System.out.println("CASE 3");
                                    } else {
                                        currBlobby.setFitness(currBlobby.getFitness() * (fightRatio + distance1));
                                        //System.out.println("CASE 4");
                                    }
                                    if (currBlobby.getFitness() >= 1) {
                                        currBlobby.setFitness(1);
                                    }
                                    System.out.println("Current Fitness for " + currBlobby
                                            + "\n" + currBlobby.getFitness());
                                    System.out.println("Fitness for " + currBlobby2 + " was "
                                            + "\n" + currBlobby2.getFitness());
                                    //right blob
                                    if (currBlobby2.getFitness() == 0d
                                            && arenaOutcome.get(0).floatValue() > arenaOutcome.get(1).floatValue()) {
                                        currBlobby2.setFitness(0);
                                        //System.out.println("CASE 5");
                                    } else if (currBlobby2.getFitness() == 0d
                                            && arenaOutcome.get(0).floatValue() < arenaOutcome.get(1).floatValue()) {
                                        currBlobby2.setFitness(1);
                                        //System.out.println("CASE 6");
                                    } else if (currBlobby2.getFitness() == 0d
                                            && arenaOutcome.get(0).floatValue() == arenaOutcome.get(1).floatValue()) {
                                        currBlobby2.setFitness(distance2);
                                        //System.out.println("CASE 7");
                                    } else {
                                        currBlobby2.setFitness(currBlobby2.getFitness() * ((1 / fightRatio) + distance2));
                                        // System.out.println("CASE 8");
                                    }
                                    if (currBlobby2.getFitness() >= 1) {
                                        currBlobby2.setFitness(1);
                                    }
                                    System.out.println("Current Fitness for " + currBlobby2
                                            + "\n" + currBlobby2.getFitness());
                                }
                                //System.out.println("SETTING FITNESS");
                                ga.setFitness(currBlobby, currBlobby.getFitness());
                                //model.getSettings().pause = false;
                                //ga.getCurrIteration().get(ga.getCurrIteration().indexOf(currBlobby)).setFitness(distanceTraveled);
                            }
                        }
                        if (!loopTest) {
                            this.runTestButton.setEnabled(true);
                            this.loopTestButton.setEnabled(true);
                            this.showBestButton.setEnabled(true);
                            this.saveButton.setEnabled(true);
                            this.loadButton.setEnabled(true);
                            runTest = false;
                        }
                    }
                    repaint();
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e) {
                    }
                }
                ga.evolve();
                this.CommandQueueTextArea.insert("Some new floppyblobs\nhave appeared!\n", 0);
                //this is for arguing purposes
                double[] stats = ga.getStats();
                this.CommandQueueTextArea.insert("Current Pop.:     " + stats[5] + "\n", 0);
                this.CommandQueueTextArea.insert("Current Gen.:     " + currGen + "\n", 0);
                this.CommandQueueTextArea.insert("Max Fitness:      " + stats[0] + "\n", 0);
                this.CommandQueueTextArea.insert("Min Fitness:      " + stats[1] + "\n", 0);
                this.CommandQueueTextArea.insert("Mean Fitness:     " + stats[2] + "\n", 0);
                this.CommandQueueTextArea.insert("Median Fitness:   " + stats[3] + "\n", 0);
                this.CommandQueueTextArea.insert("St. Dev. Fitness: " + stats[4] + "\n", 0);
                currGen++;
            }
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backGroundPanel = new javax.swing.JPanel();
        testingProgressBar = new javax.swing.JProgressBar();
        runTestButton = new javax.swing.JButton();
        loopTestButton = new javax.swing.JButton();
        showBestButton = new javax.swing.JButton();
        CommandQueueScrollPane = new javax.swing.JScrollPane();
        CommandQueueTextArea = new javax.swing.JTextArea();
        toggleScreenButton = new javax.swing.JToggleButton();
        saveButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        modeSelector = new javax.swing.JComboBox();
        currentParams = new javax.swing.JTextField();
        statsButton = new javax.swing.JButton();
        jayLayble = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("2D Creature Evolver");

        backGroundPanel.setPreferredSize(new java.awt.Dimension(600, 500));

        testingProgressBar.setMaximum(1000);
        testingProgressBar.setStringPainted(true);

        runTestButton.setText("Run Test");
        runTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTestButtonActionPerformed(evt);
            }
        });

        loopTestButton.setText("Loop Test");
        loopTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loopTestButtonActionPerformed(evt);
            }
        });

        showBestButton.setText("Alpha Creature");
        showBestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showBestButtonActionPerformed(evt);
            }
        });

        CommandQueueScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        CommandQueueTextArea.setEditable(false);
        CommandQueueTextArea.setColumns(20);
        CommandQueueTextArea.setLineWrap(true);
        CommandQueueTextArea.setRows(5);
        CommandQueueScrollPane.setViewportView(CommandQueueTextArea);

        toggleScreenButton.setSelected(true);
        toggleScreenButton.setText("Display Screen");
        toggleScreenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleScreenButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        loadButton.setText("Load");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        modeSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FloppyElephant", "FloppyBlob", "FloppyBlob2", "FloppyBlob3", "FloppyFight" }));
        modeSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeSelectorActionPerformed(evt);
            }
        });

        currentParams.setEditable(false);

        statsButton.setText("Stats");
        statsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsButtonActionPerformed(evt);
            }
        });

        jayLayble.setText("Displacement");

        javax.swing.GroupLayout backGroundPanelLayout = new javax.swing.GroupLayout(backGroundPanel);
        backGroundPanel.setLayout(backGroundPanelLayout);
        backGroundPanelLayout.setHorizontalGroup(
            backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backGroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CommandQueueScrollPane)
                    .addComponent(testingProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jayLayble, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(backGroundPanelLayout.createSequentialGroup()
                        .addComponent(modeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentParams))
                    .addGroup(backGroundPanelLayout.createSequentialGroup()
                        .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(backGroundPanelLayout.createSequentialGroup()
                                .addComponent(showBestButton)
                                .addGap(41, 41, 41)
                                .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(toggleScreenButton)
                                    .addComponent(loopTestButton)))
                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(runTestButton, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backGroundPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );
        backGroundPanelLayout.setVerticalGroup(
            backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backGroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CommandQueueScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jayLayble, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(testingProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentParams, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(runTestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(backGroundPanelLayout.createSequentialGroup()
                        .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toggleScreenButton))
                        .addGap(16, 16, 16)
                        .addGroup(backGroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loopTestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showBestButton, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statsButton)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backGroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backGroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runTestButtonActionPerformed
        runTest = true;
        int width = panel.getSize().width;
        int height = panel.getSize().height;
        //System.out.println("Running; size is currently " + width + " by " + height);
        //System.out.println("area is currently " + width * height);
        this.runTestButton.setEnabled(false);
        this.loopTestButton.setEnabled(false);
        this.showBestButton.setEnabled(false);
        this.saveButton.setEnabled(false);
        this.loadButton.setEnabled(false);
        this.CommandQueueTextArea.insert("TEST RUNNING\n", 0);
    }//GEN-LAST:event_runTestButtonActionPerformed

    private void loopTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loopTestButtonActionPerformed
        loopTest = !loopTest;
        this.CommandQueueTextArea.insert("TEST LOOPING: " + loopTest + "\n", 0);
        this.runTestButton.setEnabled(false);
        this.showBestButton.setEnabled(false);
        this.saveButton.setEnabled(false);
        this.loadButton.setEnabled(false);
    }//GEN-LAST:event_loopTestButtonActionPerformed

    private void showBestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showBestButtonActionPerformed
        if (!ga.getCurrIteration().isEmpty()) {
            this.CommandQueueTextArea.insert("ALPHA BOSS\n", 0);
            this.CommandQueueTextArea.insert(ga.getCurrIteration().get(0).toString() + "\n", 0);
            alphaDisplay = true;
            runTestButton.doClick();
        }
    }//GEN-LAST:event_showBestButtonActionPerformed

    private void toggleScreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleScreenButtonActionPerformed
        testbed.setVisible(this.toggleScreenButton.isSelected());
    }//GEN-LAST:event_toggleScreenButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        this.CommandQueueTextArea.insert("Saving Data\n", 0);
        this.fileBrowser.setCommandButton("Save");
        this.fileBrowser.setList(ga.getCurrIteration());
        this.fileBrowser.setLocation(this.getLocation());
        this.fileBrowser.setVisible(true);
        this.CommandQueueTextArea.insert("Data Saved\n", 0);
    }//GEN-LAST:event_saveButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        this.CommandQueueTextArea.insert("Loading Data\n", 0);
        this.fileBrowser.setCommandButton("Load");
        this.fileBrowser.setLocation(this.getLocation());
        this.fileBrowser.setVisible(true);
        this.CommandQueueTextArea.insert("Data Loaded\n", 0);
    }//GEN-LAST:event_loadButtonActionPerformed

    private void modeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeSelectorActionPerformed
        mode = modeSelector.getSelectedIndex();
        currGen = 1;
        try {
            CallofDuty();
        } catch (Exception e) {
            System.out.println("lol");
        }
    }//GEN-LAST:event_modeSelectorActionPerformed

    private void statsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsButtonActionPerformed
        double[] stats = ga.getStats();
        this.CommandQueueTextArea.insert("Current Pop.:     " + stats[5] + "\n", 0);
        this.CommandQueueTextArea.insert("Current Gen.:     " + currGen + "\n", 0);
        this.CommandQueueTextArea.insert("Max Fitness:      " + stats[0] + "\n", 0);
        this.CommandQueueTextArea.insert("Min Fitness:      " + stats[1] + "\n", 0);
        this.CommandQueueTextArea.insert("Mean Fitness:     " + stats[2] + "\n", 0);
        this.CommandQueueTextArea.insert("Median Fitness:   " + stats[3] + "\n", 0);
        this.CommandQueueTextArea.insert("St. Dev. Fitness: " + stats[4] + "\n", 0);
    }//GEN-LAST:event_statsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BreveKnockOff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BreveKnockOff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BreveKnockOff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BreveKnockOff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new BreveKnockOff(true).setVisible(true);
                } catch (Exception ex) {
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane CommandQueueScrollPane;
    private javax.swing.JTextArea CommandQueueTextArea;
    private javax.swing.JPanel backGroundPanel;
    private javax.swing.JTextField currentParams;
    private javax.swing.JLabel jayLayble;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton loopTestButton;
    private javax.swing.JComboBox modeSelector;
    private javax.swing.JButton runTestButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton showBestButton;
    private javax.swing.JButton statsButton;
    private javax.swing.JProgressBar testingProgressBar;
    private javax.swing.JToggleButton toggleScreenButton;
    // End of variables declaration//GEN-END:variables
}