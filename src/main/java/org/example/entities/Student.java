package org.example.entities;

import lombok.*;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
public class Student implements Comparable<Student> {

    private String name;
    private List<Integer> grades = new ArrayList<>();

    @ToString.Exclude
    private List<StateChange> stateChanges = new ArrayList<>();

    public Student(String name, int... grades) {
        this.name = name;
        addGrades(grades);
    }

    public Student(Student student) {
        this.name = student.getName();
        this.grades = new ArrayList<>(student.getGrades());
    }

    public void removeGrade(int index) {
        Integer a = grades.remove(index);
        stateChanges.add(StateChange.ofGrade(false, a, index));
    }

    public void addGrades(int... grades) {
        StateChange stateChange = StateChange.empty();
        for (int gr : grades) {
            if (gr < 2 || gr > 5) throw new IllegalArgumentException("Grade is not in range! " + gr);
            this.grades.add(gr);

            stateChange.add(AtomicGradeChange.added());
        }

        stateChanges.add(stateChange);
    }

    public void setName(String name) {
        stateChanges.add(StateChange.ofName(this.name));
        this.name = name;
    }

    public void undo() {
        if (stateChanges.isEmpty()) return;
        StateChange stateChange = stateChanges.removeLast();
        stateChange.undo(this);
    }

    public Save save() {
        return new Student.Save(stateChanges.isEmpty() ? null : stateChanges.getLast());
    }

    public double average() {
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public boolean isCool() {
        if (grades.isEmpty()) return false;
        return grades.stream().allMatch(i -> i == 5);
    }

    @Override
    public int compareTo(Student o) {
        return Double.compare(this.average(), o.average());
    }


    @RequiredArgsConstructor
    public class Save {
        private final StateChange targetStateChange;

        public void restore() {
            if (targetStateChange == null) {
                while (!Student.this.stateChanges.isEmpty()) Student.this.undo();
            } else {
                while (Student.this.stateChanges.getLast() != targetStateChange) Student.this.undo();
            }
        }

        @Contract(pure = true, value = " -> new")
        public Student fromSave(){
            Student student = new Student(Student.this);
            student.stateChanges=new ArrayList<>(Student.this.stateChanges);
            if (targetStateChange == null) {
                while (!student.stateChanges.isEmpty()) student.undo();
            } else {
                while (student.stateChanges.getLast() != targetStateChange) student.undo();
            }
            return student;
        }
    }

    // Combination of multiple atomic changes
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class StateChange {
        private final List<AtomicStateChange> atomicChanges = new ArrayList<>();

        public void undo(Student student) {
            atomicChanges.forEach(asc -> asc.undo(student));
        }

        public void add(AtomicStateChange change){
            this.atomicChanges.add(change);
        }

        // Using lambda
        public static StateChange ofName(String oldName) {
            StateChange stateChange = new StateChange();
            stateChange.atomicChanges.add(student -> student.name = oldName);
            return stateChange;
        }

        // Using concrete class
        public static StateChange ofGrade(boolean add, int grade, int index) {
            StateChange stateChange = new StateChange();
            if (!add) stateChange.atomicChanges.add(AtomicGradeChange.removed(grade, index));
            else stateChange.atomicChanges.add(AtomicGradeChange.added());
            return stateChange;
        }

        public static StateChange empty() {
            return new StateChange();
        }

    }

    interface AtomicStateChange {
        void undo(Student student);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    static class AtomicGradeChange implements AtomicStateChange {
        private final boolean gradeAdded; // if false - grade was removed
        private final int grade;
        private final int index;

        @Override
        public void undo(Student student) {
            if (gradeAdded) student.grades.removeLast();
            else student.grades.add(index, grade);
        }

        public static AtomicGradeChange removed(int grade, int index) {
            return new AtomicGradeChange(false, grade, index);
        }

        public static AtomicGradeChange added() {
            return new AtomicGradeChange(true, 0, 0);
        }
    }
}
