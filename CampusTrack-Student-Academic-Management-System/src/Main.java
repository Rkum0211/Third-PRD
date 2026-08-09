import java.util.Scanner;

/**
 * Console-based student academic and fee clearance tracker.
 */
public class Main {
    private static final int SUBJECT_PASS_MARK = 40;
    private static final double OVERALL_PASS_PERCENTAGE = 50.0;
    private static final double MIN_ATTENDANCE = 75.0;
    private static final double MIN_ASSIGNMENT_AVERAGE = 50.0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int continueChoice;

        System.out.println("========================================");
        System.out.println("          CAMPUS TRACK SYSTEM");
        System.out.println("========================================");

        do {
            System.out.println("\n--- Student Registration ---");
            System.out.print("Student ID: ");
            String studentId = scanner.nextLine().trim();
            while (studentId.isEmpty()) {
                System.out.print("Student ID cannot be empty. Enter again: ");
                studentId = scanner.nextLine().trim();
            }

            System.out.print("Full name: ");
            String fullName = scanner.nextLine().trim();
            while (fullName.isEmpty()) {
                System.out.print("Name cannot be empty. Enter again: ");
                fullName = scanner.nextLine().trim();
            }

            int age = readInt(scanner, "Age (15-35): ");
            while (age < 15 || age > 35) {
                System.out.println("Error: age must be between 15 and 35.");
                age = readInt(scanner, "Age (15-35): ");
            }

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            while (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
                System.out.print("Enter a valid email address: ");
                email = scanner.nextLine().trim();
            }

            int courseChoice;
            String courseName = "";
            double baseSemesterFee = 0;
            do {
                System.out.println("\nCourse Menu");
                System.out.println("1. BCA                 - Rs. 45,000");
                System.out.println("2. BBA                 - Rs. 40,000");
                System.out.println("3. B.Tech              - Rs. 65,000");
                System.out.println("4. BA English          - Rs. 30,000");
                courseChoice = readInt(scanner, "Choose a course (1-4): ");

                switch (courseChoice) {
                    case 1:
                        courseName = "Bachelor of Computer Applications (BCA)";
                        baseSemesterFee = 45000;
                        break;
                    case 2:
                        courseName = "Bachelor of Business Administration (BBA)";
                        baseSemesterFee = 40000;
                        break;
                    case 3:
                        courseName = "Bachelor of Technology (B.Tech)";
                        baseSemesterFee = 65000;
                        break;
                    case 4:
                        courseName = "Bachelor of Arts in English (BA English)";
                        baseSemesterFee = 30000;
                        break;
                    default:
                        System.out.println("Invalid course choice. Please select 1 to 4.");
                }
            } while (courseChoice < 1 || courseChoice > 4);

            int semester = readInt(scanner, "Semester (1-8): ");
            while (semester < 1 || semester > 8) {
                System.out.println("Semester must be from 1 to 8.");
                semester = readInt(scanner, "Semester (1-8): ");
            }

            // readInt consumes the pending newline, so nextLine can read the full goal.
            System.out.print("Complete career goal: ");
            String careerGoal = scanner.nextLine().trim();
            while (careerGoal.isEmpty()) {
                System.out.print("Career goal cannot be empty. Enter again: ");
                careerGoal = scanner.nextLine().trim();
            }

            System.out.println("\nEnter subject marks (0-100)");
            int subject1 = readMark(scanner, "Subject 1 mark: ");
            int subject2 = readMark(scanner, "Subject 2 mark: ");
            int subject3 = readMark(scanner, "Subject 3 mark: ");
            int subject4 = readMark(scanner, "Subject 4 mark: ");
            int subject5 = readMark(scanner, "Subject 5 mark: ");
            int totalMarks = subject1 + subject2 + subject3 + subject4 + subject5;
            double percentage = ((double) totalMarks / 500) * 100;

            boolean allSubjectsPassed = subject1 >= SUBJECT_PASS_MARK
                    && subject2 >= SUBJECT_PASS_MARK && subject3 >= SUBJECT_PASS_MARK
                    && subject4 >= SUBJECT_PASS_MARK && subject5 >= SUBJECT_PASS_MARK;
            boolean academicCriteriaMet = allSubjectsPassed && percentage >= OVERALL_PASS_PERCENTAGE;
            String grade = determineGrade(percentage, allSubjectsPassed);

            int totalClasses = readInt(scanner, "\nTotal classes conducted (1-200): ");
            while (totalClasses < 1 || totalClasses > 200) {
                System.out.println("Total classes must be between 1 and 200.");
                totalClasses = readInt(scanner, "Total classes conducted (1-200): ");
            }
            int attendedClasses = readInt(scanner, "Classes attended (0-" + totalClasses + "): ");
            while (attendedClasses < 0 || attendedClasses > totalClasses) {
                System.out.println("Attended classes must be from 0 to " + totalClasses + ".");
                attendedClasses = readInt(scanner, "Classes attended (0-" + totalClasses + "): ");
            }
            double attendancePercentage = ((double) attendedClasses / totalClasses) * 100;
            String attendanceStatus = attendancePercentage >= MIN_ATTENDANCE ? "ELIGIBLE" : "SHORTAGE";
            boolean attendanceCriteriaMet = attendancePercentage >= MIN_ATTENDANCE;

            int requestedAssignments = readInt(scanner, "\nNumber of assignment entries (1-20): ");
            while (requestedAssignments < 1 || requestedAssignments > 20) {
                System.out.println("Assignment entries must be between 1 and 20.");
                requestedAssignments = readInt(scanner, "Number of assignment entries (1-20): ");
            }
            int assignmentTotal = 0;
            int validAssignmentCount = 0;
            for (int entry = 1; entry <= requestedAssignments; entry++) {
                int score = readInt(scanner, "Assignment " + entry + " score (0-100, -1 to finish): ");
                if (score == -1) {
                    System.out.println("Assignment entry finished early.");
                    break;
                }
                if (score < 0 || score > 100) {
                    System.out.println("Invalid score skipped.");
                    continue;
                }
                assignmentTotal += score;
                validAssignmentCount++;
            }
            double assignmentAverage = validAssignmentCount > 0
                    ? (double) assignmentTotal / validAssignmentCount : 0;
            boolean assignmentCriteriaMet = validAssignmentCount > 0
                    && assignmentAverage >= MIN_ASSIGNMENT_AVERAGE;
            String assignmentStatus = assignmentCriteriaMet ? "SATISFACTORY" : "NOT SATISFACTORY";

            double scholarshipPercentage = determineScholarship(percentage, attendancePercentage,
                    assignmentCriteriaMet, academicCriteriaMet);
            double scholarshipAmount = baseSemesterFee * scholarshipPercentage / 100;
            double finalPayableFee = baseSemesterFee - scholarshipAmount;

            double amountPaid = readDouble(scanner, "\nAmount paid (Rs. 0 to " + money(finalPayableFee) + "): ");
            while (amountPaid < 0 || amountPaid > finalPayableFee) {
                System.out.println("Amount paid must be from Rs. 0 to Rs. " + money(finalPayableFee) + ".");
                amountPaid = readDouble(scanner, "Amount paid: ");
            }
            double feeBalance = finalPayableFee - amountPaid;
            String feeStatus = feeBalance <= 0 ? "CLEARED" : "PENDING";
            boolean feeCriteriaMet = feeBalance <= 0;
            boolean finalClearance = academicCriteriaMet && attendanceCriteriaMet
                    && assignmentCriteriaMet && feeCriteriaMet;
            String clearanceStatus = finalClearance ? "CLEARED" : "NOT CLEARED";

            printReport(studentId, fullName, age, email, courseName, semester, careerGoal,
                    subject1, subject2, subject3, subject4, subject5, totalMarks, percentage,
                    grade, attendancePercentage, attendanceStatus, assignmentAverage,
                    assignmentStatus, baseSemesterFee, scholarshipPercentage, scholarshipAmount,
                    finalPayableFee, amountPaid, feeBalance, feeStatus, allSubjectsPassed,
                    academicCriteriaMet, attendanceCriteriaMet, assignmentCriteriaMet,
                    feeCriteriaMet, clearanceStatus);

            continueChoice = readInt(scanner, "\nProcess another student? (1 = Yes, 0 = No): ");
            while (continueChoice != 1 && continueChoice != 0) {
                System.out.println("Please enter only 1 or 0.");
                continueChoice = readInt(scanner, "Process another student? (1 = Yes, 0 = No): ");
            }
        } while (continueChoice == 1);

        System.out.println("\nThank you for using CampusTrack. Goodbye!");
        scanner.close();
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }
            System.out.println("Please enter a whole number.");
            scanner.nextLine();
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            }
            System.out.println("Please enter a valid amount.");
            scanner.nextLine();
        }
    }

    private static int readMark(Scanner scanner, String prompt) {
        int mark = readInt(scanner, prompt);
        while (mark < 0 || mark > 100) {
            System.out.println("Mark must be between 0 and 100.");
            mark = readInt(scanner, prompt);
        }
        return mark;
    }

    private static String determineGrade(double percentage, boolean allSubjectsPassed) {
        if (!allSubjectsPassed) return "F";
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        return "F";
    }

    private static double determineScholarship(double percentage, double attendance,
                                                boolean assignmentsMet, boolean academicsMet) {
        if (!academicsMet || !assignmentsMet || attendance < MIN_ATTENDANCE) return 0;
        if (percentage >= 90) return 25;
        if (percentage >= 80) return 15;
        if (percentage >= 70) return 10;
        return 0;
    }

    private static String money(double amount) {
        return String.format("%.2f", amount);
    }

    private static void printReport(String studentId, String fullName, int age, String email,
                                    String courseName, int semester, String careerGoal,
                                    int subject1, int subject2, int subject3, int subject4,
                                    int subject5, int totalMarks, double percentage, String grade,
                                    double attendancePercentage, String attendanceStatus,
                                    double assignmentAverage, String assignmentStatus,
                                    double baseFee, double scholarshipPercentage,
                                    double scholarshipAmount, double finalPayableFee,
                                    double amountPaid, double feeBalance, String feeStatus,
                                    boolean allSubjectsPassed, boolean academicsMet,
                                    boolean attendanceMet, boolean assignmentsMet,
                                    boolean feeMet, String clearanceStatus) {
        System.out.println("\n========================================");
        System.out.println("         CAMPUS TRACK STUDENT REPORT");
        System.out.println("========================================");
        System.out.println("ID: " + studentId + " | Name: " + fullName + " | Age: " + age);
        System.out.println("Email: " + email);
        System.out.println("Course: " + courseName + " | Semester: " + semester);
        System.out.println("Career goal: " + careerGoal);
        System.out.println("Marks: " + subject1 + ", " + subject2 + ", " + subject3 + ", "
                + subject4 + ", " + subject5);
        System.out.println("Total: " + totalMarks + "/500 | Percentage: " + money(percentage)
                + "% | Grade: " + grade);
        System.out.println("Attendance: " + money(attendancePercentage) + "% (" + attendanceStatus + ")");
        System.out.println("Assignment average: " + money(assignmentAverage) + "% (" + assignmentStatus + ")");
        System.out.println("Base fee: Rs. " + money(baseFee) + " | Scholarship: "
                + money(scholarshipPercentage) + "% (Rs. " + money(scholarshipAmount) + ")");
        System.out.println("Final payable: Rs. " + money(finalPayableFee) + " | Paid: Rs. "
                + money(amountPaid) + " | Balance: Rs. " + money(feeBalance) + " (" + feeStatus + ")");
        System.out.println("FINAL CLEARANCE: " + clearanceStatus);

        if (!allSubjectsPassed) System.out.println("Failed condition: one or more subjects are below 40.");
        if (!academicsMet) System.out.println("Failed condition: overall academic requirement is not met.");
        if (!attendanceMet) System.out.println("Failed condition: attendance is below 75%.");
        if (!assignmentsMet) System.out.println("Failed condition: assignment average is below 50% or no valid score was entered.");
        if (!feeMet) System.out.println("Failed condition: outstanding fee balance remains.");

        if (!attendanceMet) System.out.println("Recommendation: attend more classes to reach 75% attendance.");
        if (!assignmentsMet) System.out.println("Recommendation: complete assignments and target at least 50%.");
        if (!academicsMet) System.out.println("Recommendation: seek academic support and improve marks in weak subjects.");
        if (!feeMet) System.out.println("Recommendation: pay the remaining fee balance to receive clearance.");
        if (academicsMet && attendanceMet && assignmentsMet && feeMet) {
            System.out.println("Recommendation: all clearance conditions have been met. Keep up the good work!");
        }
    }
}

