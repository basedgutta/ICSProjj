import java.util.ArrayList;
import java.io.IOException;
import java.util.*;

public class Main {
	public static void main(String[] args) {
        QuestionBank qb = new QuestionBank();

        MainApplicationFrame f = new MainApplicationFrame(qb);
        f.setVisible(true);
	}
}
