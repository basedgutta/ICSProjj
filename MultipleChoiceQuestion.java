import java.util.*;

public class MultipleChoiceQuestion extends Question{
    private ArrayList<String> choices;
    private int correctChoiceIndex;

    public MultipleChoiceQuestion(String text, ArrayList<String> choices, int correctChoiceIndex, String name, int difficulty) {
        super(text, choices.get(correctChoiceIndex), name, difficulty);
        this.choices = choices;
        this.correctChoiceIndex = correctChoiceIndex;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getCorrectChoiceIndex() {
        return correctChoiceIndex;
    }
    
    public String getCorrectAnswer() {
    	return choices.get(correctChoiceIndex);
    }

    @Override
    public String getQuestionType() {
        return "Multiple Choice";
    }

    @Override
    public void displayQuestion() {
        System.out.println("Multiple Choice Question:");
        System.out.println(super.getText());
        System.out.println("Choices:");
        for (int i = 0; i < choices.size(); i++) {
            System.out.println((i + 1) + ". " + choices.get(i));
        }
    }

    @Override
    public String toFileString() { // save to file.
        String delimiter = "|";
        String choicesString = String.join(",", choices);
        return getQuestionType() + delimiter + super.getText() + delimiter + choicesString + delimiter + correctChoiceIndex + delimiter + super.getName() + delimiter + super.getDifficulty();
    }

}

