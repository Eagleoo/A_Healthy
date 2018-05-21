package com.example.administrator.steps_count.Main_Activity;

public class TextResult {
    private String t_type;
    private String answer;
    private String result;

    public String getT_type() {
        return t_type;
    }

    public void setT_type(String t_type) {
        this.t_type = t_type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Text[t_type="+t_type+",answer="+answer+",result="+result+"]";
    }
}
