package net.zomis.reviewprepare;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ReviewPrepareFrame extends JFrame {

    private static final long serialVersionUID = 2050188992596669693L;
    private final JTextArea result = new JTextArea();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            EventQueue.invokeLater(() -> {
                try {
                    new ReviewPrepareFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            ReviewPreparer.start(args);
        }
    }

    /**
     * Create the frame.
     */
    public ReviewPrepareFrame() {
        setTitle("Prepare code for Code Review");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        final DefaultListModel<File> model = new DefaultListModel<>();
        final JList<File> list = new JList<>();
        panel.add(list);
        list.setModel(model);

        JButton btnAddFiles = new JButton("Add files");
        btnAddFiles.addActionListener(e -> {
            JFileChooser dialog = new JFileChooser();
            dialog.setMultiSelectionEnabled(true);
            if (dialog.showOpenDialog(ReviewPrepareFrame.this)
                    == JFileChooser.APPROVE_OPTION) {
                for (File file : dialog.getSelectedFiles()) {
                    model.addElement(file);
                }
            }
        });
        panel.add(btnAddFiles);

        JButton btnRemoveFiles = new JButton("Remove files");
        btnRemoveFiles.addActionListener(e -> {
            for (File file : new ArrayList<>(list.getSelectedValuesList())) {
                model.removeElement(file);
            }
        });
        panel.add(btnRemoveFiles);

        JButton performButton = new JButton(
                "Create Question stub with code included");
        performButton.addActionListener(arg0 -> {
            ReviewPreparer preparer = new ReviewPreparer(filesToList(model));
            result.setText(preparer.createFormattedQuestion());
        });
        contentPane.add(performButton, BorderLayout.SOUTH);
        contentPane.add(result, BorderLayout.CENTER);

//	    JScrollPane scroll = new JScrollPane(result);
//	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public List<File> filesToList(DefaultListModel<File> model) {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            files.add(model.get(i));
        }
        return files;
    }

}
