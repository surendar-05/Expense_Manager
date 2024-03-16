import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ExpenseManager {
    private JFrame frame;
    private JTextField salaryField;
    private JTextField ExpenseField;
    private JTextField ExpenseDateField;
    private JButton salaryButton;
    private JTextField expenseField;
    private DefaultListModel<String> expenseListModel;
    private JList<String> expenseList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton; 
    private JLabel remainingLabel;
    private double monthlySalary = 0.0;

    public ExpenseManager() {
        frame = new JFrame("Expense Manager");
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        expenseListModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseListModel);
        JScrollPane expenseScrollPane = new JScrollPane(expenseList);
        frame.add(expenseScrollPane, BorderLayout.CENTER);

        JPanel expenseInputPanel = new JPanel();
        salaryButton = new JButton("Enter Salary");
        salaryField = new JTextField(10);
        ExpenseDateField = new JTextField(10);
        ExpenseField = new JTextField(10);

        expenseField = new JTextField(10);
        addButton = new JButton("Add Expense");
        deleteButton = new JButton("Delete Expense");
        editButton = new JButton("Edit Expense"); // Initialize edit button

        remainingLabel = new JLabel("Remaining Balance: $0.00");

        salaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String salaryText = salaryField.getText();
                if (!salaryText.isEmpty()) {
                    try {
                        monthlySalary = Double.parseDouble(salaryText);
                        updateRemainingLabel();
                        salaryField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid salary amount!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
          // ActionListener for add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expenseDateField = ExpenseDateField.getText();
                String expenseText = expenseField.getText();
                String Expensefield = ExpenseField.getText();
        
                if (!expenseText.isEmpty()) {
                    try {
                        double expense = Double.parseDouble(expenseText);
                        if (expense <= monthlySalary) {
                            expenseListModel.addElement(expenseDateField + "  :  " + Expensefield + "  :  " + "$"
                                    + String.format("%.2f", expense));
                            monthlySalary -= expense;
                            updateRemainingLabel();
                            expenseField.setText("");
                            ExpenseField.setText(""); // Clear expense name field
                            ExpenseDateField.setText(""); // Clear expense date field
                        } else {
                            JOptionPane.showMessageDialog(frame, "Expense exceeds remaining balance!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid expense amount!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
          // ActionListener for delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = expenseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedExpense = expenseListModel.getElementAt(selectedIndex);
                    int colonIndex = selectedExpense.lastIndexOf(":");
                    int dollarIndex = selectedExpense.lastIndexOf("$");
                    if (colonIndex != -1 && dollarIndex != -1) {
                        String expenseAmountStr = selectedExpense.substring(dollarIndex + 1).trim();
                        try {
                            double expense = Double.parseDouble(expenseAmountStr);
                            expenseListModel.remove(selectedIndex);
                            monthlySalary += expense;
                            updateRemainingLabel();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid expense amount!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // ActionListener for edit button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedIndex = expenseList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            String selectedExpense = expenseListModel.getElementAt(selectedIndex);
                            String[] parts = selectedExpense.split(":");
                            if (parts.length == 3) {
                                String expenseDateField = parts[0].trim();
                                String expenseName = parts[1].trim();
                                String expenseAmountStr = parts[2].trim().substring(1);
                                String newExpenseName = JOptionPane.showInputDialog(frame, "Enter new expense name:", expenseName);
                                String newExpenseDate = JOptionPane.showInputDialog(frame, "Enter new expense date:", expenseDateField);
                                String newExpenseAmountStr = JOptionPane.showInputDialog(frame, "Enter new expense amount:", expenseAmountStr);
                                try {
                                    double newExpenseAmount = Double.parseDouble(newExpenseAmountStr);
                                    double originalExpenseAmount = Double.parseDouble(expenseAmountStr);
                                    expenseListModel.set(selectedIndex, newExpenseDate + " : " + newExpenseName + " : $" + String.format("%.2f", newExpenseAmount));
                                    monthlySalary += (originalExpenseAmount - newExpenseAmount);
                                    updateRemainingLabel();
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(frame, "Invalid expense amount!", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                });
                
               
                
            }
        });

        expenseInputPanel.add(new JLabel("Enter Monthly Salary: $"));
        expenseInputPanel.add(salaryField);
        expenseInputPanel.add(salaryButton);
        expenseInputPanel.add(new JLabel("Enter Expense Name:"));
        expenseInputPanel.add(ExpenseField);
        expenseInputPanel.add(new JLabel("Enter Expense Date:"));
        expenseInputPanel.add(ExpenseDateField);

        expenseInputPanel.add(new JLabel("Enter Expense: $"));
        expenseInputPanel.add(expenseField);
        expenseInputPanel.add(addButton);
        expenseInputPanel.add(deleteButton);
        expenseInputPanel.add(editButton); 

        frame.add(expenseInputPanel, BorderLayout.SOUTH);
        frame.add(remainingLabel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private void updateRemainingLabel() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        remainingLabel.setText("Remaining Balance: $" + decimalFormat.format(monthlySalary));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseManager();
        });
    }
}
