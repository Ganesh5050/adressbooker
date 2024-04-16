import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Contact implements Serializable {
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private String residentialAddress; // Added residential address field

    public Contact(String name, String phoneNumber, String emailAddress, String residentialAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.residentialAddress = residentialAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone Number: " + phoneNumber + ", Email Address: " + emailAddress + ", Residential Address: " + residentialAddress;
    }
}

class AddressBook implements Serializable {
    private List<Contact> contacts;

    public AddressBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(String name) {
        contacts.removeIf(contact -> contact.getName().equals(name));
    }

    public Contact searchContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    // Method to write address book to a file
    public void saveToFile(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(contacts);
            System.out.println("Address book saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving address book: " + e.getMessage());
        }
    }

    // Method to read address book from a file
    public void loadFromFile(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            contacts = (List<Contact>) inputStream.readObject();
            System.out.println("Address book loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while loading address book: " + e.getMessage());
        }
    }
}

public class AddressBookGUI extends JFrame {
    private AddressBook addressBook;
    private JTable table;
    private DefaultTableModel tableModel;

    public AddressBookGUI() {
        addressBook = new AddressBook();

        setTitle("Address Book");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add Contact");
        JButton removeButton = new JButton("Remove Contact");
        JButton searchButton = new JButton("Search Contact");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load from File");

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Email Address");
        tableModel.addColumn("Residential Address");
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.NORTH);
        panel.add(removeButton, BorderLayout.SOUTH);
        panel.add(searchButton, BorderLayout.EAST);
        panel.add(saveButton, BorderLayout.WEST);
        panel.add(loadButton, BorderLayout.SOUTH);

        add(panel);

        addButton.addActionListener(e -> addContact());
        removeButton.addActionListener(e -> removeContact());
        searchButton.addActionListener(e -> searchContact());
        saveButton.addActionListener(e -> saveAddressBook());
        loadButton.addActionListener(e -> loadAddressBook());
    }

    private void addContact() {
        String name = JOptionPane.showInputDialog("Enter name:");
        String phoneNumber = JOptionPane.showInputDialog("Enter phone number:");
        String emailAddress = JOptionPane.showInputDialog("Enter email address:");
        String residentialAddress = JOptionPane.showInputDialog("Enter residential address:");

        if (name != null && phoneNumber != null && emailAddress != null && residentialAddress != null) {
            Contact contact = new Contact(name, phoneNumber, emailAddress, residentialAddress);
            addressBook.addContact(contact);
            updateTable();
        }
    }

    private void removeContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) table.getValueAt(selectedRow, 0);
            addressBook.removeContact(name);
            updateTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to remove.");
        }
    }

    private void searchContact() {
        String name = JOptionPane.showInputDialog("Enter name to search:");
        if (name != null) {
            Contact contact = addressBook.searchContact(name);
            if (contact != null) {
                JOptionPane.showMessageDialog(null, contact);
            } else {
                JOptionPane.showMessageDialog(null, "Contact not found.");
            }
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear the table
        for (Contact contact : addressBook.getContacts()) {
            Object[] row = {contact.getName(), contact.getPhoneNumber(), contact.getEmailAddress(), contact.getResidentialAddress()};
            tableModel.addRow(row);
        }
    }

    private void saveAddressBook() {
        String filename = JOptionPane.showInputDialog("Enter filename to save:");
        if (filename != null) {
            addressBook.saveToFile(filename);
        }
    }

    private void loadAddressBook() {
        String filename = JOptionPane.showInputDialog("Enter filename to load:");
        if (filename != null) {
            addressBook.loadFromFile(filename);
            updateTable();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddressBookGUI addressBookGUI = new AddressBookGUI();
            addressBookGUI.setVisible(true);
        });
    }
}
