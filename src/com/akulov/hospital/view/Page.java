package com.akulov.hospital.view;

import javax.swing.JPanel;

public interface Page {
    JPanel getPanel();
    void updateData();
}