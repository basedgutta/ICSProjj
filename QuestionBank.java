import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import java.util.List;
import java.util.Stack;

public class QuestionBank {
    private ArrayList<Question> questions = new ArrayList<>();
    private List<Test> tests = new ArrayList<>();

    private Stack<Question> undoQuestions = new Stack<>();
    private Stack<Test> undoTests = new Stack<>();


    public QuestionBank() {

        // Create "pdfs" directory if it doesn't exist
        File pdfsDir = new File("pdfs");
        if (!pdfsDir.exists()) {
            boolean wasDirectoryMade = pdfsDir.mkdirs();
            if (wasDirectoryMade) {
                System.out.println("Directory created: " + pdfsDir.getName());
            } else {
                System.out.println("Failed to create directory.");
            }
        }

        File file = new File("questions.txt");

        // Try to create the file if it doesn't exist, but don't load questions here
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        // Now separately load questions from the file if it exists
        if (file.exists()) {
            loadQuestionsFromFile("questions.txt");
        }

        // Initialize tests and attempt to load them from a file
        tests = new ArrayList<>();
        File testFile = new File("tests.txt");
        if (!testFile.exists()) {
            try {
                testFile.createNewFile(); // Create the file if it doesn't exist
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadTestsFromFile("tests.txt"); // Load tests from the file
    }


    public List<Test> getTests() {
        return tests;
    }

    public void removeTest(int index) {
        if (index >= 0 && index < tests.size()) {
            tests.remove(index);
            saveTestsToFile("tests.txt"); // Adjust filename as necessary
        }
    }


    public void loadTestsFromFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    tests = (List<Test>) obj;
                } else if (obj instanceof Test) {
                    tests = new ArrayList<>();
                    tests.add((Test) obj); // Add the single Test object to the list
                }
            } catch (EOFException e) {
                System.out.println("The file is empty or corrupted.");
                tests.clear(); // Optionally clear the list to start fresh
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading from file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist or is empty. Starting with an empty test list.");
            tests = new ArrayList<>(); // Start with an empty list if the file doesn't exist or is empty
        }
    }


    public void saveTestsToFile(String filename, Test test) {
        File file = new File(filename);
        boolean append = file.exists() && file.length() > 0;

        try (ObjectOutputStream oos = append ?
                new AppendableObjectOutputStream(new FileOutputStream(file, true)) :
                new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(test);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveTestsToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tests);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean removeTest(Test test) {
        if (tests.remove(test)) {
            saveTestsToFile("tests.txt"); // Save the updated test list to the file
            return true;
        }
        return false;
    }





    public void addQuestion(Question question){
        questions.add(question);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("questions.txt", true))) {
            writer.write(question.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        undoQuestions.push(question);

    }

    public void addQuestion(Question question, int i){
        questions.add(question);
    } // remove the infinite loop


    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public boolean removeQuestion(int index) {
        boolean isSuccess = false;
        try (RandomAccessFile raf = new RandomAccessFile("questions.txt", "rw")) {
            int currentIndex = 0;
            long prevPosition = 0;
            String currentLine;
            while ((currentLine = raf.readLine()) != null) {
                if (currentIndex == index) {
                    raf.seek(prevPosition); // Go back to the start of the line
                    raf.writeBytes("DELETED: " + currentLine); // Mark the line as deleted
                    isSuccess = true;
                    break;
                }
                prevPosition = raf.getFilePointer();
                currentIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }


    public void removeLastQuestion() {
        if (!undoQuestions.isEmpty()) {
            Question question = undoQuestions.pop();
            questions.remove(question);
        }
    }



    // Randomize the order of questions
    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }

    // Load questions from a file, code inspired by stack overflow.
    public void loadQuestionsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if ("Multiple Choice".equals(parts[0])) {
                    ArrayList<String> choices = new ArrayList<>(List.of(parts[2].split(",")));
                    int correctChoiceIndex = Integer.parseInt(parts[3]);
                    int difficulty = Integer.parseInt(parts[5]);
                    addQuestion(new MultipleChoiceQuestion(parts[1], choices, correctChoiceIndex, parts[4], difficulty), 1);
                } else if ("Written".equals(parts[0])) {
                	int difficulty = Integer.parseInt(parts[4]);
                    addQuestion(new WrittenResponseQuestion(parts[1], parts[2], parts[3], difficulty), 1);
                }
            }
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void addTest(Test test) {
        tests.add(test);
        undoTests.push(test);

        saveTestsToFile("tests.txt", test);
    }

    public void removeLastTest() {
        if (!undoTests.isEmpty()) {
            Test test = undoTests.pop();
            tests.remove(test);
        }
    }



}
