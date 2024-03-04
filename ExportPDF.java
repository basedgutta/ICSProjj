import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileNotFoundException;
import java.util.List;

public class ExportPDF {

    public void exportQuestionsToPDF(List<Question> questions, String dest) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        for (Question question : questions) {
            // Add the question text
            document.add(new Paragraph("Question: " + question.getText()));
            // Optionally add space for the answer or other formatting
            document.add(new Paragraph("Answer: _______________________________"));
        }

        document.close();
    }

    public void exportTestsToPDF(List<Test> tests, String dest) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        for (Test test : tests) {
            // Here you can format how you display each test, for example:
            document.add(new Paragraph("Test: " + test.getName()));
            // Iterate through questions of each test if applicable
            for (Question question : test.getQuestions()) {
                document.add(new Paragraph("Question: " + question.getText()));
                document.add(new Paragraph("Answer: _______________________________"));
            }
        }

        document.close();
    }
}