import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;



public class TestPage extends JPanel {
    private QuestionBank questionBank; // Reference to your QuestionBank
    private JList<String> testList; // Display test names
    private DefaultListModel<String> testListModel; // Model for managing test names

    public TestPage(QuestionBank questionBank) {
        this.questionBank = questionBank;
        initializeUI();
        loadTests();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        testListModel = new DefaultListModel<>();
        testList = new JList<>(testListModel);
        JScrollPane testScrollPane = new JScrollPane(testList);
        JButton undoLastTestButton = new JButton("Undo Last Test");

        add(testScrollPane, BorderLayout.CENTER);

        JButton sortButton = new JButton("Sort by Name");
        sortButton.addActionListener(this::sortTestsByName);


        JButton undoButton = new JButton("Undo Last Test");


        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Test");
        addButton.addActionListener(this::addTest);
        JButton deleteButton = new JButton("Delete Test");
        deleteButton.addActionListener(this::deleteTest);
        JButton infoButton = new JButton("Test Info");
        infoButton.addActionListener(this::showTestInfo);
        
        buttonPanel.add(addButton);
        buttonPanel.add(sortButton);

        undoButton.addActionListener(this::undoLastTest);
        buttonPanel.add(undoButton);

        buttonPanel.add(deleteButton);
        buttonPanel.add(infoButton);
        add(buttonPanel, BorderLayout.SOUTH);

        undoLastTestButton.addActionListener(e -> {
            questionBank.removeLastTest();
            testListModel.clear();
            for (Test test : questionBank.getTests()) {
                testListModel.addElement(test.getName());
            }
            // Optionally, refresh or update other UI components if necessary
        });
    }

    private void loadTests() {
        testListModel.clear();
        for (Test test : questionBank.getTests()) {
            testListModel.addElement(test.getName()); // Assuming Test has a getName() method
        }
    }

    private void undoLastTest(ActionEvent e) {

        List<Test> tests = questionBank.getTests();
        if (!tests.isEmpty()) {
            Test lastTest = tests.get(tests.size() - 1);
            questionBank.removeTest(lastTest); // You'll need to implement removeTest in QuestionBank
            testListModel.removeElement(lastTest.getName());
        } else {
            JOptionPane.showMessageDialog(this, "No tests to undo.", "Undo Last Test", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addTest(ActionEvent e) {
        List<Question> allQuestions = questionBank.getQuestions();
        if (allQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no questions available to create a new test.", "No Questions", JOptionPane.INFORMATION_MESSAGE);
        } else {
            List<Question> selectedQuestions = showQuestionSelectionDialog(allQuestions);
            if (!selectedQuestions.isEmpty()) {
                String testName = JOptionPane.showInputDialog("Enter Test Name:");
                if (testName != null && !testName.isEmpty()) {
                    Test newTest = new Test(testName);
                    for (Question question : selectedQuestions) {
                        newTest.addQuestion(question);
                    }
                    questionBank.addTest(newTest);
                    testListModel.addElement(newTest.getName());
                    // Refresh or update the test list display accordingly
                }
            }
        }
    }


    private void deleteTest(ActionEvent e) {
        String selectedTestName = testList.getSelectedValue();
        if (selectedTestName != null) {
            Test testToRemove = questionBank.getTests().stream()
                                    .filter(test -> test.getName().equals(selectedTestName))
                                    .findFirst().orElse(null);
            if (testToRemove != null) {
                questionBank.removeTest(testToRemove);
                testListModel.removeElement(selectedTestName);
            }
        }
    }

    private void showTestInfo(ActionEvent e) {
        String selectedTestName = testList.getSelectedValue();
        Test selectedTest = questionBank.getTests().stream()
                .filter(test -> test.getName().equals(selectedTestName))
                .findFirst().orElse(null);

        if (selectedTest != null) {
            List<Question> questions = new ArrayList<>();
            selectedTest.inOrderTraversal(selectedTest.getRootNode(), questions);

            String[] columnNames = {"Question Name", "Question Type"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Second parameter is row count, starting with 0
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog();
            dialog.setTitle("Test Info: " + selectedTestName);

            // Populate table data
            for (String[] rowData : populateData(questions)) {
                model.addRow(rowData);
            }

            // Create search field and button
            JTextField searchField = new JTextField(20);
            JButton searchButton = new JButton("Search");
            JPanel searchPanel = new JPanel();
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            searchButton.addActionListener(event -> {
                String searchText = searchField.getText();
                boolean found = selectedTest.searchQuestionByName(searchText);
                JOptionPane.showMessageDialog(dialog, found ? "Question found: " + searchText : "Question not found: " + searchText);
            });

            JButton sortButton = new JButton("Sort Questions");
            sortButton.addActionListener(event -> {
                List<Question> sortedQuestions = new ArrayList<>();
                selectedTest.postOrderTraversal(selectedTest.getRootNode(), sortedQuestions);
                String[][] sortedData = populateData(sortedQuestions);

                // Clear existing rows
                model.setRowCount(0);

                // Populate with sorted data
                for (String[] row : sortedData) {
                    model.addRow(row);
                }
            });

            searchPanel.add(sortButton);

            // Adding components to the dialog
            dialog.setLayout(new BorderLayout()); // Use BorderLayout for dialog
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(searchPanel, BorderLayout.NORTH);

            dialog.pack();
            dialog.setLocationRelativeTo(null); // Center on screen
            dialog.setVisible(true);
        }
    }


    private String[][] populateData(List<Question> questions) {
        String[][] data = new String[questions.size()][2];
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            data[i][0] = q.getName(); // Assuming Question has a getName() method
            data[i][1] = q.getQuestionType(); // Assuming Question has a getQuestionType() method
        }
        return data;
    }


    // Example method to show question selection dialog
    private List<Question> showQuestionSelectionDialog(List<Question> allQuestions) {
        Map<JCheckBox, Boolean> checkBoxSelectionMap = new HashMap<>();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (Question question : allQuestions) {
            JCheckBox checkBox = new JCheckBox(question.getName() + " - " + question.getQuestionType());
            checkBoxSelectionMap.put(checkBox, false); // Initialize all checkboxes as unselected
            panel.add(checkBox);

            // Add item listener to each checkbox to update selection state
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox sourceCheckBox = (JCheckBox) e.getSource();
                    checkBoxSelectionMap.put(sourceCheckBox, sourceCheckBox.isSelected());
                }
            });
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Select Questions", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Retrieve the selected Question objects based on selected checkboxes
            List<Question> selectedQuestions = checkBoxSelectionMap.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .map(entry -> {
                        JCheckBox checkBox = entry.getKey();
                        return allQuestions.get(panel.getComponentZOrder(checkBox)); // Get the corresponding Question from the original list
                    })
                    .collect(Collectors.toList());

            return selectedQuestions;
        } else {
            // User canceled or closed the dialog
            return Collections.emptyList(); // Or handle cancellation differently
        }
    }

    // Custom cell renderer for rendering checkboxes in the JList
    private static class CheckBoxListCellRenderer implements ListCellRenderer<JCheckBox> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index, boolean isSelected, boolean cellHasFocus) {
            value.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            value.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            value.setSelected(value.isSelected());
            value.setText(value.getText());
            return value;
        }
    }



    private void onAddTestClicked(ActionEvent event) {
        List<Question> allQuestions = questionBank.getQuestions(); // Assuming a method to get all questions
        DefaultListModel<Question> listModel = new DefaultListModel<>();
        allQuestions.forEach(listModel::addElement);

        JList<Question> list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        if (JOptionPane.showConfirmDialog(null, scrollPane, "Select Questions", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            List<Question> selectedQuestions = list.getSelectedValuesList();
            String testName = JOptionPane.showInputDialog("Enter test name:");
            if (testName != null && !testName.isEmpty()) {
                Test newTest = new Test(testName); // Corrected constructor parameters
                newTest.addQuestions(selectedQuestions);
                questionBank.addTest(newTest); // Assuming a method to add the test to the question bank
                // Optionally: Update UI or model to reflect the new test addition
            }
        }
    }

    private void sortTestsByName(ActionEvent e) {
        // Directly call the specific sorting method for Test objects
        MergeSort.mergeSortTests(questionBank.getTests());
        testListModel.clear();
        for (Test test : questionBank.getTests()) {
            testListModel.addElement(test.getName());
        }
    }


}

