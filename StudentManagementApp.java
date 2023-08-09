import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentManagementApp {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JButton manageProfileButton;
    private JButton manageResultsButton;
    private JButton manageSubjectsButton;
    private JButton exitButton;

    private JFrame subjectFrame;
    private JTextField subjectCodeField;
    private JTextField subjectNameField;
    private JButton confirmSubjectButton;
    private JButton exitSubjectButton;

    private JFrame subjectInfoFrame;
    private JLabel subjectCodeLabel;
    private JLabel subjectNameLabel;
    private JLabel teacherNameLabel;
    private JLabel semesterLabel;
    private JLabel examScheduleLabel;

    private Connection connection;
    private PreparedStatement preparedStatement;

    public StudentManagementApp() {
        initializeDatabase();
        initializeMainUI();
    }

    private void initializeDatabase() {
        String dbUrl = "jdbc:mysql://localhost:3306/your_db_name";
        String dbUser = "your_db_username";
        String dbPassword = "your_db_password";

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeMainUI() {
        mainFrame = new JFrame("Quản lý học sinh");
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));
        
        manageProfileButton = new JButton("Quản lý hồ sơ học sinh");
        manageProfileButton.setFont(new Font("Arial", Font.BOLD, 25));
        manageResultsButton = new JButton("Quản lý kết quả học tập học sinh");
        manageResultsButton.setFont(new Font("Arial", Font.BOLD, 25));
        manageSubjectsButton = new JButton("Quản lý môn học học sinh");
        manageSubjectsButton.setFont(new Font("Arial", Font.BOLD, 25));
        exitButton = new JButton("Thoát");
        exitButton.setFont(new Font("Arial", Font.BOLD, 25));
        
        mainPanel.add(manageProfileButton);
        mainPanel.add(manageResultsButton);
        mainPanel.add(manageSubjectsButton);
        mainPanel.add(exitButton);

        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setSize(500, 300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        manageSubjectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSubjectForm();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void showSubjectForm() {
        subjectFrame = new JFrame("Quản lý môn học");
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel subjectCodeLabel = new JLabel("Nhập mã môn học:");
        subjectCodeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        JLabel subjectNameLabel = new JLabel("Nhập tên môn học:");
        subjectNameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        subjectCodeField = new JTextField();
        subjectNameField = new JTextField();

        confirmSubjectButton = new JButton("Xác nhận");
        confirmSubjectButton.setFont(new Font("Arial", Font.BOLD, 25));
        exitSubjectButton = new JButton("Thoát");
        exitSubjectButton.setFont(new Font("Arial", Font.BOLD, 25));
        
        panel.add(subjectCodeLabel);
        panel.add(subjectCodeField);
        panel.add(subjectNameLabel);
        panel.add(subjectNameField);
        panel.add(confirmSubjectButton);
        panel.add(exitSubjectButton);

        subjectFrame.add(panel);
        subjectFrame.pack();
        subjectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        subjectFrame.setSize(500, 300);
        subjectFrame.setLocationRelativeTo(null);
        subjectFrame.setVisible(true);

        confirmSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSubjectInfo();
            }
        });

        exitSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectFrame.dispose();
            }
        });
    }

    private void checkSubjectInfo() {
        String subjectCode = subjectCodeField.getText();
        String subjectName = subjectNameField.getText();

        String query = "SELECT * FROM subjects WHERE subject_code = ? AND subject_name = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, subjectCode);
            preparedStatement.setString(2, subjectName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                showSubjectInfoForm(resultSet);
            } else {
                JOptionPane.showMessageDialog(null, "Môn học này không tồn tại");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showSubjectInfoForm(ResultSet resultSet) throws SQLException {
        subjectInfoFrame = new JFrame("Thông tin môn học");
        JPanel panel = new JPanel(new GridLayout(5, 2));

        subjectCodeLabel = new JLabel("Mã môn học:");
        subjectCodeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        subjectNameLabel = new JLabel("Tên môn học:");
        subjectNameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        teacherNameLabel = new JLabel("Tên giáo viên dạy:");
        teacherNameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        semesterLabel = new JLabel("Học kì giảng dạy:");
        semesterLabel.setFont(new Font("Arial", Font.BOLD, 25));
        examScheduleLabel = new JLabel("Lịch kiểm tra:");
        examScheduleLabel.setFont(new Font("Arial", Font.BOLD, 25));

        String subjectCode = resultSet.getString("subject_code");
        String subjectName = resultSet.getString("subject_name");
        String teacherName = resultSet.getString("teacher_name");
        String semester = resultSet.getString("semester");
        String examSchedule = resultSet.getString("exam_schedule");

        JLabel subjectCodeValue = new JLabel(subjectCode);
        JLabel subjectNameValue = new JLabel(subjectName);
        JLabel teacherNameValue = new JLabel(teacherName);
        JLabel semesterValue = new JLabel(semester);
        JLabel examScheduleValue = new JLabel(examSchedule);

        panel.add(subjectCodeLabel);
        panel.add(subjectCodeValue);
        panel.add(subjectNameLabel);
        panel.add(subjectNameValue);
        panel.add(teacherNameLabel);
        panel.add(teacherNameValue);
        panel.add(semesterLabel);
        panel.add(semesterValue);
        panel.add(examScheduleLabel);
        panel.add(examScheduleValue);

        subjectInfoFrame.add(panel);
        subjectInfoFrame.pack();
        subjectInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        subjectInfoFrame.setSize(500, 300);
        subjectInfoFrame.setLocationRelativeTo(null);
        subjectInfoFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementApp();
            }
        });
    }
}
