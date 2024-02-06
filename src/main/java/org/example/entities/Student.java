package org.example.entities;

import lombok.*;

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

    public Student(String name, int... grades){
        this.name = name;
        addGrades(grades);
    }

    public Student(Student student){
        this.name = student.getName();
        this.grades = new ArrayList<>(student.getGrades());
    }

    public void removeGrade(int index){
        List<StateChange.GradeChange> gradeChanges = new ArrayList<>();
        Integer a = grades.remove(index);
        gradeChanges.add(new StateChange.GradeChange(false, a, index));

        stateChanges.add(new StateChange(gradeChanges, null));
    }

    public void addGrades(int... grades){
        List<StateChange.GradeChange> gradeChanges = new ArrayList<>();
        for(int gr : grades){
            if(gr<2 || gr > 5) throw new IllegalArgumentException("Grade is not in range! "+gr);
            this.grades.add(gr);
            gradeChanges.add(new StateChange.GradeChange(true, 0, 0));
        }

        stateChanges.add(new StateChange(gradeChanges, null));
    }

    public void setName(String name){
        stateChanges.add(new StateChange(null, this.name));
        this.name = name;
    }

    public void undo(){
        if(stateChanges.isEmpty()) return;
        StateChange stateChange = stateChanges.removeLast();
        stateChange.undo(this);
    }

    public Save save(){
        return new Save(stateChanges.isEmpty() ? null : stateChanges.getLast(), this);
    }

    public double average(){
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public boolean isCool(){
        if(grades.isEmpty()) return false;
        return grades.stream().allMatch(i -> i==5);
    }

    @Override
    public int compareTo(Student o) {
        return Double.compare(this.average(), o.average());
    }


    @RequiredArgsConstructor
    public static class Save{
        private final StateChange targetStateChange;
        private final Student student;

        public void undo(){
            if(targetStateChange == null){
                while (!student.stateChanges.isEmpty()) student.undo();
            } else{
                while (student.stateChanges.getLast() != targetStateChange) student.undo();
            }
        }
    }

    @RequiredArgsConstructor
    @ToString
    static class StateChange{
        record GradeChange(boolean added, int data, int index){}

        private final List<GradeChange> gradeChanges;
        private final String oldName;

        public void undo(Student student){
            if(oldName != null) student.name = oldName;
            if(gradeChanges != null){
                for(GradeChange change : gradeChanges){
                    if(change.added) student.getGrades().removeLast();
                    else {
                        student.getGrades().add(change.data, change.index);
                    }
                }
            }
        }
    }
}
