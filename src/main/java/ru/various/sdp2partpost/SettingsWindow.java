package ru.various.sdp2partpost;

import ru.various.sdp2partpost.enums.Source;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SettingsWindow extends JFrame {
    private ColorUIResource gray = new ColorUIResource(Color.GRAY);
    private ColorUIResource black = new ColorUIResource(Color.BLACK);
    private JTextField sdpPathText, sdpUsernameText, sdpPasswordText,
            ppPathText, ppUsernameText, ppPasswordText;

    private PropertiesHolder propertiesHolder;

    private static final String PATH = "Путь к БД";
    private static final String USERNAME = "Имя пользователя";
    private static final String PASSWORD = "Пароль";

    public SettingsWindow(PropertiesHolder propertiesHolder) {
        this.propertiesHolder = propertiesHolder;

        setTitle("Настройки");
        setSize(new Dimension(400, 200));
        setLocation(350, 350);
        setMinimumSize(new Dimension(400, 200));
        setMaximumSize(new Dimension(400, 200));

        JTabbedPane mainPanel = new JTabbedPane();
        JPanel sdpPanel = new JPanel();
        sdpPanel.setLayout(new BoxLayout(sdpPanel, BoxLayout.PAGE_AXIS));
        JPanel ppPanel = new JPanel();
        ppPanel.setLayout(new BoxLayout(ppPanel, BoxLayout.PAGE_AXIS));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        Border border = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        sdpPanel.setBorder(border);
        ppPanel.setBorder(border);
        buttonsPanel.setBorder(border);


        Dimension textFieldsSize = new Dimension(300, 30);
        sdpPathText = new JTextField(PATH);
        sdpPathText.setAlignmentX(Component.LEFT_ALIGNMENT);
        sdpPathText.setMaximumSize(textFieldsSize);
        sdpPathText.setForeground(gray);

        sdpUsernameText = new JTextField(USERNAME);
        sdpUsernameText.setAlignmentX(Component.LEFT_ALIGNMENT);
        sdpUsernameText.setMaximumSize(textFieldsSize);
        sdpUsernameText.setForeground(gray);

        sdpPasswordText = new JTextField(PASSWORD);
        sdpPasswordText.setAlignmentX(Component.LEFT_ALIGNMENT);
        sdpPasswordText.setMaximumSize(textFieldsSize);
        sdpPasswordText.setForeground(gray);

        ppPathText = new JTextField(PATH);
        ppPathText.setAlignmentX(Component.LEFT_ALIGNMENT);
        ppPathText.setMaximumSize(textFieldsSize);
        ppPathText.setForeground(gray);

        ppUsernameText = new JTextField(USERNAME);
        ppUsernameText.setAlignmentX(Component.LEFT_ALIGNMENT);
        ppUsernameText.setMaximumSize(textFieldsSize);
        ppUsernameText.setForeground(gray);

        ppPasswordText = new JTextField(PASSWORD);
        ppPasswordText.setAlignmentX(Component.LEFT_ALIGNMENT);
        ppPasswordText.setMaximumSize(textFieldsSize);
        ppPasswordText.setForeground(gray);

        setFocusListener(sdpPathText, PATH);
        setFocusListener(sdpUsernameText, USERNAME);
        setFocusListener(sdpPasswordText, PASSWORD);

        setFocusListener(ppPathText, PATH);
        setFocusListener(ppUsernameText, USERNAME);
        setFocusListener(ppPasswordText, PASSWORD);

        sdpPanel.add(sdpPathText);
        sdpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sdpPanel.add(sdpUsernameText);
        sdpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sdpPanel.add(sdpPasswordText);

        ppPanel.add(ppPathText);
        ppPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        ppPanel.add(ppUsernameText);
        ppPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        ppPanel.add(ppPasswordText);

        JButton OK = new JButton("OK");
        OK.setAlignmentX(Component.RIGHT_ALIGNMENT);
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
                dispose();
            }
        });
        JButton quit = new JButton("Закрыть");
        quit.setAlignmentX(Component.RIGHT_ALIGNMENT);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonsPanel.add(OK);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(quit);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.PAGE_END);
        mainPanel.addTab("Настройки СДП", sdpPanel);
        mainPanel.addTab("Настройки Partpost", ppPanel);

        loadSettings();
        checkDefaults();

        setVisible(true);
        pack();
    }

    public void setFocusListener(final JTextComponent field, final String defaultText) {
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (defaultText.equals(field.getText())) {
                    field.setText("");
                    field.setForeground(black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(field.getText())) {
                    field.setText(defaultText);
                    field.setForeground(gray);
                }
            }
        });
    }

    private void saveSettings() {
        String sdpPath = PropertiesHolder.PATH.equals(sdpPathText.getText()) ? "" : sdpPathText.getText();
        String sdpUsername = PropertiesHolder.USERNAME.equals(sdpUsernameText.getText()) ? "" : sdpUsernameText.getText();
        String sdpPassword = PropertiesHolder.PASSWORD.equals(sdpPasswordText.getText()) ? "" : sdpPasswordText.getText();
        String ppPath = PropertiesHolder.PATH.equals(ppPathText.getText()) ? "" : ppPathText.getText();
        String ppUsername = PropertiesHolder.USERNAME.equals(ppUsernameText.getText()) ? "" : ppUsernameText.getText();
        String ppPassword = PropertiesHolder.PASSWORD.equals(ppPasswordText.getText()) ? "" : ppPasswordText.getText();

        propertiesHolder.setProperty(Source.SDP, PropertiesHolder.PATH, sdpPath);
        propertiesHolder.setProperty(Source.SDP, PropertiesHolder.USERNAME, sdpUsername);
        propertiesHolder.setProperty(Source.SDP, PropertiesHolder.PASSWORD, sdpPassword);
        propertiesHolder.setProperty(Source.PP, PropertiesHolder.PATH, ppPath);
        propertiesHolder.setProperty(Source.PP, PropertiesHolder.USERNAME, ppUsername);
        propertiesHolder.setProperty(Source.PP, PropertiesHolder.PASSWORD, ppPassword);

        propertiesHolder.storeToRegistry(Source.SDP);
        propertiesHolder.storeToRegistry(Source.PP);
    }

    private void loadSettings() {
        propertiesHolder.loadFromRegistry(Source.SDP);
        propertiesHolder.loadFromRegistry(Source.PP);

        String sdpPath = propertiesHolder.getProperty(Source.SDP, PropertiesHolder.PATH, PATH);
        String sdpUsername = propertiesHolder.getProperty(Source.SDP, PropertiesHolder.USERNAME, USERNAME);
        String sdpPassword = propertiesHolder.getProperty(Source.SDP, PropertiesHolder.PASSWORD, PASSWORD);

        String ppPath = propertiesHolder.getProperty(Source.PP, PropertiesHolder.PATH, PATH);
        String ppUsername = propertiesHolder.getProperty(Source.PP, PropertiesHolder.USERNAME, USERNAME);
        String ppPassword = propertiesHolder.getProperty(Source.PP, PropertiesHolder.PASSWORD, PASSWORD);

        sdpPathText.setText(sdpPath);
        sdpUsernameText.setText(sdpUsername);
        sdpPasswordText.setText(sdpPassword);

        ppPathText.setText(ppPath);
        ppUsernameText.setText(ppUsername);
        ppPasswordText.setText(ppPassword);
    }

    private void checkDefaults() {
        sdpPathText.setForeground(PATH.equals(sdpPathText.getText()) ? gray : black);
        sdpUsernameText.setForeground(USERNAME.equals(sdpUsernameText.getText()) ? gray : black);
        sdpPasswordText.setForeground(PASSWORD.equals(sdpPasswordText.getText()) ? gray : black);
        ppPathText.setForeground(PATH.equals(ppPathText.getText()) ? gray : black);
        ppUsernameText.setForeground(USERNAME.equals(ppUsernameText.getText()) ? gray : black);
        ppPasswordText.setForeground(PASSWORD.equals(ppPasswordText.getText()) ? gray : black);
    }
}
