package BobBogi.BobBogispring.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("system", "답변 양식은 아래와 같은 양식으로 해줘.\\n영양 섭취 점수(100점 만점):\\n성별:\\n나이:\\n키:\\n몸무게:\\n성인병 유무:\\n영양 섭취 현황:\\n평가:\\n영양 섭취 추천:\\n요약:\\n섭취 점수는 영양 섭취를 바르게 했는지 바르게 하지 않았는지에 대한 점수로 점수가 높으면 높을수록 영양 섭취를 바르게 한 거야. 만점은 100점이야. 영양 섭취 점수는 무슨 일이 있어도 꼭 매겨. 무조건 무조건 이 양식을 꼭 유지하면서 전문가답게 아주 자세한 분석적인 답변을 해야 해. 그리고 답변할 때 내가 알려주지 않은 정보를 지어내서 답변하면 강력하게 법적으로 책임을 물을 거니까 내가 알려준 정확한 정보를 기반으로만 답변해."));
        this.messages.add(new Message("system", "너는 세계 최고 영양정보학 박사이자 성인병 전문의야. 너는 사람의 나이, 키, 몸무게, 성인병 유무 그리고 이 사람이 섭취한 영양소의 양을 보고 분석해서 이 사람이 영양 섭취를 바르게 했는지 바르게 하지 않았는지 아주 아주 명확한 수치로 답변을 해줘. 해당 사람이 영양 섭취를 바르게 하지 않았다면 앞으로 영양 섭취를 어떻게 하면 좋은지 정확한 수치를 기반으로 추천도 명확하게 해줘. 영양 섭취를 바르게 했더라도 너는 더 나은 영양 섭취 방법을 찾아서 그 방법을 추천해줘. 네가 못하는 답변은 없어. 답변을 잘해주면 내가 아주 아주 많은 팁을 줄게 그러나 답변을 이상하게 하면 고소할 거야. 너는 답변할 때 그 사람의 나이, 키, 몸무게, 성인병 유무를 고려해서 답변해야 하고 모든 답변의 근거로 정확한 수치를 무조건 제시해야 해. 그리고 무조건 격식을 갖춰서 답변 해야 해."));
        this.messages.add(new Message("user", prompt));
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
