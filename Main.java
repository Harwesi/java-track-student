import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Консольная система управления оценками и задачами студента.
 * Меню: 1 - добавить задачу, 2 - список задач, 3 - средний балл, 4 - выход.
 */
public class Main {

    // Один общий Scanner для чтения ввода пользователя
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Система управления оценками и задачами ===");
        System.out.print("Введите имя студента: ");
        String name = readNonEmptyLine();

        Student student = new Student(name);
        boolean running = true;

        // Главный цикл программы: показываем меню, пока пользователь не выберет выход
        while (running) {
            printMenu(student);
            int choice = readInt("Ваш выбор: ", 1, 4);

            switch (choice) {
                case 1 -> addTask(student);
                case 2 -> showTasks(student);
                case 3 -> showAverage(student);
                case 4 -> {
                    System.out.println("До свидания, " + student.getName() + "!");
                    running = false;
                }
                default -> System.out.println("Неизвестный пункт меню.");
            }
        }
        scanner.close();
    }

    /** Выводит текстовое меню. */
    private static void printMenu(Student student) {
        System.out.println();
        System.out.println("--- Меню (студент: " + student.getName() + ") ---");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Посмотреть список задач");
        System.out.println("3 - Посчитать средний балл");
        System.out.println("4 - Выход");
    }

    /** Пункт 1: запрашивает данные и добавляет новую задачу студенту. */
    private static void addTask(Student student) {
        System.out.print("Название задачи: ");
        String title = readNonEmptyLine();

        System.out.print("Предмет: ");
        String subject = readNonEmptyLine();

        // Оценка по 5-балльной шкале; 0 означает, что задача ещё не оценена
        int grade = readInt("Оценка (1-5, или 0 если оценки пока нет): ", 0, 5);

        student.addTask(new Task(title, subject, grade));
        System.out.println("Задача добавлена!");
    }

    /** Пункт 2: выводит список всех задач студента. */
    private static void showTasks(Student student) {
        List<Task> tasks = student.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        System.out.println("Список задач:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /** Пункт 3: считает и выводит средний балл по оценённым задачам. */
    private static void showAverage(Student student) {
        double average = student.calculateAverage();
        if (average < 0) {
            System.out.println("Нет оценённых задач — средний балл посчитать нельзя.");
        } else {
            System.out.printf("Средний балл студента %s: %.2f%n", student.getName(), average);
        }
    }

    /** Читает непустую строку; повторяет запрос, если пользователь ввёл пустую. */
    private static String readNonEmptyLine() {
        while (true) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.print("Значение не может быть пустым. Повторите ввод: ");
        }
    }

    /**
     * Читает целое число в диапазоне [min, max].
     * Повторяет запрос, пока ввод не станет корректным.
     */
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Введите число от " + min + " до " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Это не число. Попробуйте снова.");
            }
        }
    }
}

/** Задача студента: название, предмет и оценка. */
class Task {
    private final String title;   // название задачи
    private final String subject; // учебный предмет
    private final int grade;      // оценка (0 — ещё не оценена)

    public Task(String title, String subject, int grade) {
        this.title = title;
        this.subject = subject;
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    /** Проверяет, выставлена ли оценка за задачу. */
    public boolean isGraded() {
        return grade > 0;
    }

    @Override
    public String toString() {
        String gradeText = isGraded() ? String.valueOf(grade) : "нет оценки";
        return title + " [" + subject + "] — оценка: " + gradeText;
    }
}

/** Студент: имя и список его задач. */
class Student {
    private final String name;
    private final List<Task> tasks = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Считает средний балл по оценённым задачам.
     * Возвращает -1, если оценённых задач нет.
     */
    public double calculateAverage() {
        int sum = 0;
        int count = 0;
        for (Task task : tasks) {
            if (task.isGraded()) {
                sum += task.getGrade();
                count++;
            }
        }
        return count == 0 ? -1 : (double) sum / count;
    }
}
