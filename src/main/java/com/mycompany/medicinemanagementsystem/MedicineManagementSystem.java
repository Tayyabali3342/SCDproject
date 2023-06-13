/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.medicinemanagementsystem;

/**
 *
 * @author user
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Medicine {
    private String name;
    private int quantity;

    public Medicine(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

interface MedicineRepository {
    void insertMedicine(Medicine medicine);

    void deleteMedicine(String name);

    void updateMedicine(String name, int newQuantity);

    List<Medicine> getAllMedicines();
}

class InMemoryMedicineRepository implements MedicineRepository {
    private List<Medicine> inventory;

    public InMemoryMedicineRepository(List<Medicine> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void insertMedicine(Medicine medicine) {
        inventory.add(medicine);
    }

    @Override
    public void deleteMedicine(String name) {
        inventory.removeIf(medicine -> medicine.getName().equalsIgnoreCase(name));
    }

    @Override
    public void updateMedicine(String name, int newQuantity) {
        for (Medicine medicine : inventory) {
            if (medicine.getName().equalsIgnoreCase(name)) {
                medicine.setQuantity(newQuantity);
                break;
            }
        }
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return inventory;
    }
}

class MedicineManagement {
    private MedicineRepository medicineRepository;

    public MedicineManagement(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public void insertMedicine(Medicine medicine) {
        medicineRepository.insertMedicine(medicine);
    }

    public void updateMedicine(String name, int newQuantity) {
        medicineRepository.updateMedicine(name, newQuantity);
    }

    public void deleteMedicine(String name) {
        medicineRepository.deleteMedicine(name);
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.getAllMedicines();
    }
}

public class MedicineManagementSystem {
    private JFrame frame;
    private JTextArea medicineListTextArea;
    private JTextField nameField;
    private JTextField quantityField;
    private MedicineManagement medicineManagement;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                List<Medicine> inventory = new ArrayList<>();
                MedicineRepository medicineRepository = new InMemoryMedicineRepository(inventory);
                MedicineManagementSystem system = new MedicineManagementSystem(medicineRepository);
                system.createAndShowGUI();
            }
        });
    }

    public MedicineManagementSystem(MedicineRepository medicineRepository) {
        medicineManagement = new MedicineManagement(medicineRepository);
    }

    public void createAndShowGUI() {
        frame = new JFrame("Medicine Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Medicine Name:");
        nameField = new JTextField(20);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(10);

        JButton insertButton = new JButton("Insert Medicine");
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertMedicine();
            }
        });

        JButton updateButton = new JButton("Update Medicine");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMedicine();
            }
        });

        JButton deleteButton = new JButton("Delete Medicine");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMedicine();
            }
        });

        JButton manageInventoryButton = new JButton("Manage Inventory");
        manageInventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageInventory();
            }
        });

        buttonPanel.add(nameLabel);
        buttonPanel.add(nameField);
        buttonPanel.add(quantityLabel);
        buttonPanel.add(quantityField);
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(manageInventoryButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        medicineListTextArea = new JTextArea(10, 40);
        medicineListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(medicineListTextArea);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void insertMedicine() {
        String name = nameField.getText();
        String quantityStr = quantityField.getText();

        if (!name.isEmpty() && !quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);
            Medicine medicine = new Medicine(name, quantity);
            medicineManagement.insertMedicine(medicine);

            nameField.setText("");
            quantityField.setText("");

            updateMedicineList();
            JOptionPane.showMessageDialog(frame, "Medicine inserted successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter medicine name and quantity!");
        }
    }

    private void updateMedicine() {
        String name = nameField.getText();
        String quantityStr = quantityField.getText();

        if (!name.isEmpty() && !quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);
            medicineManagement.updateMedicine(name, quantity);

            nameField.setText("");
            quantityField.setText("");

            updateMedicineList();
            JOptionPane.showMessageDialog(frame, "Medicine updated successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter medicine name and quantity!");
        }
    }

    private void deleteMedicine() {
        String name = nameField.getText();

        if (!name.isEmpty()) {
            medicineManagement.deleteMedicine(name);

            nameField.setText("");
            quantityField.setText("");

            updateMedicineList();
            JOptionPane.showMessageDialog(frame, "Medicine deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter medicine name!");
        }
    }

    private void manageInventory() {
        List<Medicine> medicines = medicineManagement.getAllMedicines();
        StringBuilder sb = new StringBuilder();

        sb.append("======= Inventory =======\n");
        for (Medicine medicine : medicines) {
            sb.append("Medicine Name: ").append(medicine.getName()).append("\n");
            sb.append("Quantity: ").append(medicine.getQuantity()).append("\n");
            sb.append("------------------------\n");
        }

        medicineListTextArea.setText(sb.toString());
    }

    private void updateMedicineList() {
        manageInventory();
    }
}

