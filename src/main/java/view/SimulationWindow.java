package view;

import simulation.Server;
import utils.Status;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SimulationWindow
{
    private final int iconSizeX = 40;
    private final int iconSizeY = 40;
    private final int xSize = 1200;
    private final int ySize = 700;
    private int maxNbOfClients = 40;
    private int maxNbOfQueues = 40;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private List<JLabel> queue;
    private JScrollPane scrollPane;
    private JPanel queuePanel;
    private JPanel parametersPanel;
    private JLabel averageWaitingTime;
    private JLabel averageServiceTime;
    private JLabel peakTime;
    private JLabel status;
    private ImageIcon imageIconClient;
    private ImageIcon imageIconCheckOut;
    private Component[][] placeHolder;

    public SimulationWindow(int maxNbOfClients, int maxNbOfQueues)
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.maxNbOfClients = maxNbOfClients;
        this.maxNbOfQueues = maxNbOfQueues;
        mainFrame = new JFrame("simulation");
        mainFrame.setSize(new Dimension(xSize, ySize));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridBagLayout());
        queue = new ArrayList<>();
        queuePanel = new JPanel(new GridLayout(maxNbOfQueues, maxNbOfClients));
        parametersPanel = new JPanel(new GridLayout(1, 4));

        imageIconClient = new ImageIcon("src/main/resources/HumanIcon.png");
        Image image = getScaledImage(imageIconClient.getImage(), iconSizeX, iconSizeY);
        imageIconClient = new ImageIcon(image);

        ImageIcon imageIconCheckOut = new ImageIcon("src/main/resources/checkOut.png");
        Image image1 = getScaledImage(imageIconCheckOut.getImage(), iconSizeX, iconSizeY);
        imageIconCheckOut = new ImageIcon(image1);

        initializeParametersPane();
        initializeMatrix(imageIconClient, imageIconCheckOut);

        GridBagConstraints c = setGridBagLayoutWeight(1, 0.05, 0, 0);

        JScrollPane scrollPane = new JScrollPane(queuePanel);
        TitledBorder titledBorder = new TitledBorder("queue list");
        titledBorder.setTitleFont(new Font(Font.DIALOG, 0, 20));
        scrollPane.setBorder(titledBorder);
        mainPanel.add(parametersPanel, c);

        c = setGridBagLayoutWeight(1, 1, 0, 1);
        mainPanel.add(scrollPane, c);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private void initializeMatrix(ImageIcon client, ImageIcon checkOut)
    {

        placeHolder = new Component[maxNbOfQueues][maxNbOfClients + 1];

        for (int i = 0; i < maxNbOfQueues; i++)
        {
            JLabel checkOutLabel = new JLabel(checkOut);
            checkOutLabel.setBorder(new TitledBorder(String.valueOf(i)));
            placeHolder[i][0] = checkOutLabel;
            queuePanel.add(checkOutLabel);

            for (int j = 0; j < maxNbOfClients; j++)
            {
                JLabel jLabel = new JLabel(client);
                placeHolder[i][j + 1] = jLabel;
                queuePanel.add(jLabel);
            }
        }
    }

    private GridBagConstraints setGridBagLayoutWeight(double wx, double wy, int x, int y)
    {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = wx;
        gridBagConstraints.weighty = wy;
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        return gridBagConstraints;
    }

    private void initializeParametersPane()
    {
        averageServiceTime = new JLabel("");
        averageWaitingTime = new JLabel("");
        peakTime = new JLabel("");
        status = new JLabel("");

        averageWaitingTime.setBorder(new TitledBorder("Average waiting time"));
        averageServiceTime.setBorder(new TitledBorder("Average service time"));
        peakTime.setBorder(new TitledBorder("Peak time"));
        status.setBorder(new TitledBorder("Status"));

        parametersPanel.add(averageServiceTime);
        parametersPanel.add(averageWaitingTime);
        parametersPanel.add(peakTime);
        parametersPanel.add(status);
    }

    private Image getScaledImage(Image srcImg, int w, int h)
    {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public void setAverageServiceTime(Number value)
    {
        SwingUtilities.invokeLater(() -> averageServiceTime.setText(value.toString()));
    }

    public void setAverageWaitingTime(Number value)
    {
        SwingUtilities.invokeLater(() -> averageWaitingTime.setText(value.toString()));
    }

    public void setPeakTime(Number value)
    {
        SwingUtilities.invokeLater(() -> peakTime.setText(value.toString()));
    }

    public void setStatus(Status statusValue)
    {
        SwingUtilities.invokeLater(() ->
        {
            if (statusValue == Status.ON)
            {
                status.setText("Running");
                status.setForeground(Color.GREEN);
            } else
            {
                status.setText("Stopped");
                status.setForeground(Color.RED);
            }
        });
    }

    public void updateQueues(List<Server> serverList)
    {
        for (int i = 0; i < maxNbOfQueues; i++)
        {
            for (int j = 0; j <= maxNbOfClients; j++)
            {
                placeHolder[i][j].setVisible(false);
            }
        }

        for (int i = 0; i < serverList.size(); i++)
        {
            placeHolder[i][0].setVisible(true);
            for (int j = 0; j < serverList.get(i).getNbOfWaitingTasks(); j++)
            {
                placeHolder[i][j + 1].setVisible(true);
            }
        }

    }
}
