package com.chen.chenaiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public abstract class ReActAgent extends BaseAgent{

    /**
     * 处理当前状态并决定下一步行动
     * @return
     */
    public abstract boolean think();

    public abstract String act();

    @Override
    public String step() {
        try {
            boolean thinkResult = think();
            if(!thinkResult){
                return "思考结束，无需行动";
            }
            return act();
        } catch (Exception e) {
            e.printStackTrace();
            return "执行错误: " + e.getMessage();
        }
    }
}
