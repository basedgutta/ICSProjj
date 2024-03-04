import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

// In progress
// Code taken from binary search tree lesson.
public class QuestionPage extends JPanel {
    private QuestionBank questionBank;
    private JTable questionsTable;
    private DefaultTableModel model;
    
    String[] difficulties = {"Easy", "Medium", "Hard"};
    
    Map<String, Integer> difficultyMap = new HashMap<>(); // hashmap
    Map<Integer, String> mapInversedDifficulty = new HashMap<>();

    public QuestionPage(QuestionBank questionBank) {
        this.questionBank = questionBank;
        setLayout(new BorderLayout());

        difficultyMap.put("Easy", 1);
        difficultyMap.put("Medium", 2);
        difficultyMap.put("Hard", 3);

        for(Map.Entry<String, Integer> entry : difficultyMap.entrySet()){
            mapInversedDifficulty.put(entry.getValue(), entry.getKey());
        }

        model = new DefaultTableModel();
        model.addColumn("Question Name");
        model.addColumn("Type");
        model.addColumn("Difficulty");
        questionsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(questionsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons to avoid the sort button taking up too much space
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Use FlowLayout for proper button sizing and positioning
        JButton addButton = new JButton("Add Question");
        JButton deleteButton = new JButton("Delete Question");
        addButton.addActionListener(this::addQuestion);
        deleteButton.addActionListener(this::deleteQuestion);

        JButton undoLastQuestionButton = new JButton("Undo Last Question");
        undoLastQuestionButton.addActionListener(e -> {
            questionBank.removeLastQuestion();
            refreshQuestionsTable();

        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(undoLastQuestionButton);

        // Add the sort button to the button panel instead of directly to the main panel
        addSortButton(buttonPanel); // Pass the button panel to the addSortButton method

        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom of the main panel




        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom of the main panel


        refreshQuestionsTable();
    }

    // Modified to accept a panel as a parameter
    private void addSortButton(JPanel panel) {
        JButton sortButton = new JButton("Sort Questions");
        sortButton.addActionListener(e -> {
            String[] options = {"By Name", "By Difficulty"};
            int choice = JOptionPane.showOptionDialog(null, "Sort questions:",
                    "Sort Criteria", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            List<Question> questions = questionBank.getQuestions(); // Assuming a getter method exists
            if (choice == 0) {
                MergeSort.mergeSort(questions); // Directly call the method for sorting by name
            } else if (choice == 1) {
                MergeSort.mergeSortByDifficulty(questions); // Directly call the method for sorting by difficulty
            }
            refreshQuestionsTable(); // Update the table with sorted questions
        });

        panel.add(sortButton); // Add the sort button to the provided panel
    }





    private void addQuestion(ActionEvent actionEvent) {
        String[] questionTypes = {"Multiple Choice", "Written Response"};
        int type = JOptionPane.showOptionDialog(null, "Select the type of question to add:",
                "Question Type", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, questionTypes, questionTypes[0]);

        String difficultyLabel = (String) JOptionPane.showInputDialog(null, "Select difficulty:",
                "Difficulty", JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (difficultyLabel != null) {
            int difficulty = difficultyMap.get(difficultyLabel);
            // Prompt for the question name here
            String questionName = JOptionPane.showInputDialog("Enter the question name:");
            if (questionName != null && !questionName.isEmpty()) {
                if (type == 0) {
                    addMultipleChoiceQuestion(difficulty, questionName);
                } else if (type == 1) {
                    addWrittenResponseQuestion(difficulty, questionName);
                }
            }
        }
    }


    private void addMultipleChoiceQuestion(int difficulty, String name) {
        String questionText = JOptionPane.showInputDialog("Enter the multiple choice question:");
        if (questionText != null && !questionText.trim().isEmpty()) {
            ArrayList<String> options = new ArrayList<>();
            while (true) {
                String option = JOptionPane.showInputDialog("Enter an option (or cancel to finish):");
                if (option == null || option.trim().isEmpty()) {
                    break;
                }
                options.add(option.trim());
            }

            if (!options.isEmpty()) {
                Object[] optionsArray = options.toArray(new String[0]); // Explicitly specify the type for clarity
                String correctAnswer = (String) JOptionPane.showInputDialog(null,
                        "Select the correct answer:", "Correct Answer", JOptionPane.QUESTION_MESSAGE, null,
                        optionsArray, optionsArray[0]);

                if (correctAnswer != null && options.contains(correctAnswer)) {
                    MultipleChoiceQuestion mcQuestion = new MultipleChoiceQuestion(questionText, options, options.indexOf(correctAnswer), name, difficulty);
                    questionBank.addQuestion(mcQuestion); // Ensure this method properly adds the question to your bank
                }
            } else {
                JOptionPane.showMessageDialog(null, "No options were entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        refreshQuestionsTable();
    }


    private void addWrittenResponseQuestion(int difficulty, String name) {
        String questionText = JOptionPane.showInputDialog("Enter the written response question:");
        if (questionText != null && !questionText.isEmpty()) {
            String answerText = JOptionPane.showInputDialog("Enter the answer");

            WrittenResponseQuestion wrQuestion = new WrittenResponseQuestion(questionText, answerText, name, difficulty);
            questionBank.addQuestion(wrQuestion);
            refreshQuestionsTable();
        }
    }
        private void deleteQuestion (ActionEvent actionEvent){
            int selectedRow = questionsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a question to delete.", "No Question Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }


            ArrayList<Question> allQuestions = questionBank.getQuestions();
            Question questionToDelete = allQuestions.get(selectedRow);


            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?\n" + questionToDelete.getText(), "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {

                if (questionBank.removeQuestion(allQuestions.indexOf(questionToDelete))) {
                    JOptionPane.showMessageDialog(this, "Question deleted successfully.", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete the question. Please try again.", "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                }
                refreshQuestionsTable();
            }
        }





    private void refreshQuestionsTable() {
        model.setRowCount(0); // Clear the table
        for (Question question : questionBank.getQuestions()) {
            model.addRow(new Object[]{question.getName(), question.getQuestionType(), mapInversedDifficulty.get(question.getDifficulty())});
        }
    }

}
