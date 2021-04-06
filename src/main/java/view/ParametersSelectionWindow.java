package view;

import policy.SelectionPolicy;
import utils.Parameters;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParametersSelectionWindow
{
    private final int xSize = 600;
    private final int ySize = 400;

    private JFrame mainFrame;
    private List<JTextField> parameters;
    private JPanel mainPanel;
    private JPanel parametersPanel;
    private JPanel selectionPanel;
    private JButton startSimulationButton;
    private JComboBox<String> selectionPolicy;


    public ParametersSelectionWindow()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainFrame = new JFrame("Select parameters");
        mainFrame.setSize(new Dimension(xSize, ySize));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridLayout(2,1));
        parametersPanel = new JPanel(new GridLayout(4, 2));
        selectionPanel = new JPanel(new GridLayout(2, 1));
        parameters = new ArrayList<>();
        initializeParametersTextFields();
        addParametersToPanel();
        initializeSelections();
        addSelectionsToPanel();

        mainPanel.add(parametersPanel);
        mainPanel.add(selectionPanel);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private void initializeSelections()
    {
        String[] choices = new String[SelectionPolicy.values().length];
        for (int i = 0; i < SelectionPolicy.values().length; i++)
        {
            choices[i] = SelectionPolicy.values()[i].toString();
        }

        selectionPolicy = new JComboBox<>(choices);
        startSimulationButton = new JButton("Start Simulation");

    }

    private void addSelectionsToPanel()
    {
        selectionPanel.add(selectionPolicy);
        selectionPanel.add(startSimulationButton);
    }

    private void initializeParametersTextFields()
    {
        for (int i = 0; i < Parameters.values().length; i++)
        {
            JTextField newTextField = new JTextField();
            newTextField.setBorder(new TitledBorder(Parameters.values()[i].toString()));
            parameters.add(newTextField);
        }
    }

    private void addParametersToPanel()
    {
        for (JTextField jTextField : parameters)
        {
            parametersPanel.add(jTextField);
        }
    }

    public void addActionListener(ActionListener e)
    {
        startSimulationButton.addActionListener(e);
    }

    public int getValue(Parameters parameterWanted)
    {
        return Integer.parseInt(parameters.get(parameterWanted.ordinal()).getText());
    }

    public SelectionPolicy getSelectionPolicy()
    {
        return SelectionPolicy.valueOf(Objects.requireNonNull(selectionPolicy.getSelectedItem()).toString());
    }

}
