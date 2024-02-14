package si.uni_lj.fe.seminar.mathparadise;

import java.util.ArrayList;
import java.util.List;

public class QuestionsBank {
    // Metoda za pridobivanje vprašanj iz različnih kategorij:
    private static List<QuestionsList> javaQuestions(){

        final List<QuestionsList> questionsLists = new ArrayList<>();

        //Ustvari prazen seznam za shranjevanje vprašanj
        final QuestionsList question1 = new QuestionsList("Ugotovi kateri lik je na sliki!","Kvadrat","Trikotnik","Krog","Pravokotnik","Kvadrat","",R.drawable.tezavnost1naloga2);
        final QuestionsList question2 = new QuestionsList("Ugotovi kateri lik je na sliki!","Krog","Trikotnik","Pravokotnik","Kvadrat","Trikotnik","",R.drawable.tezavnost1naloga1);
        final QuestionsList question3 = new QuestionsList("Ugotovi kateri lik je na sliki!","Trikotnik","Kvadrat","Pravokotnik","Krog","Krog","",R.drawable.tezavnost1naloga3);
        final QuestionsList question4 = new QuestionsList("Ugotovi kateri lik je na sliki!","Kvadrat","Krog","Pravokotnik","Trikotnik","Pravokotnik","",R.drawable.tezavnost1naloga4);


        //Dodaj vsa vprašanja v seznam QuestionsList
        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);

        // Vrni seznam vprašanj
        return questionsLists;
    }

    private static List<QuestionsList> phpQuestions(){

        final List<QuestionsList>questionsLists = new ArrayList<>();

        final QuestionsList question1 = new QuestionsList("Ugotovi katero zaporedje sledi!","5 6 7","1 2 3","2 1 3","7 8 9","5 6 7","",R.drawable.tez1nal1);
        final QuestionsList question2 = new QuestionsList("Ugotovi katero zaporedje sledi!","5 6 7","1 1 2","7 8 9","8 9 10","7 8 9","",R.drawable.tez1nal2);
        final QuestionsList question3 = new QuestionsList("Ugotovi katero zaporedje sledi!"," 3 5 6","7 8 9","8 7 6","11 12 13","11 12 13","",R.drawable.tez1nal3);
        final QuestionsList question4 = new QuestionsList("Ugotovi katero zaporedje sledi!","1 4 2","22 21 23","24 25 26","49 50 66","24 25 26","",R.drawable.tez1nal4);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);

        return questionsLists;
    }

    private static List<QuestionsList> htmlQuestions(){

        final List<QuestionsList>questionsLists = new ArrayList<>();

        final QuestionsList question1 = new QuestionsList("Preizkusi se v osnovnem računanju!","2","4","15","3","2","",R.drawable.tez3nal1);
        final QuestionsList question2 = new QuestionsList("Preizkusi se v osnovnem računanju!","4","1","6","2","6","",R.drawable.tez3nal2);
        final QuestionsList question3 = new QuestionsList("Preizkusi se v osnovnem računanju!","14","4","2","15","15","",R.drawable.tez3nal3);
        final QuestionsList question4 = new QuestionsList("Preizkusi se v osnovnem računanju!","39","40","41","38","40","",R.drawable.tez3nal4);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);

        return questionsLists;
    }

    private static List<QuestionsList> androidQuestions(){

        final List<QuestionsList>questionsLists = new ArrayList<>();

        final QuestionsList question1 = new QuestionsList("Preizkusi se v naprednem računanju!","6","9","11","8","8","",R.drawable.tez4nal1);
        final QuestionsList question2 = new QuestionsList("Preizkusi se v naprednem računanju!","10","9","6","2","9","",R.drawable.tez4nal2);
        final QuestionsList question3 = new QuestionsList("Preizkusi se v naprednem računanju!","1","10","17","24","24","",R.drawable.tez4nal3);
        final QuestionsList question4 = new QuestionsList("Preizkusi se v naprednem računanju!","56","0","54","49","56","",R.drawable.tez4nal4);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);

        return questionsLists;
    }

    // Metoda za pridobivanje vprašanj glede na izbrano temo
    public static List<QuestionsList> getQuestions(String selectedTopicName){
        switch (selectedTopicName){
            case "Liki":
                return javaQuestions();

            case "Zaporedje":
                return phpQuestions();

            case "Napredno":
                return androidQuestions();
            default:
                return htmlQuestions();
        }
    }
}
