package com.example.vale.fitnessapp.personal_record;

/*
PersonalRecord is a simple data structure that contains the four attributes that define a personal
record.
 */

public class PersonalRecord{

    private String discipline;
    private double quantity;
    private int unit; //0 = kg, 1 = reps
    private String comment;

    public PersonalRecord(String discipline, double quantity, int unit, String comment){
        this.discipline = discipline;
        this.quantity = quantity;
        this.unit = unit;
        this.comment = comment;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
