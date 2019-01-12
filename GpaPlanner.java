//Matthew Kovalenko msk9pe
//Homework 5

//Assistance from: Peter Sepulveda, Sarah Benson, Luke McPhillips
/*
Sources:
https://www.dreamincode.net/forums/topic/204866-creating-an-instance-of-an-object-via-user-input/
https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
https://docs.oracle.com/javase/8/docs/api/javax/swing/DefaultListModel.html
https://stackoverflow.com/questions/11570356/jframe-in-full-screen-java
https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
https://stackoverflow.com/questions/17132452/java-check-if-jtextfield-is-empty-or-not
https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
https://gpacalculator.net/how-to-calculate-gpa/
http://www.java2s.com/Code/JavaAPI/java.awt/newFontStringnameintstyleintsize.htm
*/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;

/**
 * @author Matthew Kovalenko
 * Creates a GUI that allows the user to input previously taken, current, or anticipated
 * course. The courses are displayed on a JList and the user can choose to remove single
 * selected courses or remove all of them. In addition, the GPA will be calculated for
 * courses that contain grades, and if the user wishes to achieve a certain GPA, the
 * required GPA can be calculated for the current and anticipated courses.
 */
public class GpaPlanner extends JFrame{
		
	//JLabels
	private JLabel addCreditsHeader; //title header for add credits input field
	private JLabel addGradeHeader; //title header for grade selection combo box
	private JLabel addCourseTitleHeader; //title header for add course name input field
	private JLabel currentGPA; //displays current GPA calculated for inputed courses
	private JLabel targetGPA; //title header for target GPA input field
	private JLabel requiredGPA; //displays the required GPA to achieve target GPA
	private JLabel instructionsLabel1; //displays first row of instructions
	private JLabel instructionsLabel2; //displays second row of instructions
	private JLabel suggestionLabel; //displays suggestion of either adding courses or taking less credit hours depending on calculated required GPA for the target GPA

	//ArrayLists
	ArrayList<Course> gradedCourses; //list of all the inputed courses that have grades
	ArrayList<Course> noGradeCourses; //list of all the inputed courses that do not have grades
	
	//JButtons
	private JButton addNewCourse; //button for adding courses to the list
	private JButton removeCourse; //button for removing the selected course from list
	private JButton clearCourses; //button for removing all courses from list
	private JButton calculateRequiredGPA; //button for calculating GPA required for non-graded courses to achieve targetGPA
	
	//JTextFields
	private JTextField creditField; //text field for inputing number of credits
	private JTextField nameField; //text field for inputing course name
	private JTextField targetGPAField; //text field for inputing desired GPA
	
	//JComboBox
	private JComboBox<String> gradeBox; //combo box for selecting grade for course
	
	private BorderLayout layout = new BorderLayout(); //BorderLayout that the panels are added to
	
	private JList<Course> courseWindow;	//JList for list of courses
	private DefaultListModel<Course> listModel;	//List model that the list of courses implements
	
	/** adds components to the frame **/
	public void addComponentsToPane(Container pane) {
		/**
		 * ActionListener that is called whenever the "Add course" button is pressed
		 * It takes the inputed values for credits, grade, and course name, and creates
		 * a course with those values. It then adds that course to the list. In addition,
		 * if the course has a grade it is added to the ArrayList of taken course, or
		 * else it is added to the ArrayList of courses with no grade. Finally, the
		 * current GPA label is updated.
		 */
		class AddCourseListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				//default values
				int creditsInput = 0;
				String gradeSelection = "";
				String nameInput = "";
				if(e.getActionCommand().equals("click")) {
					creditsInput = Integer.parseInt(creditField.getText());
					if(!gradeBox.getSelectedItem().equals("")) { //to avoid null pointer exception
						gradeSelection = (String) gradeBox.getSelectedItem();
					}
					if(!nameField.getText().isEmpty()) {
						nameInput = nameField.getText();
					}
					
					if(!gradeSelection.equals("") && !nameInput.equals("")) {
						Course newCourse = new Course(creditsInput, gradeSelection, nameInput);
						listModel.addElement(newCourse);
						gradedCourses.add(newCourse);
					}
					if(gradeSelection.equals("") && !nameInput.equals("")) {
						Course newCourse = new Course(creditsInput);
						newCourse.setName(nameInput);
						listModel.addElement(newCourse);
						noGradeCourses.add(newCourse);
					}
					if(!gradeSelection.equals("") && nameInput.equals("")) {
						Course newCourse = new Course(creditsInput);
						newCourse.setLetterGrade(gradeSelection);
						listModel.addElement(newCourse);
						gradedCourses.add(newCourse);
					}
					if(gradeSelection.equals("") && nameInput.equals("")) {
						Course newCourse = new Course(creditsInput);
						listModel.addElement(newCourse);
						noGradeCourses.add(newCourse);
					}
					currentGPA.setText("GPA: " + calculateCurrentGPA(gradedCourses));
				}
			}
		}
		/**
		 * ActionListener that is called when the "Remove selected course" button is
		 * pressed. It removes the currently selected course from the list as well as
		 * its respective ArrayList. It then updates the GPA label.
		 */
		class RemoveCourseListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("click")) {
					if(gradedCourses.contains(courseWindow.getSelectedValue())){
						gradedCourses.remove(courseWindow.getSelectedValue());
					}
					if(noGradeCourses.contains(courseWindow.getSelectedValue())) {
						noGradeCourses.remove(courseWindow.getSelectedValue());
					}
					listModel.removeElement(courseWindow.getSelectedValue());
					currentGPA.setText("GPA: " + calculateCurrentGPA(gradedCourses));
				}
			}
		}
		/**
		 * ActionListener that is called when the "Clear all courses" button is pressed.
		 * It removes every course from the list and from both gradedCourses and
		 * noGradeCourses ArrayLists. It then updates the GPA label.
		 */
		class ClearCoursesListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("click")) {
					gradedCourses.clear();
					noGradeCourses.clear();
					listModel.removeAllElements();
					currentGPA.setText("GPA: " + calculateCurrentGPA(gradedCourses));
				}
			}
		}
		/**
		 * ActionListener that is called when the "Calculate required GPA" button is
		 * pressed. It takes the desired GPA input and calculates the GPA required for
		 * the current/anticipated courses (which have no grade) to achieve the desired
		 * GPA. If the required GPA is above 4.0, the GUI suggests that more credit
		 * hours need to be taken and if the required GPA is below 2.0, the GUI suggests
		 * that the user could take fewer credit hours.
		 */
		class CalculateRequiredGPAListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				double idealGPA = Double.parseDouble(targetGPAField.getText());
				int cumulativeCredits = 0;
				int futureCredits = 0;
				for(Course ele : gradedCourses) {
					cumulativeCredits += ele.getCredits();
				}
				for(Course ele : noGradeCourses) {
					futureCredits += ele.getCredits();
				}
				int totalFutureCredits = cumulativeCredits + futureCredits;
				if(e.getActionCommand().equals("click")) {
					requiredGPA.setText("Required GPA: " + Double.toString(((idealGPA * totalFutureCredits) - (Double.parseDouble(currentGPA.getText().substring(5)) * cumulativeCredits)) / futureCredits));
					requiredGPA.setVisible(true);
					if(Double.parseDouble(requiredGPA.getText().substring(13)) > 4.0) {
						suggestionLabel.setText(("You need to add more credit hours to achieve this GPA"));
						suggestionLabel.setVisible(true);
					}
					if(Double.parseDouble(requiredGPA.getText().substring(13)) < 2.0) {
						suggestionLabel.setText(("You could take fewer credit hours"));
						suggestionLabel.setVisible(true);
					}
				}
			}
		}
		
		pane.setLayout(layout); //sets the layout so that each panel will occupy a region of the window through BorderLayout
		gradedCourses = new ArrayList<Course>();
		Font labelFont = new Font("labelFont", Font.PLAIN, 30);
		
		//panel that list and currentGPA are added to
		JPanel listPanel = new JPanel();
		//JList setup
		listModel = new DefaultListModel<Course>();
		courseWindow = new JList<Course>(listModel);
		courseWindow.setModel(listModel);
		courseWindow.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		courseWindow.setLayoutOrientation(JList.VERTICAL);
		courseWindow.setSize(100, 100);
		courseWindow.setFont(labelFont);
		currentGPA = new JLabel();
		currentGPA.setText("GPA: 0.0");
		currentGPA.setFont(labelFont);
		BoxLayout listLayout = new BoxLayout(listPanel, BoxLayout.PAGE_AXIS);
		listPanel.setLayout(listLayout);
		listPanel.add(currentGPA);
		listPanel.add(courseWindow);
		pane.add(listPanel, BorderLayout.WEST); //adds listPanel to left side of screen

		Font inputFont = new Font("inputFont", Font.PLAIN, 20);
		//panel that course input fields, combo boxes, and their labels
		JPanel courseInfoPanel = new JPanel();
		courseInfoPanel.setLayout(new FlowLayout()); //gives flowLayout to this panel
		//input field for inputing number of credits and label
		creditField = new JTextField(3);
		creditField.setFont(inputFont);
		addCreditsHeader = new JLabel("Credits:");
		addCreditsHeader.setFont(labelFont);
		//Combo box for selecting course grade
		String[] possibleGrades = {"", "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"};
		gradeBox = new JComboBox<String>(possibleGrades);
		gradeBox.setFont(inputFont);
		addGradeHeader = new JLabel("Grade:");
		addGradeHeader.setFont(labelFont);
		//Text field for inputing name of course
		nameField = new JTextField(15);
		nameField.setFont(inputFont);
		addCourseTitleHeader = new JLabel("Course name:");
		addCourseTitleHeader.setFont(labelFont);
		courseInfoPanel.add(addCreditsHeader);
		courseInfoPanel.add(creditField);
		courseInfoPanel.add(addGradeHeader);
		courseInfoPanel.add(gradeBox);
		courseInfoPanel.add(addCourseTitleHeader);
		courseInfoPanel.add(nameField);
		pane.add(courseInfoPanel, BorderLayout.CENTER); //adds courseInfoPanel to center of screen

		
		noGradeCourses = new ArrayList<Course>();
		Font buttonFont = new Font("buttonFont", Font.PLAIN, 25);
		
		//Add course button
		addNewCourse = new JButton("Add course");
		addNewCourse.setFont(buttonFont);
		addNewCourse.setActionCommand("click");
		addNewCourse.addActionListener(new AddCourseListener());
		//Remove course button
		removeCourse = new JButton("Remove selected course");
		removeCourse.setFont(buttonFont);
		removeCourse.setActionCommand("click");
		removeCourse.addActionListener(new RemoveCourseListener());
		//Clear courses button
		clearCourses = new JButton("Clear all courses");
		clearCourses.setFont(buttonFont);
		clearCourses.setActionCommand("click");
		clearCourses.addActionListener(new ClearCoursesListener());
		//Target GPA input field and its header
		targetGPAField = new JTextField(3);
		targetGPAField.setFont(inputFont);
		targetGPA = new JLabel("Desired GPA: ");
		targetGPA.setFont(labelFont);
		//Target GPA button
		calculateRequiredGPA = new JButton("Calculate required GPA");
		calculateRequiredGPA.setFont(buttonFont);
		calculateRequiredGPA.setActionCommand("click");
		calculateRequiredGPA.addActionListener(new CalculateRequiredGPAListener());
		//Required GPA label
		requiredGPA = new JLabel("TBD");
		requiredGPA.setFont(labelFont);
		//panel that the buttons are added to
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(10, 2));
		buttonsPanel.add(addNewCourse);
		buttonsPanel.add(removeCourse);
		buttonsPanel.add(clearCourses);
		JLabel bufferLabel = new JLabel(""); //for allowing targetGPA and targetGPAField to line up horizontally
		buttonsPanel.add(bufferLabel);
		buttonsPanel.add(targetGPA);
		buttonsPanel.add(targetGPAField);
		buttonsPanel.add(calculateRequiredGPA);
		buttonsPanel.add(requiredGPA);
		requiredGPA.setVisible(false);
		pane.add(buttonsPanel, BorderLayout.EAST); //adds buttonsPanel to right side of screen

		Font instructionsFont = new Font("instructionsFont", Font.PLAIN, 25);
		JPanel messagePanel = new JPanel();
		BoxLayout messageLayout = new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS);
		messagePanel.setLayout(messageLayout);
		instructionsLabel1 = new JLabel("Enter the required credits, grade, and name for a course. Note that credit number is required but grade and course name are optional.");
		instructionsLabel1.setFont(instructionsFont);
		instructionsLabel2 = new JLabel("To calculate what GPA is required for current/anticipated courses to get a desired GPA, enter your target GPA and hit 'Calculate required GPA.");
		instructionsLabel2.setFont(instructionsFont);
		suggestionLabel = new JLabel();
		suggestionLabel.setFont(new Font("suggestionLabel", Font.BOLD, 50));
		suggestionLabel.setVisible(false);
		messagePanel.add(suggestionLabel);
		messagePanel.add(instructionsLabel1);
		messagePanel.add(instructionsLabel2);
		pane.add(messagePanel, BorderLayout.SOUTH);
		
	}
	
	/** calculates current gpa for all courses with a grade **/
	public static double calculateCurrentGPA(ArrayList<Course> gradedCourses) {
		double sumGPA = 0.0;
		int totalCredits = 0;
		if(gradedCourses.isEmpty()) {
			return 0.0;
		}
		for(Course ele : gradedCourses) {
			//GPA = (grades * credits) / credits
			totalCredits += ele.getCredits();
			sumGPA += (ele.convertToGPA(ele.getLetterGrade()) * ele.getCredits());
		}
		return sumGPA / totalCredits;
	}
	
	/** create and set up the window **/
	public static void createAndShowGUI() {
		GpaPlanner frame = new GpaPlanner();
		frame.setTitle("GPA Calculator/Course Planner");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
