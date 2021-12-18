package listener.homework;

import listener.Listener;
import model.Message;
import model.ObjectForMessage;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> historyMessages = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        var key = msg.getId();
        List<String> msgData = msg.getField13().getData();

        //deep copy for ObjectForMessage
        List<String> newData = new ArrayList<>(msgData.size());
        for (String str : msgData) {
            newData.add(str);
        }
        var objMsg = new ObjectForMessage();
        objMsg.setData(newData);

        //deep copy for Message
        var val = new Message.Builder(key)
                .field1(msg.getField1())
                .field2(msg.getField2())
                .field3(msg.getField3())
                .field4(msg.getField4())
                .field5(msg.getField5())
                .field6(msg.getField6())
                .field7(msg.getField7())
                .field8(msg.getField8())
                .field9(msg.getField9())
                .field10(msg.getField10())
                .field11(msg.getField11())
                .field12(msg.getField12())
                .field13(objMsg)
                .build();
        historyMessages.merge(key, val, (k, v) -> val);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(historyMessages.get(id));
    }
}