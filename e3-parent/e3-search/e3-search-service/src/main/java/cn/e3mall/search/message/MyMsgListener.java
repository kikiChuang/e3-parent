package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by xuhongliang on 2017/8/17.
 */
public class MyMsgListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String msg = textMessage.getText();
            System.out.println(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
