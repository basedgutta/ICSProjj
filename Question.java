import java.io.Serializable;

public abstract class Question implements Serializable {
    private String text;
    private String answer;  // Add an answer field
    private String name;
    private int difficulty; 

    public Question(String text, String answer, String name, int difficulty) {
        this.text = text;
        this.answer = answer;
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getText() {
        return text;
    }
    
    public int getDifficulty() {
    	return difficulty;
    }

    public String getAnswer() {
        return answer;
    }
    
    public String getName() {
        return name;
    }

    // Define an abstract method to get the question type
    public abstract String getQuestionType();

    // Define an abstract method to display the question
    public abstract void displayQuestion();

    public abstract String toFileString();


}








