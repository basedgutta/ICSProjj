import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;

public class ExportPage extends JPanel {
    private JTable selectionTable;
    private DefaultTableModel tableModel;
    private QuestionBank questionBank;

    public ExportPage(QuestionBank questionBank) {
        this.questionBank = questionBank;
        setLayout(new BorderLayout());
        initializeUI();
        populateTableModel();
    }

    private void initializeUI() {
        // Table model with selection checkboxes
        tableModel = new DefaultTableModel(new Object[]{"Select", "Type", "Name"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Make checkboxes editable
            }
        };

        // Populate the table model with questions and tests
        populateTableModel();

        selectionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(selectionTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton exportButton = new JButton("Export to PDF");
        exportButton.addActionListener(this::exportToPDF);
        add(exportButton, BorderLayout.SOUTH);
    }

    private void populateTableModel() {
        // Clear the table model first to avoid duplicate entries
        tableModel.setRowCount(0);

        // Add questions to the table
        for (Question question : questionBank.getQuestions()) {
            tableModel.addRow(new Object[]{false, "Question", question.getName()});
        }

        // Add tests to the table
        for (Test test : questionBank.getTests()) {
            tableModel.addRow(new Object[]{false, "Test", test.getName()});
        }
    }

    private void exportToPDF(ActionEvent e) {
        String pdfName = JOptionPane.showInputDialog(this, "Enter PDF name:");
        if (pdfName == null || pdfName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "PDF name cannot be empty.");
            return;
        }
        String destination = "pdfs/" + pdfName + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(destination);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title page
            document.add(new Paragraph(pdfName).setFontSize(24).setTextAlignment(TextAlignment.CENTER));
            pdf.addNewPage();

            // Iterate through table model
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
                if (isSelected) {
                    String type = (String) tableModel.getValueAt(i, 1);
                    String name = (String) tableModel.getValueAt(i, 2);
                    document.add(new Paragraph(type + ": " + name));
                    document.add(new Paragraph("Answer: _______________________________\n"));
                    pdf.addNewPage(); // Start each question/test on a new page
                }
            }

            document.close();
            JOptionPane.showMessageDialog(this, "PDF exported successfully to: " + destination);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to export PDF.");
        }
    }

}

