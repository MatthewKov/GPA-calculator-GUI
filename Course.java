//Matthew Kovalenko msk9pe
//Homework 5

public class Course {
	
	private int credits; //number of credits the course is worth
	private String letterGrade; //Optional course letter grade with assumption that grade will be inputed as a letter grade
	private String name; //Course name

	public Course(int credits, String grade, String name) {
		this.credits = credits;
		this.letterGrade = grade;
		this.name = name;
	}
	
	public Course(int credits) {
		this.credits = credits;
		this.letterGrade= "";
		this.name= "";
	}
	
	/** Converts user inputed letter grade into equivalent grade point value **/
	public double convertToGPA(String letterGrade) {
		if(letterGrade.equals("A+") || letterGrade.equals("A")) {
			return 4.0;
		}
		else if(letterGrade.equals("A-")) {
			return 3.7;
		}
		else if(letterGrade.equals("B+")) {
			return 3.3;
		}
		else if(letterGrade.equals("B")) {
			return 3.0;
		}
		else if(letterGrade.equals("B-")) {
			return 2.7;
		}
		else if(letterGrade.equals("C+")) {
			return 2.3;
		}
		else if(letterGrade.equals("C")) {
			return 2.0;
		}
		else if(letterGrade.equals("C-")) {
			return 1.7;
		}
		else if(letterGrade.equals("D+")) {
			return 1.3;
		}
		else if(letterGrade.equals("D")) {
			return 1.0;
		}
		else if(letterGrade.equals("D-")) {
			return 0.7;
		}
		else {
			return 0.0;
		}
	}
	
	/** Method of displaying the info for a course on the GUI **/
	public String toString() {
		return this.name + " " + this.credits + " credits " + this.getLetterGrade();
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getLetterGrade() {
		return letterGrade;
	}

	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
