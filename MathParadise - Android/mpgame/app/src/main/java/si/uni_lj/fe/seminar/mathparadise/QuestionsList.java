package si.uni_lj.fe.seminar.mathparadise;

import java.lang.reflect.Constructor;

public class QuestionsList {
    private String question, option1, option2, option3, option4, answer;
    private String userSelectedAnswer;
    private int imagePath;

    // *Konstruktor razreda* , ki inicializira vse atribute z vrednostmi
    public QuestionsList(String question, String option1, String option2, String option3, String option4, String answer, String userSelectedAnswer, int imagePath) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.userSelectedAnswer = userSelectedAnswer;
        this.imagePath = imagePath;
    }

    // Getter metode za dostop do vrednosti posameznih atributov
    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    public String getUserSelectedAnswer() {
        return userSelectedAnswer;
    }

    // Getter metoda za dostop do vrednosti atributa imagePath
    public int getImagePath() {
        return imagePath;
    }

    // Setter metoda za nastavljanje vrednosti atributa userSelectedAnswer
    public void setUserSelectedAnswer(String userSelectedAnswer) {
        this.userSelectedAnswer = userSelectedAnswer;
    }
}
