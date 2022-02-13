package processor.homework;

import model.Message;
import processor.Processor;

public class ProcessorReplaceFileds implements Processor {

    @Override
    public Message process(Message message) {
        var fld11 = message.getField11();
        var fld12 = message.getField12();
        return message.toBuilder().field11(fld12).field12(fld11).build();
    }
}
