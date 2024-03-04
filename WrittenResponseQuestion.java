public class WrittenResponseQuestion extends Question{
    public WrittenResponseQuestion(String text, String answer, String name, int difficulty) {
        super(text, answer, name, difficulty);
    }

    @Override
    public String getQuestionType() {
        return "Written";
    }

    @Override
    public void displayQuestion() {
        System.out.println(super.getName());
        System.out.println(super.getText());
    }

    public String toFileString() {
        String delimiter = "|";
        return getQuestionType() + delimiter + super.getText() + delimiter + super.getAnswer() + delimiter + super.getName() + delimiter + super.getDifficulty();
    }
}

