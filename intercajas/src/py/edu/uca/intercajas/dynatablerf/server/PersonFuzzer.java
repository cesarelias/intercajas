/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package py.edu.uca.intercajas.dynatablerf.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import py.edu.uca.intercajas.dynatablerf.domain.Person;
import py.edu.uca.intercajas.dynatablerf.domain.Schedule;

/**
 * Utility class for creating random people.
 */
class PersonFuzzer {

  public static final int MAX_PEOPLE = 50;

  private static final String[] FIRST_NAMES = new String[] {
      "Cesar", "Graciela", "Lia", "Elias", "Irene", "Narciso", "Meli", "Amelie",
      "Sebastian", "Abigail"};

  private static final String[] LAST_NAMES = new String[] {
      "Sanabria", "Ramirez", "Perez", "Ayala", "Balanovsky", "Lugo"};

  private static final String[] SUBJECTS = new String[] {
      "Antiguedad", "Liquidacion", "Finiquito"};

  private static final int STUDENTS_PER_PROF = 5;

  public static List<Schedule> collectSchedules(List<Person> randomPeople) {
    List<Schedule> toReturn = new ArrayList<Schedule>();
    for (Person person : randomPeople) {
      toReturn.add(person.getClassSchedule());
    }
    return toReturn;
  }

  public static Person generatePerson() {
    Random rnd = new Random();
    Person toReturn = generateRandomPerson(rnd);
    AddressFuzzer.fuzz(rnd, toReturn.getAddress());
    return toReturn;
  }

  public static List<Person> generateRandomPeople() {
    List<Person> toReturn = new ArrayList<Person>(MAX_PEOPLE);
    Random rnd = new Random(3);
    for (long i = 0; i < MAX_PEOPLE; i++) {
      Person person = generateRandomPerson(rnd);
      AddressFuzzer.fuzz(rnd, person.getAddress());
      toReturn.add(person);
    }
    return toReturn;
  }

  private static Person generateRandomPerson(Random rnd) {
    if (isChosenAsProfessor(rnd)) {
      return generateRandomProfessor(rnd);
    } else {
      return generateRandomStudent(rnd);
    }
  }

  private static Person generateRandomProfessor(Random rnd) {
    Person prof = new Person();

    String firstName = pickRandomString(rnd, FIRST_NAMES);
    String lastName = pickRandomString(rnd, LAST_NAMES);
    prof.setName("Dr. " + firstName + " " + lastName);

    String subject = pickRandomString(rnd, SUBJECTS);
    prof.setDescription("Professor of " + subject);

    prof.setClassSchedule(ScheduleFuzzer.generateRandomSchedule(rnd));

    return prof;
  }

  private static Person generateRandomStudent(Random rnd) {
    Person student = new Person();

    String firstName = pickRandomString(rnd, FIRST_NAMES);
    String lastName = pickRandomString(rnd, LAST_NAMES);
    student.setName(firstName + " " + lastName);

    String subject = pickRandomString(rnd, SUBJECTS);
    student.setDescription("En proceso de " + subject);

    student.setClassSchedule(ScheduleFuzzer.generateRandomSchedule(rnd));

    return student;
  }

  private static boolean isChosenAsProfessor(Random rnd) {
    return rnd.nextInt(STUDENTS_PER_PROF + 1) == 0;
  }

  private static String pickRandomString(Random rnd, String[] a) {
    int i = rnd.nextInt(a.length);
    return a[i];
  }

}
